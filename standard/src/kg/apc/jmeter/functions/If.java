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

public class If extends AbstractFunction {
    private static final List<String> desc = new LinkedList<String>();
    private static final String KEY = "__if";

    static {
        desc.add("Actual value");
        desc.add("Expected value");
        desc.add("Result if actual == expected");
        desc.add("Result if actual != expected");
        desc.add("Name of variable in which to store the result (optional)");
    }

    private Object[] values;

    public If() {
    }

    @Override
    public synchronized String execute(SampleResult previousResult, Sampler currentSampler)
            throws InvalidVariableException {

        String actual = getParameter(0);
        String expected = getParameter(1);

        String result = null;
        if (actual.equals(expected)) {
            result = getParameter(2).toString();
        } else {
            result = getParameter(3).toString();
        }

        JMeterVariables vars = getVariables();
        if (vars != null && values.length > 4) {
            String varName = getParameter(4).trim();
            vars.put(varName, result);
        }

        return result;
    }

    @Override
    public synchronized void setParameters(Collection<CompoundVariable> parameters) throws InvalidVariableException {
        checkMinParameterCount(parameters, 4);
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

    private String getParameter(int i) {
        return ((CompoundVariable) values[i]).execute();
    }
}
