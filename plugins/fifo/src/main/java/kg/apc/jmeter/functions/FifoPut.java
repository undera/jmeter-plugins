package kg.apc.jmeter.functions;

import kg.apc.jmeter.modifiers.FifoMap;
import org.apache.jmeter.engine.util.CompoundVariable;
import org.apache.jmeter.functions.AbstractFunction;
import org.apache.jmeter.functions.InvalidVariableException;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.samplers.Sampler;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class FifoPut extends AbstractFunction {
    private static final Logger log = LoggingManager.getLoggerForClass();

    private static final List<String> desc = new LinkedList<String>();
    private static final String KEY = "__fifoPut";

    static {
        desc.add("Queue name to put value");
        desc.add("String value to put into FIFO queue");
    }

    private Object[] values;

    public FifoPut() {
        FifoMap.getInstance().clear();
    }

    @Override
    public synchronized String execute(SampleResult previousResult, Sampler currentSampler)
            throws InvalidVariableException {
        String fifoName = ((CompoundVariable) values[0]).execute();
        String value = ((CompoundVariable) values[1]).execute();
        try {
            FifoMap.getInstance().put(fifoName, value);
        } catch (InterruptedException ex) {
            log.warn("Interrupted put into queue " + fifoName);
            value = "INTERRUPTED";
        }

        return value;
    }

    @Override
    public synchronized void setParameters(Collection<CompoundVariable> parameters) throws InvalidVariableException {
        checkMinParameterCount(parameters, 2);
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
