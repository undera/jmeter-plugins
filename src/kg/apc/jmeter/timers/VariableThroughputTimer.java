package kg.apc.jmeter.timers;

import kg.apc.jmeter.threads.UltimateThreadGroup;
import org.apache.jmeter.testelement.AbstractTestElement;
import org.apache.jmeter.testelement.property.CollectionProperty;
import org.apache.jmeter.testelement.property.JMeterProperty;
import org.apache.jmeter.timers.ConstantThroughputTimer;
import org.apache.jmeter.timers.Timer;

/**
 *
 * @author undera
 * @see ConstantThroughputTimer
 */
public class VariableThroughputTimer extends AbstractTestElement
        implements Timer {

    public long delay() {
        return 0;
    }

    void setData(CollectionProperty rows) {
        setProperty(rows);
    }

    JMeterProperty getData() {
        JMeterProperty prop = getProperty(UltimateThreadGroup.DATA_PROPERTY);
        return prop;
    }
}
