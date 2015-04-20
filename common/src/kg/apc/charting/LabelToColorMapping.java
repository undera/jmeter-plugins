package kg.apc.charting;

import java.awt.Color;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

public class LabelToColorMapping {

    private static final Logger log = LoggingManager.getLoggerForClass();
	
	public LabelToColorMapping() {
		loadStandardHtmlColors();
	}
	private static Map<String,Color> standardHtmlColorNames = null;
	private Map<String,Color> labelToColorMapping = new Hashtable<String,Color>();
	

	public Color parseStandardHtmlColor(String someKindOfColorDescription) {
		Color c = standardHtmlColorNames.get( someKindOfColorDescription.toLowerCase() );
		if (c==null) {
			if (someKindOfColorDescription.trim().startsWith("#") && someKindOfColorDescription.length() >= 2) {
				String hexColor = someKindOfColorDescription.substring(1);
				int intColor = Integer.parseInt(hexColor,16);
				if (intColor >0 && intColor < 0x1010000) {
					c = new Color(intColor);
				}
			}
		}
		return c;
	}

	public void addStandardHtmlColor(String colorName, Color c) {
        standardHtmlColorNames.put(colorName.toLowerCase().trim(), c);
	}
	public Color getColorForLabel(String label) {
		Iterator<Map.Entry<String, Color>> it = labelToColorMapping.entrySet().iterator();
		Color rc = null;
		while (it.hasNext()) {

			  Map.Entry<String, Color> entry = it.next();

			  if (log.isDebugEnabled()) {
				  log.debug("Label [" + label + "] entry.getKey["  + entry.getKey() + "]");
			  }
			  if (label.toLowerCase().contains(entry.getKey()) ) {
				  rc = entry.getValue();
				  break;
			  }
		}
		return rc;
	}

	public void addMapping(String label, Color c) {
		this.labelToColorMapping.put(label.toLowerCase().trim(), c);
	}

