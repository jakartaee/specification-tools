#!/bin/bash -ex

##[ Imports ]####################

source "$(dirname $0)/utils.sh"

##[ Required Environment ]#######

require SPEC_NAME "[a-z][a-z-]*[a-z]"

##[ Main ]#######################

ZONE="/home/data/httpd/download.eclipse.org/jakartaee/"
DROP="/home/data/httpd/download.eclipse.org/jakartaee/${SPEC_NAME}"
HOST='genie.jakartaee-spec-committee@projects-storage.eclipse.org'

( # Test SSH access and write access
    
    # do a simple ssh test to flush out basic issues
    ssh "$HOST" "uname -a" || fail "Unable to SSH to download server. See setup instructions https://wiki.eclipse.org/Jenkins#Freestyle_job"
    # do a simple ssh test to flush out basic issues
    ssh "$HOST" "ls -la $ZONE" || fail "Remote directory missing \"$ZONE\""
    
    # do a simple ssh test to flush out basic issues
    ssh "$HOST" "touch ${ZONE}status && rm ${ZONE}status" || fail "Remote directory write access denied to \"$ZONE\""
)

ssh "$HOST" "[ ! -e $DROP ]" || fail "Directory \"$DROP\" not found"

# Remove the remote directory
ssh "$HOST" "rm -r $DROP" || fail "Unable to remove directory \"$DROP\""
