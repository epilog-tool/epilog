package org.ginsim.epilog.gui.tab;

import javax.swing.tree.TreePath;

import org.ginsim.epilog.ProjectModelFeatures;
import org.ginsim.epilog.core.Epithelium;
import org.ginsim.epilog.core.EpitheliumCell;

public class EpiTabInitialConditions extends EpiTabDefinitions {
	private EpitheliumCell[][] userCellGrid;
	
	public EpiTabInitialConditions(Epithelium e, TreePath path, ProjectModelFeatures modelFeatures) {
		super(e,path, modelFeatures);
		
	}
	
	public void initialize() {
		
	}

	@Override
	protected void buttonReset() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void buttonAccept() {
		// TODO Auto-generated method stub
		
	}
}
