package kg.apc.jmeter.control.sampler;

import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.engine.util.ValueReplacer;
import org.apache.jmeter.functions.InvalidVariableException;
import org.apache.jmeter.testelement.property.JMeterProperty;
import org.apache.jmeter.threads.JMeterContextService;
import org.apache.jmeter.threads.JMeterVariables;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class SetVariablesActionTest {
    private SetVariablesAction instance;

    @Before
    public void setUp() {
        JMeterVariables vars = new JMeterVariables();
        vars.put("var1", "val1");
        JMeterContextService.getContext().setVariables(vars);
        JMeterContextService.getContext().setSamplingStarted(true);

        instance = new SetVariablesAction();
        instance.setRunningVersion(true);
    }

    @After
    public void tearDown() {
        JMeterContextService.getContext().setSamplingStarted(false);
    }

    @Test
    public void testSample() throws InvalidVariableException {

        System.out.println("next");
        Arguments args = new Arguments();
        args.addArgument("var2", "${var1}");
        args.addArgument("var3", "${var2}");


        instance.setUserDefinedVariables(args);

        ValueReplacer replacer = new ValueReplacer();
        replacer.replaceValues(instance);
        args.setRunningVersion(true);

        instance.sample(null);

        JMeterVariables vars = JMeterContextService.getContext().getVariables();
        assertEquals("${var2}", vars.get("var3"));
        assertEquals("val1", vars.get("var2"));
        instance.sample(null);
        assertEquals("val1", vars.get("var3"));
    }

    @Test
    public void testSetUserDefinedVariables() {
        System.out.println("setUserDefinedVariables");
        Arguments vars = new Arguments();
        vars.addArgument("var1", "val0");
        instance.setUserDefinedVariables(vars);
        JMeterProperty property = instance.getUserDefinedVariablesAsProperty();
        Arguments args = (Arguments) property.getObjectValue();
        assertEquals("val0", args.getArgumentsAsMap().get("var1"));
    }

    @Test
    public void testGetUserDefinedVariablesAsProperty() {
        System.out.println("getUserDefinedVariablesAsProperty");
        Arguments vars = new Arguments();
        vars.addArgument("key", "value");
        instance.setUserDefinedVariables(vars);
        JMeterProperty result = instance.getUserDefinedVariablesAsProperty();
        assertNotNull(result);
    }
}