package org.ginsim.epilog.gui.widgets;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import org.ginsim.epilog.core.Epithelium;

public class GridPanel extends JPanel {
	private static final long serialVersionUID = 6126822003689575762L;

	private int x;
	private int y;
	private Epithelium epi;
	private double height;
	private double radius;

	/**
	 * Generates the hexagons grid.
	 * 
	 * @param mainframe
	 *            related mainframe
	 * 
	 * @see pt.igc.nmd.epilog.gui.MainFrame mainFrame
	 */
	public GridPanel(Epithelium epi) {
		this.x = epi.getEpitheliumGrid().getX();
		this.y = epi.getEpitheliumGrid().getY();
		this.epi = epi;
		this.setBorder(BorderFactory.createTitledBorder("GridPanel"));
		this.setPreferredSize(new Dimension(500, 450));
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

		int max = 0;
		try {
			max = Math.max(this.x, this.y);

		} catch (NumberFormatException e) {
			System.out
					.println("java.lang.NumberFormatException: For input string: ''");

		} catch (Exception e) {

			e.printStackTrace();
		}

		if (this.x > 0 && this.y > 0) {

			if (this.y == max) {

				height = 500 / max;
				height = (500 - 1 * height) / (max);
				radius = height / Math.sqrt(3.0);
			}

			else {
				radius = (500 / this.x) / 2;

				height = radius * Math.sqrt(3.0);
			}

			double x = 0, y = 0, centerX = 0, centerY = radius;
			double s = radius;
			double incX = Math.sqrt(Math.pow(s, 2) - Math.pow(s / 2, 2));

			for (int k = 0; k < this.x; k++) {
				for (int j = 0; j < this.y; j++) {
					if (j % 2 == 0)
						centerX = incX * (2 * k + 1);
					else
						centerX = 2 * incX * (k + 1);
					centerY = s * (1 + j * 1.5);

					Polygon hexagon = new Polygon();

					for (int i = 0; i < 6; i++) {

						x = centerX + radius
								* Math.cos(i * 2 * Math.PI / 6 + Math.PI / 6);
						y = centerY + radius
								* Math.sin(i * 2 * Math.PI / 6 + Math.PI / 6);
						hexagon.addPoint((int) (x), (int) (y));
					}

					BasicStroke selectedStroke = stroke;
					if (this.epi.getEpitheliumGrid().getPerturbation(k, j) != null) {
						selectedStroke = perturbedStroke;
					}
					this.paintHexagon(selectedStroke,
							this.epi.getCellColor(k, j, new ArrayList<String>()), hexagon, g2);

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
	// public void drawHexagon(int i, int j, Graphics g, Color color) {
	//
	// System.out.println("i: "+i+"j: "+j);
	//
	// Graphics2D g2 = (Graphics2D) g;
	// BasicStroke stroke = new BasicStroke(1.0f);
	// BasicStroke perturbedStroke = new BasicStroke(3.0f);
	//
	// if (color == null)
	// color = Color.white;
	//
	// double centerX = 0, centerY = 0, x = 0, y = 0;
	// double s = radius;
	// double incX = Math.sqrt(Math.pow(s, 2)-Math.pow(s/2, 2));
	//
	// if (j % 2 == 0)
	// centerX = incX*(2*i+1);
	// else
	// centerX = 2*incX*(i+1);
	// centerY=s*(1+j*1.5);
	//
	// Polygon polygon2 = new Polygon();
	//
	// for (int k = 0; k < 6; k++) {
	//
	// x = centerX + radius * Math.cos(k * 2 * Math.PI / 6+Math.PI / 6);
	// y = centerY + radius * Math.sin(k * 2 * Math.PI / 6+Math.PI / 6);
	// polygon2.addPoint((int) (x), (int) (y));
	// }
	//
	// if (this.mainFrame.isDrawingCells()) {
	// BasicStroke selectedStroke = stroke;
	// paintHexagons(stroke, color, polygon2, g2);
	//
	// } else if (this.mainFrame.isDrawingPerturbations()) {
	// BasicStroke selectedStroke = stroke;
	// color = color.white;
	// if (this.mainFrame.epithelium.isCellPerturbedDraw(instance)) {
	// selectedStroke = perturbedStroke;
	// if (this.mainFrame.epithelium.getActivePerturbation() != null)
	// color = this.mainFrame.epithelium
	// .getPerturbationColor(this.mainFrame.epithelium
	// .getActivePerturbation().toString());
	// }
	// paintHexagons(selectedStroke, color, polygon2, g2);
	//
	// } else {
	// if (this.mainFrame.epithelium.isCellPerturbed(instance)) {
	// BasicStroke selectedStroke = perturbedStroke;
	//
	// paintHexagons(selectedStroke, color, polygon2, g2);
	// }
	// }
	// }

	/**
	 * Paint a single Hexagon
	 * 
	 * @param stroke
	 *            thickness of the hexagon edges
	 * @param hexagon
	 *            graphics2d of the hexagons grid
	 * @param g
	 *            graphic (hexagons grid)
	 * @param color
	 *            color to paint the instance
	 * 
	 * @see drawHexagon(int instance, Graphics g, Color color)
	 */
	private void paintHexagon(BasicStroke stroke, Color color, Polygon hexagon, Graphics2D g) {
		g.setStroke(stroke);
		g.setColor(color);
		g.fillPolygon(hexagon);
		g.setColor(Color.black);
		g.drawPolygon(hexagon);
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

		int max = 0;
		try {
			max = Math.max(this.x, this.y);

		} catch (NumberFormatException e) {
			System.out
					.println("java.lang.NumberFormatException: For input string: ''");

		} catch (Exception e) {

			e.printStackTrace();
		}

		if (this.x > 0 && this.y > 0) {

			if (this.y == max) {

				height = 500 / max;
				height = (500 - 1 * height) / (max);
				radius = height / Math.sqrt(3.0);
			}

			else {
				radius = (500 / this.x) / 2;

				height = radius * Math.sqrt(3.0);
			}

			double x = 0, y = 0, centerX = 0, centerY = radius;

			super.paintComponent(g);
			g.setColor(Color.black);

			double s = radius;
			double incX = Math.sqrt(Math.pow(s, 2) - Math.pow(s / 2, 2));

			for (int k = 0; k < this.x; k++) {
				for (int j = 0; j < this.y; j++) {
					if (j % 2 == 0)
						centerX = incX * (2 * k + 1);
					else
						centerX = 2 * incX * (k + 1);
					centerY = s * (1 + j * 1.5);

					Polygon polygon2 = new Polygon();

					for (int i = 0; i < 6; i++) {

						x = centerX + radius
								* Math.cos(i * 2 * Math.PI / 6 + Math.PI / 6);
						y = centerY + radius
								* Math.sin(i * 2 * Math.PI / 6 + Math.PI / 6);
						polygon2.addPoint((int) (x), (int) (y));
					}

					paintHexagon(stroke, Color.white, polygon2, g2);

				}
			}
		} else {
			System.out.println("XX and YY must be bigger than zero");
		}
	}
}