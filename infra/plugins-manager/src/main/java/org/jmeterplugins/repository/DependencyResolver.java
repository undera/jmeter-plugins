package org.jmeterplugins.repository;

import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DependencyResolver {
    private static final Logger log = LoggingManager.getLoggerForClass();
    public static final String JAVA_CLASS_PATH = "java.class.path";
    protected final Set<Plugin> deletions = new HashSet<>();
    protected final Set<Plugin> additions = new HashSet<>();
    protected final Map<String, String> libAdditions = new HashMap<>();
    protected final Set<String> libDeletions = new HashSet<>();
    protected final Map<Plugin, Boolean> allPlugins;

    public DependencyResolver(Map<Plugin, Boolean> allPlugins) {
        this.allPlugins = allPlugins;

        resolveFlags();
        resolveUpgrades();
        resolveDeleteByDependency();
        resolveInstallByDependency();
        resolveDeleteLibs();
        resolveInstallLibs();
    }

    // TODO: return iterators to make values read-only
    public Set<Plugin> getDeletions() {
        return deletions;
    }

    public Set<Plugin> getAdditions() {
        return additions;
    }

    public Map<String, String> getLibAdditions() {
        return libAdditions;
    }

    public Set<String> getLibDeletions() {
        return libDeletions;
    }

    private Plugin getPluginByID(String id) {
        for (Plugin plugin : allPlugins.keySet()) {
            if (plugin.getID().equals(id)) {
                return plugin;
            }
        }
        throw new RuntimeException("Plugin not found by ID: " + id);
    }

    private Set<Plugin> getDependants(Plugin plugin) {
        Set<Plugin> res = new HashSet<>();
        for (Plugin pAll : allPlugins.keySet()) {
            for (String depID : pAll.getDepends()) {
                if (depID.equals(plugin.getID())) {
                    res.add(pAll);
                }
            }
        }
        return res;
    }

    private void resolveFlags() {
        for (Map.Entry<Plugin, Boolean> entry : allPlugins.entrySet()) {
            if (entry.getKey().isInstalled()) {
                if (!entry.getValue()) {
                    deletions.add(entry.getKey());
                }
            } else if (entry.getValue()) {
                additions.add(entry.getKey());
            }
        }
    }

    private void resolveUpgrades() {
        // detect upgrades
        for (Map.Entry<Plugin, Boolean> entry : allPlugins.entrySet()) {
            Plugin plugin = entry.getKey();
            if (entry.getValue() && plugin.isInstalled() && !plugin.getInstalledVersion().equals(plugin.getCandidateVersion())) {
                deletions.add(plugin);
                additions.add(plugin);
            }
        }
    }

    private void resolveDeleteByDependency() {
        // delete by depend
        boolean hasModifications = true;
        while (hasModifications) {
            log.debug("Check uninstall dependencies");
            hasModifications = false;
            for (Plugin plugin : deletions) {
                if (!additions.contains(plugin)) {
                    for (Plugin dep : getDependants(plugin)) {
                        if (!deletions.contains(dep) && dep.isInstalled()) {
                            deletions.add(dep);
                            hasModifications = true;
                        }
                        if (additions.contains(dep)) {
                            additions.remove(dep);
                            hasModifications = true;
                        }
                    }
                }

                if (hasModifications) {
                    break; // prevent ConcurrentModificationException
                }
            }
        }
    }

    private void resolveInstallByDependency() {
        // resolve dependencies
        boolean hasModifications = true;
        while (hasModifications) {
            log.debug("Check install dependencies: " + additions);
            hasModifications = false;
            for (Plugin plugin : additions) {
                for (String pluginID : plugin.getDepends()) {
                    Plugin depend = getPluginByID(pluginID);
                    if (!additions.contains(depend)) {
                        log.debug("Add to install: " + depend);
                        additions.add(depend);
                        hasModifications = true;
                    }
                }

                if (hasModifications) {
                    break; // prevent ConcurrentModificationException
                }
            }
        }
    }

    private void resolveInstallLibs() {
        for (Plugin plugin : additions) {
            Map<String, String> libs = plugin.getLibs(plugin.getCandidateVersion());
            for (String lib : libs.keySet()) {
                if (Plugin.getLibInstallPath(lib) == null) {
                    libAdditions.put(lib, libs.get(lib));
                }
            }
        }
    }

    private void resolveDeleteLibs() {
        for (Plugin plugin : deletions) {
            if (additions.contains(plugin)) { // skip upgrades
                continue;
            }

            Map<String, String> libs = plugin.getLibs(plugin.getInstalledVersion());
            for (String lib : libs.keySet()) {
                if (Plugin.getLibInstallPath(lib) != null) {
                    libDeletions.add(lib);
                } else {
                    log.warn("Did not find library to uninstall it: " + lib);
                }
            }
        }

        for (Plugin plugin : allPlugins.keySet()) {
            if (additions.contains(plugin) || (plugin.isInstalled() && !deletions.contains(plugin))) {
                String ver = additions.contains(plugin) ? plugin.getCandidateVersion() : plugin.getInstalledVersion();
                //log.debug("Affects " + plugin + " v" + ver);
                Map<String, String> libs = plugin.getLibs(ver);
                for (String lib : libs.keySet()) {
                    if (libDeletions.contains(lib)) {
                        log.debug("Won't delete lib " + lib + " since it is used by " + plugin);
                        libDeletions.remove(lib);
                    }
                }
            }
        }
    }
}
