@echo off

java -jar %0\..\lib\ext\${project.build.finalName}.jar --tool org.jmeterplugins.repository.PluginManagerCMD %*
