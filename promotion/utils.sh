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

    # Filter out any blank lines
    local expected="$(echo "${!name}" | egrep -v "^[\t ]*$")"

    # (optional) Match against a regex and fail if it doesn't match
    if [ -n "$regex" ]; then
        local actual="$(echo "$expected" | egrep "^$regex$")"
        [[ "$expected" == "$actual" ]] || fail "$name value \"${!name}\" does not match pattern \"$regex\""
    fi
}

function optional {
    local name="${1?Specify a variable name}"
    local regex="${2?Specify a regex}"

    # Filter out any blank lines
    local expected="$(echo "${!name}" | egrep -v "^[\t ]*$")"

    # Collecting matching lines
    local actual="$(echo "$expected" | egrep "^$regex$")"

    [[ "$expected" == "$actual" ]] || fail "$name value \"${!name}\" does not match pattern \"$regex\""
}
