package org.jmeterplugins.repository;


import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.jmeter.engine.JMeterEngine;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Plugin {
    private static final Logger log = LoggingManager.getLoggerForClass();
    private static final Pattern dependsParser = Pattern.compile("([^=<>]+)([=<>]+[0-9.]+)?");
    public static final String VER_STOCK = "0.0.0-STOCK";
    protected JSONObject versions = new JSONObject();
    protected String id;
    protected String markerClass;
    protected String installedPath;
    protected String installedVersion;
    protected String tempName;
    protected String destName;
    protected String name;
    protected String description;
    protected String screenshot;
    protected String helpLink;
    protected String vendor;
    protected String candidateVersion;
    protected boolean canUninstall = true;

    public Plugin(String aId) {
        id = aId;
    }

    public static Plugin fromJSON(JSONObject elm) {
        Plugin inst = new Plugin(elm.getString("id"));
        inst.markerClass = elm.getString("markerClass");
        if (elm.get("versions") instanceof JSONObject) {
            inst.versions = elm.getJSONObject("versions");
        }
        inst.name = elm.getString("name");
        inst.description = elm.getString("description");
        inst.screenshot = elm.getString("screenshotUrl");
        inst.helpLink = elm.getString("helpUrl");
        inst.vendor = elm.getString("vendor");
        if (elm.containsKey("canUninstall")) {
            inst.canUninstall = elm.getBoolean("canUninstall");
        }
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
            if (installedVersion.equals(VER_STOCK) && isVersionFrozenToJMeter()) {
                installedVersion = getJMeterVersion();
            }

            log.debug("Found plugin " + this + " version " + installedVersion + " at path " + installedPath);
        }

        if (isInstalled()) {
            candidateVersion = installedVersion;
        } else {
            candidateVersion = getMaxVersion();
        }
    }

    private boolean isVersionFrozenToJMeter() {
        return versions.containsKey("");
    }

    public String getMaxVersion() {
        Set<String> versions = getVersions();
        if (versions.size() > 0) {
            String[] vers = versions.toArray(new String[0]);
            return vers[vers.length - 1];
        }
        return null;
    }

    public Set<String> getVersions() {
        Set<String> versions = new TreeSet<>(new VersionComparator());
        for (Object o : this.versions.keySet()) {
            if (o instanceof String) {
                String ver = (String) o;
                if (ver.isEmpty()) {
                    versions.add(getJMeterVersion());
                } else {
                    versions.add(ver);
                }
            }
        }

        if (isInstalled()) {
            versions.add(installedVersion);
        }
        return versions;
    }

    public static String getJMeterVersion() {
        String ver = JMeterUtils.getJMeterVersion();
        String[] parts = ver.split(" ");
        if (parts.length > 1) {
            return parts[0];
        }

        return ver;
    }

    public static String getVersionFromPath(String installedPath) {
        Pattern p = Pattern.compile("-([\\.0-9]+(-[\\w]+)?).jar");
        Matcher m = p.matcher(installedPath);
        if (m.find()) {
            return m.group(1);
        }
        return VER_STOCK;
    }

    private String getJARPath(String className) {
        Class cls;
        try {
            log.debug("Trying: " + className);
            cls = Thread.currentThread().getContextClassLoader().loadClass(className);
        } catch (Throwable e) {
            if (e instanceof ClassNotFoundException) {
                log.debug("Plugin not found by class: " + className);
            } else {
                log.warn("Unable to load class: " + className, e);
            }
            return null;
        }

        String file = cls.getProtectionDomain().getCodeSource().getLocation().getFile();
        if (!file.toLowerCase().endsWith(".jar")) {
            log.warn("Path is not JAR: " + file);
            return null;
        }

        return file;
    }

    public static String getLibInstallPath(String lib) {
        String[] cp = System.getProperty(DependencyResolver.JAVA_CLASS_PATH).split(File.pathSeparator);
        String path = getLibPath(lib, cp);
        if (path != null) return path;
        return null;
    }

    public static String getLibPath(String lib, String[] paths) {
        for (String path : paths) {
            Pattern p = Pattern.compile("\\W" + lib + "-([0-9]+\\..+).jar");
            Matcher m = p.matcher(path);
            if (m.find()) {
                log.debug("Found library " + lib + " at " + path);
                return path;
            }
        }
        return null;
    }

    public String getID() {
        return id;
    }

    public String getInstalledPath() {
        return installedPath;
    }

    public String getDestName() {
        return destName;
    }

    public String getTempName() {
        return tempName;
    }

    public boolean isInstalled() {
        return installedPath != null;
    }

    public void download(GenericCallback<String> notify) throws IOException {
        String version = getCandidateVersion();

        URI url;
        if (isVersionFrozenToJMeter()) {
            String downloadUrl = versions.getJSONObject("").getString("downloadUrl");
            url = URI.create(String.format(downloadUrl, getJMeterVersion()));
        } else {
            if (!versions.containsKey(version)) {
                throw new IllegalArgumentException("Version " + version + " not found for plugin " + this);
            }
            url = URI.create(versions.getJSONObject(version).getString("downloadUrl"));
        }

        Downloader dwn = new Downloader(notify);
        tempName = dwn.download(id, url);
        File f = new File(JMeterEngine.class.getProtectionDomain().getCodeSource().getLocation().getFile());
        destName = f.getParent() + File.separator + dwn.getFilename();
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getScreenshot() {
        return screenshot;
    }

    public String getHelpLink() {
        return helpLink;
    }

    public String getVendor() {
        return vendor;
    }

    public String getCandidateVersion() {
        return candidateVersion;
    }

    public boolean canUninstall() {
        return canUninstall;
    }

    public String getInstalledVersion() {
        return installedVersion;
    }

    public void setCandidateVersion(String candidateVersion) {
        this.candidateVersion = candidateVersion;
    }

    public boolean isUpgradable() {
        if (!isInstalled()) {
            return false;
        }

        VersionComparator comparator = new VersionComparator();
        return comparator.compare(getInstalledVersion(), getMaxVersion()) < 0;
    }

    public Set<String> getDepends() {
        Set<String> depends = new HashSet<>();
        JSONObject version = versions.getJSONObject(getCandidateVersion());
        if (version.containsKey("depends")) {
            JSONArray list = version.getJSONArray("depends");
            for (Object o : list) {
                if (o instanceof String) {
                    String dep = (String) o;
                    Matcher m = dependsParser.matcher(dep);
                    if (!m.find()) {
                        throw new IllegalArgumentException("Cannot parse depend str: " + dep);
                    }
                    depends.add(m.group(1));
                }
            }
        }
        return depends;
    }

    public Map<String, String> getLibs(String verStr) {
        Map<String, String> depends = new HashMap<>();
        JSONObject version = versions.getJSONObject(verStr);
        if (version.containsKey("libs")) {
            JSONObject list = version.getJSONObject("libs");
            for (Object o : list.keySet()) {
                if (o instanceof String) {
                    String dep = (String) o;
                    Matcher m = dependsParser.matcher(dep);
                    if (!m.find()) {
                        throw new IllegalArgumentException("Cannot parse depend str: " + dep);
                    }
                    depends.put(m.group(1), list.getString(dep));
                }
            }
        }
        return depends;
    }

    private class VersionComparator implements java.util.Comparator<String> {
        @Override
        public int compare(String a, String b) {
            String[] aParts = a.split("\\W+");
            String[] bParts = b.split("\\W+");

            for (int aN = 0; aN < aParts.length; aN++) {
                if (aN < bParts.length) {
                    int res = compare2(aParts[aN], bParts[aN]);
                    if (res != 0) return res;
                }
            }

            return a.compareTo(b);
        }

        private int compare2(String a, String b) {
            if (a.equals(b)) {
                return 0;
            }

            Object ai, bi;
            try {
                ai = Integer.parseInt(a);
            } catch (NumberFormatException e) {
                ai = a;
            }

            try {
                bi = Integer.parseInt(b);
            } catch (NumberFormatException e) {
                bi = b;
            }

            if (ai instanceof Integer && bi instanceof Integer) {
                return Integer.compare((Integer) ai, (Integer) bi);
            } else if (ai instanceof String && bi instanceof String) {
                return ((String) ai).compareTo((String) bi);
            } else if (ai instanceof String) {
                return 1;
            } else {
                return -1;
            }
        }
    }
}
