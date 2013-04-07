package com.googlecode.jmeter.plugins.webdriver.config.gui;

import kg.apc.jmeter.JMeterPluginsUtils;

public class FirefoxDriverConfigGui extends WebDriverConfigGui {
    @Override
    public String getStaticLabel() {
        return JMeterPluginsUtils.prefixLabel("Firefox Driver Config");
    }

    @Override
    public String getLabelResource() {
        return getClass().getCanonicalName();
    }

//    @Override
//    public void configure(TestElement element) {
//        super.configure(element);
//    }
//
//    @Override
//    public TestElement createTestElement() {
//        return super.createTestElement();
//    }
//
//    @Override
//    public void modifyTestElement(TestElement element) {
//        super.modifyTestElement(element);
//    }
//
//    @Override
//    public void clearGui() {
//        super.clearGui();
//    }
}
