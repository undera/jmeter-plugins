package com.blazemeter.jmeter.xmpp;

import com.blazemeter.jmeter.xmpp.actions.AbstractXMPPAction;
import com.blazemeter.ui.ComponentTitledBorder;
import kg.apc.jmeter.JMeterPluginsUtils;
import org.apache.jmeter.samplers.gui.AbstractSamplerGui;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;
import java.util.Map;

public class JMeterXMPPSamplerGui extends AbstractSamplerGui implements ActionListener {
    private static final Logger log = LoggingManager.getLoggerForClass();
    private static final String CONTAINER = "container";

    private ButtonGroup actionsGroup;
    private Map<String, AbstractXMPPAction> actions = getActions();

    protected Map<String, AbstractXMPPAction> getActions() {
        return JMeterXMPPConnection.getAvailableActions();
    }

    public JMeterXMPPSamplerGui() {
        super();
        init();
        initFields();
    }

    @Override
    public TestElement createTestElement() {
        TestElement el = new JMeterXMPPSampler();
        modifyTestElement(el);
        el.setComment("This plugin is developed by www.blazemeter.com");
        return el;
    }

    @Override
    public void configure(TestElement element) {
        super.configure(element);
        if (element instanceof JMeterXMPPSampler) {
            JMeterXMPPSampler sampler = (JMeterXMPPSampler) element;
            // fill controls
            selectActionByName(sampler.getAction());

            for (AbstractXMPPAction action : actions.values()) {
                action.setGuiFieldsFromSampler(sampler);
            }
        }
    }

    @Override
    public void modifyTestElement(TestElement element) {
        configureTestElement(element);
        if (element instanceof JMeterXMPPSampler) {
            JMeterXMPPSampler sampler = (JMeterXMPPSampler) element;
            sampler.setAction(getSelectedAction());

            for (AbstractXMPPAction action : actions.values()) {
                action.setSamplerProperties(sampler);
            }
        }
    }

    @Override
    public void clearGui() {
        super.clearGui();
        initFields();
    }

    @Override
    public String getLabelResource() {
        return getClass().getCanonicalName();
    }

    @Override
    public String getStaticLabel() {
        return JMeterPluginsUtils.prefixLabel("XMPP Sampler");
    }

    private void initFields() {
        for (AbstractXMPPAction action : actions.values()) {
            action.clearGui();
        }

        actionsGroup.getElements().nextElement().doClick();
    }

    private void init() {
        setLayout(new BorderLayout(0, 5));
        setBorder(makeBorder());

        add(JMeterPluginsUtils.addHelpLinkToPanel(makeTitlePanel(), getWikiPage()), BorderLayout.NORTH);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder());

        addActionBlocks(mainPanel);

        add(mainPanel, BorderLayout.CENTER);
    }

    private void addActionBlocks(JPanel mainPanel) {
        GridBagConstraints labelConstraints = new GridBagConstraints();
        labelConstraints.anchor = GridBagConstraints.FIRST_LINE_END;

        GridBagConstraints editConstraints = new GridBagConstraints();
        editConstraints.anchor = GridBagConstraints.FIRST_LINE_START;
        editConstraints.weightx = 1.0;
        editConstraints.fill = GridBagConstraints.HORIZONTAL;

        editConstraints.insets = new Insets(2, 0, 0, 0);
        labelConstraints.insets = new Insets(2, 0, 0, 0);

        actionsGroup = new ButtonGroup();
        for (AbstractXMPPAction action : actions.values()) {
            JRadioButton radio = new JRadioButton(action.getLabel());
            radio.addActionListener(this);
            actionsGroup.add(radio);
            JPanel contentPanel = new JPanel(new GridBagLayout());
            radio.putClientProperty(CONTAINER, contentPanel);

            action.addUI(contentPanel, labelConstraints, editConstraints);

            Border border = new ComponentTitledBorder(radio, contentPanel, BorderFactory.createEtchedBorder());
            contentPanel.setBorder(border);
            contentPanel.setEnabled(false);

            mainPanel.add(contentPanel);
            mainPanel.add(Box.createRigidArea(new Dimension(0, 10))); // a bit space between panels
        }
    }

    private void selectActionByName(String action) {
        AbstractXMPPAction actionObj = actions.get(action);
        Enumeration<AbstractButton> buttons = actionsGroup.getElements();
        if (actionObj != null) {
            while (buttons.hasMoreElements()) {
                AbstractButton button = buttons.nextElement();
                if (button.getText().equals(actionObj.getLabel())) {
                    button.doClick();
                    return;
                }
            }
        }
        log.warn("Did not find control to select for action: " + action);
    }

    private String getSelectedAction() {
        Enumeration<AbstractButton> buttons = actionsGroup.getElements();
        while (buttons.hasMoreElements()) {
            AbstractButton button = buttons.nextElement();
            if (button.isSelected()) {
                for (AbstractXMPPAction action : actions.values()) {
                    if (action.getLabel().equals(button.getText()))
                        return action.getClass().getCanonicalName();
                }
            }
        }
        log.warn("No action selected");
        return "";
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        JRadioButton button = (JRadioButton) actionEvent.getSource();
        disableAllBlocks();
        setBlockEnabled(button, true);
        if (button.getParent() != null && button.getParent().getParent() != null) {
            button.getParent().getParent().getParent().repaint();
        }
    }

    private void disableAllBlocks() {
        Enumeration<AbstractButton> btns = actionsGroup.getElements();
        while (btns.hasMoreElements()) {
            AbstractButton btn = btns.nextElement();
            setBlockEnabled(btn, false);
        }
    }

    private void setBlockEnabled(AbstractButton obj, boolean b) {
        Container container = (Container) obj.getClientProperty(CONTAINER);
        for (Component component : container.getComponents()) {
            component.setEnabled(b);
        }
    }

    private String getWikiPage() {
        return "XMPPSampler";
    }
}
