package com.blazemeter.api.explorer;

import com.blazemeter.api.explorer.base.HttpBaseEntity;
import com.blazemeter.jmeter.StatusNotifierCallbackTest;
import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.http.client.methods.HttpRequestBase;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;

public class WorkspaceTest {
    @org.junit.Test
    public void testFlow() throws Exception {
        StatusNotifierCallbackTest notifier = new StatusNotifierCallbackTest();
        HttpBaseEntity emul = new HttpBaseEntity(notifier, "test_address", "test_data_address", "test_id", false);

        JSONObject result = new JSONObject();
        result.put("id", "999");
        result.put("name", "NEW_PROJECT");
        JSONObject response = new JSONObject();
        response.put("result", result);

        WorkspaceExt workspace = new WorkspaceExt(emul, "888", "workspace_name");
        workspace.addEmul(response);
        Project project = workspace.createProject("NEW_PROJECT");
        assertEquals("999", project.getId());
        assertEquals("NEW_PROJECT", project.getName());

        response.clear();
        JSONArray results = new JSONArray();
        results.add(result);
        results.add(result);
        response.put("result", results);
        workspace.addEmul(response);

        List<Project> projects = workspace.getProjects();
        assertEquals(2, projects.size());
        for (Project p :projects) {
            assertEquals("999", p.getId());
            assertEquals("NEW_PROJECT", p.getName());
        }
    }

    @org.junit.Test
    public void testFromJSON() throws Exception {
        StatusNotifierCallbackTest notifier = new StatusNotifierCallbackTest();
        HttpBaseEntity emul = new HttpBaseEntity(notifier, "test_address", "test_data_address", "test_id", false);
        JSONObject object = new JSONObject();
        object.put("id", "workspaceId");
        object.put("name", "workspaceName");
        Workspace workspace = Workspace.fromJSON(emul, object);
        assertEquals("workspaceId", workspace.getId());
        assertEquals("workspaceName", workspace.getName());
        assertEquals("test_address", workspace.getAddress());
        assertEquals("test_data_address", workspace.getDataAddress());
        assertEquals(notifier, workspace.getNotifier());
    }

    protected static class WorkspaceExt extends Workspace {
        private LinkedList<JSON> responses = new LinkedList<>();

        public WorkspaceExt(HttpBaseEntity entity, String id, String name) {
            super(entity, id, name);
        }


        public void addEmul(JSON response) {
            responses.add(response);
        }

        @Override
        protected JSON query(HttpRequestBase request, int expectedCode) throws IOException {
            log.info("Simulating request: " + request);
            if (responses.size()>0) {
                JSON resp = responses.remove();
                log.info("Response: " + resp);
                return resp;
            } else {
                throw new IOException("No responses to emulate");
            }
        }
        @Override
        protected JSONObject queryObject(HttpRequestBase request, int expectedCode) throws IOException {
            log.info("Simulating request: " + request);
            if (responses.size()>0) {
                JSON resp = responses.remove();
                log.info("Response: " + resp);
                return (JSONObject) resp;
            } else {
                throw new IOException("No responses to emulate");
            }
        }
    }
}