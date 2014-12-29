#!/bin/sh

java -Djava.awt.headless=true -jar $(dirname $0)/CMDRunner.jar --tool TestPlanCheck "$@"
