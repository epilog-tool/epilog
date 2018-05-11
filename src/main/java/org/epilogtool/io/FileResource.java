package org.epilogtool.io;

import java.net.URL;

import javax.swing.ImageIcon;

public class FileResource {
	private static final String PATH = "/org/epilogtool/icon/";

	public static URL getResource(String file) {
		return ButtonFactory.class.getResource(FileResource.PATH + file);
	}

	public static ImageIcon getImageIcon(String image) {
		URL url = FileResource.getResource(image);
		if (url != null) {
			return new ImageIcon(url);
		}
		return null;
	}
}
