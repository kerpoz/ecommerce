#!/bin/bash

BRANCH=$(git rev-parse --abbrev-ref HEAD)

if [ "$BRANCH" == "main" ]; then
  export SPRING_PROFILES_ACTIVE=main
elif [ "$BRANCH" == "develop" ]; then
  export SPRING_PROFILES_ACTIVE=dev
else
  echo "Unknown branch: $BRANCH"
  exit 1
fi