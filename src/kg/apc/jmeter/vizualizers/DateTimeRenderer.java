package kg.apc.jmeter.vizualizers;

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
   private static final Logger log = LoggingManager.getLoggerForClass();

    /**
     *
     */
    protected final SimpleDateFormat dateFormatter;
    private boolean isRelative = false;
    private long relativeStartTime = 0;
    private static final String EMPTY = "";

    /**
     *
     */
    public DateTimeRenderer()
    {
        super();
        dateFormatter = (SimpleDateFormat) SimpleDateFormat.getInstance();
    }

    /**
     *
     * @param format
     */
    public DateTimeRenderer(String format)
    {
        super();
        dateFormatter = new SimpleDateFormat(format);
    }

    /**
     * Creates new instance
     * @param format - date/time format
     * @param isRelative - use reative to test start time or not
     */
    public DateTimeRenderer(String format, long aRelativeStartTime)
    {
        this(format);
        isRelative = true;
        relativeStartTime = aRelativeStartTime;
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
            long val = (Long) value;
            setText(dateFormatter.format(val - relativeStartTime));
        }
    }
}
