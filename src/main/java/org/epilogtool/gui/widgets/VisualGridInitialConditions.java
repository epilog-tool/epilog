package org.epilogtool.gui.widgets;

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

import org.colomoto.logicalmodel.LogicalModel;
import org.epilogtool.common.Tuple2D;
import org.epilogtool.core.EmptyModel;
import org.epilogtool.core.EpitheliumGrid;
import org.epilogtool.gui.color.ColorUtils;
import org.epilogtool.project.ComponentPair;

public class VisualGridInitialConditions extends VisualGrid {
	private static final long serialVersionUID = 7590023855645466271L;

	private EpitheliumGrid epiGrid;
	private Map<String, Byte> mNode2ValueSelected;
	private Map<String, ComponentPair> mEnvironmentalComponents;
	private Map<String, Color> colorMapClone;
	private boolean isRectFill;
	private Tuple2D<Integer> initialRectPos;
	private LogicalModel selectedModel;
	private GridInformation valuePanel;

	public VisualGridInitialConditions(EpitheliumGrid gridClone, Map<String, Color> colorMapClone,
			Map<String, Byte> mNode2ValueSelected, GridInformation valuePanel,
			Map<String, ComponentPair> mEnvironmentalComponents) {
		super(gridClone.getX(), gridClone.getY(), gridClone.getTopology());
		this.epiGrid = gridClone;
		this.colorMapClone = colorMapClone;
		this.mNode2ValueSelected = mNode2ValueSelected;
		this.mEnvironmentalComponents = mEnvironmentalComponents;
		this.isRectFill = false;
		this.initialRectPos = null;
		this.selectedModel = null;
		this.valuePanel = valuePanel;

		this.addMouseMotionListener(new MouseMotionListener() {
			@Override
			public void mouseMoved(MouseEvent e) {
				mousePosition2Grid(e);
				updateComponentValues(mouseGrid);
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

	@Override
	protected void paintCellAt(Tuple2D<Integer> pos) {
		// Get selected cell grid XY
		if (!this.isInGrid(pos))
			return;

		if (epiGrid.getModel(pos.getX(), pos.getY()).equals(this.selectedModel) ||
				!this.mEnvironmentalComponents.isEmpty())
			super.paintCellAt(pos);
	}

	private void updateComponentValues(Tuple2D<Integer> pos) {
		if (!isInGrid(pos))
			return;

		this.valuePanel.updateValues(pos.getX(), pos.getY(), this.epiGrid);
	}

	public void setModel(LogicalModel m) {
		this.selectedModel = m;
	}

	private void drawRectangleOverSelectedCells() {
		// It would be difficult to compute the color of a rectangle since
		// selected components could not be present in some of the cells over
		// the rectangle
		Color c = Color.LIGHT_GRAY;

		// Paint the rectangle
		super.highlightCellsOverRectangle(this.initialRectPos, this.mouseGrid, c);
	}

	public void isRectangleFill(boolean fill) {
		this.isRectFill = fill;
	}

	protected void applyDataAt(int x, int y) {
		for (String nodeID : this.mNode2ValueSelected.keySet()) {
			if (!this.mEnvironmentalComponents.containsKey(nodeID)) {
				this.epiGrid.setCellComponentValue(x, y, nodeID, this.mNode2ValueSelected.get(nodeID));
				this.epiGrid.setCellInitialStateComponentValue(x, y, nodeID, this.mNode2ValueSelected.get(nodeID));
			} else {
				this.epiGrid
				.setEnvironmentalInput(x, y, this.mEnvironmentalComponents.get(nodeID), 
						this.mNode2ValueSelected.get(nodeID));
			}
		}
	}

	@Override
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;

		this.radius = this.topology.computeBestRadius(this.gridX, this.gridY, this.getSize().width,
				this.getSize().height);

		for (int x = 0; x < this.gridX; x++) {
			for (int y = 0; y < this.gridY; y++) {
				List<Color> colorList = new ArrayList<Color>();
				
				//environmental components
				if (!this.mEnvironmentalComponents.isEmpty()) {
					List<Color> lColors = new ArrayList<Color>();
					for (String nodeID : this.mEnvironmentalComponents.keySet()) {
						Color cBase = this.colorMapClone.get(nodeID);
						byte value = this.epiGrid.getCellEnvironment(x, y)
								.get(this.mEnvironmentalComponents.get(nodeID));
						if (value > 0) {
							byte max = this.mEnvironmentalComponents.get(nodeID).getNodeInfo().getMax();
							lColors.add(ColorUtils.getColorAtValue(cBase, max, value));
						}
					}
					colorList.addAll(lColors);
				}
				
				//empty cells
				if (this.epiGrid.isEmptyCell(x, y)){
					colorList.add(EmptyModel.getInstance().getColor());
				}
				
				//current model
				else if (this.epiGrid.getModel(x, y).equals(this.selectedModel)) {
					List<Color> lColors = new ArrayList<Color>();
					for (String nodeID : this.mNode2ValueSelected.keySet()) {
						if (!(this.mEnvironmentalComponents.containsKey(nodeID))) { 
							int index = this.epiGrid.getNodeIndex(x, y, nodeID);
							if (index >= 0) { // if cell has nodeID
								Color cBase = this.colorMapClone.get(nodeID);
								byte value = this.epiGrid.getCellState(x, y)[index];
								if (value > 0) {
									byte max = this.epiGrid.getModel(x, y).getNodeOrder().get(index).getMax();
									lColors.add(ColorUtils.getColorAtValue(cBase, max, value));
								}
							}
						}
					}
					colorList.addAll(lColors);
				} else { //other models
					colorList.add(this.getParent().getBackground());
				}
				Color cCombined = ColorUtils.combine(colorList);
				Tuple2D<Double> center = topology.getPolygonCenter(this.radius, x, y);
				Polygon polygon = topology.createNewPolygon(this.radius, center);
				this.paintPolygon(this.strokeBasic, cCombined, polygon, g2);
			}
		}
	}

}
