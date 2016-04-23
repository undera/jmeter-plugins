package kg.apc.jmeter.functions;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import org.apache.jmeter.engine.util.CompoundVariable;
import org.apache.jmeter.functions.AbstractFunction;
import org.apache.jmeter.functions.InvalidVariableException;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.samplers.Sampler;
import org.apache.jmeter.threads.JMeterVariables;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

public class Substring extends AbstractFunction {

    private static final List<String> desc = new LinkedList<String>();
    private static final String KEY = "__substring";

    static {
        desc.add("String to get part of");
        desc.add("Begin index (first is 0)");
        desc.add("End index");
        desc.add("Name of variable in which to store the result (optional)");
    }

    private Object[] values;

    public Substring() {
    }

    @Override
    public synchronized String execute(SampleResult previousResult, Sampler currentSampler)
            throws InvalidVariableException {

        String str = getParameter(0);

        int begin = Integer.parseInt(getParameter(1).trim());
        int len = Integer.parseInt(getParameter(2).trim());

        String totalString = str.substring(begin, len);

        JMeterVariables vars = getVariables();

        if (values.length > 3) {
            String varName = getParameter(3);
            if (vars != null && varName != null && varName.length() > 0) {// vars will be null on TestPlan
                vars.put(varName, totalString);
            }
        }

        return totalString;
    }

    @Override
    public synchronized void setParameters(Collection<CompoundVariable> parameters) throws InvalidVariableException {
        checkMinParameterCount(parameters, 3);
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
