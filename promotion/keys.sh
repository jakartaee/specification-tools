#!/bin/bash -ex

source "$(dirname $0)/utils.sh"

function gpg-import {
    local keyfile="${1?Specifiy a keyfile to import}"

    require GNUPGHOME "/[a-zA-Z0-9/_-]+"

    [ ! -e "$GNUPGHOME" ] && {
        mkdir "$GNUPGHOME" && chmod 700 "$GNUPGHOME" || fail "Cannot create GNUPGHOME dir $GNUPGHOME"
    }

    gpg --batch --import "${keyfile}"

    # Iterate over each of the imported private keys and mark them trusted
    for key in $(gpg --list-keys --with-colons | tr ':' '\t' | grep '^fpr' | cut -f 10); do
        # The echo statement handles providing two values to an interactive prompt
        echo -e "5\ny\n" |  gpg --batch --command-fd 0 --expert --edit-key "$key" trust;
    done
}