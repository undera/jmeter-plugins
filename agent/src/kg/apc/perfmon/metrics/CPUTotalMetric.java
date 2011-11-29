package kg.apc.perfmon.metrics;

import java.util.Arrays;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;
import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.SigarProxy;

/**
 *
 * @author undera
 */
class CPUTotalMetric extends AbstractCPUMetric {

    private static final Logger log = LoggingManager.getLoggerForClass();
    public static final byte COMBINED = 0;
    public static final byte IDLE = 1;
    public static final byte IRQ = 2;
    public static final byte NICE = 3;
    public static final byte SOFTIRQ = 4;
    public static final byte STOLEN = 5;
    public static final byte SYSTEM = 6;
    public static final byte USER = 7;
    public static final byte IOWAIT = 8;
    public static final String[] types = {"combined", "idle", "irq", "nice", "softirq",
        "stolen", "system", "user", "iowait"};
    private int type = -1;
    private int coreID = -1;

    protected CPUTotalMetric(SigarProxy aSigar, MetricParams params) {
        super(aSigar, params);

        if (params.type.isEmpty()) {
            type = COMBINED;
        } else {
            type = Arrays.asList(types).indexOf(params.type);
            if (type < 0) {
                throw new IllegalArgumentException("Invalid total cpu type: " + params.type);
            }
        }

        if (params.coreID >= 0) {
            int avail;
            try {
                avail = aSigar.getCpuList().length;
            } catch (SigarException ex) {
                throw new IllegalArgumentException("Cannot get CPU count at this system", ex);
            }
            if (params.coreID >= avail) {
                throw new IllegalArgumentException("Invalid core ID on this system: " + params.type);
            }
            coreID = params.coreID;
        }
    }

    public void getValue(StringBuilder res) throws SigarException {
        CpuPerc cpu;
        if (coreID < 0) {
            cpu = sigarProxy.getCpuPerc();
        } else {
            cpu = sigarProxy.getCpuPercList()[coreID];
        }

        double val;
        switch (type) {

            case COMBINED:
                val = cpu.getCombined();
                break;
            case IDLE:
                val = cpu.getIdle();
                break;
            case IRQ:
                val = cpu.getIrq();
                break;
            case NICE:
                val = cpu.getNice();
                break;
            case SOFTIRQ:
                val = cpu.getSoftIrq();
                break;
            case STOLEN:
                val = cpu.getStolen();
                break;
            case SYSTEM:
                val = cpu.getSys();
                break;
            case USER:
                val = cpu.getUser();
                break;
            case IOWAIT:
                val = cpu.getWait();
                break;
            default:
                throw new SigarException("Unknown proc total type " + type);
        }

        if (!Double.isNaN(val)) {
            res.append(Double.toString(100 * val));
        } else {
            log.warn("Failed to get total cpu metric: " + types[type]);
        }
    }
}
