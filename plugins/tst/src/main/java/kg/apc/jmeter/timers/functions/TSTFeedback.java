package kg.apc.jmeter.timers.functions;

import org.apache.jmeter.engine.util.CompoundVariable;
import org.apache.jmeter.functions.AbstractFunction;
import org.apache.jmeter.functions.InvalidVariableException;
import org.apache.jmeter.gui.MainFrame;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.samplers.Sampler;
import org.apache.jmeter.testelement.TestStateListener;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class TSTFeedback extends AbstractFunction implements TestStateListener {
    private static final Logger log = LoggingManager.getLoggerForClass();

    private static final List<String> desc = new LinkedList<>();
    private static final String KEY = "__tstFeedback";

    // Number of parameters expected - used to reject invalid calls
    private static final int MIN_PARAMETER_COUNT = 2;

    static {
        desc.add("Name of Throughput Shaping Timer to integrate with");
        desc.add("Starting concurrency");
        desc.add("Max concurrency");
        desc.add("Spare threads ratio");
    }

    private CompoundVariable[] values;
    private boolean justStarted = true;

    @Override
    public synchronized String execute(SampleResult previousResult, Sampler currentSampler) {
        String tstName = values[0].execute();
        String concName = tstName + "_concurrency";

        if (justStarted) {
            JMeterUtils.setProperty(concName, String.valueOf(values[1].execute()));
            justStarted = false;
        }

        int limit = Integer.MAX_VALUE;
        if (values.length > 2) {
            try {
                limit = Integer.parseInt(values[2].execute());
            } catch (NumberFormatException exc) {
                log.debug("Failed to parse value for limit, defaulting to infinity", exc);
            }
        }

        double spare = 0.1; //TODO: parameterize it somehow?
        if (values.length > 3) { // We have a 3rd parameter
            try {
                spare = Double.parseDouble(values[3].execute());
            } catch (NumberFormatException exc) {
                log.debug("Failed to parse value for spare ratio, defaulting to 0", exc);
                spare = 1;
            }
        }

        int con = Integer.parseInt(JMeterUtils.getPropDefault(concName, "1"));
        int delayed = Integer.parseInt(JMeterUtils.getPropDefault(tstName + "_cntDelayed", "0"));
        int sent = Integer.parseInt(JMeterUtils.getPropDefault(tstName + "_cntSent", "0"));
        float rps = Float.parseFloat(JMeterUtils.getPropDefault(tstName + "_rps", "0"));
        int needed = con;
        if (rps <= 0) {
            // no action needed
        } else if (delayed > 0) {
            needed = decreaseNeeded(spare, con, delayed, needed);
        } else if (sent < rps) {
            needed = (int) Math.ceil(con * (2 - sent / rps));
        }

        if (needed != con && log.isDebugEnabled()) {
            log.debug("Need to change " + concName + ": " + con + "=>" + needed + " (" + sent + "/" + rps + "/" + delayed + ")");
        }

        if (needed <= 0) {
            log.warn("Got concurrency less than zero: " + needed);
            needed = 1;
        }

        if (needed > limit) {
            log.warn("Got concurrency more than limit: " + needed);
            needed = limit;
        }

        JMeterUtils.setProperty(concName, String.valueOf(needed));
        JMeterUtils.setProperty(tstName + "_rps", "0");
        return String.valueOf(needed);
    }

    private int decreaseNeeded(double spare, int con, int delayed, int needed) {
        if (spare >= 1) { // absolute count
            needed -= delayed - spare;
            if (con > spare) {
                return (int) Math.max(spare, needed);
            }
        } else {
            if (delayed > Math.ceil(con * spare)) {
                needed = (int) (con * (1 - spare));
            }
        }

        return needed;
    }

    @Override
    public synchronized void setParameters(Collection<CompoundVariable> parameters) throws InvalidVariableException {
        checkParameterCount(parameters, MIN_PARAMETER_COUNT, desc.size());
        values = parameters.toArray(new CompoundVariable[0]);
    }

    @Override
    public String getReferenceKey() {
        return KEY;
    }

    @Override
    public List<String> getArgumentDesc() {
        return desc;
    }

    @Override
    public void testStarted() {
        testStarted(MainFrame.LOCAL);
    }

    @Override
    public void testStarted(String s) {
        justStarted = true;
    }

    @Override
    public void testEnded() {
        testEnded(MainFrame.LOCAL);
    }

    @Override
    public void testEnded(String s) {

    }

}
