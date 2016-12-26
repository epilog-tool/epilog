package org.epilogtool.gui.widgets;

import java.awt.Component;
import java.awt.Font;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.SwingConstants;

import org.epilogtool.io.FileResource;

public class JComboImageBox extends JComboBox<ImageIcon> {
	private static final long serialVersionUID = 5114067663247442502L;

	private ImageIcon[] selectedImages;
	private String[] selectedDescr;

	public JComboImageBox(String[] names) {
		super();
		selectedImages = new ImageIcon[names.length];
		selectedDescr = names;
		for (int i = 0; i < names.length; i++) {
			selectedImages[i] = FileResource.getImageIcon(names[i] + ".png");
		}
		setModel(new DefaultComboBoxModel(selectedDescr));
		setRenderer(new ComboBoxRenderer());
	}

	class ComboBoxRenderer extends JLabel implements ListCellRenderer<Object> {
		private static final long serialVersionUID = 2340379218653860517L;
		private Font uhOhFont;

		public ComboBoxRenderer() {
			setOpaque(true);
			setHorizontalAlignment(SwingConstants.CENTER);
			setVerticalAlignment(SwingConstants.CENTER);
		}

		@Override
		public Component getListCellRendererComponent(JList<?> list, Object value,
				int index, boolean isSelected, boolean cellHasFocus) {

			if (isSelected) {
				setBackground(list.getSelectionBackground());
				setForeground(list.getSelectionForeground());
			} else {
				setBackground(list.getBackground());
				setForeground(list.getForeground());
			}

			String selected = (String) value;
			int selectedIndex = 0;
			for (int i = 0; i < selectedDescr.length; i++) {
				if (selectedDescr[i].equals(selected)) {
					selectedIndex = i;
					break;
				}
			}

			// Set the icon and text. If icon was null, say so.
			ImageIcon icon = selectedImages[selectedIndex];
			String descr = selectedDescr[selectedIndex];
			setIcon(icon);
			if (icon != null) {
				setText(descr);
				setFont(list.getFont());
			} else {
				setUhOhText(descr + " (no image available)", list.getFont());
			}

			return this;
		}

		// Set the font and text when no image was found.
		protected void setUhOhText(String uhOhText, Font normalFont) {
			if (uhOhFont == null) { // lazily create this font
				uhOhFont = normalFont.deriveFont(Font.ITALIC);
			}
			setFont(uhOhFont);
			setText(uhOhText);
		}
	}
}
