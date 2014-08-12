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
import org.colomoto.logicalmodel.perturbation.AbstractPerturbation;
import org.ginsim.epilog.ProjectModelFeatures;
import org.ginsim.epilog.Tuple2D;
import org.ginsim.epilog.core.topology.Topology;

public class VisualGridPerturbation extends VisualGrid {
	private static final long serialVersionUID = -8878704517273291774L;

	private LogicalModel[][] modelGridOriginal;
	private AbstractPerturbation[][] perturbGridClone;
	private Map<LogicalModel, Color> colorMapClone;
	private ProjectModelFeatures modelFeatures;
	private String selModelName;
	private AbstractPerturbation selAbsPerturb;
	private boolean isRectFill;
	private Tuple2D initialRectPos;

	public VisualGridPerturbation(int gridX, int gridY, Topology topology,
			LogicalModel[][] modelGridOriginal,
			Map<LogicalModel, Color> colorMapClone,
			ProjectModelFeatures modelFeatures) {
		super(gridX, gridY, topology);
		this.modelGridOriginal = modelGridOriginal;
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
					drawRectangleOverSelectedCells();
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
					initialRectPos = mouseGrid.clone();
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

	private void drawRectangleOverSelectedCells() {
		if (!isInGrid(this.initialRectPos) || !isInGrid(this.mouseGrid))
			return;
		this.paintComponent(this.getGraphics());
		double incX = radius;
		double incY = radius * Math.sqrt(3) / 2;
		double initY, initX = incX + this.initialRectPos.getX()
				* (3 * radius / 2);
		if (this.initialRectPos.getX() % 2 == 0)
			initY = incY + this.initialRectPos.getY() * 2 * incY;
		else
			initY = 2 * incY + this.initialRectPos.getY() * 2 * incY;
		double finalY, finalX = incX + this.mouseGrid.getX() * (3 * radius / 2);
		if (this.mouseGrid.getX() % 2 == 0)
			finalY = incY + this.mouseGrid.getY() * 2 * incY;
		else
			finalY = 2 * incY + this.mouseGrid.getY() * 2 * incY;
		if (this.initialRectPos.getX() == this.mouseGrid.getX()
				&& this.initialRectPos.getY() == this.mouseGrid.getY()) {
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

		// Get selected model color
		LogicalModel m = this.modelFeatures.getModel(this.selModelName);
		Color c = this.colorMapClone.get(m);
		// Paint the rectangle
		Graphics2D g = (Graphics2D) this.getGraphics();
		g.setStroke(new BasicStroke(4.0f));
		g.setColor(c);
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

		for (int x = minX; x <= maxX; x++) {
			for (int y = minY; y <= maxY; y++) {
				this.applyPerturbAt(x, y);
			}
		}
		this.paintComponent(this.getGraphics());
	}

	public void setSelModelName(String name) {
		this.selModelName = name;
	}

	public void setSelAbsPerturb(AbstractPerturbation ap) {
		this.selAbsPerturb = ap;
	}

	public void applyPerturbAllCells() {
		for (int x = 0; x < this.gridX; x++) {
			for (int y = 0; y < this.gridY; y++) {
				this.applyPerturbAt(x, y);
			}
		}
		this.paintComponent(this.getGraphics());
	}

	public void isRectangleFill(boolean fill) {
		this.isRectFill = fill;
	}

	private void applyPerturbAt(int x, int y) {
		LogicalModel m = this.modelFeatures.getModel(this.selModelName);
		if (this.modelGridOriginal[x][y].equals(m)) {
			this.perturbGridClone[x][y] = this.selAbsPerturb;
		}
	}

	private void paintClickedCell() {
		if (this.selModelName == null || this.selAbsPerturb == null)
			return;

		// Get selected cell grid XY
		if (!this.isInGrid(this.mouseGrid))
			return;

		this.applyPerturbAt(this.mouseGrid.getX(), this.mouseGrid.getY());
		this.paintComponent(this.getGraphics());
	}

	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;

		this.radius = this.topology.computeBestRadius(this.gridX, this.gridY,
				this.getSize().width, this.getSize().height);

		for (int x = 0; x < this.gridX; x++) {
			for (int y = 0; y < this.gridY; y++) {
				Polygon polygon = topology.createNewPolygon(this.radius, x, y);
				Color c = this.colorMapClone.get(this.modelGridOriginal[x][y]);
				this.paintPolygon(this.basicStroke, c, polygon, g2);
			}
		}
	}
}