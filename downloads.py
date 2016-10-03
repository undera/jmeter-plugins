# TODO: add download links to catalogue
# TODO: add dependency info to catalogue
# TODO: add instruction on downloading into pmgr doc page
# TODO: add link to catalogue search to every doc page

import json
import logging
import os
import re
import zipfile
from distutils.version import StrictVersion

import requests

base_dir = os.path.dirname(os.path.abspath(__file__))
repo_dir = os.path.join(base_dir, "site", "dat", "repo")
dest_dir = os.path.join(base_dir, "site", "files", "packages")


def is_version_packed(fname):
    resp = requests.head("https://jmeter-plugins.org/files/packages/%s" % fname)  # FIXME
    logging.debug("%s %s %s", resp.status_code, resp.text, resp.headers)
    return resp.status_code == 200


def pack_version(fname, ver_obj, pmgr_obj):
    # TODO: pack every plugin from repo into zip
    # TODO: using dependencies from target
    with zipfile.ZipFile(fname, 'w', zipfile.ZIP_DEFLATED) as zip:
        # pack pmgr
        resp = requests.get(pmgr_obj['downloadUrl'])
        assert resp.status_code == 200
        remote_filename = re.findall("filename=(.+)", resp.headers['content-disposition'])[0]
        zip.writestr(os.path.join("lib", "ext", remote_filename), resp.content)

        # pack main file
        resp = requests.get(ver_obj['downloadUrl'])
        assert resp.status_code == 200
        remote_filename = re.findall("filename=(.+)", resp.headers['content-disposition'])[0]
        zip.writestr(os.path.join("lib", "ext", remote_filename), resp.content)

        # pack libs
        if 'libs' in ver_obj:
            for libname in ver_obj['libs']:
                resp = requests.get(ver_obj['libs'][libname])
                assert resp.status_code == 200
                remote_filename = re.findall("filename=(.+)", resp.headers['content-disposition'])[0]
                zip.writestr(os.path.join("lib", remote_filename), resp.content)


def get_pmgr():
    global pmgr_obj, plugin
    pmgr_obj = None
    for plugin in plugins:
        if plugin['id'] == 'jpgc-plugins-manager':
            versions = sorted(plugin['versions'].keys(), key=StrictVersion)
            return plugin['versions'][versions[-1]]

    raise Exception("Failed to find plugins manager meta info")


if __name__ == "__main__":
    logging.basicConfig(level=logging.DEBUG)

    if not os.path.exists(dest_dir):
        os.makedirs(dest_dir)

    plugins = []
    for repo_file in os.listdir(repo_dir):
        with open(os.path.join(repo_dir, repo_file)) as fhd:
            plugins.extend(json.loads(fhd.read()))

    # find pmgr
    pmgr_obj = get_pmgr()

    for plugin in plugins:
        logging.debug("Processing plugin: %s", plugin['id'])

        for version in plugin['versions']:
            logging.debug("Version: %s", version)
            if not version:
                continue
            dest_file = "%s-%s.zip" % (plugin['id'], version)
            if is_version_packed(dest_file):
                logging.info("Skip: %s", plugin['id'])
                continue

            pack_version(os.path.join(dest_dir, dest_file), plugin['versions'][version], pmgr_obj)
