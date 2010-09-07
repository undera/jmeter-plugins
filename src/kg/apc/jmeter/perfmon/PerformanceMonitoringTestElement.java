/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package kg.apc.jmeter.perfmon;

import java.io.Serializable;
import java.util.ArrayList;
import org.apache.jmeter.gui.util.PowerTableModel;
import org.apache.jmeter.samplers.Clearable;
import org.apache.jmeter.testelement.AbstractTestElement;
import org.apache.jmeter.testelement.property.CollectionProperty;
import org.apache.jmeter.testelement.property.JMeterProperty;

/**
 *
 * @author Stephane Hoblingre
 */
public class PerformanceMonitoringTestElement extends AbstractTestElement implements Serializable, Clearable
{
    public static final String DATA_PROPERTY = "perfomdata";
    public static final String MONITORING_TYPE = "monitoringType";

    public PerformanceMonitoringTestElement () {
        super();
    }

   public static CollectionProperty tableModelToCollectionProperty(PowerTableModel model)
   {
      CollectionProperty rows = new CollectionProperty(PerformanceMonitoringTestElement.DATA_PROPERTY, new ArrayList<Object>());
      for (int col = 0; col < model.getColumnCount(); col++)
      {
         rows.addItem(model.getColumnData(model.getColumnName(col)));
      }
      return rows;
   }

      public JMeterProperty getData()
   {
      //log.info("getData");
      JMeterProperty prop = getProperty(DATA_PROPERTY);
      return prop;
   }

   public void setData(CollectionProperty rows)
   {
      //log.info("setData");
      setProperty(rows);
   }

   public int getType() {
       return super.getPropertyAsInt(MONITORING_TYPE, 0);
   }

   public void setType(int type) {
       super.setProperty(MONITORING_TYPE, type);
   }

    @Override
    public void clearData() {
    }
}
