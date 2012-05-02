package kg.apc.jmeter.modifiers;

import java.awt.BorderLayout;
import kg.apc.jmeter.JMeterPluginsUtils;
import org.apache.jmeter.processor.gui.AbstractPreProcessorGui;
import org.apache.jmeter.testelement.TestElement;

public class AnchorModifierGui extends AbstractPreProcessorGui {

    private static final String WIKIPAGE = "SpiderPreProcessor";

    public AnchorModifierGui() {
        super();
        init();
    }

    @Override
    public String getStaticLabel() {
        return JMeterPluginsUtils.prefixLabel("Spider PreProcessor");
    }

    @Override
    public String getLabelResource() {
        return this.getClass().getName();
    }

    @Override
    public TestElement createTestElement() {
        AnchorModifier modifier = new AnchorModifier();
        modifyTestElement(modifier);
        return modifier;
    }

    /**
     * Modifies a given TestElement to mirror the data in the gui components.
     *
     * @see
     * org.apache.jmeter.gui.JMeterGUIComponent#modifyTestElement(TestElement)
     */
    @Override
    public void modifyTestElement(TestElement modifier) {
        configureTestElement(modifier);
    }

    private void init() {
        setLayout(new BorderLayout(0, 5));
        setBorder(makeBorder());

        add(JMeterPluginsUtils.addHelpLinkToPanel(makeTitlePanel(), WIKIPAGE), BorderLayout.NORTH);
    }
}
