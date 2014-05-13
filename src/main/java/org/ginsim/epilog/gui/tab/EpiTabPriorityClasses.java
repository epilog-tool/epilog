package org.ginsim.epilog.gui.tab;

import javax.swing.tree.TreePath;

import org.ginsim.epilog.core.Epithelium;
import org.ginsim.epilog.core.EpitheliumPriorityClasses;

public class EpiTabPriorityClasses extends EpiTabDefinitions {
	private static final long serialVersionUID = 1176575422084167530L;
	
	private EpitheliumPriorityClasses userPriorityClasses;

	public EpiTabPriorityClasses(Epithelium e, TreePath path) {
		super(e,path);
		this.initializeGUI();
	}

	private void initializeGUI() {
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
