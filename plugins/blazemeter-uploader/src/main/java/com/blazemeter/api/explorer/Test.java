package com.blazemeter.api.explorer;


import com.blazemeter.api.explorer.base.BaseEntity;
import net.sf.json.JSONObject;

public class Test extends BaseEntity {

    public static final String DEFAULT_TEST = "Default test";

    public Test(String id, String name) {
        super(id, name);
    }

    public static Test fromJSON(JSONObject obj) {
        return new Test(obj.getString("id"), obj.getString("name"));
    }
}
