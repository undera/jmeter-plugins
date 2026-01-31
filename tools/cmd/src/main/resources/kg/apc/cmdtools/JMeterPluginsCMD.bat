@echo off

java %JVM_ARGS% -jar %~dp0\..\lib\cmdrunner-2.3.jar  --tool Reporter %*
