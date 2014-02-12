package org.ginsim.epilog.gui.tab;

import javax.swing.JPanel;

import org.ginsim.epilog.core.Epithelium;

public abstract class EpiTab extends JPanel {
	protected Epithelium epithelium;

	protected EpiTab(Epithelium e) {
		this.epithelium = e;
	}
}
