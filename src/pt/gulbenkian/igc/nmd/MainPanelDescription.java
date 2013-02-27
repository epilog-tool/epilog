package pt.gulbenkian.igc.nmd;

import java.awt.Color;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class MainPanelDescription {

	public static String optionString;
	static JPanel optionsRunPanel = new JPanel();
	static JPanel optionsStartPanel = new JPanel();
	static JPanel iterationNumberPanel= new JPanel();
	static JPanel RollOverPanel;
	public final static JTextField inputState = new JTextField("Initial State");;

	public static void paintHexagons() {
		MainPanel.hexagonsPanel.cellGenes.clear();
		DrawPolygon hexagonsPanel = MainPanel.hexagonsPanel;

		hexagonsPanel.paintComponent(hexagonsPanel.getGraphics());
	}



	public static void cleanRollOverPanel() {

		RollOverPanel.removeAll();
		RollOverPanel.revalidate();
		RollOverPanel.repaint();
	}

	public static void cleanOptionsRunPanel() {

		optionsRunPanel.removeAll();
		optionsRunPanel.revalidate();
		optionsRunPanel.repaint();

	}

	public static void cleanOptionsStartPanel() {

		optionsStartPanel.removeAll();
		optionsStartPanel.revalidate();
		optionsStartPanel.repaint();

	}
	public static void cleanButtonPanel() {
		MapColorPanel buttonPanel = MainPanel.buttonPanel;
		buttonPanel.removeAll();
		buttonPanel.revalidate();
		buttonPanel.repaint();
	}
	public static JButton optionchanger(String option) {
		final JButton btnOption = new JButton("" + option);
		btnOption.setBounds(20, 13, 130, 30);
		btnOption.setText(option);
		optionsRunPanel.add(btnOption);
		optionsRunPanel.repaint();
		MainPanel.contentPanel.repaint();
		return btnOption;
	}

	public static String optionDelivery(String option) {
		String optionDelivered = null;
		if ((option.equalsIgnoreCase("Run"))) {
	//		Simulation.runPushed();
			
			optionDelivered = "Stop";
			cleanOptionsRunPanel();
			JButton btnOption = optionchanger(optionDelivered);
			btnOption.removeActionListener(null);
			btnOption.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {

					String optionIterative = optionDelivery("Stop");
					
				}
			});
			optionsRunPanel.add(btnOption);

		} else if (option.equalsIgnoreCase("Stop")) {
		//	Simulation.stopPushed();
			optionDelivered = "Continue";
			JButton btnOption = optionchanger(optionDelivered);
			cleanOptionsRunPanel();
			btnOption.removeActionListener(null);
			btnOption.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {

					String optionIterative = optionDelivery("Continue");
				}
			});
			optionsRunPanel.add(btnOption);

		} else if (option == "Continue") {
	//		Simulation.continuePushed();
			optionDelivered = "Run";
			JButton btnOption = optionchanger(optionDelivered);
			;
			cleanOptionsRunPanel();
			btnOption.removeActionListener(null);
			btnOption.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {

					String optionIterative = optionDelivery("Run");
				}
			});
			optionsRunPanel.add(btnOption);

		}

		return optionDelivered;
	}

	public static String setupRollOverPanel() {

		RollOverPanel = new JPanel();
		RollOverPanel.setBounds(850, 10, 280, 40);
		RollOverPanel.setLayout(null);
		RollOverPanel.setBackground(Color.white);
		JComboBox rollOver = new JComboBox();
		rollOver.addItem("No Roll-Over");
		rollOver.addItem("Vertical Roll-Over");
		rollOver.addItem("Horizontal Roll-Over");

		inputState.setHorizontalAlignment(JTextField.CENTER);
		rollOver.setBackground(Color.white);
		rollOver.setBounds(20, 10, 180, 20);
		RollOverPanel.add(rollOver);

		MainPanel.contentPanel.add(RollOverPanel);

		rollOver.addActionListener(new ActionListener() {

			
			@Override
			public void actionPerformed(ActionEvent event) {
				JComboBox source = (JComboBox) event.getSource();
				String optionString = (String) source.getSelectedItem();
				setRollOver(optionString);
				System.out.println(optionString);
				Neighbours.oneDistanceNeighbours(30);
			}
		});
		return optionString;
	}

	public static void setupOptionsRunPanel() {
		JButton btnRunOption = new JButton();
		optionsRunPanel.setBackground(Color.white);
		optionsRunPanel.setBounds(500, 0, 150, 70);
		optionsRunPanel.setLayout(null);
		MainPanel.contentPanel.add(optionsRunPanel);
		btnRunOption = optionchanger("Run");
		btnRunOption.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String optionIterative = optionDelivery("Run");
			}
		});
		optionsRunPanel.add(btnRunOption);
	}

	/* Start -Next */

	public static JButton optionStartchanger(String option) {
		final JButton btnStartOption = new JButton("" + option);
		btnStartOption.setBounds(20, 13, 130, 30);
		btnStartOption.setText(option);
		optionsStartPanel.add(btnStartOption);
		optionsStartPanel.repaint();
		MainPanel.contentPanel.repaint();
		return btnStartOption;
	}

	public static String optionStartDelivery(String option) {
		String optionStartDelivered = null;
		if (option.equalsIgnoreCase("Step-by-Step")) {
		//	Simulation.stepbystepPushed();
			optionStartDelivered = "Step-by-Step";
			cleanOptionsStartPanel();
			JButton btnStartOption = optionStartchanger(optionStartDelivered);
			btnStartOption.removeActionListener(null);
			btnStartOption.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					String optionStartIterative = optionStartDelivery("Step-by-Step");
				}
			});
			optionsStartPanel.add(btnStartOption);
		}
