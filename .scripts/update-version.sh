#!/usr/bin/env sh

export CURRENT_VERSION=$(mvn3 org.apache.maven.plugins:maven-help-plugin:2.2:evaluate -Dexpression=project.version | grep -v '\[')
export NEW_VERSION=$(echo $CURRENT_VERSION | sed -e "s/-SNAPSHOT/.$TRAVIS_BUILD_NUMBER/g")
mvn versions:set -DnewVersion=$NEW_VERSION
