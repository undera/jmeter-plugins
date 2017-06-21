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

    public Workspace createWorkspace(String name) {
        // TODO:
        return null;
    }

    public static Account fromJSON(HttpBaseEntity entity, JSONObject obj) {
        return new Account(entity, obj.getString("id"), obj.getString("name"));
    }
}
