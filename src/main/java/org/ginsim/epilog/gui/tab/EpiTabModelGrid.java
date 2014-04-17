package org.ginsim.epilog.gui.tab;

import javax.swing.tree.TreePath;

import org.ginsim.epilog.core.Epithelium;
import org.ginsim.epilog.core.EpitheliumCell;

public class EpiTabModelGrid extends EpiTabDefinitions {
	private static final long serialVersionUID = -5262665948855829161L;
	
	private EpitheliumCell[][] userCellGrid;

	public EpiTabModelGrid(Epithelium e, TreePath path) {
		super(e, path);
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

	// TODO: Accept button -> calls epigrid.updateModelSet()
}
