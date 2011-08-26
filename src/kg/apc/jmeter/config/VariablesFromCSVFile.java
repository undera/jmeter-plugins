package kg.apc.jmeter.config;

import java.io.IOException;
import org.apache.jmeter.config.CSVDataSet;
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

/**
 *
 * @author apc
 * @see CSVDataSet
 */
public class VariablesFromCSVFile
      extends ConfigTestElement
      implements TestBean,
                 LoopIterationListener,
                 NoThreadClone
{
   private static final Logger log = LoggingManager.getLoggerForClass();
   private String variablesPrefix;
   private String delimiter;
   private String filename;

   /**
    * 
    * @return
    */
   public String getFilename()
   {
      return filename;
   }

   /**
    *
    * @param filename
    */
   public void setFilename(String filename)
   {
      this.filename = filename;
   }

   /**
    *
    * @return
    */
   public String getVariablesPrefix()
   {
      return variablesPrefix;
   }

   /**
    *
    * @param variableNames
    */
   public void setVariablesPrefix(String variableNames)
   {
      this.variablesPrefix = variableNames;
   }

   /**
    *
    * @return
    */
   public String getDelimiter()
   {
      return delimiter;
   }

   /**
    *
    * @param delimiter
    */
   public void setDelimiter(String delimiter)
   {
      this.delimiter = delimiter;
   }

   public void iterationStart(LoopIterationEvent iterEvent)
   {
      int threanNo = JMeterContextService.getContext().getThreadNum();
      // once only
      if (iterEvent.getIteration() > 1)
      {
         log.debug("Variables already loaded for thread " + Integer.toString(threanNo));
         return;
      }
      
      if (getFilename().length()==0)
      {
         return;
      }

      log.debug("Started loading variables from CSV for thread " + Integer.toString(threanNo));

      JMeterVariables variables = JMeterContextService.getContext().getVariables();
      String alias = this.getClass().getName() + getFilename() + Integer.toString(threanNo);
      FileServer server = FileServer.getFileServer();
      server.reserveFile(getFilename(), "UTF-8", alias);

      String delim = getResultingDelimiter();

      try
      {
         String line = null;
         while ((line = server.readLine(alias, false)) != null)
         {
            processCSVFileLine(line, delim, variables);
         }

         server.closeFile(alias);
      }
      catch (IOException e)
      {
         log.error(e.toString());
      }

      log.debug("Finished loading variables from CSV for thread " + Integer.toString(threanNo));
   }

   private void processCSVFileLine(String line, String delim, JMeterVariables variables)
   {
      String[] lineValues = JOrphanUtils.split(line, delim, false);

       //REV SH - Issue 3: Blank lines cause Java Exception
       switch (lineValues.length) {
           case 1:
               log.warn("Less than 2 columns at line: " + line);
               variables.put(getVariablesPrefix() + lineValues[0], "");
               break;
           case 2:
               //log.info("Variable: " + getVariablesPrefix() + lineValues[0] + "=" + lineValues[1] + " was: " + variables.get(getVariablesPrefix() + lineValues[0]));
               variables.put(getVariablesPrefix() + lineValues[0], lineValues[1]);
               break;
           default:
               log.warn("Bad format for line: " + line);
               break;
       }
   }

   private String getResultingDelimiter()
   {
      String delim = delimiter;
      if (delim.equals("\\t"))
      {
         delim = "\t";
      }
      else if (delim.length() == 0)
      {
         log.warn("Empty delimiter converted to ','");
         delim = ",";
      }

      //log.debug("Delimiter: " + delim);
      return delim;
   }
}
