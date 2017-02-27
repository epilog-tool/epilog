package org.cellularasynchrony;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
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
	private MainFrame mainFrame;

	public int countSelected = 0;

	/**
	 * Generates the hexagons grid.
	 * 
	 * @param mainframe
	 *            related mainframe

	 * @see  pt.igc.nmd.epilog.gui.MainFrame mainFrame
	 */
	public DrawPolygon(MainFrame mainFrame) {
		this.mainFrame = mainFrame;
		this.setPreferredSize(new Dimension(450, 500));
	}

	/**
	 * Paints the hexagons grid. If there is no model loaded it paints in white,
	 * otherwise with the resulting color of the selected components.
	 * 
	 * @param g
	 *            graphics
	 */
	public void paintComponent(Graphics g) {

		Graphics2D g2 = (Graphics2D) g;
		BasicStroke stroke = new BasicStroke(1.0f);
		BasicStroke perturbedStroke = new BasicStroke(3.0f);

		int XX = 0, YY = 0, max = 0;
		try {
			XX = this.mainFrame.topology.getWidth();
			YY = this.mainFrame.topology.getHeight();

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

			super.paintComponent(g);
			g.setColor(Color.black);

			for (int k = 0; k < XX; k++) {

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

					int instance = this.mainFrame.topology
							.coords2Instance(k, j);
					if (this.mainFrame.epithelium.getUnitaryModel() == null) {
						paintHexagons(stroke, Color.white, polygon2, g2);
					}

					else {
						BasicStroke selectedStroke = stroke;
						if (this.mainFrame.epithelium.isCellPerturbed(instance)) {
							selectedStroke = perturbedStroke;
						}
						if (!mainFrame.isStochastic())
							paintHexagons(selectedStroke,
								this.mainFrame.simulation
										.getCoordinateCurrentColor(instance),
								polygon2, g2);
						if (mainFrame.isStochastic())
							paintHexagons(selectedStroke,
								this.mainFrame.stochasticSimulation
										.getCoordinateCurrentColor(instance),
								polygon2, g2);
					}

					if (k % 2 == 0)
						centerY = (j + 1 + 0.5) * radius * Math.sqrt(3.0);
					else
						centerY = (j + 2) * radius * Math.sqrt(3.0);
				}
			}
		} else {
			System.out.println("XX and YY must be bigger than zero");
		}
	}

	/**
	 * Paints an instance of the hexagons grid.
	 * 
	 * @param instance
	 *            instance to be painted
	 * @param g
	 *            graphic (hexagons grid)
	 * @param color
	 *            color to paint the instance
	 * 
	 * @see paintHexagons(BasicStroke stroke, Color color, Polygon polygon2,
	 *      Graphics2D g2)
	 */
	public void drawHexagon(int instance, Graphics g, Color color) {

		int i = this.mainFrame.topology.instance2i(instance);
		int j = this.mainFrame.topology.instance2j(instance);

		Graphics2D g2 = (Graphics2D) g;
		BasicStroke stroke = new BasicStroke(1.0f);
		BasicStroke perturbedStroke = new BasicStroke(3.0f);

		if (color == null)
			color = Color.white;

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

		if (this.mainFrame.isDrawingCells()) {
			BasicStroke selectedStroke = stroke;
			paintHexagons(stroke, color, polygon2, g2);

		} else if (this.mainFrame.isDrawingPerturbations()) {
			BasicStroke selectedStroke = stroke;
			color = color.white;
			if (this.mainFrame.epithelium.isCellPerturbedDraw(instance)) {
				selectedStroke = perturbedStroke;
				if (this.mainFrame.epithelium.getActivePerturbation() != null)
					color = this.mainFrame.epithelium
							.getPerturbationColor(this.mainFrame.epithelium
									.getActivePerturbation().toString());
			}
			paintHexagons(selectedStroke, color, polygon2, g2);

		} else {
			if (this.mainFrame.epithelium.isCellPerturbed(instance)) {
				BasicStroke selectedStroke = perturbedStroke;

				paintHexagons(selectedStroke, color, polygon2, g2);
			}
		}
	}

	/**
	 * Paints Hexagons.
	 * 
	 * @param stroke
	 *            thickness of the hexagon edges
	 * @param polygon2
	 *            graphics2d of the hexagons grid
	 * @param g
	 *            graphic (hexagons grid)
	 * @param color
	 *            color to paint the instance
	 * 
	 * @see drawHexagon(int instance, Graphics g, Color color)
	 */
	private void paintHexagons(BasicStroke stroke, Color color,
			Polygon polygon2, Graphics2D g2) {
		g2.setStroke(stroke);
		g2.setColor(color);
		g2.fillPolygon(polygon2);
		g2.setColor(Color.black);
		g2.drawPolygon(polygon2);
	}

	/**
	 * Paints all hexagons in white.
	 * 
	 * @param g
	 *            graphic (hexagons grid)
	 * 
	 * @see paintHexagons(BasicStroke stroke, Color color, Polygon polygon2,
	 *      Graphics2D g2)
	 */
	public void clearAllCells(Graphics g) {

		Graphics2D g2 = (Graphics2D) g;
		BasicStroke stroke = new BasicStroke(1.0f);

		int XX = 0, YY = 0, max = 0;
		try {
			XX = this.mainFrame.topology.getWidth();
			YY = this.mainFrame.topology.getHeight();

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

					paintHexagons(stroke, Color.white, polygon2, g2);

					if (k % 2 == 0)
						centerY = (j + 1 + 0.5) * radius * Math.sqrt(3.0);
					else
						centerY = (j + 2) * radius * Math.sqrt(3.0);
				}
			}
		} else {
			System.out.println("XX and YY must be bigger than zero");
		}
	}
	
	
	
	
	
	
	
}