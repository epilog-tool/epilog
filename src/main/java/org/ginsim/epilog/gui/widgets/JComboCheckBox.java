package org.ginsim.epilog.gui.widgets;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.ComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

public class JComboCheckBox extends JComboBox {
	public JComboCheckBox() {
		init();
	}

	public JComboCheckBox(JCheckBox[] items) {
		super(items);
		init();
	}

	public JComboCheckBox(Vector items) {
		super(items);
		init();
	}

	public JComboCheckBox(ComboBoxModel aModel) {
		super(aModel);
		init();
	}

	private void init() {
		setRenderer(new ComboBoxRenderer());
		// addActionListener(new ActionListener() {
		// public void actionPerformed(ActionEvent ae) {
		// itemSelected();
		// }
		// });
	}

	private void itemSelected() {
		if (getSelectedItem() instanceof JCheckBox) {
			JCheckBox jcb = (JCheckBox) getSelectedItem();
			jcb.setSelected(!jcb.isSelected());
		}
	}

	public List<String> getSelectedItems() {
		itemSelected();
		
		List<String> sItems = new ArrayList<String>();
		System.out.println("Model Size: " + this.getModel().getSize());

		for (int i = 0; i < this.getModel().getSize(); i++) {
			JCheckBox jcb = (JCheckBox) this.getModel().getElementAt(i);
			System.out.println(jcb.getText() + " is "
					+ (jcb.isSelected() ? "" : "not") + " selected");
			if (jcb.isSelected()) {
				sItems.add(jcb.getText());
			}
		}
		return sItems;
	}

	class ComboBoxRenderer implements ListCellRenderer {
		private JLabel defaultLabel;

		public ComboBoxRenderer() {
			setOpaque(true);
		}

		public Component getListCellRendererComponent(JList list, Object value,
				int index, boolean isSelected, boolean cellHasFocus) {
			if (value instanceof Component) {
				Component c = (Component) value;
				return c;
			} else {
				if (defaultLabel == null)
					defaultLabel = new JLabel(value.toString());
				else
					defaultLabel.setText(value.toString());
				return defaultLabel;
			}
		}
	}
}