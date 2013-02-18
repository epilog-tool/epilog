package pt.gulbenkian.igc.nmd;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

public class MainPanelDescription {
	static JPanel optionsRunPanel = new JPanel();
	static JPanel optionsStartPanel = new JPanel();

	public static void paintHexagons() {

		DrawPolygon hexagonsPanel = MainPanel.hexagonsPanel;

		hexagonsPanel.paintComponent(hexagonsPanel.getGraphics());
	}

	public static void cleanButtonPanel() {
		MapColorPanel buttonPanel = MainPanel.buttonPanel;
		buttonPanel.removeAll();
		buttonPanel.revalidate();
		buttonPanel.repaint();
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
	
	public static JButton optionchanger(String option) {
		final JButton btnOption = new JButton("" + option);
		btnOption.setBounds(20, 13, 100, 30);
		btnOption.setText(option);
		optionsRunPanel.add(btnOption);
		optionsRunPanel.repaint();
		MainPanel.contentPanel.repaint();
		return btnOption;
	}

	public static String optionDelivery(String option) {
		String optionDelivered = null;
		if ((option.equalsIgnoreCase("Run"))) {
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

		} 
		else if (option == "Continue") {
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
		btnStartOption.setBounds(20, 13, 100, 30);
		btnStartOption.setText(option);
		optionsStartPanel.add(btnStartOption);
		optionsStartPanel.repaint();
		MainPanel.contentPanel.repaint();
		return btnStartOption;
	}

	public static String optionStartDelivery(String option) {
		String optionStartDelivered = null;
		if (option.equalsIgnoreCase("Start")) {
			optionStartDelivered = "Next";
			cleanOptionsStartPanel();
			JButton btnStartOption = optionStartchanger(optionStartDelivered);
			btnStartOption.removeActionListener(null);
			btnStartOption.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					String optionStartIterative = optionStartDelivery("Next");
				}
			});
			optionsStartPanel.add(btnStartOption);

		} else if (option.equalsIgnoreCase("Next")) {
			optionStartDelivered = "Start";
			JButton btnStartOption = optionchanger(optionStartDelivered);
			cleanOptionsStartPanel();
			btnStartOption.removeActionListener(null);
			btnStartOption.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					String optionStartIterative = optionStartDelivery("Start");
				}
			});
			optionsStartPanel.add(btnStartOption);

		} 
		

		return optionStartDelivered;
	}

	public static void setupOptionsStartPanel() {
		JButton btnStartOption = new JButton();
		optionsStartPanel.setBackground(Color.white);
		optionsStartPanel.setBounds(650, 0, 150, 70);
		optionsStartPanel.setLayout(null);
		MainPanel.contentPanel.add(optionsStartPanel);
		btnStartOption = optionStartchanger("Start");
		btnStartOption.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String optionStartIterative = optionStartDelivery("Start");
			}
		});
		optionsStartPanel.add(btnStartOption);
	}
	
	
	
}



