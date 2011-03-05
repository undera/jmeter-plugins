// TODO: implement load scheme reading from property
package kg.apc.jmeter.timers;

import java.util.Iterator;
import java.util.List;
import org.apache.jmeter.engine.util.NoThreadClone;
import org.apache.jmeter.testelement.AbstractTestElement;
import org.apache.jmeter.testelement.property.CollectionProperty;
import org.apache.jmeter.testelement.property.JMeterProperty;
import org.apache.jmeter.testelement.property.NullProperty;
import org.apache.jmeter.threads.JMeterContextService;
import org.apache.jmeter.timers.ConstantThroughputTimer;
import org.apache.jmeter.timers.Timer;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/**
 *
 * @author undera
 * @see ConstantThroughputTimer
 */
public class VariableThroughputTimer
        extends AbstractTestElement
        implements Timer, NoThreadClone {

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

    public long delay() {
        synchronized (this) {

            while (true) {
                int delay = 0;
                long curTime = System.currentTimeMillis();
                long msecs = curTime % 1000;
                long secs = curTime - msecs;
                checkNextSecond(secs);
                if (rps < 0) {
                    log.info("No further RPS schedule, asking threads to stop...");
                    JMeterContextService.getContext().getEngine().askThreadsToStop();
                    notifyAll();
                } else {
                    delay = getDelay(msecs);
                }

                if (delay < 1) {
                    notify();
                    break;
                }
                cntDelayed++;
                try {
                    log.debug("Waiting for " + delay);
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
        if (time != secs) {
            if (startSec == 0) {
                startSec = secs;
            }
            time = secs;
            rps = getRPSForSecond((secs - startSec) / 1000);
            log.debug("Second changed " + ((secs - startSec) / 1000) + ", sleeping: " + cntDelayed + " sent " + cntSent + " RPS: " + rps);
            cntSent = 0;
            msecPerReq = 1000d / rps;
        }
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
        return getProperty(DATA_PROPERTY);
    }

    public int getRPSForSecond(long sec) {
        JMeterProperty data = getData();
        if (data instanceof NullProperty) {
            log.error("Got NullProperty");
            return 0;
        }

        int result = 0;
        CollectionProperty columns = (CollectionProperty) data;
        List<?> col = (List<?>) columns.get(DURATION_FIELD_NO).getObjectValue();
        if (col.isEmpty())
        {
            return -1;
        }
        
        Iterator<?> iter = col.iterator();
        int rowNo = 0;
        while (true) {
            JMeterProperty prop = (JMeterProperty) iter.next();
            int duration = prop.getIntValue();
            if (sec - duration <= 0) {
                int from = getIntCell(columns, rowNo, FROM_FIELD_NO);
                int to = getIntCell(columns, rowNo, TO_FIELD_NO);
                result = from + (int) (sec * ((to - from) / (double) duration));
                //log.info(sec+" "+from+" "+to+" "+result);
                break;
            } else {
                sec -= duration;
            }
            rowNo++;
            if (!iter.hasNext()) {
                return -1;
            }
        }

        return result;
    }

    private int getIntCell(CollectionProperty columns, int rowNo, int col) {
        List<?> list = (List<?>) columns.get(col).getObjectValue();
        JMeterProperty prop = (JMeterProperty) list.get(rowNo);
        return prop.getIntValue();
    }
}
