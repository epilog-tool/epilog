package org.epilogtool.io;

import java.awt.Image;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
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

	public static Image getImage(String image) {
		URL url = FileResource.getResource(image);
		if (url != null) {
			try {
				return ImageIO.read(url);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}
}
