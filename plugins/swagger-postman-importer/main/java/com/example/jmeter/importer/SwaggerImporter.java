package com.example.jmeter.importer;

import com.example.jmeter.importer.model.RequestModel;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Parses OpenAPI 2.0 (Swagger) and OpenAPI 3.x JSON/YAML specifications
 * and returns a list of {@link RequestModel} objects.
 *
 * <p>Captures per request: protocol, host, port, method, path,
 * query params, headers (including auth), content-type, and body data.</p>
 */
public class SwaggerImporter {

    /**
     * Parse a Swagger/OpenAPI file (JSON or YAML) and return request models.
     *
     * @param filePath absolute path to the spec file
     * @return list of parsed {@link RequestModel} instances
     * @throws IOException on file-read errors
     */
    public List<RequestModel> parse(String filePath) throws IOException {
        String content = new String(Files.readAllBytes(Paths.get(filePath)));

        String lower = filePath.toLowerCase();
        if (lower.endsWith(".yaml") || lower.endsWith(".yml")) {
            content = stripYamlComments(content);
            content = new YamlToJsonConverter().convert(content);
        }

        JSONObject root = new JSONObject(content);
        List<RequestModel> requests = new ArrayList<>();

        boolean isV3 = root.has("openapi");

        // ----------------------------------------------------------------
        // Server base-URL
        // ----------------------------------------------------------------
        String defaultProtocol = "http";
        String defaultHost     = "localhost";
        int    defaultPort     = 0;
        String defaultBasePath = "";

        if (isV3) {
            if (root.has("servers")) {
                JSONArray servers = root.getJSONArray("servers");
                if (servers.length() > 0) {
                    ServerInfo si = parseServerUrl(
                            servers.getJSONObject(0).optString("url", ""));
                    defaultProtocol = si.protocol;
                    defaultHost     = si.host;
                    defaultPort     = si.port;
                    defaultBasePath = si.basePath;
                }
            }
        } else {
            String scheme = "http";
            if (root.has("schemes")) {
                JSONArray schemes = root.getJSONArray("schemes");
                if (schemes.length() > 0) scheme = schemes.getString(0);
            }
            defaultProtocol = scheme;
            defaultHost     = root.optString("host", "localhost");
            defaultBasePath = root.optString("basePath", "");
        }

        // ----------------------------------------------------------------
        // Walk paths
        // ----------------------------------------------------------------
        if (!root.has("paths")) return requests;
        JSONObject paths = root.getJSONObject("paths");

        for (Iterator<String> pathIter = paths.keys(); pathIter.hasNext(); ) {
            String pathKey = pathIter.next();
            JSONObject pathItem = paths.getJSONObject(pathKey);

            for (String httpMethod : new String[]{"get", "post", "put", "delete",
                    "patch", "options", "head", "trace"}) {
                if (!pathItem.has(httpMethod)) continue;

                JSONObject operation = pathItem.getJSONObject(httpMethod);
                RequestModel req = new RequestModel();

                String operationId = operation.optString("operationId", "");
                String summary     = operation.optString("summary", "");
                req.setName(
                        !operationId.isEmpty() ? operationId :
                        !summary.isEmpty()     ? summary :
                        httpMethod.toUpperCase() + " " + pathKey);
                req.setDescription(operation.optString("description", summary));

                req.setProtocol(defaultProtocol);
                req.setHost(defaultHost);
                req.setPort(defaultPort);
                req.setMethod(httpMethod.toUpperCase());
                req.setPath(defaultBasePath + pathKey);

                // ---- Parameters ----------------------------------------
                JSONArray params = new JSONArray();
                if (pathItem.has("parameters"))
                    mergeParams(params, pathItem.getJSONArray("parameters"));
                if (operation.has("parameters"))
                    mergeParams(params, operation.getJSONArray("parameters"));

                for (int i = 0; i < params.length(); i++) {
                    JSONObject p = params.getJSONObject(i);
                    String pName  = p.optString("name", "");
                    String pIn    = p.optString("in", "");
                    String pValue = extractDefaultValue(p);

                    if ("query".equals(pIn))        req.addQueryParam(pName, pValue);
                    else if ("header".equals(pIn))  req.addHeader(pName, pValue);
                }

                // ---- Request body --------------------------------------
                if (isV3) {
                    if (operation.has("requestBody")) {
                        JSONObject rb = operation.getJSONObject("requestBody");
                        if (rb.has("content")) {
                            JSONObject contentObj = rb.getJSONObject("content");
                            String mediaType = contentObj.has("application/json")
                                    ? "application/json"
                                    : contentObj.keys().hasNext()
                                        ? contentObj.keys().next() : null;
                            if (mediaType != null) {
                                req.setContentType(mediaType);
                                req.addHeader("Content-Type", mediaType);
                                JSONObject media = contentObj.getJSONObject(mediaType);
                                if (media.has("example")) {
                                    req.setBodyData(media.get("example").toString());
                                } else if (media.has("schema")) {
                                    req.setBodyData(
                                            schemaToSampleJson(
                                                media.getJSONObject("schema"), root)
                                            .toString());
                                }
                            }
                        }
                    }
                } else {
                    // Swagger 2.0 – body parameter
                    for (int i = 0; i < params.length(); i++) {
                        JSONObject p = params.getJSONObject(i);
                        if ("body".equals(p.optString("in"))) {
                            req.setContentType("application/json");
                            req.addHeader("Content-Type", "application/json");
                            if (p.has("schema"))
                                req.setBodyData(
                                        schemaToSampleJson(
                                            p.getJSONObject("schema"), root)
                                        .toString());
                            break;
                        }
                    }
                    // consumes
                    JSONArray consumes = operation.optJSONArray("consumes");
                    if (consumes == null) consumes = root.optJSONArray("consumes");
                    if (consumes != null && consumes.length() > 0
                            && req.getContentType() == null) {
                        String ct = consumes.getString(0);
                        req.setContentType(ct);
                        req.addHeader("Content-Type", ct);
                    }
                }

                // ---- Security / auth headers ---------------------------
                JSONArray secRefs = operation.optJSONArray("security");
                if (secRefs == null) secRefs = root.optJSONArray("security");
                if (secRefs != null) appendSecurityHeaders(req, secRefs, root);

                requests.add(req);
            }
        }
        return requests;
    }

