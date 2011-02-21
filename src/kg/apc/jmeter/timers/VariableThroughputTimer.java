package kg.apc.jmeter.timers;

import kg.apc.jmeter.threads.UltimateThreadGroup;
import org.apache.jmeter.engine.util.NoThreadClone;
import org.apache.jmeter.testelement.AbstractTestElement;
import org.apache.jmeter.testelement.property.CollectionProperty;
import org.apache.jmeter.testelement.property.JMeterProperty;
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

    private static final Logger log = LoggingManager.getLoggerForClass();
    private long cntSent;
    private long cntDelayed;
    private long time = 0;
    private long curTime; // put this in fields because we don't want create variables in tight loops
    private long secs;
    private long curRps;
    private double msecPerReq;
    private long msecs;

    public long delay() {
        // FIXME: possible bottleneck, use more special mutex
        synchronized (this) {
            curTime = System.currentTimeMillis();
            msecs=curTime % 1000+1;
            secs = curTime - msecs;

            // next second
            if (time != secs) {
                time = secs;
                curRps = getCurrentRPS();
                msecPerReq = 1000d / curRps;
                cntSent = 0; //cntDelayed > 0 ? 1 : 0; // if we have scheduled sample from previous second
                if (cntDelayed > 0) {
                    cntDelayed--; // reduce delayed samples count
                }
                log.info("Second changed");
            }

            if (msecs >= (int)(cntSent * msecPerReq)) {
                //log.info("Sending\t"+msecs+"\t"+(int)(cntSent*msecPerReq)+"\t"+cntSent+"\t"+cntDelayed);
                cntSent++;
                return 0;
            } else {
                log.info("Delaying\t"+msecs+"\t"+(int)(cntSent*msecPerReq)+"\t"+cntSent+"\t"+cntDelayed);
                cntDelayed++;
                long d = 1001 * cntDelayed;
                return d;
            }
        }
    }

    void setData(CollectionProperty rows) {
        setProperty(rows);
    }

    JMeterProperty getData() {
        JMeterProperty prop = getProperty(UltimateThreadGroup.DATA_PROPERTY);
        return prop;
    }

    private int getCurrentRPS() {
        return 1350;
    }
}
