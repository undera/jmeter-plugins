package kg.apc.jmeter.gui;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import org.apache.jorphan.gui.NumberRenderer;

public class CustomNumberRenderer extends NumberRenderer {
    private NumberFormat customFormatter = null;

    public CustomNumberRenderer() {
        super();
    }

    public CustomNumberRenderer(String format) {
       super(format);
    }

    public CustomNumberRenderer(String format, char groupingSeparator) {
        super();
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator(groupingSeparator);
        customFormatter = new DecimalFormat(format, symbols);
    }

   @Override
    public void setValue(Object value) {
        String str = "";
        if(value != null) {
           if(customFormatter != null) {
              str = customFormatter.format(value);
           } else {
              str = formatter.format(value);
           }
        }
        setText(str);
    }
}
