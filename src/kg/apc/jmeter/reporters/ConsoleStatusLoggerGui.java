package kg.apc.jmeter.reporters;

import java.awt.BorderLayout;
import kg.apc.jmeter.JMeterPluginsUtils;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.visualizers.gui.AbstractListenerGui;

/**
 *
 * @author undera
 */
public class ConsoleStatusLoggerGui extends AbstractListenerGui {

    public static final String WIKIPAGE = "ConsoleStatusLogger";

    public ConsoleStatusLoggerGui() {
        super();
        init();
    }

    @Override
    public String getStaticLabel() {
        return JMeterPluginsUtils.prefixLabel("Console Status Logger");
    }

    public String getLabelResource() {
        return getClass().getCanonicalName();
    }

    public TestElement createTestElement() {
        TestElement te = new ConsoleStatusLogger();
        modifyTestElement(te);
        te.setComment(JMeterPluginsUtils.getWikiLinkText(WIKIPAGE));
        return te;
    }

    public void modifyTestElement(TestElement te) {
        super.configureTestElement(te);
    }

    private void init() {
        setLayout(new BorderLayout(0, 5));
        setBorder(makeBorder());
        add(JMeterPluginsUtils.addHelpLinkToPanel(makeTitlePanel(), WIKIPAGE), BorderLayout.NORTH);
    }
}
