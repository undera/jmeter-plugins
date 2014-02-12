package kg.apc.jmeter.config;

import kg.apc.jmeter.JMeterPluginsUtils;
import org.apache.jmeter.config.gui.AbstractConfigGui;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class DistributedTestControlGui extends AbstractConfigGui {

    public static final String WIKIPAGE = "DistributedTestControl";
    public static Logger log = LoggingManager.getLoggerForClass();
    private ServersListPanel serversPanel;

    public DistributedTestControlGui() {
        super();
        init();
        initFields();
    }

    //do not insert this vizualiser in any JMeter menu
    @Override
    public Collection<String> getMenuCategories() {
        return new ArrayList<String>();
    }

    @Override
    public String getStaticLabel() {
        return JMeterPluginsUtils.prefixLabel("Distributed Test Control");
    }

    @Override
    public String getLabelResource() {
        return getClass().getCanonicalName();
    }

    @Override
    public void configure(TestElement te) {
        super.configure(te);
        // fill controls
        serversPanel.loadFromTestElement((DistributedTestControl) te);
    }

    @Override
    public TestElement createTestElement() {
        String srv_list = JMeterUtils.getPropDefault(DistributedTestControl.PROP_HOSTS, "127.0.0.1");
        ArrayList<String> data = new ArrayList<String>(Arrays.asList(srv_list.split(",")));

        for (String srv_name : data) {
            serversPanel.add(srv_name);
        }

        DistributedTestControl control = new DistributedTestControl();
        control.setData(data);
        modifyTestElement(control);
        return control;
    }

    @Override
    public void modifyTestElement(TestElement te) {
        configureTestElement(te);
        // fill props
        serversPanel.saveToTestElement((DistributedTestControl) te);
    }

    @Override
    public void clearGui() {
        super.clearGui();
        initFields();
    }

    private void init() {
        setLayout(new BorderLayout(0, 5));
        setBorder(makeBorder());
        add(JMeterPluginsUtils.addHelpLinkToPanel(makeTitlePanel(), WIKIPAGE), BorderLayout.NORTH);

        serversPanel = new ServersListPanel();

        JPanel buttonPanel = new JPanel();
        JButton btnAdd = new JButton("Add Slave Server");
        btnAdd.addActionListener(new AddRemoteServerAction(serversPanel));
        buttonPanel.add(btnAdd);
        JButton btnStatus = new JButton("Get Status for All");
        //btnAdd.addActionListener(new AddRemoteServerAction(serversPanel));
        buttonPanel.add(btnStatus);

        JPanel container = new JPanel(new BorderLayout());
        container.add(buttonPanel, BorderLayout.NORTH);
        container.add(serversPanel, BorderLayout.CENTER);
        add(container, BorderLayout.CENTER);
    }

    private void initFields() {
        serversPanel.clear();
    }
}
