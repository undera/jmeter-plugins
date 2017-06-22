package com.blazemeter.api.explorer.base;

import org.junit.Test;

import static org.junit.Assert.*;

public class BaseEntityTest {

    @Test
    public void test() throws Exception {
        BaseEntity entity = new BaseEntity("id", "name");
        assertEquals("id", entity.getId());
        assertEquals("name", entity.getName());
        entity.setId("id1");
        entity.setName("name1");
        assertEquals("id1", entity.getId());
        assertEquals("name1", entity.getName());
    }
}