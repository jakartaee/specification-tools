#!/bin/bash -ex

################################
# 
# Script of reusable functions
#
# Keeps the main scripts cleaner
# and cuts down on duplication
#
################################

function fail {
    echo "$1"
    exit 1
}

function require {
    local name="${1?Specify a variable name}"
    local regex="$2"

    # Fail if the variable name doesn't exist
    [ -n "${!name}" ] || fail "$name environment variable was not set"

    # (optional) Match against a regex and fail if it doesn't match
    if [ -n "$regex" ]; then
	echo "${!name}" | egrep --silent "^$regex$" ||
	    fail "$name value \"${!name}\" does not match pattern \"$regex\""
    fi
}
