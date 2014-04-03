package org.ginsim.epilog.gui.tab;

import javax.swing.tree.TreePath;

import org.ginsim.epilog.core.Epithelium;
import org.ginsim.epilog.core.EpitheliumIntegrationFunctions;

public class EpiTabIntegrationFunctions extends EpiTabDefinitions {
	private EpitheliumIntegrationFunctions userIntegrationFunctions;
	
	public EpiTabIntegrationFunctions(Epithelium e, TreePath path) {
		super(e,path);
		this.initializeGUI();
	}
	
	private void initializeGUI() {
		// Define center GUI components
	}
}
