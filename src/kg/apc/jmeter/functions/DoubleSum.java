package kg.apc.jmeter.functions;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.apache.jmeter.engine.util.CompoundVariable;
import org.apache.jmeter.functions.AbstractFunction;
import org.apache.jmeter.functions.InvalidVariableException;
import org.apache.jmeter.functions.LongSum;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.samplers.Sampler;
import org.apache.jmeter.threads.JMeterVariables;

/**
 * Provides a DoubleSum function that adds two or more Double values.
 * Mostly copied from LongSum
 * @see LongSum
 */
public class DoubleSum extends AbstractFunction {

    private static final List<String> desc = new LinkedList<String>();
    private static final String KEY = "__doubleSum"; 

    static {
        desc.add("First double to add"); 
        desc.add("Second long to add - further doubles can be summed by adding further arguments"); 
        desc.add("Name of variable in which to store the result (optional)"); 
    }
    private Object[] values;

    /**
     * No-arg constructor.
     */
    public DoubleSum() {
    }

    /** {@inheritDoc} */
    @Override
    public synchronized String execute(SampleResult previousResult, Sampler currentSampler)
            throws InvalidVariableException {

        JMeterVariables vars = getVariables();

        Double sum = 0D;
        String varName = ((CompoundVariable) values[values.length - 1]).execute().trim();

        for (int i = 0; i < values.length - 1; i++) {
            sum += Double.parseDouble(((CompoundVariable) values[i]).execute());
        }

        try {
            sum += Double.parseDouble(varName);
            varName = null; // there is no variable name
        } catch (NumberFormatException ignored) {
        }

        String totalString = Double.toString(sum);
        if (vars != null && varName != null && varName.length() > 0) {// vars will be null on TestPlan
            vars.put(varName, totalString);
        }

        return totalString;

    }

    /** {@inheritDoc} */
    @Override
    public synchronized void setParameters(Collection<CompoundVariable> parameters) throws InvalidVariableException {
        checkMinParameterCount(parameters, 2);
        values = parameters.toArray();
    }

    /** {@inheritDoc} */
    @Override
    public String getReferenceKey() {
        return KEY;
    }

    /** {@inheritDoc} */
    @Override
    public List<String> getArgumentDesc() {
        return desc;
    }
}
