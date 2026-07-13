#!/usr/bin/env python3
"""Merge dat/repo/*.json into a single repo.json for the /repo/ endpoint."""

import json
import os
import sys

src_dir = sys.argv[1] if len(sys.argv) > 1 else 'site/dat/repo'
dest = sys.argv[2] if len(sys.argv) > 2 else 'upload/dat/repo/repo.json'

plugins = []
for f in sorted(os.listdir(src_dir)):
    if f.endswith('.json'):
        with open(os.path.join(src_dir, f)) as fh:
            plugins.extend(json.load(fh))

os.makedirs(os.path.dirname(dest), exist_ok=True)
with open(dest, 'w') as fh:
    json.dump(plugins, fh)

print(f'Merged {len(plugins)} plugins from {src_dir} into {dest}')
