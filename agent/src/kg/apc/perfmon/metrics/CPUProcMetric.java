package kg.apc.perfmon.metrics;

import java.util.Arrays;
import org.hyperic.sigar.ProcCpu;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.SigarProxy;

/**
 *
 * @author undera
 */
class CPUProcMetric extends AbstractCPUMetric {

    public static final byte PERCENT = 0;
    public static final byte TOTAL = 1;
    public static final byte SYSTEM = 2;
    public static final byte USER = 3;
    public static final String[] types = {"percent", "total", "system", "user"};
    private int type = -1;
    private double prev = -1;

    public CPUProcMetric(SigarProxy aSigar, MetricParams params) {
        super(aSigar, params);
        type = Arrays.asList(types).indexOf(params.type);
        if (type < 0) {
            type = PERCENT;
        }
    }

    public void getValue(StringBuilder res) throws SigarException {
        ProcCpu cpu = sigarProxy.getProcCpu(params.PID);
        double val;
        long cur;

        switch (type) {
            case PERCENT:
                val = cpu.getPercent();
                break;
            case TOTAL:
                cur = cpu.getTotal();
                val = prev > 0 ? cur - prev : 0;
                prev = cur;
                break;
            case SYSTEM:
                cur = cpu.getSys();
                val = prev > 0 ? cur - prev : 0;
                prev = cur;
                break;
            case USER:
                cur = cpu.getUser();
                val = prev > 0 ? cur - prev : 0;
                prev = cur;
                break;
            default:
                throw new SigarException("Unknown proc cpu type " + type);
        }

        res.append(Double.toString(val));
    }
}
