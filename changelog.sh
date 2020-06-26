#!/usr/bin/env bash

set -e

function check_changelog() {
  if [[ ! -f ./CHANGELOG.md ]]; then
    touch CHANGELOG.md
  fi
}


function get_changelog() {
  local currentTag previousTag prevChangelogContents
  currentTag=$(git describe --abbrev=0 --tags)
  previousTag=$(git describe --abbrev=0 ${currentTag}^)
  prevChangelogContents=$(cat ./CHANGELOG.md)

  {
    echo "## $currentTag";
    echo "";
#    echo $(git shortlog --no-merges | grep -v "Upgrade to";)
    git log --pretty="%ad %h - %s %n" --decorate --color --date=format:'%Y/%m/%d' ${currentTag}...${previousTag}
    echo "";
  } > CHANGELOG.md
  echo "$prevChangelogContents" >> CHANGELOG.md
}

function main() {
  check_changelog
  get_changelog
}

main