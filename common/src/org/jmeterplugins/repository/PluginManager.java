package org.jmeterplugins.repository;


import net.sf.json.*;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.io.IOUtils;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class PluginManager {
    private static final Logger log = LoggingManager.getLoggerForClass();
    protected HttpClient httpClient = new HttpClient();
    private static int TIMEOUT = 5;
    private final static String address = JMeterUtils.getPropDefault("jpgc.repo.address", "http://undera-desktop:8003");
    private Set<Plugin> plugins = new HashSet<>();


    public void load() throws IOException {
        loadRepo();
    }

    public void modifierHook(final Set<Plugin> deletions, final Set<Plugin> additions) {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                try {
                    startModifications(deletions, additions);
                } catch (Exception e) {
                    log.warn("Failed to run plugin cleaner job");
                }
            }
        });
    }

    public Plugin getPluginByID(String id) {
        for (Plugin plugin : plugins) {
            if (plugin.getID().equals(id)) {
                return plugin;
            }
        }
        throw new RuntimeException("Plugin not found by ID: " + id);
    }

    private void loadRepo() throws IOException {
        if (plugins.size() > 0) {
            return;
        }

        httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(TIMEOUT * 1000);
        httpClient.getHttpConnectionManager().getParams().setSoTimeout(TIMEOUT * 1000);

        JSON json = getJSON("/repo/");
        if (!(json instanceof JSONArray)) {
            throw new RuntimeException("Result is not array");
        }

        for (Object elm : (JSONArray) json) {
            if (elm instanceof JSONObject) {
                plugins.add(Plugin.fromJSON((JSONObject) elm));
            } else {
                log.warn("Invalid array element: " + elm);
            }
        }

        log.debug("Plugins: " + plugins);
        for (Plugin plugin : plugins) {
            plugin.detectInstalled();
        }
    }

    private JSON getJSON(String path) throws IOException {
        String uri = address + path;
        log.debug("Requesting " + uri);
        GetMethod get = new GetMethod(uri);
        int result = httpClient.executeMethod(get);
        byte[] bytes = IOUtils.toByteArray(get.getResponseBodyAsStream());
        if (bytes == null) {
            bytes = "null".getBytes();
        }
        String response = new String(bytes);
        if (result >= 300) {
            log.warn("Response with code " + result + ": " + response);
        } else {
            log.debug("Response with code " + result + ": " + response);
        }
        JsonConfig config = new JsonConfig();
        return JSONSerializer.toJSON(response, config);

    }

    public void startModifications(Set<Plugin> delPlugins, Set<Plugin> installPlugins) throws IOException {
        final File delFile = makeDeletionsFile(delPlugins);
        final File addFile = makeAdditionsFile(installPlugins);
        final ProcessBuilder builder = getProcessBuilder(delFile, addFile);
        log.info("JAR Modifications log will be saved into: " + builder.redirectOutput().file().getPath());
        builder.start();
    }

    private ProcessBuilder getProcessBuilder(File delFile, File addFile) throws IOException {
        String jvm_location;
        if (System.getProperty("os.name").startsWith("Win")) {
            jvm_location = System.getProperties().getProperty("java.home") + File.separator + "bin" + File.separator + "java.exe";
        } else {
            jvm_location = System.getProperties().getProperty("java.home") + File.separator + "bin" + File.separator + "java";
        }

        String jarPath = PluginManager.class.getProtectionDomain().getCodeSource().getLocation().getFile();
        if (!jarPath.endsWith(".jar"))
            throw new IllegalArgumentException("Invalid JAR path detected: " + jarPath);
        final File currentJar = new File(jarPath);

        final ArrayList<String> command = new ArrayList<>();
        command.add(jvm_location);
        command.add("-classpath");
        command.add(currentJar.getPath());
        command.add(SafeDeleter.class.getCanonicalName());
        command.add("--delete-list");
        command.add(delFile.getAbsolutePath());
        command.add("--copy-list");
        command.add(addFile.getAbsolutePath());
        log.debug("Command to execute: " + command);
        final ProcessBuilder builder = new ProcessBuilder(command);
        File cleanerLog = File.createTempFile("jpgc-cleaner", ".log");
        builder.redirectError(cleanerLog);
        builder.redirectOutput(cleanerLog);
        return builder;
    }

    private File makeDeletionsFile(Set<Plugin> plugins) throws IOException {
        final File file = File.createTempFile("jpgc-delete", ".list");
        PrintWriter out = new PrintWriter(file);
        for (Plugin plugin : plugins) {
            out.print(plugin.getInstalledPath());
        }
        out.close();
        return file;
    }

    private File makeAdditionsFile(Set<Plugin> plugins) throws IOException {
        final File file = File.createTempFile("jpgc-install", ".list");
        PrintWriter out = new PrintWriter(file);
        for (Plugin plugin : plugins) {
            out.print(plugin.getTempName() + "\t" + plugin.getDestName());
        }
        out.close();
        return file;
    }

    public Set<Plugin> getPlugins() {
        return plugins;
    }
}
