package kg.apc.perfmon.metrics;

import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;
import org.hyperic.sigar.SigarProxy;

/**
 *
 * @author undera
 */
abstract class AbstractMemMetric extends AbstractPerfMonMetric {

    private static final Logger log = LoggingManager.getLoggerForClass();
    protected final MetricParamsSigar params;

    public static AbstractMemMetric getMetric(SigarProxy sigar, MetricParamsSigar params) {
        if (params.PID >= 0) {
            return new MemProcMetric(sigar, params);
        } else {
            return new MemTotalMetric(sigar, params);
        }
    }

    public AbstractMemMetric(SigarProxy aSigar, MetricParamsSigar params) {
        super(aSigar);
        this.params = params;
    }
}
