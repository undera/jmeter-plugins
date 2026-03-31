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
python -c "
import json, os
plugins = []
for f in sorted(os.listdir('site/dat/repo')):
    if f.endswith('.json'):
        plugins.extend(json.load(open('site/dat/repo/' + f)))
os.makedirs('upload/dat/repo', exist_ok=True)
json.dump(plugins, open('upload/dat/repo/repo.json', 'w'))
"

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
