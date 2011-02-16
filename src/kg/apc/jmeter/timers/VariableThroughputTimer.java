package kg.apc.jmeter.timers;

import org.apache.jmeter.testelement.AbstractTestElement;
import org.apache.jmeter.timers.ConstantThroughputTimer;
import org.apache.jmeter.timers.Timer;

/**
 *
 * @author undera
 * @see ConstantThroughputTimer
 */
public class VariableThroughputTimer  extends AbstractTestElement
        implements Timer
{

    public long delay() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
