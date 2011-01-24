package kg.apc.jmeter.charting;

import java.text.SimpleDateFormat;

import org.apache.jorphan.gui.NumberRenderer;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

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
   private static final Logger log = LoggingManager.getLoggerForClass();
    protected final SimpleDateFormat dateFormatter;
    private long relativeStartTime = 0;
    private static final String EMPTY = "";

    /**
     *
     */
    public DateTimeRenderer()
    {
        super();
        dateFormatter = (SimpleDateFormat) SimpleDateFormat.getInstance();
        log.info("Simple inst");
    }

    /**
     *
     * @param format
     */
    public DateTimeRenderer(String format)
    {
        super();
        dateFormatter = new SimpleDateFormat(format);
        log.info("Format inst "+format);
    }

    /**
     * Creates new instance
     * @param format - date/time format
     * @param isRelative - use reative to test start time or not
     */
    public DateTimeRenderer(String format, long aRelativeStartTime)
    {
        this(format);
        relativeStartTime = aRelativeStartTime;
        log.info("Relative inst "+format+" "+aRelativeStartTime);
    }

    /**
     * Sets value to render
     * @param value
     */
    @Override
    public void setValue(Object value)
    {
        if (!(value instanceof Long))
            setText(EMPTY);
        else
        {
        log.info(value.toString());
            long val = (Long) value;
            setText(dateFormatter.format(val - relativeStartTime));
        }
    }
}
