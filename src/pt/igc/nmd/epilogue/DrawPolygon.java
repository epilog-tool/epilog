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
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.colomoto.logicalmodel.NodeInfo;

import pt.gulbenkian.igc.nmd.CellGenes;

public class DrawPolygon extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6126822003689575762L;

	private double height = 0.0, radius = 0.0;

	// private Color colors[] = { Color.orange, Color.green, Color.blue,
	// Color.pink, Color.yellow, Color.magenta, Color.cyan, Color.red,
	// Color.LIGHT_GRAY, Color.black };
	public List<NodeInfo> listNodes;
	// private Color background = Color.WHITE;
	// private MapColorPanel mpanel = null;
	private MainPanel mainPanel = null;
	public ArrayList<ArrayList<CellGenes>> cellGenes;
	public int countSelected = 0;
	public ArrayList<ArrayList<Cell>> cells = new ArrayList<ArrayList<Cell>>();

	public DrawPolygon() {

		this.setPreferredSize(new Dimension(500, 500));
		this.cellGenes = new ArrayList<ArrayList<CellGenes>>();

	}

	public void paintComponent(Graphics g) {

		int XX = 0, YY = 0, max = 0;
		try {
			XX = StartPanel.getGridWidth();
			YY = StartPanel.getGridHeight();
			max = Math.max(XX, YY);

		} catch (NumberFormatException e) {
			System.out
					.println("java.lang.NumberFormatException: For input string: ''");

		} catch (Exception e) {

			e.printStackTrace();
		}

		if (XX > 0 && YY > 0) {

			if (YY == max) {

				height = 500 / max;
				height = (500 - 1 * height) / (max);
				radius = height / Math.sqrt(3.0);

			}

			else {
				radius = (500 / XX) / 2;

				height = radius * Math.sqrt(3.0);
			}

			double x = 0, y = 0, centerX = radius, centerY = 0;

			for (int k = 0; k < XX; k++) {
				g.setColor(Color.black);
				if (k == 0)
					centerX = radius;
				else
					centerX = (1.5 * radius * (k)) + radius;
				if (k % 2 == 0)
					centerY = radius * Math.sqrt(3.0) / 2;// h
				else
					centerY = radius * Math.sqrt(3.0);// h+h/2
				for (int j = 0; j < YY; j++) {

					Polygon polygon2 = new Polygon();

					for (int i = 0; i < 6; i++) {

						x = centerX + radius * Math.cos(i * 2 * Math.PI / 6);
						y = centerY + radius * Math.sin(i * 2 * Math.PI / 6);
						polygon2.addPoint((int) (x), (int) (y));

					}

					if (1 == 1) {
						g.setColor(Color.white);
						g.fillPolygon(polygon2);
						g.setColor(Color.black);
						g.drawPolygon(polygon2);

					}

					if (k % 2 == 0)
						centerY = (j + 1 + 0.5) * radius * Math.sqrt(3.0);
					else
						centerY = (j + 2) * radius * Math.sqrt(3.0);

				}

			}
		} else {
			System.out.println("XX e YY têm que ser maiores do que zero");

		}

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
				System.out.println("size " + cellGenes.get(i).size());
			}

		}

	}

}