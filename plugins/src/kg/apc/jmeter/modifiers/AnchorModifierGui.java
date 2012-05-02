package kg.apc.jmeter.modifiers;

import java.awt.BorderLayout;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import kg.apc.jmeter.JMeterPluginsUtils;
import org.apache.jmeter.processor.gui.AbstractPreProcessorGui;
import org.apache.jmeter.testelement.TestElement;

public class AnchorModifierGui extends AbstractPreProcessorGui {

    private static final String WIKIPAGE = "SpiderPreProcessor";

    public AnchorModifierGui() {
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

        JTextArea info = new JTextArea();
        info.setEditable(false);
        info.setWrapStyleWord(true);
        info.setOpaque(false);
        info.setLineWrap(true);
        info.setColumns(20);

        JScrollPane jScrollPane1 = new javax.swing.JScrollPane();
        jScrollPane1.setViewportView(info);
        jScrollPane1.setBorder(null);

        info.setText("This item will create a HTTP request to all links found in a page.");

        add(jScrollPane1, BorderLayout.CENTER);
    }
}
