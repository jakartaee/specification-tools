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

    # get the value of the variable
    eval "value=${!name}"
    
    # Fail if the variable name doesn't exist
    [ -n "$value" ] || fail "$name environment variable was not set"

    # (optional) Match against a regex and fail if it doesn't match
    [ -n "$regex" ] && {
	echo "$value" | egrep --silent "^$regex$" ||
	    fail "$name value \"$value\" does not match pattern \"$regex\""
    }
}
