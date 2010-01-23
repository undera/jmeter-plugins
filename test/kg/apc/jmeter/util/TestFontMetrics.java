package kg.apc.jmeter.util;

import java.awt.Font;
import java.awt.FontMetrics;

public class TestFontMetrics
     extends FontMetrics
{
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
