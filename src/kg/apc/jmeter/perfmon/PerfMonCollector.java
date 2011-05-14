package kg.apc.jmeter.perfmon;

import org.apache.jmeter.reporters.ResultCollector;
import org.apache.jmeter.samplers.SampleEvent;
import org.apache.jmeter.testelement.property.CollectionProperty;
import org.apache.jmeter.testelement.property.JMeterProperty;
import org.apache.jmeter.threads.JMeterContext;
import org.apache.jmeter.threads.JMeterContextService;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/**
 *
 * @author APC
 */
public class PerfMonCollector
      extends ResultCollector
      implements Runnable {
   private static final Logger log = LoggingManager.getLoggerForClass();
   public static final String DATA_PROPERTY = "metricConnections";
   private Thread thread;
   private JMeterContext context;

   public void setData(CollectionProperty rows) {
      setProperty(rows);
   }

   public JMeterProperty getData() {
      return getProperty(DATA_PROPERTY);
   }

   @Override
   public void sampleOccurred(SampleEvent event) {
      // just drop regular test samples
   }

   public synchronized void run() {
      while(true)
      {
         PerfMonSampleResult res = new PerfMonSampleResult();

         String name = context.getThreadGroup().getName();
         SampleEvent e=new SampleEvent(res, name, context.getVariables());
         super.sampleOccurred(e);
         try {
            this.wait(1000);
         }
         catch (InterruptedException ex) {
            log.warn("Monitoring thread was interrupted");
         }
      }
   }

   @Override
   public void testStarted(String host) {
      thread=new Thread(this);
      thread.start();
      context=JMeterContextService.getContext(); // get it to use in created thread

      super.testStarted(host);
   }

   @Override
   public void testEnded(String host) {
      thread.interrupt();

      super.testEnded(host);
   }
}
