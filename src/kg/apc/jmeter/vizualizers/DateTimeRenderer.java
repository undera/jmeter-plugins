package kg.apc.jmeter.vizualizers;

import java.text.SimpleDateFormat;

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
