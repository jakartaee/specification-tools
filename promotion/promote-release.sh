#!/bin/bash -ex

##[ Imports ]####################

source "$(dirname $0)/utils.sh"
source "$(dirname $0)/report.sh"

##[ Required Environment ]#######

require KEYRING
require PASSPHRASE
require SPEC_NAME "[a-z][a-z-]*[a-z]"
require SPEC_VERSION "[1-9][0-9.]*"
require FILE_URLS "https?://download.eclipse.org/[a-zA-Z0-9_./-]+\.(zip|tar.gz|pdf|jar|war|ear|txt)"

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

    for url in $FILE_URLS; do
        # Local file name of the TCK zip or tar.gz
        file="$(basename "$url")"

        # File name must start with safe characters
        # No passing us a "-r" or ".." file or some cleverness
        require file "[a-zA-Z0-9].*"

        # Download the file or fail
        curl "$url" > "$file" || fail "Could not download $url"

        # If the tck starts with "eclipse-" rename it "jakarta-"
        [[ "$file" == eclipse-* ]] && cp "$file" "${file/eclipse-/jakarta-}"
    done

    # Our list of files before we start signing
    FILES=($(ls))

    ( # Hash and sign

        for file in *; do
            # Sign the TCK or fail
            echo "${PASSPHRASE}" | gpg --sign --armor --batch --passphrase-fd 0 --output "$file".sig --detach-sig "$file" || fail "Signature failed"

            # Verify the signature to be safe
            gpg --verify "$file".sig "$file" || fail "Signature Verification failed for \"$file\""

            # Calculate the sha256 for convenience
            shasum -a 256 "$file" | tr ' ' '\t' | cut -f 1 > "$file.sha256" || fail "SHA-256 creation failed for \"$file\""
        done
    )

    ZONE="/home/data/httpd/download.eclipse.org/jakartaee/"
    DROP="/home/data/httpd/download.eclipse.org/jakartaee/${SPEC_NAME}/${SPEC_VERSION}"
    HOST='genie.jakartaee-spec-committee@projects-storage.eclipse.org'

    ( # Test SSH access and write permissions

        # do a simple ssh test to flush out basic issues
        ssh "$HOST" "uname -a" || fail "Unable to SSH to download server. See setup instructions https://wiki.eclipse.org/Jenkins#Freestyle_job"
        # do a simple ssh test to flush out basic issues
        ssh "$HOST" "ls -la $ZONE" || fail "Remote directory missing \"$ZONE\""

        # do a simple ssh test to flush out basic issues
        ssh "$HOST" "touch ${ZONE}status && rm ${ZONE}status" || fail "Remote directory write access denied to \"$ZONE\""
    )

    ( # Perform the upload
        # make the remote directory, if needed
        ssh "$HOST" "[ -e $DROP ] || mkdir -p $DROP" || fail "Remote directory \"$DROP\" could not be created"

        # If any of the local files already exist remotely, fail before we've copied anything
        for file in *; do
            ssh "$HOST" "[ ! -e $DROP/$file ]" || fail "Refusing to overwrite existing file $file"
        done

        # Ok, we're clear to copy for real
        rsync -v -e ssh * "$HOST:$DROP/"
    )

    (# Verify our upload

        VERIFY="/tmp/verify-$$" && mkdir "$VERIFY" || fail "Could not make tmp directory for verification"
        for file in *; do
            curl "https://download.eclipse.org/jakartaee/${SPEC_NAME}/${SPEC_VERSION}/$file" > "$VERIFY/$file"
        done

        for sig in $VERIFY/*.sig; do
            # remove the ".sig" extension, this is the file we're verifying
            file="${sig/.sig/}"

            # Verify the signature
            gpg --verify "$sig" "$file" || fail "Signature Verification failed.  Upload was incomplete. $file $sig"
        done
    )

    # Record the public key
    gpg --armor --export 'jakarta.ee-spec@eclipse.org' > "$WORKSPACE/jakarta.ee-spec.pub"

    report "${FILES[@]}" > "$WORKSPACE/${SPEC_NAME}-${SPEC_VERSION}.html"

)
