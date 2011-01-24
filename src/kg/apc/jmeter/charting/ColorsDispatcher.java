package kg.apc.jmeter.charting;

import java.awt.Color;
import java.util.Random;
import org.apache.jmeter.gui.util.JMeterColor;

/**
 *
 * @author apc
 */
public class ColorsDispatcher
{

    private final static Color[] fixedColors =
   {
      Color.RED,
      Color.GREEN,
      Color.BLUE,
      JMeterColor.purple,
      Color.ORANGE,
      Color.CYAN,
      Color.MAGENTA,
      Color.PINK,
      Color.YELLOW,
      JMeterColor.LAVENDER,
      JMeterColor.dark_green,
      Color.GRAY,
      Color.LIGHT_GRAY
   };

    public static Color RED = fixedColors[0];
    public static Color GREEN = fixedColors[1];

    private int index = -1;
    private final Random rnd;

    /**
     *
     */
    public ColorsDispatcher()
    {
        rnd = new Random();
    }

    /**
     *
     * @return
     */
    public Color getNextColor()
    {
        index++;
        if (index < fixedColors.length)
        {
            return fixedColors[index];
        } else
        {
            return new Color(rnd.nextInt(0xFFFFFF));
        }
    }

    public void reset()
    {
        index = -1;
    }
}
