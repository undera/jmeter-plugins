package kg.apc.jmeter.config;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.jorphan.util.JOrphanUtils;
import org.apache.log.Logger;

/**
 *
 * @author Stephane Hoblingre
 */
public class VariableFromCsvFileReader {

    private static final Logger log = LoggingManager.getLoggerForClass();
    private BufferedReader input;

    /**
     * Initialize a new CSV reader for the named file.
     *
     * @param csvFileName name of the CSV input file
     */
    public VariableFromCsvFileReader(String csvFileName) {
        try {
            input = new BufferedReader(new FileReader(csvFileName));
        } catch (FileNotFoundException ex) {
            log.error("File not found: " + ex.getMessage());
        }
    }

    /**
     * Initialize a new CSV reader with a BufferedReader as input.
     *
     * @param input the CSV input
     */
    public VariableFromCsvFileReader(BufferedReader input) {
        this.input = input;
    }

    public Map<String, String> getDataAsMap(String prefix, String separator) {
        if (separator.isEmpty()) {
            throw new IllegalArgumentException("CSV separator cannot be empty");
        }

        HashMap ret = new HashMap<String, String>();
        if (input != null) {
            try {
                String line;
                while ((line = input.readLine()) != null) {
                    String[] lineValues = JOrphanUtils.split(line, separator, false);

                    switch (lineValues.length) {
                        case 1:
                            log.warn("Less than 2 columns at line: " + line);
                            ret.put(prefix + lineValues[0], "");
                            break;
                        case 2:
                            ret.put(prefix + lineValues[0], lineValues[1]);
                            break;
                        default:
                            log.warn("Bad format for line: " + line);
                            break;
                    }
                }
            } catch (IOException ex) {
                log.error("Error while reading: " + ex.getMessage());
            }
        }
        return ret;
    }
}
