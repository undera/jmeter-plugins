package org.jmeterplugins.repository;

import net.sf.json.JSON;

import java.io.IOException;

public class JARSourceEmul extends JARSource {
    @Override
    public JSON getRepo() throws IOException {
        return null;
    }

    @Override
    public void reportStats(String[] usageStats) throws IOException {
        // NOOP
    }

    @Override
    public void setTimeout(int timeout) {
        // NOOP
    }

    @Override
    public DownloadResult getJAR(String id, String location, GenericCallback<String> statusChanged) throws IOException {
        return null;
    }
}
