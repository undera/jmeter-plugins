package kg.apc.jmeter.perfmon.agent;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.SigarProxy;
import org.hyperic.sigar.SigarProxyCache;

/**
 * This class will do the Sigar calls. It is a singleton and will be shared
 * with all ConnectionThreads.
 * This class is based on Sigar API: http://www.hyperic.com/products/sigar
 * @author Stephane Hoblingre
 */
public class MetricsGetter
{
    /*
     * The unic instance
     */
    private static MetricsGetter instance = new MetricsGetter();
    /*
     * The hostName where the server is run (retrieved by Sigar)
     */
    private String hostName = null;
    /*
     * We need to use the Sigar proxy to ensure high AgentConnector demands can be handled
     */
    private SigarProxy sigarProxy = null;

    /**
     * The constructor which instanciates the Sigar service
     */
    private MetricsGetter()
    {
        try
        {
            hostName = InetAddress.getLocalHost().getHostName();
            sigarProxy = SigarProxyCache.newInstance(new Sigar(), 500);
        } catch (UnknownHostException e)
        {
            ServerAgent.logMessage(e.getMessage());
        }
    }

    /**
     * The only way to retrieve the object
     * @return the MetricsGetter instance
     */
    public static MetricsGetter getInstance()
    {
        return instance;
    }

    /**
     * Get the current cpu load in percent
     * @return the cpu load, or -1 if a problem occurred
     */
    private double getCpuUsage()
    {
        try
        {
            return sigarProxy.getCpuPerc().getCombined();
        } catch (SigarException e)
        {
            ServerAgent.logMessage(e.getMessage());
            return -1;
        }
    }

    /**
     * Get the current memory usage in bytes
     * @return the memory size or -1 if a problem occurred
     */
    private long getUsedMem()
    {
        try
        {
            return sigarProxy.getMem().getUsed();
        } catch (SigarException e)
        {
            ServerAgent.logMessage(e.getMessage());
            return -1;
        }
    }
/**
     * Get the current swap usage in number of pages in and number of pages out
     * @return [page in][page out]
     */
    private long[] getSwap() {
        long[] ret = new long[2];
        try
        {
            ret[0] = sigarProxy.getSwap().getPageIn();
            ret[1] = sigarProxy.getSwap().getPageOut();
        } catch (SigarException e)
        {
            ServerAgent.logMessage(e.getMessage());
            ret[0] = -1;
            ret[1] = -1;
        }

        return ret;

    }

    /**
     * Get the server name
     * @return the server name retrieved by Sigar
     */
    private String getServerName()
    {
        return hostName;
    }

    /**
     * The main method to get metrics from Sigar
     * @param value the command to retrieve one metric
     * @return a String representing the value or badCmd if the command is unknown
     */
    public String getValues(String value)
    {

        StringBuffer buff = new StringBuffer();

        if (value.equals("cpu"))
        {
            buff.append(getCpuUsage());
        } else if (value.equals("mem"))
        {
            buff.append(getUsedMem());
        } else if (value.equals("swp"))
        {
            long[] values = getSwap();
            buff.append(values[0]);
            buff.append(":");
            buff.append(values[1]);

        } else if (value.equals("name"))
        {
            buff.append(getServerName());
        } else
        {
            buff.append("badCmd:");
            buff.append(value);
        }
        return buff.toString();
    }
}
