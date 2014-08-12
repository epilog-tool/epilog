package org.ginsim.epilog.gui.widgets;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.ginsim.epilog.Tuple2D;
import org.ginsim.epilog.core.EpitheliumCell;
import org.ginsim.epilog.core.topology.Topology;
import org.ginsim.epilog.gui.color.ColorUtils;

public class VisualGridInitialConditions extends VisualGrid {
	private static final long serialVersionUID = 7590023855645466271L;

	private EpitheliumCell[][] cellGridClone;
	private Map<String, Byte> mNode2ValueSelected;
	private Map<String, Color> colorMapClone;
	private boolean isRectFill;
	private Tuple2D initialRectPos;

	public VisualGridInitialConditions(int gridX, int gridY, Topology topology,
			EpitheliumCell[][] cellGridClone, Map<String, Color> colorMapClone,
			Map<String, Byte> mNode2ValueSelected) {
		super(gridX, gridY, topology);
		this.cellGridClone = cellGridClone;
		this.colorMapClone = colorMapClone;
		this.mNode2ValueSelected = mNode2ValueSelected;
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
					paintCellsAtRectangle(initialRectPos, mouseGrid);
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
		// TODO: which color to draw ?
		Color c = Color.BLACK;

		// Paint the rectangle
		super.drawRectangleOverCells(this.initialRectPos, this.mouseGrid, c);
	}

	public void isRectangleFill(boolean fill) {
		this.isRectFill = fill;
	}

	protected void applyDataAt(int x, int y) {
		for (String nodeID : this.mNode2ValueSelected.keySet()) {
			this.cellGridClone[x][y].setValue(nodeID,
					this.mNode2ValueSelected.get(nodeID));
		}
	}

	@Override
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;

		this.radius = this.topology.computeBestRadius(this.gridX, this.gridY,
				this.getSize().width, this.getSize().height);

		for (int x = 0; x < this.gridX; x++) {
			for (int y = 0; y < this.gridY; y++) {
				Polygon polygon = topology.createNewPolygon(this.radius, x, y);
				List<Color> lColors = new ArrayList<Color>();
				for (String nodeID : this.mNode2ValueSelected.keySet()) {
					int index = this.cellGridClone[x][y].getNodeIndex(nodeID);
					if (index > 0) { // if cell has nodeID
						Color cBase = this.colorMapClone.get(nodeID);
						if (cBase == null) {
							System.out.println("[" + x + "," + y + "] "
									+ nodeID + " : " + cBase);
						}
						byte value = this.cellGridClone[x][y].getState()[index];
						if (value > 0) {
							byte max = this.cellGridClone[x][y].getModel()
									.getNodeOrder().get(index).getMax();
							lColors.add(ColorUtils.getColorAtValue(cBase, max,
									value));
						}
					}
				}
				Color cCombined = ColorUtils.combine(lColors);
				this.paintPolygon(this.basicStroke, cCombined, polygon, g2);
			}
		}
	}

}
