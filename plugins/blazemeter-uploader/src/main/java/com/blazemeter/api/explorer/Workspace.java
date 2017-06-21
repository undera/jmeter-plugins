package com.blazemeter.api.explorer;

import com.blazemeter.api.entity.BlazemeterReport;
import com.blazemeter.jmeter.StatusNotifierCallback;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Workspace extends AbstractHttpEntity {

    private String id;
    private String name;

    public Workspace(StatusNotifierCallback notifier, String address, String dataAddress, BlazemeterReport report, String id, String name) {
        super(notifier, address, dataAddress, report);
        this.id = id;
        this.name = name;
    }

    public List<Project> getProjects() throws IOException {
        String uri = address + String.format("/api/v4/projects?workspaceId=%s&limit=99999", id);
        JSONObject response = queryObject(createGet(uri), 200);
        return extractProjects(response.getJSONArray("result"));
    }

    private List<Project> extractProjects(JSONArray result) {
        List<Project> projects = new ArrayList<>();

        for (Object obj : result) {
            projects.add(createProject((JSONObject) obj));
        }

        return projects;
    }

    private Project createProject(JSONObject obj) {
        return new Project();
//        return new Project(notifier, address, dataAddress, report, obj.getString("id"), obj.getString("name"));
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
