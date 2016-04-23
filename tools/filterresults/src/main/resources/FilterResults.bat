@echo off

rem When you are running FilterResults tool with JMeterPluginsCMD use the following parameters
rem --input-file <filenameIn> --output-file <filenameFilteredOut>
rem [ 
rem --success-filter <true/false>   (true : Only success samplers, false : all results by default)
rem --include-labels <labels list>
rem --exclude-labels <labels list>
rem --include-label-regex <true/false>
rem --exclude-label-regex <true/false>
rem --start-offset <sec>
rem --end-offset <sec>
rem --save-as-xml <true/false> (false : CSV format by default) "
rem ]

rem example 1 :
rem jmeter\lib\ext\FilterResults.bat --output-file filteredout.csv --input-file inputfile.jtl --include-label-regex true --include-labels "P[1-3].*"

rem example 2 :
rem jmeter\lib\ext\FilterResults.bat --output-file filteredout.xml --input-file inputfile.csv --include-label-regex true --include-labels "P[1-3].*" --start-offset 2 --end-offset 180 --success-filter true --save-as-xml true

rem May be you need to declare the path to the java binary
java -jar %0\..\CMDRunner.jar --tool FilterResults %*
