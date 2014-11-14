package org.ginsim.epilog.gui.widgets;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Map;

import org.colomoto.logicalmodel.LogicalModel;
import org.ginsim.epilog.project.ProjectModelFeatures;
import org.ginsim.epilog.common.Tuple2D;
import org.ginsim.epilog.core.topology.Topology;

public class VisualGridModel extends VisualGrid {
	private static final long serialVersionUID = -8878704517273291774L;

	private LogicalModel[][] modelGridClone;
	private Map<LogicalModel, Color> colorMapClone;
	private ProjectModelFeatures modelFeatures;
	private String selModelName;
	private boolean isRectFill;
	private Tuple2D<Integer> initialRectPos;

	public VisualGridModel(int gridX, int gridY, Topology topology,
			LogicalModel[][] modelGridClone,
			Map<LogicalModel, Color> colorMapClone,
			ProjectModelFeatures modelFeatures) {
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
					drawRectangleOverSelectedCells();
				} else {
					paintCellAt(mouseGrid);
				}
			}
		});
		this.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if (isRectFill) {
					applyRectangleOnCells(initialRectPos, mouseGrid);
				}
			}

			@Override
			public void mousePressed(MouseEvent e) {
				if (isRectFill) {
					initialRectPos = mouseGrid.clone();
				} else {
					paintCellAt(mouseGrid);
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
		// Get selected model color
		LogicalModel m = this.modelFeatures.getModel(this.selModelName);
		Color c = this.colorMapClone.get(m);

		// Paint the rectangle
		super.highlightCellsOverRectangle(this.initialRectPos, this.mouseGrid,
				c);
	}

	public void setSelModelName(String name) {
		this.selModelName = name;
	}

	public void isRectangleFill(boolean fill) {
		this.isRectFill = fill;
	}

	protected void applyDataAt(int x, int y) {
		if (this.selModelName == null)
			return;
		LogicalModel m = this.modelFeatures.getModel(this.selModelName);
		this.modelGridClone[x][y] = m;
	}

	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;

		this.radius = this.topology.computeBestRadius(this.gridX, this.gridY,
				this.getSize().width, this.getSize().height);

		for (int x = 0; x < this.gridX; x++) {
			for (int y = 0; y < this.gridY; y++) {
				Tuple2D<Double> center = topology.getPolygonCenter(this.radius,
						x, y);
				Polygon polygon = topology
						.createNewPolygon(this.radius, center);
				Color c = this.colorMapClone.get(this.modelGridClone[x][y]);
				this.paintPolygon(this.strokeBasic, c, polygon, g2);
			}
		}
	}
}