package org.jmeterplugins.repository;


import net.sf.json.*;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.io.IOUtils;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.jorphan.util.JOrphanUtils;
import org.apache.log.Logger;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.net.*;
import java.util.*;

public class PluginManager {
    private static final Logger log = LoggingManager.getLoggerForClass();
    private int timeout = 1000; // don't delay JMeter startup for more than 1 second
    protected HttpClient httpClient = new HttpClient();
    private final static String address = JMeterUtils.getPropDefault("jpgc.repo.address", "http://jmeter-plugins.org");
    protected Map<Plugin, Boolean> allPlugins = new HashMap<>();

    public PluginManager() {
    }

    public void load() throws IOException {
        if (allPlugins.size() > 0) {
            return;
        }

        JSON json = getJSON("/repo/?installID=" + getInstallID());
        if (!(json instanceof JSONArray)) {
            throw new RuntimeException("Result is not array");
        }

        for (Object elm : (JSONArray) json) {
            if (elm instanceof JSONObject) {
                Plugin plugin = Plugin.fromJSON((JSONObject) elm);
                if (plugin.getName().isEmpty()) {
                    log.debug("Skip empty name: " + plugin);
                    continue;
                }

                if (!plugin.isVirtual()) {
                    plugin.detectInstalled(getInstalledPlugins());
                }
                allPlugins.put(plugin, plugin.isInstalled());
            } else {
                log.warn("Invalid array element: " + elm);
            }
        }

        // after all usual plugins detected, detect virtual sets
        for (Plugin plugin : allPlugins.keySet()) {
            if (plugin.isVirtual()) {
                plugin.detectInstalled(getInstalledPlugins());
            }
        }

        log.debug("Plugins: " + allPlugins.keySet());

        if (JMeterUtils.getPropDefault("jpgc.repo.sendstats", "true").equals("true")) {
            try {
                reportStats();
            } catch (Exception e) {
                log.debug("Failed to report usage stats", e);
            }
        }
    }

