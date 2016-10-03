# pack every plugin from repo into zip
# using dependencies from target
# skip files that exist on the web - check with HEAD request
import json
import logging
import os

if __name__ == "__main__":
    logging.basicConfig(level=logging.DEBUG)
    base_dir = os.path.dirname(os.path.abspath(__file__))
    repo_dir = os.path.join(base_dir, "site", "dat", "repo")
    dest_dir = os.path.join(base_dir, "site", "files", "packages")

    for repo_file in os.listdir(repo_dir):
        logging.debug("Processing file: %s", repo_file)
        with open(os.path.join(repo_dir, repo_file)) as fhd:
            plugins = json.loads(fhd.read())

        for plugin in plugins:
            logging.debug("Processing plugin: %s", plugin)

            for version in plugin['versions']:
                logging.debug("Version: %s %s", version, plugin['versions'][version])

            break  # FIXME

        break  # FIXME
