package kg.apc.jmeter.samplers;

import java.awt.BorderLayout;
import org.apache.jmeter.gui.util.VerticalPanel;
import org.apache.jmeter.samplers.gui.AbstractSamplerGui;
import org.apache.jmeter.testelement.TestElement;

public class DummySamplerGui
      extends AbstractSamplerGui
{
    private TCPConfigGui TcpDefaultPanel;

    public DummySamplerGui() {
        init();
    }

    public void configure(TestElement element) {
        super.configure(element);
        TcpDefaultPanel.configure(element);
    }

    public TestElement createTestElement() {
        DummySampler sampler = new DummySampler();
        modifyTestElement(sampler);
        return sampler;
    }

    /**
     * Modifies a given TestElement to mirror the data in the gui components.
     *
     * @see org.apache.jmeter.gui.JMeterGUIComponent#modifyTestElement(TestElement)
     */
    public void modifyTestElement(TestElement sampler) {
        sampler.clear();
        sampler.addTestElement(TcpDefaultPanel.createTestElement());
        this.configureTestElement(sampler);
    }

    /**
     * Implements JMeterGUIComponent.clearGui
     */
    public void clearGui() {
        super.clearGui();

        TcpDefaultPanel.clearGui();
    }

    public String getLabelResource() {
        return "tcp_sample_title"; // $NON-NLS-1$
    }

    private void init() {
        setLayout(new BorderLayout(0, 5));
        setBorder(makeBorder());

        add(makeTitlePanel(), BorderLayout.NORTH);

        VerticalPanel mainPanel = new VerticalPanel();

        TcpDefaultPanel = new TCPConfigGui(false);
        mainPanel.add(TcpDefaultPanel);

        add(mainPanel, BorderLayout.CENTER);
    }
}
