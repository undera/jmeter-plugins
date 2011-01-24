#!/bin/sh

java -cp ServerAgent.jar:sigar.jar kg.apc.jmeter.perfmon.agent.ServerAgent $1

