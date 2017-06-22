package com.blazemeter.api.explorer;

import com.blazemeter.api.explorer.base.HttpBaseEntity;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Account extends HttpBaseEntity {

    public Account(HttpBaseEntity entity, String id, String name) {
        super(entity, id, name);
    }

    /**
     * Create Workspace in current Account
     * @param name - Name of the new Workspace
     */
    public Workspace createWorkspace(String name) throws IOException {
        String uri = address + "/api/v4/workspaces";
        JSONObject data = new JSONObject();
        data.put("name", name);
        data.put("accountId", getId());
        JSONObject response = queryObject(createPost(uri, data.toString()), 201);
        return Workspace.fromJSON(this, response.getJSONObject("result"));
    }

    /**
     * @return list of Workspace in current Account
     */
    public List<Workspace> getWorkspaces() throws IOException {
        String uri = address + String.format("/api/v4/workspaces?accountId=%s&enabled=true&limit=100", getId());
        JSONObject response = queryObject(createGet(uri), 200);
        return extractWorkspaces(response.getJSONArray("result"));
    }

    private List<Workspace> extractWorkspaces(JSONArray result) {
        List<Workspace> workspaces = new ArrayList<>();

        for (Object obj : result) {
            workspaces.add(Workspace.fromJSON(this, (JSONObject) obj));
        }

        return workspaces;
    }

    public static Account fromJSON(HttpBaseEntity entity, JSONObject obj) {
        return new Account(entity, obj.getString("id"), obj.getString("name"));
    }
}
