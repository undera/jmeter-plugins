#!/bin/bash
set -e

cd "$(dirname "$0")"

mkdocs build --clean

# Static assets not managed by MkDocs
cp robots.txt build/
cp favicon.ico build/ 2>/dev/null || true
cp -r img build/
mkdir -p build/dat/stats
cp -r dat/repo build/dat/

echo "Build complete: build/"
