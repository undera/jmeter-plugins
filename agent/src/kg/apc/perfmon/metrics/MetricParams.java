package kg.apc.perfmon.metrics;

import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/**
 *
 * @author undera
 */
public class MetricParams {

    private static final Logger log = LoggingManager.getLoggerForClass();

    protected static void parseParams(String metricParams, MetricParams inst) throws NumberFormatException {
        StringTokenizer tokens = tokenizeString(metricParams);

        List params = new LinkedList();
        while (true) {
            String token;
            try {
                token = tokens.nextToken();

                String buff = "";
                String tmp;

                while (token.endsWith("\\")) {
                    tmp = token.substring(0, token.length() - 1) + AbstractPerfMonMetric.PARAMS_DELIMITER;
                    token = tokens.nextToken();
                    buff += tmp;
                }

                token = buff + token;
            } catch (NoSuchElementException e) {
                break;
            }
            inst.populateParams(token, params);
        }

        inst.params = (String[]) params.toArray(new String[0]);
    }
    String label = "";
    long PID = -1;
    int coreID = -1;
    String fs = "";
    String iface = "";
    String[] params = new String[0];
    String type = "";

    protected MetricParams() {
    }

    protected void populateParams(String token, List params) throws NumberFormatException {
        if (token.startsWith("pid=")) {
            this.PID = getPIDByPID(token);
        } else if (token.startsWith("iface=")) {
            this.iface = getParam(token);
        } else if (token.startsWith("label=")) {
            this.label = getParam(token);
        } else if (token.startsWith("fs=")) {
            this.fs = getParam(token);
        } else if (token.startsWith("core=")) {
            this.coreID = Integer.parseInt(getParam(token));
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

    protected static String getParam(String token) {
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
