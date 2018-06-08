// TODO: fight with lagging on start
// TODO: create a thread which will wake up at least one sampler to provide rps
package kg.apc.jmeter.timers;

import kg.apc.jmeter.JMeterPluginsUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.jmeter.engine.StandardJMeterEngine;
import org.apache.jmeter.engine.util.NoThreadClone;
import org.apache.jmeter.gui.util.PowerTableModel;
import org.apache.jmeter.testelement.AbstractTestElement;
import org.apache.jmeter.testelement.TestStateListener;
import org.apache.jmeter.testelement.property.CollectionProperty;
import org.apache.jmeter.testelement.property.JMeterProperty;
import org.apache.jmeter.testelement.property.NullProperty;
import org.apache.jmeter.testelement.property.PropertyIterator;
import org.apache.jmeter.threads.AbstractThreadGroup;
import org.apache.jmeter.threads.JMeterContextService;
import org.apache.jmeter.timers.Timer;
import org.apache.jmeter.util.JMeterUtils;
import org.slf4j.LoggerFactory;

import java.util.List;

public class VariableThroughputTimer
        extends AbstractTestElement
        implements Timer, NoThreadClone, TestStateListener {

    private static final long serialVersionUID = -8557540133988335686L;
    protected static final String[] columnIdentifiers = new String[]{
            "Start RPS", "End RPS", "Duration, sec"
    };
    protected static final Class[] columnClasses = new Class[]{
            String.class, String.class, String.class
    };
    // TODO: eliminate magic property
    public static final String DATA_PROPERTY = "load_profile";
    public static final int DURATION_FIELD_NO = 2;
    public static final int FROM_FIELD_NO = 0;
    public static final int TO_FIELD_NO = 1;
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(VariableThroughputTimer.class);
    /* put this in fields because we don't want create variables in tight loops */
    /**
     * Current threads waiting, if &lt; 1 it means we don't have enough threads to reach RPS
     */
    private int cntDelayed;
    /**
     * Current second in Millis
     */
    private double time = 0;
    /**
     * How many requests per millis
     */
    private double msecPerReq;
    /**
     * Number of samples sent since last second
     */
    private long cntSent;
    /**
     * Current RPS
     */
    private double rps;
    private double startSec = 0;
    private CollectionProperty overrideProp;
    private int stopTries;
    private double lastStopTry;
    private boolean stopping;

    public VariableThroughputTimer() {
        super();
        trySettingLoadFromProperty();
    }

    /**
     * Internally handles that delay for caller thread
     *
     * @return 0
     */
    public synchronized long delay() {
        while (true) {
            long curTimeMs = System.currentTimeMillis();
            long millisSinceLastSecond = curTimeMs % 1000;
            long nowInMsRoundedAtSec = curTimeMs - millisSinceLastSecond;
            checkNextSecond(nowInMsRoundedAtSec);
            int delayMs = getDelay(millisSinceLastSecond);

            if (stopping) {
                delayMs = delayMs > 0 ? 10 : 0;
                notify(); // NOSONAR Don't notifyAll as cost is too big in terms of performances
            }

            if (delayMs < 1) {
                notify(); // NOSONAR Don't notifyAll as cost is too big in terms of performances
                break;
            }
            cntDelayed++;
            try {
                wait(delayMs);
            } catch (InterruptedException ex) {
                log.debug("Waiting thread was interrupted", ex);
                Thread.currentThread().interrupt();
            }
            cntDelayed--;
        }
        cntSent++;
        return 0;
    }

    /**
     * If we have switched to next second:
     * <li>
     * <ul>Updates time</ul>
     * <ul>Updates startSec</ul>
     * <ul>Resets cntSent</ul>
     * </li>
     *
     * @param nowInMsRoundedAtSec Now in millis rounded as second
     */
    private synchronized void checkNextSecond(double nowInMsRoundedAtSec) {
        // next second
        if (time == nowInMsRoundedAtSec) {
            return;
        }

        if (startSec == 0) {
            startSec = nowInMsRoundedAtSec;
        }
        time = nowInMsRoundedAtSec;

        Pair<Double, Long> pair = getRPSForSecond((nowInMsRoundedAtSec - startSec) / 1000);
        double nextRps = pair.getLeft();
        if (nextRps < 0) {
            stopping = true;
            int factor = stopTries > 10 ? 2 : 1;
            rps = rps > 0 ? rps * factor : 1;
            stopTest();
            notifyAll();
        } else {
            rps = nextRps;
        }

        if (log.isDebugEnabled()) {
            log.debug("Second changed {} , waiting: {}, samples sent {}, current rps: {} rps",
                    ((nowInMsRoundedAtSec - startSec) / 1000), cntDelayed, cntSent, rps);
        }

        if (cntDelayed < 1 && cntSent < rps) {
            log.warn("No free threads available in current Thread Group {}, made {} samples/s for expected rps {} samples/s, increase your number of threads",
                    JMeterContextService.getContext().getThreadGroup().getName(), cntSent, rps);
        }

        String elementName = getName();
        JMeterUtils.setProperty(elementName + "_totalDuration", String.valueOf(pair.getRight()));
        JMeterUtils.setProperty(elementName + "_cntDelayed", String.valueOf(cntDelayed));
        JMeterUtils.setProperty(elementName + "_cntSent", String.valueOf(cntSent));
        JMeterUtils.setProperty(elementName + "_rps", String.valueOf(rps));

        cntSent = 0;
        msecPerReq = 1000d / rps;
    }

    /**
     * @param millisSinceLastSecond Millis since last second tick
     * @return delay in Millis to apply at current millis, < 0 if no delay
     */
    private int getDelay(long millisSinceLastSecond) {
        if (log.isDebugEnabled()) {
            log.debug("Calculating {} {} {}", millisSinceLastSecond, cntSent * msecPerReq, cntSent);
        }
        if (millisSinceLastSecond < (cntSent * msecPerReq)) {
            // TODO : Explain this for other maintainers
            // cntDelayed + 1 : Current threads waiting + this thread
            return (int) (1 + 1000.0 * (cntDelayed + 1) / rps);
        }
        // we're under rate
        return 0;
    }

    public void setData(CollectionProperty rows) {
        setProperty(rows);
    }

    public JMeterProperty getData() {
        if (overrideProp != null) {
            return overrideProp;
        }
        return getProperty(DATA_PROPERTY);
    }

    /**
     * @param elapsedSinceStartOfTestSec Elapsed time since start of test in seconds
     * @return double RPS at that second or -1 if we're out of schedule
     */
    public Pair<Double, Long> getRPSForSecond(final double elapsedSinceStartOfTestSec) {
        JMeterProperty data = getData();
        if (data instanceof NullProperty) {
            return Pair.of(-1.0, 0L);
        }
        CollectionProperty rows = (CollectionProperty) data;
        PropertyIterator scheduleIT = rows.iterator();
        double newSec = elapsedSinceStartOfTestSec;
        double result = -1;
        boolean resultComputed = false;
        long totalDuration = 0;
        while (scheduleIT.hasNext()) {
            @SuppressWarnings("unchecked")
            List<Object> curProp = (List<Object>) scheduleIT.next().getObjectValue();
            int duration = getIntValue(curProp, DURATION_FIELD_NO);
            totalDuration += duration;
            if (!resultComputed) {
                double fromRps = getDoubleValue(curProp, FROM_FIELD_NO);
                double toRps = getDoubleValue(curProp, TO_FIELD_NO);
                if (newSec - duration <= 0) {
                    result = fromRps + newSec * (toRps - fromRps) / (double) duration;
                    resultComputed = true;
                } else {
                    // We're not yet in the slot
                    newSec -= duration;
                }
            }
        }
        return Pair.of(result, totalDuration);
    }

    private double getDoubleValue(List<Object> prop, int colID) {
        JMeterProperty val = (JMeterProperty) prop.get(colID);
        return val.getDoubleValue();
    }

    private int getIntValue(List<Object> prop, int colID) {
        JMeterProperty val = (JMeterProperty) prop.get(colID);
        return val.getIntValue();
    }

    private void trySettingLoadFromProperty() {
        String loadProp = JMeterUtils.getProperty(DATA_PROPERTY);
        log.debug("Loading property: {}={}", DATA_PROPERTY, loadProp);
        if (!StringUtils.isEmpty(loadProp)) {
            log.info("GUI load profile will be ignored as property {} is defined", DATA_PROPERTY);
            PowerTableModel dataModel = new PowerTableModel(VariableThroughputTimer.columnIdentifiers, VariableThroughputTimer.columnClasses);

            String[] chunks = loadProp.split("\\)");

            for (String chunk : chunks) {
                try {
                    parseChunk(chunk, dataModel);
                } catch (RuntimeException e) {
                    log.warn("Wrong load chunk {} will be ignored", chunk, e);
                }
            }

            log.info("Setting load profile from property {}: {}", DATA_PROPERTY, loadProp);
            overrideProp = JMeterPluginsUtils.tableModelRowsToCollectionProperty(dataModel, VariableThroughputTimer.DATA_PROPERTY);
        }
    }

    private static void parseChunk(String chunk, PowerTableModel model) {
        log.debug("Parsing chunk: {}", chunk);
        String[] parts = chunk.split("[(,]");
        String loadVar = parts[0].trim();

        if (loadVar.equalsIgnoreCase("const")) {
            int constLoad = Integer.parseInt(parts[1].trim());
            Integer[] row = new Integer[3];
            row[FROM_FIELD_NO] = constLoad;
            row[TO_FIELD_NO] = constLoad;
            row[DURATION_FIELD_NO] = JMeterPluginsUtils.getSecondsForShortString(parts[2]);
            model.addRow(row);

        } else if (loadVar.equalsIgnoreCase("line")) {
            Integer[] row = new Integer[3];
            row[FROM_FIELD_NO] = Integer.parseInt(parts[1].trim());
            row[TO_FIELD_NO] = Integer.parseInt(parts[2].trim());
            row[DURATION_FIELD_NO] = JMeterPluginsUtils.getSecondsForShortString(parts[3]);
            model.addRow(row);

        } else if (loadVar.equalsIgnoreCase("step")) {
            // FIXME: float (from-to)/inc will be stepped wrong
            int from = Integer.parseInt(parts[1].trim());
            int to = Integer.parseInt(parts[2].trim());
            int inc = Integer.parseInt(parts[3].trim()) * (from > to ? -1 : 1);
            log.debug("step from {} to {} with step {}", from, to, inc);
            for (int n = from; (inc > 0 ? n <= to : n > to); n += inc) {
                Integer[] row = new Integer[3];
                row[FROM_FIELD_NO] = n;
                row[TO_FIELD_NO] = n;
                row[DURATION_FIELD_NO] = JMeterPluginsUtils.getSecondsForShortString(parts[4]);
                log.debug("Adding row from {} to {} with duration {}s", row[FROM_FIELD_NO],
                        row[TO_FIELD_NO], row[DURATION_FIELD_NO]);
                model.addRow(row);
            }
        } else {
            throw new IllegalArgumentException("Unknown load type: " + parts[0]);
        }
    }

    protected void stopTest() {
        if (stopTries > 30) {
            throw new IllegalStateException("More than 30 retries - stopping with exception");
        }

        if (lastStopTry == time) {
            return;
        }
        log.info("No further RPS schedule, asking threads to stop...");
        lastStopTry = time;
        stopTries++;
        if (stopTries > 10) {
            log.info("Tries more than 10, stopping engine NOW!");
            StandardJMeterEngine.stopEngineNow();
        } else if (stopTries > 5) {
            log.info("Tries more than 5, shutting down engine!");
            StandardJMeterEngine.stopEngine();
        } else if (stopTries > 3) {
            AbstractThreadGroup threadGroup = JMeterContextService.getContext().getThreadGroup();
            log.info("Tries more than 3, hard stopping threads of Thread Group : {}", threadGroup.getName());
            threadGroup.tellThreadsToStop();
        } else {
            AbstractThreadGroup threadGroup = JMeterContextService.getContext().getThreadGroup();
            log.info("Stopping gracefuly threads of Thread Group : {}", threadGroup.getName());
            threadGroup.stop();
        }
    }

    @Override
    public void testStarted() {
        stopping = false;
        stopTries = 0;
    }

    @Override
    public void testStarted(String string) {
        testStarted();
    }

    @Override
    public void testEnded() {
        // NOOP
    }

    @Override
    public void testEnded(String string) {
        testEnded();
    }
}
