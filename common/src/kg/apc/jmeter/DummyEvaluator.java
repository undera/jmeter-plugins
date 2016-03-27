package kg.apc.jmeter;

public class DummyEvaluator extends JMeterVariableEvaluator {
    @Override
    public String evaluate(String text) {
        return text;
    }
}
