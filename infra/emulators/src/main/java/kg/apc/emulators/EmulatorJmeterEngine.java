package kg.apc.emulators;

import org.apache.jmeter.engine.StandardJMeterEngine;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

public class EmulatorJmeterEngine extends StandardJMeterEngine{
    private static final Logger log = LoggerFactory.getLogger(EmulatorJmeterEngine.class);

    @Override
    public void askThreadsToStop() {
        log.debug("Engine emulator asking threads to stop");
    }
}
