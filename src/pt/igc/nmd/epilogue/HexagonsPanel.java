package pt.igc.nmd.epilogue;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class HexagonsPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static DrawPolygon hexagonsPanel = null;
	private MainPanel mainPanel = null;

	public HexagonsPanel() {
		init();

	}

	private JPanel init() {

		hexagonsPanel = new DrawPolygon(this.mainPanel);
		// hexagonsPanel.setBorder(BorderFactory.createLineBorder(Color.blue,5));
		// hexagonsPanel.setBackground(Color.cyan);
		hexagonsPanel.setBounds(10, 50, 500, 500);

		setLayout(null);
		return hexagonsPanel;
	}
}
