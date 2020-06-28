#!/usr/bin/env bash

set -e

function check_tags_version() {
    projectUrlCommit="https://github.com/RenanLukas/ShadowLayout/commit/"
    currentTag=$(git describe --abbrev=0 --tags)
    previousTag=$(git describe --abbrev=0 ${currentTag}^)
}

function check_changelog() {
  if [[ ! -f ./CHANGELOG.md ]]; then
    touch CHANGELOG.md
  fi
}

function check_current_tag() {
  local tag=${currentTag}
  if grep -w ${tag} CHANGELOG.md; then
     echo "The tag:${tag}  is present in changelog file"
     exit 1
  fi
}

function get_tag_added() {
    local contentAdded tag
    tag="Added"

contentAdded=$(cat << EOM

#### ${tag}

$(git log --grep="${tag}:" --pretty=format:"* [%ad] [%h](${projectUrlCommit}%h) - %s" --decorate --date=format:'%d/%m/%Y' ${currentTag}...${previousTag})

EOM
)
echo "${contentAdded}" | sed -e 's/Added: //g'
}

function get_tag_removed() {
    local contentRemoved tag
    tag="Remove"

contentRemoved=$(cat << EOM

#### ${tag}

$(git log --grep="${tag}:" --pretty=format:"* [%ad] [%h](${projectUrlCommit}%h) - %s" --decorate --date=format:'%d/%m/%Y' ${currentTag}...${previousTag})

EOM
)

echo "${contentRemoved}" | sed -e 's/Remove: //g'
}

function get_changelog() {
    local contentAdded contentRemoved prevChangelogContents
    prevChangelogContents=$(cat ./CHANGELOG.md)

    contentAdded=$(get_tag_added)
    contentRemoved=$(get_tag_removed)

content=$(cat << EOM
# Changelog

## ${currentTag} - $(date +'[%d/%m/%Y]')

${contentAdded}
${contentRemoved}
EOM
)
    echo "$content" > CHANGELOG.md
    echo "$prevChangelogContents" | sed -e 's/\# Changelog//g' >> CHANGELOG.md
}

function main() {
    check_changelog
    check_tags_version
    check_current_tag
    get_changelog
}

main