@echo off

java -Dlog4j.configurationFile=%~dp0\log4j2.xml -jar %~dp0\..\lib\cmdrunner-2.2.jar --tool TestPlanCheck %*
