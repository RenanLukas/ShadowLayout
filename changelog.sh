#!/usr/bin/env bash

set -e

function check_changelog() {
  if [[ ! -f ./CHANGELOG.md ]]; then
    touch CHANGELOG.md
  fi
}

function check_current_tag() {
  local tag=$(git describe --abbrev=0 --tags)
  if grep -w ${tag} CHANGELOG.md; then
     echo "The tag:${tag}  is present in changelog file"
     exit 1
  fi
}

function get_tag_new() {
    local contentNew
    currentTag=$(git describe --abbrev=0 --tags)
    previousTag=$(git describe --abbrev=0 ${currentTag}^)

    contentNew=$(cat << EOM
### Added
   * $(git log --pretty="[%ad] %h - %s %n" --grep="Added:" --decorate --color --date=format:'%Y/%m/%d' ${currentTag}...${previousTag})
EOM
)

}

function get_changelog() {
  local currentTag previousTag prevChangelogContents content
  currentTag=$(git describe --abbrev=0 --tags)
  previousTag=$(git describe --abbrev=0 ${currentTag}^)
  prevChangelogContents=$(cat ./CHANGELOG.md)

content=$(cat << EOM
# Changelog

## ${currentTag} - $(date +'[%d/%m/%Y]')

$(git log --pretty="[%ad] %h - %s %n" --decorate --color --date=format:'%Y/%m/%d' ${currentTag}...${previousTag})
EOM
)
    echo "$content" > CHANGELOG.md
    echo "$prevChangelogContents" | sed -e 's/\# Changelog//g' >> CHANGELOG.md
}

function main() {
  check_changelog
  check_current_tag
  get_changelog
#  $(git log --pretty="[%ad] %h - %s %n" --decorate --color --date=format:'%Y/%m/%d' ${currentTag}...${previousTag})
}

main