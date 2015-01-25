#! /bin/sh -ex

tar -xzf upload.tgz

# upload
ncftpput -f ~/.jmeter-plugins.org-ncftp.cfg -R -F / upload/*
