package com.blazemeter.jmeter.threads;

/**
 * Created by undera on 2/28/16.
 */
public interface ParamsPanel {
    void modelToUI(AbstractDynamicThreadGroup tg);

    void UItoModel(AbstractDynamicThreadGroup tg);

    void clearUI();
}
