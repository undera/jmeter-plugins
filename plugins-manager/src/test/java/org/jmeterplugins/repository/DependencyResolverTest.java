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
    public void testUpgrade() throws Exception {
        Map<Plugin, Boolean> plugs = new HashMap<>();
        PluginMock upgrade = new PluginMock("install", "1.0", new HashSet<String>());
        upgrade.setCandidateVersion("0.1");
        plugs.put(upgrade, true);

        DependencyResolver obj = new DependencyResolver(plugs);
        Set<Plugin> adds = obj.getAdditions();
        Set<Plugin> dels = obj.getDeletions();

        assertEquals(1, adds.size());
        assertEquals(1, dels.size());
        assertTrue(adds.contains(upgrade));
        assertTrue(dels.contains(upgrade));
    }

    @Test
    public void testDepInstall() throws Exception {
        Map<Plugin, Boolean> plugs = new HashMap<>();
        PluginMock jdbc = new PluginMock("jdbc", null, new HashSet<String>());
        plugs.put(jdbc, false);
        PluginMock http = new PluginMock("http", null, new HashSet<String>());
        plugs.put(http, false);
        PluginMock components = new PluginMock("components", null, new HashSet<String>());
        plugs.put(components, false);

        HashSet<String> depsStandard = new HashSet<>();
        depsStandard.add(http.getID());
        depsStandard.add(components.getID());
        depsStandard.add(DependencyResolver.JMETER);
        PluginMock standard = new PluginMock("standard", null, depsStandard);
        plugs.put(standard, false);

        HashSet<String> depsExtras = new HashSet<>();
        depsExtras.add(standard.getID());
        depsExtras.add(jdbc.getID());
        depsExtras.add(http.getID());
        depsExtras.add(DependencyResolver.JMETER);
        PluginMock extras = new PluginMock("extras", null, depsExtras);
        plugs.put(extras, true);

        DependencyResolver obj = new DependencyResolver(plugs);
        Set<Plugin> adds = obj.getAdditions();
        Set<Plugin> dels = obj.getDeletions();

        assertEquals(5, adds.size());
        assertEquals(0, dels.size());
        assertTrue(adds.contains(jdbc));
        assertTrue(adds.contains(components));
        assertTrue(adds.contains(standard));
        assertTrue(adds.contains(extras));
        assertTrue(adds.contains(http));
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