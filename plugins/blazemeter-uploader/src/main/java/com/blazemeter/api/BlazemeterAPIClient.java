package com.blazemeter.api;

import com.blazemeter.api.entity.BlazemeterReport;
import com.blazemeter.api.entity.Session;
import com.blazemeter.api.explorer.AbstractHttpEntity;
import com.blazemeter.api.explorer.Account;
import com.blazemeter.api.explorer.Project;
import com.blazemeter.api.explorer.User;
import com.blazemeter.api.explorer.Workspace;
import com.blazemeter.jmeter.StatusNotifierCallback;
import net.sf.json.JSONObject;

import java.io.IOException;
import java.util.List;

public class BlazemeterAPIClient extends AbstractHttpEntity {

    private Session session;
    private String signature;


    public BlazemeterAPIClient(StatusNotifierCallback notifier, String address, String dataAddress, BlazemeterReport report) {
        super(notifier, address, dataAddress, report);
        try {
            ping();
        } catch (IOException e) {
            log.error("Cannot reach online results storage, maybe the address/token is wrong");
            return;
        }
        if (!isAnonymousTest()) {
            try {
                prepareClient();
            } catch (IOException e) {
                log.error("Cannot prepare client for sending report", e);
            }
        }

    }


    public void ping() throws IOException {
        String uri = address + "/api/v4/web/version";
        query(createGet(uri), 200);
    }

    private void prepareClient() throws IOException {
        User user = new User(notifier, address, dataAddress, report);
        List<Account> accounts = user.getAccounts();
        Workspace workspace = findWorkspace(accounts);
        if (workspace != null) {
            findProject(workspace);
        }
    }

    private Project findProject(Workspace workspace) throws IOException {
        List<Project> projects = workspace.getProjects();

        return null;
    }

    private Workspace findWorkspace(List<Account> accounts) throws IOException {
        final String workspace = report.getWorkspace();

        for (Account account : accounts) {
            List<Workspace> workspaces = account.getWorkspaces();
            for (Workspace wsp : workspaces) {
                if (workspace.equals(wsp.getId()) || workspace.equals(wsp.getName())) {
                    return wsp;
                }
            }
        }

        for (Account account : accounts) {
            Workspace wsp = account.createWorkspace(workspace);
            if (wsp != null) {
                return wsp;
            }
        }

        log.error("Cannot find workspace or create it");
        return null;
    }

    public String startOnline() throws IOException {
        if (isAnonymousTest()) {
            notifier.notifyAbout("No BlazeMeter API key provided, will upload anonymously");
            return startAnonymousTest();
        } else {
            return startTest();
        }
    }

    public void sendOnlineData(JSONObject data) throws IOException {
        if (isAnonymousTest()) {
            sendAnonymousData(data);
        } else {
            // TODO:
        }

    }

    public void endOnline() throws IOException {
        if (isAnonymousTest()) {
            finishAnonymousTest();
        } else {
            finishTest();
        }
    }

    private String startAnonymousTest() throws IOException {
        String uri = address + "/api/v4/sessions";
        JSONObject response = queryObject(createPost(uri, ""), 201);
        JSONObject result = response.getJSONObject("result");
        this.session = extractSession(result);
        this.signature = result.optString("signature");
        return result.optString("publicTokenUrl");
    }

    private String startTest() {
        return "startTest";
    }

    private Session extractSession(JSONObject result) {
        final JSONObject session = result.getJSONObject("session");
        return new Session(session.optString("id"), session.optString("testId"), session.optString("userId"));
    }


    private void sendAnonymousData(JSONObject data) throws IOException {
        String uri = dataAddress +
                String.format("/submit.php?session_id=%s&signature=%s&test_id=%s&user_id=%s",
                        session.getId(), signature, session.getTestId(), session.getUserId());
        uri += "&pq=0&target=labels_bulk&update=1"; //TODO: % self.kpi_target
        String dataStr = data.toString();
        log.info("Sending active test data: " + dataStr);
        query(createPost(uri, dataStr), 200);
    }



    private void finishAnonymousTest() throws IOException {
        String uri = address + String.format("/api/v4/sessions/%s/terminate-external", session.getId());
        JSONObject data = new JSONObject();
        data.put("signature", signature);
        data.put("testId", session.getTestId());
        data.put("sessionId", session.getId());
        query(createPost(uri, data.toString()), 500);
    }

    private void finishTest() {
    }



}

