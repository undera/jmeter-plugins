package kg.apc.jmeter.functions;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.apache.jmeter.engine.util.CompoundVariable;
import org.apache.jmeter.functions.AbstractFunction;
import org.apache.jmeter.functions.InvalidVariableException;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.samplers.Sampler;
import org.apache.jmeter.threads.JMeterVariables;

public class IsDefined extends AbstractFunction {

    private static final List<String> desc = new LinkedList<String>();
    private static final String KEY = "__isDefined";

    static {
        desc.add("Name of variable to test");
    }
    private Object[] values;

    /**
     * No-arg constructor.
     */
    public IsDefined() {
    }

    /** {@inheritDoc} */
    @Override
    public synchronized String execute(SampleResult previousResult, Sampler currentSampler)
            throws InvalidVariableException {
        JMeterVariables vars = getVariables();
        String res = ((CompoundVariable) values[0]).execute().trim();

        if (vars != null) {
            Object var = vars.getObject(res);
            if (var != null) {
                return "1";
            }
        }

        return "0";

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
    @Override
    public List<String> getArgumentDesc() {
        return desc;
    }
}
