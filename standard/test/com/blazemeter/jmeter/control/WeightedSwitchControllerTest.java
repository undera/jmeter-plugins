package com.blazemeter.jmeter.control;

import org.apache.jmeter.gui.util.PowerTableModel;
import org.apache.jmeter.sampler.DebugSampler;
import org.apache.jmeter.samplers.Sampler;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class WeightedSwitchControllerTest {

    private static final Logger log = LoggingManager.getLoggerForClass();

    @Test
    public void testGeneric() {
        WeightedSwitchController obj = new WeightedSwitchController();
        obj.addTestElement(getSampler("0"));
        obj.addTestElement(getSampler("1"));
        obj.addTestElement(getSampler("2"));
        obj.addTestElement(getSampler("5"));
        PowerTableModel mdl = new PowerTableModel(new String[]{"name", WeightedSwitchController.WEIGHTS}, new Class[]{String.class, String.class});
        mdl.addRow(new String[]{"0", "0"});
        mdl.addRow(new String[]{"1", "1"});
        mdl.addRow(new String[]{"2", "2"});
        mdl.addRow(new String[]{"5", "5"});
        obj.setData(mdl);

        for (int n = 0; n < 800; n++) {
            Sampler s = obj.next();
            log.info("Sampler: " + s.getName());
            assertNotNull(s);
            assertNull(obj.next());
            assertNotEquals(s.getName(), "0");
        }
    }

    private DebugSampler getSampler(String s) {
        DebugSampler debugSampler = new DebugSampler();
        debugSampler.setName(s);
        return debugSampler;
    }

    @Test
    public void testComb() {
        long[] sums = new long[]{0L, 0L};
        long[] prio = new long[]{2, 3};
        long[] choices = new long[]{0, 0};
        for (int i = 0; i < 100; i++) {
            // inc counts
            for (int k = 0; k < sums.length; k++) {
                sums[k] += prio[k];
            }

            if (sums[0] >= sums[1]) {
                // 2
                sums[0] -= prio[0];
                choices[0]++;
            } else {
                // 3
                sums[1] -= prio[1];
                choices[1]++;
            }

            // minimize nums
            long min;
            if (sums[0] >= sums[1]) {
                min = sums[1];
            } else {
                min = sums[0];
            }
            for (int z = 0; z < sums.length; z++) {
                sums[z] -= min;
            }

        }
        System.out.print(Arrays.toString(choices));
    }
}