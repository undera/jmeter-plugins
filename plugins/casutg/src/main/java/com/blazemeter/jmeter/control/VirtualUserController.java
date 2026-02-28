package com.blazemeter.jmeter.control;

import com.blazemeter.jmeter.threads.AbstractDynamicThreadGroup;
import com.blazemeter.jmeter.threads.DynamicThread;
import com.blazemeter.jmeter.threads.arrivals.ArrivalsThreadGroup;
import com.blazemeter.jmeter.threads.concurrency.ConcurrencyThreadGroup;
import org.apache.jmeter.control.GenericController;
import org.apache.jmeter.control.IteratingController;
import org.apache.jmeter.control.NextIsNullException;
import org.apache.jmeter.engine.event.LoopIterationEvent;
import org.apache.jmeter.samplers.Sampler;
import org.apache.jmeter.threads.JMeterContextService;
import org.apache.jmeter.threads.JMeterThread;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

public class VirtualUserController extends GenericController implements IteratingController {
    private static final Logger log = LoggerFactory.getLogger(VirtualUserController.class);
    private boolean hasArrived = false;
    protected AbstractDynamicThreadGroup owner;
    private boolean breakLoop;
    private long iterationNo = 0;

    @Override
    public Sampler next() {
        updateIterationIndex();
        try {
            if (breakLoop || owner.isLimitReached()) {
                setDone(true);
            } else if (!hasArrived) {
                if (owner.isLimitReached()) {
                    throw new IllegalStateException("Should not have more iterations");
                }
                hasArrived = true;
                incrementLoopCount();
                updateIterationIndex();
                if (owner instanceof ArrivalsThreadGroup) {
                    getOwnerAsArrivals().arrivalFact(JMeterContextService.getContext().getThread(), iterationNo);
                    if (!owner.isRunning()) {
                        setDone(true);
                        return null;
                    }
                }
            }
            return super.next();
        } finally {
            updateIterationIndex();
        }
    }

    private void updateIterationIndex() {
        // This controller increment prior sample and ThreadGroup controllers do the opposite
        // We subtract one to JMeter IterationIndex to be aligned with TG (start with 0)
        updateIterationIndex(owner.getName(), getIterCount() - 1);
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
    protected void setDone(boolean done) {
        resetBreakLoop();
        super.setDone(done);
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

    protected void incrementLoopCount() {
        iterationNo++;
    }

    protected void resetLoopCount() {
        iterationNo = 0;
    }

    public void setOwner(AbstractDynamicThreadGroup owner) {
        this.owner = owner;
    }

    @Override
    protected int getIterCount() {
        return (int) (iterationNo);
    }

    public void startNextLoop() {
        JMeterThread thread = JMeterContextService.getContext().getThread();
        if (owner instanceof ArrivalsThreadGroup) {
            getOwnerAsArrivals().abandonFact(thread, iterationNo);

            if (!moveToPool(thread)) {
                setDone(true);
            }
        } else {
            reInitialize();
        }
    }

    @Override
    public void breakLoop() {
        breakLoop = true;
        setFirst(true);
        resetCurrent();
        resetLoopCount();
        recoverRunningVersion();
    }

    private void resetBreakLoop() {
        if(breakLoop) {
            breakLoop = false;
        }
    }

    private ArrivalsThreadGroup getOwnerAsArrivals() {
        return (ArrivalsThreadGroup) owner;
    }

    @Override
    public void removed() {
        super.removed();
    }

    @Override
    public void iterationStart(LoopIterationEvent loopIterationEvent) {
        if (log.isDebugEnabled()) {
            log.debug("iterationStart called on {} with source {} and iteration {}", owner.getName(),
                loopIterationEvent.getSource(), loopIterationEvent.getIteration());
        }
        reInitialize();
        resetLoopCount();
    }
}
