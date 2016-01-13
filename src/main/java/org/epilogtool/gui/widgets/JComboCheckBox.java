package org.epilogtool.gui.widgets;

import java.awt.Component;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.ComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

public class JComboCheckBox extends JComboWideBox {
	public JComboCheckBox() {
		init();
	}

	public JComboCheckBox(JCheckBox[] items) {
		super(items);
		if (items != null && items.length > 0) {
			items[0].setSelected(true);
		}
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
	}

	private void itemSelected() {
		if (getSelectedItem() instanceof JCheckBox) {
			JCheckBox jcb = (JCheckBox) getSelectedItem();
			jcb.setSelected(!jcb.isSelected());
		}
	}

	public void updateSelected() {
		this.itemSelected();
	}
	
	public List<JCheckBox> getItems(){
		List<JCheckBox> lItems = new ArrayList<JCheckBox>();
		for (int i = 0; i < this.getModel().getSize(); i++) {
			lItems.add((JCheckBox) this.getModel().getElementAt(i));
		}
		return lItems;
	}

	public List<String> getSelectedItems() {
		List<String> lItemStrings = new ArrayList<String>();
		List<JCheckBox> lItemJCBs = this.getItems();
		for (JCheckBox jcb : lItemJCBs){
			if (jcb.isSelected()) {
				lItemStrings.add(jcb.getText());
			}
		}
		return lItemStrings;
	}
	
	@Override
	public void setPreferredWidth(){
		List<JCheckBox> lItemJCBs = this.getItems();
		String longestModelName = "";
		for (JCheckBox jcb : lItemJCBs) {
			if (jcb.getText().length() > longestModelName.length()) {
				longestModelName = jcb.getText();
			}
		}
		this.openDim = longestModelName.length()*8;
	}

	class ComboBoxRenderer implements ListCellRenderer {
		private JLabel defaultLabel;

		public ComboBoxRenderer() {
			setOpaque(true);
		}

		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
				boolean cellHasFocus) {
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