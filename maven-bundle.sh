#! /bin/sh -xe

mvn -Dmaven.test.skip=true -Dadditionalparam=-Xdoclint:none clean package javadoc:jar source:jar verify gpg:sign deploy
