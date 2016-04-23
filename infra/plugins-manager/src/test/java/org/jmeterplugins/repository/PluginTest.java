package org.jmeterplugins.repository;

import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import net.sf.json.JsonConfig;
import org.apache.jmeter.engine.JMeterEngine;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class PluginTest {
    @Test
    public void testVerFromPath() throws Exception {
        assertEquals("0.1", Plugin.getVersionFromPath("/tmp/plugins-manager-0.1.jar"));
        assertEquals("0.1-BETA", Plugin.getVersionFromPath("/tmp/plugins-manager-0.1-BETA.jar"));
        assertEquals("0.1-SNAPSHOT", Plugin.getVersionFromPath("/tmp/plugins-manager-0.1-SNAPSHOT.jar"));
    }

    @Test
    public void testLibInstallPath() throws Exception {
        assertNotNull(Plugin.getLibPath("hector-core", new String[] {"/tmp/hector-core-1.1-2.jar"}));
        assertNotNull(Plugin.getLibPath("kafka_2.8.2", new String[] {"/tmp/kafka_2.8.2-0.8.0.jar"}));
        assertNotNull(Plugin.getLibPath("websocket-api", new String[] {"/tmp/websocket-api-9.1.1.v20140108.jar"}));
        assertNotNull(Plugin.getLibPath("cglib-nodep", new String[] {"/tmp/cglib-nodep-2.1_3.jar"}));
    }

    @Test
    public void testVersionComparator() throws Exception {
        Plugin obj = new PluginExposed();
        obj.detectInstalled();
        assertEquals("2.13", obj.getCandidateVersion());
    }

    private class PluginExposed extends Plugin {
        public PluginExposed() {
            super("test");
            JsonConfig config = new JsonConfig();
            markerClass = JMeterEngine.class.getCanonicalName();
            versions = (JSONObject) JSONSerializer.toJSON("{\"3.0\": null,\"1.4.0\": null,\"1.1.0\": null}", config);
        }
    }
}