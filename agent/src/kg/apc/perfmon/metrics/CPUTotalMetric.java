/**
 * type - user, system, io, idle
 * core ID
 * process id
 * image name
 * 
 * 
 */
package kg.apc.perfmon.metrics;

import java.util.Arrays;
import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.SigarProxy;

/**
 *
 * @author undera
 */
class CPUTotalMetric extends AbstractCPUMetric {

    public static final byte combined = 0;
    public static final byte idle = 1;
    public static final byte irq = 2;
    public static final byte nice = 3;
    public static final byte softirq = 4;
    public static final byte stolen = 5;
    public static final byte system = 6;
    public static final byte user = 7;
    public static final byte iowait = 8;
    public static final String[] types = {"combined", "idle", "irq", "nice", "softirq",
        "stolen", "system", "user", "iowait"};
    private int type = -1;

    public CPUTotalMetric(SigarProxy aSigar, MetricParams params) {
        super(aSigar, params);
        type = Arrays.asList(types).indexOf(params.type);
        if (type < 0) {
            type = combined;
        }
    }

    public void getValue(StringBuilder res) throws SigarException {
        CpuPerc cpu = sigarProxy.getCpuPerc();
        sigarProxy.getCpuPercList();
        double val;
        switch (type) {

            case combined:
                val = cpu.getCombined();
                break;
            case idle:
                val = cpu.getIdle();
                break;
            case irq:
                val = cpu.getIrq();
                break;
            case nice:
                val = cpu.getNice();
                break;
            case softirq:
                val = cpu.getSoftIrq();
                break;
            case stolen:
                val = cpu.getStolen();
                break;
            case system:
                val = cpu.getSys();
                break;
            case user:
                val = cpu.getUser();
                break;
            case iowait:
                val = cpu.getWait();
                break;
            default:
                throw new SigarException("Unknown proc cpu type " + type);
        }
        res.append(Double.toString(100*val));
    }
}
