package org.ginsim.epilog.gui.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import org.ginsim.epilog.core.topology.RollOver;
import org.ginsim.epilog.gui.color.ColorUtils;

public class DialogNewEpithelium extends JPanel {
	private static final long serialVersionUID = 1877338344309723137L;

	private final int DEFAULT_WIDTH = 20;

	private JTextField jtfEpiName;
	private JComboBox<String> jcbSBMLs;
	private List<String> listSBMLs;
	private List<String> listEpiNames;
	private JButton buttonCancel;
	private JButton buttonOK;
	private JComboBox<RollOver> jcbRollover;

	private boolean bIsOK;

	public DialogNewEpithelium(Set<String> lSBMLs, List<String> lEpiNames) {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.listSBMLs = new ArrayList<String>(lSBMLs);
		this.listSBMLs.add(0, "---");
		this.listEpiNames = lEpiNames;

		// Name chooser
		JPanel jpName = new JPanel(new BorderLayout());
		jpName.add(new JLabel("Name"), BorderLayout.LINE_START);
		jtfEpiName = new JTextField(DEFAULT_WIDTH);
		jtfEpiName.addKeyListener(new KeyListener() {
			@Override
			public void keyReleased(KeyEvent e) {
				validateDialog();
			}

			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyPressed(KeyEvent e) {
			}
		});
		jpName.add(jtfEpiName, BorderLayout.CENTER);
		this.add(jpName);

		// SBML chooser
		JPanel jpSBML = new JPanel(new BorderLayout());
		jpSBML.add(new JLabel("SBML"), BorderLayout.LINE_START);
		jcbSBMLs = new JComboBox<String>(
				this.listSBMLs.toArray(new String[this.listSBMLs.size()]));
		jcbSBMLs.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				validateDialog();
			}
		});
		jpSBML.add(jcbSBMLs, BorderLayout.CENTER);
		this.add(jpSBML);

		// Rollover chooser
		JPanel jpRollover = new JPanel(new BorderLayout());
		jpRollover.add(new JLabel("Rollover"), BorderLayout.LINE_START);
		jcbRollover = new JComboBox<RollOver>(new RollOver[] {
				RollOver.NOROLLOVER, RollOver.HORIZONTAL, RollOver.VERTICAL });
		jpRollover.add(jcbRollover, BorderLayout.CENTER);
		this.add(jpRollover);

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
		this.add(bottom);
		this.validateDialog();
	}

	public String getEpiName() {
		return this.jtfEpiName.getText();
	}

	public String getSBMLName() {
		return (String) this.jcbSBMLs.getSelectedItem();
	}
	
	public RollOver getRollOver() {
		return (RollOver) this.jcbRollover.getSelectedItem();
	}

	private boolean validateTextField() {
		boolean valid = true;
		if (this.listEpiNames.contains(this.jtfEpiName.getText())) {
			valid = false;
		}
		if (!valid) {
			this.jtfEpiName.setBackground(ColorUtils.LIGHT_RED);
		} else {
			this.jtfEpiName.setBackground(Color.WHITE);
		}
		return valid;
	}

	private boolean validateComboBox() {
		boolean valid = true;
		if (this.jcbSBMLs.getSelectedIndex() == 0) {
			valid = false;
		}
		System.out.println("jcb: " + this.jcbSBMLs.getSelectedIndex()
				+ " valid:" + valid);
		return valid;
	}

	private void buttonAction(boolean bIsOK) {
		this.bIsOK = bIsOK;
		if (bIsOK && !this.validateDialog()) {
			return;
		}
		Window win = SwingUtilities.getWindowAncestor(this);
		if (win != null) {
			win.dispose();
		}
	}

	public boolean isDefined() {
		return this.bIsOK;
	}

	private boolean validateDialog() {
		boolean isValid = false;
		if (this.validateTextField() && this.validateComboBox()) {
			isValid = true;
		}
		System.out.println("dialog: " + isValid);
		this.buttonOK.setEnabled(isValid);
		return isValid;
	}
}
