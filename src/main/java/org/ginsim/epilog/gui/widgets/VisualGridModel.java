package org.ginsim.epilog.gui.widgets;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Map;

import org.colomoto.logicalmodel.LogicalModel;
import org.ginsim.epilog.ProjectModelFeatures;
import org.ginsim.epilog.Tuple2D;
import org.ginsim.epilog.core.topology.Topology;

public class VisualGridModel extends VisualGrid {
	private static final long serialVersionUID = -8878704517273291774L;

	private LogicalModel[][] modelGridClone;
	private Map<LogicalModel, Color> colorMapClone;
	private ProjectModelFeatures modelFeatures;
	private String selModelName;
	private boolean isRectFill;
	private Tuple2D initialRectPos;

	public VisualGridModel(int gridX, int gridY, Topology topology,
			LogicalModel[][] modelGridClone, Map<LogicalModel, Color> colorMapClone, ProjectModelFeatures modelFeatures) {
		super(gridX, gridY, topology);
		this.modelGridClone = modelGridClone;
		this.colorMapClone = colorMapClone;
		this.modelFeatures = modelFeatures;
		this.selModelName = null;
		this.isRectFill = false;
		this.initialRectPos = null;

		this.addMouseMotionListener(new MouseMotionListener() {
			@Override
			public void mouseMoved(MouseEvent e) {
				mousePosition2Grid(e);
			}

			@Override
			public void mouseDragged(MouseEvent e) {
				mousePosition2Grid(e);
				if (isRectFill) {
					paintMouseRectangle();
				} else {
					paintClickedCell();
				}
			}
		});
		this.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if (isRectFill) {
					paintSelectedRectangleCells();
				}
			}

			@Override
			public void mousePressed(MouseEvent e) {
				if (isRectFill) {
					initialRectPos = new Tuple2D(mouseGrid.getX(), mouseGrid
							.getY());
				} else {
					paintClickedCell();
				}
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseClicked(MouseEvent e) {
			}
		});
	}

	private boolean isInGrid(Tuple2D pos) {
		return (pos != null && pos.getX() >= 0 && pos.getX() < this.gridX
				&& pos.getY() >= 0 && pos.getY() < this.gridY);
	}

	private void paintMouseRectangle() {
		if (!isInGrid(this.initialRectPos) || !isInGrid(this.mouseGrid))
			return;
		double incX = radius;
		double incY = radius * Math.sqrt(3) / 2;
		double initY, initX = incX + this.initialRectPos.getX() * (3 * radius / 2);
		if (this.initialRectPos.getX() % 2 == 0)
			initY = incY + this.initialRectPos.getY() * 2 * incY;
		else
			initY = 2 * incY + this.initialRectPos.getY() * 2 * incY;
		double finalY, finalX = incX + this.mouseGrid.getX() * (3 * radius / 2);
		if (this.mouseGrid.getX() % 2 == 0)
			finalY = incY + this.mouseGrid.getY() * 2 * incY;
		else
			finalY = 2 * incY + this.mouseGrid.getY() * 2 * incY;
		Polygon square = new Polygon();
		square.addPoint((int)initX, (int)initY);
		square.addPoint((int)initX, (int)finalY);
		square.addPoint((int)finalX, (int)finalY);
		square.addPoint((int)finalX, (int)initY);
		
		Graphics2D g = (Graphics2D)this.getGraphics();
		g.setStroke(new BasicStroke(3.0f));
		g.setColor(Color.RED);
		g.drawPolygon(square);
	}
	
	private void paintSelectedRectangleCells() {
		if (!isInGrid(this.initialRectPos) || !isInGrid(this.mouseGrid))
			return;
		int minX = this.initialRectPos.getX();
		int maxX = this.mouseGrid.getX();
		int minY = this.initialRectPos.getY();
		int maxY = this.mouseGrid.getY();
		if (this.initialRectPos.getX() > this.mouseGrid.getX()) {
			minX = this.mouseGrid.getX();
			maxX = this.initialRectPos.getX();
		}
		if (this.initialRectPos.getY() > this.mouseGrid.getY()) {
			maxY = this.initialRectPos.getY();
			minY = this.mouseGrid.getY();
		}
		
		LogicalModel m = this.modelFeatures.getModel(this.selModelName);
		for (int x = minX; x <= maxX; x++) {
			for (int y = minY; y <= maxY; y++) {
				this.modelGridClone[x][y] = m;
			}
		}
		this.paintComponent(this.getGraphics());
	}

	public void setSelModelName(String name) {
		this.selModelName = name;
	}

	private void mousePosition2Grid(MouseEvent e) {
		this.mouseGrid = this.topology.getSelectedCell(this.radius, e.getX(),
				e.getY());
	}

	public void makeModelUbiquitus() {
		LogicalModel m = this.modelFeatures.getModel(this.selModelName);
		for (int x = 0; x < this.gridX; x++) {
			for (int y = 0; y < this.gridY; y++) {
				this.modelGridClone[x][y] = m;
			}
		}
		this.paintComponent(this.getGraphics());
	}

	public void isRectangleFill(boolean fill) {
		this.isRectFill = fill;
	}

	private void paintClickedCell() {
		if (this.selModelName == null)
			return;

		// Get selected cell grid XY
		if (!this.isInGrid(this.mouseGrid))
			return;
		this.modelGridClone[this.mouseGrid.getX()][this.mouseGrid.getY()] = this.modelFeatures
				.getModel(this.selModelName);

		this.paintComponent(this.getGraphics());
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

		this.radius = this.topology.computeBestRadius(this.gridX, this.gridY,
				this.getSize().width, this.getSize().height);

		for (int x = 0; x < this.gridX; x++) {
			for (int y = 0; y < this.gridY; y++) {
				Polygon polygon = topology.createNewPolygon(this.radius, x, y);
				Color c = this.colorMapClone.get(this.modelGridClone[x][y]);
				this.paintPolygon(this.basicStroke, c, polygon, g2);
			}
		}
	}
}