    // -----------------------------------------------------------------------
    // Helpers
    // -----------------------------------------------------------------------

    private void mergeParams(JSONArray target, JSONArray source) {
        for (int i = 0; i < source.length(); i++) target.put(source.get(i));
    }

    private String extractDefaultValue(JSONObject param) {
        if (param.has("default")) return param.get("default").toString();
        if (param.has("example")) return param.get("example").toString();
        if (param.has("schema")) {
            JSONObject s = param.getJSONObject("schema");
            if (s.has("default")) return s.get("default").toString();
            if (s.has("example")) return s.get("example").toString();
            return exampleForType(s.optString("type", "string"));
        }
        return exampleForType(param.optString("type", "string"));
    }

    private String exampleForType(String type) {
        switch (type) {
            case "integer": case "number": return "1";
            case "boolean": return "true";
            case "array":   return "[]";
            case "object":  return "{}";
            default:        return "string";
        }
    }

    private Object schemaToSampleJson(JSONObject schema, JSONObject root) {
        if (schema.has("$ref")) {
            JSONObject resolved = resolveRef(schema.getString("$ref"), root);
            if (resolved != null) return schemaToSampleJson(resolved, root);
        }
        String type = schema.optString("type", "object");
        switch (type) {
            case "string":  return schema.opt("example") != null ? schema.get("example")
                                   : schema.opt("default") != null ? schema.get("default") : "string";
            case "integer": case "number":
                return schema.opt("example") != null ? schema.get("example") : 0;
            case "boolean": return schema.opt("example") != null ? schema.get("example") : false;
            case "array": {
                JSONArray arr = new JSONArray();
                if (schema.has("items")) arr.put(schemaToSampleJson(schema.getJSONObject("items"), root));
                return arr;
            }
            default: {
                if (schema.opt("example") != null) return schema.get("example");
                JSONObject obj   = new JSONObject();
                JSONObject props = schema.optJSONObject("properties");
                if (props != null)
                    for (Iterator<String> k = props.keys(); k.hasNext(); ) {
                        String key = k.next();
                        obj.put(key, schemaToSampleJson(props.getJSONObject(key), root));
                    }
                return obj;
            }
        }
    }

    private JSONObject resolveRef(String ref, JSONObject root) {
        if (!ref.startsWith("#/")) return null;
        String[] parts = ref.substring(2).split("/");
        JSONObject cur = root;
        for (String p : parts) {
            if (!cur.has(p)) return null;
            Object next = cur.get(p);
            if (next instanceof JSONObject) cur = (JSONObject) next;
            else return null;
        }
        return cur;
    }

    private void appendSecurityHeaders(RequestModel req, JSONArray secRefs,
                                       JSONObject root) {
        JSONObject secDefs = null;
        if (root.has("securityDefinitions"))
            secDefs = root.getJSONObject("securityDefinitions");
        else if (root.has("components")
                && root.getJSONObject("components").has("securitySchemes"))
            secDefs = root.getJSONObject("components").getJSONObject("securitySchemes");

        if (secDefs == null) return;
        for (int i = 0; i < secRefs.length(); i++) {
            JSONObject secRef = secRefs.getJSONObject(i);
            for (Iterator<String> k = secRef.keys(); k.hasNext(); ) {
                String schemeName = k.next();
                if (!secDefs.has(schemeName)) continue;
                JSONObject scheme = secDefs.getJSONObject(schemeName);
                String sType = scheme.optString("type", "");
                if ("apiKey".equals(sType) && "header".equals(scheme.optString("in")))
                    req.addHeader(scheme.optString("name", "X-API-Key"), "<api-key>");
                else if ("http".equals(sType) || "oauth2".equals(sType))
                    req.addHeader("Authorization", "Bearer <token>");
                else if ("basic".equals(sType))
                    req.addHeader("Authorization", "Basic <base64-credentials>");
            }
        }
    }

    private String stripYamlComments(String yaml) {
        String[] rawLines = yaml.split("\n");
        StringBuilder sb = new StringBuilder();
        for (String line : rawLines) {
            int hashIdx = line.indexOf('#');
            if (hashIdx >= 0) {
                long quotesBefore = line.substring(0, hashIdx).chars()
                        .filter(c -> c == '"' || c == '\'').count();
                if (quotesBefore % 2 == 0) line = line.substring(0, hashIdx);
            }
            sb.append(line).append("\n");
        }
        return sb.toString();
    }

    private ServerInfo parseServerUrl(String url) {
        ServerInfo si = new ServerInfo();
        if (url == null || url.isEmpty()) return si;
        url = url.replaceAll("\\{[^}]+\\}", "localhost");
        try {
            java.net.URL parsed = new java.net.URL(url);
            si.protocol = parsed.getProtocol();
            si.host     = parsed.getHost();
            si.port     = parsed.getPort() < 0 ? 0 : parsed.getPort();
            si.basePath = parsed.getPath();
        } catch (Exception ignored) {}
        return si;
    }

    private static class ServerInfo {
        String protocol = "http";
        String host     = "localhost";
        int    port     = 0;
        String basePath = "";
    }
}
