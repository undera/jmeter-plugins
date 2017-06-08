package nz.co.breakpoint.jmeter.vizualizers.sshmon;

import java.util.ArrayList;

import kg.apc.jmeter.vizualizers.MonitoringResultsCollector;

import org.apache.jmeter.testelement.property.CollectionProperty;
import org.apache.jmeter.testelement.property.JMeterProperty;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.commons.io.FileUtils;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/**
 * Class that collects SSHMonSampler sample results.
 * Acts as a link between the SSHMonGui and the actual samplers.
 * Implementation of the abstract MonitoringResultsCollector.
 */
public class SSHMonCollector
        extends MonitoringResultsCollector {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggingManager.getLoggerForClass();

    @Override
    protected String getPrefix() { return "SSHMon"; }

    @Override
    protected String getForceFilePropertyName() { return "forceSSHMonFile"; }

    @Override
    protected int getInterval() {
        return JMeterUtils.getPropDefault("jmeterPlugin.sshmon.interval", 1000);
    }

    @Override
    protected void initiateConnectors() {
        samplers.clear();
        CollectionProperty rows = getSamplerSettings();

        for (int i = 0; i < rows.size(); i++) {
            ArrayList<Object> row = (ArrayList<Object>) rows.get(i).getObjectValue();
            String  label      = ((JMeterProperty)row.get(0)).getStringValue();
            String  host       = ((JMeterProperty)row.get(1)).getStringValue();
            int     port       = ((JMeterProperty)row.get(2)).getIntValue();
            String  username   = ((JMeterProperty)row.get(3)).getStringValue();
            String  privateKey = ((JMeterProperty)row.get(4)).getStringValue();
            String  password   = ((JMeterProperty)row.get(5)).getStringValue();
            String  command    = ((JMeterProperty)row.get(6)).getStringValue();
            boolean isDelta    = ((JMeterProperty)row.get(7)).getBooleanValue();

            ConnectionDetails connectionDetails = new ConnectionDetails(username, host, port, password, 
                privateKey.isEmpty()? null: privateKey.getBytes());
            samplers.add(new SSHMonSampler(label, connectionDetails, command, isDelta));
        }
    }
}
