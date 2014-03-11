package org.ginsim.epilog.gui.color;

import java.awt.Color;
import java.util.List;
import java.util.Random;

public class ColorUtils {

	private static int getRandomNumber(int n) {
		return (new Random()).nextInt(n);
	}

	public static Color getColor(String r, String g, String b) {
		return new Color(Integer.parseInt(r), Integer.parseInt(g),
				Integer.parseInt(b));
	}

	public static Color random() {
		return new Color(ColorUtils.getRandomNumber(256),
				ColorUtils.getRandomNumber(256),
				ColorUtils.getRandomNumber(256));
	}

	public static Color combine(List<Color> l) {
		int sumR = 255, sumG = 255, sumB = 255;

		for (Color c : l) {
			sumR += c.getRed();
			sumG += c.getGreen();
			sumB += c.getBlue();
		}
		sumR /= (l.size() + 1);
		sumG /= (l.size() + 1);
		sumB /= (l.size() + 1);
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
