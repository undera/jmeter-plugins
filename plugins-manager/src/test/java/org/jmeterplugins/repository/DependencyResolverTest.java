package org.jmeterplugins.repository;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import org.junit.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DependencyResolverTest {
    @Test
    public void testSimpleInstall() throws Exception {
        Map<Plugin, Boolean> plugs = new HashMap<>();
        PluginMock install = new PluginMock("install", null, new HashSet<String>());
        plugs.put(install, true);
        PluginMock uninstall = new PluginMock("uninstall", "1.0", new HashSet<String>());
        plugs.put(uninstall, false);

        DependencyResolver obj = new DependencyResolver(plugs);
        Set<Plugin> adds = obj.getAdditions();
        Set<Plugin> dels = obj.getDeletions();

        assertEquals(1, adds.size());
        assertEquals(1, dels.size());
        assertTrue(adds.contains(install));
        assertTrue(dels.contains(uninstall));
    }

    @Test
    public void testDepInstall() throws Exception {
        Map<Plugin, Boolean> plugs = new HashMap<>();
        PluginMock a = new PluginMock("dep-to-install", null, new HashSet<String>());
        plugs.put(a, false);
        HashSet<String> deps = new HashSet<>();
        deps.add(a.getID());
        PluginMock b = new PluginMock("dep-trigger", null, deps);
        plugs.put(b, true);


        DependencyResolver obj = new DependencyResolver(plugs);
        Set<Plugin> adds = obj.getAdditions();
        Set<Plugin> dels = obj.getDeletions();

        assertEquals(2, adds.size());
        assertEquals(0, dels.size());
        assertTrue(adds.contains(a));
        assertTrue(adds.contains(b));
    }

    @Test
    public void testDepUninstall() throws Exception {
        Map<Plugin, Boolean> plugs = new HashMap<>();
        PluginMock a = new PluginMock("root", "1.0", new HashSet<String>());
        plugs.put(a, false);
        HashSet<String> deps = new HashSet<>();
        deps.add(a.getID());
        PluginMock b = new PluginMock("cause", "1.0", deps);
        plugs.put(b, true);

        DependencyResolver obj = new DependencyResolver(plugs);
        Set<Plugin> adds = obj.getAdditions();
        Set<Plugin> dels = obj.getDeletions();

        assertEquals(0, adds.size());
        assertEquals(2, dels.size());
        assertTrue(dels.contains(a));
        assertTrue(dels.contains(b));
    }

    private class PluginMock extends Plugin {
        private Set<String> depends;

        public PluginMock(String id, String iVer, Set<String> depends) {
            super(id);
            this.depends = depends;
            installedVersion = iVer;
            installedPath = iVer;
            versions = JSONObject.fromObject("{\"1.0\":null,\"0.1\":null,\"0.1.5\":null}", new JsonConfig());
            candidateVersion = getMaxVersion();
        }

        @Override
        public Set<String> getDepends() {
            return depends;
        }
    }
}