    private void modifierHook(final Set<Plugin> deletions, final Set<Plugin> additions, final Map<String, String> libInstalls, final Set<String> libDeletions) {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                try {
                    startModifications(deletions, additions, libInstalls, libDeletions);
                } catch (Exception e) {
                    log.warn("Failed to run plugin cleaner job", e);
                }
            }
        });
    }

    protected JSON getJSON(String path) throws IOException {
        httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(timeout);
        httpClient.getHttpConnectionManager().getParams().setSoTimeout(timeout);

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
        return JSONSerializer.toJSON(response, new JsonConfig());
    }

    public void startModifications(Set<Plugin> delPlugins, Set<Plugin> installPlugins, Map<String, String> installLibs, Set<String> libDeletions) throws IOException {
        final File moveFile = makeMovementsFile(delPlugins, installPlugins, installLibs, libDeletions);
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

    private File makeMovementsFile(Set<Plugin> deletes, Set<Plugin> installs, Map<String, String> installLibs, Set<String> libDeletions) throws IOException {
        final File file = File.createTempFile("jpgc-jar-changes", ".list");
        PrintWriter out = new PrintWriter(file);

        if (!deletes.isEmpty() || !libDeletions.isEmpty()) {
            File delDir = File.createTempFile("jpgc-deleted-jars-", "");
            delDir.delete();
            delDir.mkdir();
            log.info("Will move deleted JARs to directory " + delDir);
            for (Plugin plugin : deletes) {
                File installed = new File(plugin.getInstalledPath());
                String delTo = delDir + File.separator + installed.getName();
                out.print(plugin.getInstalledPath() + "\t" + delTo + "\n");
            }

            for (String lib : libDeletions) {
                for (Plugin plugin : allPlugins.keySet()) {
                    if (plugin.isInstalled() && plugin.getInstalledPath().equals(lib)) {
                        log.warn("Cannot delete " + lib + " since it is part of plugin " + plugin);
                        libDeletions.remove(lib);
                    }
                }
            }

            for (String lib : libDeletions) {
                File installed = new File(lib);
                String delTo = delDir + File.separator + installed.getName();
                out.print(lib + "\t" + delTo + "\n");
            }
        }

        String libPath = new File(JOrphanUtils.class.getProtectionDomain().getCodeSource().getLocation().getFile()).getParent();
        for (Map.Entry<String, String> lib : installLibs.entrySet()) {
            out.print(lib.getKey() + "\t" + libPath + File.separator + lib.getValue() + "\n");
        }

        for (Plugin plugin : installs) {
            out.print(plugin.getTempName() + "\t" + plugin.getDestName() + "\n");
        }
        out.close();
        return file;
    }

    public void applyChanges(GenericCallback<String> statusChanged) {
        DependencyResolver resolver = new DependencyResolver(allPlugins);
        Set<Plugin> additions = resolver.getAdditions();
        Map<String, String> libInstalls = new HashMap<>();

        for (Map.Entry<String, String> entry : resolver.getLibAdditions().entrySet()) {
            Downloader dwn = new Downloader(statusChanged);
            try {
                String tmpFile = dwn.download(entry.getKey(), new URI(entry.getValue()));
                libInstalls.put(tmpFile, dwn.getFilename());
            } catch (Exception e) {
                String msg = "Failed to download " + entry.getKey();
                log.error(msg, e);
                statusChanged.notify(msg);
                throw new RuntimeException("Failed to download library " + entry.getKey(), e);
            }
        }

        for (Plugin plugin : additions) {
            try {
                plugin.download(statusChanged);
            } catch (IOException e) {
                String msg = "Failed to download " + plugin;
                log.error(msg, e);
                statusChanged.notify(msg);
                additions.remove(plugin);
            }
        }

        statusChanged.notify("Restarting JMeter...");

        Set<String> libDeletions = new HashSet<>();
        for (String lib : resolver.getLibDeletions()) {
            libDeletions.add(Plugin.getLibInstallPath(lib));
        }

        modifierHook(resolver.getDeletions(), additions, libInstalls, libDeletions);
    }

    private void reportStats() throws IOException {
        httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(1000);
        httpClient.getHttpConnectionManager().getParams().setSoTimeout(3000);

        String uri = address + "/repo/";
        PostMethod post = new PostMethod(uri);
        post.addParameter("stats", getUsageStats());

        log.debug("Requesting " + uri);
        httpClient.executeMethod(post);
    }

    protected String getUsageStats() {
        ArrayList<String> data = new ArrayList<>();
        data.add(getInstallID());
        data.add(JMeterUtils.getJMeterVersion());

        for (Plugin p : getInstalledPlugins()) {
            data.add(p.getID() + "=" + p.getInstalledVersion());
        }
        log.debug("Usage stats: " + data);
        return Arrays.toString(data.toArray(new String[0]));
    }

    public String getChangesAsText() {
        DependencyResolver resolver = new DependencyResolver(allPlugins);

        String text = "";

        for (Plugin pl : resolver.getDeletions()) {
            text += "Uninstall plugin: " + pl + " " + pl.getInstalledVersion() + "\n";
        }

        for (String pl : resolver.getLibDeletions()) {
            text += "Uninstall library: " + pl + "\n";
        }

        for (String pl : resolver.getLibAdditions().keySet()) {
            text += "Install library: " + pl + "\n";
        }

        for (Plugin pl : resolver.getAdditions()) {
            text += "Install plugin: " + pl + " " + pl.getCandidateVersion() + "\n";
        }

        return text;
    }

    public Set<Plugin> getInstalledPlugins() {
        Set<Plugin> result = new TreeSet<>(new PluginComparator());
        for (Plugin plugin : allPlugins.keySet()) {
            if (plugin.isInstalled()) {
                result.add(plugin);
            }
        }
        return result;
    }

    public Set<Plugin> getAvailablePlugins() {
        Set<Plugin> result = new TreeSet<>(new PluginComparator());
        for (Plugin plugin : allPlugins.keySet()) {
            if (!plugin.isInstalled()) {
                result.add(plugin);
            }
        }
        return result;
    }

    public void toggleInstalled(Plugin plugin, boolean cbState) {
        allPlugins.put(plugin, cbState);
    }

    public boolean hasAnyUpdates() {
        for (Plugin p : allPlugins.keySet()) {
            if (p.isUpgradable()) {
                return true;
            }
        }
        return false;
    }

    private class PluginComparator implements java.util.Comparator<Plugin> {
        @Override
        public int compare(Plugin o1, Plugin o2) {
            return o1.getName().compareTo(o2.getName());
        }
    }

    /**
     * This function makes sure anonymous identifier sent
     *
     * @return unique ID for installation
     */
    public String getInstallID() {
        String str = "";
        str += getClass().getProtectionDomain().getCodeSource().getLocation().getFile();
        try {
            str += "\t" + InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            log.warn("Cannot get local host name", e);
        }

        try {
            Enumeration<NetworkInterface> ifs = NetworkInterface.getNetworkInterfaces();
            for (NetworkInterface netint : Collections.list(ifs)) {
                str += "\t" + Arrays.toString(netint.getHardwareAddress());
            }
        } catch (SocketException e) {
            log.warn("Failed to get network addresses", e);
        }

        return DigestUtils.md5Hex(str);
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

}
