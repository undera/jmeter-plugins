// TODO: add column numbers selection
// TODO: use all CSV parsing options
package kg.apc.jmeter.config;

import java.io.IOException;
import org.apache.jmeter.config.ConfigTestElement;
import org.apache.jmeter.engine.event.LoopIterationEvent;
import org.apache.jmeter.engine.event.LoopIterationListener;
import org.apache.jmeter.engine.util.NoThreadClone;
import org.apache.jmeter.services.FileServer;
import org.apache.jmeter.testbeans.TestBean;
import org.apache.jmeter.threads.JMeterContextService;
import org.apache.jmeter.threads.JMeterVariables;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.jorphan.util.JOrphanUtils;
import org.apache.log.Logger;

public class VariablesFromCSVFile
     extends ConfigTestElement
     implements TestBean, LoopIterationListener, NoThreadClone
{
   private static final Logger log = LoggingManager.getLoggerForClass();
   private String variablesPrefix;
   private String delimiter;
   private String filename;

   public String getFilename()
   {
      return filename;
   }

   public void setFilename(String filename)
   {
      this.filename = filename;
   }

   /**
    * @return Returns the variableNames.
    */
   public String getVariablesPrefix()
   {
      return variablesPrefix;
   }

   /**
    * @param variableNames
    *            The variableNames to set.
    */
   public void setVariablesPrefix(String variableNames)
   {
      this.variablesPrefix = variableNames;
   }

   public String getDelimiter()
   {
      return delimiter;
   }

   public void setDelimiter(String delimiter)
   {
      this.delimiter = delimiter;
   }

   public void iterationStart(LoopIterationEvent iterEvent)
   {
      // once only
      if (iterEvent.getIteration() > 1)
         return;

      //log.debug("Vars from CSV started");

      JMeterVariables variables = JMeterContextService.getContext().getVariables();
      String _fileName = getFilename();
      FileServer server = FileServer.getFileServer();
      server.reserveFile(_fileName);
      String delim = getResultingDelimiter();

      try
      {
         String line = null;
         while ((line = server.readLine(_fileName, false)) != null)
         {
            processCSVFileLine(line, delim, variables);
         }

         server.closeFile(_fileName);
      }
      catch (IOException e)
      {
         log.error(e.toString());
      }

      //log.info("Vars from CSV finished");
   }

   private void processCSVFileLine(String line, String delim, JMeterVariables variables)
   {
      String[] lineValues = JOrphanUtils.split(line, delim, false);
      if (lineValues.length < 2)
      {
         log.warn("Less than 2 columns at line: " + line);
         variables.put(getVariablesPrefix() + lineValues[0], "");
      }
      else
      {
         //log.info("Variable: " + getVariablesPrefix() + lineValues[0] + "=" + lineValues[1] + " was: " + variables.get(getVariablesPrefix() + lineValues[0]));
         variables.put(getVariablesPrefix() + lineValues[0], lineValues[1]);
      }
   }

   private String getResultingDelimiter()
   {
      String delim = delimiter;
      if (delim.equals("\\t"))
         delim = "\t";
      else
         if (delim.length() == 0)
         {
            log.warn("Empty delimiter converted to ','");
            delim = ",";
         }

      //log.debug("Delimiter: " + delim);
      return delim;
   }
}
