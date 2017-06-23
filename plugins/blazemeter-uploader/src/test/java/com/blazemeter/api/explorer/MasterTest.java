package com.blazemeter.api.explorer;

import com.blazemeter.api.explorer.base.HttpBaseEntity;
import com.blazemeter.jmeter.StatusNotifierCallbackTest;
import net.sf.json.JSON;
import net.sf.json.JSONObject;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;
import org.junit.Test;

import java.io.IOException;
import java.util.LinkedList;

import static org.junit.Assert.*;

public class MasterTest {

    @Test
    public void testFlow() throws Exception {
        StatusNotifierCallbackTest notifier = new StatusNotifierCallbackTest();
        HttpBaseEntity emul = new HttpBaseEntity(notifier, "test_address", "test_data_address", "test_id", false);

        JSONObject result = new JSONObject();
        result.put("publicToken", "test_token");
        JSONObject response = new JSONObject();
        response.put("result", result);

        MasterExt account = new MasterExt(emul, "master_id", "master_name");
        account.addEmul(response);
        String url = account.makeReportPublic();
        assertEquals("test_address/app/?public-token=test_token#/masters/master_id/summary", url);
    }

    @Test
    public void testFromJSON() throws Exception {
        StatusNotifierCallbackTest notifier = new StatusNotifierCallbackTest();
        HttpBaseEntity emul = new HttpBaseEntity(notifier, "test_address", "test_data_address", "test_id", false);
        JSONObject object = new JSONObject();
        object.put("id", "masterId");
        object.put("name", "masterName");
        Master account = Master.fromJSON(emul, object);
        assertEquals("masterId", account.getId());
        assertEquals("masterName", account.getName());
        assertEquals("test_address", account.getAddress());
        assertEquals("test_data_address", account.getDataAddress());
        assertEquals(notifier, account.getNotifier());
    }

    protected static class MasterExt extends Master {
        private static final Logger log = LoggingManager.getLoggerForClass();

        private LinkedList<JSON> responses = new LinkedList<>();

        public MasterExt(HttpBaseEntity entity, String id, String name) {
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