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

    // FIXME: do we need SigarException here?
    abstract public void getValue(StringBuffer res) throws SigarException;

    public static AbstractPerfMonMetric createMetric(String metricType, String metricParamsStr, SigarProxy sigarProxy) {
        log.debug("Creating metric: " + metricType + " with params: " + metricParamsStr);
        AbstractPerfMonMetric metric;
        if (metricType.indexOf(' ') > 0) {
            metricType = metricType.substring(0, metricType.indexOf(' '));
        }

        MetricParamsSigar metricParams = MetricParamsSigar.createFromString(metricParamsStr, sigarProxy);

        try {
            if (metricType.equalsIgnoreCase("exec")) {
                metric = new ExecMetric(metricParams);
            } else if (metricType.equalsIgnoreCase("tail")) {
                metric = new TailMetric(metricParams);
            } else if (metricType.equalsIgnoreCase("cpu")) {
                metric = AbstractCPUMetric.getMetric(sigarProxy, metricParams);
            } else if (metricType.equalsIgnoreCase("memory")) {
                metric = AbstractMemMetric.getMetric(sigarProxy, metricParams);
            } else if (metricType.equalsIgnoreCase("swap")) {
                metric = new SwapMetric(sigarProxy, metricParams);
            } else if (metricType.equalsIgnoreCase("disks")) {
                metric = new DiskIOMetric(sigarProxy, metricParams);
            } else if (metricType.equalsIgnoreCase("network")) {
                metric = new NetworkIOMetric(sigarProxy, metricParams);
            } else if (metricType.equalsIgnoreCase("tcp")) {
                metric = new TCPStatMetric(sigarProxy, metricParams);
            } else {
                throw new SigarException("No SIGAR object for metric type " + metricType);
            }
        } catch (SigarException ex) {
            log.error("Invalid metric specified: " + metricType, ex);
            metric = new InvalidPerfMonMetric();
        } catch (IllegalArgumentException ex) {
            log.error(ex.toString());
            log.error("Invalid parameters specified for metric " + metricType + ": " + metricParams);
            metric = new InvalidPerfMonMetric();
        }

        log.debug("Have metric object: " + metric.toString());
        return metric;
    }
}
