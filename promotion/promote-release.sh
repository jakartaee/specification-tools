#!/bin/bash -ex

##[ Imports ]####################

source "$(dirname $0)/utils.sh"

##[ Required Environment ]#######

require KEYRING
require PASSPHRASE
require SPEC_NAME "[a-z][a-z-]*[a-z]"
require SPEC_VERSION "[1-9][0-9.]*"
require TCK_BINARY_URL "https?://download.eclipse.org/.*\.(zip|tar.gz)"

##[ Main ]#######################

( # Load the keyring into gpg so we can sign
    
    gpg --batch --import "${KEYRING}"
    # Iterate over each of the imported private keys and mark them trusted
    for key in $(gpg --list-keys --with-colons | tr ':' '\t' | grep '^fpr' | cut -f 10); do
	# The echo statement handles providing two values to an interactive prompt
	echo -e "5\ny\n" |  gpg --batch --command-fd 0 --expert --edit-key "$key" trust;
    done
)

( # Create a tmp dir and download the TCK
    TMP="/tmp/download-$$" && mkdir "$TMP" && cd "$TMP"

    # Local file name of the TCK zip or tar.gz
    TCK="$(basename "$TCK_BINARY_URL")"

    # If the tck starts with "eclipse-" rename it "jakarta-"
    TCK="${TCK/eclipse-/jakarta-}"

    # Download the TCK or fail
    curl "$TCK_BINARY_URL" > "$TCK" || fail "Could not download $TCK_BINARY_URL"

    # Sign the TCK or fail
    echo "${PASSPHRASE}" | gpg --sign --armor --batch --passphrase-fd 0 --output "$TCK".sig --detach-sig "$TCK" || fail "Signature failed"

    # Verify the signature to be safe
    gpg --verify "$TCK".sig "$TCK" || fail "Signature Verification failed"

    # Calculate the sha256 for convenience
    shasum -a 256 "$TCK" | tr ' ' '\t' | cut -f 1 > "$TCK.sha256"

    ZONE="/home/data/httpd/download.eclipse.org/jakartaee/"
    DROP="/home/data/httpd/download.eclipse.org/jakartaee/${SPEC_NAME}/${SPEC_VERSION}/"
    HOST='genie.jakartaee-spec-committee@projects-storage.eclipse.org'

    ( # Test SSH access and write access
	
	# do a simple ssh test to flush out basic issues
	ssh "$HOST" "uname -a" || fail "Unable to SSH to download server. See setup instructions https://wiki.eclipse.org/Jenkins#Freestyle_job"
	# do a simple ssh test to flush out basic issues
	ssh "$HOST" "ls -la $ZONE" || fail "Remote directory missing \"$ZONE\""

	# do a simple ssh test to flush out basic issues
	ssh "$HOST" "touch ${ZONE}status" || fail "Remote directory write access denied to \"$ZONE\""
    )
    
    # make the remote directory, if needed
    ssh "$HOST" "[ -e $DROP ] || mkdir -p $DROP" || fail "Remote directory \"$DROP\" could not be created"

    # If any of the files already exist, fail before we've copied anything
    for file in "$DROP$TCK"{,.sig,sha256}; do
	ssh "$HOST" "[ ! -e $file ]" || fail "Refusing to overwrite existing file $file"
    done

    # Ok, we're clear to copy for real
    for file in "$TCK"{,.sig,sha256}; do
	echo "Uploading $DROP/$file"
	scp "$file" "$HOST:$DROP" 
    done

    # Because hey why not, download it again and check the signature
    scp "$HOST:$DROP$TCK" "$TCK.copy" || fail "Unable to redownload $TCK"

    # Verify the signature to be safe
    gpg --verify "$TCK".sig "$TCK.copy" || fail "Signature Verification failed.  Upload was incomplete"

    # Record the public key
    gpg --armor --export 'jakarta.ee-spec@eclipse.org' > "$WORKSPACE/jakarta.ee-spec.pub"
    
    # Output a clean record
    echo "
-------------------------------------
DATE            : $(date)
SPEC_NAME       : $SPEC_NAME
SPEC_VERSION    : $SPEC_VERSION
TCK_BINARY_SHA  : $(cat $TCK.sha256)
TCK_BINARY_URL  : $TCK_BINARY_URL
-------------------------------------
PROMOTED_NAME   : $TCK
PROMOTED_URL    : https://download.eclipse.org/jakartaee/${SPEC_NAME}/$SPEC_VERSION}/$TCK

--[signature]------------------------
$(cat $TCK.sig)

$(gpg --armor --export 'jakarta.ee-spec@eclipse.org')

" | tee "$WORKSPACE/audit.txt"
)
