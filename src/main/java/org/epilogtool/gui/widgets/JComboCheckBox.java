package org.epilogtool.gui.widgets;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

public class JComboCheckBox extends JComboWideBox<JCheckBox> {
	private static final long serialVersionUID = -2552852517721880821L;

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

	public JComboCheckBox(Vector<JCheckBox> items) {
		super(items);
		init();
	}

	public JComboCheckBox(ComboBoxModel<JCheckBox> aModel) {
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

	private List<JCheckBox> getCheckBoxes() {
		List<JCheckBox> lItems = new ArrayList<JCheckBox>();
		for (int i = 0; i < this.getModel().getSize(); i++) {
			lItems.add((JCheckBox) this.getModel().getElementAt(i));
		}
		return lItems;
	}

	public void updateItemList(List<String> lItems) {
		List<JCheckBox> lCurrJCB = this.getCheckBoxes();
		int sz = lCurrJCB.size();
		// 1. adicionar no final
		for (String sName : lItems) {
			boolean exists = false;
			for (int i = 0; i < sz; i++) {
				if (lCurrJCB.get(i).getText().equals(sName)) {
					exists = true;
					break;
				}
			}
			if (!exists) {
				lCurrJCB.add(new JCheckBox(sName, false));
				System.out.println(" . JCB.add: " + sName);
			}
		}

		// 2. remover os que já não existem
		for (int i = sz - 1; i >= 0; i--) {
			if (!lItems.contains(lCurrJCB.get(i).getText())) {
				System.out.println(" . JCB.remove: " + lCurrJCB.get(i).getText());
				lCurrJCB.remove(i);
			}
		}
		// put it inside
		setModel(new DefaultComboBoxModel(lCurrJCB.toArray()));
	}

	public List<String> getSelectedItems() {
		List<String> lItemStrings = new ArrayList<String>();
		List<JCheckBox> lItemJCBs = this.getCheckBoxes();
		for (JCheckBox jcb : lItemJCBs) {
			if (jcb.isSelected()) {
				lItemStrings.add(jcb.getText());
			}
		}
		return lItemStrings;
	}

	@Override
	public void setPreferredWidth() {
		List<JCheckBox> lItemJCBs = this.getCheckBoxes();
		String longestModelName = "";
		for (JCheckBox jcb : lItemJCBs) {
			if (jcb.getText().length() > longestModelName.length()) {
				longestModelName = jcb.getText();
			}
		}
		this.openDim = longestModelName.length() * 8;
	}

	class ComboBoxRenderer implements ListCellRenderer<Object> {
		private JLabel defaultLabel;

		public ComboBoxRenderer() {
			setOpaque(true);
		}

		public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
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