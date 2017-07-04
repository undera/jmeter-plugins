package com.blazemeter.api.explorer;

import com.blazemeter.api.explorer.base.HttpBaseEntity;
import com.blazemeter.jmeter.StatusNotifierCallbackTest;
import net.sf.json.JSON;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import net.sf.json.JsonConfig;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;

import static org.junit.Assert.assertEquals;

public class SessionTest {

    @org.junit.Test
    public void testFlow() throws Exception {
        StatusNotifierCallbackTest notifier = new StatusNotifierCallbackTest();
        HttpBaseEntity emul = new HttpBaseEntity(notifier, "test_address", "test_data_address", "test_id", false);

        JSONObject data = new JSONObject();
        data.put("data", "Hello, World!");

        SessionExt session = new SessionExt(emul, "sessionId", "sessionName", "userId", "testId", "testSignature");

        session.addEmul(JSONSerializer.toJSON("{\"result\":{\"session\":{\"statusCode\":15}}}", new JsonConfig()));
        session.sendData(data);
        assertEquals(1, session.getRequests().size());
        assertEquals("{\"data\":\"Hello, World!\"}", session.getRequests().get(0));
        session.clean();

        session.addEmul(new JSONObject());
        session.stop();
        assertEquals(1, session.getRequests().size());
        assertEquals("", session.getRequests().get(0));
        session.clean();

        session.addEmul(new JSONObject());
        session.stopAnonymous();
        assertEquals(1, session.getRequests().size());
        assertEquals("{\"signature\":\"testSignature\",\"testId\":\"testId\",\"sessionId\":\"sessionId\"}", session.getRequests().get(0));
        session.clean();
    }

    @org.junit.Test
    public void testFromJSON() throws Exception {
        StatusNotifierCallbackTest notifier = new StatusNotifierCallbackTest();
        HttpBaseEntity emul = new HttpBaseEntity(notifier, "test_address", "test_data_address", "test_id", false);
        JSONObject object = new JSONObject();
        object.put("id", "sessionId");
        object.put("name", "sessionName");
        object.put("userId", "testUserId");
        Session session = Session.fromJSON(emul, "testId", "testSignature", object);
        assertEquals("sessionId", session.getId());
        assertEquals("sessionName", session.getName());
        assertEquals("testId", session.getTestId());
        assertEquals("testSignature", session.getSignature());
        assertEquals("testUserId", session.getUserId());
        assertEquals("test_address", session.getAddress());
        assertEquals("test_data_address", session.getDataAddress());
        assertEquals(notifier, session.getNotifier());
    }

    protected static class SessionExt extends Session {
        private LinkedList<String> requests = new LinkedList<>();
        private LinkedList<JSON> responses = new LinkedList<>();

        public SessionExt(HttpBaseEntity entity, String id, String name, String userId, String testId, String signature) {
            super(entity, id, name, userId, testId, signature);
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
            return getResponse(request);
        }

        private void extractBody(HttpRequestBase request) throws IOException {
            if (request instanceof HttpPost) {
                HttpPost post = (HttpPost) request;
                InputStream inputStream = post.getEntity().getContent();
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                IOUtils.copy(inputStream, outputStream);
                requests.add(outputStream.toString());
            } else {
                throw new IOException("POST request was expected");
            }
        }

        @Override
        protected JSONObject queryObject(HttpRequestBase request, int expectedCode) throws IOException {
            extractBody(request);
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