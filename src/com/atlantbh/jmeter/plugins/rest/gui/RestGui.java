/*  Copyright 2009 Fabrizio Cannizzo
 *
 *  This file is part of JMeterRestSampler.
 *
 *  JMeterRestSampler (http://code.google.com/p/rest-fixture/) is free software:
 *  you can redistribute it and/or modify it under the terms of the
 *  BSD License
 *
 *  You should have received a copy of the BSD License
 *  along with JMeterRestSampler.  If not, see <http://opensource.org/licenses/bsd-license.php>.
 *
 *  If you want to contact the author please see http://smartrics.blogspot.com
 */

package com.atlantbh.jmeter.plugins.rest.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.jmeter.gui.util.HorizontalPanel;
import org.apache.jmeter.gui.util.VerticalPanel;
import org.apache.jmeter.samplers.gui.AbstractSamplerGui;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.gui.JLabeledChoice;
import org.apache.jorphan.gui.JLabeledTextArea;
import org.apache.jorphan.gui.JLabeledTextField;

import com.atlantbh.jmeter.plugins.rest.RestSampler;

/**
 * Frontend to the REST sampler.
 * 
 * It only eposes GET/POST/PUT/DELETE.
 */
public class RestGui extends AbstractSamplerGui {
    private static final long serialVersionUID = -5576774730632101012L;
    private JCheckBox useKeepAlive;
    private JCheckBox automaticRedirect;
    private JLabeledTextArea body;
    private JLabeledTextArea headers;
    private JLabeledTextField hostBaseUrl;
    private JLabeledTextField resource;
    private JLabeledTextField port;
    private JLabeledChoice httpMethods;

    public RestGui() {
        init();
    }

    public String getLabelResource() {
        return "rest_sampler_title"; //$NON-NLS-1$
    }

    public String getStaticLabel() {
        return "Rest Sampler";
    }

    /*
     * (non-Javadoc)
     *
     * @see org.apache.jmeter.gui.JMeterGUIComponent#createTestElement()
     */
    public TestElement createTestElement() {
        RestSampler sampler = new RestSampler();
        modifyTestElement(sampler);
        return sampler;
    }

    public void clear() {
        this.httpMethods.setText("GET");
        this.hostBaseUrl.setText("");
        this.headers.setText("");
        this.resource.setText("");
        this.port.setText("8080");
        this.useKeepAlive.setSelected(true);
        this.automaticRedirect.setSelected(true);
        this.body.setText("");
    }

    /**
     * Modifies a given TestElement to mirror the data in the gui components.
     *
     * @see org.apache.jmeter.gui.JMeterGUIComponent#modifyTestElement(TestElement)
     */
    public void modifyTestElement(TestElement s) {
        super.configureTestElement(s);
        if (s instanceof RestSampler) {
            RestSampler sampler = (RestSampler) s;
            sampler.setRequestBody(body.getText());
            sampler.setMethod(httpMethods.getText());
            sampler.setUseKeepAlive(useKeepAlive.isSelected());
            sampler.setAutoRedirects(automaticRedirect.isSelected());
            sampler.setHostBaseUrl(hostBaseUrl.getText());
            sampler.setResource(resource.getText());
            sampler.setPortNumber(port.getText());
            sampler.setRequestHeaders(headers.getText());
        }
    }

    /**
     * Implements JMeterGUIComponent.clearGui
     */
    public void clearGui() {
        super.clearGui();
        clear();
    }

    private JPanel getResourceConfigPanel() {
        automaticRedirect = new JCheckBox(JMeterUtils.getResString("follow_redirects"));
        httpMethods = new JLabeledChoice("Method", new String[] { "GET", "POST", "PUT", "DELETE" });
        httpMethods.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                JLabeledChoice c = (JLabeledChoice) e.getSource();
                String text = c.getText();
                if ("PUT".equals(text) || "POST".equals(text)) {
                    automaticRedirect.setSelected(false);
                    automaticRedirect.setEnabled(false);
                } else {
                    automaticRedirect.setEnabled(true);
                }
            }
        });
        useKeepAlive = new JCheckBox(JMeterUtils.getResString("use_keepalive"));
        hostBaseUrl = new JLabeledTextField("Base Url", 25);
        resource = new JLabeledTextField("Resource", 20);
        port = new JLabeledTextField("Port", 10);
        VerticalPanel panel = new VerticalPanel();
        HorizontalPanel panel1 = new HorizontalPanel();
        panel1.add(httpMethods);
        panel1.add(useKeepAlive);
        panel1.add(automaticRedirect);
        HorizontalPanel panel2 = new HorizontalPanel();
        panel2.add(hostBaseUrl);
        panel2.add(port);
        HorizontalPanel panel3 = new HorizontalPanel();
        panel3.add(resource);
        panel.add(panel1);
        panel.add(panel2);
        panel.add(panel3);
        return panel;
    }

    private JPanel getRequestPanel() {
        body = new JLabeledTextArea("Body"); //$NON-NLS-1$
        headers = new JLabeledTextArea("Headers"); //$NON-NLS-1$
        VerticalPanel panel = new VerticalPanel();
        panel.add(headers, BorderLayout.NORTH);
        panel.add(body, BorderLayout.CENTER);
        return panel;
    }

    private void init() {
        setLayout(new BorderLayout());
        setBorder(makeBorder());
        add(makeTitlePanel(), BorderLayout.NORTH);
        VerticalPanel panel = new VerticalPanel();
        panel.setBorder(BorderFactory.createEtchedBorder());
        panel.add(getResourceConfigPanel(), BorderLayout.NORTH);
        panel.add(getRequestPanel(), BorderLayout.CENTER);
        add(panel, BorderLayout.CENTER);
    }

    public void configure(TestElement el) {
        super.configure(el);
        RestSampler sampler = (RestSampler) el;
        body.setText(sampler.getRequestBody());
        headers.setText(sampler.getRequestHeaders());
        useKeepAlive.setSelected(sampler.getUseKeepAlive());
        automaticRedirect.setSelected(sampler.getAutoRedirects());
        httpMethods.setText(sampler.getMethod());
        resource.setText(sampler.getResource());
        port.setText(sampler.getPortNumber());
        hostBaseUrl.setText(sampler.getHostBaseUrl());
    }

    public Dimension getPreferredSize() {
        return getMinimumSize();
    }
}