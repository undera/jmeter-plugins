package com.blazemeter.jmeter;

import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.visualizers.gui.AbstractListenerGui;

import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

public class BlazemeterUploaderGui extends AbstractListenerGui implements HyperlinkListener {
    @Override
    public void hyperlinkUpdate(HyperlinkEvent e) {

    }

    @Override
    public String getLabelResource() {
        return null;
    }

    @Override
    public TestElement createTestElement() {
        return null;
    }

    @Override
    public void modifyTestElement(TestElement testElement) {

    }
}
