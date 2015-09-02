#!/usr/bin/env sh

export CURRENT_VERSION=$(ruby -r rexml/document -e 'puts REXML::Document.new(File.new(ARGV.shift)).elements["/project/version"].text' pom.xml)
export NEW_VERSION=$(echo $CURRENT_VERSION | sed -e "s/-SNAPSHOT/.$TRAVIS_BUILD_NUMBER/g")
mvn versions:set -DnewVersion=$NEW_VERSION -DgenerateBackupPoms=false
