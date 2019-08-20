#!/bin/bash -ex

##[ Imports ]####################

source "$(dirname $0)/utils.sh"

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
