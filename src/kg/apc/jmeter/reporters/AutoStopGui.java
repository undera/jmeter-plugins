package kg.apc.jmeter.reporters;

import java.awt.BorderLayout;
import javax.swing.JPanel;
import kg.apc.jmeter.JMeterPluginsUtils;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.visualizers.gui.AbstractListenerGui;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/**
 *
 * @author undera
 */
public class AutoStopGui extends AbstractListenerGui {

    private static final Logger log = LoggingManager.getLoggerForClass();
    public static final String WIKIPAGE = "AutoStop";
    private JAutoStopPanel autoStopPanel;

    public AutoStopGui() {
        super();
        init();
        autoStopPanel.initFields();
    }

    @Override
    public String getStaticLabel() {
        return JMeterPluginsUtils.prefixLabel("AutoStop Listener");
    }

    @Override
    public String getLabelResource() {
        return getClass().getCanonicalName();
    }

    @Override
    public TestElement createTestElement() {
        TestElement te = new AutoStop();
        modifyTestElement(te);
        te.setComment(JMeterPluginsUtils.getWikiLinkText(WIKIPAGE));
        return te;
    }

    @Override
    public void modifyTestElement(TestElement te) {
        super.configureTestElement(te);
        if (te instanceof AutoStop) {
            AutoStop fw = (AutoStop) te;
            autoStopPanel.modifyTestElement(fw);
        }
    }

    @Override
    public void clearGui() {
        super.clearGui();
        autoStopPanel.initFields();
    }

    @Override
    public void configure(TestElement element) {
        super.configure(element);
        if (element instanceof AutoStop) {
            AutoStop fw = (AutoStop) element;
            autoStopPanel.configure(fw);
        }
    }

    private void init() {
        autoStopPanel = new JAutoStopPanel();
        setLayout(new BorderLayout(0, 5));
        setBorder(makeBorder());

        add(JMeterPluginsUtils.addHelpLinkToPanel(makeTitlePanel(), WIKIPAGE), BorderLayout.NORTH);

        JPanel container = new JPanel(new BorderLayout());
        container.add(autoStopPanel, BorderLayout.NORTH);
        add(container, BorderLayout.CENTER);
    }
}
