#!/bin/sh

##   Licensed to the Apache Software Foundation (ASF) under one or more
##   contributor license agreements.  See the NOTICE file distributed with
##   this work for additional information regarding copyright ownership.
##   The ASF licenses this file to You under the Apache License, Version 2.0
##   (the "License"); you may not use this file except in compliance with
##   the License.  You may obtain a copy of the License at
##
##       http://www.apache.org/licenses/LICENSE-2.0
##
##   Unless required by applicable law or agreed to in writing, software
##   distributed under the License is distributed on an "AS IS" BASIS,
##   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
##   See the License for the specific language governing permissions and
##   limitations under the License.

##   Run the JMeter simple table server in non-GUI mode

##   Default settings:
##   jmeterPlugin.sts.port=9191
##   jmeterPlugin.sts.addTimestamp=true
##   jmeterPlugin.sts.datasetDirectory=<JMETER_HOME/bin> (leave it empty)
##   jmeterPlugin.sts.loadAndRunOnStartup=true

cd `dirname $0`

CP=../lib/ext/ApacheJMeter_core.jar:../lib/jorphan.jar:../lib/ext/JMeterPlugins-Extras.jar
CP=${CP}:../lib/logkit-2.0.jar:../lib/avalon-framework-4.1.4.jar

java -cp $CP org.jmeterplugins.protocol.http.control.HttpSimpleTableServer