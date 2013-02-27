package pt.igc.nmd.epilogue;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import pt.gulbenkian.igc.nmd.MainPanelDescription;

public class StartPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JButton restartButton;
	JButton modelButton;
	JButton textWButton;
	JButton textHButton;

	

	public StartPanel() {
		init();
		
	}

	private JPanel init() {

		restartButton = new JButton("Restart");
		modelButton = new JButton("Model");
		textWButton = new JButton("textW");
		textHButton = new JButton("textH");

		FlowLayout layout = new FlowLayout();
		layout.setAlignment(FlowLayout.LEFT);
		
		
		JButton btnRestart = new JButton("Restart");
		//btnRestart.setBounds(120, 13, 100, 30);
		System.out.print("restasdrt");
		btnRestart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.print("restart");
				//repaint();

//				if (MainPanelDescription.RollOverPanel != null)
//					MainPanelDescription.cleanRollOverPanel();
//				selectedFilenameLabel.setText("");
			}
		});

		setLayout(new FlowLayout());
		add(textWButton);
		add(textHButton);
		add(restartButton);
		add(modelButton);
		return this;
	}
	


}

