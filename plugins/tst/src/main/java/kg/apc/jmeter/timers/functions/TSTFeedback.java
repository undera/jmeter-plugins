package kg.apc.jmeter.timers.functions;

import org.apache.jmeter.engine.util.CompoundVariable;
import org.apache.jmeter.functions.AbstractFunction;
import org.apache.jmeter.functions.InvalidVariableException;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.samplers.Sampler;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class TSTFeedback extends AbstractFunction {
    private static final Logger log = LoggingManager.getLoggerForClass();

    private static final List<String> desc = new LinkedList<>();
    private static final String KEY = "__tstFeedback";

    // Number of parameters expected - used to reject invalid calls
    private static final int MIN_PARAMETER_COUNT = 2;
    private static final int MAX_PARAMETER_COUNT = 4;

    static {
        //desc.add("Name of environment variable");
        //desc.add("Name of variable in which to store the result (optional)");
        //desc.add("Default value");
    }

    private CompoundVariable[] values;

    @Override
    public synchronized String execute(SampleResult previousResult, Sampler currentSampler)
            throws InvalidVariableException {
        String tstName = values[0].execute();
        String concName = values[1].execute();
        double spare = 0.1;
        if (values.length > 2) { // We have a 3rd parameter
            spare = Double.parseDouble(values[2].execute());
        }

        int limit = Integer.MAX_VALUE;
        if (values.length > 3) {
            limit = Integer.parseInt(values[3].execute());
        }

        int con = Integer.parseInt(JMeterUtils.getPropDefault(concName, "1"));
        int delayed = Integer.parseInt(JMeterUtils.getPropDefault(tstName + "_cntDelayed", "0"));
        int sent = Integer.parseInt(JMeterUtils.getPropDefault(tstName + "_cntSent", "0"));
        float rps = Float.parseFloat(JMeterUtils.getPropDefault(tstName + "_rps", "0"));
        int needed = con;
        if (rps <= 0) {
            // no action needed
        } else if (delayed > 0) {
            if (delayed > Math.ceil(con * spare)) {
                needed = (int) (con * (1 - spare));
            }
        } else if (sent < rps) {
            needed = (int) Math.ceil(con * (2 - sent / rps));
            if (needed > limit) {
                needed = limit;
            }
        }

        if (needed != con) {
            log.info("Need to change " + concName + ": " + con + "=>" + needed + " (" + sent + "/" + rps + "/" + delayed + ")");
        }

        JMeterUtils.setProperty(concName, String.valueOf(needed));
        JMeterUtils.setProperty(tstName + "_rps", "0");
        return String.valueOf(needed);
    }

    @Override
    public synchronized void setParameters(Collection<CompoundVariable> parameters) throws InvalidVariableException {
        checkParameterCount(parameters, MIN_PARAMETER_COUNT, MAX_PARAMETER_COUNT);
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
}
