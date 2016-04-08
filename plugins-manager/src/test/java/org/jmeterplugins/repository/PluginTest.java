package org.jmeterplugins.repository;

import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import net.sf.json.JsonConfig;
import org.apache.jmeter.engine.JMeterEngine;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PluginTest {
    @Test
    public void testVersionComparator() throws Exception {
        Plugin obj = new PluginExposed();
        obj.detectInstalled();
        assertEquals("3.0", obj.getCandidateVersion());
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