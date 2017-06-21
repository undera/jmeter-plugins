package com.blazemeter.api.explorer;

import com.blazemeter.api.entity.BlazemeterReport;
import com.blazemeter.jmeter.StatusNotifierCallback;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Workspace extends BaseEntity {

    public static final String DEFAULT_WORKSPACE = "Default workspace";


    public Workspace(StatusNotifierCallback notifier, String address, String dataAddress, BlazemeterReport report, String id, String name) {
        super(notifier, address, dataAddress, report, id, name);
    }

    public List<Project> getProjects() throws IOException {
        String uri = address + String.format("/api/v4/projects?workspaceId=%s&limit=99999", getId());
        JSONObject response = queryObject(createGet(uri), 200);
        return extractProjects(response.getJSONArray("result"));
    }

    private List<Project> extractProjects(JSONArray result) {
        List<Project> projects = new ArrayList<>();

        for (Object obj : result) {
            projects.add(convertToProject((JSONObject) obj));
        }

        return projects;
    }

    private Project convertToProject(JSONObject obj) {
        return new Project(notifier, address, dataAddress, report, obj.getString("id"), obj.getString("name"));
    }

    public Project createProject(String name) {
        return null;
    }
}
