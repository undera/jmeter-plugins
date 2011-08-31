package kg.apc.jmeter;

import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.SigarProxy;

/**
 *
 * @author undera
 */
public abstract class AbstractPerfMonMetric {

    protected final SigarProxy sigarProxy;

    public AbstractPerfMonMetric(SigarProxy aSigar) {
        sigarProxy = aSigar;
    }

    abstract public void getValue(StringBuilder res) throws SigarException;
}
