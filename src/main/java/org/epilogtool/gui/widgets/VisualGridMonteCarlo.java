package org.epilogtool.gui.widgets;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;

import org.colomoto.logicalmodel.LogicalModel;
import org.colomoto.logicalmodel.NodeInfo;
import org.epilogtool.common.Tuple2D;
import org.epilogtool.core.EmptyModel;
import org.epilogtool.core.Epithelium;
import org.epilogtool.core.EpitheliumGrid;
import org.epilogtool.gui.color.ColorUtils;
import org.epilogtool.project.ProjectFeatures;

public class VisualGridMonteCarlo extends VisualGrid {
	private static final long serialVersionUID = -3880244278613986980L;

	private EpitheliumGrid epiGrid;
	private ProjectFeatures projectFeatures;
	private Tuple2D<Integer> lastPos;
	private GridInformation valuePanel;

	public VisualGridMonteCarlo(EpitheliumGrid epiGrid,ProjectFeatures projectFeatures,GridInformation valuePanel) {

		super(epiGrid.getX(), epiGrid.getY(), epiGrid.getTopology());

	this.epiGrid = epiGrid;
	this.projectFeatures = projectFeatures;
	this.valuePanel = valuePanel;
	
//	System.out.println("Graphics when creating the vgMC " + this.getGraphics());
	
	this.addMouseMotionListener(new MouseMotionListener() {
		@Override
		public void mouseMoved(MouseEvent e) {
			mousePosition2Grid(e);
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			mousePosition2Grid(e);
			updateComponentValues(mouseGrid);
			paintComponent(getGraphics());
//			System.out.println(getGraphics());
		}
	});
	this.addMouseListener(new MouseListener() {
		@Override
		public void mouseReleased(MouseEvent e) {
		}

		@Override
		public void mousePressed(MouseEvent e) {
			updateComponentValues(mouseGrid);
			paintComponent(getGraphics());
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
	
	
	private void updateComponentValues(Tuple2D<Integer> pos) {
		if (!isInGrid(pos))
			return;
		this.lastPos = pos;
		this.valuePanel.updateValues(pos.getX(), pos.getY(), this.epiGrid);
	}

	public void setEpitheliumGrid(EpitheliumGrid grid) {
		this.epiGrid = grid;
		this.updateComponentValues(this.lastPos);
	}

	@Override
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;

		this.radius = this.topology.computeBestRadius(this.gridX, this.gridY,
				this.getSize().width, this.getSize().height);

		for (int x = 0; x < this.gridX; x++) {
			for (int y = 0; y < this.gridY; y++) {
				BasicStroke stroke = this.strokeBasic;
				if (this.epiGrid.getPerturbation(x, y) != null) {
					stroke = this.strokePerturb;
				}
				LogicalModel m = this.epiGrid.getModel(x, y);
//				System.out.println("I am painting the cell with logical model " + m);
				List<Color> lColors = new ArrayList<Color>();
				if (EmptyModel.getInstance().isEmptyModel(m)) {
					lColors.add(EmptyModel.getInstance().getColor());
				} else {
					for (NodeInfo node : m.getNodeOrder()) {
						String nodeID = node.getNodeID();
//						System.out.println("I am painting the node " + nodeID);
				
						Color cBase = this.projectFeatures.getNodeColor(nodeID);
						if (this.projectFeatures.hasNode(nodeID, m)) {
							byte max = this.projectFeatures.getNodeInfo(nodeID,
									m).getMax();

							int index = this.epiGrid.getNodeIndex(x, y, nodeID);
							if (index >= 0) { // if cell has nodeID
								byte value = this.epiGrid.getCellState(x, y)[index];
								if (value > 0) {
									lColors.add(ColorUtils.getColorAtValue(
											cBase, max, value));
								}
							}
						}
					}
				}
				Color cCombined = ColorUtils.combine(lColors);
//				System.out.println("The resulting color is  " + cCombined);
				Tuple2D<Double> center = topology.getPolygonCenter(this.radius,
						x, y);
				Polygon polygon = topology
						.createNewPolygon(this.radius, center);
				this.paintPolygon(stroke, cCombined, polygon, g2);
//				System.out.println("I just painted an hexagon  ");
				// Highlights the selected cell
				if (this.lastPos != null && this.lastPos.getX() == x
						&& this.lastPos.getY() == y) {
					center = topology.getPolygonCenter(this.radius, x, y);
					polygon = topology
							.createNewPolygon(this.radius / 3, center);
					this.paintPolygon(stroke, ColorUtils.LIGHT_RED, polygon, g2);
				}
			}
		}
	}





	@Override
	protected void applyDataAt(int x, int y) {
		// TODO Auto-generated method stub
		
	}
}
