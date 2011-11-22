package kg.apc.perfmon.metrics;

import kg.apc.perfmon.PerfMonMetricGetter;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.SigarProxy;

/**
 *
 * @author undera
 */
public abstract class AbstractPerfMonMetric {

    private static final Logger log = LoggingManager.getLoggerForClass();
    protected static final String PARAMS_DELIMITER = PerfMonMetricGetter.DVOETOCHIE;
    protected final SigarProxy sigarProxy;

    public AbstractPerfMonMetric(SigarProxy aSigar) {
        sigarProxy = aSigar;
    }

    public void setParams(String params) {
        if (!params.isEmpty()) {
            log.warn("Nothing to do with params: " + params);
        }
    }

    // FIXME: do we need SigarException here?
    abstract public void getValue(StringBuilder res) throws SigarException;

    public static AbstractPerfMonMetric createMetric(String metricType, String metricParams, SigarProxy sigarProxy) {
        log.debug("Creating metric: " + metricType + " with params: " + metricParams);
        AbstractPerfMonMetric metric;
        try {
            if (metricType.equalsIgnoreCase("exec")) {
                metric = new ExecMetric();
            } else if (metricType.equalsIgnoreCase("tail")) {
                metric = new TailMetric();
            } else if (metricType.equalsIgnoreCase("cpu")) {
                metric = AbstractCPUMetric.getMetric(sigarProxy, metricParams);
            } else if (metricType.equalsIgnoreCase("memory")) {
                metric = AbstractMemMetric.getMetric(sigarProxy, metricParams);
            } else {
                throw new SigarException("No SIGAR object for metric type " + metricType);
            }
        } catch (SigarException ex) {
            log.error("Invalid metric specified: " + metricType, ex);
            metric = new InvalidPerfMonMetric();
        }

        metric.setParams(metricParams);
        log.debug("Have metric object: " + metric.toString());
        return metric;
    }
}
