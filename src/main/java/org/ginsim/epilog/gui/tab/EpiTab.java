package org.ginsim.epilog.gui.tab;

import javax.swing.JPanel;
import javax.swing.tree.TreePath;

import org.ginsim.epilog.core.Epithelium;

public abstract class EpiTab extends JPanel {
	private static final long serialVersionUID = 4245892165061439503L;

	protected Epithelium epithelium;
	protected TreePath path;

	protected EpiTab(Epithelium e, TreePath path) {
		this.epithelium = e;
		this.path = path;
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
