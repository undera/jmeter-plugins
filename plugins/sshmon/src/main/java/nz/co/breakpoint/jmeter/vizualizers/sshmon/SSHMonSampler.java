package nz.co.breakpoint.jmeter.vizualizers.sshmon;

import java.io.InterruptedIOException;

import kg.apc.jmeter.vizualizers.MonitoringSampler;
import kg.apc.jmeter.vizualizers.MonitoringSampleGenerator;

import org.apache.commons.io.IOUtils;
import org.apache.commons.pool2.KeyedObjectPool;
import org.apache.commons.pool2.impl.GenericKeyedObjectPool;
import org.apache.commons.pool2.impl.GenericKeyedObjectPoolConfig;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

/**
 * Monitoring sampler that collects the numeric output of a remote command 
 * executed over a pooled SSH session.
 */
public class SSHMonSampler
        implements MonitoringSampler {

    private static final Logger log = LoggingManager.getLoggerForClass();
    private String metricName;
    private ConnectionDetails connectionDetails;
    private String remoteCommand;
    private boolean sampleDeltaValue = true;
    private double oldValue = Double.NaN;

    /**
     * Manage ssh connections and share existing connections
     */
    private static KeyedObjectPool<ConnectionDetails, Session> pool;
    static {
        GenericKeyedObjectPoolConfig config = new GenericKeyedObjectPoolConfig();
        config.setMinIdlePerKey(1);
        log.debug("Creating GenericKeyedObjectPool");
        pool = new GenericKeyedObjectPool<ConnectionDetails, Session>(new SSHSessionFactory(), config);
    }

    public SSHMonSampler(String name, ConnectionDetails connectionDetails, String remoteCommand, boolean sampleDeltaValue) {
        this.metricName = name;
        this.connectionDetails = connectionDetails;
        this.remoteCommand = remoteCommand;
        this.sampleDeltaValue = sampleDeltaValue;
    }

    @Override
    public void generateSamples(MonitoringSampleGenerator collector) {
        Session session = null;
        ChannelExec channel = null;

        try {
            log.debug("Borrowing session for "+connectionDetails);
            session = pool.borrowObject(connectionDetails);

            channel = (ChannelExec)session.openChannel("exec");
            channel.setCommand(remoteCommand);
            channel.setPty(true);
            channel.connect();
            
            final double val = Double.valueOf(IOUtils.toString(channel.getInputStream()).trim());

            if (sampleDeltaValue) {
                if (!Double.isNaN(oldValue)) {
                    collector.generateSample(val - oldValue, metricName);
                }
                oldValue = val;
            } else {
                collector.generateSample(val, metricName);
            }
        }
        catch (InterruptedIOException ex) { // stopping the test has caused a thread interrupt
            log.info(ex.toString());
        } 
        catch (JSchException ex) { // stopping the test has closed the session
            log.info(ex.toString());
        }
        catch (Exception ex) { 
            log.error(ex.toString());
        }
        finally {
            if (channel != null && channel.isConnected()) {
                channel.disconnect();
            }
            if (session != null) {
                try {
                    if (session.isConnected()) {
                        log.debug("Returning session for "+connectionDetails);
                        pool.returnObject(connectionDetails, session);
                    }
                    else {
                        log.debug("Invalidating session for "+connectionDetails);
                        pool.invalidateObject(connectionDetails, session);
                    }
                }
                catch (Exception ex) {
                    log.warn(ex.getMessage());
                }
            }
        }
    }

	public String getMetricName() {
		return metricName;
	}

	public void setMetricName(String metricName) {
		this.metricName = metricName;
	}

    public ConnectionDetails getConnectionDetails() {
        return connectionDetails;
    }
    
    public void setConnectionDetails(ConnectionDetails connectionDetails) {
        this.connectionDetails = connectionDetails;
    }
    
    public String getRemoteCommand() {
        return remoteCommand;
    }
    
    public void setRemoteCommand(String remoteCommand) {
        this.remoteCommand = remoteCommand;
    }
    
	public boolean isSampleDeltaValue() {
		return sampleDeltaValue;
	}

	public void setSampleDeltaValue(boolean sampleDeltaValue) {
		this.sampleDeltaValue = sampleDeltaValue;
	}

	public double getOldValue() {
		return oldValue;
	}

	public void setOldValue(double oldValue) {
		this.oldValue = oldValue;
	}
}
