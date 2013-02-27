package pt.igc.nmd.epilogue;




import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;





public class MainPanel extends JFrame {

	/**
	 * 
	 */
	
	private StartPanel startPanel = null;
	
	
	private static final long serialVersionUID = 1L;
	
	JPanel contentPanel = new JPanel();
	
	public void initialize() throws Exception {

		UIManager.setLookAndFeel(UIManager
				.getCrossPlatformLookAndFeelClassName());
		
		setupMainPanel();
		
		
	}

	public void setupMainPanel() {
		startPanel = new StartPanel();

		contentPanel.setPreferredSize(new Dimension(1100, 600));
		// contentPanel.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY,2));
		contentPanel.setBackground(Color.white);
		this.setResizable(true);
		

		JButton btnClose = new JButton("Close");
		btnClose.setBounds(900, 500, 100, 30);
		contentPanel.add(btnClose);
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		
		contentPanel.add(startPanel);
		//add.(hexagonsPanel);


		setContentPane(contentPanel);
		pack();
		setVisible(true);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}

	
}
