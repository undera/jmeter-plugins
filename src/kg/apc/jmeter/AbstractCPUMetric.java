package kg.apc.jmeter;

import org.hyperic.sigar.SigarProxy;

/**
 *
 * @author undera
 */
abstract public class AbstractCPUMetric extends AbstractPerfMonMetric {

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
