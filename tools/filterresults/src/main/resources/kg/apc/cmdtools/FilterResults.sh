#!/bin/sh

# When you are running FilterResults tool with JMeterPluginsCMD use the following parameters
# --input-file <filenameIn> --output-file <filenameFilteredOut>
# [ 
# --success-filter <true/false>   (true : Only success samplers, false : all results by default)
# --include-labels <labels list>
# --exclude-labels <labels list>
# --include-label-regex <true/false>
# --exclude-label-regex <true/false>
# --start-offset <sec>
# --end-offset <sec>
# --save-as-xml <true/false> (false : CSV format by default) "
# ]
#
# example 1 :
# jmeter/lib/ext/FilterResults.sh --output-file filteredout.csv --input-file inputfile.jtl --include-label-regex true --include-labels "P[1-3].*"
#
# example 2 :
# jmeter/lib/ext/FilterResults.sh --output-file filteredout.xml --input-file inputfile.csv --include-label-regex true --include-labels "P[1-3].*" --start-offset 2 --end-offset 180 --success-filter true --save-as-xml true
#
# May be you need to declare the path to the java binary
java -Djava.awt.headless=true -jar $(dirname $0)/../lib/cmdrunner-2.2.jar --tool FilterResults "$@"
