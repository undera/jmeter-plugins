package kg.apc.perfmon.metrics;

import java.io.File;
import java.util.StringTokenizer;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;
import org.hyperic.sigar.ProcExe;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.SigarProxy;

/**
 * Class to parse metric params like process name, PID, metric type
 * @author undera
 */
class MetricParams {

    private static final Logger log = LoggingManager.getLoggerForClass();
    long PID = -1;
    String type = null;

    private MetricParams() {
    }

    public static MetricParams createFromString(String metricParams, String defaultType, SigarProxy sigar) {
        StringTokenizer st = new StringTokenizer(metricParams);

        long PID = -1;
        String type = defaultType;
        while (st.hasMoreTokens()) {
            String token = st.nextToken(":");
            if (token.startsWith("name=")) {
                PID = getPIDByName(token, sigar);
            } else if (token.startsWith("pid=")) {
                PID = getPIDByPID(token);
            } else {
                type = token;
            }
        }

        MetricParams inst = new MetricParams();
        inst.PID = PID;
        inst.type = type;
        return inst;
    }

    private static long getPIDByName(String token, SigarProxy sigar) {
        long PID = 0;
        String name = token.substring(token.indexOf("=") + 1);
        String[] parts = name.split("=");
        try {
            PID = getPIDByProcName(sigar, parts[0], Long.parseLong(parts[1]));
        } catch (ArrayIndexOutOfBoundsException e) {
            log.warn("Error processing token: " + token, e);
            PID = -1;
        } catch (NumberFormatException e) {
            log.warn("Error processing token: " + token, e);
            PID = -1;
        }

        return PID;
    }

    private static long getPIDByPID(String token) {
        long PID = -1;
        try {
            String PIDStr = token.substring(token.indexOf("=") + 1);
            PID = Long.parseLong(PIDStr);
        } catch (ArrayIndexOutOfBoundsException e) {
            log.warn("Error processing token: " + token, e);
            PID = -1;
        } catch (NumberFormatException e) {
            log.warn("Error processing token: " + token, e);
            PID = -1;
        }
        return PID;
    }

    public static long getPIDByProcName(SigarProxy sigar, String name, long index) {
        int procIndex = 0;
        long[] list;
        ProcExe proc;
        try {
            list = sigar.getProcList();
        } catch (SigarException ex) {
            log.error("Failed to get process list", ex);
            return -1;
        }

        for (int n = 0; n < list.length; n++) {
            try {
                proc = sigar.getProcExe(list[n]);
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
}
