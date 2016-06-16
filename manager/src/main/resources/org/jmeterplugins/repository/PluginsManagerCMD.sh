#!/bin/sh

java -Djava.awt.headless=true $JVM_ARGS -jar $(dirname $0)/../lib/cmdrunner-2.0.jar --tool org.jmeterplugins.repository.PluginManagerCMD "$@"
