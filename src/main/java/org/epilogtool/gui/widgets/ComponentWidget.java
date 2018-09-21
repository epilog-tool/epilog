//package org.epilogtool.gui.widgets;
//
//import java.awt.Color;
//import java.awt.GridBagConstraints;
//import java.awt.GridBagLayout;
//
//import javax.swing.JButton;
//import javax.swing.JCheckBox;
//import javax.swing.JPanel;
//
//public class ComponentWidget extends JPanel {
//	private static final long serialVersionUID = 5908016417648767361L;
//
//	private String name;
//	private Color color;
//	private JCheckBox jCheckBox;
//	private JButton jbColor;
//	
//	private ComponentWidget(String name, Color c) {
//		this.name = name;
//		this.color = c;
//		
//		this.setLayout(new GridBagLayout());
//		GridBagConstraints gbc = new GridBagConstraints();
//		gbc.gridx = 0;
//		gbc.gridy = 0;
//		this.jCheckBox = new JCheckBox(name);
//		this.add(this.jCheckBox, gbc);
//		
//		gbc.gridx = 1;
//		gbc.gridy = 0;
//		this.jbColor = new JButton();
//		this.jbColor.setBackground(this.color);
//		this.add(this.jbColor, gbc);
//	}
//	
////	public static ComponentWidget getNew(String name, Color c) {
////		return new ComponentWidget(name, c);
////	}
////	
////	public String getName() {
////		return this.name;
////	}
////	public JCheckBox getCheckBox() {
////		return this.jCheckBox;
////	}
////	
////	public Color getColor() {
////		return this.color;
////	}
//}
