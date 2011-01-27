package kg.apc.jmeter.charting;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

import org.apache.jorphan.gui.NumberRenderer;

/**
 *
 * @author apc
 */
public class DateTimeRenderer
        extends NumberRenderer
{

    /**
     *
     */
   // private static final Logger log = LoggingManager.getLoggerForClass();
    protected final SimpleDateFormat dateFormatter;
    private long relativeStartTime = 0;
    private static final String EMPTY = "";
    public static final String HHMMSS = "HH:mm:ss";

    /**
     *
     */
    public DateTimeRenderer()
    {
        super();
        dateFormatter = (SimpleDateFormat) SimpleDateFormat.getInstance();
        //log.info("Simple inst");
    }

    /**
     *
     * @param format
     */
    public DateTimeRenderer(String format)
    {
        super();
        dateFormatter = new SimpleDateFormat(format);
       // log.info("Format inst " + format);
    }

    /**
     * Creates new instance
     * @param format - date/time format
     * @param aRelativeStartTime - test start time
     */
    public DateTimeRenderer(String format, long aRelativeStartTime)
    {
        this(format);
        TimeZone utc = TimeZone.getTimeZone("UTC");
        dateFormatter.setTimeZone(utc);

        relativeStartTime = aRelativeStartTime;
    }

    @Override
    public void setValue(Object value)
    {
        if (value == null)
            setText(EMPTY);
        else
            setLongValue((Long) value);
    }

    /**
     * Sets value to render
     * @param value
     */
    private void setLongValue(Long value)
    {
        long tick = value - relativeStartTime;
        // 500 means half a second.
        // We allow to have half a second error with first label
        String res = dateFormatter.format(tick > 500 ? tick : 0);
        setText(res);
    }
}
