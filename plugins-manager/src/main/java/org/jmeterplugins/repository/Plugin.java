package org.jmeterplugins.repository;


import net.sf.json.JSONObject;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.SystemDefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;
import org.apache.jmeter.engine.JMeterEngine;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

import java.io.*;
import java.net.URI;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Plugin {
    private static final Logger log = LoggingManager.getLoggerForClass();
    private static final String VER_STOCK = "0.0.0-STOCK";
    private JSONObject versions = new JSONObject();
    private String id;
    private String markerClass;
    private String installedPath;
    private String installedVersion;
    private String tempName;
    private String destName;
    private String name;
    private String description;
    private String screenshot;
    private String helpLink;
    private String vendor;

    public Plugin(String aId) {
        id = aId;
    }

    public static Plugin fromJSON(JSONObject elm) {
        Plugin inst = new Plugin(elm.getString("id"));
        inst.markerClass = elm.getString("markerClass");
        inst.versions = elm.getJSONObject("versions");
        inst.name = elm.getString("name");
        inst.description = elm.getString("description");
        inst.screenshot = elm.getString("screenshotUrl");
        inst.helpLink = elm.getString("helpUrl");
        inst.vendor = elm.getString("vendor");
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
        Class c;
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

    public void download(String version) throws IOException {
        URI url = URI.create(versions.getJSONObject(version).getString("downloadUrl"));
        log.info("Downloading: " + url);
        HttpClient httpClient = new SystemDefaultHttpClient();
        HttpGet httpget = new HttpGet(url);

        HttpContext context = new BasicHttpContext();
        HttpResponse response = httpClient.execute(httpget, context);
        if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK)
            throw new IOException(response.getStatusLine().toString());

        HttpEntity entity = response.getEntity();

        File tempFile = File.createTempFile(id, ".jar");

        InputStream inputStream = entity.getContent();
        OutputStream outputStream = new FileOutputStream(tempFile);
        IOUtils.copy(inputStream, outputStream);
        outputStream.close();

        tempName = tempFile.getPath();

        Header cd = response.getLastHeader("Content-Disposition");
        File f = new File(JMeterEngine.class.getProtectionDomain().getCodeSource().getLocation().getFile());
        String filename;
        if (cd != null) {
            filename = cd.getValue().split(";")[1].split("=")[1];
        } else {
            HttpUriRequest currentReq = (HttpUriRequest) context.getAttribute(ExecutionContext.HTTP_REQUEST);
            HttpHost currentHost = (HttpHost) context.getAttribute(ExecutionContext.HTTP_TARGET_HOST);
            String currentUrl = (currentReq.getURI().isAbsolute()) ? currentReq.getURI().toString() : (currentHost.toURI() + currentReq.getURI());
            filename = FilenameUtils.getName(currentUrl);
        }
        destName = f.getParent() + File.separator + filename;
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
}
