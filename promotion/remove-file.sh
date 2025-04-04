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

##[ Required Environment ]#######

require SPEC_NAME "[a-z][a-z-]*[a-z]"
require SPEC_VERSION "[1-9][0-9]*(.[0-9]+)?"
require SPEC_FILE "[a-zA-Z][a-zA-Z0-9._-]+[a-zA-Z0-9]"

##[ Main ]#######################

ZONE="/home/data/httpd/download.eclipse.org/jakartaee/"
DROP="/home/data/httpd/download.eclipse.org/jakartaee/${SPEC_NAME}/${SPEC_VERSION}/"
FILE="/home/data/httpd/download.eclipse.org/jakartaee/${SPEC_NAME}/${SPEC_VERSION}/${SPEC_FILE}"
HOST='genie.jakartaee-spec-committee@projects-storage.eclipse.org'

( # Test SSH access and write access
    
    # do a simple ssh test to flush out basic issues
    ssh "$HOST" "uname -a" || fail "Unable to SSH to download server. See setup instructions https://wiki.eclipse.org/Jenkins#Freestyle_job"
    # do a simple ssh test to flush out basic issues
    ssh "$HOST" "ls -la $ZONE" || fail "Remote directory missing \"$ZONE\""
    
    # do a simple ssh test to flush out basic issues
    ssh "$HOST" "touch ${ZONE}status && rm ${ZONE}status" || fail "Remote directory write access denied to \"$ZONE\""
)

# Do a directory listing and report if the spec version is not found
ssh "$HOST" "ls -la $DROP" || fail "Specification directory \"$DROP\" not found"

# Remove the remote file
ssh "$HOST" "rm -r $FILE" || fail "Unable to remove file \"$FILE\""
