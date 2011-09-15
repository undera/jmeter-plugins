package kg.apc.jmeter.threads;

import org.apache.jmeter.engine.event.LoopIterationEvent;
import org.apache.jmeter.testelement.TestListener;
import org.apache.log.Logger;

import org.apache.jmeter.threads.AbstractThreadGroup;
import org.apache.jmeter.threads.JMeterThread;
import org.apache.jorphan.logging.LoggingManager;

/**
 *
 * @author apc
 */
public class SteppingThreadGroup
        extends AbstractThreadGroup implements TestListener {

    private static final Logger log = LoggingManager.getLoggerForClass();
    /**
     *
     */
    private static final String THREAD_GROUP_DELAY = "Threads initial delay";
    /**
     *
     */
    private static final String INC_USER_PERIOD = "Start users period";
    /**
     *
     */
    private static final String INC_USER_COUNT = "Start users count";
    /**
     *
     */
    private static final String DEC_USER_PERIOD = "Stop users period";
    /**
     *
     */
    private static final String DEC_USER_COUNT = "Stop users count";
    /**
     *
     */
    private static final String FLIGHT_TIME = "flighttime";
    private static final String RAMPUP = "rampUp";
    private long testStartTime;

    /**
     *
     */
    public SteppingThreadGroup() {
        super();
    }

    /**
     *
     * @param thread
     */
    @Override
    public void scheduleThread(JMeterThread thread) {
        int inUserCount = getInUserCountAsInt();
        int outUserCount = getOutUserCountAsInt();

        if(inUserCount == 0) inUserCount = getNumThreads();
        if(outUserCount == 0) outUserCount = getNumThreads();

        int threadGroupDelay = 1000 * getThreadGroupDelayAsInt();
        long ascentPoint = testStartTime + threadGroupDelay;
        int inUserPeriod = 1000 * getInUserPeriodAsInt();
        int additionalRampUp = 1000 * getRampUpAsInt() / inUserCount;
        int flightTime = 1000 * getFlightTimeAsInt();
        int outUserPeriod = 1000 * getOutUserPeriodAsInt();
        
        long rampUpDuration = 1000 * getRampUpAsInt();
        long iterationDuration = inUserPeriod + rampUpDuration;
        //number of complete iteration, ie full (in user time + rampup duration) used
        int iterationCountTotal = (int) Math.ceil((double) getNumThreads() / inUserCount) - 1;
        int iterationCountBeforeMe = (int) Math.floor((double) thread.getThreadNum() / inUserCount);

        int lastIterationUserCount = getNumThreads() % inUserCount;
        if(lastIterationUserCount == 0) lastIterationUserCount = inUserCount;
        long descentPoint = ascentPoint + iterationCountTotal * iterationDuration + additionalRampUp * lastIterationUserCount + flightTime;

        long startTime = ascentPoint + iterationCountBeforeMe * iterationDuration + (thread.getThreadNum() % inUserCount) * additionalRampUp;
        long endTime = descentPoint + outUserPeriod * (int) Math.floor((double) thread.getThreadNum() / outUserCount);

        log.debug(String.format("threadNum=%d, rampUpDuration=%d, iterationDuration=%d, iterationCountTotal=%d, iterationCountBeforeMe=%d, ascentPoint=%d, descentPoint=%d, startTime=%d, endTime=%d",
                thread.getThreadNum(), rampUpDuration, iterationDuration, iterationCountTotal, iterationCountBeforeMe, ascentPoint, descentPoint, startTime, endTime));

        thread.setStartTime(startTime);
        thread.setEndTime(endTime);
        thread.setScheduled(true);
    }

    /**
     *
     * @return
     */
    public String getThreadGroupDelay() {
        return getPropertyAsString(THREAD_GROUP_DELAY);
    }

    public void setThreadGroupDelay(String delay) {
        setProperty(THREAD_GROUP_DELAY, delay);
    }

    /**
     *
     * @return
     */
    public String getInUserPeriod() {
        return getPropertyAsString(INC_USER_PERIOD);
    }

    public void setInUserPeriod(String value) {
        setProperty(INC_USER_PERIOD, value);
    }

    /**
     *
     * @return
     */
    public String getInUserCount() {
        return getPropertyAsString(INC_USER_COUNT);
    }

    public void setInUserCount(String delay) {
        setProperty(INC_USER_COUNT, delay);
    }

    /**
     *
     * @return
     */
    public String getFlightTime() {
        return getPropertyAsString(FLIGHT_TIME);
    }

    public void setFlightTime(String delay) {
        setProperty(FLIGHT_TIME, delay);
    }

    /**
     *
     * @return
     */
    public String getOutUserPeriod() {
        return getPropertyAsString(DEC_USER_PERIOD);
    }

    public void setOutUserPeriod(String delay) {
        setProperty(DEC_USER_PERIOD, delay);
    }

    /**
     *
     * @return
     */
    public String getOutUserCount() {
        return getPropertyAsString(DEC_USER_COUNT);
    }

    public void setOutUserCount(String delay) {
        setProperty(DEC_USER_COUNT, delay);
    }

    public String getRampUp() {
        return getPropertyAsString(RAMPUP);
    }

    public void setRampUp(String delay) {
        setProperty(RAMPUP, delay);
    }

    public int getThreadGroupDelayAsInt() {
        return getPropertyAsInt(THREAD_GROUP_DELAY);
    }

    public int getInUserPeriodAsInt() {
        return getPropertyAsInt(INC_USER_PERIOD);
    }

    public int getInUserCountAsInt() {
        return getPropertyAsInt(INC_USER_COUNT);
    }

    public int getRampUpAsInt() {
        return getPropertyAsInt(RAMPUP);
    }

    public int getFlightTimeAsInt() {
        return getPropertyAsInt(FLIGHT_TIME);
    }

    public int getOutUserPeriodAsInt() {
        return getPropertyAsInt(DEC_USER_PERIOD);
    }

    public int getOutUserCountAsInt() {
        return getPropertyAsInt(DEC_USER_COUNT);
    }

    public void setNumThreads(String execute) {
        setProperty(NUM_THREADS, execute);
    }

    public String getNumThreadsAsString() {
        return getPropertyAsString(NUM_THREADS);
    }

    @Override
    public void testStarted() {
        testStartTime = System.currentTimeMillis();
    }

    @Override
    public void testStarted(String string) {
        testStarted();
    }

    @Override
    public void testEnded() {
    }

    @Override
    public void testEnded(String string) {
        testEnded();
    }

    @Override
    public void testIterationStart(LoopIterationEvent lie) {
    }
}
