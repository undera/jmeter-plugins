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
    private String command;

    public ExecMetric() {
        super(null);
    }

    public void setParams(String string) {
        command = string;
        super.setParams(string);
    }

    public void getValue(StringBuilder res) throws SigarException {
        try {
            log.debug("Executing command: "+command);
            Process p = Runtime.getRuntime().exec(command);

            BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));

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
