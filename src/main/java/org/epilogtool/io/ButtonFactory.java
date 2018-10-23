package org.epilogtool.io;

import java.awt.Image;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.border.Border;

public class ButtonFactory {

	public static JButton getImageNoBorder(String image) {
		JButton button = new JButton(FileResource.getImageIcon(image)) {
			private static final long serialVersionUID = 5137309676670505260L;

			@Override
			public void setBorder(Border border) {
			}
		};
		// button.setMargin(new Insets(0, 0, 0, 0));
		return button;
	}

	public static JButton getImageLineBorder(String image, int width, int height) {
		Image img = FileResource.getImageIcon(image).getImage();
		Image newimg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
		JButton jbutton =  new JButton(new ImageIcon(newimg));
		jbutton.setBorder(BorderFactory.createEtchedBorder(1));
		return jbutton;
	}

	public static JButton getNoMargins(String text) {
		JButton jb = new JButton(text);
		jb.setMargin(new Insets(0, 0, 0, 0));
		return jb;
	}
}