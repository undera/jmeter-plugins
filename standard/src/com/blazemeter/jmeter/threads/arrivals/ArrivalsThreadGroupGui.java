package com.blazemeter.jmeter.threads.arrivals;

import com.blazemeter.jmeter.threads.*;
import kg.apc.jmeter.JMeterPluginsUtils;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

import java.awt.*;

public class ArrivalsThreadGroupGui extends AbstractDynamicThreadGroupGui {
    private static final Logger log = LoggingManager.getLoggerForClass();

    public ArrivalsThreadGroupGui() {
        super();
        JMeterPluginsUtils.addHelpLinkToPanel(this, getClass().getSimpleName());
    }

    @Override
    public String getLabelResource() {
        return getClass().getCanonicalName();
    }

    @Override
    public String getStaticLabel() {
        return "bzm - Arrivals Thread Group";
    }


    protected ArrivalsThreadGroup createThreadGroupObject() {
        return new ArrivalsThreadGroup();
    }

    @Override
    protected AdditionalFieldsPanel getAdditionalFieldsPanel() {
        return new AdditionalFieldsPanel(true);
    }


    @Override
    protected void setChartPropertiesFromTG(AbstractDynamicThreadGroup tg) {
        if (tg instanceof ArrivalsThreadGroup) {
            ArrivalsThreadGroup atg = (ArrivalsThreadGroup) tg;
            previewChart.setYAxisLabel("Number of arrivals/" + atg.getUnitStr());
        }
    }

    @Override
    protected Color getRowColor() {
        return Color.MAGENTA;
    }

    @Override
    protected String getRowLabel(double totalArrivals) {
        log.debug("Total arr: " + totalArrivals);
        return "Arrival Rate (~" + Math.round(totalArrivals) + " total arrivals)";
    }

    @Override
    protected ParamsPanel createLoadPanel() {
        LoadParamsFieldsPanel loadFields = new LoadParamsFieldsPanel("Target Rate (arrivals/sec): ", "Ramp Up Time (sec): ", "Hold Target Rate Time (sec): ");
        loadFields.addUpdateListener(this);
        return loadFields;
    }

}
