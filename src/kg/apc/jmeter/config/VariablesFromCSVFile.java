// todo: add column numbers selection
// todo: use all CSV parsing options

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
   private boolean isVariablesPrepared = false;
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
      if (isVariablesPrepared)
         return;
      else
         isVariablesPrepared = true;

      JMeterVariables variables = JMeterContextService.getContext().getVariables();
      String _fileName = getFilename();
      FileServer server = FileServer.getFileServer();
      server.reserveFile(_fileName);

      String delim = getResultingDelimiter();

      try
      {
         String line;
         while ((line = server.readLine(_fileName, false))!=null)
         {
            String[] lineValues = JOrphanUtils.split(line, delim, false);
            if (lineValues.length < 2)
            {
               log.warn("Less than 2 columns at line: "+line);
               break;
            }

            log.debug("Variable: "+getVariablesPrefix() + lineValues[0]+"="+lineValues[1]);
            variables.put(getVariablesPrefix() + lineValues[0], lineValues[1]);
         }
      }
      catch (IOException e)
      {// TODO - should the error be indicated in the variables?
         log.error(e.toString());
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

      log.debug("Delimiter: "+delim);
      return delim;
   }
}
