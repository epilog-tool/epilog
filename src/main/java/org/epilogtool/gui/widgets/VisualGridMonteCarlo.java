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
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.colomoto.biolqm.LogicalModel;
import org.epilogtool.common.Tuple2D;
import org.epilogtool.common.Tuple3D;
import org.epilogtool.core.EmptyModel;
import org.epilogtool.core.EpitheliumGrid;
import org.epilogtool.gui.color.ColorUtils;
import org.epilogtool.project.Project;

public class VisualGridMonteCarlo extends VisualGrid {
	private static final long serialVersionUID = -3880244278613986980L;

	private EpitheliumGrid epiGrid;
	private List<String> lCompON;
	private GridInformation valuePanel;
	private Tuple2D<Integer> lastPos;
	private boolean isCumulative;

	private Map<Tuple3D<Integer>, Float> cellNode2Count;

	public VisualGridMonteCarlo(EpitheliumGrid epiGrid, List<String> lCompON, GridInformation valuePanel) {
		super(epiGrid.getX(), epiGrid.getY(), epiGrid.getTopology());

		this.epiGrid = epiGrid;
		this.lCompON = lCompON;
		this.valuePanel = valuePanel;
		this.isCumulative = false;

		this.addMouseMotionListener(new MouseMotionListener() {
			@Override
			public void mouseMoved(MouseEvent e) {
				mousePosition2Grid(e);
			}

			@Override
			public void mouseDragged(MouseEvent e) {
				mousePosition2Grid(e);
				updateComponentValues(mouseGrid);
				if (!isCumulative)
					paintComponent(getGraphics());
				else
					paintCumulative(getGraphics(), cellNode2Count);
			}
		});
		this.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
			}

			@Override
			public void mousePressed(MouseEvent e) {
				updateComponentValues(mouseGrid);
				if (!isCumulative)
					paintComponent(getGraphics());
				else
					paintCumulative(getGraphics(), cellNode2Count);
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
	protected void applyDataAt(int x, int y) {
		// Does not need to apply data
	}

	private void updateComponentValues(Tuple2D<Integer> pos) {
		if (!isInGrid(pos))
			return;
		this.lastPos = pos;
		this.valuePanel.updateValues(pos.getX(), pos.getY(), this.epiGrid, null);
	}

	public void setIsCumulative(boolean isCumulative) {
		this.isCumulative = isCumulative;
	}

	public void setEpitheliumGrid(EpitheliumGrid grid) {
		this.epiGrid = grid;
		this.updateComponentValues(this.lastPos);
	}

	@Override
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;

		this.radius = this.topology.computeBestRadius(this.gridX, this.gridY, this.getSize().width,
				this.getSize().height);

		for (int x = 0; x < this.gridX; x++) {
			for (int y = 0; y < this.gridY; y++) {
				BasicStroke stroke = this.strokeBasic;
				if (this.epiGrid.getPerturbation(x, y) != null) {
					stroke = this.strokePerturb;
				}
				LogicalModel m = this.epiGrid.getModel(x, y);
				List<Color> lColors = new ArrayList<Color>();
				if (EmptyModel.getInstance().isEmptyModel(m)) {
					lColors.add(EmptyModel.getInstance().getColor());
				} else {
					for (String nodeID : this.lCompON) {
						Color cBase = Project.getInstance().getProjectFeatures().getNodeColor(nodeID);
						if (Project.getInstance().getProjectFeatures().hasNode(nodeID, m)) {
							byte max = Project.getInstance().getProjectFeatures().getNodeInfo(nodeID).getMax();

							int index = this.epiGrid.getNodeIndex(x, y, nodeID);
							if (index >= 0) { // if cell has nodeID
								byte value = this.epiGrid.getCellState(x, y)[index];
								if (value > 0) {
									lColors.add(ColorUtils.getColorAtValue(cBase, max, value));
								}
							}
						}
					}
				}
				Color cCombined = ColorUtils.combine(lColors);
				Tuple2D<Double> center = topology.getPolygonCenter(this.radius, x, y);
				Polygon polygon = topology.createNewPolygon(this.radius, center);
				this.paintPolygon(stroke, cCombined, polygon, g2);
				// Highlights the selected cell
				if (this.lastPos != null && this.lastPos.getX() == x && this.lastPos.getY() == y) {
					center = topology.getPolygonCenter(this.radius, x, y);
					polygon = topology.createNewPolygon(this.radius / 3, center);
					this.paintPolygon(stroke, ColorUtils.LIGHT_RED, polygon, g2);
				}
			}
		}
	}

	public EpitheliumGrid getEpitheliumGrid() {
		return epiGrid;
	}

	public void paintCumulative(Graphics g, Map<Tuple3D<Integer>, Float> cellNode2Count) {
		this.cellNode2Count = cellNode2Count;

		Graphics2D g2 = (Graphics2D) g;
		this.radius = this.topology.computeBestRadius(this.gridX, this.gridY, this.getSize().width,
				this.getSize().height);

		for (int x = 0; x < this.gridX; x++) {
			for (int y = 0; y < this.gridY; y++) {
				BasicStroke stroke = this.strokeBasic;
				if (this.epiGrid.getPerturbation(x, y) != null) {
					stroke = this.strokePerturb;
				}
				LogicalModel m = this.epiGrid.getModel(x, y);
				List<Color> lColors = new ArrayList<Color>();
				if (EmptyModel.getInstance().isEmptyModel(m)) {
					lColors.add(EmptyModel.getInstance().getColor());
				} else {
					for (String nodeID : this.lCompON) {

						float max = getMaxNodeCellCount(nodeID);

						Color cBase = Project.getInstance().getProjectFeatures().getNodeColor(nodeID);

						float value = this.cellNode2Count.get(new Tuple3D<Integer>(x, y, nodeID));
						if (value > 0) {
							lColors.add(ColorUtils.getColorAtValue(cBase, max, value));

						}
					}

				}
				Color cCombined = ColorUtils.combine(lColors);
				Tuple2D<Double> center = topology.getPolygonCenter(this.radius, x, y);
				Polygon polygon = topology.createNewPolygon(this.radius, center);
				this.paintPolygon(stroke, cCombined, polygon, g2);
				// Highlights the selected cell
				if (this.lastPos != null && this.lastPos.getX() == x && this.lastPos.getY() == y) {
					center = topology.getPolygonCenter(this.radius, x, y);
					polygon = topology.createNewPolygon(this.radius / 3, center);
					this.paintPolygon(stroke, ColorUtils.LIGHT_RED, polygon, g2);
				}
			}
		}
	}

	private Float getMaxNodeCellCount(String nodeID) {

		List<Float> value = new ArrayList<Float>();

		for (int x = 0; x < this.gridX; x++) {
			for (int y = 0; y < this.gridY; y++) {

				float m = this.cellNode2Count.get(new Tuple3D<Integer>(x, y, nodeID));
				value.add(m);
			}
		}

		return Collections.max(value);
	}

}
