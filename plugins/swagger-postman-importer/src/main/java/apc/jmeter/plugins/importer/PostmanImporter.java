package kg.apc.jmeter.plugins.importer;

import kg.apc.jmeter.plugins.importer.model.RequestModel;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Parses Postman Collection v2.0 and v2.1 JSON files and returns a list of
 * {@link RequestModel} objects.
 *
 * <p>Captures per request: protocol, host, port, method, path,
 * headers, query params, body (raw, form-encoded, multipart, GraphQL),
 * and auth (Bearer, Basic, API Key, OAuth2) – with folder/collection
 * auth inheritance.</p>
 */
public class PostmanImporter {

    /**
     * Parse a Postman collection JSON file.
     *
     * @param filePath absolute path to the Postman JSON export
     * @return list of parsed {@link RequestModel} instances
     * @throws IOException on file-read errors
     */
    public List<RequestModel> parse(String filePath) throws IOException {
        String content = new String(Files.readAllBytes(Paths.get(filePath)));
        JSONObject root = new JSONObject(content);
        List<RequestModel> requests = new ArrayList<>();

        CollectionContext ctx = new CollectionContext();
        if (root.has("auth"))     ctx.collectionAuth = root.getJSONObject("auth");
        if (root.has("variable")) ctx.variables      = root.getJSONArray("variable");

        if (!root.has("item")) return requests;
        walkItems(root.getJSONArray("item"), ctx, requests);
        return requests;
    }

    // -----------------------------------------------------------------------
    // Item tree walker
    // -----------------------------------------------------------------------

    private void walkItems(JSONArray items, CollectionContext parentCtx,
                           List<RequestModel> results) {
        for (int i = 0; i < items.length(); i++) {
            JSONObject item = items.getJSONObject(i);
            if (item.has("item")) {
                walkItems(item.getJSONArray("item"), parentCtx.inherit(item), results);
            } else if (item.has("request")) {
                RequestModel req = parseRequest(item, parentCtx);
                if (req != null) results.add(req);
            }
        }
    }

    // -----------------------------------------------------------------------
    // Single request parser
    // -----------------------------------------------------------------------

    private RequestModel parseRequest(JSONObject item, CollectionContext ctx) {
        JSONObject postmanReq = item.optJSONObject("request");
        if (postmanReq == null) return null;

        RequestModel req = new RequestModel();
        req.setName(item.optString("name", "Request"));
        req.setDescription(postmanReq.optString("description", ""));

        // ---- URL -----------------------------------------------------------
        Object urlObj = postmanReq.opt("url");
        UrlInfo url;
        if (urlObj instanceof JSONObject)
            url = parseUrlObject((JSONObject) urlObj, ctx);
        else
            url = parseUrlString(ctx.resolve(urlObj != null ? urlObj.toString() : ""), ctx);

        req.setProtocol(url.protocol);
        req.setHost(url.host);
        req.setPort(url.port);
        req.setPath(url.path);
        for (RequestModel.ParamEntry qp : url.queryParams) req.addQueryParam(qp.getName(), qp.getValue());

        // ---- Method --------------------------------------------------------
        req.setMethod(postmanReq.optString("method", "GET").toUpperCase());

        // ---- Headers -------------------------------------------------------
        if (postmanReq.has("header")) {
            JSONArray headers = toArray(postmanReq.get("header"));
            for (int h = 0; h < headers.length(); h++) {
                Object hObj = headers.get(h);
                if (hObj instanceof JSONObject) {
                    JSONObject he = (JSONObject) hObj;
                    if (!he.optBoolean("disabled", false))
                        req.addHeader(ctx.resolve(he.optString("key", "")),
                                      ctx.resolve(he.optString("value", "")));
                }
            }
        }

        // ---- Body ----------------------------------------------------------
        if (postmanReq.has("body")) {
            JSONObject body = postmanReq.getJSONObject("body");
            String mode = body.optString("mode", "raw");
            switch (mode) {
                case "raw": {
                    String rawBody = ctx.resolve(body.optString("raw", ""));
                    req.setBodyData(rawBody);
                    String ct = null;
                    if (body.has("options")) {
                        JSONObject opts = body.optJSONObject("options");
                        if (opts != null && opts.has("raw")) {
                            String lang = opts.getJSONObject("raw").optString("language", "text");
                            ct = langToContentType(lang);
                        }
                    }
                    if (ct == null) ct = headerContentType(req);
                    if (ct == null) ct = "application/json";
                    req.setContentType(ct);
                    if (!hasHeader(req, "Content-Type")) req.addHeader("Content-Type", ct);
                    break;
                }
                case "urlencoded": {
                    req.setContentType("application/x-www-form-urlencoded");
                    if (!hasHeader(req, "Content-Type"))
                        req.addHeader("Content-Type", "application/x-www-form-urlencoded");
                    if (body.has("urlencoded")) {
                        req.setBodyData(buildFormBody(toArray(body.get("urlencoded")), ctx));
                    }
                    break;
                }
                case "formdata": {
                    req.setContentType("multipart/form-data");
                    if (!hasHeader(req, "Content-Type"))
                        req.addHeader("Content-Type", "multipart/form-data");
                    if (body.has("formdata")) {
                        req.setBodyData(buildFormBody(toArray(body.get("formdata")), ctx));
                    }
                    break;
                }
                case "graphql": {
                    req.setContentType("application/json");
                    if (!hasHeader(req, "Content-Type"))
                        req.addHeader("Content-Type", "application/json");
                    JSONObject gqlBody = new JSONObject();
                    gqlBody.put("query", ctx.resolve(body.optString("graphql", "")));
                    req.setBodyData(gqlBody.toString());
                    break;
                }
                default:
                    break;
            }
        }

        // ---- Auth ----------------------------------------------------------
        JSONObject auth = postmanReq.optJSONObject("auth");
        if (auth == null) auth = ctx.effectiveAuth();
        if (auth != null) applyAuth(req, auth, ctx);

        return req;
    }

