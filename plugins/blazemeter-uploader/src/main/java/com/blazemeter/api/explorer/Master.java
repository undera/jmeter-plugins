package com.blazemeter.api.explorer;

import com.blazemeter.api.explorer.base.HttpBaseEntity;
import net.sf.json.JSONObject;

import java.io.IOException;

public class Master extends HttpBaseEntity {

    public Master(HttpBaseEntity entity, String id, String name) {
        super(entity, id, name);
    }

    public String makeReportPublic() throws IOException {
        String uri = address + String.format("/api/v4/masters/%s/public-token", getId());
        JSONObject obj = new JSONObject();
        obj.put("publicToken", "None");
        JSONObject response = queryObject(createPost(uri, obj.toString()), 201);

        return address + String.format("/app/?public-token=%s#/masters/%s/summary",
                extractPublicToken(response.getJSONObject("result")), getId());
    }

    private String extractPublicToken(JSONObject result) {
        return result.getString("publicToken");
    }

    public static Master fromJSON(HttpBaseEntity entity, JSONObject obj) {
        return new Master(entity, obj.getString("id"), obj.getString("name"));
    }
}
