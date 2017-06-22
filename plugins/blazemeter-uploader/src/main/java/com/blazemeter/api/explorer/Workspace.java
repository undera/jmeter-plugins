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

    /**
     * Create Project in current Workspace
     * @param name - Name of the new project
     */
    public Project createProject(String name) throws IOException {
        String uri = address + "/api/v4/projects";
        JSONObject data = new JSONObject();
        data.put("name", name);
        data.put("workspaceId", getId());
        JSONObject response = queryObject(createPost(uri, data.toString()), 201);
        return Project.fromJSON(this, response.getJSONObject("result"));
    }

    /**
     * @return list of Projects in current workspace
     */
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

    public static Workspace fromJSON(HttpBaseEntity entity, JSONObject obj) {
        return new Workspace(entity, obj.getString("id"), obj.getString("name"));
    }
}
