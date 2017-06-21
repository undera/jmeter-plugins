package com.blazemeter.api.entity;


public class Test {

    public static final String DEFAULT_TEST = "Default test";

    private final String id;
    private final String name;

    public Test(String id, String name) {
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
