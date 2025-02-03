#!/bin/bash
# This script is used to set the Spring profile based on the current branch, won't be necessary if we will containerize with Docker

echo "Running set-profile.sh script"

BRANCH=$(git rev-parse --abbrev-ref HEAD)

if [ "$BRANCH" == "main" ]; then
  export SPRING_PROFILES_ACTIVE=main
elif [ "$BRANCH" == "develop" ]; then
  export SPRING_PROFILES_ACTIVE=dev
else
  echo "Unknown branch: $BRANCH"
  exit 1
fi

#TODO investigate why after debugging seems OK, but finally it doesn't change the env vars (at least Spring is not picking it up), use .githooks/post-checkout to automatize process
#TODO implement Dockerfile to avoid this script
echo "Switched to branch '$BRANCH', profile set to '$SPRING_PROFILES_ACTIVE'"