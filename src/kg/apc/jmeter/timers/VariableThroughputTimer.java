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
        implements Timer, NoThreadClone
{
   private static final Logger log= LoggingManager.getLoggerForClass();;

    private long cntSent;
    private long cntDelayed;
    private long time=0;
    private long curTime; // put this in fields because we don't want create variables in tight loops
    private long secs;
    private long curRps;
    private double msecPerReq;

    public long delay() {
        // FIXME: possible bottleneck, use more special mutex
        synchronized(this)
        {
            curTime=System.currentTimeMillis();
            secs=curTime-curTime%1000;

            // next second
            if (time!=secs)
            {
                time=secs;
                curRps=getCurrentRPS();
                msecPerReq=1000/curRps;
                cntSent=0;
                cntDelayed=0;
                //log.info("Second changed");
            }

            if ((curTime%1000)>(cntSent*msecPerReq))
            {
                cntSent++;
                //log.info("Sending "+curTime%1000+"/"+cntSent*msecPerReq);
                return 0;
            }
            else
            {
                cntDelayed++;
                long d=1010*(cntDelayed);
                //log.info("Delaying for "+d+"/"+cntDelayed+"/"+cntSent);
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
        return 50;
    }
}
