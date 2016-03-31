package org.jmeterplugins.repository;


import net.sf.json.JSONObject;

public class Plugin {
    public Plugin(String id) {

    }

    public static Plugin fromJSON(JSONObject elm) {
        Plugin inst = new Plugin(elm.getString("id"));
        return inst;
    }
}
