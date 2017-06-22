package com.blazemeter.api.explorer;

import com.blazemeter.api.explorer.base.HttpBaseEntity;
import net.sf.json.JSONObject;

import java.io.IOException;

public class Master extends HttpBaseEntity {

    public Master(HttpBaseEntity entity, String id, String name) {
        super(entity, id, name);
    }

//    url = self.address + "/api/v4/masters/%s/public-token" % self['id']
//    res = self._request(url, {"publicToken": None}, method="POST")
//    public_token = res['result']['publicToken']
//    report_link = self.address + "/app/?public-token=%s#/masters/%s/summary" % (public_token, self['id'])
//            return report_link

    public String makeReportPublic() throws IOException {
        String uri = address + String.format("/api/v4/masters/%s/public-token", getId());
        JSONObject response = queryObject(createPost(uri, "{\"publicToken\": None}"), 200);
        System.out.println();
        return "";
    }

    public static Master fromJSON(HttpBaseEntity entity, JSONObject obj) {
        return new Master(entity, obj.getString("id"), obj.getString("name"));
    }
}
