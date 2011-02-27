package kg.apc.jmeter.config;

import java.beans.PropertyDescriptor;
import kg.apc.jmeter.JMeterPluginsUtils;
import org.apache.jmeter.testbeans.BeanInfoSupport;

/**
 *
 * @author apc
 */
public class VariablesFromCSVFileBeanInfo
     extends BeanInfoSupport
{
   // These names must agree case-wise with the variable and property names
   private static final String FILENAME = "filename";               //$NON-NLS-1$
   private static final String VARIABLES_PREFIX = "variablesPrefix";    //$NON-NLS-1$
   private static final String DELIMITER = "delimiter";             //$NON-NLS-1$

   /**
    * 
    */
   public VariablesFromCSVFileBeanInfo()
   {
      super(VariablesFromCSVFile.class);

      createPropertyGroup("csv_data", //$NON-NLS-1$
           new String[]
           {
              FILENAME,
              VARIABLES_PREFIX,
              DELIMITER,
           });

      PropertyDescriptor p = property(FILENAME);
      p.setValue(NOT_UNDEFINED, Boolean.TRUE);
      p.setValue(DEFAULT, "");        //$NON-NLS-1$
      p.setValue(NOT_EXPRESSION, Boolean.TRUE);

      p = property(VARIABLES_PREFIX);
      p.setValue(NOT_UNDEFINED, Boolean.TRUE);
      p.setValue(DEFAULT, "");        //$NON-NLS-1$
      p.setValue(NOT_EXPRESSION, Boolean.TRUE);

      p = property(DELIMITER);
      p.setValue(NOT_UNDEFINED, Boolean.TRUE);
      p.setValue(DEFAULT, ",");        //$NON-NLS-1$
      p.setValue(NOT_EXPRESSION, Boolean.TRUE);

      getBeanDescriptor().setDisplayName(JMeterPluginsUtils.prefixLabel(getBeanDescriptor().getDisplayName()));
   }
}
