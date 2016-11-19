package org.jmeterplugins.repository;

import java.io.IOException;

import net.sf.json.JSON;

abstract public class JARSource {
    public abstract JSON getRepo() throws IOException;

    public abstract void reportStats(String[] usageStats) throws IOException;

    public abstract void setTimeout(int timeout);

    public abstract DownloadResult getJAR(String id, String location, GenericCallback<String> statusChanged) throws IOException;

    public class DownloadResult {
        private final String tmpFile;
        private final String filename;

        public DownloadResult(String tmpFile, String filename) {
            this.tmpFile = tmpFile;
            this.filename = filename;
        }

        public String getTmpFile() {
            return tmpFile;
        }

        public String getFilename() {
            return filename;
        }
    }
}
