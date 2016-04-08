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
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.*;

public class PluginManager {
    private static final Logger log = LoggingManager.getLoggerForClass();
    protected HttpClient httpClient = new HttpClient();
    private static int TIMEOUT = 5;
    private final static String address = JMeterUtils.getPropDefault("jpgc.repo.address", "http://undera-desktop:8003"); // FIXME: that's temporary address
    private Set<Plugin> allPlugins = new TreeSet<>(new PluginComparator());
    private Set<Plugin> deletions = new HashSet<>();
    private Set<Plugin> additions = new HashSet<>();

    public void load() throws IOException {
        loadRepo();
    }

    private void modifierHook(final Set<Plugin> deletions, final Set<Plugin> additions) {
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

    @SuppressWarnings("unused") // FIXME: do we need it?
    public Plugin getPluginByID(String id) {
        for (Plugin plugin : allPlugins) {
            if (plugin.getID().equals(id)) {
                return plugin;
            }
        }
        throw new RuntimeException("Plugin not found by ID: " + id);
    }

    private void loadRepo() throws IOException {
        if (allPlugins.size() > 0) {
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
                allPlugins.add(Plugin.fromJSON((JSONObject) elm));
            } else {
                log.warn("Invalid array element: " + elm);
            }
        }

        log.debug("Plugins: " + allPlugins);
        for (Plugin plugin : allPlugins) {
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
        final File moveFile = makeMovementsFile(delPlugins, installPlugins);
        final ProcessBuilder builder = getProcessBuilder(moveFile);
        log.info("JAR Modifications log will be saved into: " + builder.redirectOutput().file().getPath());
        builder.start();
    }

    private ProcessBuilder getProcessBuilder(File moveFile) throws IOException {
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

        File restartFile = getRestartFile(jvm_location);

        final ArrayList<String> command = new ArrayList<>();
        command.add(jvm_location);
        command.add("-classpath");
        command.add(currentJar.getPath());
        command.add(SafeDeleter.class.getCanonicalName());
        command.add("--move-list");
        command.add(moveFile.getAbsolutePath());
        command.add("--restart-command");
        command.add(restartFile.getAbsolutePath());

        log.debug("Command to execute: " + command);
        final ProcessBuilder builder = new ProcessBuilder(command);
        File cleanerLog = File.createTempFile("jpgc-cleaner-", ".log");
        builder.redirectError(cleanerLog);
        builder.redirectOutput(cleanerLog);
        return builder;
    }

    private File getRestartFile(String jvm_location) throws IOException {
        File file = File.createTempFile("jpgc-restart-", ".list");
        PrintWriter out = new PrintWriter(file);
        out.print(jvm_location + "\n");

        RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
        List<String> jvmArgs = runtimeMXBean.getInputArguments();
        for (String arg : jvmArgs) {
            out.print(arg + "\n");
        }

        out.print("-jar\n");

        out.print(JMeterUtils.getJMeterBinDir() + File.separator + "ApacheJMeter.jar\n");

        out.close();
        return file;
    }

    private File makeMovementsFile(Set<Plugin> deletes, Set<Plugin> installs) throws IOException {
        final File file = File.createTempFile("jpgc-jar-changes", ".list");
        PrintWriter out = new PrintWriter(file);

        if (!deletes.isEmpty()) {
            File delDir = File.createTempFile("jpgc-deleted-jars-", "");
            delDir.delete();
            delDir.mkdir();
            log.info("Will move deleted JARs to directory " + delDir);
            for (Plugin plugin : deletes) {
                File installed = new File(plugin.getInstalledPath());
                String delTo = delDir + File.separator + installed.getName();
                out.print(plugin.getInstalledPath() + "\t" + delTo + "\n");
            }
        }

        for (Plugin plugin : installs) {
            out.print(plugin.getTempName() + "\t" + plugin.getDestName() + "\n");
        }
        out.close();
        return file;
    }

    public void applyChanges() {
        for (Plugin plugin : additions) {
            try {
                plugin.download(plugin.getCandidateVersion());
            } catch (IOException e) {
                log.error("Failed to download " + plugin, e);
                additions.remove(plugin);
            }
        }

        modifierHook(deletions, additions);
    }

    public String getChangesAsText() {
        String text = "";

        for (Plugin pl : deletions) {
            text += "Uninstall " + pl + "\n";
        }

        for (Plugin pl : additions) {
            text += "Install " + pl + "\n";
        }

        return text;
    }

    public Set<Plugin> getInstalledPlugins() {
        Set<Plugin> result = new TreeSet<>(new PluginComparator());
        for (Plugin plugin : allPlugins) {
            if (plugin.isInstalled()) {
                result.add(plugin);
            }
        }
        return result;
    }

    public Set<Plugin> getAvailablePlugins() {
        Set<Plugin> result = new TreeSet<>(new PluginComparator());
        for (Plugin plugin : allPlugins) {
            if (!plugin.isInstalled()) {
                result.add(plugin);
            }
        }
        return result;
    }

    public void toggleInstalled(Plugin plugin) {
        if (deletions.contains(plugin)) {
            deletions.remove(plugin);
        } else if (additions.contains(plugin)) {
            additions.remove(plugin);
        } else if (plugin.isInstalled()) {
            deletions.add(plugin);
        } else {
            additions.add(plugin);
        }
    }

    private class PluginComparator implements java.util.Comparator<Plugin> {
        @Override
        public int compare(Plugin o1, Plugin o2) {
            return o1.getName().compareTo(o2.getName());
        }
    }
}
