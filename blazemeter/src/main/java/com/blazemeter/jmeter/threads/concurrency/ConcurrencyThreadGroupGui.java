package com.blazemeter.jmeter.threads.concurrency;

import com.blazemeter.jmeter.threads.AbstractDynamicThreadGroup;
import com.blazemeter.jmeter.threads.AbstractDynamicThreadGroupGui;
import kg.apc.jmeter.JMeterPluginsUtils;

import java.awt.*;

public class ConcurrencyThreadGroupGui extends AbstractDynamicThreadGroupGui {
    @Override
    public String getLabelResource() {
        return getClass().getCanonicalName();
    }

    @Override
    public String getStaticLabel() {
        return JMeterPluginsUtils.prefixLabel("Concurrency Thread Group");
    }

    public ConcurrencyThreadGroupGui() {
        super();
        targetRateLabel.setText("Target Concurrency: ");
        rampUpLabel.setText("Ramp Up Time (sec): ");
        holdLabel.setText("Hold Target Rate Time (sec): ");
        JMeterPluginsUtils.addHelpLinkToPanel(this, getClass().getSimpleName());
    }

    protected AbstractDynamicThreadGroup createThreadGroupObject() {
        return new ConcurrencyThreadGroup();
    }

    @Override
    protected void setChartPropertiesFromTG(AbstractDynamicThreadGroup atg) {
        previewChart.setYAxisLabel("Number of concurrent threads");
    }

    @Override
    protected Color getRowColor() {
        return Color.RED;
    }

    @Override
    protected String getRowLabel(double totalArrivals) {
        return "Concurrent Threads";
    }
}
