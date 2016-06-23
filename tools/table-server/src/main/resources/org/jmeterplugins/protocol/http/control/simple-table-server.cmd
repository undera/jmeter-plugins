@echo off

rem   Licensed to the Apache Software Foundation (ASF) under one or more
rem   contributor license agreements.  See the NOTICE file distributed with
rem   this work for additional information regarding copyright ownership.
rem   The ASF licenses this file to You under the Apache License, Version 2.0
rem   (the "License"); you may not use this file except in compliance with
rem   the License.  You may obtain a copy of the License at
rem
rem       http://www.apache.org/licenses/LICENSE-2.0
rem
rem   Unless required by applicable law or agreed to in writing, software
rem   distributed under the License is distributed on an "AS IS" BASIS,
rem   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
rem   See the License for the specific language governing permissions and
rem   limitations under the License.

rem   Run the JMeter simple table server in non-GUI mode

rem   Default settings:
rem   jmeterPlugin.sts.port=9191
rem   jmeterPlugin.sts.addTimestamp=true
rem   jmeterPlugin.sts.datasetDirectory=<JMETER_HOME/bin> (leave it empty)
rem   jmeterPlugin.sts.loadAndRunOnStartup=true
rem   You can set this property likes : java -cp %CP% -DjmeterPlugin.sts.port=9191 org.jmeterplugins.protocol.http.control.HttpSimpleTableServer

setlocal

cd /D %~dp0

set CP=..\lib\ext\ApacheJMeter_core.jar;..\lib\jorphan.jar;..\lib\ext\jmeter-plugins-table-server-${version}.jar
set CP=%CP%;..\lib\logkit-2.0.jar;..\lib\avalon-framework-4.1.4.jar

java -cp %CP% org.jmeterplugins.protocol.http.control.HttpSimpleTableServer
