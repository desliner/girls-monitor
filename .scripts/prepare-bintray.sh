#!/usr/bin/env sh

export VERSION=$(ruby -r rexml/document -e 'puts REXML::Document.new(File.new(ARGV.shift)).elements["/project/version"].text' pom.xml)
export DATE=$(date +"%Y-%m-%d")
export DESCRIPTION=$(git log --oneline -n1 | python -c 'import json,sys,re; print json.dumps(re.sub(r"^\S+? ","",sys.stdin.read()[:-1])[0:50])[1:-1]')

echo "Setting version to $VERSION"
echo "Setting date to $DATE"
echo "Setting description to \"$DESCRIPTION\""

sed -i.bak -e "s/__VERSION__/$VERSION/g" -e "s/__DATE__/$DATE/g" -e "s/__DESCRIPTION__/$DESCRIPTION/g" .scripts/bintray.json
