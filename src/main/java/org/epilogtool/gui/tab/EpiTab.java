package org.epilogtool.gui.tab;

import javax.swing.JPanel;
import javax.swing.tree.TreePath;

import org.epilogtool.common.Txt;
import org.epilogtool.core.Epithelium;
import org.epilogtool.gui.EpiGUI.ProjChangeNotifyTab;

public abstract class EpiTab extends JPanel {
	private static final long serialVersionUID = 4245892165061439503L;

	public static final String TAB_MODELGRID = Txt.get("s_TAB_MODELGRID");
	public static final String TAB_INTEGRATION = Txt.get("s_TAB_INTEGRATION");
	public static final String TAB_INITCONDITIONS = Txt.get("s_TAB_INITCONDITIONS");
	public static final String TAB_PERTURBATIONS = Txt.get("s_TAB_PERTURBATIONS");
	public static final String TAB_PRIORITIES = Txt.get("s_TAB_PRIORITIES");
	public static final String TAB_EPIUPDATING = Txt.get("s_TAB_EPIUPDATING");
	public static final String TAB_CELLDIVISION = Txt.get("s_TAB_CELLDIVISION");
	public static final String TOOL_SIMULATION = Txt.get("s_TOOL_SIMULATION");
	public static final String TOOL_MONTECARLO = Txt.get("s_TOOL_MONTECARLO");

	protected Epithelium epithelium;
	protected TreePath path;
	protected boolean isInitialized;
	protected ProjChangeNotifyTab projChanged;

	protected EpiTab(Epithelium e, TreePath path,
			ProjChangeNotifyTab projChanged) {
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
		// Reminder: not necessary to do .equals()!!
		return (this.epithelium == epi);
	}

	public abstract String getName();

	public abstract void initialize();

	public abstract boolean canClose();

	public abstract void applyChange();

	public abstract void notifyChange();
}
