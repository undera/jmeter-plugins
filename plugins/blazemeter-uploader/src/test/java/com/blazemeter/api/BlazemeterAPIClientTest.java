package com.blazemeter.api;

import com.blazemeter.api.data.JSONConverter;
import com.blazemeter.api.explorer.Account;
import com.blazemeter.api.explorer.Master;
import com.blazemeter.api.explorer.Project;
import com.blazemeter.api.explorer.Session;
import com.blazemeter.api.explorer.Test;
import com.blazemeter.api.explorer.User;
import com.blazemeter.api.explorer.Workspace;
import com.blazemeter.api.explorer.base.HttpBaseEntity;
import com.blazemeter.jmeter.StatusNotifierCallback;
import com.blazemeter.jmeter.StatusNotifierCallbackTest;
import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import net.sf.json.JsonConfig;
import org.apache.commons.io.FileUtils;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.jmeter.samplers.SampleResult;


import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class BlazemeterAPIClientTest {

    @org.junit.Test
    public void testStartAnonTest() throws Exception {
        StatusNotifierCallbackTest callback = new StatusNotifierCallbackTest();
        BlazemeterReport report = new BlazemeterReport();
        report.setAnonymousTest(true);
        BlazemeterAPIClient apiClient = new BlazemeterAPIClient(callback, "https://a.blazemeter.com/", "https://data.blazemeter.com/", report);
        apiClient.prepare();
        assertEquals(report, apiClient.getReport());
        String link = apiClient.startOnline();
        System.out.println(link);
        assertFalse(link.isEmpty());
        List<SampleResult> sampleResults = generateResults();
        apiClient.sendOnlineData(JSONConverter.convertToJSON(sampleResults, sampleResults));
        apiClient.endOnline();
    }

    public static List<SampleResult> generateResults() {
        List<SampleResult> list = new LinkedList<>();
        list.add(new SampleResult(System.currentTimeMillis(), 1));
        list.add(new SampleResult(System.currentTimeMillis() + 1000, 1));
        list.add(new SampleResult(System.currentTimeMillis() + 2000, 1));
        list.add(new SampleResult(System.currentTimeMillis() + 3000, 1));

        SampleResult res = new SampleResult(System.currentTimeMillis() + 3000, 3);
        res.setSampleLabel("L2");
        list.add(res);

        res = new SampleResult(System.currentTimeMillis() + 3000, 3);
        res.setSampleLabel("L2");
        list.add(res);

        res = new SampleResult(System.currentTimeMillis() + 5000, 2);
        res.setSampleLabel("L2");
        list.add(res);

        res = new SampleResult(System.currentTimeMillis() + 4000, 3);
        res.setSampleLabel("L2");
        list.add(res);
        return list;
    }

    @org.junit.Test
    public void testUserFlow() throws Exception {

        URL url = BlazemeterAPIClientTest.class.getResource("/responses.json");
        JSONArray jsonArray = (JSONArray) JSONSerializer.toJSON(FileUtils.readFileToString(new File(url.getPath())), new JsonConfig());


        StatusNotifierCallbackTest notifier = new StatusNotifierCallbackTest();
        BlazemeterReport report = new BlazemeterReport();
        report.setAnonymousTest(false);
        report.setShareTest(true);
        report.setWorkspace("New workspace");
        report.setProject("New project");
        report.setTitle("New test");
        report.setToken("123456");

        BlazemeterAPIClientExt apiClient = new BlazemeterAPIClientExt(notifier, "https://a.blazemeter.com/", "data_address", report, jsonArray);
        apiClient.prepare();


        // if share user test
        String linkPublic = apiClient.startOnline();
        System.out.println(linkPublic);
        assertFalse(linkPublic.isEmpty());
        assertEquals("https://a.blazemeter.com//app/?public-token=public_test_token#/masters/master_id/summary", linkPublic);

        // if private user test
        report.setShareTest(false);
        String linkPrivate = apiClient.startOnline();
        System.out.println(linkPrivate);
        assertFalse(linkPrivate.isEmpty());
        assertEquals("https://a.blazemeter.com//app/#/masters/master_id", linkPrivate);


        List<SampleResult> sampleResults = generateResults();
        apiClient.sendOnlineData(JSONConverter.convertToJSON(sampleResults, sampleResults));
        apiClient.endOnline();
    }

    protected static class BlazemeterAPIClientExt extends BlazemeterAPIClient {
        private LinkedList<JSON> responses = new LinkedList<>();

        public BlazemeterAPIClientExt(StatusNotifierCallback notifier, String address, String dataAddress, BlazemeterReport report, JSONArray responses) {
            super(notifier, address, dataAddress, report);
            UserExt user = new UserExt(this);
            for (Object resp : responses) {
                addEmul((JSON) resp);
            }
            this.user = user;

        }

        private class UserExt extends User {
            public UserExt(HttpBaseEntity entity) {
                super(entity);
            }

            @Override
            protected JSON query(HttpRequestBase request, int expectedCode) throws IOException {
                return getResponse(request);
            }

            @Override
            protected JSONObject queryObject(HttpRequestBase request, int expectedCode) throws IOException {
                return (JSONObject) getResponse(request);
            }

            @Override
            public List<Account> getAccounts() throws IOException {
                List<Account> accounts = super.getAccounts();
                List<Account> result = new ArrayList<>();
                for (Account acc : accounts) {
                    AccountExt accountExt = new AccountExt(acc, acc.getId(), acc.getName());
                    result.add(accountExt);
                }
                return result;
            }
        }


        private class AccountExt extends Account {
            public AccountExt(HttpBaseEntity entity, String id, String name) {
                super(entity, id, name);
            }

            @Override
            protected JSON query(HttpRequestBase request, int expectedCode) throws IOException {
                return getResponse(request);
            }

            @Override
            protected JSONObject queryObject(HttpRequestBase request, int expectedCode) throws IOException {
                return (JSONObject) getResponse(request);
            }

            @Override
            public List<Workspace> getWorkspaces() throws IOException {
                List<Workspace> workspaces = super.getWorkspaces();
                List<Workspace> result = new ArrayList<>();
                for (Workspace workspace : workspaces) {
                    WorkspaceExt workspaceExt = new WorkspaceExt(workspace, workspace.getId(), workspace.getName());
                    result.add(workspaceExt);
                }
                return result;
            }

            @Override
            public Workspace createWorkspace(String name) throws IOException {
                Workspace workspace = super.createWorkspace(name);
                return new WorkspaceExt(workspace, workspace.getId(), workspace.getName());
            }
        }

        private class WorkspaceExt extends Workspace {
            public WorkspaceExt(HttpBaseEntity entity, String id, String name) {
                super(entity, id, name);
            }

            @Override
            protected JSON query(HttpRequestBase request, int expectedCode) throws IOException {
                return getResponse(request);
            }

            @Override
            protected JSONObject queryObject(HttpRequestBase request, int expectedCode) throws IOException {
                return (JSONObject) getResponse(request);
            }

            @Override
            public List<Project> getProjects() throws IOException {
                List<Project> projects = super.getProjects();
                List<Project> result = new ArrayList<>();
                for (Project project : projects) {
                    ProjectExt projectExt = new ProjectExt(project, project.getId(), project.getName());
                    result.add(projectExt);
                }
                return result;
            }

            @Override
            public Project createProject(String name) throws IOException {
                Project project = super.createProject(name);
                return new ProjectExt(project, project.getId(), project.getName());
            }
        }

        private class ProjectExt extends Project {

            public ProjectExt(HttpBaseEntity entity, String id, String name) {
                super(entity, id, name);
            }

            @Override
            protected JSON query(HttpRequestBase request, int expectedCode) throws IOException {
                return getResponse(request);
            }

            @Override
            protected JSONObject queryObject(HttpRequestBase request, int expectedCode) throws IOException {
                return (JSONObject) getResponse(request);
            }

            @Override
            public List<Test> getTests() throws IOException {
                List<Test> tests = super.getTests();
                List<Test> result = new ArrayList<>();
                for (Test test : tests) {
                    TestExt testExt = new TestExt(test, test.getId(), test.getName());
                    result.add(testExt);
                }
                return result;
            }

            @Override
            public Test createTest(String name) throws IOException {
                Test test = super.createTest(name);
                return new TestExt(test, test.getId(), test.getName());
            }
        }

        private class TestExt extends Test {

            public TestExt(HttpBaseEntity entity, String id, String name) {
                super(entity, id, name);
            }

            @Override
            protected JSON query(HttpRequestBase request, int expectedCode) throws IOException {
                return getResponse(request);
            }

            @Override
            protected JSONObject queryObject(HttpRequestBase request, int expectedCode) throws IOException {
                return (JSONObject) getResponse(request);
            }

            @Override
            public Master getMaster() {
                Master master = super.getMaster();
                return new MasterExt(master, master.getId(), master.getName());
            }

            @Override
            public Session getSession() {
                Session session = super.getSession();
                return new SessionExt(session, session.getId(), session.getName(), session.getUserId(), session.getTestId(), session.getSignature());
            }
        }

        private class MasterExt extends Master {

            public MasterExt(HttpBaseEntity entity, String id, String name) {
                super(entity, id, name);
            }

            @Override
            protected JSON query(HttpRequestBase request, int expectedCode) throws IOException {
                return getResponse(request);
            }

            @Override
            protected JSONObject queryObject(HttpRequestBase request, int expectedCode) throws IOException {
                return (JSONObject) getResponse(request);
            }
        }

        private class SessionExt extends Session {

            public SessionExt(HttpBaseEntity entity, String id, String name, String userId, String testId, String signature) {
                super(entity, id, name, userId, testId, signature);
            }

            @Override
            protected JSON query(HttpRequestBase request, int expectedCode) throws IOException {
                return getResponse(request);
            }

            @Override
            protected JSONObject queryObject(HttpRequestBase request, int expectedCode) throws IOException {
                return (JSONObject) getResponse(request);
            }
        }

        public void addEmul(JSON response) {
            responses.add(response);
        }


        @Override
        protected JSON query(HttpRequestBase request, int expectedCode) throws IOException {
            return getResponse(request);
        }

        @Override
        protected JSONObject queryObject(HttpRequestBase request, int expectedCode) throws IOException {
            return (JSONObject) getResponse(request);
        }

        public JSON getResponse(HttpRequestBase request) throws IOException {
            log.info("Simulating request: " + request);
            if (responses.size() > 0) {
                JSON resp = responses.remove();
                log.info("Response: " + resp);
                return resp;
            } else {
                throw new IOException("No responses to emulate");
            }
        }
    }
}