#!/usr/bin/env sh

git config --global user.email "builds@travis-ci.com"
git config --global user.name "Travis CI"
git tag -a -m "Generated tag from TravisCI for build $TRAVIS_BUILD_NUMBER" $NEW_VERSION
git push origin $NEW_VERSION
