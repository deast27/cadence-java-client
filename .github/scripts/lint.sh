#!/bin/sh

set -ex

./gradlew spotlessApply

if [ -n "$(git status --porcelain)" ]; then
  echo "There are changes after linting (used spotlessApply) cmd: ./gradlew spotlessApply"
  echo "Please rerun the command and commit the changes"
  git status --porcelain
  exit 1
fi
