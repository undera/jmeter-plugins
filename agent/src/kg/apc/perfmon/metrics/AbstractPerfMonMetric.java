package kg.apc.perfmon.metrics;

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
    protected final SigarProxy sigarProxy;

    public AbstractPerfMonMetric(SigarProxy aSigar) {
        sigarProxy = aSigar;
    }

    protected long getPIDByProcName(String name, int index) {
        return 0;
    }

    public void setParams(String params) {
    }

    // FIXME: do we need SigarException here?
    abstract public void getValue(StringBuilder res) throws SigarException;

    public static AbstractPerfMonMetric createMetric(String metricType, String metricParams, SigarProxy sigarProxy) {
        log.debug("Creating metric: " + metricType + " with params: " + metricParams);
        AbstractPerfMonMetric metric;
        try {
            if (metricType.equals("exec")) {
                metric = new ExecMetric();
            } else if (metricType.equals("tail")) {
                metric = new TailMetric();
            } else if (metricType.equals("cpu")) {
                metric = AbstractCPUMetric.getMetric(sigarProxy, metricParams);
            } else {
                throw new SigarException("No SIGAR object for metric type " + metricType);
            }
        } catch (SigarException ex) {
            log.error("Invalid metric specified: " + metricType, ex);
            metric = new InvalidPerfMonMetric();
        }

        metric.setParams(metricParams);
        return metric;
    }
}
