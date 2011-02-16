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

   private int cnt;
    private long time=0;
    private long curTime; // put this in fields because we don't want create variables in tight loops
    private long secs;

    public long delay() {
        synchronized(this)
        {
            curTime=System.currentTimeMillis();
            secs=curTime-curTime%1000;
            if (time!=secs)
            {
                time=secs;
                cnt=0;
            }

            cnt++;

            if (cnt>=getCurrentRPS())
            {
               long d=1000*(cnt-getCurrentRPS());
               log.info("delaying for "+d);
                return d;
            }

        }

        return 0;
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
