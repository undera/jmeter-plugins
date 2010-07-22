package kg.apc.jmeter.threads;

import java.util.Iterator;
import java.util.List;
import org.apache.jmeter.testelement.property.CollectionProperty;
import org.apache.jmeter.testelement.property.JMeterProperty;
import org.apache.jmeter.testelement.property.NullProperty;
import org.apache.jmeter.testelement.property.StringProperty;
import org.apache.jmeter.threads.JMeterThread;

class ThreadScheduleParams
{
   int initialDelay;
   int startRampUp;
   int flightTime;
   int threadSeqNum;
   int numThreads;
   int endRampUp;
   private final JMeterThread currentThread;
   private final CollectionProperty columns;

   ThreadScheduleParams(JMeterThread thread, JMeterProperty aData)
   {
      currentThread = thread;

      if (aData instanceof NullProperty)
      {
         throw new IllegalArgumentException("Received null property instead of collection");
      }
      columns = (CollectionProperty) aData;

      numThreads = getCell(0);
      initialDelay = getCell(1);
      startRampUp = getCell(2);
      flightTime = getCell(3);
      endRampUp = getCell(4);
   }

   private int getCell(int col)
   {
      List<?> column = (List<?>) columns.get(col).getObjectValue();

      Iterator it = ((List<?>) columns.get(0).getObjectValue()).iterator();
      int row = 0;
      int threadNo = currentThread.getThreadNum();
      while (it.hasNext())
      {
         int threads = ((StringProperty) it.next()).getIntValue();

         if (threads > threadNo)
         {
            threadSeqNum = threadNo;
            JMeterProperty cell = (JMeterProperty) column.get(row);
            return cell.getIntValue() < 0 ? 0 : cell.getIntValue();
         }

         threadNo -= threads;
         row++;
      }

      throw new IllegalArgumentException("Thread record not found for col " + col);
   }
}
