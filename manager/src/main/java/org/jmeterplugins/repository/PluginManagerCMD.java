package org.jmeterplugins.repository;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

import kg.apc.cmdtools.AbstractCMDTool;

import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;
import org.apache.log.Priority;

public class PluginManagerCMD extends AbstractCMDTool implements GenericCallback<String> {
    private static final Logger log = LoggingManager.getLoggerForClass();

    @Override
    protected int processParams(ListIterator listIterator) throws UnsupportedOperationException, IllegalArgumentException {
        LoggingManager.setPriority(Priority.INFO);
        if (!listIterator.hasNext()) {
//            showHelp(System.out);
//            return -1;
            throw new IllegalArgumentException("Command parameter is missing");
        }

        String command = listIterator.next().toString();
        log.info("Command is: " + command);
        try {
            switch (command) {
                case "status":
                    System.out.println(PluginManager.getAllPluginsStatus());
                    break;
                case "install":
                    process(listIterator, true);
                    break;
                case "uninstall":
                    process(listIterator, false);
                    break;
                case "help":
                    showHelp(System.out);
                    break;
                case "available":
                    showAvailable();
                    break;
                case "upgrades":
                    showUpgrades();
                    break;
                default:
                    throw new UnsupportedOperationException("Wrong command: " + command);
            }
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Throwable e) {
            throw new RuntimeException("Failed to perform cmdline operation: " + e.getMessage(), e);
        }

        return 0;
    }

    protected void process(ListIterator listIterator, boolean install) throws Throwable {
        if (!listIterator.hasNext()) {
            throw new IllegalArgumentException("Plugins list parameter is missing");
        }

        Map<String, String> params = parseParams(listIterator.next().toString());
        PluginManager mgr = new PluginManager();
        mgr.setTimeout(30000); // TODO: add property?
        mgr.load();
        mgr.setDoRestart(false);

        for (Map.Entry<String, String> pluginSpec : params.entrySet()) {
            Plugin plugin = mgr.getPluginByID(pluginSpec.getKey());
            if (pluginSpec.getValue() != null) {
                plugin.setCandidateVersion(pluginSpec.getValue());
            }
            mgr.toggleInstalled(plugin, install);
        }
        mgr.applyChanges(this);
    }

    private Map<String, String> parseParams(String paramStr) {
        log.info("Params line is: " + paramStr);
        HashMap<String, String> res = new HashMap<>();
        for (String part : paramStr.split(",")) {
            if (part.contains("=")) {
                String[] pieces = part.split("=");
                res.put(pieces[0].trim(), pieces[1].trim());
            } else {
                res.put(part.trim(), null);
            }
        }
        return res;
    }

    @Override
    protected void showHelp(PrintStream printStream) {
        printStream.println("Options for tool 'PluginManagerCMD': <command> <paramstr> "
                + " where <command> is one of: status, install, uninstall, help, available, upgrades");
    }

    protected void showAvailable() {
        //TODO: remove
        System.out.println("showAvailable");

        PluginManager pmgr = PluginManager.getStaticManager();

        final Set<Plugin> availablePlugins = pmgr.getAvailablePlugins();


        final StringBuilder buf = new StringBuilder("Available Plugins\r\n");

        for (Plugin plugin : availablePlugins) {
            buf.append("[");

            String id = plugin.getID();

            Set<String> versionSet = plugin.getVersions();
            String[] versions = versionSet.toArray(new String[versionSet.size()]);

            for (int i = versions.length - 1; i >= 0; i--) {
                buf.append(id).append('=').append(versions[i]);
                if (i != 0) {
                    buf.append(", ");
                }
            }
            buf.append("]\r\n");

        }

        System.out.println(buf.toString());
    }

    protected void showUpgrades() {
        //TODO: remove
        System.out.println("showUpgrades");


    }

    @Override
    public void notify(String s) {
        if (s.endsWith("%")) {
            log.debug(s);
        } else {
            log.info(s);
        }
    }
}
