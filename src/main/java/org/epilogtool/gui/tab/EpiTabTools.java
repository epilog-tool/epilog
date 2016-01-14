package org.epilogtool.gui.tab;

import javax.swing.tree.TreePath;

import org.epilogtool.core.Epithelium;
import org.epilogtool.gui.EpiGUI;
import org.epilogtool.gui.EpiGUI.ProjectChangedInTab;

public abstract class EpiTabTools extends EpiTab {
	private static final long serialVersionUID = -8985332131102726886L;

	protected EpiTabTools(Epithelium e, TreePath path, ProjectChangedInTab projChanged) {
		super(e, path, projChanged);
	}

	@Override
	public void initialize() {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void notifyChange() {
		if (!this.isInitialized) {
			return;
		}
		this.applyChange();
		EpiGUI.getInstance().selectTabJTreePath(this.path);
	}
}
