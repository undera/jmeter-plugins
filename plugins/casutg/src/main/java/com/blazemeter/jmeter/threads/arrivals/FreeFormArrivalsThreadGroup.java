package com.blazemeter.jmeter.threads.arrivals;

import kg.apc.jmeter.JMeterPluginsUtils;
import org.apache.jmeter.engine.StandardJMeterEngine;
import org.apache.jmeter.gui.util.PowerTableModel;
import org.apache.jmeter.testelement.property.CollectionProperty;
import org.apache.jmeter.testelement.property.JMeterProperty;
import org.apache.jmeter.testelement.property.NullProperty;
import org.apache.jmeter.threads.ListenerNotifier;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.collections.ListedHashTree;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

public class FreeFormArrivalsThreadGroup extends ArrivalsThreadGroup {
    public static final String SCHEDULE = "Schedule";
    public static final String DEFAULT_IDENTIFIER_PROPERTY_NAME = "arrivals.schedule";
    private static final Logger log = LoggingManager.getLoggerForClass();
    private static final int START_THREADS_CNT_FIELD_NO = 0;
    private static final int END_THREADS_CNT_FIELD_NO = 1;
    private static final int DURATION_TIME_NO = 2;


    private static void parseChunk(String chunk, PowerTableModel model) {

        String[] parts = chunk.split("[(,]");
        String loadVar = parts[0].trim();

        if (loadVar.equalsIgnoreCase("spawn")) {
            Integer[] row = new Integer[3];
            row[START_THREADS_CNT_FIELD_NO] = Integer.parseInt(parts[1].trim());
            row[END_THREADS_CNT_FIELD_NO] = Integer.parseInt(parts[2]);
            row[DURATION_TIME_NO] = JMeterPluginsUtils.getSecondsForShortString(parts[3]);
            model.addRow(row);
        } else {
            throw new RuntimeException("Unknown load type: " + parts[0]);
        }
    }

    public CollectionProperty getData() {
        JMeterProperty prop = getProperty(SCHEDULE);
        String propertyName = getIdentifier().isEmpty() ? DEFAULT_IDENTIFIER_PROPERTY_NAME : getIdentifier();
        if (!(prop instanceof CollectionProperty)) {
            prop = new CollectionProperty();
        }

        JMeterProperty brokenProp = getProperty(propertyName);
        JMeterProperty usualProp = getProperty(SCHEDULE);

        if (brokenProp instanceof CollectionProperty) {
            if (usualProp == null || usualProp instanceof NullProperty) {
                log.warn("Copying '" + propertyName + "' into '" + SCHEDULE + "'");
                JMeterProperty newProp = brokenProp.clone();
                newProp.setName(SCHEDULE);
                setProperty(newProp);
            }
            log.warn("Removing property '" + propertyName + "' as invalid");
            removeProperty(propertyName);
        }
        if (getUseIdentifier()) {
            CollectionProperty overrideProp = getLoadFromExternalProperty(getIdentifier());
            if (overrideProp != null) {
                return overrideProp;
            }
        }
        return (CollectionProperty) prop;
    }

    public void setData(PowerTableModel model) {
        CollectionProperty prop = JMeterPluginsUtils.tableModelRowsToCollectionProperty(model, SCHEDULE);
        setProperty(prop);
    }

    private CollectionProperty getLoadFromExternalProperty(String propertyName) {
        String externalDataProperty = propertyName.isEmpty() ? DEFAULT_IDENTIFIER_PROPERTY_NAME : propertyName;
        String loadProp = JMeterUtils.getProperty(externalDataProperty);
        log.debug("Profile prop: " + loadProp);
        if (loadProp != null && loadProp.length() > 0) {
//            expected format : arrivals.schedule="spawn(1,13,16s) spawn(13,13,10m)"
            log.info("GUI threads profile will be ignored");
            PowerTableModel dataModel = new PowerTableModel(FreeFormLoadPanel.getColumnIdentifiers(), FreeFormLoadPanel.getColumnClasses());
            String[] chunks = loadProp.split("\\)");
            for (String chunk : chunks) {
                try {
                    parseChunk(chunk, dataModel);
                } catch (RuntimeException e) {
                    log.warn("Wrong  chunk ignored: " + chunk, e);
                }
            }
            log.debug("Setting threads profile from property " + externalDataProperty + ": " + loadProp);
            return JMeterPluginsUtils.tableModelRowsToCollectionProperty(dataModel, FreeFormArrivalsThreadGroup.SCHEDULE);
        }
        return null;
    }


    @Override
    protected Thread getThreadStarter(int groupIndex, ListenerNotifier listenerNotifier, ListedHashTree testTree, StandardJMeterEngine engine) {
        return new FreeFormArrivalsThreadStarter(groupIndex, listenerNotifier, testTree, engine, this);
    }
}
