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
    resp = requests.head("https://jmeter-plugins.org/files/packages/%s" % fname)
    resp.close()
    return resp.status_code == 200


def pack_version(fname, ver_obj, pmgr_obj):
    if not ver_obj['downloadUrl']:
        return

    with zipfile.ZipFile(fname, 'w', zipfile.ZIP_DEFLATED) as ziph:
        # pack main file
        download_into_zip(ziph, ver_obj['downloadUrl'], os.path.join("lib", "ext"))

        # pack libs
        if 'libs' in ver_obj:
            for libname in ver_obj['libs']:
                download_into_zip(ziph, ver_obj['libs'][libname], os.path.join("lib"))

        # pack pmgr
        download_into_zip(ziph, pmgr_obj['downloadUrl'], os.path.join("lib", "ext"))


def download_into_zip(ziph, url, dest_subpath):
    """
    :type ziph: zipfile.ZipFile
    :type url: str
    """
    logging.info("Downloading: %s", url)
    resp = requests.get(url)
    assert resp.status_code == 200
    if 'content-disposition' in resp.headers:
        remote_filename = re.findall("filename=(.+)", resp.headers['content-disposition'])[0]
    else:
        remote_filename = os.path.basename(resp.url)
    ziph.writestr(os.path.join(dest_subpath, remote_filename), resp.content)
    resp.close()


def get_pmgr(plugins_list):
    for plug in plugins_list:
        if plug['id'] == 'jpgc-plugins-manager':
            versions = sorted(plug['versions'].keys(), key=StrictVersion)
            return plug['versions'][versions[-1]]

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
    pmgr_obj = get_pmgr(plugins)

    for plugin in plugins:
        logging.debug("Processing plugin: %s", plugin['id'])
        if plugin['id'] == 'jpgc-plugins-manager':
            continue

        for version in plugin['versions']:
            logging.debug("Version: %s", version)
            if not version:
                continue
            dest_file = "%s-%s.zip" % (plugin['id'], version)
            if is_version_packed(dest_file):
                logging.info("Skip: %s", plugin['id'])
                continue

            pack_version(os.path.join(dest_dir, dest_file), plugin['versions'][version], pmgr_obj)
