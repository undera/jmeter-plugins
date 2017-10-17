import json
import logging
import os
import re
import requests
import subprocess
import tempfile
import zipfile
from distutils.version import StrictVersion

base_dir = os.path.dirname(os.path.abspath(__file__))
repo_dir = os.path.join(base_dir, "site", "dat", "repo")
dest_dir = os.path.join(base_dir, "site", "files", "packages")


def is_version_packed(fname):
    resp = requests.head("https://jmeter-plugins.org/files/packages/%s" % fname)
    resp.close()
    return resp.status_code == 200


def pack_version(fname, ver_obj, pmgr_obj, installer_cls):
    if not ver_obj['downloadUrl']:
        return

    with zipfile.ZipFile(fname, 'w', zipfile.ZIP_DEFLATED) as ziph:
        tmp_dir = tempfile.mkdtemp()

        # download main jar
        jar_path = download_into_dir(tmp_dir, ver_obj['downloadUrl'], os.path.join("lib", "ext"))

        if installer_cls is not None:
            os.mkdir(os.path.join(tmp_dir, 'bin'))
            subprocess.check_call(["java", "-cp", jar_path, installer_cls])

        # download libs
        if 'libs' in ver_obj:
            for libname in ver_obj['libs']:
                download_into_dir(tmp_dir, ver_obj['libs'][libname], os.path.join("lib"))

        # download pmgr
        download_into_dir(tmp_dir, pmgr_obj['downloadUrl'], os.path.join("lib", "ext"))

        # archive temp folder
        zip_dir(tmp_dir, ziph)


def zip_dir(path, ziph):
    # ziph is zipfile handle
    for root, dirs, files in os.walk(path):
        for fname in files:
            ziph.write(os.path.join(root, fname), os.path.join(root[len(path):], fname))


def download_into_dir(dirname, url, dest_subpath):
    logging.info("Downloading: %s", url)
    resp = requests.get(url)
    assert resp.status_code == 200
    if 'content-disposition' in resp.headers:
        remote_filename = re.findall("filename=(.+)", resp.headers['content-disposition'])[0]
    else:
        remote_filename = os.path.basename(resp.url)

    dir_path = os.path.join(dirname, dest_subpath)
    if not os.path.exists(dir_path):
        os.makedirs(dir_path)

    with open(os.path.join(dirname, dest_subpath, remote_filename), 'w') as fname:
        fname.write(resp.content)
        fname.close()
    resp.close()
    return os.path.join(dir_path, remote_filename)


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
    download_into_dir(tempfile.mkdtemp(), pmgr_obj['downloadUrl'], "pmgr")  # TODO: use it as cached

    for plugin in plugins:
        if 'screenshotUrl' not in plugin:
            raise ValueError("%s has no screenshotUrl" % plugin['id'])

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

            pack_version(os.path.join(dest_dir, dest_file), plugin['versions'][version], pmgr_obj,
                         plugin.get('installerClass'))
