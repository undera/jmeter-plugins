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

public class ProjectTest {
    @org.junit.Test
    public void testFlow() throws Exception {
        StatusNotifierCallbackTest notifier = new StatusNotifierCallbackTest();
        HttpBaseEntity emul = new HttpBaseEntity(notifier, "test_address", "test_data_address", "test_id", false);

        JSONObject result = new JSONObject();
        result.put("id", "100");
        result.put("name", "NEW_TEST");
        JSONObject response = new JSONObject();
        response.put("result", result);

        ProjectExt project = new ProjectExt(emul, "10", "projectName");
        project.addEmul(response);
        Test test = project.createTest("NEW_WORKSPACE");
        assertEquals("100", test.getId());
        assertEquals("NEW_TEST", test.getName());

        response.clear();
        JSONArray results = new JSONArray();
        results.add(result);
        results.add(result);
        response.put("result", results);
        project.addEmul(response);

        List<Test> tests = project.getTests();
        assertEquals(2, tests.size());
        for (Test t :tests) {
            assertEquals("100", t.getId());
            assertEquals("NEW_TEST", t.getName());
        }
    }

    @org.junit.Test
    public void testFromJSON() throws Exception {
        StatusNotifierCallbackTest notifier = new StatusNotifierCallbackTest();
        HttpBaseEntity emul = new HttpBaseEntity(notifier, "test_address", "test_data_address", "test_id", false);
        JSONObject object = new JSONObject();
        object.put("id", "projectId");
        object.put("name", "projectName");
        Project project = Project.fromJSON(emul, object);
        assertEquals("projectId", project.getId());
        assertEquals("projectName", project.getName());
        assertEquals("test_address", project.getAddress());
        assertEquals("test_data_address", project.getDataAddress());
        assertEquals(notifier, project.getNotifier());
    }

    protected static class ProjectExt extends Project {
        private LinkedList<JSON> responses = new LinkedList<>();

        public ProjectExt(HttpBaseEntity entity, String id, String name) {
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