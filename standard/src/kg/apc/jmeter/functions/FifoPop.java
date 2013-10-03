package kg.apc.jmeter.functions;

import kg.apc.jmeter.modifiers.FifoMap;
import org.apache.jmeter.engine.util.CompoundVariable;
import org.apache.jmeter.functions.AbstractFunction;
import org.apache.jmeter.functions.InvalidVariableException;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.samplers.Sampler;
import org.apache.jmeter.threads.JMeterVariables;
import org.apache.jmeter.util.JMeterUtils;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class FifoPop extends AbstractFunction {

    private static final List<String> desc = new LinkedList<String>();
    private static final String KEY = "__fifoPop";
    private long timeout;

    static {
        desc.add("FIFO queue name to pop value");
        desc.add("Name of variable in which to store the result (optional)");
    }

    private Object[] values;

    public FifoPop() {
        timeout = JMeterUtils.getPropDefault(FifoMap.TIMEOUT_PROP, Long.MAX_VALUE);
    }

    @Override
    public synchronized String execute(SampleResult previousResult, Sampler currentSampler)
            throws InvalidVariableException {
        String fifoName = ((CompoundVariable) values[0]).execute();

        String value;
        try {
            value = FifoMap.getInstance().pop(fifoName, timeout);
        } catch (InterruptedException ex) {
            value = "INTERRUPTED";
        }

        JMeterVariables vars = getVariables();
        if (vars != null && values.length > 1) {
            String varName = ((CompoundVariable) values[1]).execute().trim();
            vars.put(varName, value);
        }

        return value;
    }

    @Override
    public synchronized void setParameters(Collection<CompoundVariable> parameters) throws InvalidVariableException {
        checkMinParameterCount(parameters, 1);
        values = parameters.toArray();
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
