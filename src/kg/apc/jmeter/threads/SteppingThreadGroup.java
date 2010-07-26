// TODO: сделать плагин с гридовым указанием юзеров
package kg.apc.jmeter.threads;

import org.apache.jmeter.threads.AbstractThreadGroup;
import org.apache.jmeter.threads.JMeterThread;
import org.apache.jmeter.util.JMeterUtils;

/**
 *
 * @author apc
 */
public class SteppingThreadGroup
      extends AbstractThreadGroup
{
   /**
    *
    */
   public static final String THREAD_GROUP_DELAY = "Threads initial delay";
   /**
    *
    */
   public static final String INC_USER_PERIOD = "Start users period";
   /**
    *
    */
   public static final String INC_USER_COUNT = "Start users count";
   /**
    *
    */
   public static final String DEC_USER_PERIOD = "Stop users period";
   /**
    *
    */
   public static final String DEC_USER_COUNT = "Stop users count";
   /**
    *
    */
   public static final String FLIGHT_TIME = "flighttime";

   /**
    *
    */
   public SteppingThreadGroup()
   {
      super();
   }

   /**
    *
    * @param thread
    */
   public void scheduleThread(JMeterThread thread)
   {
      int additionalRampUp = 0;
      String rampupProperty = JMeterUtils.getProperty("steppingthreadgroup.additionalrampup");
      if (rampupProperty != null)
      {
         additionalRampUp = Integer.parseInt(rampupProperty);
      }

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
      thread.setScheduled(true);
   }

   /**
    *
    * @return
    */
   public int getThreadGroupDelay()
   {
      return getPropertyAsInt(THREAD_GROUP_DELAY, 0);
   }

   /**
    *
    * @return
    */
   public int getInUserPeriod()
   {
      return getPropertyAsInt(INC_USER_PERIOD, 0);
   }

   /**
    *
    * @return
    */
   public int getInUserCount()
   {
      return getPropertyAsInt(INC_USER_COUNT, 1);
   }

   /**
    *
    * @return
    */
   public int getFlightTime()
   {
      return getPropertyAsInt(FLIGHT_TIME, 0);
   }

   /**
    *
    * @return
    */
   public int getOutUserPeriod()
   {
      return getPropertyAsInt(DEC_USER_PERIOD, 0);
   }

   /**
    *
    * @return
    */
   public int getOutUserCount()
   {
      return getPropertyAsInt(DEC_USER_COUNT, 1);
   }
}
