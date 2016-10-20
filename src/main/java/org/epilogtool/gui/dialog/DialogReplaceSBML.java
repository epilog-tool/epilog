package org.epilogtool.gui.dialog;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.epilogtool.gui.color.ColorUtils;

public class DialogReplaceSBML extends EscapableDialog {
	private static final long serialVersionUID = 1877338344309723137L;


	private JComboBox jcbModelName;


	private List<String> listModelNames;
	private boolean bIsOK;
	private boolean bIsNameOK;

	private JButton buttonCancel;
	private JButton buttonOK;
	private String model;

	public DialogReplaceSBML(String model, List<String> modelNames) {
		this.listModelNames = modelNames;
		this.listModelNames.remove(model);
		this.model = model;

		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		// Name JLabel
		c.gridx = 0;
		c.gridy = 2;
		this.add(new JLabel("Replace: " + model + " with: "), c);

		// Name JComboBox
		String[] array = this.listModelNames.toArray(new String[this.listModelNames.size()]);
		this.jcbModelName = new JComboBox(array);
		
		this.jcbModelName.addKeyListener(new KeyListener() {
			@Override
			public void keyReleased(KeyEvent e) {
				validateTextField();
			}

			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyPressed(KeyEvent e) {
			}
		});
		c.gridx = 1;
		c.gridy = 2;
		this.add(this.jcbModelName, c);

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
		c.gridx = 0;
		c.gridy = 6;
		c.gridwidth = 2;
		this.add(bottom, c);

		this.validateTextField();

	}

	private void validateTextField() {
		this.bIsNameOK = false;
		
		List<String> listShortModelNames = new ArrayList<String>();
		for (String modelName:listModelNames){
			String newName = modelName.substring(0, modelName.length()-5);
			listShortModelNames.add(newName);
		}
		
		this.validateDialog();
	}

	private void buttonAction(boolean bIsOK) {
		this.bIsOK = bIsOK;
		if (bIsOK && !this.validateDialog()) {
			// Not valid
			return;
		}
		this.dispose();
	}

	public boolean isDefined() {
		return this.bIsOK;
	}

	private boolean validateDialog() {
		boolean isValid = bIsNameOK;
		this.buttonOK.setEnabled(isValid);
		return isValid;
	}

	@Override
	public void focusComponentOnLoad() {
		this.jcbModelName.requestFocusInWindow();
	}

	public String getModelName() {
		return this.jcbModelName.getName();
	}
}
