#!/bin/bash -ex

##[ Imports ]####################

source "$(dirname $0)/utils.sh"
source "$(dirname $0)/report.sh"
source "$(dirname $0)/keys.sh"

##[ Required Environment ]#######

require KEYRING
require PASSPHRASE
require SPEC_NAME "[a-z][a-z-]*[a-z]"
require SPEC_VERSION "[1-9][0-9]*(.[0-9]+)?"
require FILE_URLS "https?://download.eclipse.org/[a-zA-Z0-9_./-]+\.(zip|tar.gz|pdf|jar|war|ear|txt|html)"

##[ Main ]#######################

COMMITTEE_KEYRING=/tmp/committee
CONSUMER_KEYRING=/tmp/consumer
UPDATED_KEYRING=/tmp/updated

( # Import the locally available private keys into a dedicated keyring
    export GNUPGHOME="$COMMITTEE_KEYRING"

    # The Eclipse infrastrucutre team gives us the KEYRING file via the Jenkins job
    # It's possible these keys have been rotated and we will need to take action
    gpg-import "$KEYRING"

    # Export the corresponding public key.  If the private key has been rotated we will need to
    # republish our public keys with the current keys included
    gpg --armor --export 'jakarta.ee-spec@eclipse.org' > "/tmp/jakartaee-spec.current.pub" || fail "Unable to export jakartaee-spec.current.pub from $GNUPGHOME"
)

( # Download and import the published public keys into a dedicated keyring
    export GNUPGHOME="$CONSUMER_KEYRING"

    # For safety, we only verify with public keys the Specification Committee has explicitly published
    curl "https://jakarta.ee/specifications/jakartaee-spec-committee.pub" > "/tmp/jakartaee-spec.published.pub" || fail "Cannot download published jakartaee-spec-committee.pub"
    gpg-import "/tmp/jakartaee-spec.published.pub"
)

( # Import both the current and published keys into one keyring
  #
  # If our keys have been rotated, "jakartaee-spec-committee.updated.pub" is the file
  # we need to publish to the url above.
  #
  # Overwritting the contents of jakartaee-spec-committee.pub
  # with jakartaee-spec-committee.updated.pub and committing it is how we would do that.
    export GNUPGHOME="$UPDATED_KEYRING"

    gpg-import "/tmp/jakartaee-spec.published.pub"
    gpg-import "/tmp/jakartaee-spec.current.pub"

    gpg --armor --export 'jakarta.ee-spec@eclipse.org' > "$WORKSPACE/jakartaee-spec-committee.updated.pub" || fail "Unable to export jakartaee-spec-committee.updated.pub from $GNUPGHOME"
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
        curl -L "$url" > "$file" || fail "Could not download $url"

        # If the tck starts with "eclipse-" rename it "jakarta-"
        [[ "$file" == eclipse-* ]] && cp "$file" "${file/eclipse-/jakarta-}"

    done

    # Our list of files before we start signing.  Used in the final report step
    FILES=($(ls))

    export GPG_TTY=$(tty)

    ( # Hash and sign
        for file in *; do
            ( # Sign using the committee keyring
                export GNUPGHOME="$COMMITTEE_KEYRING"
                echo "${PASSPHRASE}" | gpg --sign --armor --batch --passphrase-fd 0 --output "$file".sig --detach-sig "$file" || fail "Signature failed"
            )

            ( # Verify using the consumer keyring
                export GNUPGHOME="$CONSUMER_KEYRING"

                # If someone has rotated the private key but NOT updated the
                # public key file in github, this step will fail
                gpg --verify "$file".sig "$file" || fail "Outdated Public Keys. Signature Verification failed for \"$file\". Our keys have likely been rotated and we need to publish jakartaee-spec-committee.updated.pub"
            )

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

            ( # Verify the signature
                export GNUPGHOME="$CONSUMER_KEYRING"
                gpg --verify "$sig" "$file" || fail "Downloaded Signature Verification failed.  Upload was incomplete. $file $sig"
            )
        done
    )

    export GNUPGHOME="$COMMITTEE_KEYRING"
    report "${FILES[@]}" > "$WORKSPACE/${SPEC_NAME}-${SPEC_VERSION}.html"

)
