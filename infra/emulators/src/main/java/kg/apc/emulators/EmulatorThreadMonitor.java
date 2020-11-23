package kg.apc.emulators;

import org.apache.jmeter.threads.JMeterThread;
import org.apache.jmeter.threads.JMeterThreadMonitor;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

public class EmulatorThreadMonitor implements JMeterThreadMonitor {
   private static final Logger log = LoggerFactory.getLogger(EmulatorThreadMonitor.class);

   @Override
   public void threadFinished(JMeterThread jmt) {
      log.debug("ThreadMonitor emulator notify thread finished");
   }

}
