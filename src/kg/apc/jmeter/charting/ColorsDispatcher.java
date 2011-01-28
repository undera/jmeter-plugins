package kg.apc.jmeter.charting;

import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;
import org.apache.jmeter.gui.util.JMeterColor;

/**
 *
 * @author apc
 */
public class ColorsDispatcher implements Serializable
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

    private static ArrayList<Color> randomColors = new ArrayList<Color>();

    public final static Color RED = fixedColors[0];
    public final static Color GREEN = fixedColors[1];

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
        Color ret;
        index++;
        if (index < fixedColors.length)
        {
            ret = fixedColors[index];
        } else
        {
            int rndIndex = index - fixedColors.length;
            
            if(randomColors.size() > rndIndex)
            {
                ret = randomColors.get(rndIndex);
            } else
            {
                ret = new Color(rnd.nextInt(0xFFFFFF));
                randomColors.add(ret);
            }
        }
        return ret;
    }

    public void reset()
    {
        index = -1;
    }
}
