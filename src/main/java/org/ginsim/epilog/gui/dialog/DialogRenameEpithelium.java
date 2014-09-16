package org.ginsim.epilog.gui.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.ginsim.epilog.gui.color.ColorUtils;

public class DialogRenameEpithelium extends EscapableDialog {
	private static final long serialVersionUID = 1877338344309723137L;

	private final int COL_SIZE = 30;
	private JTextField jtfName;
	private JLabel errorMsg;
	private List<String> reservedNames;
	private String originalName;
	private JButton buttonCancel;
	private JButton buttonOK;

	private boolean bIsOK;

	public DialogRenameEpithelium(String originalName, List<String> reservedNames) {
		setLayout(new BorderLayout());
		this.reservedNames = reservedNames;
		this.originalName = originalName;
		
		// PAGE_START begin
		JPanel top = new JPanel(new FlowLayout());
		this.errorMsg = new JLabel("");
		this.add(top, BorderLayout.PAGE_START);
		
		// CENTER begin
		JPanel center = new JPanel(new FlowLayout());
		center.add(new JLabel("Name:"));
		this.jtfName = new JTextField();//COL_SIZE);
		this.jtfName.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				validateTextField(e);
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
			}
		});
		this.jtfName.setText(originalName);
		center.add(this.jtfName);
		this.add(center, BorderLayout.CENTER);

		// PAGE_END begin
		JPanel bottom = new JPanel(new FlowLayout());
		buttonCancel = new JButton("Cancel");
		buttonCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				buttonAction(false);
			}
		});
		bottom.add(buttonCancel);
		buttonOK = new JButton("OK");
		buttonOK.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				buttonAction(true);
			}
		});
		bottom.add(buttonOK);
		this.add(bottom, BorderLayout.PAGE_END);
		this.validateDialog();
	}

	private void validateTextField(KeyEvent e) {
		this.validateDialog();
	}

	public String getEpitheliumName() {
		return this.jtfName.getText();
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
		boolean isValid = true;
		String name = this.jtfName.getText();
		String msg = "";
		this.jtfName.setBackground(Color.WHITE);
		if (name.isEmpty()) {
			isValid = false;
		} else if (this.reservedNames.contains(name) && !this.originalName.equals(name)) {
			msg = "There is another epithelium with this name!";
			this.jtfName.setBackground(ColorUtils.LIGHT_RED);
			isValid = false;
		}
		this.buttonOK.setEnabled(isValid);
		this.errorMsg.setText(msg);
		return isValid;
	}
}
