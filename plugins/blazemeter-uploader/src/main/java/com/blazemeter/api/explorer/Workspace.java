package com.blazemeter.api.explorer;

import com.blazemeter.api.explorer.base.HttpBaseEntity;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Workspace extends HttpBaseEntity {

    public static final String DEFAULT_WORKSPACE = "Default workspace";


    public Workspace(HttpBaseEntity entity, String id, String name) {
        super(entity, id, name);
    }

    public List<Project> getProjects() throws IOException {
        String uri = address + String.format("/api/v4/projects?workspaceId=%s&limit=99999", getId());
        JSONObject response = queryObject(createGet(uri), 200);
        return extractProjects(response.getJSONArray("result"));
    }

    private List<Project> extractProjects(JSONArray result) {
        List<Project> projects = new ArrayList<>();

        for (Object obj : result) {
            projects.add(Project.fromJSON(this, (JSONObject) obj));
        }

        return projects;
    }


    public Project createProject(String name) {
        return null;
    }

    public static Workspace fromJSON(HttpBaseEntity entity, JSONObject obj) {
        return new Workspace(entity, obj.getString("id"), obj.getString("name"));
    }
}
