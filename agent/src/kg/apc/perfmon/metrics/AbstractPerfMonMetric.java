package kg.apc.perfmon.metrics;

import java.io.File;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;
import org.hyperic.sigar.ProcExe;
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
        int procIndex = 0;
        long[] list;
        ProcExe proc;
        try {
            list = sigarProxy.getProcList();
        } catch (SigarException ex) {
            log.error("Failed to get process list", ex);
            return -1;
        }

        for (int n = 0; n < list.length; n++) {
            try {
                proc = sigarProxy.getProcExe(list[n]);
            } catch (SigarException e) {
                log.debug("Can't get process exe for pid " + list[n], e);
                continue;
            }

            // case insensitive match
            String pname = proc.getName().toLowerCase();
            if (pname.endsWith(File.separator + name.toLowerCase())) {
                if (procIndex == index) {
                    return list[n];
                }
                procIndex++;
            }
        }
        return -1;
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
        log.debug("Have metric object: " + metric.toString());
        return metric;
    }
}
