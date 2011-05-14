package kg.apc.jmeter.perfmon;

import org.apache.jmeter.reporters.ResultCollector;
import org.apache.jmeter.samplers.SampleEvent;
import org.apache.jmeter.testelement.property.CollectionProperty;
import org.apache.jmeter.testelement.property.JMeterProperty;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/**
 *
 * @author APC
 */
public class PerfMonCollector
      extends ResultCollector
      implements Runnable {
   private static final String PERFMON = "PerfMon";
   private static final Logger log = LoggingManager.getLoggerForClass();
   public static final String DATA_PROPERTY = "metricConnections";
   private Thread thread;

   public void setData(CollectionProperty rows) {
      setProperty(rows);
   }

   public JMeterProperty getData() {
      return getProperty(DATA_PROPERTY);
   }

   @Override
   public void sampleOccurred(SampleEvent event) {
      // just dropping regular test samples
   }

   public synchronized void run() {
      while (true) {
            PerfMonSampleResult res = new PerfMonSampleResult();
            SampleEvent e = new SampleEvent(res, PERFMON);
            super.sampleOccurred(e);
            log.info("Got sample: " + res.getSampleLabel()+" "+getVisualizer());

         try {
            this.wait(1000);
         }
         catch (InterruptedException ex) {
            log.debug("Monitoring thread was interrupted", ex);
            break;
         }
      }
   }

   @Override
   public void testStarted(String host) {
      thread = new Thread(this);
      thread.start();

      super.testStarted(host);
   }

   @Override
   public void testEnded(String host) {
      thread.interrupt();

      super.testEnded(host);
   }
}
