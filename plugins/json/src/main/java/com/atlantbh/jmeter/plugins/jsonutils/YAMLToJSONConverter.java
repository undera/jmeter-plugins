package com.atlantbh.jmeter.plugins.jsonutils;


import net.minidev.json.JSONObject;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;
import org.yaml.snakeyaml.Yaml;

import java.util.Map;

public class YAMLToJSONConverter {
    private static final Logger log = LoggingManager.getLoggerForClass();

    public static String convert(String yamlString) {
        Yaml yaml = new Yaml();
        log.debug("Try to load yaml string: " + yamlString);
        Map<String, ?> load = yaml.load(yamlString);
        return JSONObject.toJSONString(load);
    }
}
