package com.blazemeter.api.explorer;

import com.blazemeter.api.explorer.base.HttpBaseEntity;
import com.blazemeter.jmeter.StatusNotifierCallbackTest;
import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.http.client.methods.HttpRequestBase;
import org.junit.Test;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class AccountTest {

    @Test
    public void testFlow() throws Exception {
        StatusNotifierCallbackTest notifier = new StatusNotifierCallbackTest();
        HttpBaseEntity emul = new HttpBaseEntity(notifier, "test_address", "test_data_address", "test_id", false);

        JSONObject result = new JSONObject();
        result.put("id", "100");
        result.put("name", "NEW_WORKSPACE");
        JSONObject response = new JSONObject();
        response.put("result", result);

        AccountExt account = new AccountExt(emul, "777", "account_name");
        account.addEmul(response);
        Workspace workspace = account.createWorkspace("NEW_WORKSPACE");
        assertEquals("100", workspace.getId());
        assertEquals("NEW_WORKSPACE", workspace.getName());

        response.clear();
        JSONArray results = new JSONArray();
        results.add(result);
        results.add(result);
        response.put("result", results);
        account.addEmul(response);

        List<Workspace> workspaces = account.getWorkspaces();
        assertEquals(2, workspaces.size());
        for (Workspace wsp :workspaces) {
            assertEquals("100", wsp.getId());
            assertEquals("NEW_WORKSPACE", wsp.getName());
        }
    }

    @Test
    public void testFromJSON() throws Exception {
        StatusNotifierCallbackTest notifier = new StatusNotifierCallbackTest();
        HttpBaseEntity emul = new HttpBaseEntity(notifier, "test_address", "test_data_address", "test_id", false);
        JSONObject object = new JSONObject();
        object.put("id", "accountId");
        object.put("name", "accountName");
        Account account = Account.fromJSON(emul, object);
        assertEquals("accountId", account.getId());
        assertEquals("accountName", account.getName());
        assertEquals("test_address", account.getAddress());
        assertEquals("test_data_address", account.getDataAddress());
        assertEquals(notifier, account.getNotifier());
    }

    protected static class AccountExt extends Account {
        private LinkedList<JSON> responses = new LinkedList<>();

        public AccountExt(HttpBaseEntity entity, String id, String name) {
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