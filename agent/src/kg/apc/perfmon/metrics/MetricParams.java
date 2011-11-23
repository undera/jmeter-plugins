package kg.apc.perfmon.metrics;

import java.io.File;
import java.util.HashMap;
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
    String fs = null;
    String iface = null;

    private MetricParams() {
    }

    public static MetricParams createFromString(String metricParams, SigarProxy sigar) {
        StringTokenizer st = new StringTokenizer(metricParams);

        long PID = -1;
        String type = "";
        String FS = "";
        String ifc = "";
        while (st.hasMoreTokens()) {
            String token = st.nextToken(":");
            if (token.startsWith("name=")) {
                PID = getPIDByName(token, sigar);
            } else if (token.startsWith("pid=")) {
                PID = getPIDByPID(token);
            } else if (token.startsWith("iface=")) {
                ifc = getParam(token);
            } else if (token.startsWith("fs=")) {
                FS = getParam(token);
            } else if (token.startsWith("ptql=")) {
                PID = getPIDByPTQL(token, sigar);
            } else {
                type = token;
            }
        }

        MetricParams inst = new MetricParams();
        inst.PID = PID;
        inst.type = type.toLowerCase();
        inst.fs = FS;
        inst.iface = ifc;
        return inst;
    }

    private static long getPIDByName(String token, SigarProxy sigar) {
        long PID = -1;
        String name = token.substring(token.indexOf("=") + 1);
        String[] parts = name.split("#");
        try {
            long index = parts.length > 1 ? Long.parseLong(parts[1]) : 0;
            PID = getPIDByProcName(sigar, parts[0], index);
        } catch (ArrayIndexOutOfBoundsException e) {
            log.warn("Error processing token: " + token, e);
            PID = -1;
        } catch (NumberFormatException e) {
            log.warn("Error processing token: " + token, e);
            PID = -1;
        }

        return PID;
    }

    private static String getParam(String token) {
        return token.substring(token.indexOf("=") + 1);
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

    private static long getPIDByPTQL(String token, SigarProxy sigar) {
        String query = token.substring(token.indexOf("=") + 1);
        try {
            return sigar.getProcState(query).getPpid();
        } catch (SigarException ex) {
            log.warn("Error querying PTQL: " + query, ex);
            return -1;
        }
    }

    private static long getPIDByProcName(SigarProxy sigar, String name, long index) {
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

    static void logAvailableProcesses(SigarProxy sigar) {
        log.info("*** Logging available processes ***");

        long[] list = new long[0];
        try {
            list = sigar.getProcList();
        } catch (SigarException ex) {
            log.error("Failed to get process list", ex);
        }

        HashMap nameMap = new HashMap();
        ProcExe proc;
        for (int n = 0; n < list.length; n++) {
            try {
                proc = sigar.getProcExe(list[n]);
            } catch (SigarException e) {
                log.debug("Can't get process exe for pid " + list[n], e);
                continue;
            }

            StringBuilder str = new StringBuilder("Process: ");
            str.append("pid=").append(list[n]).append(' ');
            String pname = proc.getName().substring(proc.getName().lastIndexOf(File.separator) + 1).toLowerCase();
            str.append("name=").append(pname);
            if (nameMap.containsKey(pname)) {
                Long val = (Long) nameMap.get(pname);
                val = new Long(val.longValue() + 1);
                nameMap.put(pname, val);
                str.append('#').append(val);
            } else {
                nameMap.put(pname, new Long(0));
            }

            try {
                String[] args = sigar.getProcArgs(list[n]);
                str.append(" args=");
                join(str, args, " ");
            } catch (SigarException ex) {
                log.debug("Can't get process args for pid " + list[n], ex);
            }

            log.info(str.toString());
        }
    }

    private static String join(final StringBuilder buff, final Object array[],
            final String delim) {
        boolean haveDelim = (delim != null);

        for (int i = 0; i < array.length; i++) {
            buff.append(array[i]);

            // if this is the last element then don't append delim
            if (haveDelim && (i + 1) < array.length) {
                buff.append(delim);
            }
        }

        return buff.toString();
    }
}
