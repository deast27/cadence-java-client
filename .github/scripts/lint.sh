#!/bin/sh

set -ex

# goJF may return non-zero due to sherter plugin v0.9 incompatibility with
# google-java-format 1.17.0; rely on git status check instead
./gradlew goJF || true

if [ -n "$(git status --porcelain)" ]; then
  echo "There are changes after linting (used goJF) cmd: ./gradlew goJF"
  echo "Please rerun the command and commit the changes"
  git status --porcelain
  exit 1
fi
