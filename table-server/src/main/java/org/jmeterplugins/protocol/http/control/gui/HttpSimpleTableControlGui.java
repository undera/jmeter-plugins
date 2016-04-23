/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.jmeterplugins.protocol.http.control.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import kg.apc.jmeter.JMeterPluginsUtils;

import org.apache.jmeter.control.gui.LogicControllerGui;
import org.apache.jmeter.gui.JMeterGUIComponent;
import org.apache.jmeter.gui.UnsharedComponent;
import org.apache.jmeter.gui.util.HorizontalPanel;
import org.apache.jmeter.gui.util.MenuFactory;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;
import org.jmeterplugins.protocol.http.control.HttpSimpleTableControl;

public class HttpSimpleTableControlGui extends LogicControllerGui implements
        JMeterGUIComponent, ActionListener, UnsharedComponent {

    private static final long serialVersionUID = 240L;

    private static final Logger log = LoggingManager.getLoggerForClass();

    public static final String WIKIPAGE = "HttpSimpleTableServer";

    private JTextField portField;

    private JTextField datasetDirectoryField;

    private JCheckBox timestampChkBox;

    private JButton stop, start;

    private static final String ACTION_STOP = "stop"; 

    private static final String ACTION_START = "start"; 

    private HttpSimpleTableControl simpleTableController;

    public HttpSimpleTableControlGui() {
        super();
        log.debug("Creating HttpSimpleTableControlGui");
        init();
    }

    @Override
    public TestElement createTestElement() {
        simpleTableController = new HttpSimpleTableControl();
        log.debug("creating/configuring model = " + simpleTableController);
        modifyTestElement(simpleTableController);
        return simpleTableController;
    }

    /**
     * Modifies a given TestElement to mirror the data in the gui components.
     *
     * @see org.apache.jmeter.gui.JMeterGUIComponent#modifyTestElement(TestElement)
     */
    @Override
    public void modifyTestElement(TestElement el) {
        configureTestElement(el);
        if (el instanceof HttpSimpleTableControl) {
            simpleTableController = (HttpSimpleTableControl) el;
            if (portField.getText().isEmpty()) {
                simpleTableController
                        .setPort(HttpSimpleTableControl.DEFAULT_PORT_S);
            } else {
                simpleTableController.setPort(portField.getText());
            }
            if (datasetDirectoryField.getText().isEmpty()) {
                simpleTableController
                        .setDataDir(HttpSimpleTableControl.DEFAULT_DATA_DIR);
            } else {
                simpleTableController.setDataDir(datasetDirectoryField
                        .getText());
            }
            simpleTableController.setTimestamp(timestampChkBox.isSelected());
        }
    }

    public String getLabelResource() {
        return this.getClass().getSimpleName();
    }

    @Override
    public String getStaticLabel() {
        return JMeterPluginsUtils.prefixLabel("HTTP Simple Table Server");
    }

    @Override
    public Collection<String> getMenuCategories() {
        return Arrays.asList(new String[] { MenuFactory.NON_TEST_ELEMENTS });
    }

    @Override
    public void configure(TestElement element) {
        log.debug("Configuring gui with " + element);
        super.configure(element);
        simpleTableController = (HttpSimpleTableControl) element;
        portField.setText(simpleTableController.getPortString());
        datasetDirectoryField.setText(simpleTableController.getDataDir());
        timestampChkBox.setSelected(simpleTableController.getTimestamp());
        repaint();
    }

    @Override
    public void actionPerformed(ActionEvent action) {
        String command = action.getActionCommand();
        Exception except = null;

        if (command.equals(ACTION_STOP)) {
            simpleTableController.stopHttpSimpleTable();
            stop.setEnabled(false);
            start.setEnabled(true);
        } else if (command.equals(ACTION_START)) {
            modifyTestElement(simpleTableController);
            try {
                simpleTableController.startHttpSimpleTable();
            } catch (IOException e) {
                e.printStackTrace();
                except = e;
            }
            if (null == except) {
                start.setEnabled(false);
                stop.setEnabled(true);
            }
        }
    }

    private void init() {
        setLayout(new BorderLayout(0, 5));
        setBorder(makeBorder());

        add(JMeterPluginsUtils.addHelpLinkToPanel(makeTitlePanel(), WIKIPAGE),
                BorderLayout.NORTH);

        JPanel mainPanel = new JPanel(new BorderLayout());

        Box myBox = Box.createVerticalBox();
        myBox.add(createPortPanel());
        mainPanel.add(myBox, BorderLayout.NORTH);

        mainPanel.add(createControls(), BorderLayout.CENTER);

        add(mainPanel, BorderLayout.CENTER);
    }

    private JPanel createControls() {
        start = new JButton(JMeterUtils.getResString("start")); 
        start.addActionListener(this);
        start.setActionCommand(ACTION_START);
        start.setEnabled(true);

        stop = new JButton(JMeterUtils.getResString("stop")); 
        stop.addActionListener(this);
        stop.setActionCommand(ACTION_STOP);
        stop.setEnabled(false);

        JPanel panel = new JPanel();
        panel.add(start);
        panel.add(stop);
        return panel;
    }

    private JPanel createPortPanel() {
        portField = new JTextField(HttpSimpleTableControl.DEFAULT_PORT_S, 8);
        portField.setName(HttpSimpleTableControl.PORT);

        JLabel label = new JLabel(JMeterUtils.getResString("port")); 
        label.setLabelFor(portField);

        datasetDirectoryField = new JTextField(
                HttpSimpleTableControl.DEFAULT_DATA_DIR, 8);
        datasetDirectoryField.setName(HttpSimpleTableControl.DATA_DIR);

        JLabel ddLabel = new JLabel("Dataset directory:"); 
        ddLabel.setLabelFor(datasetDirectoryField);

        timestampChkBox = new JCheckBox();
        timestampChkBox.setSelected(HttpSimpleTableControl.DEFAULT_TIMESTAMP);
        timestampChkBox.setName(HttpSimpleTableControl.TIMESTAMP);

        JLabel tsLabel = new JLabel("Timestamp:"); 
        tsLabel.setLabelFor(timestampChkBox);

        HorizontalPanel panel = new HorizontalPanel();
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Settings")); 

        panel.add(label);
        panel.add(portField);

        panel.add(ddLabel);
        panel.add(datasetDirectoryField);

        panel.add(tsLabel);
        panel.add(timestampChkBox);

        panel.add(Box.createHorizontalStrut(10));

        return panel;
    }

    @Override
    public void clearGui() {
        super.clearGui();
        portField.setText(HttpSimpleTableControl.DEFAULT_PORT_S);
        datasetDirectoryField.setText(HttpSimpleTableControl.DEFAULT_DATA_DIR);
        timestampChkBox.setSelected(HttpSimpleTableControl.DEFAULT_TIMESTAMP);
    }
}