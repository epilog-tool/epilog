package org.epilogtool.gui.widgets;

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
import org.epilogtool.common.Tuple2D;
import org.epilogtool.core.EmptyModel;
import org.epilogtool.core.EpitheliumCell;
import org.epilogtool.core.topology.Topology;

public class VisualGridPerturbation extends VisualGrid {
	private static final long serialVersionUID = -8878704517273291774L;

	private EpitheliumCell[][] cellGridClone;
	private Map<AbstractPerturbation, Color> colorMapClone;
	private boolean isRectFill;
	private Tuple2D<Integer> initialRectPos;

	private LogicalModel selectedModel;
	private AbstractPerturbation selAbsPerturb;

	public VisualGridPerturbation(int gridX, int gridY, Topology topology,
			EpitheliumCell[][] cellGridClone,
			Map<AbstractPerturbation, Color> colorMapClone) {
		super(gridX, gridY, topology);
		this.cellGridClone = cellGridClone;
		this.colorMapClone = colorMapClone;
		this.selectedModel = null;
		this.selAbsPerturb = null;
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

	public void setModel(LogicalModel m) {
		this.selectedModel = m;
	}

	private void drawRectangleOverSelectedCells() {
		// Get selected perturbation color
		Color c = this.colorMapClone.get(this.selAbsPerturb);

		// Paint the rectangle
		super.highlightCellsOverRectangle(this.initialRectPos, this.mouseGrid,
				c);
	}

	public void setSelAbsPerturb(AbstractPerturbation ap) {
		this.selAbsPerturb = ap;
	}

	public void isRectangleFill(boolean fill) {
		this.isRectFill = fill;
	}

	protected void applyDataAt(int x, int y) {
		if (this.cellGridClone[x][y].getModel().equals(this.selectedModel)) {
			this.cellGridClone[x][y].setPerturbation(this.selAbsPerturb);
		}
	}

	public void clearDataFromAll() {
		for (int x = 0; x < this.gridX; x++) {
			for (int y = 0; y < this.gridY; y++) {
				AbstractPerturbation ap = this.cellGridClone[x][y]
						.getPerturbation();
				if (selAbsPerturb == null || ap != null && ap.equals(selAbsPerturb)) {
					this.cellGridClone[x][y].setPerturbation(null);
				}
			}
		}
		this.paintComponent(this.getGraphics());
	}

	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;

		this.radius = this.topology.computeBestRadius(this.gridX, this.gridY,
				this.getSize().width, this.getSize().height);

		for (int x = 0; x < this.gridX; x++) {
			for (int y = 0; y < this.gridY; y++) {
				BasicStroke stroke = this.strokeBasic;
				Color cPerturb = this.getParent().getBackground();
				if (EmptyModel.getInstance().isEmptyModel(this.cellGridClone[x][y].getModel())){
					cPerturb = EmptyModel.getInstance().getColor();
				}
				
				else if (this.cellGridClone[x][y].getModel().equals(
						this.selectedModel)) {
					AbstractPerturbation ap = this.cellGridClone[x][y]
							.getPerturbation();
					if (ap != null) {
						cPerturb = this.colorMapClone.get(ap);
						stroke = this.strokePerturb;
					} else
						cPerturb = Color.WHITE;
				}
				Tuple2D<Double> center = topology.getPolygonCenter(this.radius,
						x, y);
				Polygon polygon = topology
						.createNewPolygon(this.radius, center);
				this.paintPolygon(stroke, cPerturb, polygon, g2);
			}
		}
	}
}