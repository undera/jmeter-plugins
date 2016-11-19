package org.jmeterplugins.repository;


import java.io.File;
import java.io.IOException;

import net.sf.json.JSON;
import net.sf.json.JSONSerializer;
import net.sf.json.JsonConfig;

import org.apache.commons.io.FileUtils;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

public class JARSourceFilesystem extends JARSource {
    private static final Logger log = LoggingManager.getLoggerForClass();
    private final File base;
    private File jsonFile;

    public JARSourceFilesystem(File jsonFile) {
        this.jsonFile = jsonFile;
        this.base = jsonFile.getParentFile();
    }

    @Override
    public JSON getRepo() throws IOException {
        return JSONSerializer.toJSON(FileUtils.readFileToString(jsonFile), new JsonConfig());
    }

    @Override
    public void reportStats(java.lang.String[] usageStats) throws IOException {
        log.debug("Not reporting stats");
    }

    @Override
    public void setTimeout(int timeout) {
        log.debug("Filesystem does not care of timeout");
    }

    @Override
    public DownloadResult getJAR(String id, String location, GenericCallback<String> statusChanged) throws IOException {
        File orig = new File(base.getAbsolutePath() + File.separator + location);
        File tmp = File.createTempFile("jpgc-", ".jar");
        FileUtils.copyFile(orig, tmp);
        return new DownloadResult(tmp.getAbsolutePath(), orig.getName());
    }
}
