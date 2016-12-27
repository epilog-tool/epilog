package org.epilogtool.gui.tab;

import javax.swing.JPanel;
import javax.swing.tree.TreePath;

import org.epilogtool.core.Epithelium;
import org.epilogtool.gui.EpiGUI.ProjChangeNotifyTab;

public abstract class EpiTab extends JPanel {
	private static final long serialVersionUID = 4245892165061439503L;

	public static final String TAB_MODELGRID = "Model Grid";
	public static final String TAB_INTEGRATION = "Input Definition";
	public static final String TAB_INITCONDITIONS = "Initial Conditions";
	public static final String TAB_PERTURBATIONS = "Component Perturbations";
	public static final String TAB_PRIORITIES = "Model Updating";
	public static final String TAB_EPIUPDATING = "Epithelium Updating";
	public static final String TAB_CELLDIVISION = "Cell Division";
	public static final String TOOL_SIMULATION = "Simulation";

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
