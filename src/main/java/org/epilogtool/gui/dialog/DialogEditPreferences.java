package org.epilogtool.gui.dialog;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.epilogtool.OptionStore;

public class DialogEditPreferences extends EscapableDialog {
	private static final long serialVersionUID = 1877338344309723137L;

	private JPanel panelSimulation;
	private JComboBox<GridNodePercent> jcbGridNodePercent;

	private boolean bIsOK;

	private JButton buttonCancel;
	private JButton buttonOK;

	public DialogEditPreferences() {
		this.setLayout(new BorderLayout());

		this.panelSimulation = new JPanel();
		this.panelSimulation.setBorder(BorderFactory.createTitledBorder("Simulation Performance"));
		this.panelSimulation.setLayout(new GridBagLayout());
		this.add(this.panelSimulation, BorderLayout.CENTER);

		GridBagConstraints c = new GridBagConstraints();
		// natural height, maximum width
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(3, 3, 3, 3);

		// Node percentage in grid
		c.gridx = 0;
		c.gridy = 0;
		this.panelSimulation.add(new JLabel(GridNodePercent.title()), c);
		this.jcbGridNodePercent = new JComboBox<GridNodePercent>(
				new GridNodePercent[] { GridNodePercent.YES, GridNodePercent.NO });
		String nodePercent = (String) OptionStore.getOption("PrefsNodePercent");
		for (int i = 0; i < this.jcbGridNodePercent.getItemCount(); i++) {
			if (nodePercent != null && nodePercent.equals(this.jcbGridNodePercent.getItemAt(i).toString()))
				this.jcbGridNodePercent.setSelectedIndex(i);
		}
		c.gridx = 1;
		c.gridy = 0;
		this.panelSimulation.add(this.jcbGridNodePercent, c);

		// Bottom Panel
		JPanel bottom = new JPanel(new FlowLayout());
		this.buttonCancel = new JButton("Cancel");
		this.buttonCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				buttonAction(false);
			}
		});
		bottom.add(this.buttonCancel);

		this.buttonOK = new JButton("OK");
		this.buttonOK.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				buttonAction(true);
			}
		});
		bottom.add(this.buttonOK);
		this.add(bottom, BorderLayout.SOUTH);
	}

	private void buttonAction(boolean bIsOK) {
		this.bIsOK = bIsOK;
		this.dispose();
	}

	public boolean isDefined() {
		return this.bIsOK;
	}

	public String getNodePercent() {
		return ((GridNodePercent) this.jcbGridNodePercent.getSelectedItem()).toString();
	}

	@Override
	public void focusComponentOnLoad() {
		// TODO Auto-generated method stub
	}
}
