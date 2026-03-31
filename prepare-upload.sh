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

# Repo JSON and download packages
mkdir -p upload/dat
cp -r site/dat/repo upload/dat/
cp -r site/files upload/ 2>/dev/null || true

cd upload
zip -r site.zip *
cd ..
