package org.jmeterplugins.repository;

import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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
        String expected = "[jpgc-graphs-basic=1.3.1, jpgc-sense=1.3.1, jpgc-cmd=1.3.1, jpgc-graphs-composite=1.3.1, jpgc-csl=1.3.1, jpgc-functions=1.3.1, jpgc-dummy=1.3.1, jpgc-ffw=1.3.1, jmeter-http=2.13, jpgc-fifo=1.3.1, jmeter-core=2.13, jpgc-mergeresults=1.3.1, jpgc-perfmon=1.3.1, jpgc-synthesis=1.3.1, jpgc-plancheck=1.3.1, jpgc-tst=1.3.1, jmeter-components=2.13]";
        assertEquals(expected, res);
    }

    @Test
    public void testStatusSingle() throws IOException {
        assertEquals("2.13", PluginManager.getPluginStatus("jmeter-core"));
        assertEquals(null, PluginManager.getPluginStatus("jmeter-nonexistent"));
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