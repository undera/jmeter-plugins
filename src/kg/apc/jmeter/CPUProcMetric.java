package kg.apc.jmeter;

import org.hyperic.sigar.ProcCpu;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.SigarProxy;

/**
 *
 * @author undera
 */
class CPUProcMetric extends AbstractCPUMetric {

    private long PID = -1;

    public CPUProcMetric(SigarProxy sigar) {
        super(sigar);
    }

    @Override
    public void getValue(StringBuilder res) throws SigarException {
        if (PID < 0) {
            PID = getPIDByProcName(null, 9);
        }
        if (PID > 0) {
            ProcCpu cpu = sigarProxy.getProcCpu(PID);
        }
    }
}
