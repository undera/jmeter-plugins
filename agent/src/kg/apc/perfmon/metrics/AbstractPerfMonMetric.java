package kg.apc.perfmon.metrics;

import java.util.Hashtable;
import kg.apc.perfmon.PerfMonMetricGetter;
import kg.apc.perfmon.metrics.jmx.JMXConnectorHelper;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;
import org.hyperic.sigar.SigarProxy;

/**
 *
 * @author undera
 */
public abstract class AbstractPerfMonMetric {

    private static final Logger log = LoggingManager.getLoggerForClass();
    protected static final String PARAMS_DELIMITER = PerfMonMetricGetter.DVOETOCHIE;
    protected final SigarProxy sigarProxy;
    private final static Hashtable unitDividingFactors = new Hashtable();

    public AbstractPerfMonMetric(SigarProxy aSigar) {
        sigarProxy = aSigar;
        unitDividingFactors.put("b", new Integer(1));
        unitDividingFactors.put("kb", new Integer(1024));
        unitDividingFactors.put("mb", new Integer(1024*1024));
    }
    
    protected int getUnitDividingFactor(String unit) {
        if(!unitDividingFactors.containsKey(unit)) {
            return 1;
        } else {
            return ((Integer)unitDividingFactors.get(unit)).intValue();
        }
    }

    abstract public void getValue(StringBuffer res) throws Exception;

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
            } else if (metricType.equalsIgnoreCase("jmx")) {
                metric = new JMXMetric(metricParams, new JMXConnectorHelper());
            } else {
                throw new RuntimeException("No collector object for metric type " + metricType);
            }
        } catch (IllegalArgumentException ex) {
            log.error(ex.toString());
            log.error("Invalid parameters specified for metric " + metricType + ": " + metricParams);
            metric = new InvalidPerfMonMetric();
        } catch (RuntimeException ex) {
            log.error("Invalid metric specified: " + metricType, ex);
            metric = new InvalidPerfMonMetric();
        }

        log.debug("Have metric object: " + metric.toString());
        return metric;
    }
}
