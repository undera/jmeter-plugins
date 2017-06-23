package com.blazemeter.api.explorer;

import com.blazemeter.api.explorer.base.HttpBaseEntity;
import com.blazemeter.jmeter.StatusNotifierCallbackTest;
import net.sf.json.JSON;
import net.sf.json.JSONObject;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class TestTest {

    @org.junit.Test
    public void testFlow() throws Exception {
        StatusNotifierCallbackTest notifier = new StatusNotifierCallbackTest();
        HttpBaseEntity emul = new HttpBaseEntity(notifier, "test_address", "test_data_address", "test_id", false);

        JSONObject testResponse = new JSONObject();
        testResponse.put("id", "responseTestId");
        testResponse.put("name", "responseTestName");

        JSONObject sessionResponse = new JSONObject();
        sessionResponse.put("id", "responseSessionId");
        sessionResponse.put("name", "responseSessionName");
        sessionResponse.put("userId", "responseUserId");

        JSONObject masterResponse = new JSONObject();
        masterResponse.put("id", "responseMasterId");
        masterResponse.put("name", "responseMasterName");

        final String expectedURL = "localhost:7777/report";
        JSONObject result = new JSONObject();
        result.put("publicTokenUrl", expectedURL);
        result.put("test", testResponse);
        result.put("signature", "responseSignature");
        result.put("session", sessionResponse);
        result.put("master", masterResponse);

        JSONObject response = new JSONObject();
        response.put("result", result);

        TestExt test = new TestExt(emul, "testId", "testName");
        test.addEmul(response);
        test.startExternal();
        assertEquals(1, test.getRequests().size());
        assertEquals("", test.getRequests().get(0));
        assertNull(test.getReportURL());
        checkTest(test, false);
        test.clean();

        test = new TestExt(emul);

        test.addEmul(response);
        String url = test.startAnonymousExternal();

        assertEquals(expectedURL, url);
        assertEquals(expectedURL, test.getReportURL());
        checkTest(test, true);
    }

    private void checkTest(Test test, boolean isStartAnonymous) {
        Master master = test.getMaster();
        assertEquals("responseMasterId", master.getId());
        assertEquals("responseMasterName", master.getName());

        Session session = test.getSession();
        assertEquals("responseSessionId", session.getId());
        assertEquals("responseSessionName", session.getName());
        assertEquals("responseUserId", session.getUserId());
        if (!isStartAnonymous) {
            assertEquals("testId", session.getTestId());
        } else {
            assertEquals("responseTestId", session.getTestId());
        }
        assertEquals("responseSignature", session.getSignature());

        assertEquals("responseSignature", test.getSignature());
    }

    @org.junit.Test
    public void testFromJSON() throws Exception {
        StatusNotifierCallbackTest notifier = new StatusNotifierCallbackTest();
        HttpBaseEntity emul = new HttpBaseEntity(notifier, "test_address", "test_data_address", "test_id", false);
        JSONObject object = new JSONObject();
        object.put("id", "testId");
        object.put("name", "testName");
        Test test = Test.fromJSON(emul, object);
        assertEquals("testId", test.getId());
        assertEquals("testName", test.getName());
        assertEquals(notifier, test.getNotifier());
    }

    protected static class TestExt extends Test {
        private LinkedList<JSON> responses = new LinkedList<>();
        private LinkedList<String> requests = new LinkedList<>();

        public TestExt(HttpBaseEntity entity) {
            super(entity);
        }

        public TestExt(HttpBaseEntity entity, String id, String name) {
            super(entity, id, name);
        }

        public void clean() {
            requests.clear();
        }

        public LinkedList<String> getRequests() {
            return requests;
        }

        public void addEmul(JSON response) {
            responses.add(response);
        }


        @Override
        protected JSON query(HttpRequestBase request, int expectedCode) throws IOException {
            extractBody(request);
            log.info("Simulating request: " + request);
            if (responses.size()>0) {
                JSON resp = responses.remove();
                log.info("Response: " + resp);
                return resp;
            } else {
                throw new IOException("No responses to emulate");
            }
        }

        private void extractBody(HttpRequestBase request) throws IOException {
            if (request instanceof HttpPost) {
                HttpPost post = (HttpPost) request;
                InputStream inputStream = post.getEntity().getContent();
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                IOUtils.copy(inputStream, outputStream);
                requests.add(outputStream.toString());
            }
        }


        @Override
        protected JSONObject queryObject(HttpRequestBase request, int expectedCode) throws IOException {
            extractBody(request);
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