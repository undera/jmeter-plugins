package org.jmeterplugins.repository;


import net.sf.json.*;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.NTCredentials;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpParams;
import org.apache.jmeter.JMeter;
import org.apache.jmeter.engine.JMeterEngine;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

import java.io.File;
import java.io.IOException;
import java.net.*;
import java.nio.file.AccessDeniedException;
import java.util.*;

public class PluginManager {
    private static final Logger log = LoggingManager.getLoggerForClass();
    private int timeout = 1000; // don't delay JMeter startup for more than 1 second
    protected AbstractHttpClient httpClient = new DefaultHttpClient();
    private final static String address = JMeterUtils.getPropDefault("jpgc.repo.address", System.getProperty("jpgc.repo.address", "https://jmeter-plugins.org"));
    protected Map<Plugin, Boolean> allPlugins = new HashMap<>();
    private static PluginManager staticManager = new PluginManager();
    private boolean doRestart = true;

    public PluginManager() {
        String proxyHost = System.getProperty("https.proxyHost", "");
        if (!proxyHost.isEmpty()) {
            int proxyPort = Integer.parseInt(System.getProperty("https.proxyPort", "-1"));
            log.info("Using proxy " + proxyHost + ":" + proxyPort);
            HttpParams params = httpClient.getParams();
            HttpHost proxy = new HttpHost(proxyHost, proxyPort);
            params.setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);

            String proxyUser = System.getProperty(JMeter.HTTP_PROXY_USER, JMeterUtils.getProperty(JMeter.HTTP_PROXY_USER));
            if (proxyUser != null) {
                log.info("Using authenticated proxy with username: " + proxyUser);
                String proxyPass = System.getProperty(JMeter.HTTP_PROXY_PASS, JMeterUtils.getProperty(JMeter.HTTP_PROXY_PASS));
                String localHost = getLocalHost();
                AuthScope authscope = new AuthScope(proxyHost, proxyPort);
                String proxyDomain = JMeterUtils.getPropDefault("http.proxyDomain", "");
                NTCredentials credentials = new NTCredentials(proxyUser, proxyPass, localHost, proxyDomain);
                httpClient.getCredentialsProvider().setCredentials(authscope, credentials);
            }
        }
    }

    private String getLocalHost() {
        try {
            return InetAddress.getLocalHost().getCanonicalHostName();
        } catch (Throwable e) {
            log.error("Failed to get local host name, defaulting to 'localhost'", e);
            return "localhost";
        }
    }

    public void load() throws Throwable {
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

        log.info("Plugins Status: " + getAllPluginsStatusString());

        if (JMeterUtils.getPropDefault("jpgc.repo.sendstats", "true").equals("true")) {
            try {
                reportStats();
            } catch (Exception e) {
                log.debug("Failed to report usage stats", e);
            }
        }

        String jarPath = Plugin.getJARPath(JMeterEngine.class.getCanonicalName());
        if (jarPath != null) {
            File libext = new File(URLDecoder.decode(jarPath, "UTF-8")).getParentFile();
            if (!isWritable(libext)) {
                allPlugins.clear(); // TODO: this makes it to retry requests, which is not good
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

    protected JSON getJSON(String path) throws IOException {
        String uri = address + path;
        log.debug("Requesting " + uri);

        HttpRequestBase get = new HttpGet(uri);
        HttpParams requestParams = get.getParams();
        requestParams.setIntParameter(CoreConnectionPNames.SO_TIMEOUT, timeout);
        requestParams.setIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, timeout);

        HttpResponse result = httpClient.execute(get);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        HttpEntity entity = result.getEntity();
        try {
            entity.writeTo(bos);
            byte[] bytes = bos.toByteArray();
            if (bytes == null) {
                bytes = "null".getBytes();
            }
            String response = new String(bytes);
            int statusCode = result.getStatusLine().getStatusCode();
            if (statusCode >= 300) {
                log.warn("Response with code " + result + ": " + response);
                throw new IOException("Repository responded with wrong status code: " + statusCode);
            } else {
                log.debug("Response with code " + result + ": " + response);
            }
            return JSONSerializer.toJSON(response, new JsonConfig());
        } finally {
            get.abort();
            entity.getContent().close();
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

    private void reportStats() throws IOException {
        String uri = address + "/repo/";
        HttpPost post = new HttpPost(uri);
        HttpEntity body = new StringEntity("stats=" + URLEncoder.encode(getUsageStats(), "UTF-8"));
        post.setEntity(body);
        HttpParams requestParams = post.getParams();
        requestParams.setIntParameter(CoreConnectionPNames.SO_TIMEOUT, 3000);
        requestParams.setIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 1000);

        log.debug("Requesting " + uri);
        httpClient.execute(post);
        post.abort();
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
}
