package kg.apc.jmeter.threads;

import org.apache.jmeter.engine.event.LoopIterationEvent;
import org.apache.jmeter.testelement.TestListener;
import org.apache.jmeter.testelement.property.CollectionProperty;
import org.apache.jmeter.testelement.property.JMeterProperty;
import org.apache.jmeter.testelement.property.NullProperty;
import org.apache.jmeter.testelement.property.PropertyIterator;
import org.apache.jmeter.threads.JMeterThread;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class UltimateThreadGroup
        extends AbstractSimpleThreadGroup
        implements Serializable, TestListener {

    private static final Logger log = LoggingManager.getLoggerForClass();
    public static final String DATA_PROPERTY = "ultimatethreadgroupdata";
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

        int numThreads = currentRecord.get(0).getIntValue();
        int initialDelay = currentRecord.get(1).getIntValue();
        int startRampUp = currentRecord.get(2).getIntValue();
        int flightTime = currentRecord.get(3).getIntValue();
        int endRampUp = currentRecord.get(4).getIntValue();

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
        //log.info("getData: "+getProperty(DATA_PROPERTY));
        return getProperty(DATA_PROPERTY);
    }

    void setData(CollectionProperty rows) {
        //log.info("setData");
        setProperty(rows);
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

    @Override
    public void testIterationStart(LoopIterationEvent event) {
    }
}
