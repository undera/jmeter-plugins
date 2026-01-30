@echo off

java %JVM_ARGS% -jar %~dp0\..\lib\cmdrunner-2.2.jar  --tool Reporter %*
