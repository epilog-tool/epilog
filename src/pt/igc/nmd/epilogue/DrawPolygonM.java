package pt.igc.nmd.epilogue;

import java.awt.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.*;

//import org.colomoto.logicalmodel.NodeInfo;

import java.util.ArrayList;
//import java.util.List;

public class DrawPolygonM extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public double height = 0.0;
	public  static double radius = 0.0;
	public ArrayList<ArrayList<Cell>> cells = new ArrayList<ArrayList<Cell>>();
	public int startX;
	public int startY;
	public int endX;
	public int endY;
	public  Map frame;
	public MainPanel mainPanel;

	public DrawPolygonM(Map frame, int width, int height, String nodeId,
			int maxId, MainPanel mainPanel) {

		this.setBackground(Color.white);
		this.setPreferredSize(new Dimension(600, 700));
		frame.setTitle(nodeId);
		frame.setBackground(Color.white);
		this.mainPanel = mainPanel;
		Container contentPane = frame.getContentPane();
		this.frame = frame;
		contentPane.add(this);

	}

	public static void drawHexagon(int i, int j, Graphics g, Color c) {
		double centerX = 100, centerY = 0, x = 0, y = 0;

		if (i % 2 == 0) {
			centerX = (1.5 * radius * (i)) + radius;
			centerY = (j) * radius * Math.sqrt(3.0) + radius * Math.sqrt(3.0)
					/ 2;
		} else {
			centerX = (1.5 * radius * (i)) + radius;
			centerY = (j) * radius * Math.sqrt(3.0) + radius * Math.sqrt(3.0);
		}

		Polygon polygon2 = new Polygon();

		for (int k = 0; k < 6; k++) {

			x = centerX + radius * Math.cos(k * 2 * Math.PI / 6);
			y = centerY + radius * Math.sin(k * 2 * Math.PI / 6);
			polygon2.addPoint((int) (x), (int) (y));

		}

		g.setColor(c);
		g.fillPolygon(polygon2);
		g.setColor(Color.black);
		g.drawPolygon(polygon2);

	}

	public  void clearHexagon(int i, int j, Graphics g) {
		double centerX = 0, centerY = 0, x = 0, y = 0;

		if (i % 2 == 0) {
			centerX = (1.5 * radius * (i)) + radius;
			centerY = (j) * radius * Math.sqrt(3.0) + radius * Math.sqrt(3.0)
					/ 2;
		} else {
			centerX = (1.5 * radius * (i)) + radius;
			centerY = (j) * radius * Math.sqrt(3.0) + radius * Math.sqrt(3.0);
		}

		Polygon polygon2 = new Polygon();

		for (int k = 0; k < 6; k++) {

			x = centerX + radius * Math.cos(k * 2 * Math.PI / 6);
			y = centerY + radius * Math.sin(k * 2 * Math.PI / 6);
			polygon2.addPoint((int) (x), (int) (y));

		}

		g.setColor(Color.white);
		g.fillPolygon(polygon2);
		g.setColor(Color.black);
		g.drawPolygon(polygon2);

	}

	public void paintComponent(Graphics g) {

		int XX = 0, YY = 0, max = 0;
		try {
			XX = StartPanel.getGridWidth();
			YY = StartPanel.getGridHeight();
			max = Math.max(XX, YY);
			System.out.println("max " + max);
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
			//System.out.println("height: " + height);
			//System.out.println("radius: " + radius);
			double x = 0, y = 0, centerX = radius, centerY = 0;

			super.paintComponent(g);

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

					if (cells.size() > 0
							&& cells.get(k).get(j).color1.getRGB() != Color.white
									.getRGB()) {
						g.setColor(frame.color);
						g.fillPolygon(polygon2);
						g.setColor(Color.black);
						g.drawPolygon(polygon2);

					} else {
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

	public static ArrayList<ArrayList<Cell>> getMappedCells(String file_name) {
		ArrayList<ArrayList<Cell>> cells = new ArrayList<ArrayList<Cell>>();

		try {
			ObjectOutputStream oos = new ObjectOutputStream(
					new FileOutputStream(file_name, true));
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(
					file_name));

			cells = (ArrayList<ArrayList<Cell>>) ois.readObject();
			// frame.mainPanel.cells=cells;
			// frame.mainPanel.hexagonsPanel.paintComponent(frame.mainPanel.hexagonsPanel.getGraphics());
		} catch (FileNotFoundException e) { // TODO Auto-generated catch block

		} catch (IOException e) { // TODO Auto-generated catch block

		} catch (ClassNotFoundException e) { // TODO Auto-generated catch block

		}

		finally {
			return cells;

		}
	}

}