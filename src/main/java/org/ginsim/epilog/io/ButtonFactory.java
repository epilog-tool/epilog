package org.ginsim.epilog.io;

import java.awt.Insets;

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

	public static JButton getNoMargins(String text) {
		JButton jb = new JButton(text);
		jb.setMargin(new Insets(0, 0, 0, 0));
		return jb;
	}
}