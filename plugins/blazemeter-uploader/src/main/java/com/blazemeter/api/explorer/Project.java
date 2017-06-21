package com.blazemeter.api.explorer;

import com.blazemeter.api.entity.BlazemeterReport;
import com.blazemeter.api.entity.Test;
import com.blazemeter.jmeter.StatusNotifierCallback;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Project extends BaseEntity {

    public static final String DEFAULT_PROJECT = "Default project";

    public Project(StatusNotifierCallback notifier, String address, String dataAddress, BlazemeterReport report, String id, String name) {
        super(notifier, address, dataAddress, report, id, name);
    }

    public Test createTest(String name) throws IOException {
        String uri = address + "/api/v4/tests";
        JSONObject data = new JSONObject();
        data.put("name", name);
        data.put("projectId", getId());
        data.put("configuration", "{\"type\": \"external\"}");
        JSONObject response = queryObject(createPost(uri, data.toString()), 201);
        System.out.println();
        return null;
    }

    public List<Test> getTests() throws IOException {
        String uri = address + String.format("/api/v4/tests?projectId=%s", getId());
        if (getName() != null && !getName().isEmpty()) {
            uri += String.format("&name=%s", getName());
        }
        JSONObject response = queryObject(createGet(uri), 200);
        return extractTests(response.getJSONArray("result"));
    }

    private List<Test> extractTests(JSONArray result) {
        List<Test> accounts = new ArrayList<>();

        for (Object obj : result) {
            accounts.add(convertToTest((JSONObject) obj));
        }

        return accounts;
    }

    private Test convertToTest(JSONObject obj) {
        return new Test(obj.getString("id"), obj.getString("name"));
    }

}
