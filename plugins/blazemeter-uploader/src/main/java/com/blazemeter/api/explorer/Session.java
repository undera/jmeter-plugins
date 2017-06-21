package com.blazemeter.api.explorer;

import com.blazemeter.api.explorer.base.HttpBaseEntity;

public class Session extends HttpBaseEntity {

    private Test test;
    private String userId;

    public Session(HttpBaseEntity entity, String id, String name, Test test, String userId) {
        super(entity, id, name);
        this.test = test;
        this.userId = userId;
    }

    public Test getTest() {
        return test;
    }

    public void setTest(Test test) {
        this.test = test;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
