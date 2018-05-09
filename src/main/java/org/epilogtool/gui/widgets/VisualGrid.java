package org.epilogtool.gui.widgets;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;

import org.epilogtool.common.Tuple2D;
import org.epilogtool.core.topology.Topology;

public abstract class VisualGrid extends JPanel {
	private static final long serialVersionUID = 6126822003689575762L;

	protected final BasicStroke strokeBasic = new BasicStroke(1.0f);
	protected final BasicStroke strokePerturb = new BasicStroke(3.0f);
	protected final BasicStroke strokeRect = new BasicStroke(4.0f);

	protected int gridX;
	protected int gridY;
	protected Topology topology;
	protected double radius;
	protected Tuple2D<Integer> mouseGrid;

	public VisualGrid(int gridX, int gridY, Topology topology) {
		this.gridX = gridX;
		this.gridY = gridY;
		this.topology = topology;
		this.setSize(800, 450);
		this.mouseGrid = new Tuple2D<Integer>(-1, -1);
	}

	protected boolean isInGrid(Tuple2D<Integer> pos) {
		return (pos != null && pos.getX() >= 0 && pos.getX() < this.gridX
				&& pos.getY() >= 0 && pos.getY() < this.gridY);
	}

	protected void highlightCellsOverRectangle(Tuple2D<Integer> init,
			Tuple2D<Integer> end, Color c) {
		if (!isInGrid(init) || !isInGrid(end))
			return;
		Graphics g = this.getGraphics();
		Graphics2D g2 = (Graphics2D) g;
		this.paintComponent(g);

		Tuple2D<Integer> tMin = init.getMin(end);
		Tuple2D<Integer> tMax = init.getMax(end);

		for (int x = tMin.getX(); x <= tMax.getX(); x++) {
			for (int y = tMin.getY(); y <= tMax.getY(); y++) {
				Tuple2D<Double> center = topology.getPolygonCenter(this.radius,
						x, y);
				Polygon polygon = topology
						.createNewPolygon(this.radius, center);
				this.paintPolygon(this.strokeBasic, c, polygon, g2);
			}
		}
	}

	protected void applyRectangleOnCells(Tuple2D<Integer> init,
			Tuple2D<Integer> end) {
		if (!isInGrid(init) || !isInGrid(end)) {
			this.paintComponent(this.getGraphics());
			return;
		}
		Tuple2D<Integer> min = init.getMin(end);
		Tuple2D<Integer> max = init.getMax(end);
		for (int x = min.getX(); x <= max.getX(); x++) {
			for (int y = min.getY(); y <= max.getY(); y++) {
				this.applyDataAt(x, y);
			}
		}
		this.paintComponent(this.getGraphics());
	}

	protected void paintCellAt(Tuple2D<Integer> pos) {
		// Get selected cell grid XY
		if (!this.isInGrid(pos))
			return;

		this.applyDataAt(pos.getX(), pos.getY());
		this.paintComponent(this.getGraphics());
	}

	protected abstract void applyDataAt(int x, int y);

	public void applyDataToAll() {
		for (int x = 0; x < this.gridX; x++) {
			for (int y = 0; y < this.gridY; y++) {
				this.applyDataAt(x, y);
			}
		}
		this.paintComponent(this.getGraphics());
	}

	protected void mousePosition2Grid(MouseEvent e) {
		this.mouseGrid = this.topology.getSelectedCell(this.radius, e.getX(),
				e.getY());
	}

	public abstract void paintComponent(Graphics g);

	protected void paintPolygon(BasicStroke stroke, Color color,
			Polygon polygon, Graphics2D g) {
		g.setStroke(stroke);
		g.setColor(color);
		g.fillPolygon(polygon);
		g.setColor(Color.black);
		g.drawPolygon(polygon);
	}
}