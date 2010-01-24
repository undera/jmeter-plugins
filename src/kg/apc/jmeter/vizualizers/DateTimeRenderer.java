package kg.apc.jmeter.vizualizers;

import java.text.SimpleDateFormat;

import org.apache.jorphan.gui.NumberRenderer;

public class DateTimeRenderer
     extends NumberRenderer
{
   protected final SimpleDateFormat dateFormatter;

   public DateTimeRenderer()
   {
      super();
      dateFormatter = (SimpleDateFormat) SimpleDateFormat.getInstance();
   }

   public DateTimeRenderer(String format)
   {
      super();
      dateFormatter = new SimpleDateFormat(format);
   }

   @Override
   public void setValue(Object value)
   {
      setText((value == null) ? "" : dateFormatter.format(value));
   }
}
