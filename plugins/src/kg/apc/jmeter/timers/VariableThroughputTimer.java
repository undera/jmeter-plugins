// TODO: fight with lagging on start
// TODO: create a thread which will wake up at least one sampler to provide rps
package kg.apc.jmeter.timers;

import java.util.ArrayList;
import kg.apc.jmeter.JMeterPluginsUtils;
import org.apache.jmeter.engine.StandardJMeterEngine;
import org.apache.jmeter.engine.event.LoopIterationEvent;
import org.apache.jmeter.engine.util.NoThreadClone;
import org.apache.jmeter.gui.util.PowerTableModel;
import org.apache.jmeter.testelement.AbstractTestElement;
import org.apache.jmeter.testelement.TestListener;
import org.apache.jmeter.testelement.property.CollectionProperty;
import org.apache.jmeter.testelement.property.JMeterProperty;
import org.apache.jmeter.testelement.property.NullProperty;
import org.apache.jmeter.testelement.property.PropertyIterator;
import org.apache.jmeter.threads.JMeterContextService;
import org.apache.jmeter.timers.ConstantThroughputTimer;
import org.apache.jmeter.timers.Timer;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/**
 *
 * @author undera
 * @see ConstantThroughputTimer
 */
public class VariableThroughputTimer
        extends AbstractTestElement
        implements Timer, NoThreadClone, TestListener {

    public static final String[] columnIdentifiers = new String[]{
        "Start RPS", "End RPS", "Duration, sec"
    };
    public static final Class[] columnClasses = new Class[]{
        String.class, String.class, String.class
    };
    // TODO: eliminate magic property
    public static final String DATA_PROPERTY = "load_profile";
    public static final int DURATION_FIELD_NO = 2;
    public static final int FROM_FIELD_NO = 0;
    public static final int TO_FIELD_NO = 1;
    private static final Logger log = LoggingManager.getLoggerForClass();
    /* put this in fields because we don't want create variables in tight loops */
    private long cntDelayed;
    private long time = 0;
    private double msecPerReq;
    private long cntSent;
    private int rps;
    private long startSec = 0;
    private CollectionProperty overrideProp;
    private int stopTries;
    private long lastStopTry;
    private boolean stopping;

    public VariableThroughputTimer() {
        super();
        trySettingLoadFromProperty();
    }

    public long delay() {
        synchronized (this) {

            while (true) {
                int delay = 0;
                long curTime = System.currentTimeMillis();
                long msecs = curTime % 1000;
                long secs = curTime - msecs;
                checkNextSecond(secs);
                delay = getDelay(msecs);

                if (stopping) {
                    delay = delay > 0 ? 10 : 0;
                    notify();
                }

                if (delay < 1) {
                    notify();
                    break;
                }
                cntDelayed++;
                try {
                    wait(delay);
                } catch (InterruptedException ex) {
                    log.error("Waiting thread was interrupted", ex);
                }
                cntDelayed--;
            }
            cntSent++;
        }
        return 0;
    }

    private synchronized void checkNextSecond(long secs) {
        // next second
        if (time == secs) {
            return;
        }

        if (startSec == 0) {
            startSec = secs;
        }
        time = secs;

        int nextRps = getRPSForSecond((secs - startSec) / 1000);
        if (nextRps < 0) {
            stopping = true;
            rps = rps > 0 ? rps * (stopTries > 10 ? 2 : 1) : 1;
            stopTest();
            notifyAll();
        } else {
            rps = nextRps;
        }

        if (log.isDebugEnabled()) {
            log.debug("Second changed " + ((secs - startSec) / 1000) + ", sleeping: " + cntDelayed + " sent " + cntSent + " RPS: " + rps);
        }

        if (cntDelayed < 1) {
            log.warn("No free threads left in worker pool, made  " + cntSent + '/' + rps + " samples");
        }

        cntSent = 0;
        msecPerReq = 1000d / rps;

    }

    private int getDelay(long msecs) {
        //log.info("Calculating "+msecs + " " + cntSent * msecPerReq+" "+cntSent);
        if (msecs < (cntSent * msecPerReq)) {
            int delay = 1 + (int) (1000.0 * (cntDelayed + 1) / (double) rps);
            return delay;
        }
        return 0;
    }

    void setData(CollectionProperty rows) {
        setProperty(rows);
    }

    JMeterProperty getData() {
        if (overrideProp != null) {
            return overrideProp;
        }
        return getProperty(DATA_PROPERTY);
    }

    public int getRPSForSecond(long sec) {
        JMeterProperty data = getData();
        if (data instanceof NullProperty) return -1;
        CollectionProperty rows = (CollectionProperty) data;
        PropertyIterator scheduleIT = rows.iterator();

        while (scheduleIT.hasNext()) {
            ArrayList<Object> curProp = (ArrayList<Object>) scheduleIT.next().getObjectValue();

            int duration = getIntValue(curProp, DURATION_FIELD_NO);
            int from = getIntValue(curProp, FROM_FIELD_NO);
            int to = getIntValue(curProp, TO_FIELD_NO);
            //log.debug("sec "+sec+" Dur: "+duration+" from "+from+" to "+to);
            if (sec - duration <= 0) {
                int rpsCalculated = from + (int) (sec * ((to - from) / (double) duration));
                //log.debug("RPS: "+rps);
                return rpsCalculated;
            } else {
                sec -= duration;
            }
        }
        return -1;
    }

    private int getIntValue(ArrayList<Object> prop, int colID) throws NumberFormatException {
        JMeterProperty val = (JMeterProperty) prop.get(colID);
        return val.getIntValue();
    }

    private void trySettingLoadFromProperty() {
        String loadProp = JMeterUtils.getProperty(DATA_PROPERTY);
        log.debug("Load prop: " + loadProp);
        if (loadProp != null && loadProp.length() > 0) {
            log.info("GUI load profile will be ignored");
            PowerTableModel dataModel = new PowerTableModel(VariableThroughputTimer.columnIdentifiers, VariableThroughputTimer.columnClasses);

            String[] chunks = loadProp.split("\\)");

            for (int c = 0; c < chunks.length; c++) {
                try {
                    parseChunk(chunks[c], dataModel);
                } catch (RuntimeException e) {
                    log.warn("Wrong load chunk ignored: " + chunks[c], e);
                }
            }

            log.info("Setting load profile from property " + DATA_PROPERTY + ": " + loadProp);
            overrideProp = JMeterPluginsUtils.tableModelRowsToCollectionProperty(dataModel, VariableThroughputTimer.DATA_PROPERTY);
        }
    }

    private static void parseChunk(String chunk, PowerTableModel model) {
        log.debug("Parsing chunk: " + chunk);
        String[] parts = chunk.split("[(,]");
        String loadVar = parts[0].trim();

        if (loadVar.equalsIgnoreCase("const")) {
            int const_load = Integer.parseInt(parts[1].trim());
            Integer[] row = new Integer[3];
            row[FROM_FIELD_NO] = const_load;
            row[TO_FIELD_NO] = const_load;
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
            //log.info(from + " " + to + " " + inc);
            for (int n = from; (inc > 0 ? n <= to : n > to); n += inc) {
                //log.info(" " + n);
                Integer[] row = new Integer[3];
                row[FROM_FIELD_NO] = n;
                row[TO_FIELD_NO] = n;
                row[DURATION_FIELD_NO] = JMeterPluginsUtils.getSecondsForShortString(parts[4]);
                model.addRow(row);
            }

        } else {
            throw new RuntimeException("Unknown load type: " + parts[0]);
        }
    }

    // TODO: resolve shutdown problems. Patch JMeter if needed
    // TODO: make something with test stopping in JMeter. Write custom plugin that tries to kill all threads? Guillotine Stopper! 
    protected void stopTest() {
        if (stopTries > 30) {
            throw new RuntimeException("More than 30 seconds - stopping by exception");
        }

        if (lastStopTry == time) {
            return;
        }
        log.info("No further RPS schedule, asking threads to stop...");
        lastStopTry = time;
        stopTries++;
        if (stopTries > 10) {
            log.info("Tries more than 10, stop it NOW!");
            StandardJMeterEngine.stopEngineNow();
        } else if (stopTries > 5) {
            log.info("Tries more than 5, stop it!");
            StandardJMeterEngine.stopEngine();
        } else {
            JMeterContextService.getContext().getEngine().askThreadsToStop();
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
    }

    @Override
    public void testEnded(String string) {
        testEnded();
    }

    @Override
    public void testIterationStart(LoopIterationEvent lie) {
    }
}
