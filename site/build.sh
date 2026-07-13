#!/bin/bash
set -e

cd "$(dirname "$0")"

# Assemble MkDocs source with only markdown files
rm -rf .mkdocs_src
mkdir -p .mkdocs_src/wiki .mkdocs_src/install
cp dat/wiki/*.md .mkdocs_src/wiki/
cp dat/install/Install.md .mkdocs_src/install/
cp dat/index.md dat/catalogue.md dat/stats.md .mkdocs_src/

mkdocs build --clean

rm -rf .mkdocs_src

# Static assets not managed by MkDocs
cp robots.txt build/
cp favicon.ico build/ 2>/dev/null || true
cp -r img build/

echo "Build complete: build/"
