package kg.apc.jmeter.control.sampler;

import kg.apc.jmeter.JMeterPluginsUtils;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.config.gui.ArgumentsPanel;
import org.apache.jmeter.samplers.gui.AbstractSamplerGui;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.testelement.property.JMeterProperty;
import org.apache.jmeter.testelement.property.NullProperty;
import org.apache.jmeter.util.JMeterUtils;

import javax.swing.*;
import java.awt.*;

/**
 *
 */
public class ParameterizedSamplerGui extends AbstractSamplerGui {
    public static final String WIKIPAGE = "ParameterizedController";
    private ArgumentsPanel argsPanel;

    public ParameterizedSamplerGui() {
        init();
    }

    public TestElement createTestElement() {
        ParameterizedSampler tc = new ParameterizedSampler();
        modifyTestElement(tc);
        tc.setComment(JMeterPluginsUtils.getWikiLinkText(WIKIPAGE));
        return tc;
    }

    public void modifyTestElement(TestElement te) {
        super.configureTestElement(te);
        if (te instanceof ParameterizedSampler) {
            ParameterizedSampler controller = (ParameterizedSampler) te;
            controller.setUserDefinedVariables((Arguments) argsPanel.createTestElement());
        }
    }

    public String getLabelResource() {
        return getClass().getName();
    }

    @Override
    public String getStaticLabel() {
        return JMeterPluginsUtils.prefixLabel("Parameterized Sampler");
    }

    private void init() {
        setLayout(new BorderLayout());
        setBorder(makeBorder());
        add(JMeterPluginsUtils.addHelpLinkToPanel(makeTitlePanel(), WIKIPAGE), BorderLayout.NORTH);

        add(createVariablePanel(), BorderLayout.CENTER);
    }

    private JPanel createVariablePanel() {
        argsPanel = new ArgumentsPanel(JMeterUtils.getResString("user_defined_variables"), null, true, true);
        return argsPanel;
    }

    @Override
    public void clearGui() {
        super.clearGui();
        argsPanel.clear();
    }

    @Override
    public void configure(TestElement te) {
        super.configure(te);
        ParameterizedSampler controller = (ParameterizedSampler) te;
        final JMeterProperty udv = controller.getUserDefinedVariablesAsProperty();
        if (udv != null && !(udv instanceof NullProperty)) {
            argsPanel.configure((Arguments) udv.getObjectValue());
        }
    }
}
