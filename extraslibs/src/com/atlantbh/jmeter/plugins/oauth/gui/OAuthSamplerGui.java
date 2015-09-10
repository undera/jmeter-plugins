/*!
 * AtlantBH Custom Jmeter Components v1.0.0
 * http://www.atlantbh.com/jmeter-components/
 *
 * Copyright 2011, AtlantBH
 *
 * Licensed under the under the Apache License, Version 2.0.
 */
package com.atlantbh.jmeter.plugins.oauth.gui;

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

import com.atlantbh.jmeter.plugins.oauth.OAuthSampler;
import kg.apc.jmeter.JMeterPluginsUtils;

/**
 * Frontend to the OAuth sampler.
 *
 * It only exposes GET/POST/PUT/DELETE.
 */
public class OAuthSamplerGui extends AbstractSamplerGui {

    private static final long serialVersionUID = -5576774730632101012L;
    private JCheckBox useKeepAlive;
    private JCheckBox automaticRedirect;
    private JLabeledTextField consumerKey;
    private JLabeledTextField consumerSecret;
    private JLabeledTextArea body;
    private JLabeledTextArea headers;
    private JLabeledTextField hostBaseUrl;
    private JLabeledTextField resource;
    private JLabeledTextField port;
    private JLabeledChoice httpMethods;
    private static final String WIKIPAGE = "OAuthSampler";

    public OAuthSamplerGui() {
        init();
    }

    @Override
    public String getLabelResource() {
        return this.getClass().getSimpleName();
    }

    @Override
    public String getStaticLabel() {
        return JMeterPluginsUtils.prefixLabel("OAuth Sampler");
    }

    /*
     * (non-Javadoc)
     *
     * @see org.apache.jmeter.gui.JMeterGUIComponent#createTestElement()
     */
    @Override
    public TestElement createTestElement() {
        OAuthSampler sampler = new OAuthSampler();
        modifyTestElement(sampler);
        sampler.setComment(JMeterPluginsUtils.getWikiLinkText(WIKIPAGE));
        return sampler;
    }

    public void clear() {
        this.consumerKey.setText("");
        this.consumerSecret.setText("");
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
     * @see
     * org.apache.jmeter.gui.JMeterGUIComponent#modifyTestElement(TestElement)
     */
    @Override
    public void modifyTestElement(TestElement s) {
        super.configureTestElement(s);
        if (s instanceof OAuthSampler) {
            OAuthSampler sampler = (OAuthSampler) s;
            sampler.setConsumerKey(consumerKey.getText());
            sampler.setConsumerSecret(consumerSecret.getText());
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
    @Override
    public void clearGui() {
        super.clearGui();
        clear();
    }

    private JPanel getResourceConfigPanel() {
        consumerKey = new JLabeledTextField("Consumer Key", 25);
        consumerSecret = new JLabeledTextField("Consumer Secret", 25);
        automaticRedirect = new JCheckBox(JMeterUtils.getResString("follow_redirects"));
        httpMethods = new JLabeledChoice("Method", new String[]{"GET", "POST", "PUT", "DELETE", "PATCH"});
        httpMethods.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                JLabeledChoice c = (JLabeledChoice) e.getSource();
                String text = c.getText();
                if ("PUT".equals(text) || "POST".equals(text) || "PATCH".equals(text)) {
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
        VerticalPanel resourceConfigPanel = new VerticalPanel();
        HorizontalPanel oAuthPanel = new HorizontalPanel();
        oAuthPanel.add(consumerKey);
        oAuthPanel.add(consumerSecret);
        HorizontalPanel panel1 = new HorizontalPanel();
        panel1.add(httpMethods);
        panel1.add(useKeepAlive);
        panel1.add(automaticRedirect);
        HorizontalPanel panel2 = new HorizontalPanel();
        panel2.add(hostBaseUrl);
        panel2.add(port);
        HorizontalPanel panel3 = new HorizontalPanel();
        panel3.add(resource);
        resourceConfigPanel.add(panel1);
        resourceConfigPanel.add(oAuthPanel);
        resourceConfigPanel.add(panel2);
        resourceConfigPanel.add(panel3);
        return resourceConfigPanel;
    }

    private JPanel getRequestPanel() {
        body = new JLabeledTextArea("Body"); 
        headers = new JLabeledTextArea("Headers"); 
        VerticalPanel panel = new VerticalPanel();
        panel.add(headers, BorderLayout.NORTH);
        panel.add(body, BorderLayout.CENTER);
        return panel;
    }

    private void init() {
        setLayout(new BorderLayout());
        setBorder(makeBorder());
        add(JMeterPluginsUtils.addHelpLinkToPanel(makeTitlePanel(), WIKIPAGE), BorderLayout.NORTH);
        VerticalPanel panel = new VerticalPanel();
        panel.setBorder(BorderFactory.createEtchedBorder());
        panel.add(getResourceConfigPanel(), BorderLayout.NORTH);
        panel.add(getRequestPanel(), BorderLayout.CENTER);
        add(panel, BorderLayout.CENTER);
    }

    @Override
    public void configure(TestElement el) {
        super.configure(el);
        OAuthSampler sampler = (OAuthSampler) el;
        consumerKey.setText(sampler.getConsumerKey());
        consumerSecret.setText(sampler.getConsumerSecret());
        body.setText(sampler.getRequestBody());
        headers.setText(sampler.getRequestHeaders());
        useKeepAlive.setSelected(sampler.getUseKeepAlive());
        automaticRedirect.setSelected(sampler.getAutoRedirects());
        httpMethods.setText(sampler.getMethod());
        resource.setText(sampler.getResource());
        port.setText(sampler.getPortNumber());
        hostBaseUrl.setText(sampler.getHostBaseUrl());
    }

    @Override
    public Dimension getPreferredSize() {
        return getMinimumSize();
    }
}