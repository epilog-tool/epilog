package org.epilogtool.gui.widgets;

import org.epilogtool.core.topology.Topology;
import org.epilogtool.gui.tab.EpiTabDefinitions.TabProbablyChanged;

public abstract class VisualGridDefinitions extends VisualGrid {
	private static final long serialVersionUID = 5865698060852735125L;
	
	protected TabProbablyChanged tpc;

	public VisualGridDefinitions(int gridX, int gridY, Topology topology,
			TabProbablyChanged tpc) {
		super(gridX, gridY, topology);
		this.tpc = tpc;
	}
}