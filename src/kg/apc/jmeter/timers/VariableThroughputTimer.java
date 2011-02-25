package kg.apc.jmeter.timers;

import java.util.Iterator;
import java.util.List;
import org.apache.jmeter.engine.util.NoThreadClone;
import org.apache.jmeter.testelement.AbstractTestElement;
import org.apache.jmeter.testelement.property.CollectionProperty;
import org.apache.jmeter.testelement.property.JMeterProperty;
import org.apache.jmeter.testelement.property.NullProperty;
import org.apache.jmeter.testelement.property.PropertyIterator;
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
    private static final Logger log = LoggingManager.getLoggerForClass();
    /* put this in fields because we don't want create variables in tight loops */
    private long cntDelayed;
    private long time = 0;
    private double msecPerReq;
    private long leftToSend;
    private PropertyIterator rpsProfileIterator;
    private int curSecond = 0;

    public long delay() {
        // FIXME: possible bottleneck, use more special mutex
        synchronized (this) {
            long curTime = System.currentTimeMillis();
            long msecs = curTime % 1000 + 1;
            long secs = curTime - msecs;

            // next second
            if (time != secs) {
                time = secs;
                log.info("Second changed, left: " + leftToSend);
                leftToSend += getNextSecondRPS();
                msecPerReq = 1000d / leftToSend;
                if (cntDelayed > 0) {
                    leftToSend--;
                    cntDelayed--; // reduce delayed samples count
                }
            }

            if (msecs >= (1000 - leftToSend * msecPerReq)) {
                leftToSend--;
                return 0;
            } else {
                debug("Delaying ");
                cntDelayed++;
                return 1010 * cntDelayed;
            }
        }
    }

    private void debug(String str) {
        log.info(str + " "
                + +(1000 - leftToSend * msecPerReq) + " "
                + leftToSend + " " + cntDelayed);
    }

    void setData(CollectionProperty rows) {
        setProperty(rows);
        rpsProfileIterator = null;
    }

    JMeterProperty getData() {
        return getProperty(DATA_PROPERTY);
    }

    private int getNextSecondRPS() {
        if (rpsProfileIterator == null) {
            JMeterProperty data = getData();
            if (data instanceof NullProperty) {
                return -1;
            }
            CollectionProperty rows = (CollectionProperty) data;
            rpsProfileIterator = rows.iterator();
            curSecond = 0;
        }

        int result = 0;

        JMeterProperty data = getData();
        CollectionProperty columns = (CollectionProperty) data;
        List<?> col = (List<?>) columns.get(0).getObjectValue();
        Iterator<?> iter = col.iterator();
        while (iter.hasNext()) {
            JMeterProperty prop = (JMeterProperty) iter.next();
            result += prop.getIntValue();
        }

        log.error("" + result);
        return (result > 0) ? result : 100;
    }
}
