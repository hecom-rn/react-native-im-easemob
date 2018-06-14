#!/bin/bash

cachedir="$HOME/.rncache"
mkdir -p "$cachedir"
curdir="$(pwd)"
echo "$curdir"

function file_fail () {
    cachefile=$1
    msg=$2

    echo "$msg.  Debug info:" 2>&1
    ls -l "$cachefile" 2>&1
    shasum "$cachefile" 2>&1
    exit 1
}

function fetch_and_unpack () {
    file=$1
    url=$2
    hash=$3

    retries=4
    fetched=no

    while true; do
        if [ -f "$cachedir/$file" ]; then
           if shasum -p "$cachedir/$file" |
              awk -v hash="$hash" '{exit $1 != hash}'; then
               break
           else
               echo "Incorrect hash:" 2>&1
               shasum -p "$cachedir/$file" 2>&1
               echo "Retrying..." 2>&1
           fi
        fi

        (( retries = retries - 1 ))
        if (( retries < 0 )); then
            file_fail "$cachedir/$file" "Failed to successfully download '$file'"
        fi

        rm -f "$cachedir/$file"
        (cd "$cachedir"; curl -J -L -O "$url")
        fetched=yes
    done

    dir=$(basename "$file" .zip)
    if [ "$fetched" = "yes" ] || [ ! -f "$curdir/RNEaseMob/ThirdParty/HyphenateFullSDK-3.4.1/Hyphenate.framework/Hyphenate" ]; then
        (cd "$cachedir"
         rm -rf "$dir"
         echo Unpacking "$cachedir/$file"...
         if ! tar zxf "$cachedir/$file"; then
             file_fail "$cachedir/$file" "Unpacking '$cachedir/$file' failed"
         fi
         mv "$dir/HyphenateFullSDK/Hyphenate.framework/Hyphenate" "$curdir/RNEaseMob/ThirdParty/HyphenateFullSDK-3.4.1/Hyphenate.framework/"
         rm -rf "$dir" __MACOSX)
    fi
}

fetch_and_unpack iOS_IM_SDK_V3.4.1.zip http://downloads.easemob.com/downloads/iOS_IM_SDK_V3.4.1.zip 9f59348473bf980b0ed093bb725046a909d8490d
