package kg.apc.emulators;

import org.apache.jmeter.threads.JMeterThread;
import org.apache.jmeter.threads.JMeterThreadMonitor;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

public class EmulatorThreadMonitor implements JMeterThreadMonitor {
   private static final Logger log = LoggingManager.getLoggerForClass();

   @Override
   public void threadFinished(JMeterThread jmt) {
      log.debug("ThreadMonitor emulator notify thread finished");
   }

}
