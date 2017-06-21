package com.blazemeter.api.explorer;

import com.blazemeter.api.entity.BlazemeterReport;
import com.blazemeter.jmeter.StatusNotifierCallback;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Account extends AbstractHttpEntity {

    private final String id;

    public Account(StatusNotifierCallback notifier, String address, String dataAddress, BlazemeterReport report, String id) {
        super(notifier, address, dataAddress, report);
        this.id = id;
    }

    public List<Workspace> getWorkspaces() throws IOException {
        String uri = address + String.format("/api/v4/workspaces?accountId=%s&enabled=true&limit=100", id);
        JSONObject response = queryObject(createGet(uri), 200);
        return extractWorkspaces(response.getJSONArray("result"));
    }

    private List<Workspace> extractWorkspaces(JSONArray result) {
        List<Workspace> workspaces = new ArrayList<>();

        for (Object obj : result) {
            workspaces.add(convertToWorkspace((JSONObject) obj));
        }

        return workspaces;
    }

    private Workspace convertToWorkspace(JSONObject obj) {
        return new Workspace(notifier, address, dataAddress, report, obj.getString("id"), obj.getString("name"));
    }

    public Workspace createWorkspace(String workspace) {
        // TODO:
        return null;
    }
}
