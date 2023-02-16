#! /bin/sh -ex

REV=`git log -1 --format="%H" | cut -c1-10`

echo Running with REV=$REV

rm -rf upload
mkdir -p upload

# site docs
cp -r site/* upload/

python downloads.py

# examples
cp -r examples upload/img/

php --version
curl -sS https://getcomposer.org/installer | php
cd upload
../composer.phar update --no-dev --prefer-stable
cp vendor/undera/pwe/.htaccess ./
cd ..

cd upload
zip -r site.zip * .htaccess
cd ..
