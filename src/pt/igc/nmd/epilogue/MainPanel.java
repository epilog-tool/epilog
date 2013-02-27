package pt.igc.nmd.epilogue;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;

public class MainPanel extends JFrame {

	/**
	 * 
	 */

	private StartPanel startPanel = null;
	public static HexagonsPanel hexagonsPanel = null;
	private InputPanel inputPanel = null;

	public MainPanel mainPanel = this;

	private static final long serialVersionUID = 1L;

	public void initialize() throws Exception {

		UIManager.setLookAndFeel(UIManager
				.getCrossPlatformLookAndFeelClassName());

		setupMainPanel();

	}

	public void setupMainPanel() {
		startPanel = new StartPanel();
		hexagonsPanel = new HexagonsPanel();
		inputPanel = new InputPanel();

		getContentPane().setPreferredSize(new Dimension(1100, 600));
		// contentPanel.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY,2));
		getContentPane().setBackground(Color.white);
		this.setResizable(true);

		JButton btnClose = new JButton("Close");
		btnClose.setBounds(900, 500, 100, 30);
		getContentPane().add(btnClose);
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});

		startPanel.setBounds(0, 0, 500, 40);
		getContentPane().setLayout(null);
		getContentPane().add(startPanel);
		getContentPane().add(hexagonsPanel);
		getContentPane().add(inputPanel);

		// Adding overall ScrollPane
		JScrollPane scrollPane = new JScrollPane(getContentPane());
		setContentPane(scrollPane);

		// House Keeping
		pack();
		setVisible(true);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}

}
