package com.blazemeter.api.explorer;

import com.blazemeter.api.explorer.base.HttpBaseEntity;
import net.sf.json.JSONObject;

public class Master extends HttpBaseEntity {

    public Master(HttpBaseEntity entity, String id, String name) {
        super(entity, id, name);
    }

    public static Master fromJSON(HttpBaseEntity entity, JSONObject obj) {
        return new Master(entity, obj.getString("id"), obj.getString("name"));
    }
}
