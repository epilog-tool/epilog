package pt.igc.nmd.epilogue;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.ItemSelectable;
import java.awt.Polygon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.colomoto.logicalmodel.NodeInfo;

public class DrawPolygon extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6126822003689575762L;

	private double height = 0.0, radius = 0.0;

	private Color colors[] = { Color.orange, Color.green, Color.blue,
			Color.pink, Color.yellow, Color.magenta, Color.cyan, Color.red,
			Color.LIGHT_GRAY, Color.black };

	private Color background = Color.WHITE;
	private MapColorPanel mpanel = null;
	private MainPanel mainPanel = null;
	public ArrayList<ArrayList<CellGenes>> cellGenes;
	public int countSelected = 0;
	public ArrayList<ArrayList<Cell>> cells = new ArrayList<ArrayList<Cell>>();

	public DrawPolygon(MainPanel frame) {

		this.mainPanel = frame;
		this.setBackground(background);
		this.setPreferredSize(new Dimension(500, 500));
		frame.setBackground(Color.white);
		this.cellGenes = new ArrayList<ArrayList<CellGenes>>();
		Container contentPane = this.mainPanel.getContentPane();
		contentPane.add(this);

	}



	public void drawHexagon(int i, int j, Graphics g) {
		double centerX = 0, centerY = 0, x = 0, y = 0;

		if (i % 2 == 0) {
			centerX = (1.5 * radius * (i)) + radius;
			centerY = (j) * radius * Math.sqrt(3.0) + radius * Math.sqrt(3.0)
					/ 2;
		} else {
			centerX = (1.5 * radius * (i)) + radius;
			centerY = (j) * radius * Math.sqrt(3.0) + radius * Math.sqrt(3.0);
		}

		Polygon polygon = new Polygon();

		for (int k = 0; k < 6; k++) {

			x = centerX + radius * Math.cos(k * 2 * Math.PI / 6);
			y = centerY + radius * Math.sin(k * 2 * Math.PI / 6);
			polygon.addPoint((int) (x), (int) (y));

		}

		g.fillPolygon(polygon);
	}

	public void clearHexagon(int i, int j, Graphics g) {
		double centerX = 0, centerY = 0, x = 0, y = 0;

		if (i % 2 == 0) {
			centerX = (1.5 * radius * (i)) + radius;
			centerY = (j) * radius * Math.sqrt(3.0) + radius * Math.sqrt(3.0)
					/ 2;
		} else {
			centerX = (1.5 * radius * (i)) + radius;
			centerY = (j) * radius * Math.sqrt(3.0) + radius * Math.sqrt(3.0);
		}

		Polygon polygon = new Polygon();

		for (int k = 0; k < 6; k++) {

			x = centerX + radius * Math.cos(k * 2 * Math.PI / 6);
			y = centerY + radius * Math.sin(k * 2 * Math.PI / 6);
			polygon.addPoint((int) (x), (int) (y));

		}

		g.fillPolygon(polygon);
		g.drawPolygon(polygon);

	}

	
	public void initializeCellGenes(int size) {
		
		cellGenes = new ArrayList<ArrayList<CellGenes>>();

		for (int i = 0; i < StartPanel.getGridWidth(); i++) {

			cellGenes.add(new ArrayList<CellGenes>());
			for (int j = 0; j < StartPanel.getGridHeight(); j++) {
				cellGenes.get(i).add(new CellGenes(size));
				 System.out.println("size "+cellGenes.get(i).size());
			}

		}

	}

}