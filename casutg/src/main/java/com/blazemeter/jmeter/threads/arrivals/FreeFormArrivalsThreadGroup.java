package com.blazemeter.jmeter.threads.arrivals;

import kg.apc.jmeter.JMeterPluginsUtils;
import org.apache.jmeter.engine.StandardJMeterEngine;
import org.apache.jmeter.gui.util.PowerTableModel;
import org.apache.jmeter.testelement.property.CollectionProperty;
import org.apache.jmeter.testelement.property.JMeterProperty;
import org.apache.jmeter.threads.ListenerNotifier;
import org.apache.jorphan.collections.ListedHashTree;

public class FreeFormArrivalsThreadGroup extends ArrivalsThreadGroup {
    public static final String SCHEDULE = "Schedule";

    public void setData(PowerTableModel model) {
        CollectionProperty prop = JMeterPluginsUtils.tableModelRowsToCollectionProperty(model, SCHEDULE);
        setProperty(prop);
    }

    public CollectionProperty getData() {
        JMeterProperty prop = getProperty(SCHEDULE);
        if (prop instanceof CollectionProperty) {
            return (CollectionProperty) prop;
        } else {
            return new CollectionProperty();
        }
    }

    @Override
    protected Thread getThreadStarter(int groupIndex, ListenerNotifier listenerNotifier, ListedHashTree testTree, StandardJMeterEngine engine) {
        return new FreeFormArrivalsThreadStarter(groupIndex, listenerNotifier, testTree, engine, this);
    }
}
