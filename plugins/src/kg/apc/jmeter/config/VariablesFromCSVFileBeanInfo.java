package kg.apc.jmeter.config;

import java.beans.PropertyDescriptor;
import kg.apc.jmeter.JMeterPluginsUtils;
import org.apache.jmeter.testbeans.BeanInfoSupport;
import org.apache.jmeter.util.JMeterUtils;

/**
 *
 * @author apc
 */
public class VariablesFromCSVFileBeanInfo
     extends BeanInfoSupport
{
   // These names must agree case-wise with the variable and property names
   private static final String FILENAME = "filename";               
   private static final String VARIABLES_PREFIX = "variablesPrefix";    
   private static final String DELIMITER = "delimiter";
   private static final String MIN_JMETER_VERSION = "2.5.2";

   /**
    * 
    */
   public VariablesFromCSVFileBeanInfo()
   {
      super(VariablesFromCSVFile.class);

      createPropertyGroup("csv_data", 
           new String[]
           {
              FILENAME,
              VARIABLES_PREFIX,
              DELIMITER,
           });

      PropertyDescriptor p = property(FILENAME);
      p.setValue(NOT_UNDEFINED, Boolean.TRUE);
      p.setValue(DEFAULT, "");        
      p.setValue(NOT_EXPRESSION, Boolean.TRUE);

      p = property(VARIABLES_PREFIX);
      p.setValue(NOT_UNDEFINED, Boolean.TRUE);
      p.setValue(DEFAULT, "");        
      p.setValue(NOT_EXPRESSION, Boolean.TRUE);

      p = property(DELIMITER);
      p.setValue(NOT_UNDEFINED, Boolean.TRUE);
      p.setValue(DEFAULT, ",");        
      p.setValue(NOT_EXPRESSION, Boolean.TRUE);

      getBeanDescriptor().setDisplayName(JMeterPluginsUtils.prefixLabel(getBeanDescriptor().getDisplayName()));

      //hide bean from menu, but hidden flag is taken in account in JMeter >2.5.1 only
      getBeanDescriptor().setHidden(true);

      //if JMeter <= 2.5.1, use null name hack
      if(MIN_JMETER_VERSION.compareTo(JMeterUtils.getJMeterVersion()) > 0) {
         getBeanDescriptor().setDisplayName(null);
      }    
   }
}
