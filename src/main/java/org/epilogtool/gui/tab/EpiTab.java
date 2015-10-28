package org.epilogtool.gui.tab;

import javax.swing.JPanel;
import javax.swing.tree.TreePath;

import org.epilogtool.core.Epithelium;
import org.epilogtool.gui.EpiGUI.ProjectChangedInTab;

public abstract class EpiTab extends JPanel {
	private static final long serialVersionUID = 4245892165061439503L;

	protected Epithelium epithelium;
	protected TreePath path;
	protected boolean isInitialized;
	protected ProjectChangedInTab projChanged;

	protected EpiTab(Epithelium e, TreePath path, ProjectChangedInTab projChanged) {
		this.epithelium = e;
		this.path = path;
		this.isInitialized = false;
		this.projChanged = projChanged;
	}
	
	public TreePath getPath() {
		return this.path;
	}

	public boolean containsPath(TreePath path) {
		return this.path.equals(path);
	}

	public boolean containsEpithelium(Epithelium epi) {
		return (this.epithelium == epi); // TODO: PTGM: not necessary to do .equals()
	}

	public abstract void initialize();

	public abstract boolean canClose();
	
	public abstract void notifyChange();
}
