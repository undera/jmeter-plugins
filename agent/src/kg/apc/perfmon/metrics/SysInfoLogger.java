package kg.apc.perfmon.metrics;

import org.hyperic.sigar.SigarProxy;

/**
 *
 * @author undera
 */
public class SysInfoLogger {

    public static void doIt(SigarProxy sigar) {
        DiskIOMetric.logAllAvailableFilesystems(sigar);
    }
}
