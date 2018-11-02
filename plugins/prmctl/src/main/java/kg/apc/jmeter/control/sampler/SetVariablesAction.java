package kg.apc.jmeter.control.sampler;

import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.samplers.AbstractSampler;
import org.apache.jmeter.samplers.Entry;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.testelement.property.JMeterProperty;
import org.apache.jmeter.testelement.property.TestElementProperty;
import org.apache.jmeter.threads.JMeterContextService;
import org.apache.jmeter.threads.JMeterVariables;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

import java.util.Iterator;
import java.util.Map;

public class SetVariablesAction extends AbstractSampler {
    private static final Logger log = LoggingManager.getLoggerForClass();

    private void processVariables() {
        final Arguments args1 = (Arguments) this.getUserDefinedVariablesAsProperty().getObjectValue();
        Arguments args = (Arguments) args1.clone();

        final JMeterVariables vars = JMeterContextService.getContext().getVariables();

        Iterator<Map.Entry<String, String>> it = args.getArgumentsAsMap().entrySet().iterator();
        Map.Entry<String, String> var;
        while (it.hasNext()) {
            var = it.next();
            if (log.isDebugEnabled()) {
                log.debug("Setting " + var.getKey() + "=" + var.getValue());
            }
            vars.put(var.getKey(), var.getValue());
        }
    }


    @Override
    public SampleResult sample(Entry entry) {
        processVariables();
        return null;
    }

    public void setUserDefinedVariables(Arguments vars) {
        setProperty(new TestElementProperty(this.getClass().getSimpleName(), vars));
    }

    public JMeterProperty getUserDefinedVariablesAsProperty() {
        return getProperty(this.getClass().getSimpleName());
    }
}
