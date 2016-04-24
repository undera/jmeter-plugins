package kg.apc.charting;

import org.junit.Assert;
import org.junit.Test;

import java.awt.*;

public class LabelToColorMappingTest {


    @Test
    public void canDetectInvalidHtmlColor() {
        LabelToColorMapping mapping = new LabelToColorMapping();
        Color c = mapping.parseStandardHtmlColor("foo");
        Assert.assertNull("Should have received null, since 'foo' is not a standard html color", c);
    }

    @Test
    public void canDetectValidHtmlColor() {
        LabelToColorMapping mapping = new LabelToColorMapping();
        Color c = mapping.parseStandardHtmlColor("Red");
        Assert.assertEquals("Should have received red, since 'foo' is not a standard html color", new Color(0xFF0000), c);
    }

    @Test
    public void canDetectValidCaseInsensitiveHtmlColor() {
        LabelToColorMapping mapping = new LabelToColorMapping();
        Color c = mapping.parseStandardHtmlColor("red");
        Assert.assertEquals("Should have received red, since 'foo' is not a standard html color", new Color(0xFF0000), c);
    }

    @Test
    public void canGetColorMappedFromColorName() {
        LabelToColorMapping mapping = new LabelToColorMapping();
        Color c = mapping.parseStandardHtmlColor("Red");
        mapping.addMapping("CPU", c);

        Color actualColor = mapping.getColorForLabel("MyServer CPU");

        Color expectedColor = new Color(0xFF0000);
        Assert.assertEquals("The color for my CPU label was supposed to be red", expectedColor, actualColor);


    }

    @Test
    public void canGetColorMappedFromCaseInsensitiveColorName() {
        LabelToColorMapping mapping = new LabelToColorMapping();
        Color c = mapping.parseStandardHtmlColor("Red");
        mapping.addMapping("CPU", c);

        Color actualColor = mapping.getColorForLabel("MyServer CPU");

        Color expectedColor = new Color(0xFF0000);
        Assert.assertEquals("The color for my CPU label was supposed to be red", expectedColor, actualColor);
    }

    @Test
    public void canGetColorMappedFromColorNameForCaseInsensitiveLabel() {
        LabelToColorMapping mapping = new LabelToColorMapping();
        Color c = mapping.parseStandardHtmlColor("red");

        mapping.addMapping("cpU", c);

        Color actualColor = mapping.getColorForLabel("MyServer CPU");

        Color expectedColor = new Color(0xFF0000);
        Assert.assertEquals("The color for my CPU label was supposed to be red", expectedColor, actualColor);

    }

    @Test
    public void canGetColorMappedFromHexColor() {
        LabelToColorMapping mapping = new LabelToColorMapping();
        Color c = mapping.parseStandardHtmlColor("#FF0000");
        mapping.addMapping("CPU", c);

        Color actualColor = mapping.getColorForLabel("MyServer CPU");

        Color expectedColor = new Color(0xFF0000);
        Assert.assertEquals("The color for my CPU label was supposed to be red", expectedColor, actualColor);


    }

    @Test
    public void canDetectNoMap() {
        LabelToColorMapping mapping = new LabelToColorMapping();
        Color c = mapping.parseStandardHtmlColor("#FF0000");
        mapping.addMapping("foobar", c);

        Color actualColor = mapping.getColorForLabel("MyServer CPU");

        Assert.assertNull("A mapping was found for my label, even though zero mappings were added.", actualColor);


    }

    @Test
    public void canGetColorMappedFromColorName_mappingFromConfigString() {
        LabelToColorMapping mapping = LabelToColorMapping.load("CPU=Red");

        Color actualColor = mapping.getColorForLabel("MyServer CPU");

        Color expectedColor = new Color(0xFF0000);
        Assert.assertEquals("The color for my CPU label was supposed to be red", expectedColor, actualColor);


    }

    @Test
    public void canGetColorMappedFromHexColor_mappingFromConfigString() {
        LabelToColorMapping mapping = LabelToColorMapping.load("CPU=#FF0000");

        Color actualColor = mapping.getColorForLabel("MyServer CPU");

        Color expectedColor = new Color(0xFF0000);
        Assert.assertEquals("The color for my CPU label was supposed to be red", expectedColor, actualColor);


    }

    @Test
    public void canDetectNoMap_mappingFromConfigString() {
        LabelToColorMapping mapping = LabelToColorMapping.load("foobar=#FF0000");

        Color actualColor = mapping.getColorForLabel("MyServer CPU");

        Assert.assertNull("A mapping was found for my label, even though zero mappings were added.", actualColor);
    }
}
