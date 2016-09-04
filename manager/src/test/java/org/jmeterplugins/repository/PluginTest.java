package org.jmeterplugins.repository;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import org.apache.jmeter.engine.JMeterEngine;
import org.junit.Test;

import java.util.HashSet;

import static org.junit.Assert.*;

public class PluginTest {
    @Test
    public void testVerFromPath() throws Exception {
        assertEquals("0.1", Plugin.getVersionFromPath("/tmp/plugins-manager-0.1.jar"));
        assertEquals("0.1-BETA", Plugin.getVersionFromPath("/tmp/plugins-manager-0.1-BETA.jar"));
        assertEquals("0.1-SNAPSHOT", Plugin.getVersionFromPath("/tmp/plugins-manager-0.1-SNAPSHOT.jar"));
    }

    @Test
    public void testLibInstallPath() throws Exception {
        assertNotNull(Plugin.getLibPath("hector-core", new String[]{"/tmp/hector-core-1.1-2.jar"}));
        assertNotNull(Plugin.getLibPath("kafka_2.8.2", new String[]{"/tmp/kafka_2.8.2-0.8.0.jar"}));
        assertNotNull(Plugin.getLibPath("websocket-api", new String[]{"/tmp/websocket-api-9.1.1.v20140108.jar"}));
        assertNotNull(Plugin.getLibPath("cglib-nodep", new String[]{"/tmp/cglib-nodep-2.1_3.jar"}));
    }

    @Test
    public void testVersionComparator() throws Exception {
        PluginMock obj = new PluginMock();
        obj.setMarkerClass(JMeterEngine.class.getCanonicalName());
        obj.detectInstalled(new HashSet<Plugin>());
        assertEquals("2.13", obj.getCandidateVersion());
    }

    @Test
    public void testVirtual() throws Exception {
        PluginMock obj = new PluginMock("virtual");
        obj.setCandidateVersion("0");
        obj.setVersions(JSONObject.fromObject("{\"0\": {\"downloadUrl\": null}}", new JsonConfig()));
        assertTrue(obj.isVirtual());
        HashSet<String> depends = new HashSet<>();
        depends.add("mock");
        obj.setDepends(depends);

        HashSet<Plugin> others = new HashSet<>();
        PluginMock e = new PluginMock();
        e.setMarkerClass(JMeterEngine.class.getCanonicalName());
        e.detectInstalled(new HashSet<Plugin>());
        others.add(e);
        obj.detectInstalled(others);
        assertTrue(obj.isInstalled());

        obj.download(new JARSourceEmul(), new GenericCallback<String>() {
            @Override
            public void notify(String s) {
            }
        });
    }

    @Test
    public void testInstallerClass() {
        String str = "{\"id\": 0, \"markerClass\": 0, \"name\": 0, \"description\": 0, \"helpUrl\": 0, \"vendor\": 0, \"installerClass\": \"test\"}";
        Plugin p = Plugin.fromJSON(JSONObject.fromObject(str, new JsonConfig()));
        assertEquals("test", p.getInstallerClass());
    }
}