    // -----------------------------------------------------------------------
    // URL parsing
    // -----------------------------------------------------------------------

    private UrlInfo parseUrlObject(JSONObject urlNode, CollectionContext ctx) {
        UrlInfo info = new UrlInfo();
        info.protocol = urlNode.optString("protocol", "http");
        if (info.protocol.isEmpty()) info.protocol = "http";

        Object hostObj = urlNode.opt("host");
        if (hostObj instanceof JSONArray) {
            JSONArray ha = (JSONArray) hostObj;
            StringBuilder hb = new StringBuilder();
            for (int i = 0; i < ha.length(); i++) {
                if (i > 0) hb.append('.');
                hb.append(ctx.resolve(ha.getString(i)));
            }
            info.host = hb.toString();
        } else {
            info.host = hostObj != null ? ctx.resolve(hostObj.toString()) : "localhost";
        }

        String portStr = ctx.resolve(urlNode.optString("port", ""));
        info.port = parsePort(portStr);

        Object pathObj = urlNode.opt("path");
        if (pathObj instanceof JSONArray) {
            JSONArray pa = (JSONArray) pathObj;
            StringBuilder pb = new StringBuilder();
            for (int i = 0; i < pa.length(); i++) {
                Object seg = pa.get(i);
                String s = ctx.resolve(seg instanceof JSONObject
                        ? ((JSONObject) seg).optString("value", seg.toString())
                        : seg.toString());
                if (s.startsWith(":")) s = "{" + s.substring(1) + "}";
                pb.append('/').append(s);
            }
            info.path = pb.length() > 0 ? pb.toString() : "/";
        } else if (pathObj != null) {
            info.path = ctx.resolve(pathObj.toString());
            if (!info.path.startsWith("/")) info.path = "/" + info.path;
        }

        if (urlNode.has("query")) {
            JSONArray qArr = toArray(urlNode.get("query"));
            for (int q = 0; q < qArr.length(); q++) {
                Object qo = qArr.get(q);
                if (qo instanceof JSONObject) {
                    JSONObject qp = (JSONObject) qo;
                    if (!qp.optBoolean("disabled", false))
                        info.queryParams.add(new RequestModel.ParamEntry(
                                ctx.resolve(qp.optString("key", "")),
                                ctx.resolve(qp.optString("value", ""))));
                }
            }
        }
        return info;
    }

    private UrlInfo parseUrlString(String raw, CollectionContext ctx) {
        UrlInfo info = new UrlInfo();
        if (raw == null || raw.isEmpty()) return info;
        int qIdx = raw.indexOf('?');
        String queryString = "";
        if (qIdx >= 0) {
            queryString = raw.substring(qIdx + 1);
            raw = raw.substring(0, qIdx);
        }
        for (String pair : queryString.isEmpty() ? new String[0] : queryString.split("&")) {
            int eq = pair.indexOf('=');
            info.queryParams.add(eq >= 0
                    ? new RequestModel.ParamEntry(pair.substring(0, eq), pair.substring(eq + 1))
                    : new RequestModel.ParamEntry(pair, ""));
        }
        try {
            java.net.URL parsed = new java.net.URL(raw);
            info.protocol = parsed.getProtocol();
            info.host     = parsed.getHost();
            info.port     = parsed.getPort() < 0 ? 0 : parsed.getPort();
            info.path     = parsed.getPath().isEmpty() ? "/" : parsed.getPath();
        } catch (Exception e) {
            info.path = raw.startsWith("/") ? raw : "/" + raw;
        }
        return info;
    }

    // -----------------------------------------------------------------------
    // Auth
    // -----------------------------------------------------------------------

