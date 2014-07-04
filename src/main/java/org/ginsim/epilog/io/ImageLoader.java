package org.ginsim.epilog.io;

import java.net.URL;

import javax.swing.ImageIcon;

public class ImageLoader {
	private static final String PATH = "/org/ginsim/epilog/icon/";

	public static ImageIcon getImageIcon(String image) {
		URL url = ImageLoader.class.getResource(ImageLoader.PATH + image);
		if (url != null) {
			return new ImageIcon(url);
		}
		return null;
	}
}