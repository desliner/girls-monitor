#!/usr/bin/env sh

export VERSION=$(ruby -r rexml/document -e 'puts REXML::Document.new(File.new(ARGV.shift)).elements["/project/version"].text' pom.xml)
export TAG_NAME=$VERSION
echo "Tagging with $TAG_NAME"
git config --global user.email "builds@travis-ci.com"
git config --global user.name "Travis CI"
git tag -a -m "Generated tag from TravisCI for build $TRAVIS_BUILD_NUMBER" $TAG_NAME
echo "https://mmyslyvtsev:$GITHUBKEY@github.com" >> ~/.git-credentials
git push https://github.com/mmyslyvtsev/girls-monitor.git --tags
