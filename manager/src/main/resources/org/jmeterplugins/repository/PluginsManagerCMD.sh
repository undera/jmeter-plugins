#!/bin/sh

java -Djava.awt.headless=true -jar $(dirname $0)/../lib/ext/${project.build.finalName}.jar --tool org.jmeterplugins.repository.PluginManagerCMD "$@"
