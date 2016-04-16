package com.blazemeter.jmeter.threads.concurrency;

import com.blazemeter.jmeter.threads.*;
import kg.apc.jmeter.JMeterPluginsUtils;

import java.awt.*;

public class ConcurrencyThreadGroupGui extends AbstractDynamicThreadGroupGui {
    @Override
    public String getLabelResource() {
        return getClass().getCanonicalName();
    }

    @Override
    public String getStaticLabel() {
        return "bzm - Concurrency Thread Group";
    }

    public ConcurrencyThreadGroupGui() {
        super();
        JMeterPluginsUtils.addHelpLinkToPanel(this, getClass().getSimpleName());
    }

    @Override
    protected ParamsPanel createLoadPanel() {
        LoadParamsFieldsPanel loadParamsFieldsPanel = new LoadParamsFieldsPanel("Target Concurrency: ", "Ramp Up Time (sec): ", "Hold Target Rate Time (sec): ");
        loadParamsFieldsPanel.addUpdateListener(this);
        return loadParamsFieldsPanel;
    }

    @Override
    protected AdditionalFieldsPanel getAdditionalFieldsPanel() {
        return new AdditionalFieldsPanel(false);
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
