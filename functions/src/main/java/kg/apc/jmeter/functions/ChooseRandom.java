package kg.apc.jmeter.functions;

import org.apache.jmeter.engine.util.CompoundVariable;
import org.apache.jmeter.functions.AbstractFunction;
import org.apache.jmeter.functions.InvalidVariableException;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.samplers.Sampler;
import org.apache.jmeter.threads.JMeterVariables;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class ChooseRandom extends AbstractFunction {

    private static final List<String> desc = new LinkedList<String>();
    private static final String KEY = "__chooseRandom";
    private static final Random random = new Random(System.currentTimeMillis());

    static {
        desc.add("Any number of values to choose from");
        desc.add("Last value must be variable name where choice will be stored");
    }

    private Object[] values;

    /**
     * No-arg constructor.
     */
    public ChooseRandom() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized String execute(SampleResult previousResult, Sampler currentSampler)
            throws InvalidVariableException {
        JMeterVariables vars = getVariables();
        String varName = ((CompoundVariable) values[values.length - 1]).execute().trim();
        int index = random.nextInt(values.length - 1);
        String choice = ((CompoundVariable) values[index]).execute();

        if (vars != null && varName != null && varName.length() > 0) {// vars will be null on TestPlan
            vars.put(varName, choice);
        }

        return choice;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void setParameters(Collection<CompoundVariable> parameters) throws InvalidVariableException {
        checkMinParameterCount(parameters, 3);
        values = parameters.toArray();
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
