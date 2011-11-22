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
    protected final MetricParams params;

    public static AbstractMemMetric getMetric(SigarProxy sigar, String metricParams) {
        MetricParams params = MetricParams.createFromString(metricParams, sigar);

        if (params.PID >= 0) {
            return new MemProcMetric(sigar, params);
        } else {
            return new MemTotalMetric(sigar, params);
        }
    }

    public AbstractMemMetric(SigarProxy aSigar, MetricParams params) {
        super(aSigar);
        this.params = params;
    }

    public void setParams(String params) {
        // just drop it
    }
}
