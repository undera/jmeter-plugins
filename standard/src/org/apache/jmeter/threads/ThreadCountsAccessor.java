package org.apache.jmeter.threads;

import org.apache.jmeter.gui.GuiPackage;

// so sad, but JMeter core has package-private visibility for some methods
public class ThreadCountsAccessor {
    private static long lastUpdate = 0;

    public static void decrNumberOfThreads() {
        JMeterContextService.decrNumberOfThreads();
        refreshUI();
    }

    public static void incrNumberOfThreads() {
        JMeterContextService.incrNumberOfThreads();
        refreshUI();
    }

    private static void refreshUI() {
        long ts = System.currentTimeMillis();
        if (ts - lastUpdate < 1000) {
            return; // throttle down updates
        }

        lastUpdate = ts;
        GuiPackage gp = GuiPackage.getInstance();
        if (gp != null) {// check there is a GUI
            gp.getMainFrame().updateCounts();
        }
    }

}
