package kg.apc.emulators;

import org.apache.jmeter.engine.StandardJMeterEngine;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/**
 *
 * @author undera
 */
public class EmulatorJmeterEngine extends StandardJMeterEngine{
    private static final Logger log = LoggingManager.getLoggerForClass();

    @Override
    public void askThreadsToStop() {
        log.debug("Engine emulator asking threads to stop");
    }
}
