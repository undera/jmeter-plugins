package org.jmeterplugins.repository;

import org.apache.jmeter.engine.JMeterEngine;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import static org.junit.Assert.*;

public class PluginManagerTest {
    @Test
    public void testResolve() throws IOException {
        Plugin[] init = new Plugin[]{};
        PluginManager obj = new PluginManagerEmul(init);
        obj.getChangesAsText();
    }

    @Test
    public void testStandardSet() throws Throwable {
        URL url = PluginManagerCMDInstaller.class.getResource("/testVirtualPlugin.json");
        System.setProperty("jpgc.repo.address", url.getFile());

        PluginManager pmgr = new PluginManager();
        pmgr.load();

        for (Plugin plugin : pmgr.allPlugins.keySet()) {
            if (plugin.getID().equals("jpgc-standard")) {
                assertTrue(pmgr.allPlugins.get(plugin));
            }
        }
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
    public void testReadOnly() throws Throwable {
        PluginManager mgr = new PluginManager();
        String jarPath = Plugin.getJARPath(JMeterEngine.class.getCanonicalName());
        assert jarPath != null;
        File ifile = new File(jarPath).getParentFile();
        ifile.setReadOnly();
        mgr.load();
        try {
            mgr.applyChanges(new LoggingCallback());
            fail();
        } catch (RuntimeException e) {
            String prefix = "Have no write access for JMeter directories, not possible to use Plugins Manager:";
            assertTrue(e.getMessage().contains(prefix));
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
        public String[] getUsageStats() {
            return super.getUsageStats();
        }
    }

    private class LoggingCallback implements GenericCallback<String> {
        @Override
        public void notify(String s) {
            System.out.println(s);
        }
    }
}