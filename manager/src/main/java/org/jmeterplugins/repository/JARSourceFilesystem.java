package org.jmeterplugins.repository;


import net.sf.json.JSON;
import net.sf.json.JSONSerializer;
import net.sf.json.JsonConfig;
import org.apache.commons.io.FileUtils;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

import java.io.File;
import java.io.IOException;

public class JARSourceFilesystem extends JARSource {
    private static final Logger log = LoggingManager.getLoggerForClass();
    private File jsonFile;

    public JARSourceFilesystem(File jsonFile) {
        this.jsonFile = jsonFile;
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
}
