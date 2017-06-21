package com.blazemeter.api.entity;

public class Session {

    private String id;
    private String testId;
    private String userId;

    public Session() {
    }

    public Session(String id, String testId, String userId) {
        this.id = id;
        this.testId = testId;
        this.userId = userId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTestId() {
        return testId;
    }

    public void setTestId(String testId) {
        this.testId = testId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
