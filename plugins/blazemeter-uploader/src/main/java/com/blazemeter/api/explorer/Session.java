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

    public static Session fromJSON(HttpBaseEntity entity, String testId, String signature, JSONObject session) {
        return new Session(entity, session.getString("id"), session.getString("name"), session.getString("userId"), testId, signature);
    }
}
