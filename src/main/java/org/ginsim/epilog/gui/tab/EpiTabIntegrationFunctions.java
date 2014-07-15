package org.ginsim.epilog.gui.tab;

import javax.swing.tree.TreePath;

import org.ginsim.epilog.ProjectModelFeatures;
import org.ginsim.epilog.core.Epithelium;
import org.ginsim.epilog.core.EpitheliumIntegrationFunctions;

public class EpiTabIntegrationFunctions extends EpiTabDefinitions {
	private static final long serialVersionUID = -2124909766318378839L;
	
	private EpitheliumIntegrationFunctions userIntegrationFunctions;
	
	public EpiTabIntegrationFunctions(Epithelium e, TreePath path, ProjectModelFeatures modelFeatures) {
		super(e,path, modelFeatures);
	}
	
	public void initialize() {
		// Define center GUI components
	}

	@Override
	protected void buttonCancel() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void buttonAccept() {
		// TODO Auto-generated method stub
		
	}
}
