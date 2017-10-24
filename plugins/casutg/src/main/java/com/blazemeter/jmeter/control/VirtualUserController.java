package com.blazemeter.jmeter.control;

import com.blazemeter.jmeter.threads.AbstractDynamicThreadGroup;
import com.blazemeter.jmeter.threads.DynamicThread;
import com.blazemeter.jmeter.threads.arrivals.ArrivalsThreadGroup;
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
            if (owner instanceof ArrivalsThreadGroup) {
                getOwnerAsArrivals().arrivalFact(JMeterContextService.getContext().getThread(), iterationNo);
            }
        }

        return super.next();
    }

    private boolean moveToPool(JMeterThread thread) {
        if (thread instanceof DynamicThread) {
            if (!owner.isLimitReached() && getOwnerAsArrivals().movedToPool((DynamicThread) thread)) {
                reInitialize();
                return true;
            }
        }
        return false;
    }

    @Override
    protected void reInitialize() {
        super.reInitialize();
        hasArrived = false;
    }

    @Override
    protected Sampler nextIsNull() throws NextIsNullException {
        JMeterThread thread = JMeterContextService.getContext().getThread();
        if (owner instanceof ArrivalsThreadGroup) {
            getOwnerAsArrivals().completionFact(thread, iterationNo);
        }

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
        } else if (owner instanceof ArrivalsThreadGroup) {
            moveToPool(thread);
            return super.nextIsNull();
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
        if (owner instanceof ArrivalsThreadGroup) {
            getOwnerAsArrivals().abandonFact(thread, iterationNo);

            if (!moveToPool(thread)) {
                setDone(true);
            }
        } else {
            // setDone(true); TODO: do we need it or not  ?
        }
    }

    private ArrivalsThreadGroup getOwnerAsArrivals() {
        return (ArrivalsThreadGroup) owner;
    }
}
