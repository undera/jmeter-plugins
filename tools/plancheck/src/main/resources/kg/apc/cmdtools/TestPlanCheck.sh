#!/bin/sh

java -Djava.awt.headless=true -Dlog4j.configurationFile=$(dirname $0)/log4j2.xml -jar $(dirname $0)/../lib/cmdrunner-2.2.jar --tool TestPlanCheck "$@"
