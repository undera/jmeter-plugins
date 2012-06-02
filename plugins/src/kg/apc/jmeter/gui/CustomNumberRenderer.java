package kg.apc.jmeter.gui;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import javax.swing.JLabel;
import org.apache.jorphan.gui.NumberRenderer;

/**
 *
 * @author Stephane Hoblingre
 */
public class CustomNumberRenderer extends NumberRenderer {
    protected final NumberFormat customFormatter;

    public CustomNumberRenderer() {
        super();
        customFormatter = null;
    }

    public CustomNumberRenderer(String format) {
       super();
       customFormatter = null;
    }

    public CustomNumberRenderer(String format, char groupingSeparator) {
        super();
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator(' ');

        customFormatter = new DecimalFormat(format, symbols);
        setHorizontalAlignment(JLabel.RIGHT);
    }

   @Override
    public void setValue(Object value) {
        setText((value == null) ? "" : (customFormatter == null ? formatter.format(value):customFormatter.format(value)));
    }
}
