package com.blazemeter.jmeter;

import org.apache.jmeter.gui.util.HorizontalPanel;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.visualizers.SimpleDataWriter;

import javax.swing.*;
import java.awt.*;

public class RotatingResultCollectorGui extends SimpleDataWriter {
    private JTextField maxSamplesCount;

    public RotatingResultCollectorGui() {
        super();
        init();
    }

    private void init() {
        setLayout(new BorderLayout());
        setBorder(makeBorder());
        Container topPanel = makeTitlePanel();
        add(topPanel, BorderLayout.NORTH);

        JPanel mainPanel = new HorizontalPanel();

        mainPanel.setBorder(BorderFactory.createTitledBorder("File Rotating Rules"));
        mainPanel.add(new JLabel("Limit samples count in file: ", JLabel.RIGHT));
        maxSamplesCount = new JTextField(20);
        mainPanel.add(maxSamplesCount);

        topPanel.add(mainPanel);
    }

    @Override
    public String getStaticLabel() {
        return "Rotating Simple Data Writer";
    }

    public TestElement createTestElement() {
        RotatingResultCollector element = new RotatingResultCollector();
        modifyTestElement(element);
        return element;
    }

    public void modifyTestElement(TestElement element) {
        super.modifyTestElement(element);

        if (element instanceof RotatingResultCollector) {
            RotatingResultCollector rotatingCollector = (RotatingResultCollector) element;
            rotatingCollector.setMaxSamplesCount(this.maxSamplesCount.getText());
        }
    }

    @Override
    public void configure(TestElement element) {
        super.configure(element);

        if (element instanceof RotatingResultCollector) {
            maxSamplesCount.setText(((RotatingResultCollector) element).getMaxSamplesCount());
        }
    }

    @Override
    public void clearGui() {
        super.clearGui();
        maxSamplesCount.setText("");
    }
}
