package kg.apc.jmeter.perfmon.agent;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import org.hyperic.sigar.FileSystem;
import org.hyperic.sigar.FileSystemUsage;
import org.hyperic.sigar.NetInterfaceStat;

import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.SigarProxy;
import org.hyperic.sigar.SigarProxyCache;
import org.hyperic.sigar.Swap;

/**
 * This class will do the Sigar calls. It is a singleton and will be shared
 * with all ConnectionThreads.
 * This class is based on Sigar API: http://www.hyperic.com/products/sigar
 * @author Stephane Hoblingre
 */
public class MetricsGetter
{
   //FIXME: APC: why don't use custom exceptions instead of error constants?
   public final static long SIGAR_ERROR = -1L;
   public final static long AGENT_ERROR = -2L;
   public final static long[] SIGAR_ERROR_ARRAY =
   {
      -1L, -1L
   };
   public final static long[] AGENT_ERROR_ARRAY =
   {
      -2L, -2L
   };

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
   private String[] networkInterfaces = null;

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
         initNetworkInterfaces();
      }
      catch (UnknownHostException e)
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

   public final void initNetworkInterfaces()
   {
      try
      {
         networkInterfaces = sigarProxy.getNetInterfaceList();
      }
      catch (SigarException ex)
      {
         networkInterfaces = new String[0];
         ServerAgent.logMessage("Error while getting network interfaces: " + ex.getMessage());
      }
   }

   /**
    * Get list of FileSystems for I/O monitoring
    */
   public final void initFileSystems()
   {
      try
      {
         ArrayList tmp = new ArrayList();
         FileSystem[] fs = sigarProxy.getFileSystemList();
         for (int i = 0; i < fs.length; i++)
         {
            FileSystem fileSystem = fs[i];
            if (fileSystem.getType() == FileSystem.TYPE_LOCAL_DISK)
            {
               tmp.add(fileSystem);
            }
         }

         fileSystems = new FileSystem[tmp.size()];

         for (int i = 0; i < tmp.size(); i++)
         {
            fileSystems[i] = (FileSystem) tmp.get(i);
         }

      }
      catch (SigarException ex)
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
      }
      catch (SigarException e)
      {
         ServerAgent.logMessage(e.getMessage());
         return MetricsGetter.AGENT_ERROR;
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
      }
      catch (SigarException e)
      {
         ServerAgent.logMessage(e.getMessage());
         return MetricsGetter.AGENT_ERROR;
      }
   }

   /**
    * Get the current swap usage in number of pages in and number of pages out
    * @return [page in][page out]
    */
   private long[] getSwap()
   {
      long[] ret = new long[2];
      try
      {
         Swap metrics = sigarProxy.getSwap();
         ret[0] = metrics.getPageIn();
         ret[1] = metrics.getPageOut();
      }
      catch (SigarException e)
      {
         ServerAgent.logMessage(e.getMessage());
         ret = MetricsGetter.AGENT_ERROR_ARRAY;
      }

      return ret;

   }

   private long[] getNetIO()
   {

      if (networkInterfaces.length == 0)
      {
         return MetricsGetter.SIGAR_ERROR_ARRAY;
      }

      long[] ret =
      {
         0L, 0L
      };

      try
      {
         for (int i = 0; i < networkInterfaces.length; i++)
         {
            NetInterfaceStat metrics = sigarProxy.getNetInterfaceStat(networkInterfaces[i]);
            long rxBytes = metrics.getRxBytes();
            long txBytes = metrics.getTxBytes();
            if (rxBytes != -1 && txBytes != -1)
            {
               ret[0] = ret[0] + rxBytes;
               ret[1] = ret[1] + txBytes;
            }
         }
      }
      catch (SigarException ex)
      {
         ret = MetricsGetter.AGENT_ERROR_ARRAY;
      }
      return ret;
   }

   /**
    * Get the current swap usage in number of pages in and number of pages out
    * @return [page in][page out]
    */
   private long[] getDisksIO()
   {
      if (fileSystems.length == 0)
      {
         return MetricsGetter.SIGAR_ERROR_ARRAY;
      }

      long[] ret =
      {
         0L, 0L
      };
      try
      {
         for (int i = 0; i < fileSystems.length; i++)
         {

            //if sigar failed to get metrics (without exception)
            FileSystemUsage metrics = sigarProxy.getFileSystemUsage(fileSystems[i].getDevName());
            long reads = metrics.getDiskReads();
            long writes = metrics.getDiskWrites();
            if (reads == -1L || writes == -1L)
            {
               return MetricsGetter.SIGAR_ERROR_ARRAY;
            }
            else
            {
               ret[0] = ret[0] + reads;
               ret[1] = ret[1] + writes;
            }
         }
      }
      catch (SigarException ex)
      {
         ret = MetricsGetter.AGENT_ERROR_ARRAY;
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
      }
      else if (value.equals("mem"))
      {
         buff.append(getUsedMem());
      }
      else if (value.equals("swp"))
      {
         long[] values = getSwap();
         buff.append(values[0]);
         buff.append(":");
         buff.append(values[1]);

      }
      else if (value.equals("dio"))
      {
         long[] values = getDisksIO();
         buff.append(values[0]);
         buff.append(":");
         buff.append(values[1]);

      }
      else if (value.equals("nio"))
      {
         long[] values = getNetIO();
         buff.append(values[0]);
         buff.append(":");
         buff.append(values[1]);

      }
      else if (value.equals("name"))
      {
         buff.append(getServerName());
      }
      else
      {
         buff.append("badCmd");
      }
      return buff.toString();
   }
}
