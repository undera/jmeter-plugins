package kg.apc.jmeter.control;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Map.Entry;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.control.GenericController;
import org.apache.jmeter.engine.event.LoopIterationEvent;
import org.apache.jmeter.engine.event.LoopIterationListener;
import org.apache.jmeter.engine.util.ValueReplacer;
import org.apache.jmeter.functions.InvalidVariableException;
import org.apache.jmeter.samplers.Sampler;
import org.apache.jmeter.testelement.property.JMeterProperty;
import org.apache.jmeter.testelement.property.TestElementProperty;
import org.apache.jmeter.threads.JMeterContextService;
import org.apache.jmeter.threads.JMeterVariables;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/**
 * 
 * @author apc
 */
public class ParameterizedController
     extends GenericController
     implements Serializable, LoopIterationListener
{
   private static final Logger log = LoggingManager.getLoggerForClass();
   private boolean processed = false;
   /**
    *
    */
   public ParameterizedController()
   {
   }

   private void processVariables()
   {
      log.debug("Replacing values");
      ValueReplacer replacer = new ValueReplacer();
      
      final Arguments args1 = (Arguments) this.getUserDefinedVariablesAsProperty().getObjectValue();
      Arguments args=(Arguments) args1.clone();

      try
      {
         replacer.replaceValues(args);
      }
      catch (InvalidVariableException ex)
      {
         log.error("Error replacing variables", ex);
      }

      final JMeterVariables vars = JMeterContextService.getContext().getVariables();

      Iterator<Entry<String, String>> it = args.getArgumentsAsMap().entrySet().iterator();
      Entry<String, String> var;
      while (it.hasNext())
      {
         var = it.next();
         log.debug("Setting " + var.getKey() + "=" + var.getValue());
         vars.put(var.getKey(), var.getValue());
      }
   }

   @Override
   public Sampler next()
   {
      if (!processed)
         processVariables();
      processed = true;
      return super.next();
   }

   /**
    *
    * @param vars
    */
   public void setUserDefinedVariables(Arguments vars)
   {
      setProperty(new TestElementProperty(this.getClass().getSimpleName(), vars));
   }

   /**
    *
    * @return
    */
   public JMeterProperty getUserDefinedVariablesAsProperty()
   {
      return getProperty(this.getClass().getSimpleName());
   }

   public void iterationStart(LoopIterationEvent lie)
   {
      processed = false;
   }
}
