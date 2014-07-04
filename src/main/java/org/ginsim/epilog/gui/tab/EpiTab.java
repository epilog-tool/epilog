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

	public boolean hasPath(TreePath path) {
		return this.path.equals(path);
	}
}
