package org.ginsim.epilog.gui.widgets;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.util.ArrayList;
import java.util.List;

import org.ginsim.epilog.core.EpitheliumComponentFeatures;
import org.ginsim.epilog.core.EpitheliumGrid;
import org.ginsim.epilog.core.topology.Topology;
import org.ginsim.epilog.gui.color.ColorUtils;

public class VisualGridSimulation extends VisualGrid {
	private static final long serialVersionUID = -3880244278613986980L;

	private EpitheliumComponentFeatures componentFeatures;
	private EpitheliumGrid epiGrid;
	private List<String> lCompON;

	public VisualGridSimulation(int gridX, int gridY, Topology topology,
			EpitheliumComponentFeatures componentFeatures,
			EpitheliumGrid epiGrid, List<String> lCompON) {
		super(gridX, gridY, topology);
		this.componentFeatures = componentFeatures;
		this.epiGrid = epiGrid;
		this.lCompON = lCompON;
	}

	@Override
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;

		this.radius = this.topology.computeBestRadius(this.gridX, this.gridY,
				this.getSize().width, this.getSize().height);

		for (int x = 0; x < this.gridX; x++) {
			for (int y = 0; y < this.gridY; y++) {
				Polygon polygon = topology.createNewPolygon(this.radius, x, y);
				BasicStroke stroke = this.basicStroke;
				if (this.epiGrid.getPerturbation(x, y) != null) {
					stroke = new BasicStroke(4.0f);
				}
				List<Color> lColors = new ArrayList<Color>();
				for (String nodeID : this.lCompON) {
					Color cBase = this.componentFeatures.getNodeColor(nodeID);
					byte max = this.componentFeatures.getNodeInfo(nodeID)
							.getMax();
					int index = this.epiGrid.getNodeIndex(x, y, nodeID);
					if (index > 0) { // if cell has nodeID
						byte value = this.epiGrid.getCellState(x, y)[index];
						if (value > 0) {
							lColors.add(ColorUtils.getColorAtValue(cBase, max,
									value));
						}
					}
				}
				Color cCombined = ColorUtils.combine(lColors);
				this.paintPolygon(stroke, cCombined, polygon, g2);
			}
		}
	}

}
