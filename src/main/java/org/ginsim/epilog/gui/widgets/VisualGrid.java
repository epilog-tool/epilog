package org.ginsim.epilog.gui.widgets;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;

import org.ginsim.epilog.Tuple2D;
import org.ginsim.epilog.core.topology.Topology;

public abstract class VisualGrid extends JPanel {
	private static final long serialVersionUID = 6126822003689575762L;

	protected final BasicStroke strokeBasic = new BasicStroke(1.0f);
	protected final BasicStroke strokePerturb = new BasicStroke(3.0f);
	protected final BasicStroke strokeRect = new BasicStroke(4.0f);

	protected int gridX;
	protected int gridY;
	protected Topology topology;
	protected double radius;
	protected Tuple2D mouseGrid;

	public VisualGrid(int gridX, int gridY, Topology topology) {
		this.gridX = gridX;
		this.gridY = gridY;
		this.topology = topology;
		// this.setBorder(BorderFactory.createTitledBorder("GridPanel"));
		this.setSize(800, 450);
		this.mouseGrid = new Tuple2D(-1, -1);
	}

	protected boolean isInGrid(Tuple2D pos) {
		return (pos != null && pos.getX() >= 0 && pos.getX() < this.gridX
				&& pos.getY() >= 0 && pos.getY() < this.gridY);
	}

	protected void drawRectangleOverCells(Tuple2D init, Tuple2D end, Color c) {
		if (!isInGrid(init) || !isInGrid(end))
			return;
		this.paintComponent(this.getGraphics());

		double incX = radius;
		double incY = radius * Math.sqrt(3) / 2;
		double initY, initX = incX + init.getX() * (3 * radius / 2);
		if (init.getX() % 2 == 0)
			initY = incY + init.getY() * 2 * incY;
		else
			initY = 2 * incY + init.getY() * 2 * incY;
		double finalY, finalX = incX + end.getX() * (3 * radius / 2);
		if (end.getX() % 2 == 0)
			finalY = incY + end.getY() * 2 * incY;
		else
			finalY = 2 * incY + end.getY() * 2 * incY;
		if (init.getX() == end.getX() && init.getY() == end.getY()) {
			initX -= incX / 10;
			initY -= incY / 10;
			finalY += incY / 10;
			finalX += incX / 10;
		}
		Polygon square = new Polygon();
		square.addPoint((int) initX, (int) initY);
		square.addPoint((int) initX, (int) finalY);
		square.addPoint((int) finalX, (int) finalY);
		square.addPoint((int) finalX, (int) initY);

		Graphics2D g = (Graphics2D) this.getGraphics();
		// Paint the rectangle
		g.setStroke(this.strokeRect);
		g.setColor(c);
		g.drawPolygon(square);
	}

	protected void paintCellsAtRectangle(Tuple2D init, Tuple2D end) {
		if (!isInGrid(init) || !isInGrid(end)) {
			this.paintComponent(this.getGraphics());
			return;
		}
		Tuple2D min = init.getMin(end);
		Tuple2D max = init.getMax(end);
		for (int x = min.getX(); x <= max.getX(); x++) {
			for (int y = min.getY(); y <= max.getY(); y++) {
				this.applyDataAt(x, y);
			}
		}
		this.paintComponent(this.getGraphics());
	}

	protected void paintCellAt(Tuple2D pos) {
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