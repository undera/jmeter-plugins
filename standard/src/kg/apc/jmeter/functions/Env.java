package kg.apc.jmeter.functions;

import kg.apc.jmeter.JMeterPluginsUtils;
import org.apache.jmeter.engine.util.CompoundVariable;
import org.apache.jmeter.functions.AbstractFunction;
import org.apache.jmeter.functions.InvalidVariableException;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.samplers.Sampler;
import org.apache.jmeter.threads.JMeterVariables;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class Env extends AbstractFunction {

    private static final List<String> desc = new LinkedList<String>();
    private static final String KEY = "__env";

    // Number of parameters expected - used to reject invalid calls
    private static final int MIN_PARAMETER_COUNT = 1;
    private static final int MAX_PARAMETER_COUNT = 3;

    static {
        desc.add("Name of environment variable");
        desc.add("Name of variable in which to store the result (optional)");
        desc.add("Default value");
    }

    private CompoundVariable[] values;

    /**
     * No-arg constructor.
     */
    public Env() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized String execute(SampleResult previousResult, Sampler currentSampler)
            throws InvalidVariableException {
        String propertyName = values[0].execute();
        String propertyDefault = propertyName;
        if (values.length > 2) { // We have a 3rd parameter
            propertyDefault = values[2].execute();
        }
        String propertyValue = JMeterPluginsUtils.getEnvDefault(propertyName, propertyDefault);
        if (values.length > 1) {
            String variableName = values[1].execute();
            if (variableName.length() > 0) {// Allow for empty name
                final JMeterVariables variables = getVariables();
                if (variables != null) {
                    variables.put(variableName, propertyValue);
                }
            }
        }
        return propertyValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void setParameters(Collection<CompoundVariable> parameters) throws InvalidVariableException {
        checkParameterCount(parameters, MIN_PARAMETER_COUNT, MAX_PARAMETER_COUNT);
        values = parameters.toArray(new CompoundVariable[0]);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getReferenceKey() {
        return KEY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getArgumentDesc() {
        return desc;
    }
}
