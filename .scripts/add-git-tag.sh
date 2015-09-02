#!/usr/bin/env sh

git config --global user.email "builds@travis-ci.com"
git config --global user.name "Travis CI"
git tag $NEW_VERSION -a -m "Generated tag from TravisCI for build $TRAVIS_BUILD_NUMBER"
git push -q https://$TAGPERM@github.com/mmyslyvtsev/girls-monitor --tags
