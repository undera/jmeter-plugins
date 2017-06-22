package com.blazemeter.api.explorer;

import com.blazemeter.api.explorer.base.HttpBaseEntity;
import net.sf.json.JSONObject;

import java.io.IOException;

public class Session extends HttpBaseEntity {

    private final String userId;
    private final String testId;
    private final String signature;

    public Session(HttpBaseEntity entity, String id, String name, String userId, String testId, String signature) {
        super(entity, id, name);
        this.userId = userId;
        this.testId = testId;
        this.signature = signature;
    }

    /**
     * Send test json data for the report
     */
    public void sendData(JSONObject data) throws IOException {
        String uri = dataAddress +
                String.format("/submit.php?session_id=%s&signature=%s&test_id=%s&user_id=%s",
                        getId(), signature, testId, userId);
        uri += "&pq=0&target=labels_bulk&update=1"; //TODO: % self.kpi_target
        String dataStr = data.toString();
        log.info("Sending active test data: " + dataStr);
        query(createPost(uri, dataStr), 200);
    }

    /**
     * Stop session for user token
     */
    public void stop() throws IOException {
        String uri = address + String.format("/api/v4/sessions/%s/stop", getId());
        query(createPost(uri, ""), 202);
    }

    /**
     * Stop anonymous session
     */
    public void stopAnonymous() throws IOException {
        String uri = address + String.format("/api/v4/sessions/%s/terminate-external", getId());
        JSONObject data = new JSONObject();
        data.put("signature", signature);
        data.put("testId", testId);
        data.put("sessionId", getId());
        query(createPost(uri, data.toString()), 500);
    }

    public String getUserId() {
        return userId;
    }

    public String getTestId() {
        return testId;
    }

    public String getSignature() {
        return signature;
    }

    public static Session fromJSON(HttpBaseEntity entity, String testId, String signature, JSONObject session) {
        return new Session(entity, session.getString("id"), session.getString("name"), session.getString("userId"), testId, signature);
    }
}
