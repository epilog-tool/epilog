package pt.igc.nmd.epilogue;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Polygon;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import org.colomoto.logicalmodel.NodeInfo;

public class DrawPolygon extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6126822003689575762L;

	public double height = 0.0, radius = 0.0;
	public List<NodeInfo> listNodes;
	private MainPanel mainPanel;
	public ArrayList<ArrayList<CellGenes>> cellGenes;
	public int countSelected = 0;
	public ArrayList<ArrayList<Cell>> cells = new ArrayList<ArrayList<Cell>>();
	private Color backgroundColor;

	public DrawPolygon(MainPanel mainPanel) {
		this.mainPanel = mainPanel;
		this.setPreferredSize(new Dimension(500, 500));
		this.cellGenes = new ArrayList<ArrayList<CellGenes>>();
		this.backgroundColor = mainPanel.backgroundColor;
	}

		
	public void paintComponent(Graphics g) {

		int XX = 0, YY = 0, max = 0;
		try {
			XX = this.mainPanel.getTopology().getWidth();
			YY = this.mainPanel.getTopology().getHeight();

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

					if (!mainPanel.getSimulation().getHasInitiated()) {
						g.setColor(Color.white); // Hexagons Color keep white white. Before Initialization
						g.fillPolygon(polygon2);
						g.setColor(Color.black);
						g.drawPolygon(polygon2);
				}

					else {
						g.setColor(mainPanel.getSimulation().Color(k,j));
						
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
		
	
		g.setColor(Color.white);
		g.fillPolygon(polygon);
		g.setColor(Color.black);
		g.drawPolygon(polygon);
	}

	public void clearAllCells(Graphics g) {

		int XX = 0, YY = 0, max = 0;
		try {
			XX = this.mainPanel.getTopology().getWidth();
			YY = this.mainPanel.getTopology().getHeight();

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

					if (mainPanel.getSimulation().getIterationNumber()==0) {
						g.setColor(Color.white);
						
				
						
						g.fillPolygon(polygon2);
						g.setColor(Color.black);
						g.drawPolygon(polygon2);
				}

					else {
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

	public void initializeCellGenes(int size) {

		cellGenes = new ArrayList<ArrayList<CellGenes>>();

		for (int i = 0; i < mainPanel.getTopology().getWidth(); i++) {

			cellGenes.add(new ArrayList<CellGenes>());
			for (int j = 0; j < mainPanel.getTopology().getHeight(); j++) {
				cellGenes.get(i).add(new CellGenes(size));
				
			}

		}

	}

	public void drawHexagon(int i, int j, Graphics g, Color color) {
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
		

		// Este set color deve ser calculado pelo conjunto de cores assinalados
		//Color color = initialConditionsColor(nodeBox, initialStatePerComponent );
		
		g.setColor(color);
		
		g.fillPolygon(polygon2);
		g.setColor(Color.black);
		g.drawPolygon(polygon2);

	}
	
	
}