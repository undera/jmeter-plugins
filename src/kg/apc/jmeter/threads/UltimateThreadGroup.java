package kg.apc.jmeter.threads;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.jmeter.gui.util.PowerTableModel;
import org.apache.jmeter.testelement.property.CollectionProperty;
import org.apache.jmeter.testelement.property.JMeterProperty;
import org.apache.jmeter.testelement.property.NullProperty;
import org.apache.jmeter.testelement.property.StringProperty;
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
   private JMeterThread currentThread;

   public UltimateThreadGroup()
   {
      super();
   }

   public static CollectionProperty tableModelToCollectionProperty(PowerTableModel model)
   {
      CollectionProperty rows = new CollectionProperty(UltimateThreadGroup.DATA_PROPERTY, new ArrayList<Object>());
      for (int col = 0; col < model.getColumnCount(); col++)
      {
         rows.addItem(model.getColumnData(model.getColumnName(col)));
      }
      return rows;
   }

   // FIXME: too inefficient
   public void scheduleThread(JMeterThread thread)
   {
      currentThread = thread;

      int threadGroupDelay = getThreadGroupDelay();
      int userCount = getUserCount();
      int inUserPeriod = getInUserPeriod();
      int flightTime = getFlightTime();
      long ascentPoint = System.currentTimeMillis() + 1000 * threadGroupDelay;
      long descentPoint = ascentPoint + 1000 * inUserPeriod * (int) Math.floor((double) getNumThreads() / userCount) + 1000 * flightTime;

      thread.setStartTime(ascentPoint + 1000 * inUserPeriod * (int) Math.floor((double) thread.getThreadNum() / userCount));
      thread.setEndTime(descentPoint);

      thread.setScheduled(true);
   }

   public JMeterProperty getData()
   {
      //log.info("getData");
      JMeterProperty prop = getProperty(DATA_PROPERTY);
      return prop;
   }

   void setData(CollectionProperty rows)
   {
      //log.info("setData");
      setProperty(rows);
   }

   @Override
   public int getNumThreads()
   {
      int result = 0;

      JMeterProperty threadValues = getData();
      if (!(threadValues instanceof NullProperty))
      {
         CollectionProperty columns = (CollectionProperty) threadValues;
         List<?> col = (List<?>) columns.get(0).getObjectValue();
         Iterator<?> iter = col.iterator();
         while (iter.hasNext())
         {
            StringProperty prop = (StringProperty) iter.next();
            result += prop.getIntValue();
         }
      }

      return result;
   }

   private int getCell(int col)
   {
      JMeterProperty threadValues = getData();
      if (threadValues instanceof NullProperty)
      {
         throw new IllegalArgumentException("Received null property instead of collection");
      }

      CollectionProperty columns = (CollectionProperty) threadValues;
      List<?> column = (List<?>) columns.get(col).getObjectValue();

      Iterator it = ((List<?>) columns.get(0).getObjectValue()).iterator();
      int row = 0;
      int threadNo = currentThread.getThreadNum();
      while (it.hasNext())
      {
         int threads = ((StringProperty) it.next()).getIntValue();

         if (threads > threadNo)
         {
            JMeterProperty cell = (JMeterProperty) column.get(row);
            return cell.getIntValue() < 0 ? 0 : cell.getIntValue();
         }

         threadNo -= threads;
         row++;
      }

      throw new IllegalArgumentException("Thread record not found for col " + col);
   }

   private int getUserCount()
   {
      return getCell(0);
   }

   private int getThreadGroupDelay()
   {
      return getCell(1);
   }

   private int getInUserPeriod()
   {
      return getCell(2);
   }

   private int getFlightTime()
   {
      return getCell(3);
   }
}
