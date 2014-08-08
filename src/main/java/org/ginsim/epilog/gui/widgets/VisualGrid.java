package org.ginsim.epilog.gui.widgets;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;

import javax.swing.JPanel;

import org.ginsim.epilog.Tuple2D;
import org.ginsim.epilog.core.topology.Topology;

public abstract class VisualGrid extends JPanel {
	private static final long serialVersionUID = 6126822003689575762L;

	protected int gridX;
	protected int gridY;
	protected Topology topology;
	protected double radius;
	protected Tuple2D mouseGrid;
	protected final BasicStroke basicStroke;

	/**
	 * Generates the hexagons grid.
	 * 
	 * @param mainframe
	 *            related mainframe
	 * 
	 * @see pt.igc.nmd.epilog.gui.MainFrame mainFrame
	 */
	public VisualGrid(int gridX, int gridY, Topology topology) {
		this.basicStroke = new BasicStroke(1.0f);
		this.gridX = gridX;
		this.gridY = gridY;
		this.topology = topology;
		// this.setBorder(BorderFactory.createTitledBorder("GridPanel"));
		this.setSize(800, 450);
		this.mouseGrid = new Tuple2D(-1, -1);
	}

	public Tuple2D getMouseGrid() {
		if (this.mouseGrid.getX() < 0 || this.mouseGrid.getX() >= this.gridX
				|| this.mouseGrid.getY() < 0
				|| this.mouseGrid.getY() >= this.gridY)
			return null;
		return this.mouseGrid;
	}

	/**
	 * Paints the hexagons grid. If there is no model loaded it paints in white,
	 * otherwise with the resulting color of the selected components.
	 * 
	 * @param g
	 *            graphics
	 */
	public abstract void paintComponent(Graphics g);

	/**
	 * Paints all hexagons in white.
	 * 
	 * @param g
	 *            graphic (hexagons grid)
	 * 
	 * @see paintHexagons(BasicStroke stroke, Color color, Polygon polygon2,
	 *      Graphics2D g2)
	 */
	protected void paintPolygon(BasicStroke stroke, Color color,
			Polygon polygon, Graphics2D g) {
		g.setStroke(stroke);
		g.setColor(color);
		g.fillPolygon(polygon);
		g.setColor(Color.black);
		g.drawPolygon(polygon);
	}
}