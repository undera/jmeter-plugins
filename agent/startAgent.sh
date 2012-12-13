#!/bin/sh

java -jar $(dirname $0)/CMDRunner.jar --tool PerfMonAgent "$@"
