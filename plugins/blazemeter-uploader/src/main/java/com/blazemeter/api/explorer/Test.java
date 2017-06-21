package com.blazemeter.api.explorer;


import com.blazemeter.api.explorer.base.HttpBaseEntity;
import net.sf.json.JSONObject;

import java.io.IOException;

public class Test extends HttpBaseEntity {

    public static final String DEFAULT_TEST = "Default test";

    private Session session;
    private Master master;
    private String signature;
    private String reportURL;

    public Test(HttpBaseEntity entity) {
        super(entity, "", "");
    }

    public Test(HttpBaseEntity entity, String id, String name) {
        super(entity, id, name);
    }

    public String startExternal() {
        return "";
    }

    public String startAnonymousExternal() throws IOException {
        String uri = address + "/api/v4/sessions";
        JSONObject response = queryObject(createPost(uri, ""), 201);
        JSONObject result = response.getJSONObject("result");
        setTestFields(result.getJSONObject("test"));
        this.signature = result.getString("signature");
        this.session = Session.fromJSON(this, getId(), signature, result.getJSONObject("session"));
        this.master = Master.fromJSON(this, result.getJSONObject("master"));
        return reportURL = result.getString("publicTokenUrl");
    }

    private void setTestFields(JSONObject obj) {
        setId(obj.getString("id"));
        setName(obj.getString("name"));
    }

    public Session getSession() {
        return session;
    }

    public Master getMaster() {
        return master;
    }

    public String getSignature() {
        return signature;
    }

    public String getReportURL() {
        return reportURL;
    }

    public static Test fromJSON(HttpBaseEntity entity, JSONObject obj) {
        return new Test(entity, obj.getString("id"), obj.getString("name"));
    }
}