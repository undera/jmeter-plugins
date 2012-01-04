package kg.apc.perfmon.metrics;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;
import org.hyperic.sigar.SigarException;

/**
 *
 * @author undera
 */
class ExecMetric extends AbstractPerfMonMetric {

    private static final Logger log = LoggingManager.getLoggerForClass();
    private String[] command;

    public ExecMetric(MetricParams params) {
        super(null);
        if (params.params.length == 0) {
            throw new IllegalArgumentException("Params cannot be null");
        }

        command = params.params;
    }

    public void getValue(StringBuffer res) throws SigarException {
        log.debug("Executing custom script: " + MetricParamsSigar.join(null, command, " "));

        try {
            Process p = Runtime.getRuntime().exec(command);

            BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));

            BufferedReader stdErr = new BufferedReader(new InputStreamReader(p.getErrorStream()));
            String e;
            while ((e = stdErr.readLine()) != null) {
                log.error("Error: " + e);
            }

            // read the output from the command
            String s, lastStr = "";
            while ((s = stdInput.readLine()) != null) {
                log.debug("Read proc out line: " + s);
                lastStr = s;
            }
            res.append(lastStr);
        } catch (IOException e) {
            log.error("Problems executing: " + command[0], e);
        }
    }
}
