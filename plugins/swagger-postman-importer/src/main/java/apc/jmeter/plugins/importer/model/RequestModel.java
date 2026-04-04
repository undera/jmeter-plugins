package kg.apc.jmeter.plugins.importer.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Unified model representing a single HTTP request extracted from
 * a Swagger/OpenAPI spec or a Postman collection.
 */
public class RequestModel {

    private String name;
    private String protocol;    // http or https
    private String host;        // server hostname or IP
    private int    port;        // 0 = use default for the protocol
    private String method;      // GET, POST, PUT, DELETE, PATCH, etc.
    private String path;        // URL path, e.g. /api/users/{id}
    private String bodyData;    // raw body (JSON, form-encoded, etc.)
    private String contentType;
    private List<HeaderEntry> headers     = new ArrayList<>();
    private List<ParamEntry>  queryParams = new ArrayList<>();
    private String description;

    /* ------------------------------------------------------------------ */
    /*  Inner value types                                                   */
    /* ------------------------------------------------------------------ */

    public static class HeaderEntry {
        private final String name;
        private final String value;
        public HeaderEntry(String name, String value) { this.name = name; this.value = value; }
        public String getName()  { return name;  }
        public String getValue() { return value; }
    }

    public static class ParamEntry {
        private final String name;
        private final String value;
        public ParamEntry(String name, String value) { this.name = name; this.value = value; }
        public String getName()  { return name;  }
        public String getValue() { return value; }
    }

    /* ------------------------------------------------------------------ */
    /*  Getters / Setters                                                   */
    /* ------------------------------------------------------------------ */

    public String getName()                    { return name; }
    public void   setName(String name)         { this.name = name; }

    public String getProtocol()                { return protocol; }
    public void   setProtocol(String protocol) { this.protocol = protocol; }

    public String getHost()                    { return host; }
    public void   setHost(String host)         { this.host = host; }

    public int    getPort()                    { return port; }
    public void   setPort(int port)            { this.port = port; }

    public String getMethod()                  { return method; }
    public void   setMethod(String method)     { this.method = method; }

    public String getPath()                    { return path; }
    public void   setPath(String path)         { this.path = path; }

    public String getBodyData()                { return bodyData; }
    public void   setBodyData(String bodyData) { this.bodyData = bodyData; }

    public String getContentType()                   { return contentType; }
    public void   setContentType(String contentType) { this.contentType = contentType; }

    public List<HeaderEntry> getHeaders()             { return headers; }
    public void setHeaders(List<HeaderEntry> headers) { this.headers = headers; }
    public void addHeader(String name, String value)  { headers.add(new HeaderEntry(name, value)); }

    public List<ParamEntry> getQueryParams()              { return queryParams; }
    public void setQueryParams(List<ParamEntry> params)   { this.queryParams = params; }
    public void addQueryParam(String name, String value)  { queryParams.add(new ParamEntry(name, value)); }

    public String getDescription()                   { return description; }
    public void   setDescription(String description) { this.description = description; }

    @Override
    public String toString() {
        return method + " " + protocol + "://" + host +
               (port > 0 ? ":" + port : "") + path + "  [" + name + "]";
    }
}
