package org.ginsim.epilog.gui.tab;

import org.ginsim.epilog.core.Epithelium;
import org.ginsim.epilog.core.EpitheliumCell;

public class EpiTabModel extends EpiTabDefinitions {
	private EpitheliumCell[][] userCellGrid;
	
	public EpiTabModel(Epithelium e) {
		super(e);
		this.initializeGUI();
	}
	
	private void initializeGUI() {
		// Define center GUI components
	}
}
