#!/bin/sh

java -Djava.awt.headless=true -jar $(dirname $0)/../lib/cmdrunner-2.2.jar --tool Reporter "$@"
