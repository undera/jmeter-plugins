// TODO: сделать плагин с гридовым указанием юзеров
package kg.apc.jmeter.threads;

import java.io.Serializable;
import org.apache.jmeter.testelement.property.CollectionProperty;
import org.apache.jmeter.testelement.property.JMeterProperty;
import org.apache.jmeter.threads.AbstractThreadGroup;
import org.apache.jmeter.threads.JMeterThread;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

public class UltimateThreadGroup
      extends AbstractThreadGroup
      implements Serializable
{
   private static final Logger log = LoggingManager.getLoggerForClass();
   public static final String DATA_PROPERTY = "ultimatethreadgroupdata";

   public UltimateThreadGroup()
   {
      super();
   }

   public void scheduleThread(JMeterThread thread)
   {
      /*
      int threadGroupDelay = getThreadGroupDelay();

      int inUserPeriod = getInUserPeriod();
      int inUserCount = getInUserCount();

      int flightTime = getFlightTime();

      int outUserPeriod = getOutUserPeriod();
      int outUserCount = getOutUserCount();

      long ascentPoint = System.currentTimeMillis() + 1000 * threadGroupDelay;
      long descentPoint = ascentPoint + 1000 * inUserPeriod * (int) Math.floor((double) getNumThreads() / inUserCount) + 1000 * flightTime;

      thread.setStartTime(ascentPoint + 1000 * inUserPeriod * (int) Math.floor((double) thread.getThreadNum() / inUserCount) + additionalRampUp * thread.getThreadNum());
      thread.setEndTime(descentPoint + 1000 * outUserPeriod * (int) Math.floor((double) thread.getThreadNum() / outUserCount));
       *
       */
      thread.setScheduled(true);
   }

   public JMeterProperty getData()
   {
      JMeterProperty prop = getProperty(DATA_PROPERTY);
      return prop;
   }

   void setData(CollectionProperty rows)
   {
      setProperty(rows);
   }

   @Override
   public int getNumThreads()
   {
      int result = 0;

      /*
      List data = model.getColumnData(model.getColumnName(0));
      Iterator it = data.iterator();
      while (it.hasNext())
      {
      result += (Integer) it.next();
      }
       * 
       */

      return result;
   }
}
