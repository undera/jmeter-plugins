package com.blazemeter.jmeter.threads.arrivals;

import com.blazemeter.jmeter.threads.AbstractBaseDynamicThreadGroupGui;
import com.blazemeter.jmeter.threads.AbstractDynamicThreadGroup;
import kg.apc.charting.AbstractGraphRow;
import kg.apc.charting.DateTimeRenderer;
import kg.apc.charting.rows.GraphRowExactValues;
import kg.apc.jmeter.JMeterPluginsUtils;
import kg.apc.jmeter.gui.Grid;
import kg.apc.jmeter.gui.GuiBuilderHelper;
import org.apache.jmeter.gui.util.VerticalPanel;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.testelement.property.CollectionProperty;
import org.apache.jmeter.testelement.property.PropertyIterator;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import java.awt.*;

public class FreeFormArrivalsThreadGroupGui extends AbstractBaseDynamicThreadGroupGui implements TableModelListener {
    private static final Logger log = LoggingManager.getLoggerForClass();
    private static final String WIKIPAGE = "FreeFormArrivalsThreadGroup";
    private Grid grid;
    private String[] columnIdentifiers = new String[]{"Start Value", "End Value", "Duration"};
    private Class[] columnClasses = new Class[]{String.class, String.class, String.class};
    private String[] defaultValues = new String[]{"1", "1", "1"};

    public FreeFormArrivalsThreadGroupGui() {
        super();
        init();
    }

    @Override
    public String getLabelResource() {
        return getClass().getCanonicalName();
    }

    @Override
    public String getStaticLabel() {
        return "Free-Form Arrivals Thread Group";
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
    protected AbstractDynamicThreadGroup createThreadGroupObject() {
        return new FreeFormArrivalsThreadGroup();
    }

    @Override
    public void modifyTestElement(TestElement element) {
        super.configureTestElement(element);
        if (element instanceof FreeFormArrivalsThreadGroup) {
            FreeFormArrivalsThreadGroup wsc = (FreeFormArrivalsThreadGroup) element;
            wsc.setData(grid.getModel());
        }
    }

    @Override
    public void configure(TestElement tg) {
        super.configure(tg);
        if (tg instanceof FreeFormArrivalsThreadGroup) {
            FreeFormArrivalsThreadGroup wsc = (FreeFormArrivalsThreadGroup) tg;
            JMeterPluginsUtils.collectionPropertyToTableModelRows(wsc.getData(), grid.getModel());
        }
    }

    protected void init() {
        JMeterPluginsUtils.addHelpLinkToPanel(this, WIKIPAGE);
        JPanel containerPanel = new VerticalPanel();

        grid = new Grid("Threads Schedule", columnIdentifiers, columnClasses, defaultValues);
        grid.getModel().addTableModelListener(this);
        containerPanel.add(grid, BorderLayout.NORTH);
        containerPanel.add(GuiBuilderHelper.getComponentWithMargin(getPreviewChart(), 2, 2, 0, 2), BorderLayout.CENTER);
        add(containerPanel, BorderLayout.CENTER);
        uiCreated = true;
        // TODO: concurrency limit field
        // TODO: arrivals log field
    }

    @Override
    public void tableChanged(TableModelEvent e) {
        log.info("Table changed");
        SwingUtilities.invokeLater(this);
    }

    @Override
    public void updateUI() {
        super.updateUI();
        if (!uiCreated) {
            return;
        }

        FreeFormArrivalsThreadGroup atg = (FreeFormArrivalsThreadGroup) createThreadGroupObject();
        modifyTestElement(atg);
        try {
            updateChart(atg.getData());
        } catch (NumberFormatException e) {
            previewChart.setErrorMessage("The values entered cannot be rendered in preview...");
        } finally {
            setChartPropertiesFromTG(atg);
            previewChart.invalidateCache();
            previewChart.repaint();
        }
    }

    private void updateChart(CollectionProperty data) {
        chartModel.clear();
        previewChart.clearErrorMessage();
        AbstractGraphRow row = new GraphRowExactValues();
        row.setColor(getRowColor());
        row.setDrawLine(true);
        row.setMarkerSize(AbstractGraphRow.MARKER_SIZE_NONE);
        row.setDrawThickLines(true);

        row.add(0, 0); // initial value to force min Y

        int offset = 0;
        double totalArrivals = 0;
        PropertyIterator it = data.iterator();
        while (it.hasNext()) {
            CollectionProperty record = (CollectionProperty) it.next();
            row.add(offset, record.get(0).getIntValue());
            offset += record.get(2).getIntValue();
            row.add(offset, record.get(1).getIntValue());
            // TODO: increment total arrivals
        }

        previewChart.setxAxisLabelRenderer(new DateTimeRenderer(DateTimeRenderer.HHMMSS, 0));
        chartModel.put(getRowLabel(totalArrivals), row);
    }
}
