package org.ginsim.epilog.gui.tab;

import javax.swing.tree.TreePath;

import org.colomoto.logicalmodel.perturbation.AbstractPerturbation;
import org.ginsim.epilog.ProjectModelFeatures;
import org.ginsim.epilog.core.Epithelium;
import org.ginsim.epilog.gui.widgets.VisualGridModel;
import org.ginsim.epilog.gui.widgets.VisualGridPerturbation;

public class EpiTabPerturbations extends EpiTabDefinitions {
	private static final long serialVersionUID = -1795100027288146018L;
	
	private VisualGridPerturbation visualGridPerturbation;
	private AbstractPerturbation[][] perturbGridClone;
	
	public EpiTabPerturbations(Epithelium e, TreePath path, ProjectModelFeatures modelFeatures) {
		super(e,path, modelFeatures);
	}
	
	public void initialize() {
		// Define center GUI components
	}

	@Override
	protected void buttonReset() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void buttonAccept() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected boolean isChanged() {
		// TODO Auto-generated method stub
		return false;
	}
}
