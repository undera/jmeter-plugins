package org.jmeterplugins.repository;

import org.apache.jmeter.engine.JMeterEngine;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.*;

public class PluginManagerTest {
    @Test
    public void testInstallID() throws Exception {
        PluginManager obj = new PluginManager();
        assertEquals(32, obj.getInstallID().length());
        String res = obj.getUsageStats();
        assertNotNull(res);
    }

    @Test
    public void testResolve() throws IOException {
        Plugin[] init = new Plugin[]{};
        PluginManager obj = new PluginManagerEmul(init);
        obj.getChangesAsText();
    }

    @Test
    public void testStatus() throws IOException {
        String res = PluginManager.getAllPluginsStatus();
        String expected = "[jmeter-http=2.13, jmeter-core=2.13, jpgc-plugins-manager=0.0.0-STOCK, jmeter-tcp=2.13, jmeter-components=2.13]";
        assertEquals(expected, res);
    }

    @Test
    public void testStatusSingle() throws IOException {
        assertEquals("2.13", PluginManager.getPluginStatus("jmeter-core"));
        assertEquals(null, PluginManager.getPluginStatus("jmeter-nonexistent"));
    }

    @Test
    public void testReadOnly() throws IOException {
        PluginManager mgr = new PluginManager();
        String jarPath = Plugin.getJARPath(JMeterEngine.class.getCanonicalName());
        assert jarPath != null;
        File ifile = new File(jarPath).getParentFile();
        ifile.setReadOnly();
        try {
            mgr.load();
            fail();
        } catch (Throwable e) {
            String prefix = "Have no write access for JMeter directories, not possible to use Plugins Manager:";
            assertTrue(e.getMessage().startsWith(prefix));
        } finally {
            ifile.setWritable(true);
        }
    }

    private class PluginManagerEmul extends PluginManager {
        public PluginManagerEmul(Plugin[] plugins) {
            for (Plugin p : plugins) {
                allPlugins.put(p, p.isInstalled());
            }
        }

        @Override
        public String getUsageStats() {
            return super.getUsageStats();
        }
    }
}