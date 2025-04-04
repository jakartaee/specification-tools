#!/bin/bash -ex

##[ Imports ]####################

source "$(dirname $0)/utils.sh"
source "$(dirname $0)/report.sh"
source "$(dirname $0)/keys.sh"

##[ Required Environment ]#######

require KEYRING

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

    ( # Hash and sign
        for file in *; do
            ( # Sign using the committee keyring
                export GNUPGHOME="$COMMITTEE_KEYRING"
                passphrase_tmp="$(mktemp)"
                echo "$PASSPHRASE" > "$passphrase_tmp"
                gpg --batch --yes --pinentry-mode loopback --passphrase-file "$passphrase_tmp" --output "$file".sig --detach-sig "$file" || fail "Signature failed"
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
    HOST='genie.jakartaee-spec-committee@projects-storage.eclipse.org'

ssh "$HOST" "ls -la $ZONE" || echo "Remote directory missing \"$ZONE\""
ssh "$HOST" "ls -la $SRC" || echo "Src file missing \"$SRC\""

echo "Would cp ${SRC} to ${DEST}..."
ssh "$HOST" "cd $ZONE; cp $SRC $DEST; cp ${SRC}.sha256 ${DEST}.sha256; cp ${SRC}.sig ${DEST}.sig;" || echo "Failed to cp file \"$SRC\""
