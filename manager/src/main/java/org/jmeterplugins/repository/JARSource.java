package org.jmeterplugins.repository;

import net.sf.json.JSON;

import java.io.IOException;

abstract public class JARSource {
    public abstract JSON getRepo() throws IOException;

    public abstract void reportStats(String[] usageStats) throws IOException;

    public abstract void setTimeout(int timeout);
}
