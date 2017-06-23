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

public class UserTest {

    @org.junit.Test
    public void testFlow() throws Exception {
        StatusNotifierCallbackTest notifier = new StatusNotifierCallbackTest();
        HttpBaseEntity emul = new HttpBaseEntity(notifier, "test_address", "test_data_address", "test_id", false);

        UserExt user = new UserExt(emul);
        user.addEmul(new JSONObject());
        user.ping();

        JSONObject acc = new JSONObject();
        acc.put("id", "accountId");
        acc.put("name", "accountName");
        JSONArray result = new JSONArray();
        result.add(acc);
        result.add(acc);
        JSONObject response = new JSONObject();
        response.put("result", result);
        user.addEmul(response);

        List<Account> accounts = user.getAccounts();
        assertEquals(2, accounts.size());
        for (Account account : accounts) {
            assertEquals("accountId", account.getId());
            assertEquals("accountName", account.getName());
        }
    }

    protected static class UserExt extends User {

        private LinkedList<JSON> responses = new LinkedList<>();


        public UserExt(HttpBaseEntity entity) {
            super(entity);
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