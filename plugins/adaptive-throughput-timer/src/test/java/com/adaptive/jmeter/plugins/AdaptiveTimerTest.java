package com.adaptive.jmeter.plugins;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class AdaptiveTimerTest {

    private AdaptiveTimer timer;

    @Before
    public void setUp() {
        timer = new AdaptiveTimer();
    }

    @Test
    public void testTimerCreation() {
        assertNotNull(timer);
    }

    @Test
    public void testMinDelayProperty() {
        timer.setMinDelay(100);
        assertEquals(100, timer.getMinDelay());
    }

    @Test
    public void testMaxDelayProperty() {
        timer.setMaxDelay(500);
        assertEquals(500, timer.getMaxDelay());
    }

    @Test
    public void testTargetThroughputProperty() {
        timer.setTargetThroughput(1000);
        assertEquals(1000, timer.getTargetThroughput());
    }

    @Test
    public void testDelayCalculation() {
        timer.setMinDelay(100);
        timer.setMaxDelay(500);
        timer.setTargetThroughput(1000);

        long delay = timer.delay();
        assertTrue(delay >= timer.getMinDelay());
        assertTrue(delay <= timer.getMaxDelay());
    }
}
