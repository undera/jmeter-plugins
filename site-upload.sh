#! /bin/sh -ex

if [ "$TRAVIS_PULL_REQUEST" == 'false' ] && [ "$TRAVIS_BRANCH" == 'master' ] && [ "$TRAVIS_EVENT_TYPE" != 'pull_request' ]; then
  curl -vfk https://jmeter-plugins.org/unzip.php -F "zipfile=@upload/site.zip" -H "Authorization: Bearer $UPLOAD_TOKEN"
fi
