package kg.apc.jmeter.vizualizers;

import java.awt.Color;
import java.util.Random;
import org.apache.jmeter.gui.util.JMeterColor;

/**
 *
 * @author apc
 */
public class ColorsDispatcher
{
   private final Color[] fixedColors =
   {
      Color.WHITE,
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
      Color.LIGHT_GRAY,
   };
   private int spentColors = 0;
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
      if (++spentColors > fixedColors.length-1)
      {
         return new Color(rnd.nextInt(0xFFFFFF));
      }
      else
      {
         return fixedColors[spentColors];
      }
   }

   public void reset()
   {
      spentColors = 0;
   }
}
