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

import javax.swing.JLabel;

import org.colomoto.biolqm.LogicalModel;
import org.colomoto.biolqm.NodeInfo;
import org.epilogtool.OptionStore;
import org.epilogtool.common.RandCentral;
import org.epilogtool.common.Tuple2D;
import org.epilogtool.core.EpitheliumGrid;
import org.epilogtool.gui.color.ColorUtils;
import org.epilogtool.gui.dialog.EnumNodePercent;
import org.epilogtool.gui.tab.EpiTabDefinitions.TabProbablyChanged;
import org.epilogtool.project.Project;

public class VisualGridInitialConditions extends VisualGridDefinitions {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private EpitheliumGrid epiGrid;
	private Map<String, Byte> mNode2ValueSelected;
	private Map<String, JLabel> mNodeID2JLabel;
	private boolean isRectFill;
	private Tuple2D<Integer> initialRectPos;
	private List<LogicalModel> selectedModels;
	private GridInformation valuePanel;

	public VisualGridInitialConditions(EpitheliumGrid gridClone, Map<String, Byte> mNode2ValueSelected,
			Map<String, JLabel> mNodeID2JLabel, GridInformation valuePanel, TabProbablyChanged tpc) {
		super(gridClone.getX(), gridClone.getY(), gridClone.getTopology(), tpc);
		
		this.epiGrid = gridClone;
		this.mNode2ValueSelected = mNode2ValueSelected;
		this.mNodeID2JLabel = mNodeID2JLabel;
		this.isRectFill = false;
		this.initialRectPos = null;
		this.selectedModels = new ArrayList<LogicalModel>();
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
				epiGrid.updateNodeValueCounts();
				updateNodePercentages();

			}
		});
		this.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if (isRectFill) {
					applyRectangleOnCells(initialRectPos, mouseGrid);
				}
				epiGrid.updateNodeValueCounts();
				updateNodePercentages();
			}

			@Override
			public void mousePressed(MouseEvent e) {
				if (isRectFill) {
					initialRectPos = mouseGrid.clone();
				} else {
					paintCellAt(mouseGrid);
				}
				epiGrid.updateNodeValueCounts();
				updateNodePercentages();

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
	
public void updateNodePercentages() {
		
		String nodePercent = (String) OptionStore.getOption("PrefsNodePercent");
		if (nodePercent != null && nodePercent.equals(EnumNodePercent.YES.toString())) {
		for (String nodeID : this.mNodeID2JLabel.keySet()) {
			JLabel jlb = this.mNodeID2JLabel.get(nodeID);
			jlb.setText(this.epiGrid.getPercentage(nodeID));
			jlb.paintComponents(this.getGraphics());
		}
	}}
	public String updateNodePercentages (String nodeID) {
		updateNodePercentages();
			return this.epiGrid.getPercentage(nodeID);

		}

	@Override
	protected void paintCellAt(Tuple2D<Integer> pos) {
		// Get selected cell grid XY
		if (!this.isInGrid(pos))
			return;

		if (this.selectedModels.contains(this.epiGrid.getModel(pos.getX(), pos.getY())))
			super.paintCellAt(pos);
	}

	private void updateComponentValues(Tuple2D<Integer> pos) {
		if (!isInGrid(pos))
			return;

		this.valuePanel.updateValues(pos.getX(), pos.getY(), this.epiGrid, null);
	}

	public void setModels(List<LogicalModel> mds) {
		this.selectedModels = mds;
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
			if (!this.tpc.isChanged()
					&& !this.mNode2ValueSelected.get(nodeID).equals(this.epiGrid.getCellValue(x, y, nodeID))) {
				this.tpc.setChanged();
			}
			this.epiGrid.setCellComponentValue(x, y, nodeID, this.mNode2ValueSelected.get(nodeID));
		}
	}

	public void clearGrid() {
		for (int x = 0; x < this.gridX; x++) {
			for (int y = 0; y < this.gridY; y++) {
				for (String nodeID : this.mNode2ValueSelected.keySet()) {
					if (!this.tpc.isChanged() && this.epiGrid.getCellValue(x, y, nodeID) > 0) {
						this.tpc.setChanged();
					}
					this.epiGrid.setCellComponentValue(x, y, nodeID, (byte) 0);
				}
			}
		}
		updateNodePercentages();
		this.paintComponent(this.getGraphics());
	}

	@Override
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;

		this.radius = this.topology.computeBestRadius(this.gridX, this.gridY, this.getSize().width,
				this.getSize().height);

		for (int x = 0; x < this.gridX; x++) {
			for (int y = 0; y < this.gridY; y++) {
				Color cCombined;
				// if (EmptyModel.getInstance().isEmptyModel(this.epiGrid.getModel(x, y))) {
				// cCombined = EmptyModel.getInstance().getColor();
				// } else
				if (this.selectedModels.contains(this.epiGrid.getModel(x, y))) {
					List<Color> lColors = new ArrayList<Color>();
					for (String nodeID : this.mNode2ValueSelected.keySet()) {
						int index = this.epiGrid.getNodeIndex(x, y, nodeID);
						if (index >= 0) { // if cell has nodeID
							Color cBase = Project.getInstance().getProjectFeatures().getNodeID2ColorMap().get(nodeID);
							byte value = this.epiGrid.getCellState(x, y)[index];
							if (value > 0) {
								byte max = this.epiGrid.getModel(x, y).getComponents().get(index).getMax();
								lColors.add(ColorUtils.getColorAtValue(cBase, max, value));
							}
						}
					}
					cCombined = ColorUtils.combine(lColors);
				} else { // other models
					cCombined = this.getParent().getBackground();
				}
				Tuple2D<Double> center = topology.getPolygonCenter(this.radius, x, y);
				Polygon polygon = topology.createNewPolygon(this.radius, center);
				this.paintPolygon(this.strokeBasic, cCombined, polygon, g2);
			}
		}
	}

	public void setRandomValue(List<NodeInfo> lNodes) {
		for (NodeInfo node : lNodes) {
			byte maxValue = node.getMax();

			this.tpc.setChanged();
			for (int x = 0; x < this.gridX; x++) {
				for (int y = 0; y < this.gridY; y++) {
					int value = RandCentral.getInstance().nextInt(maxValue + 1);
					this.epiGrid.setCellComponentValue(x, y, node.getNodeID(), (byte) value);
				}
			}
		}
		
		this.epiGrid.updateNodeValueCounts();
		updateNodePercentages();
		this.paint(getGraphics());
	}

	public void updateNodeValueCounts() {
		this.epiGrid.updateNodeValueCounts();
	}
	
	public EpitheliumGrid getEpitheliumGrid() {
		return epiGrid;
	}

}