	public static LabelToColorMapping load(String colorConfigString) {
		LabelToColorMapping mapping = null;
		//  Why 3?  Gotta have at lest "x=y" for the mapping, 3 chars.
		if (colorConfigString!=null && colorConfigString.length() >= 3) {
			mapping = new LabelToColorMapping();
			String[] manyMappings = colorConfigString.split(":");
			for(String oneMapping : manyMappings) {
				String[] twoParts = oneMapping.split("=");
				if (twoParts.length!=2)
					throw new RuntimeException("Was expecting color mapping config in this format:  myLabel1=Red:myLabel2=Blue:myLabel3=Green.  Instead found [" + colorConfigString + "]");
				Color c = mapping.parseStandardHtmlColor(twoParts[1]);
				if (c == null)
					throw new RuntimeException("Expecting the text [" + twoParts[1] + "] to be a name or RGB hex value of a color.  Entire config string: [" + colorConfigString + "]");
				mapping.addMapping(twoParts[0], c);
			}
		}
		return mapping;
	}
	/**
	 * Color Names taken from http://www.w3schools.com/htmL/html_colornames.asp
	 */
	private void loadStandardHtmlColors() {

		if (standardHtmlColorNames==null) {
			standardHtmlColorNames = new Hashtable<String,Color>();
			addStandardHtmlColor( "AliceBlue" 	, new Color(0xF0F8FF));
			addStandardHtmlColor( "AntiqueWhite" 	, new Color(0xFAEBD7));
			addStandardHtmlColor( "Aqua" 	, new Color(0x00FFFF));
			addStandardHtmlColor( "Aquamarine" 	, new Color(0x7FFFD4));
			addStandardHtmlColor( "Azure" 	, new Color(0xF0FFFF));
			addStandardHtmlColor( "Beige" 	, new Color(0xF5F5DC));
			addStandardHtmlColor( "Bisque" 	, new Color(0xFFE4C4));
			addStandardHtmlColor( "Black" 	, new Color(0x000000));
			addStandardHtmlColor( "BlanchedAlmond" 	, new Color(0xFFEBCD));
			addStandardHtmlColor( "Blue" 	, new Color(0x0000FF));
			addStandardHtmlColor( "BlueViolet" 	, new Color(0x8A2BE2));
			addStandardHtmlColor( "Brown" 	, new Color(0xA52A2A));
			addStandardHtmlColor( "BurlyWood" 	, new Color(0xDEB887));
			addStandardHtmlColor( "CadetBlue" 	, new Color(0x5F9EA0));
			addStandardHtmlColor( "Chartreuse" 	, new Color(0x7FFF00));
			addStandardHtmlColor( "Chocolate" 	, new Color(0xD2691E));
			addStandardHtmlColor( "Coral" 	, new Color(0xFF7F50));
			addStandardHtmlColor( "CornflowerBlue" 	, new Color(0x6495ED));
			addStandardHtmlColor( "Cornsilk" 	, new Color(0xFFF8DC));
			addStandardHtmlColor( "Crimson" 	, new Color(0xDC143C));
			addStandardHtmlColor( "Cyan" 	, new Color(0x00FFFF));
			addStandardHtmlColor( "DarkBlue" 	, new Color(0x00008B));
			addStandardHtmlColor( "DarkCyan" 	, new Color(0x008B8B));
			addStandardHtmlColor( "DarkGoldenRod" 	, new Color(0xB8860B));
			addStandardHtmlColor( "DarkGray" 	, new Color(0xA9A9A9));
			addStandardHtmlColor( "DarkGreen" 	, new Color(0x006400));
			addStandardHtmlColor( "DarkKhaki" 	, new Color(0xBDB76B));
			addStandardHtmlColor( "DarkMagenta" 	, new Color(0x8B008B));
			addStandardHtmlColor( "DarkOliveGreen" 	, new Color(0x556B2F));
			addStandardHtmlColor( "DarkOrange" 	, new Color(0xFF8C00));
			addStandardHtmlColor( "DarkOrchid" 	, new Color(0x9932CC));
			addStandardHtmlColor( "DarkRed" 	, new Color(0x8B0000));
			addStandardHtmlColor( "DarkSalmon" 	, new Color(0xE9967A));
			addStandardHtmlColor( "DarkSeaGreen" 	, new Color(0x8FBC8F));
			addStandardHtmlColor( "DarkSlateBlue" 	, new Color(0x483D8B));
			addStandardHtmlColor( "DarkSlateGray" 	, new Color(0x2F4F4F));
			addStandardHtmlColor( "DarkTurquoise" 	, new Color(0x00CED1));
			addStandardHtmlColor( "DarkViolet" 	, new Color(0x9400D3));
			addStandardHtmlColor( "DeepPink" 	, new Color(0xFF1493));
			addStandardHtmlColor( "DeepSkyBlue" 	, new Color(0x00BFFF));
			addStandardHtmlColor( "DimGray" 	, new Color(0x696969));
			addStandardHtmlColor( "DodgerBlue" 	, new Color(0x1E90FF));
			addStandardHtmlColor( "FireBrick" 	, new Color(0xB22222));
			addStandardHtmlColor( "FloralWhite" 	, new Color(0xFFFAF0));
			addStandardHtmlColor( "ForestGreen" 	, new Color(0x228B22));
			addStandardHtmlColor( "Fuchsia" 	, new Color(0xFF00FF));
			addStandardHtmlColor( "Gainsboro" 	, new Color(0xDCDCDC));
			addStandardHtmlColor( "GhostWhite" 	, new Color(0xF8F8FF));
			addStandardHtmlColor( "Gold" 	, new Color(0xFFD700));
			addStandardHtmlColor( "GoldenRod" 	, new Color(0xDAA520));
			addStandardHtmlColor( "Gray" 	, new Color(0x808080));
			addStandardHtmlColor( "Green" 	, new Color(0x008000));
			addStandardHtmlColor( "GreenYellow" 	, new Color(0xADFF2F));
			addStandardHtmlColor( "HoneyDew" 	, new Color(0xF0FFF0));
			addStandardHtmlColor( "HotPink" 	, new Color(0xFF69B4));
			addStandardHtmlColor( "IndianRed " 	, new Color(0xCD5C5C));
			addStandardHtmlColor( "Indigo " 	, new Color(0x4B0082));
			addStandardHtmlColor( "Ivory" 	, new Color(0xFFFFF0));
			addStandardHtmlColor( "Khaki" 	, new Color(0xF0E68C));
			addStandardHtmlColor( "Lavender" 	, new Color(0xE6E6FA));
			addStandardHtmlColor( "LavenderBlush" 	, new Color(0xFFF0F5));
			addStandardHtmlColor( "LawnGreen" 	, new Color(0x7CFC00));
			addStandardHtmlColor( "LemonChiffon" 	, new Color(0xFFFACD));
			addStandardHtmlColor( "LightBlue" 	, new Color(0xADD8E6));
			addStandardHtmlColor( "LightCoral" 	, new Color(0xF08080));
			addStandardHtmlColor( "LightCyan" 	, new Color(0xE0FFFF));
			addStandardHtmlColor( "LightGoldenRodYellow" 	, new Color(0xFAFAD2));
			addStandardHtmlColor( "LightGray" 	, new Color(0xD3D3D3));
			addStandardHtmlColor( "LightGreen" 	, new Color(0x90EE90));
			addStandardHtmlColor( "LightPink" 	, new Color(0xFFB6C1));
			addStandardHtmlColor( "LightSalmon" 	, new Color(0xFFA07A));
			addStandardHtmlColor( "LightSeaGreen" 	, new Color(0x20B2AA));
			addStandardHtmlColor( "LightSkyBlue" 	, new Color(0x87CEFA));
			addStandardHtmlColor( "LightSlateGray" 	, new Color(0x778899));
			addStandardHtmlColor( "LightSteelBlue" 	, new Color(0xB0C4DE));
			addStandardHtmlColor( "LightYellow" 	, new Color(0xFFFFE0));
			addStandardHtmlColor( "Lime" 	, new Color(0x00FF00));
			addStandardHtmlColor( "LimeGreen" 	, new Color(0x32CD32));
			addStandardHtmlColor( "Linen" 	, new Color(0xFAF0E6));
			addStandardHtmlColor( "Magenta" 	, new Color(0xFF00FF));
			addStandardHtmlColor( "Maroon" 	, new Color(0x800000));
			addStandardHtmlColor( "MediumAquaMarine" 	, new Color(0x66CDAA));
			addStandardHtmlColor( "MediumBlue" 	, new Color(0x0000CD));
			addStandardHtmlColor( "MediumOrchid" 	, new Color(0xBA55D3));
			addStandardHtmlColor( "MediumPurple" 	, new Color(0x9370DB));
			addStandardHtmlColor( "MediumSeaGreen" 	, new Color(0x3CB371));
			addStandardHtmlColor( "MediumSlateBlue" 	, new Color(0x7B68EE));
			addStandardHtmlColor( "MediumSpringGreen" 	, new Color(0x00FA9A));
			addStandardHtmlColor( "MediumTurquoise" 	, new Color(0x48D1CC));
			addStandardHtmlColor( "MediumVioletRed" 	, new Color(0xC71585));
			addStandardHtmlColor( "MidnightBlue" 	, new Color(0x191970));
			addStandardHtmlColor( "MintCream" 	, new Color(0xF5FFFA));
			addStandardHtmlColor( "MistyRose" 	, new Color(0xFFE4E1));
			addStandardHtmlColor( "Moccasin" 	, new Color(0xFFE4B5));
			addStandardHtmlColor( "NavajoWhite" 	, new Color(0xFFDEAD));
			addStandardHtmlColor( "Navy" 	, new Color(0x000080));
			addStandardHtmlColor( "OldLace" 	, new Color(0xFDF5E6));
			addStandardHtmlColor( "Olive" 	, new Color(0x808000));
			addStandardHtmlColor( "OliveDrab" 	, new Color(0x6B8E23));
			addStandardHtmlColor( "Orange" 	, new Color(0xFFA500));
			addStandardHtmlColor( "OrangeRed" 	, new Color(0xFF4500));
			addStandardHtmlColor( "Orchid" 	, new Color(0xDA70D6));
			addStandardHtmlColor( "PaleGoldenRod" 	, new Color(0xEEE8AA));
			addStandardHtmlColor( "PaleGreen" 	, new Color(0x98FB98));
			addStandardHtmlColor( "PaleTurquoise" 	, new Color(0xAFEEEE));
			addStandardHtmlColor( "PaleVioletRed" 	, new Color(0xDB7093));
			addStandardHtmlColor( "PapayaWhip" 	, new Color(0xFFEFD5));
			addStandardHtmlColor( "PeachPuff" 	, new Color(0xFFDAB9));
			addStandardHtmlColor( "Peru" 	, new Color(0xCD853F));
			addStandardHtmlColor( "Pink" 	, new Color(0xFFC0CB));
			addStandardHtmlColor( "Plum" 	, new Color(0xDDA0DD));
			addStandardHtmlColor( "PowderBlue" 	, new Color(0xB0E0E6));
			addStandardHtmlColor( "Purple" 	, new Color(0x800080));
			addStandardHtmlColor( "RebeccaPurple" 	, new Color(0x663399));
			addStandardHtmlColor( "Red" 	, new Color(0xFF0000));
			addStandardHtmlColor( "RosyBrown" 	, new Color(0xBC8F8F));
			addStandardHtmlColor( "RoyalBlue" 	, new Color(0x4169E1));
			addStandardHtmlColor( "SaddleBrown" 	, new Color(0x8B4513));
			addStandardHtmlColor( "Salmon" 	, new Color(0xFA8072));
			addStandardHtmlColor( "SandyBrown" 	, new Color(0xF4A460));
			addStandardHtmlColor( "SeaGreen" 	, new Color(0x2E8B57));
			addStandardHtmlColor( "SeaShell" 	, new Color(0xFFF5EE));
			addStandardHtmlColor( "Sienna" 	, new Color(0xA0522D));
			addStandardHtmlColor( "Silver" 	, new Color(0xC0C0C0));
			addStandardHtmlColor( "SkyBlue" 	, new Color(0x87CEEB));
			addStandardHtmlColor( "SlateBlue" 	, new Color(0x6A5ACD));
			addStandardHtmlColor( "SlateGray" 	, new Color(0x708090));
			addStandardHtmlColor( "Snow" 	, new Color(0xFFFAFA));
			addStandardHtmlColor( "SpringGreen" 	, new Color(0x00FF7F));
			addStandardHtmlColor( "SteelBlue" 	, new Color(0x4682B4));
			addStandardHtmlColor( "Tan" 	, new Color(0xD2B48C));
			addStandardHtmlColor( "Teal" 	, new Color(0x008080));
			addStandardHtmlColor( "Thistle" 	, new Color(0xD8BFD8));
			addStandardHtmlColor( "Tomato" 	, new Color(0xFF6347));
			addStandardHtmlColor( "Turquoise" 	, new Color(0x40E0D0));
			addStandardHtmlColor( "Violet" 	, new Color(0xEE82EE));
			addStandardHtmlColor( "Wheat" 	, new Color(0xF5DEB3));
			addStandardHtmlColor( "White" 	, new Color(0xFFFFFF));
			addStandardHtmlColor( "WhiteSmoke" 	, new Color(0xF5F5F5));
			addStandardHtmlColor( "Yellow" 	, new Color(0xFFFF00));
			addStandardHtmlColor( "YellowGreen" 	, new Color(0x9ACD32));
			log.debug("Standard HTML color names loaded.");
		}
	}
	
}