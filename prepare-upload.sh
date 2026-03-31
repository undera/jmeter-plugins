#! /bin/sh -ex

python downloads.py

rm -rf upload
mkdir -p upload

# Build static site with MkDocs
cd site
bash build.sh
cd ..

# Static site output
cp -r site/build/* upload/

# Examples
cp -r examples upload/img/

# Merge repo JSON files into single file
python merge_repo.py

# PHP for /repo/ endpoint (stays dynamic)
cp site/index.php upload/
cp site/cfg.php upload/
cp site/composer.json upload/
cp -r site/JPGC upload/

php --version
curl -sS https://getcomposer.org/installer | php
cd upload
../composer.phar update --no-dev --prefer-stable
cd ..

cd upload
zip -r site.zip *
cd ..
