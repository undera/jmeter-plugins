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
    private double prev = -1;
    private int dividingFactor = 1;

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
        dividingFactor = getUnitDividingFactor(params.getUnit());
    }

    public void getValue(StringBuffer res) throws SigarException {
        Swap mem = sigarProxy.getSwap();
        double val;
        double cur;
        int factor = 1;
        switch (type) {
            case PAGE_IN:
                cur = mem.getPageIn();
                val = prev != -1 ? cur - prev : 0;
                prev = cur;
                break;
            case PAGE_OUT:
                cur = mem.getPageOut();
                val = prev != -1 ? cur - prev : 0;
                prev = cur;
                break;
            case FREE:
                val = mem.getFree();
                factor = dividingFactor;
                break;
            case TOTAL:
                val = mem.getTotal();
                factor = dividingFactor;
                break;
            case USED:
                val = mem.getUsed();
                factor = dividingFactor;
                break;
            default:
                throw new SigarException("Unknown swap type " + type);
        }
        val = val/factor;
        res.append(Double.toString(val));
    }
}
