package kg.apc.perfmon.metrics;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
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

    public ExecMetric(String params) {
        super(null);
        setParams(params);
    }

    private void setParams(String string) {
        log.debug("Got command line: " + string);
        command = string.split(":");
    }

    public void getValue(StringBuilder res) throws SigarException {

        try {
            ProcessBuilder pb = new ProcessBuilder(Arrays.asList(command));

            Process p = pb.start();

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
            log.error("Problems executing: " + command, e);
        }
    }
}
