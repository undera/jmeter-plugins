package com.blazemeter.api.entity;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SessionTest {

    @Test
    public void test() throws Exception {
        Session session = new Session();

        String id = "id";
        session.setId(id);
        assertEquals(id, session.getId());

        String userId = "userId";
        session.setUserId(userId);
        assertEquals(userId, session.getUserId());

        String testId = "testId";
        session.setTestId(testId);
        assertEquals(testId, session.getTestId());

        session = new Session(id, testId, userId);
        assertEquals(id, session.getId());
        assertEquals(userId, session.getUserId());
        assertEquals(testId, session.getTestId());
    }
}