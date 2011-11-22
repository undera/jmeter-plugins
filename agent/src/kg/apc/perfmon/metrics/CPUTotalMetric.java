/**
 * type - user, system, io, idle
 * core ID
 * process id
 * image name
 * 
 * 
 */
package kg.apc.perfmon.metrics;

import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.SigarProxy;

/**
 *
 * @author undera
 */
class CPUTotalMetric extends AbstractCPUMetric {

    public CPUTotalMetric(SigarProxy aSigar, MetricParams params) {
        super(aSigar, params);
    }

    public void getValue(StringBuilder res) throws SigarException {
        CpuPerc cpu = sigarProxy.getCpuPerc();
        sigarProxy.getCpuPercList();


        cpu.getCombined();
        cpu.getIdle();
        cpu.getIrq();
        cpu.getNice();
        cpu.getSoftIrq();
        cpu.getStolen();
        cpu.getSys();
        cpu.getUser();
        cpu.getWait();

        //res.append(Double.toString(cpu.getCombined()));
    }
}
