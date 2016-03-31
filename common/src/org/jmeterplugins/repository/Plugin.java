package org.jmeterplugins.repository;


import net.sf.json.JSONObject;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Plugin {
    private static final Logger log = LoggingManager.getLoggerForClass();
    private static final String VER_STOCK = "N/A";
    private String id;
    private String markerClass;
    private String installedPath;
    private String installedVersion;

    public Plugin(String aId) {
        id = aId;
    }

    public static Plugin fromJSON(JSONObject elm) {
        Plugin inst = new Plugin(elm.getString("id"));
        inst.markerClass = elm.getString("markerClass");
        return inst;
    }

    @Override
    public String toString() {
        return this.id;
    }

    public void detectInstalled() {
        installedPath = getJARPath(markerClass);
        if (installedPath != null) {
            installedVersion = getVersionFromPath(installedPath);
            log.debug("Found plugin " + this + " version " + installedVersion + " at path " + installedPath);
        }
    }

    private String getVersionFromPath(String installedPath) {
        Pattern p = Pattern.compile("-([\\.0-9]+(-SNAPSHOT)?).jar");
        Matcher m = p.matcher(installedPath);
        if (m.find()) {
            return m.group(1);
        }
        return VER_STOCK;
    }

    private String getJARPath(String className) {
        Class c = null;
        try {
            c = Thread.currentThread().getContextClassLoader().loadClass(className);
        } catch (ClassNotFoundException e1) {
            log.debug("Plugin not found by class: " + className);
            return null;
        }

        String file = c.getProtectionDomain().getCodeSource().getLocation().getFile();
        if (!file.toLowerCase().endsWith(".jar")) {
            log.warn("Path is not JAR: " + file);
            return null;
        }

        return file;
    }

    public String getID() {
        return id;
    }

    public void deleteJAR() {
        if (installedPath == null) {
            log.warn("Cannot delete not installed plugin " + this);
            return;
        }

        log.info("Deleting " + installedPath);
        File f = new File(installedPath);
        try {
            if (!f.delete()) {
                log.warn("Failed to delete " + installedPath);
                FileChannel outChan = new FileOutputStream(f, true).getChannel();
                outChan.truncate(0);
                outChan.close();
            }
        } catch (Exception e) {
            log.error("failed to delete " + this, e);
        }
    }
}
