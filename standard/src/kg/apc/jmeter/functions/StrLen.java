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
public class StrLen extends AbstractFunction {

    private static final List<String> desc = new LinkedList<String>();
    private static final String KEY = "__strLen"; 

    static {
        desc.add("String to measure length"); 
        desc.add("Name of variable in which to store the result (optional)"); 
    }
    private Object[] values;

    /**
     * No-arg constructor.
     */
    public StrLen() {
    }

    /** {@inheritDoc} */
    @Override
    public synchronized String execute(SampleResult previousResult, Sampler currentSampler)
            throws InvalidVariableException {
        JMeterVariables vars = getVariables();
        Integer len=((CompoundVariable) values[0]).execute().length();

        if (vars != null && values.length>1) {
            String varName = ((CompoundVariable) values[1]).execute().trim();
            vars.put(varName, len.toString());
        }

        return len.toString();

    }

    /** {@inheritDoc} */
    @Override
    public synchronized void setParameters(Collection<CompoundVariable> parameters) throws InvalidVariableException {
        checkMinParameterCount(parameters, 1);
        values = parameters.toArray();
    }

    /** {@inheritDoc} */
    @Override
    public String getReferenceKey() {
        return KEY;
    }

    /** {@inheritDoc} */
    public List<String> getArgumentDesc() {
        return desc;
    }
}
