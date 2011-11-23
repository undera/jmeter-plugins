package kg.apc.perfmon.metrics;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;
import org.hyperic.sigar.SigarException;

/**
 *
 * @author undera
 */
class TailMetric extends AbstractPerfMonMetric {

    private static final Logger log = LoggingManager.getLoggerForClass();
    private String filename;
    private BufferedReader reader;

    public TailMetric(String params) {
        super(null);
        if (params.isEmpty()) {
            throw new IllegalArgumentException("Cannot tail unspecified file");
        }

        setParams(params);
    }

    private void setParams(String string) {
        log.debug("Tailing file: " + string);
        filename = string;
    }

    public void getValue(StringBuilder res) throws SigarException {
        String line, lastLine = "";
        try {
            while ((line = getReader().readLine()) != null) {
                log.debug("Read line: " + line);
                lastLine = line;
            }
            res.append(lastLine);
        } catch (IOException e) {
            log.error("Cannot read lines from file: " + filename);
        }
    }

    private BufferedReader getReader() throws IOException {
        if (reader == null) {
            reader = new BufferedReader(new FileReader(new File(filename)));
        }
        return reader;
    }
}
