#! /bin/sh -ex

curl --fail -vk https://jmeter-plugins.org/unzip.php?job=$TRAVIS_JOB_ID -F "zipfile=@upload/site.zip" -H "Authorization: Bearer $UPLOAD_TOKEN"
