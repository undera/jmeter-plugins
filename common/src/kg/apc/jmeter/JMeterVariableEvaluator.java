package kg.apc.jmeter;

import org.apache.jmeter.engine.util.CompoundVariable;
import org.apache.jmeter.testelement.property.JMeterProperty;
import org.apache.jmeter.testelement.property.StringProperty;

public class JMeterVariableEvaluator {
    public String evaluate(String text) {
        return (new CompoundVariable(text)).execute();
    }

    public double getDouble(JMeterProperty jMeterProperty) {
        String strval = evaluate(jMeterProperty.getStringValue());

        return (new StringProperty("", strval)).getDoubleValue();
    }
}
