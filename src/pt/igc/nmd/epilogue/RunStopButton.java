package pt.igc.nmd.epilogue;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import pt.gulbenkian.igc.nmd.MainPanel;


public class RunStopButton extends JButton {

	public RunStopButton() {
		init();
	}

	public JButton init() {
		
		JButton btnRunOption = new JButton();

		btnRunOption = optionchanger("Run");
		btnRunOption.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String optionIterative = optionDelivery("Run");
			}
		});

		return btnRunOption;
	}

	public JButton optionchanger(String option) {
		final JButton btnOption = new JButton("" + option);
		setBounds(20, 13, 130, 30);
		setText(option);

		return btnOption;
	}

	public String optionDelivery(String option) {
		String optionDelivered = null;
		if ((option.equalsIgnoreCase("Run"))) {
			// Simulation.runPushed();

			optionDelivered = "Stop";

			JButton btnOption = optionchanger(optionDelivered);
			btnOption.removeActionListener(null);
			btnOption.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {

					String optionIterative = optionDelivery("Stop");

				}
			});

		} else if (option.equalsIgnoreCase("Stop")) {
			// Simulation.stopPushed();
			optionDelivered = "Run";

			JButton btnOption = optionchanger(optionDelivered);

			btnOption.removeActionListener(null);
			btnOption.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {

					String optionIterative = optionDelivery("Run");
				}
			});

		}

		return optionDelivered;

	}
}
