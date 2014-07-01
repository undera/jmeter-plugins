#!/bin/sh

# Run the JMeter html code generator in non-GUI mode
# Generate a html page for visualizing graphs from JMeter bin folder
# default directory : JMETER_HOME/bin
# use parameter in jmeter.properties
#jmeterPlugin.gvg.imageDirectory=D:/outils/jmeter/apache-jmeter-2.11/bin/images
# or defined this parameter like : java -cp $CP -DjmeterPlugin.gvg.imageDirectory=D:/outils/jmeter/apache-jmeter-2.11/bin/images org.jmeterplugins.tools.HtmlGraphVisualizationGenerator

setlocal

cd `dirname $0`

CP=../lib/ext/ApacheJMeter_core.jar;../lib/jorphan.jar;../lib/ext/JMeterPlugins-Extras.jar
CP=%CP%;../lib/logkit-2.0.jar;../lib/avalon-framework-4.1.4.jar

java -cp $CP org.jmeterplugins.tools.HtmlGraphVisualizationGenerator