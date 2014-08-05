package org.ginsim.epilog.io;

import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.border.Border;

public class ButtonImageLoader {
	private static final String PATH = "/org/ginsim/epilog/icon/";

	public static ImageIcon getImageIcon(String image) {
		URL url = ButtonImageLoader.class.getResource(ButtonImageLoader.PATH
				+ image);
		if (url != null) {
			return new ImageIcon(url);
		}
		return null;
	}

	public static JButton newButtonNoBorder(String image) {
		JButton button = new JButton(
				ButtonImageLoader.getImageIcon(image)) {
			@Override
			public void setBorder(Border border) {
			}
		};
		return button;
	}
}