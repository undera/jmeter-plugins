// TODO: resolve scrolling issue here and in all other samplers
package kg.apc.jmeter.samplers;

import kg.apc.jmeter.JMeterPluginsUtils;
import kg.apc.jmeter.dummy.DummyPanel;
import org.apache.jmeter.samplers.gui.AbstractSamplerGui;
import org.apache.jmeter.testelement.TestElement;

import java.awt.*;

public class DummySamplerGui extends AbstractSamplerGui {
    public static final String WIKIPAGE = "DummySampler";
    private final DummyPanel dummyPanel;

    public DummySamplerGui() {
        setLayout(new BorderLayout(0, 5));
        setBorder(makeBorder());

        add(JMeterPluginsUtils.addHelpLinkToPanel(makeTitlePanel(), WIKIPAGE), BorderLayout.NORTH);

        this.dummyPanel = new DummyPanel();
        add(dummyPanel.init(), BorderLayout.CENTER);
        dummyPanel.initFields();
    }

    @Override
    public String getStaticLabel() {
        return JMeterPluginsUtils.prefixLabel("Dummy Sampler");
    }

    @Override
    public String getLabelResource() {
        return this.getClass().getSimpleName();
    }

    @Override
    public TestElement createTestElement() {
        DummySampler sampler = new DummySampler();
        modifyTestElement(sampler);
        sampler.setComment(JMeterPluginsUtils.getWikiLinkText(WIKIPAGE));
        return sampler;
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