//		}
//	else if (option.equalsIgnoreCase("Next")) {
//			optionStartDelivered = "Start";
//			JButton btnStartOption = optionchanger(optionStartDelivered);
//			cleanOptionsStartPanel();
//			btnStartOption.removeActionListener(null);
//			btnStartOption.addActionListener(new ActionListener() {
//				public void actionPerformed(ActionEvent e) {
//					String optionStartIterative = optionStartDelivery("Start");
//				}
//		}
//);
//		optionsStartPanel.add(btnStartOption);
//
//	}

		return optionStartDelivered;
	}

	public static void setupOptionsStartPanel() {
		JButton btnStartOption = new JButton();
		optionsStartPanel.setBackground(Color.white);
		optionsStartPanel.setBounds(650, 0, 150, 70);
		optionsStartPanel.setLayout(null);
		MainPanel.contentPanel.add(optionsStartPanel);
		btnStartOption = optionStartchanger("Step-by-Step");
		btnStartOption.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String optionStartIterative = optionStartDelivery("Step-by-Step");

			}
		});

		optionsStartPanel.add(btnStartOption);
	}
	static String roll;
	public static void setRollOver(String rollOver) {
		roll = rollOver;
	}
	public static String getRollOver() {
		return roll;
	}
	
	/*
	 * IterationNumberPanel
	 */
	
	public static void setupIterationNumberPanel() {
		JTextField iterationNumberText = new JTextField();
		
		
		iterationNumberPanel.removeAll();
		iterationNumberPanel.setBackground(Color.white);
		iterationNumberPanel.setBounds(800, 0, 90, 70);
		iterationNumberPanel.setLayout(null);
		iterationNumberText.setBounds(10,15,30,30);
		iterationNumberText.setBackground(Color.white);
		MainPanel.contentPanel.add(iterationNumberPanel);
//		int iterationNumber = Simulation.getIterationNumber();
//		System.out.println("getIterationNumber"+iterationNumber);
//		iterationNumberText.setText(""+iterationNumber);

		iterationNumberPanel.add(iterationNumberText);
		MainPanel.contentPanel.add(iterationNumberPanel);
		
	}



	public static void cleanIterationNumberPanel() {
		iterationNumberPanel.removeAll();
		iterationNumberPanel.revalidate();
		optionsStartPanel.repaint();
		
	}


	
}
