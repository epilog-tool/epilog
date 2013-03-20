package pt.igc.nmd.epilogue;







/*
 * This Class allows the change of colors of the genes in the grid. 
 * 
 * 
 * NOTE: 1) I still want to create a cancel button where the user can go back and not change colors
 * 		 2) Advanced option (something like more colors) where the user can define the colors with an RGB slider
 * 
 * 
 */

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class ColorFrame extends JFrame {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public JPanel contentPane;
	public JPanel Panel;
	public BtnChangeColor btt1;
	public BtnChangeColor btt2;
	public BtnChangeColor btt3;
	public BtnChangeColor btt4;
	public BtnChangeColor btt5;
	public BtnChangeColor btt6;
	public BtnChangeColor btt7;
	public BtnChangeColor btt8;
	public BtnChangeColor btt9;
	public BtnChangeColor btt10;
	public BtnChangeColor btt11;
	public BtnChangeColor btt12;
	public JButton buttonOk = new JButton("Ok");
	public JButton buttonCancel = new JButton("Cancel");
	public DrawPolygon dPanel;
	private String nodeID;

	//public ColorFrame(ColorButton colorb,DrawPolygon dPanel, String nodeID) {
	public ColorFrame(ColorButton colorb, String nodeID) {
		this.nodeID = nodeID;
		
		Color brown = new Color(165, 42, 42);
		Color oliveGreen = new Color(128, 128, 0);
		Color thistle = new Color(216, 191, 216);
		//Color lightSalmon = new Color(255, 160, 122);
		Color darkOrange = new Color(255, 140, 0);
		Color antiqueWhite = new Color(250, 235, 215);

		contentPane = new JPanel();
		btt1 = new BtnChangeColor(colorb, Color.yellow,nodeID);
		btt1.initialize();
		btt2 = new BtnChangeColor(colorb, darkOrange,nodeID);
		btt2.initialize();
		btt3 = new BtnChangeColor(colorb, Color.green,nodeID);
		btt3.initialize();
		btt4 = new BtnChangeColor(colorb, Color.cyan,nodeID);
		btt4.initialize();
		btt5 = new BtnChangeColor(colorb, Color.magenta,nodeID);
		btt5.initialize();
		btt6 = new BtnChangeColor(colorb, antiqueWhite,nodeID);
		btt6.initialize();
		btt7 = new BtnChangeColor(colorb, Color.red,nodeID);
		btt7.initialize();
		btt8 = new BtnChangeColor(colorb, Color.blue,nodeID);
		btt8.initialize();
		btt9 = new BtnChangeColor(colorb, Color.black,nodeID);
		btt9.initialize();

		btt10 = new BtnChangeColor(colorb, brown,nodeID);
		btt10.initialize();
		btt11 = new BtnChangeColor(colorb, oliveGreen,nodeID);
		btt11.initialize();
		btt12 = new BtnChangeColor(colorb, thistle,nodeID);
		btt12.initialize();
		//this.dPanel=dPanel;
	}

	public void initialize() {

		contentPane.setPreferredSize(new Dimension(220, 230));
		contentPane.setBorder(BorderFactory
				.createLineBorder(Color.DARK_GRAY, 2));
		contentPane.setBackground(Color.white);
		contentPane.setLayout(null);

		this.setResizable(false);

		setContentPane(contentPane);
		pack();
		// setLocationByPlatform(true);
		setVisible(true);
		setLocationRelativeTo(null);

		btt1.setBounds(20, 20, 30, 30);
		contentPane.add(btt1);
		btt2.setBounds(70, 20, 30, 30);
		contentPane.add(btt2);
		btt3.setBounds(120, 20, 30, 30);
		contentPane.add(btt3);
		btt4.setBounds(170, 20, 30, 30);
		contentPane.add(btt4);
		btt5.setBounds(20, 70, 30, 30);
		contentPane.add(btt5);
		btt6.setBounds(70, 70, 30, 30);
		contentPane.add(btt6);
		btt7.setBounds(120, 70, 30, 30);
		contentPane.add(btt7);
		btt8.setBounds(170, 70, 30, 30);
		contentPane.add(btt8);
		btt9.setBounds(20, 120, 30, 30);
		contentPane.add(btt9);
		btt10.setBounds(70, 120, 30, 30);
		contentPane.add(btt10);
		btt11.setBounds(120, 120, 30, 30);
		contentPane.add(btt11);
		btt12.setBounds(170, 120, 30, 30);
		contentPane.add(btt12);

		buttonOk.setBounds(20, 180, 80, 30);
		contentPane.add(buttonOk);
		buttonOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});

	}

}
