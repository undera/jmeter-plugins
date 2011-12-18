package kg.apc.perfmon.metrics;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
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
    String type = "";
    String fs = "";
    String iface = "";
    String[] params = new String[0];
    int coreID = -1;

    private MetricParams() {
    }

    static MetricParams createFromString(String string) {
        return createFromString(string, null);
    }

    // TODO: pretty ugly, refactor it
    public static MetricParams createFromString(String metricParams, SigarProxy sigar) {
        MetricParams inst = new MetricParams();
        StringTokenizer tokens = new StringTokenizer(metricParams, AbstractPerfMonMetric.PARAMS_DELIMITER);

        List params = new LinkedList();
        while (true) {
            String token;
            try {
                token = tokens.nextToken();

	            String buff = "";
	            String tmp = token;
	            
	            while (token.endsWith("\\")) {
	            	tmp = token.substring(0, token.length() - 1) + AbstractPerfMonMetric.PARAMS_DELIMITER;
	                token = tokens.nextToken();
	                buff += tmp;
	            }

	            token = buff + token;
            } catch (NoSuchElementException e) {
                break;
            }

            if (token.startsWith("name=")) {
                inst.PID = getPIDByName(token, sigar);
            } else if (token.startsWith("pid=")) {
                inst.PID = getPIDByPID(token);
            } else if (token.startsWith("iface=")) {
                inst.iface = getParam(token);
            } else if (token.startsWith("fs=")) {
                inst.fs = getParam(token);
            } else if (token.startsWith("ptql=")) {
                inst.PID = getPIDByPTQL(token, sigar);
            } else if (token.startsWith("core=")) {
                inst.coreID = Integer.parseInt(getParam(token));
            } else {
                params.add(token);
                inst.type = token;
            }
        }

        inst.params = (String[]) params.toArray(new String[0]);
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

            StringBuffer str = new StringBuffer("Process: ");
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

    public static String join(StringBuffer buff, final Object array[],
            final String delim) {
        if (buff == null) {
            buff = new StringBuffer();
        }

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
