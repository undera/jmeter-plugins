package kg.apc.jmeter.plugins.importer;

import org.yaml.snakeyaml.Yaml;
import org.json.JSONObject;

/**
 * Converts YAML (e.g., OpenAPI specs) to JSON using SnakeYAML.
 */
public class YamlToJsonConverter {

    public String convert(String yaml) {
        try {
            Yaml yamlParser = new Yaml();
            Object data = yamlParser.load(yaml);
            
            // Convert the parsed YAML object to JSON
            JSONObject json = new JSONObject(yamlToMap(data));
            return json.toString();
        } catch (Exception e) {
            throw new RuntimeException("Failed to convert YAML to JSON: " + e.getMessage(), e);
        }
    }

    @SuppressWarnings("unchecked")
    private Object yamlToMap(Object obj) {
        if (obj == null) return null;
        if (obj instanceof java.util.Map) {
            java.util.Map<Object, Object> map = (java.util.Map<Object, Object>) obj;
            java.util.Map<String, Object> result = new java.util.LinkedHashMap<>();
            for (java.util.Map.Entry<Object, Object> entry : map.entrySet()) {
                result.put(entry.getKey().toString(), yamlToMap(entry.getValue()));
            }
            return result;
        } else if (obj instanceof java.util.List) {
            java.util.List<Object> list = (java.util.List<Object>) obj;
            java.util.List<Object> result = new java.util.ArrayList<>();
            for (Object item : list) {
                result.add(yamlToMap(item));
            }
            return result;
        } else {
            return obj;
        }
    }
}
