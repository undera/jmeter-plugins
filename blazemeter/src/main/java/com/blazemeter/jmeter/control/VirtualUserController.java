package com.blazemeter.jmeter.control;

import com.blazemeter.jmeter.threads.AbstractDynamicThreadGroup;
import com.blazemeter.jmeter.threads.concurrency.ConcurrencyThreadGroup;
import org.apache.jmeter.control.GenericController;
import org.apache.jmeter.control.NextIsNullException;
import org.apache.jmeter.samplers.Sampler;
import org.apache.jmeter.threads.JMeterContextService;
import org.apache.jmeter.threads.JMeterThread;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

public class VirtualUserController extends GenericController {
    private static final Logger log = LoggingManager.getLoggerForClass();
    private boolean hasArrived = false;
    protected AbstractDynamicThreadGroup owner;

    public VirtualUserController() {
        super();
    }

    private long iterationNo = 0;

    @Override
    public Sampler next() {
        if (!owner.isRunning()) {
            setDone(true);
        } else if (!hasArrived) {
            if (owner.isLimitReached()) {
                throw new IllegalStateException("Should not have more iterations");
            }
            hasArrived = true;
            iterationNo++;
                    }

        return super.next();
    }

    @Override
    protected void reInitialize() {
        super.reInitialize();
        hasArrived = false;
    }

    @Override
    protected Sampler nextIsNull() throws NextIsNullException {
        JMeterThread thread = JMeterContextService.getContext().getThread();

        long iLimit = owner.getIterationsLimitAsLong();

        if (owner.isLimitReached()) {
            log.info("Test limit reached, thread is done: " + thread.getThreadName());
            setDone(true);
            return null;
        } else if (iLimit > 0 && iterationNo >= iLimit) {
            log.info("Iteration limit reached, thread is done: " + thread.getThreadName());
            setDone(true);
            return null;
        } else if (owner instanceof ConcurrencyThreadGroup && ((ConcurrencyThreadGroup) owner).tooMuchConcurrency()) {
            log.info("Need to decrease concurrency, thread is done: " + thread.getThreadName());
            setDone(true);
            return null;
        } else {
            reInitialize();
            return next();
        }
    }

    public void setOwner(AbstractDynamicThreadGroup owner) {
        this.owner = owner;
    }

    public void startNextLoop() {
        JMeterThread thread = JMeterContextService.getContext().getThread();
    }

}
