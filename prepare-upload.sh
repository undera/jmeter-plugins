#! /bin/sh -ex

REV=`git log -1 --format="%H" | cut -c1-10`

# build
#mvn clean cobertura:cobertura package

rm -rf upload
mkdir -p upload

# site docs
cp -r site/* upload/

# package snapshots
mkdir -p upload/files/nightly

python downloads.py
mv site/files/packages upload/files/packages

for D in `ls` ; do
    if ls $D/target/*-*.zip 2>/dev/null ; then
        cp $D/target/*-*.zip upload/files/nightly/
    fi
done

#cp manager/target/jmeter-plugins-manager-*.jar upload/files/nightly/

rename "s/.jar/_$REV.jar/" upload/files/nightly/*.jar
rename "s/.zip/_$REV.zip/" upload/files/nightly/*.zip

# examples
cp -r examples upload/img/

curl -sS https://getcomposer.org/installer | php
cd upload
../composer.phar update --no-dev --prefer-stable
cp vendor/undera/pwe/.htaccess ./
cd ..

cd upload
zip -r site.zip * .htaccess
cd ..