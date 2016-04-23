package com.blazemeter.jmeter.threads;

import kg.apc.jmeter.JMeterVariableEvaluator;

public interface ParamsPanel {
    void modelToUI(AbstractDynamicThreadGroup tg);

    void UItoModel(AbstractDynamicThreadGroup tg, JMeterVariableEvaluator evaluator);

    void clearUI();
}
