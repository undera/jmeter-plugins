package org.jmeterplugins.protocol.http.control;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class HttpSimpleTableControlTest {

    public HttpSimpleTableControlTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testCanRemove() throws Exception {
        System.out.println("canRemove");
        HttpSimpleTableControl instance = new HttpSimpleTableControl();
        assertTrue(instance.canRemove());

        instance.startHttpSimpleTable();
        assertFalse(instance.canRemove());

        instance.stopHttpSimpleTable();
        assertTrue(instance.canRemove());
    }

    @Test
    public void testIsServerAlive() throws Exception {
        System.out.println("isServerAlive");
        HttpSimpleTableControl instance = new HttpSimpleTableControl();
        assertFalse(instance.isServerAlive());

        instance.startHttpSimpleTable();
        assertTrue(instance.isServerAlive());

        instance.stopHttpSimpleTable();
        assertFalse(instance.isServerAlive());
    }

    @Test
    public void testStopHttpSimpleTable() {
        System.out.println("stopHttpSimpleTable");
        HttpSimpleTableControl instance = new HttpSimpleTableControl();
        instance.stopHttpSimpleTable();
    }
}
