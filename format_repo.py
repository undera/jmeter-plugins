#!/usr/bin/env python3
"""Format JSON files in site/dat/repo/ to canonical style (2-space indent)."""

import json
import os
import sys

repo_dir = os.path.join(os.path.dirname(os.path.abspath(__file__)), "site", "dat", "repo")


def canonical(text):
    return json.dumps(json.loads(text), indent=2, ensure_ascii=False) + "\n"


def format_file(path):
    with open(path) as f:
        raw = f.read()
    formatted = canonical(raw)
    if raw != formatted:
        with open(path, "w") as f:
            f.write(formatted)
        print("Formatted %s" % path)


def repo_files():
    for fname in sorted(os.listdir(repo_dir)):
        if fname != "repo.json" and fname.endswith(".json"):
            yield os.path.join(repo_dir, fname)


def check():
    base = os.path.dirname(os.path.abspath(__file__))
    bad = []
    for path in repo_files():
        with open(path) as f:
            raw = f.read()
        if raw != canonical(raw):
            bad.append(os.path.relpath(path, base))
    if bad:
        for path in bad:
            print("Not formatted: %s" % path)
        print("Run 'python3 format_repo.py %s' to fix." % " ".join(bad))
        sys.exit(1)
    print("All repo JSON files are properly formatted.")


if __name__ == "__main__":
    if "--check" in sys.argv:
        check()
    elif len(sys.argv) > 1:
        for path in sys.argv[1:]:
            format_file(path)
    else:
        for path in repo_files():
            format_file(path)
