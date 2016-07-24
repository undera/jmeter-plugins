package kg.apc.jmeter.control;

import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.control.GenericController;
import org.apache.jmeter.samplers.Sampler;
import org.apache.jmeter.testelement.property.JMeterProperty;
import org.apache.jmeter.testelement.property.TestElementProperty;
import org.apache.jmeter.threads.JMeterContextService;
import org.apache.jmeter.threads.JMeterVariables;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Map.Entry;

public class ParameterizedController extends GenericController implements Serializable {
    private static final Logger log = LoggingManager.getLoggerForClass();

    private void processVariables() {
        final Arguments args1 = (Arguments) this.getUserDefinedVariablesAsProperty().getObjectValue();
        Arguments args = (Arguments) args1.clone();

        final JMeterVariables vars = JMeterContextService.getContext().getVariables();

        Iterator<Entry<String, String>> it = args.getArgumentsAsMap().entrySet().iterator();
        Entry<String, String> var;
        while (it.hasNext()) {
            var = it.next();
            log.debug("Setting " + var.getKey() + "=" + var.getValue());
            vars.put(var.getKey(), var.getValue());
        }
    }

    @Override
    public Sampler next() {
        processVariables();
        return super.next();
    }

    public void setUserDefinedVariables(Arguments vars) {
        setProperty(new TestElementProperty(this.getClass().getSimpleName(), vars));
    }

    public JMeterProperty getUserDefinedVariablesAsProperty() {
        return getProperty(this.getClass().getSimpleName());
    }
}
