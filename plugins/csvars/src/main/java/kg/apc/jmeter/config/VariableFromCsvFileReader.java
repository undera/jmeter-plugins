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

    /**
     * Parses (name, value) pairs from the input and returns the result as a Map. The name is taken from the first column and
     * value from the second column. If an input line contains only one column its value is defaulted to an empty string.
     * Any extra columns are ignored.
     *
     * @param prefix a prefix to apply to the mapped variable names
     * @param separator the field delimiter
     * @return a map of (name, value) pairs
     */
    public Map<String, String> getDataAsMap(String prefix, String separator) {
        return getDataAsMap(prefix, separator, 0);
    }

    /**
     * Parses (name, value) pairs from the input and returns the result as a Map, with the option to skip the first line.
     * The name is taken from the first column and value from the second column. If an input line contains only one
     * column its value is defaulted to an empty string. Any extra columns are ignored.
     *
     * If the input contains headers, call with skipLines equal to the number of lines of headers. A negative value for
     * skipLines yields the same result as 0.
     *
     * @param prefix a prefix to apply to the mapped variable names
     * @param separator the field delimiter
     * @param skipLines the number of lines at the beginning of the input to skip
     * @return a map of (name, value) pairs
     */
    public Map<String, String> getDataAsMap(String prefix, String separator, int skipLines) {
	if (separator.isEmpty()) {
	    throw new IllegalArgumentException("CSV separator cannot be empty");
	}

	Map<String, String> variables = new HashMap<>();
	if (input != null) {
	    try {
		String line;
		int lineNum = 0;
		StringBuilder multiLineValue = new StringBuilder();
		String multiLineVariable = null;
		while ((line = input.readLine()) != null) {
		    if (++lineNum > skipLines) {
			if (line.startsWith("#")) {
			    continue;
			}
			String[] lineValues = JOrphanUtils.split(line, separator, false);

			if (lineValues.length == 1) {
			    if (multiLineValue.length() > 0 && lineValues[0].endsWith("\"")) {
				multiLineValue.append(lineValues[0].substring(0, lineValues[0].length() - 1));
				variables.put(prefix + multiLineVariable, multiLineValue.toString());
				// reset memory
				multiLineValue.setLength(0);
				multiLineVariable = null;
			    } else if (multiLineValue.length() > 0) {
				multiLineValue.append(lineValues[0]).append('\n');
			    } else {
				log.warn("Less than 2 columns at line: " + line);
				variables.put(prefix + lineValues[0], "");
			    }
			} else if (lineValues.length >= 2) {
			    if (lineValues[1].startsWith("\"")) {
				// multi line value
				multiLineVariable = lineValues[0];
				multiLineValue.append(lineValues[1].substring(1)).append('\n');
			    } else {
				variables.put(prefix + lineValues[0], lineValues[1]);
			    }
			}
		    }
		}
	    } catch (IOException ex) {
		log.error("Error while reading: " + ex.getMessage());
	    }
	}
	return variables;
    }
}
