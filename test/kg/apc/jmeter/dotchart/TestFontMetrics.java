/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kg.apc.jmeter.dotchart;

import java.awt.Font;
import java.awt.FontMetrics;

/**
 *
 * @author apc
 */
class TestFontMetrics
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
        //System.out.println("1");
        return str.length();
    }
}
