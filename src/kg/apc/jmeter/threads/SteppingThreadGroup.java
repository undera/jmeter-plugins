package kg.apc.jmeter.threads;

import org.apache.jmeter.threads.AbstractThreadGroup;
import org.apache.jmeter.threads.JMeterThread;

public class SteppingThreadGroup
     extends AbstractThreadGroup
{
   public SteppingThreadGroup()
   {
      super();
   }

   /**
    * This will schedule the time for the JMeterThread.
    *
    * @param thread
    * @param group
    */
   public void scheduleThread(JMeterThread thread)
   {
      int threadGroupDelay = getThreadGroupDelay();

      int inUserPeriod = getInUserPeriod();
      int inUserCount = getInUserCount();

      int flightTime = getFlightTime();

      int outUserPeriod = getOutUserPeriod();
      int outUserCount = getOutUserCount();

      long ascentPoint = System.currentTimeMillis() + 1000 * threadGroupDelay;
      long descentPoint = ascentPoint + 1000 * inUserPeriod * (int) Math.floor((double) getNumThreads() / inUserCount) + 1000 * flightTime;

      thread.setStartTime(ascentPoint + 1000 * inUserPeriod * (int) Math.floor((double) thread.getThreadNum() / inUserCount));
      thread.setEndTime(descentPoint + 1000 * outUserPeriod * (int) Math.floor((double) thread.getThreadNum() / outUserCount));
      thread.setScheduled(true);
   }

   private int getThreadGroupDelay()
   {
      throw new UnsupportedOperationException("Not yet implemented");
   }

   private int getInUserPeriod()
   {
      throw new UnsupportedOperationException("Not yet implemented");
   }

   private int getInUserCount()
   {
      throw new UnsupportedOperationException("Not yet implemented");
   }

   private int getFlightTime()
   {
      throw new UnsupportedOperationException("Not yet implemented");
   }

   private int getOutUserPeriod()
   {
      throw new UnsupportedOperationException("Not yet implemented");
   }

   private int getOutUserCount()
   {
      throw new UnsupportedOperationException("Not yet implemented");
   }
}
