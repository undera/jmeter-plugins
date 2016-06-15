package org.jmeterplugins.repository;

import kg.apc.cmdtools.AbstractCMDTool;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.ListIterator;
import java.util.Map;

public class PluginManagerCMD extends AbstractCMDTool implements GenericCallback<String> {
    private static final Logger log = LoggingManager.getLoggerForClass();

    @Override
    protected int processParams(ListIterator listIterator) throws UnsupportedOperationException, IllegalArgumentException {
        if (!listIterator.hasNext()) {
            throw new IllegalArgumentException("Command parameter is missing");
        }

        String command = listIterator.next().toString();
        try {
            switch (command) {
                case "status":
                    System.out.println(PluginManager.getAllPluginsStatus());
                    break;
                case "install":
                    install(listIterator);
                    break;
                case "uninstall":
                    uninstall(listIterator);
                    break;
                default:
                    throw new UnsupportedOperationException("Wrong command: " + command);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to perform cmdline operation: " + e.getMessage(), e);
        }

        return 0;
    }

    protected void install(ListIterator listIterator) throws IOException {
        if (!listIterator.hasNext()) {
            throw new IllegalArgumentException("Plugins list parameter is missing");
        }

        Map<String, String> params = parseParams(listIterator.next().toString());
        PluginManager mgr = new PluginManager();
        mgr.load();

        for (Map.Entry<String, String> pluginSpec : params.entrySet()) {
            Plugin plugin = mgr.getPluginByID(pluginSpec.getKey());
            if (pluginSpec.getValue() != null) {
                plugin.setCandidateVersion(pluginSpec.getValue());
            }
            mgr.toggleInstalled(plugin, true);
        }
        mgr.applyChanges(this);
    }

    protected void uninstall(ListIterator listIterator) throws IOException {
        if (!listIterator.hasNext()) {
            throw new IllegalArgumentException("Plugins list parameter is missing");
        }

        Map<String, String> params = parseParams(listIterator.next().toString());
        PluginManager mgr = new PluginManager();
        mgr.load();

        for (Map.Entry<String, String> pluginSpec : params.entrySet()) {
            Plugin plugin = mgr.getPluginByID(pluginSpec.getKey());
            if (pluginSpec.getValue() != null) {
                plugin.setCandidateVersion(pluginSpec.getValue());
            }
            mgr.toggleInstalled(plugin, false);
        }
        mgr.applyChanges(this);
    }

    private Map<String, String> parseParams(String paramStr) {
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
                + " where <command> is one of: status, install, uninstall, upgrade-all");
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
