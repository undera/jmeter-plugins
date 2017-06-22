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

    public void startExternal() throws IOException {
        JSONObject result = startTest(address + String.format("/api/v4/tests/%s/start-external", getId()), 202);
        fillFields(result);
    }

    public String startAnonymousExternal() throws IOException {
        JSONObject result = startTest(address + "/api/v4/sessions", 201);
        setTestFields(result.getJSONObject("test"));
        reportURL = result.getString("publicTokenUrl");
        fillFields(result);
        return reportURL;
    }

    private void fillFields(JSONObject result) {
        this.signature = result.getString("signature");
        this.session = Session.fromJSON(this, getId(), signature, result.getJSONObject("session"));
        this.master = Master.fromJSON(this, result.getJSONObject("master"));
    }

    private JSONObject startTest(String uri, int expectedRC) throws IOException {
        JSONObject response = queryObject(createPost(uri, ""), expectedRC);
        return response.getJSONObject("result");
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