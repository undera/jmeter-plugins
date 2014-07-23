package kg.apc.jmeter.functions;

import org.apache.jmeter.engine.util.CompoundVariable;
import org.apache.jmeter.functions.AbstractFunction;
import org.apache.jmeter.functions.InvalidVariableException;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.samplers.Sampler;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class LDJSONRead extends AbstractFunction {


    private static final List<String> desc = new LinkedList<String>();
    private static final String KEY = "__fifoGet";

    static {
        desc.add("File name to read LDJSON lines from");
        desc.add("Wait for new lines to appear in file (optional, true/false, default false)");
    }

    private Object[] values;

    public LDJSONRead() {
    }

    @Override
    public synchronized String execute(SampleResult previousResult, Sampler currentSampler)
            throws InvalidVariableException {
        String fileName = ((CompoundVariable) values[0]).execute();
        boolean followTheFile = false;
        if (values.length > 1) {
            String followStr = ((CompoundVariable) values[1]).execute().trim();
            followTheFile = followStr.equals("true");
        }

        //JMeterVariables vars = getVariables();
        String value="";

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

