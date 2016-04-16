package kg.apc.jmeter.threads;

import kg.apc.jmeter.JMeterPluginsUtils;
import org.apache.jmeter.gui.util.PowerTableModel;
import org.apache.jmeter.testelement.TestStateListener;
import org.apache.jmeter.testelement.property.CollectionProperty;
import org.apache.jmeter.testelement.property.JMeterProperty;
import org.apache.jmeter.testelement.property.NullProperty;
import org.apache.jmeter.testelement.property.PropertyIterator;
import org.apache.jmeter.threads.JMeterThread;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class UltimateThreadGroup
        extends AbstractSimpleThreadGroup
        implements Serializable, TestStateListener {

    private static final Logger log = LoggingManager.getLoggerForClass();

    public static final String DATA_PROPERTY = "ultimatethreadgroupdata";
    public static final String EXTERNAL_DATA_PROPERTY = "threads_schedule";

    public static final int START_THREADS_CNT_FIELD_NO = 0;
    public static final int INIT_DELAY_FIELD_NO = 1;
    public static final int STARTUP_TIME_FIELD_NO = 2;
    public static final int HOLD_LOAD_FOR_FIELD_NO = 3;
    public static final int SHUTDOWN_TIME_FIELD_NO = 4;

    private PropertyIterator scheduleIT;
    private int threadsToSchedule;
    private CollectionProperty currentRecord;

    public UltimateThreadGroup() {
        super();
    }

    @Override
    protected void scheduleThread(JMeterThread thread, long tgStartTime) {
        log.debug("Scheduling thread: " + thread.getThreadName());
        if (threadsToSchedule < 1) {
            if (!scheduleIT.hasNext()) {
                throw new RuntimeException("Not enough schedule records for thread #" + thread.getThreadName());
            }

            currentRecord = (CollectionProperty) scheduleIT.next();
            threadsToSchedule = currentRecord.get(0).getIntValue();
        }

        int numThreads = currentRecord.get(START_THREADS_CNT_FIELD_NO).getIntValue();
        int initialDelay = currentRecord.get(INIT_DELAY_FIELD_NO).getIntValue();
        int startRampUp = currentRecord.get(STARTUP_TIME_FIELD_NO).getIntValue();
        int flightTime = currentRecord.get(HOLD_LOAD_FOR_FIELD_NO).getIntValue();
        int endRampUp = currentRecord.get(SHUTDOWN_TIME_FIELD_NO).getIntValue();

        long ascentPoint = tgStartTime + 1000 * initialDelay;
        final int rampUpDelayForThread = (int) Math.floor(1000 * startRampUp * (double) threadsToSchedule / numThreads);
        long startTime = ascentPoint + rampUpDelayForThread;
        long descentPoint = startTime + 1000 * flightTime + 1000 * startRampUp - rampUpDelayForThread;

        thread.setStartTime(startTime);
        thread.setEndTime(descentPoint + (int) Math.floor(1000 * endRampUp * (double) threadsToSchedule / numThreads));

        thread.setScheduled(true);
        threadsToSchedule--;
    }

    public JMeterProperty getData() {
        JMeterProperty brokenProp = getProperty(EXTERNAL_DATA_PROPERTY);
        JMeterProperty usualProp = getProperty(DATA_PROPERTY);

        if (brokenProp instanceof CollectionProperty) {
            if (usualProp == null || usualProp instanceof NullProperty) {
                log.warn("Copying '" + EXTERNAL_DATA_PROPERTY + "' into '" + DATA_PROPERTY + "'");
                JMeterProperty newProp = brokenProp.clone();
                newProp.setName(DATA_PROPERTY);
                setProperty(newProp);
            }
            log.warn("Removing property '" + EXTERNAL_DATA_PROPERTY + "' as invalid");
            removeProperty(EXTERNAL_DATA_PROPERTY);
        }

        //log.info("getData: "+getProperty(DATA_PROPERTY));
        CollectionProperty overrideProp = getLoadFromExternalProperty();
        if (overrideProp != null) {
            return overrideProp;
        }

        return getProperty(DATA_PROPERTY);
    }

    public void setData(CollectionProperty rows) {
        //log.info("setData");
        setProperty(rows);
    }


    private CollectionProperty getLoadFromExternalProperty() {
        String loadProp = JMeterUtils.getProperty(EXTERNAL_DATA_PROPERTY);
        log.debug("Profile prop: " + loadProp);
        if (loadProp != null && loadProp.length() > 0) {
            //expected format : threads_schedule="spawn(1,1s,1s,1s,1s) spawn(2,1s,3s,1s,2s)"
            log.info("GUI threads profile will be ignored");
            PowerTableModel dataModel = new PowerTableModel(UltimateThreadGroupGui.columnIdentifiers, UltimateThreadGroupGui.columnClasses);
            String[] chunks = loadProp.split("\\)");

            for (String chunk : chunks) {
                try {
                    parseChunk(chunk, dataModel);
                } catch (RuntimeException e) {
                    log.warn("Wrong  chunk ignored: " + chunk, e);
                }
            }

            log.info("Setting threads profile from property " + EXTERNAL_DATA_PROPERTY + ": " + loadProp);
            return JMeterPluginsUtils.tableModelRowsToCollectionProperty(dataModel, UltimateThreadGroup.DATA_PROPERTY);
        }
        return null;
    }

    private static void parseChunk(String chunk, PowerTableModel model) {
        log.debug("Parsing chunk: " + chunk);
        String[] parts = chunk.split("[(,]");
        String loadVar = parts[0].trim();

        if (loadVar.equalsIgnoreCase("spawn")) {
            Integer[] row = new Integer[5];
            row[START_THREADS_CNT_FIELD_NO] = Integer.parseInt(parts[1].trim());
            row[INIT_DELAY_FIELD_NO] = JMeterPluginsUtils.getSecondsForShortString(parts[2]);
            row[STARTUP_TIME_FIELD_NO] = JMeterPluginsUtils.getSecondsForShortString(parts[3]);
            row[HOLD_LOAD_FOR_FIELD_NO] = JMeterPluginsUtils.getSecondsForShortString(parts[4]);
            row[SHUTDOWN_TIME_FIELD_NO] = JMeterPluginsUtils.getSecondsForShortString(parts[5]);
            model.addRow(row);
        } else {
            throw new RuntimeException("Unknown load type: " + parts[0]);
        }
    }

    @Override
    public int getNumThreads() {
        int result = 0;

        JMeterProperty threadValues = getData();
        if (!(threadValues instanceof NullProperty)) {
            CollectionProperty columns = (CollectionProperty) threadValues;
            List<?> rows = (List<?>) columns.getObjectValue();
            for (Object row1 : rows) {
                CollectionProperty prop = (CollectionProperty) row1;
                ArrayList<JMeterProperty> row = (ArrayList<JMeterProperty>) prop.getObjectValue();
                //log.info(prop.getStringValue());
                result += row.get(0).getIntValue();
            }
        }

        return result;
    }

    @Override
    public void testStarted() {
        JMeterProperty data = getData();
        if (!(data instanceof NullProperty)) {
            scheduleIT = ((CollectionProperty) data).iterator();
        }
        threadsToSchedule = 0;
    }

    @Override
    public void testStarted(String host) {
        testStarted();
    }

    @Override
    public void testEnded() {
    }

    @Override
    public void testEnded(String host) {
        testEnded();
    }


}
