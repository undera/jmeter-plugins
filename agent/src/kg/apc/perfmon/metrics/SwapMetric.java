package kg.apc.perfmon.metrics;

import java.util.Arrays;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.SigarProxy;
import org.hyperic.sigar.Swap;

/**
 *
 * @author undera
 */
class SwapMetric extends AbstractPerfMonMetric {

    public static final byte PAGE_IN = 0;
    public static final byte PAGE_OUT = 1;
    public static final byte FREE = 2;
    public static final byte TOTAL = 3;
    public static final byte USED = 4;
    public static final String[] types = {"pagein", "pageout", "free", "total", "used"};
    private int type = -1;

    public SwapMetric(SigarProxy aSigar, MetricParamsSigar params) {
        super(aSigar);
        if (params.type.length() == 0) {
            type = USED;
        } else {
            type = Arrays.asList(types).indexOf(params.type);
            if (type < 0) {
                throw new IllegalArgumentException("Unknown swap type: " + params.type);
            }
        }
    }

    public void getValue(StringBuffer res) throws SigarException {
        Swap mem = sigarProxy.getSwap();
        double val;
        switch (type) {
            case PAGE_IN:
                val = mem.getPageIn();
                break;
            case PAGE_OUT:
                val = mem.getPageOut();
                break;
            case FREE:
                val = mem.getFree();
                break;
            case TOTAL:
                val = mem.getTotal();
                break;
            case USED:
                val = mem.getUsed();
                break;
            default:
                throw new SigarException("Unknown swap type " + type);
        }
        res.append(Double.toString(val));
    }
}
