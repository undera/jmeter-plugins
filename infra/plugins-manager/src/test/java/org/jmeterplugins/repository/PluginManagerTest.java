package org.jmeterplugins.repository;

import net.sf.json.JSONObject;
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
        HashMap<Plugin, Boolean> allPlugins = new HashMap<>();
        JSONObject res = obj.getUsageStats();
        assertNotNull(res);
    }

    @Test
    public void testResolve() throws IOException {
        Plugin[] init = new Plugin[]{};
        PluginManager obj = new PluginManagerEmul(init);
        obj.getChangesAsText();
    }

    private class PluginManagerEmul extends PluginManager {
        public PluginManagerEmul(Plugin[] plugins) {
            for (Plugin p : plugins) {
                allPlugins.put(p, p.isInstalled());
            }
        }

        @Override
        public JSONObject getUsageStats() {
            return super.getUsageStats();
        }
    }
}