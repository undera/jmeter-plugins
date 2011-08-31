package kg.apc.jmeter;

import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.SigarProxy;

/**
 *
 * @author undera
 */
class CPUPerfMetric extends AbstractPerfMonMetric {

    public CPUPerfMetric(SigarProxy aSigar) {
        super(aSigar);
    }

    @Override
    public void getValue(StringBuilder res) throws SigarException {
        res.append(Double.toString(sigarProxy.getCpuPerc().getCombined()));
    }
}
