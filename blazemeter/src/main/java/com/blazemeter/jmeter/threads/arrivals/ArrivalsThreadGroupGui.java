package com.blazemeter.jmeter.threads.arrivals;

import com.blazemeter.jmeter.threads.AbstractDynamicThreadGroup;
import com.blazemeter.jmeter.threads.AbstractDynamicThreadGroupGui;
import org.apache.jmeter.gui.util.HorizontalPanel;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

import javax.swing.*;
import java.awt.*;
import java.util.Enumeration;

public class ArrivalsThreadGroupGui extends AbstractDynamicThreadGroupGui {
    private static final Logger log = LoggingManager.getLoggerForClass();
    private JTextField concurrLimit = new JTextField();
    private ButtonGroup unitGroup = new ButtonGroup();
    private JRadioButton unitSeconds = new JRadioButton("seconds");
    private JRadioButton unitMinutes = new JRadioButton("minutes");

    public ArrivalsThreadGroupGui() {
        super();
        initFields();
    }

    @Override
    public String getLabelResource() {
        return getClass().getCanonicalName();
    }

    @Override
    public String getStaticLabel() {
        return "Arrivals Thread Group";
    }


    protected ArrivalsThreadGroup createThreadGroupObject() {
        return new ArrivalsThreadGroup();
    }

    @Override
    protected void setChartPropertiesFromTG(AbstractDynamicThreadGroup tg) {
        if (tg instanceof ArrivalsThreadGroup) {
            ArrivalsThreadGroup atg = (ArrivalsThreadGroup) tg;
            previewChart.setYAxisLabel("Number of arrivals/" + getUnitStr(atg.getUnit()));
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

    private String getUnitStr(String unit) {
        if (unit.equals(ArrivalsThreadGroup.UNIT_MINUTES)) {
            return "min";
        } else {
            return "sec";
        }
    }

    private void changeUnitInLabels() {
        if (unitGroup.getSelection() != null) {
            String unitStr = getUnitStr(unitGroup.getSelection().getActionCommand());
            targetRateLabel.setText("Target Rate (arrivals/" + unitStr + "): ");
            rampUpLabel.setText("Ramp Up Time (" + unitStr + "): ");
            holdLabel.setText("Hold Target Rate Time (" + unitStr + "): ");
        }
    }


    @Override
    public void configure(TestElement element) {
        super.configure(element);
        if (element instanceof ArrivalsThreadGroup) {
            ArrivalsThreadGroup tg = (ArrivalsThreadGroup) element;
            concurrLimit.setText(tg.getConcurrencyLimit());


            Enumeration<AbstractButton> it = unitGroup.getElements();
            while (it.hasMoreElements()) {
                AbstractButton btn = it.nextElement();
                if (btn.getActionCommand().equals(tg.getUnit())) {
                    btn.setSelected(true);
                }
            }

            changeUnitInLabels();
        }
    }

    @Override
    public void modifyTestElement(TestElement element) {
        super.modifyTestElement(element);
        super.configureTestElement(element);
        if (element instanceof AbstractDynamicThreadGroup) {
            ArrivalsThreadGroup tg = (ArrivalsThreadGroup) element;
            tg.setConcurrencyLimit(concurrLimit.getText());
            if (unitGroup.getSelection() != null) {
                tg.setUnit(unitGroup.getSelection().getActionCommand());
            }
        }
    }

    @Override
    public void clearGui() {
        super.clearGui();
        concurrLimit.setText("1000");
        unitMinutes.setSelected(true);
    }

    protected JPanel initFields() {
        JPanel fields = super.getFieldsPanel();

        fields.add("Concurrency Limit: ", concurrLimit);
        unitMinutes.setActionCommand(ArrivalsThreadGroup.UNIT_MINUTES);
        unitMinutes.addActionListener(this);
        unitSeconds.setActionCommand(ArrivalsThreadGroup.UNIT_SECONDS);
        unitSeconds.addActionListener(this);
        unitGroup.add(unitMinutes);
        unitGroup.add(unitSeconds);
        JPanel groupPanel = new HorizontalPanel();
        groupPanel.add(unitMinutes);
        groupPanel.add(unitSeconds);
        fields.add("Time Unit: ", groupPanel);

        return fields;
    }

    @Override
    public void run() {
        super.run();
        changeUnitInLabels();
    }


}
