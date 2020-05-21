#!/usr/bin/env bash

set -ev

AUTH_RES=`curl -s --form project="Cognifide/Slice" --form token="$COVERITY_SCAN_TOKEN" https://scan.coverity.com/api/upload_permitted`
AUTH=`echo $AUTH_RES | ruby -e "require 'rubygems'; require 'json'; puts JSON[STDIN.read]['upload_permitted']"`

if [ "$AUTH" = "true" ]; then
  echo "master"
else
  echo "non_existing_branch_name"
fi