    private void applyAuth(RequestModel req, JSONObject auth, CollectionContext ctx) {
        String type = auth.optString("type", "");
        switch (type) {
            case "bearer": {
                String token = extractAuthParam(auth, "bearer", "token", ctx);
                if (!hasHeader(req, "Authorization"))
                    req.addHeader("Authorization", "Bearer " + token);
                break;
            }
            case "basic": {
                String user = extractAuthParam(auth, "basic", "username", ctx);
                String pass = extractAuthParam(auth, "basic", "password", ctx);
                if (!hasHeader(req, "Authorization")) {
                    String cred = java.util.Base64.getEncoder()
                            .encodeToString((user + ":" + pass).getBytes());
                    req.addHeader("Authorization", "Basic " + cred);
                }
                break;
            }
            case "apikey": {
                String key   = extractAuthParam(auth, "apikey", "key",   ctx);
                String value = extractAuthParam(auth, "apikey", "value", ctx);
                String in    = extractAuthParam(auth, "apikey", "in",    ctx);
                if ("query".equalsIgnoreCase(in)) req.addQueryParam(key, value);
                else if (!hasHeader(req, key))    req.addHeader(key, value);
                break;
            }
            case "oauth2": {
                String token = extractAuthParam(auth, "oauth2", "accessToken", ctx);
                if (!hasHeader(req, "Authorization"))
                    req.addHeader("Authorization", "Bearer " + token);
                break;
            }
            default:
                break;
        }
    }

    private String extractAuthParam(JSONObject auth, String typeName,
                                    String paramKey, CollectionContext ctx) {
        if (!auth.has(typeName)) return "<" + paramKey + ">";
        JSONArray params = toArray(auth.get(typeName));
        for (int i = 0; i < params.length(); i++) {
            Object obj = params.get(i);
            if (obj instanceof JSONObject) {
                JSONObject p = (JSONObject) obj;
                if (paramKey.equals(p.optString("key")))
                    return ctx.resolve(p.optString("value", "<" + paramKey + ">"));
            }
        }
        return "<" + paramKey + ">";
    }

    // -----------------------------------------------------------------------
    // Utilities
    // -----------------------------------------------------------------------

    private String buildFormBody(JSONArray fields, CollectionContext ctx) {
        StringBuilder sb = new StringBuilder();
        for (int f = 0; f < fields.length(); f++) {
            Object fObj = fields.get(f);
            if (fObj instanceof JSONObject) {
                JSONObject fe = (JSONObject) fObj;
                if (!fe.optBoolean("disabled", false)) {
                    if (sb.length() > 0) sb.append("&");
                    sb.append(ctx.resolve(fe.optString("key", "")))
                      .append("=")
                      .append(ctx.resolve(fe.optString("value", "")));
                }
            }
        }
        return sb.toString();
    }

    private JSONArray toArray(Object obj) {
        return obj instanceof JSONArray ? (JSONArray) obj : new JSONArray();
    }

    private int parsePort(String portStr) {
        if (portStr == null || portStr.isEmpty()) return 0;
        try { return Integer.parseInt(portStr.trim()); }
        catch (NumberFormatException e) { return 0; }
    }

    private String langToContentType(String lang) {
        if (lang == null) return "text/plain";
        switch (lang.toLowerCase()) {
            case "json":       return "application/json";
            case "xml":        return "application/xml";
            case "html":       return "text/html";
            case "javascript": return "application/javascript";
            default:           return "text/plain";
        }
    }

    private String headerContentType(RequestModel req) {
        for (RequestModel.HeaderEntry h : req.getHeaders())
            if ("content-type".equalsIgnoreCase(h.getName())) return h.getValue();
        return null;
    }

    private boolean hasHeader(RequestModel req, String name) {
        for (RequestModel.HeaderEntry h : req.getHeaders())
            if (h.getName().equalsIgnoreCase(name)) return true;
        return false;
    }

    // -----------------------------------------------------------------------
    // Inner types
    // -----------------------------------------------------------------------

    private static class UrlInfo {
        String protocol = "http";
        String host     = "localhost";
        int    port     = 0;
        String path     = "/";
        List<RequestModel.ParamEntry> queryParams = new ArrayList<>();
    }

    private static class CollectionContext {
        JSONObject collectionAuth;
        JSONArray  variables;
        JSONObject folderAuth;

        CollectionContext inherit(JSONObject folder) {
            CollectionContext child = new CollectionContext();
            child.collectionAuth = this.collectionAuth;
            child.variables      = this.variables;
            child.folderAuth     = folder.optJSONObject("auth");
            if (child.folderAuth == null) child.folderAuth = this.folderAuth;
            return child;
        }

        JSONObject effectiveAuth() {
            return folderAuth != null ? folderAuth : collectionAuth;
        }

        String resolve(String s) {
            if (s == null || variables == null) return s;
            for (int i = 0; i < variables.length(); i++) {
                Object obj = variables.get(i);
                if (obj instanceof JSONObject) {
                    JSONObject v = (JSONObject) obj;
                    String k = v.optString("key", v.optString("id", ""));
                    s = s.replace("{{" + k + "}}", v.optString("value", ""));
                }
            }
            return s;
        }
    }
}
