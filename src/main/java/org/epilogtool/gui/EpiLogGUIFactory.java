package org.epilogtool.gui;

import java.awt.Font;

import javax.swing.JLabel;

public class EpiLogGUIFactory {
	public static JLabel getJLabelBold(String text) {
		JLabel jLabel = new JLabel(text);
		Font font = jLabel.getFont();
		jLabel.setFont(new Font(font.getFontName(), Font.BOLD, font.getSize()));
		return jLabel;
	}

	public static JLabel getJLabelItalic(String text) {
		JLabel jLabel = new JLabel(text);
		Font font = jLabel.getFont();
		jLabel.setFont(new Font(font.getFontName(), Font.ITALIC, font.getSize()));
		return jLabel;
	}
}
