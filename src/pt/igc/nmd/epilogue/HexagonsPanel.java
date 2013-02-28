package pt.igc.nmd.epilogue;

import java.awt.Color;



import javax.swing.JPanel;


public class HexagonsPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static DrawPolygon hexagonsPanel = null;
	private MainPanel mainPanel = new MainPanel();

	public HexagonsPanel() {
		init();

	}

	private JPanel init() {

		hexagonsPanel = new DrawPolygon();
		// hexagonsPanel.setBorder(BorderFactory.createLineBorder(Color.blue,5));
		hexagonsPanel.setBackground(Color.red);

		setLayout(null);
		return hexagonsPanel;
	}
	
	
	public static void paintHexagons() {
		MainPanel.hexagonsPanel.cellGenes.clear();
		
		DrawPolygon hexagonsPanel = MainPanel.hexagonsPanel;

		hexagonsPanel.paintComponent(hexagonsPanel.getGraphics());
	}
}
