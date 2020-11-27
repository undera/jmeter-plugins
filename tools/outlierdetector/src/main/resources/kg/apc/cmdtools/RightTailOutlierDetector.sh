#!/bin/sh

# When you are running RightTailOutlierDetector tool with cmdrunner use the following parameters
# --input-file <filenameIn> ----tukey-k <1.5 or 3>
#
# example 1 :
# jmeter/lib/ext/RightTailOutlierDetector.sh --input-file inputfile.jtl --tukey-k 3
#
# example 2 :
# jmeter/lib/ext/RightTailOutlierDetector.sh --input-file inputfile.csv --tukey-k 1.5
#
# May be you need to declare the path to the java binary
java -Djava.awt.headless=true -jar $(dirname $0)/../lib/cmdrunner-2.2.jar --tool RightTailOutlierDetector "$@"
