package kg.apc.jmeter.modifiers;

import kg.apc.jmeter.JMeterPluginsUtils;
import kg.apc.jmeter.dummy.DummyPanel;
import org.apache.jmeter.processor.gui.AbstractPostProcessorGui;
import org.apache.jmeter.testelement.TestElement;

import java.awt.*;

public class DummySubPostProcessorGui extends AbstractPostProcessorGui {
    public static final String WIKIPAGE = "DummySampler";
    private final DummyPanel dummyPanel;

    public DummySubPostProcessorGui() {
        setLayout(new BorderLayout(0, 5));
        setBorder(makeBorder());

        add(JMeterPluginsUtils.addHelpLinkToPanel(makeTitlePanel(), WIKIPAGE), BorderLayout.NORTH);

        this.dummyPanel = new DummyPanel();
        add(dummyPanel.init(), BorderLayout.NORTH);
        dummyPanel.initFields();
    }

    @Override
    public String getStaticLabel() {
        return JMeterPluginsUtils.prefixLabel("Add Dummy Subresult");
    }

    @Override
    public String getLabelResource() {
        return this.getClass().getSimpleName();
    }

    @Override
    public TestElement createTestElement() {
        DummySubPostProcessor te = new DummySubPostProcessor();
        modifyTestElement(te);
        te.setComment(JMeterPluginsUtils.getWikiLinkText(WIKIPAGE));
        return te;
    }

    @Override
    public void configure(TestElement element) {
        super.configure(element);
        dummyPanel.configure(element);
    }

    @Override
    public void modifyTestElement(TestElement sampler) {
        super.configureTestElement(sampler);
        dummyPanel.modifyTestElement(sampler);
    }

    @Override
    public void clearGui() {
        super.clearGui();
        dummyPanel.initFields();
    }
}
