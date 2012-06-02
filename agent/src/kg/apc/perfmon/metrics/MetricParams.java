package kg.apc.perfmon.metrics;

import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/**
 *
 * @author undera
 */
public class MetricParams {

    private static final Logger log = LoggingManager.getLoggerForClass();

    String label = "";
    long PID = -1;
    int coreID = -1;
    String fs = "";
    String iface = "";
    String[] params = new String[0];
    String type = "";
    String unit = "";
    
    protected static void parseParams(String metricParams, MetricParams inst) throws NumberFormatException {
       //for split, avoid ':' preceeded with '\', ie do not process "\:"
       String[] tokens = metricParams.split("(?<!\\\\)" + AbstractPerfMonMetric.PARAMS_DELIMITER);

       List params = new LinkedList();

       for(int i=0; i<tokens.length; i++) {
          inst.populateParams(tokens[i].replaceAll("\\\\:", ":"), params);
       }

       inst.params = (String[]) params.toArray(new String[0]);
    }
    
    protected MetricParams() {
    }

    protected void populateParams(String token, List params) throws NumberFormatException {
        if (token.startsWith("pid=")) {
            this.PID = getPIDByPID(token);
        } else if (token.startsWith("iface=")) {
            this.iface = getParamValue(token);
        } else if (token.startsWith("label=")) {
            this.label = getParamValue(token);
        } else if (token.startsWith("fs=")) {
            this.fs = getParamValue(token);
        } else if (token.startsWith("core=")) {
            this.coreID = Integer.parseInt(getParamValue(token));
        } else if (token.startsWith("unit=")) {
            this.unit = getParamValue(token);
        } else {
            params.add(token);
            this.type = token;
        }
    }

    protected static StringTokenizer tokenizeString(String metricParams) {
        return new StringTokenizer(metricParams, AbstractPerfMonMetric.PARAMS_DELIMITER);
    }

    public static MetricParams createFromString(String metricParams) {
        MetricParams inst = new MetricParams();
        parseParams(metricParams, inst);
        return inst;
    }

    public static String join(StringBuffer buff, final Object[] array, final String delim) {
        if (buff == null) {
            buff = new StringBuffer();
        }
        boolean haveDelim = delim != null;
        for (int i = 0; i < array.length; i++) {
            buff.append(array[i]);
            // if this is the last element then don't append delim
            if (haveDelim && (i + 1) < array.length) {
                buff.append(delim);
            }
        }
        return buff.toString();
    }

    public String getLabel() {
        return label;
    }
    
    public String getUnit() {
        return unit;
    }

    public static String getParamValue(String token) {
        return token.substring(token.indexOf("=") + 1);
    }

    private static long getPIDByPID(String token) {
        long PID;
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
}
