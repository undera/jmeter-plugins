package org.jmeterplugins.repository;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.file.AccessDeniedException;
import java.util.*;

import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.jmeter.engine.JMeterEngine;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

public class PluginManager {
    private static final Logger log = LoggingManager.getLoggerForClass();
    private static PluginManager staticManager = new PluginManager();
    private final JARSource jarSource;
    protected Map<Plugin, Boolean> allPlugins = new HashMap<>();
    private boolean doRestart = true;

    public PluginManager() {
        String sysProp = System.getProperty("jpgc.repo.address", "https://jmeter-plugins.org/repo/");
        String jmProp = JMeterUtils.getPropDefault("jpgc.repo.address", sysProp);
        File jsonFile = new File(jmProp);
        if (jsonFile.isFile()) {
            jarSource = new JARSourceFilesystem(jsonFile);
        } else {
            jarSource = new JARSourceHTTP(jmProp);
        }
    }

    public void load() throws Throwable {
        if (allPlugins.size() > 0) {
            return;
        }

        JSON json = jarSource.getRepo();

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
                allPlugins.put(plugin, plugin.isInstalled());
            }
        }

        if (JMeterUtils.getPropDefault("jpgc.repo.sendstats", "true").equals("true")) {
            try {
                jarSource.reportStats(getUsageStats());
            } catch (Exception e) {
                log.debug("Failed to report usage stats", e);
            }
        }

        log.info("Plugins Status: " + getAllPluginsStatusString());
    }

    private void checkRW() throws UnsupportedEncodingException, AccessDeniedException {
        String jarPath = Plugin.getJARPath(JMeterEngine.class.getCanonicalName());
        if (jarPath != null) {
            File libext = new File(URLDecoder.decode(jarPath, "UTF-8")).getParentFile();
            if (!isWritable(libext)) {
                String msg = "Have no write access for JMeter directories, not possible to use Plugins Manager: ";
                throw new AccessDeniedException(msg + libext);
            }
        }
    }

    private boolean isWritable(File path) {
        File sample = new File(path, "empty.txt");
        try {
            sample.createNewFile();
            sample.delete();
            return true;
        } catch (IOException e) {
            log.debug("Write check failed for " + path, e);
            return false;
        }
    }

    public void startModifications(Set<Plugin> delPlugins, Set<Plugin> installPlugins, Map<String, String> installLibs, Set<String> libDeletions) throws IOException {
        ChangesMaker maker = new ChangesMaker(allPlugins);
        File moveFile = maker.getMovementsFile(delPlugins, installPlugins, installLibs, libDeletions);
        File installFile = maker.getInstallFile(installPlugins);
        File restartFile;
        if (doRestart) {
            restartFile = maker.getRestartFile();
        } else {
            restartFile = null;
        }
        final ProcessBuilder builder = maker.getProcessBuilder(moveFile, installFile, restartFile);
        log.info("JAR Modifications log will be saved into: " + builder.redirectOutput().file().getPath());
        builder.start();
    }

    public void applyChanges(GenericCallback<String> statusChanged) {
        try {
            checkRW();
        } catch (Throwable e) {
            throw new RuntimeException("Cannot apply changes: " + e.getMessage(), e);
        }

        DependencyResolver resolver = new DependencyResolver(allPlugins);
        Set<Plugin> additions = resolver.getAdditions();
        Map<String, String> libInstalls = new HashMap<>();

        for (Map.Entry<String, String> entry : resolver.getLibAdditions().entrySet()) {
            try {
                JARSource.DownloadResult dwn = jarSource.getJAR(entry.getKey(), entry.getValue(), statusChanged);
                libInstalls.put(dwn.getTmpFile(), dwn.getFilename());
            } catch (Throwable e) {
                String msg = "Failed to download " + entry.getKey();
                log.error(msg, e);
                statusChanged.notify(msg);
                throw new RuntimeException("Failed to download library " + entry.getKey(), e);
            }
        }

        for (Plugin plugin : additions) {
            try {
                plugin.download(jarSource, statusChanged);
            } catch (IOException e) {
                String msg = "Failed to download " + plugin;
                log.error(msg, e);
                statusChanged.notify(msg);
                throw new RuntimeException("Failed to download plugin " + plugin, e);
            }
        }

        statusChanged.notify("Restarting JMeter...");

        Set<String> libDeletions = new HashSet<>();
        for (String lib : resolver.getLibDeletions()) {
            libDeletions.add(Plugin.getLibInstallPath(lib));
        }

        modifierHook(resolver.getDeletions(), additions, libInstalls, libDeletions);
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

    protected String[] getUsageStats() {
        ArrayList<String> data = new ArrayList<>();
        data.add(JMeterUtils.getJMeterVersion());

        for (Plugin p : getInstalledPlugins()) {
            data.add(p.getID() + "=" + p.getInstalledVersion());
        }
        log.debug("Usage stats: " + data);
        return data.toArray(new String[0]);
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

    public Set<Plugin> getUpgradablePlugins() {
        Set<Plugin> result = new TreeSet<>(new PluginComparator());
        for (Plugin plugin : allPlugins.keySet()) {
            if (plugin.isUpgradable()) {
                result.add(plugin);
            }
        }
        return result;
    }


    public void toggleInstalled(Plugin plugin, boolean cbState) {
        if (!cbState && !plugin.canUninstall()) {
            log.warn("Cannot uninstall plugin: " + plugin);
            cbState = true;
        }
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

    public Plugin getPluginByID(String key) {
        for (Plugin p : allPlugins.keySet()) {
            if (p.getID().equals(key)) {
                return p;
            }
        }
        throw new IllegalArgumentException("Plugin not found in repo: " + key);
    }

    public void setDoRestart(boolean doRestart) {
        this.doRestart = doRestart;
    }

    private class PluginComparator implements java.util.Comparator<Plugin> {
        @Override
        public int compare(Plugin o1, Plugin o2) {
            return o1.getName().compareTo(o2.getName());
        }
    }

    public void setTimeout(int timeout) {
        jarSource.setTimeout(timeout);
    }

    /**
     * @return Static instance of manager, used to spare resources on repo loading
     */
    public static PluginManager getStaticManager() {
        try {
            staticManager.load();
        } catch (Throwable e) {
            throw new RuntimeException("Failed to get plugin repositories", e);
        }
        return staticManager;
    }

    /**
     * @param id ID of the plugin to check
     * @return Version name for the plugin if it is installed, null otherwise
     */
    public static String getPluginStatus(String id) {
        PluginManager manager = getStaticManager();

        for (Plugin plugin : manager.allPlugins.keySet()) {
            if (plugin.id.equals(id)) {
                return plugin.getInstalledVersion();
            }
        }
        return null;
    }

    /**
     * @return Status for all plugins
     */
    public static String getAllPluginsStatus() {
        PluginManager manager = getStaticManager();
        return manager.getAllPluginsStatusString();
    }

    private String getAllPluginsStatusString() {
        ArrayList<String> res = new ArrayList<>();
        for (Plugin plugin : getInstalledPlugins()) {
            res.add(plugin.getID() + "=" + plugin.getInstalledVersion());
        }
        return Arrays.toString(res.toArray());
    }

    /**
     * @return Available plugins
     */
    public static String getAvailablePluginsAsString() {
        PluginManager manager = getStaticManager();
        return manager.getAvailablePluginsString();
    }

    private String getAvailablePluginsString() {
        ArrayList<String> res = new ArrayList<>();
        for (Plugin plugin : getAvailablePlugins()) {
            List<String> versions = new ArrayList<>(plugin.getVersions());
            Collections.reverse(versions);
            res.add(plugin.getID() + "=" + Arrays.toString(versions.toArray()));
        }
        return Arrays.toString(res.toArray());
    }

    /**
     * @return Upgradable plugins
     */
    public static String getUpgradablePluginsAsString() {
        PluginManager manager = getStaticManager();
        return manager.getUpgradablePluginsString();
    }

    private String getUpgradablePluginsString() {
        ArrayList<String> res = new ArrayList<>();
        for (Plugin plugin : getUpgradablePlugins()) {
            res.add(plugin.getID() + "=" + plugin.getMaxVersion());
        }
        return (res.size() != 0) ?
                Arrays.toString(res.toArray()) :
                "There is nothing to update.";
    }

}