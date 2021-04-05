#! /bin/sh -ex

echo TRAVIS_PULL_REQUEST: $TRAVIS_PULL_REQUEST
echo TRAVIS_BRANCH: $TRAVIS_BRANCH
echo TRAVIS_EVENT_TYPE: $TRAVIS_EVENT_TYPE

if [ "${TRAVIS_PULL_REQUEST}" = "false" ] && [ "$TRAVIS_BRANCH" = 'master' ] && [ "$TRAVIS_EVENT_TYPE" != 'pull_request' ]; then
  curl --fail -vk https://jmeter-plugins.org/unzip.php?job=$TRAVIS_JOB_ID -F "zipfile=@upload/site.zip" -H "Authorization: Bearer $UPLOAD_TOKEN"
fi
