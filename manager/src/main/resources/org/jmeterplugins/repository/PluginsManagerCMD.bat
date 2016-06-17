@echo off

java %JVM_ARGS% -jar %~dp0\..\lib\cmdrunner-2.0.jar --tool org.jmeterplugins.repository.PluginManagerCMD %*
