package com.blazemeter.api.explorer;

import com.blazemeter.api.explorer.base.HttpBaseEntity;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Project extends HttpBaseEntity {

    public static final String DEFAULT_PROJECT = "Default project";

    public Project(HttpBaseEntity entity, String id, String name) {
        super(entity, id, name);
    }

    /**
     * Create Test in current Project
     * @param name - title of the new Test
     */
    public Test createTest(String name) throws IOException {
        String uri = address + "/api/v4/tests";
        JSONObject data = new JSONObject();
        data.put("name", name);
        data.put("projectId", getId());
        data.put("configuration", "{\"type\": \"external\"}");
        JSONObject response = queryObject(createPost(uri, data.toString()), 201);
        return Test.fromJSON(this, response.getJSONObject("result"));
    }

    /**
     * @return list of Tests in current Project
     */
    public List<Test> getTests() throws IOException {
        String uri = address + "/api/v4/tests?projectId=" + getId();
        JSONObject response = queryObject(createGet(uri), 200);
        return extractTests(response.getJSONArray("result"));
    }

    private List<Test> extractTests(JSONArray result) {
        List<Test> accounts = new ArrayList<>();

        for (Object obj : result) {
            accounts.add(Test.fromJSON(this, (JSONObject) obj));
        }

        return accounts;
    }

    public static Project fromJSON(HttpBaseEntity entity, JSONObject obj) {
        return new Project(entity, obj.getString("id"), obj.getString("name"));
    }
}
