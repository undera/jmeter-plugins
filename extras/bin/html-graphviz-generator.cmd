@echo off

rem Run the JMeter html code generator in non-GUI mode
rem Generate a html page for visualizing graphs from JMeter bin folder
rem default directory : JMETER_HOME/bin
rem use parameter in jmeter.properties
rem #jmeterPlugin.gvg.imageDirectory=D:/outils/jmeter/apache-jmeter-2.11/bin/images
rem or defined this parameter like : java -cp $CP -DjmeterPlugin.gvg.imageDirectory=D:/outils/jmeter/apache-jmeter-2.11/bin/images org.jmeterplugins.tools.HtmlGraphVisualizationGenerator

setlocal

cd /D %~dp0

set CP=..\lib\ext\ApacheJMeter_core.jar;..\lib\jorphan.jar;..\lib\ext\JMeterPlugins-Extras.jar
set CP=%CP%;..\lib\logkit-2.0.jar;..\lib\avalon-framework-4.1.4.jar

java -cp %CP% org.jmeterplugins.tools.HtmlGraphVisualizationGenerator

pause