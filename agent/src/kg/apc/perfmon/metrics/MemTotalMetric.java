package kg.apc.perfmon.metrics;

import java.util.Arrays;
import org.hyperic.sigar.Mem;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.SigarProxy;

/**
 *
 * @author undera
 */
class MemTotalMetric extends AbstractMemMetric {
    
    public static final byte ACTUAL_FREE = 0;
    public static final byte ACTUAL_USED = 1;
    public static final byte FREE = 2;
    public static final byte FREE_PERCENT = 3;
    public static final byte RAM = 4;
    public static final byte TOTAL = 5;
    public static final byte USED = 6;
    public static final byte USED_PERCENT = 7;
    public static final String[] types = {"actualfree", "actualused", "free", "freeperc",
        "ram", "total", "used", "usedperc"};
    private int type = -1;
    
    public MemTotalMetric(SigarProxy aSigar, MetricParams params) {
        super(aSigar, params);
        if (params.type.length() == 0) {
            type = USED_PERCENT;
        } else {
            type = Arrays.asList(types).indexOf(params.type);
            if (type < 0) {
                throw new IllegalArgumentException("Invalid total mem type: " + params.type);
            }
        }
    }
    
    public void getValue(StringBuffer res) throws SigarException {
        Mem mem = sigarProxy.getMem();
        double val;
        switch (type) {
            
            case ACTUAL_FREE:
                val = mem.getActualFree();
                break;
            case ACTUAL_USED:
                val = mem.getActualUsed();
                break;
            case FREE:
                val = mem.getFree();
                break;
            case FREE_PERCENT:
                val = 100 * mem.getFreePercent();
                break;
            case RAM:
                val = mem.getRam();
                break;
            case TOTAL:
                val = mem.getTotal();
                break;
            case USED:
                val = mem.getUsed();
                break;
            case USED_PERCENT:
                val = 100 * mem.getUsedPercent();
                break;
            default:
                throw new SigarException("Unknown total mem type " + type);
        }
        res.append(Double.toString(val));
    }
}
