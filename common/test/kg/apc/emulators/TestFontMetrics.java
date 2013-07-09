package kg.apc.emulators;

import java.awt.Font;
import java.awt.FontMetrics;

/**
 * 
 * @author apc
 */
public class TestFontMetrics
     extends FontMetrics
{
   /**
    *
    * @param f
    */
   public TestFontMetrics(Font f)
   {
      super(f);
   }

   @Override
   public int getHeight()
   {
      return 10;
   }

   @Override
   public int stringWidth(String str)
   {
      return str.length();
   }
}
