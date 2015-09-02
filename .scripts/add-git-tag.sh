#!/usr/bin/env sh

export TAG_NAME=$NEW_VERSION
git config --global user.email "builds@travis-ci.com"
git config --global user.name "Travis CI"
git tag -a -m "Generated tag from TravisCI for build $TRAVIS_BUILD_NUMBER" $TAG_NAME
git push origin --tags
