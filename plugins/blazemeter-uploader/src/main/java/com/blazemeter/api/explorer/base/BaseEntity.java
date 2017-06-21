package com.blazemeter.api.explorer.base;


public class BaseEntity {

    protected final String id;
    protected final String name;

    public BaseEntity(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
