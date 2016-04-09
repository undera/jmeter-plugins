package org.jmeterplugins.repository;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class DependencyResolverTest {
    @Test
    public void name() throws Exception {

        Map<Plugin, Boolean> plugs = new HashMap<>();
        plugs.put(new PluginMock("t1", "1.0"), true);
        plugs.put(new PluginMock("t2", "1.0"), false);

        DependencyResolver obj = new DependencyResolver(plugs);
        Set<Plugin> adds = obj.getAdditions();
        Set<Plugin> dels = obj.getDeletions();

        assertEquals(1, adds.size());
        assertEquals(1, dels.size());
    }

    private class PluginMock extends Plugin {
        public PluginMock(String id, String iVer) {
            super(id);
            installedVersion = iVer;
        }
    }
}