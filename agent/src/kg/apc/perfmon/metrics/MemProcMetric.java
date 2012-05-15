package kg.apc.perfmon.metrics;

import java.util.Arrays;
import org.hyperic.sigar.ProcMem;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.SigarProxy;

/**
 *
 * @author undera
 */
class MemProcMetric extends AbstractMemMetric {

    public static final byte VIRTUAL = 0;
    public static final byte SHARED = 1;
    public static final byte PAGE_FAULTS = 2;
    public static final byte MAJOR_FAULTS = 3;
    public static final byte MINOR_FAULTS = 4;
    public static final byte RESIDENT = 5;
    public static final String[] types = {"virtual", "shared", "pagefaults",
        "majorfaults", "minorfaults", "resident"};
    private int type = -1;
    private double prev = -1;
    private int dividingFactor = 1;

    public MemProcMetric(SigarProxy aSigar, MetricParamsSigar params) {
        super(aSigar, params);
        if (params.type.length() == 0) {
            type = RESIDENT;
        } else {
            type = Arrays.asList(types).indexOf(params.type);
            if (type < 0) {
                throw new IllegalArgumentException("Invalid proc mem type: " + params.type);
            }
        }
        dividingFactor = getUnitDividingFactor(params.getUnit());
    }

    public void getValue(StringBuffer res) throws SigarException {
        ProcMem mem = sigarProxy.getProcMem(params.PID);
        double val;
        long cur;
        int factor = 1;

        switch (type) {
            case VIRTUAL:
                val = mem.getSize();
                factor = dividingFactor;
                break;
            case SHARED:
                cur = mem.getShare();
                prev = cur;
                val = cur;
                factor = dividingFactor;
                break;
            case PAGE_FAULTS:
                cur = mem.getPageFaults();
                val = prev > 0 ? cur - prev : 0;
                prev = cur;
                break;
            case MAJOR_FAULTS:
                cur = mem.getMajorFaults();
                val = prev > 0 ? cur - prev : 0;
                prev = cur;
                break;
            case MINOR_FAULTS:
                cur = mem.getMinorFaults();
                val = prev > 0 ? cur - prev : 0;
                prev = cur;
                break;
            case RESIDENT:
                cur = mem.getResident();
                prev = cur;
                val = cur;
                factor = dividingFactor;
                break;
            default:
                throw new SigarException("Unknown proc mem type " + type);
        }
        val = val/factor;
        res.append(Double.toString(val));
    }
}
