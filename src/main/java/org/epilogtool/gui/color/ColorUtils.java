package org.epilogtool.gui.color;

import java.awt.Color;
import java.util.List;
import java.util.Random;

public class ColorUtils {
	public static final Color LIGHT_RED = new Color(255, 120, 120);

	public static Color getColor(String r, String g, String b) {
		return new Color(Integer.parseInt(r), Integer.parseInt(g),
				Integer.parseInt(b));
	}

	/**
	 * Convert a 8-bit Color into a CSS-like string (without the #).<br>
	 * <i>Exemple : Color(255,127,0) -> "FF7F00"</i>
	 * 
	 * @param color
	 *            the color to convert.
	 * @return String a string representation.
	 * 
	 */
	public static String getColorCode(Color color) {
		return "#"
				+ Integer.toHexString(color.getRGB() & 0xffffff | 0x1000000)
						.substring(1);
	}

	public static Color random() {
		return new Color(ColorUtils.getRandomNumber(256),
				ColorUtils.getRandomNumber(256),
				ColorUtils.getRandomNumber(256));
	}

	private static int getRandomNumber(int n) {
		return (new Random()).nextInt(n);
	}

	public static Color combine(List<Color> l) {
		int sumR = 0, sumG = 0, sumB = 0;
		int redIndex = 0;
		int blueIndex = 0;
		int greenIndex = 0;

		for (Color c : l) {

			if (c.getRed() != 255) {
				sumR += c.getRed();
				redIndex += 1;
			}
			if (c.getGreen() != 255) {
				sumG += c.getGreen();
				greenIndex += 1;
			}
			if (c.getBlue() != 255) {
				sumB += c.getBlue();
				blueIndex += 1;
			}
		}

		if (redIndex == 0)
			sumR = 255;
		else
			sumR /= redIndex;

		if (greenIndex == 0)
			sumG = 255;
		else
			sumG /= greenIndex;

		if (blueIndex == 0)
			sumB = 255;
		else
			sumB /= blueIndex;
		return new Color(sumR, sumG, sumB);
	}

	public static Color getColorAtValue(Color color, byte max, byte value) {
		if (max == value) {
			return color;
		} else if (value == 0) {
			return Color.WHITE;
		}
		int r = 255 - (255 - color.getRed()) * value / max;
		int g = 255 - (255 - color.getGreen()) * value / max;
		int b = 255 - (255 - color.getBlue()) * value / max;
		return new Color(r, g, b);
	}
}
