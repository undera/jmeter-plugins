package org.jmeterplugins.repository;

import org.junit.Test;

import java.io.IOException;

public class PluginManagerTest {
    @Test
    public void testIt() throws IOException {
        PluginManager obj = new PluginManager();
        obj.load();
    }
}