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


    ZONE="/home/data/httpd/download.eclipse.org/jakartaee/"
    HOST='genie.jakartaee-spec-committee@projects-storage.eclipse.org'

    ssh "$HOST" "ls -la $ZONE" || echo "Remote directory missing \"$ZONE\""
    ssh "$HOST" "ls -la $SRC" || echo "Src file missing \"$SRC\""

    echo "Would cp ${SRC} to ${DEST}..."
    ssh "$HOST" "cd $ZONE; cp $SRC $DEST; cp ${SRC}.sha256 ${DEST}.sha256; cp ${SRC}.sig ${DEST}.sig;" || echo "Failed to cp file \"$SRC\""
