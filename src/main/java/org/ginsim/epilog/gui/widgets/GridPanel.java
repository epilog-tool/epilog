package org.ginsim.epilog.gui.widgets;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import org.ginsim.epilog.core.Epithelium;
import org.ginsim.epilog.core.topology.Topology;

public class GridPanel extends JPanel {
	private static final long serialVersionUID = 6126822003689575762L;

	private int gridX;
	private int gridY;
	private Epithelium epi;

	/**
	 * Generates the hexagons grid.
	 * 
	 * @param mainframe
	 *            related mainframe
	 * 
	 * @see pt.igc.nmd.epilog.gui.MainFrame mainFrame
	 */
	public GridPanel(Epithelium epi) {
		this.gridX = epi.getEpitheliumGrid().getX();
		this.gridY = epi.getEpitheliumGrid().getY();
		this.epi = epi;
//		this.setBorder(BorderFactory.createTitledBorder("GridPanel"));
		this.setSize(500, 450);
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
		Topology topology = this.epi.getEpitheliumGrid().getTopology();

		double radius = topology.computeBestRadius(this.gridX, this.gridY,
				this.getSize().width, this.getSize().height);

		for (int x = 0; x < this.gridX; x++) {
			for (int y = 0; y < this.gridY; y++) {
				Polygon polygon = topology.createNewPolygon(radius, x, y);

				BasicStroke selectedStroke = stroke;
				if (this.epi.getEpitheliumGrid().getPerturbation(x, y) != null) {
					selectedStroke = perturbedStroke;
				}
				this.paintPolygon(selectedStroke,
						this.epi.getCellColor(x, y, new ArrayList<String>()),
						polygon, g2);
			}
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
	 * Paints all hexagons in white.
	 * 
	 * @param g
	 *            graphic (hexagons grid)
	 * 
	 * @see paintHexagons(BasicStroke stroke, Color color, Polygon polygon2,
	 *      Graphics2D g2)
	 */

	private void paintPolygon(BasicStroke stroke, Color color, Polygon polygon,
			Graphics2D g) {
		g.setStroke(stroke);
		g.setColor(color);
		g.fillPolygon(polygon);
		g.setColor(Color.black);
		g.drawPolygon(polygon);
	}
}