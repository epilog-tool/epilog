package org.ginsim.epilog.gui.tab;

import java.awt.Color;
import java.util.Map;

import javax.swing.tree.TreePath;

import org.ginsim.epilog.ProjectModelFeatures;
import org.ginsim.epilog.core.Epithelium;
import org.ginsim.epilog.core.EpitheliumCell;

public class EpiTabInitialConditions extends EpiTabDefinitions {
	private static final long serialVersionUID = -3626371381385041594L;
	
	private EpitheliumCell[][] gridStateClone;
	private Map<String, Byte> compValueClone;
	private Map<String, Color> compColorClone;
	
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

	@Override
	protected boolean isChanged() {
		// TODO Auto-generated method stub
		return false;
	}
}
