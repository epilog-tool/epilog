package org.ginsim.epilog.gui.widgets;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

public class SBMLPanel extends JPanel {
	private static final long serialVersionUID = -3644859105163906588L;
	
	private String name;
	private Color color;
	private JRadioButton jrButton;
	private JButton jbColor;
	
	private SBMLPanel(String name, Color c) {
		this.name = name;
		this.color = c;
		
		this.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		this.jrButton = new JRadioButton(name);
		this.add(this.jrButton, gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 0;
		this.jbColor = new JButton();
		this.jbColor.setBackground(this.color);
		this.add(this.jbColor, gbc);
	}
	
	public static SBMLPanel getNew(String name, Color c) {
		return new SBMLPanel(name, c);
	}
	
	public String getName() {
		return this.name;
	}
	public JRadioButton getRadioButton() {
		return this.jrButton;
	}
	
	public Color getColor() {
		return this.color;
	}
}
