package kg.apc.jmeter.config;

import java.io.BufferedReader;
import java.io.File;
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
   private File file;

   public VariableFromCsvFileReader(String csvFileName) {
      file = new File(csvFileName);
   }

   public Map<String, String> getDataAsMap(String prefix, String separator) {
      HashMap ret = new HashMap<String, String>();
      if (file.exists()) {
         try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = reader.readLine();
            while (line != null) {
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

               line = reader.readLine();
            }
         } catch (FileNotFoundException ex) {
            log.error("File not found: " + ex.getMessage());
         } catch (IOException ex) {
            log.error("Error while reading: " + ex.getMessage());
         }
      }
      return ret;
   }
}
