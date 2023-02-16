#! /bin/sh -ex

python downloads.py

rm -rf upload
mkdir -p upload

# site docs
cp -r site/* upload/

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
