package org.epilogtool.gui.tab;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.tree.TreePath;

import org.epilogtool.core.Epithelium;
import org.epilogtool.gui.EpiGUI;
import org.epilogtool.gui.EpiGUI.TabChangeNotifyProj;


public abstract class EpiTabTools extends EpiTab {
	private static final long serialVersionUID = -8985332131102726886L;
	
	protected JPanel center;
	protected JPanel south;

	protected EpiTabTools(Epithelium e, TreePath path, TabChangeNotifyProj tabChanged) {
		super(e, path, tabChanged);
		this.initializeGUI();
	}
	
	private void initializeGUI() {
		this.setLayout(new BorderLayout());
		center = new JPanel();
		this.add(center, BorderLayout.CENTER);
		south = new JPanel();
		this.add(south, BorderLayout.SOUTH);
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
