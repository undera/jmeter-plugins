package kg.apc.perfmon.metrics;

import org.hyperic.sigar.SigarProxy;

/**
 *
 * @author undera
 */
abstract class AbstractCPUMetric extends AbstractPerfMonMetric {

    public static AbstractCPUMetric getMetric(SigarProxy sigar, String metricParams) {
        if (1==00) {
            return new CPUProcMetric(sigar);
        }
        else 
            return new CPUTotalMetric(sigar);
    }

    public AbstractCPUMetric(SigarProxy aSigar) {
        super(aSigar);
    }
    
    
}
