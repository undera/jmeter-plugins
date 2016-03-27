package com.blazemeter.jmeter.threads.arrivals;


import com.blazemeter.jmeter.threads.AbstractDynamicThreadGroup;
import com.blazemeter.jmeter.threads.ParamsPanel;
import kg.apc.jmeter.JMeterPluginsUtils;
import kg.apc.jmeter.JMeterVariableEvaluator;
import kg.apc.jmeter.gui.Grid;

import javax.swing.event.TableModelListener;

public class FreeFormLoadPanel extends Grid implements ParamsPanel {
    private static final String[] columnIdentifiers = new String[]{"Start Value", "End Value", "Duration"};
    private static final Class[] columnClasses = new Class[]{String.class, String.class, String.class};
    private static final String[] defaultValues = new String[]{"1", "10", "60"};

    public FreeFormLoadPanel() {
        super("Threads Schedule", columnIdentifiers, columnClasses, defaultValues);
    }

    @Override
    public void modelToUI(AbstractDynamicThreadGroup tg) {
        if (tg instanceof FreeFormArrivalsThreadGroup) {
            FreeFormArrivalsThreadGroup ffatg = (FreeFormArrivalsThreadGroup) tg;
            JMeterPluginsUtils.collectionPropertyToTableModelRows(ffatg.getData(), getModel());
        }
    }

    @Override
    public void UItoModel(AbstractDynamicThreadGroup tg, JMeterVariableEvaluator evaluator) {
        if (tg instanceof FreeFormArrivalsThreadGroup) {
            FreeFormArrivalsThreadGroup ffatg = (FreeFormArrivalsThreadGroup) tg;
            ffatg.setData(getModel());
        }
    }

    @Override
    public void clearUI() {
        getModel().clearData();
    }

    public void addTableModelListener(TableModelListener listener) {
        getModel().addTableModelListener(listener);
    }
}
