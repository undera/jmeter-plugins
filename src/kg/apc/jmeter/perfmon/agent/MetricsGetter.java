package kg.apc.jmeter.perfmon.agent;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hyperic.sigar.FileSystem;

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

    /*
     * contains the list of drives for I/O metrics
     */
    private FileSystem[] fileSystems = null;
    /**
     * The constructor which instanciates the Sigar service
     */
    private MetricsGetter()
    {
        try
        {
            hostName = InetAddress.getLocalHost().getHostName();
            sigarProxy = SigarProxyCache.newInstance(new Sigar(), 500);
            initFileSystems();
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
     * Get list of FileSystems for I/O monitoring
     */
    public final void initFileSystems() {
        try
        {
            ArrayList<FileSystem> tmp = new ArrayList<FileSystem>();
            FileSystem[] fs = sigarProxy.getFileSystemList();
            for (int i = 0; i < fs.length; i++)
            {
                FileSystem fileSystem = fs[i];
                if(fileSystem.getType() == FileSystem.TYPE_LOCAL_DISK) {
                    tmp.add(fileSystem);
                }
            }

            fileSystems = new FileSystem[tmp.size()];

            for (int i = 0; i < tmp.size(); i++)
            {
                fileSystems[i] = tmp.get(i);
            }

        } catch (SigarException ex)
        {
            fileSystems = new FileSystem[0];
            ServerAgent.logMessage("Error while getting disks: " + ex.getMessage());
        }
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
            return -2;
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
            return -2;
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
            ret[0] = -2;
            ret[1] = -2;
        }

        return ret;

    }

    /**
     * Get the current swap usage in number of pages in and number of pages out
     * @return [page in][page out]
     */
    private long[] getDisksIO() {
        long[] ret = {0L, 0L};
        try
        {
            for (int i = 0; i < fileSystems.length; i++)
            {
                //if sigar failed to get metrics (without exception)
                long reads = sigarProxy.getFileSystemUsage(fileSystems[i].getDevName()).getDiskReads();
                long writes = sigarProxy.getFileSystemUsage(fileSystems[i].getDevName()).getDiskWrites();

                if(reads == -1L || writes == -1L) {
                    long[] sigarFailure = {-1L, -1L};
                    return sigarFailure;
                } else {
                    ret[0] = ret[0] + reads;
                    ret[1] = ret[1] + writes;
                }
            }
        } catch (SigarException e)
        {
            ServerAgent.logMessage(e.getMessage());
            ret[0] = -2;
            ret[1] = -2;
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

        } else if (value.equals("dio"))
        {
            long[] values = getDisksIO();
            buff.append(values[0]);
            buff.append(":");
            buff.append(values[1]);

        }else if (value.equals("name"))
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
