package org.ginsim.epilog.gui.tab;

import javax.swing.tree.TreePath;

import org.ginsim.epilog.core.Epithelium;
import org.ginsim.epilog.core.EpitheliumCell;

public class EpiTabModelGrid extends EpiTabDefinitions {
	private EpitheliumCell[][] userCellGrid;
	
	public EpiTabModelGrid(Epithelium e, TreePath path) {
		super(e,path);
		this.initializeGUI();
	}
	
	private void initializeGUI() {
		// Define center GUI components
	}
	
	// TODO: Accept button -> calls epigrid.updateModelSet() 
}
