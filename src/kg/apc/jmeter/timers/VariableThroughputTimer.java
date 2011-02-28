package kg.apc.jmeter.timers;

import java.util.Iterator;
import java.util.List;
import org.apache.jmeter.engine.util.NoThreadClone;
import org.apache.jmeter.testelement.AbstractTestElement;
import org.apache.jmeter.testelement.property.CollectionProperty;
import org.apache.jmeter.testelement.property.JMeterProperty;
import org.apache.jmeter.testelement.property.NullProperty;
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
    private long leftToSend;
    private int sequenceSecond = 1; //FIXME: if thread waits more than a second, it breaks schedule
    private int rps;

    public long delay() {
        synchronized (this) {
            long curTime = System.currentTimeMillis();
            long msecs = curTime % 1000 + 1;
            long secs = curTime - msecs;
            checkNextSecond(secs);
            doDelay(msecs);
        }
        return 0;
    }

    private void doDelay(long msecs) {
        if (msecs < (1000 - leftToSend * msecPerReq)) {
            double delay= (leftToSend > 0) ? (rps / (double) leftToSend) : 1;
            log.info("Delaying " + cntDelayed + " for " + delay +" left "+leftToSend);
            cntDelayed++;
            try {
                wait((long) (1000 * delay * cntDelayed+ 1));
            } catch (InterruptedException ex) {
                log.error("Waiting thread was interrupted", ex);
            }
            log.info("Woke up " + cntDelayed);
            cntDelayed--;
        }
        leftToSend--;
    }

    private synchronized void checkNextSecond(long secs) {
        // next second
        if (time != secs) {
            time = secs;
            rps = getNextSecondRPS();
            log.info("Second changed, left: " + leftToSend + " RPS: " + rps);
            if (leftToSend > 0) {
                notify();
            }
            leftToSend += rps;
            msecPerReq = leftToSend > 0 ? (1000d / leftToSend) : 0;
        }
    }

    void setData(CollectionProperty rows) {
        setProperty(rows);
    }

    JMeterProperty getData() {
        return getProperty(DATA_PROPERTY);
    }

    private int getNextSecondRPS() {
        JMeterProperty data = getData();
        if (data instanceof NullProperty) {
            log.error("Got NullProperty");
            return -1;
        }

        int result = 0;
        CollectionProperty columns = (CollectionProperty) data;
        List<?> col = (List<?>) columns.get(DURATION_FIELD_NO).getObjectValue();
        Iterator<?> iter = col.iterator();
        int sec = sequenceSecond;
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
                notifyAll();
                break;
            }
        }

        sequenceSecond++;
        return result;
    }

    private int getIntCell(CollectionProperty columns, int rowNo, int col) {
        List<?> list = (List<?>) columns.get(col).getObjectValue();
        JMeterProperty prop = (JMeterProperty) list.get(rowNo);
        return prop.getIntValue();
    }
}
