@echo off

rem When you are running RightTailOutlierDetector tool with cmdrunner use the following parameters
rem --input-file <filenameIn> ----tukey-k <1.5 or 3>

rem example 1 :
rem jmeter\lib\ext\RightTailOutlierDetector.bat --input-file inputfile.jtl --tukey-k 3

rem example 2 :
rem jmeter\lib\ext\RightTailOutlierDetector.bat --input-file inputfile.csv --tukey-k 1.5

rem May be you need to declare the path to the java binary
java -jar %~dp0\..\lib\cmdrunner-2.2.jar --tool RightTailOutlierDetector %*
