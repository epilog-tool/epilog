package org.epilogtool.gui.tab;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.tree.TreePath;

import org.colomoto.biolqm.LogicalModel;
import org.colomoto.biolqm.tool.simulation.grouping.ModelGrouping;
import org.colomoto.biolqm.widgets.PriorityClassPanel;
import org.colomoto.biolqm.widgets.PanelChangedEventListener;
import org.epilogtool.common.Txt;
import org.epilogtool.common.Web;
import org.epilogtool.core.Epithelium;
import org.epilogtool.core.EpitheliumUpdateSchemeIntra;
import org.epilogtool.gui.EpiGUI.TabChangeNotifyProj;
import org.epilogtool.gui.widgets.JComboWideBox;
import org.epilogtool.project.Project;

public class EpiTabCellularModelUpdate extends EpiTabDefinitions implements HyperlinkListener {
	private static final long serialVersionUID = 1176575422084167530L;

	private EpitheliumUpdateSchemeIntra userPriorityClasses;
	private LogicalModel selModel;
	private TabProbablyChanged tpc;
	private Map<LogicalModel, PriorityClassPanel> mModel2PCP;

	private JPanel jpNorth;
	private JPanel jpNorthLeft;
	private PriorityClassPanel jpPriorityPanel;

	public EpiTabCellularModelUpdate(Epithelium e, TreePath path, TabChangeNotifyProj tabChanged) {
		super(e, path, tabChanged);
	}

	public void initialize() {

		this.center.setLayout(new BorderLayout());

		this.jpNorth = new JPanel(new BorderLayout());
		this.center.add(this.jpNorth, BorderLayout.NORTH);

		// Model selection JPanel
		this.jpNorthLeft = new JPanel(new FlowLayout());
		List<LogicalModel> modelList = new ArrayList<LogicalModel>(this.epithelium.getEpitheliumGrid().getModelSet());
		JComboBox<String> jcbSBML = this.newModelCombobox(modelList);
		this.jpNorthLeft.add(jcbSBML);
		this.jpNorthLeft.setBorder(BorderFactory.createTitledBorder(Txt.get("s_MODEL_SELECT")));
		this.jpNorth.add(this.jpNorthLeft, BorderLayout.WEST);

		this.tpc = new TabProbablyChanged();
		this.mModel2PCP = new HashMap<LogicalModel, PriorityClassPanel>();

		this.userPriorityClasses = new EpitheliumUpdateSchemeIntra();
		for (LogicalModel m : modelList) {
			this.userPriorityClasses.addModelPriorityClasses(this.epithelium.getPriorityClasses(m).clone());
		}

		this.updatePriorityPanel();
		this.isInitialized = true;
	}

	private PriorityClassPanel getPriorityClassPanel(LogicalModel m) {
		if (!this.mModel2PCP.containsKey(m)) {
			ModelGrouping mpc = this.userPriorityClasses.getModelPriorityClasses(m);
			PriorityClassPanel pcp = new PriorityClassPanel(mpc, false);
			pcp.addActionListener(new PanelChangedEventListener() {
				@Override
				public void panelChangedOccurred() {
					tpc.setChanged();
				}
			});
			this.mModel2PCP.put(m, pcp);
		}
		return this.mModel2PCP.get(m);
	}

	private JComboBox<String> newModelCombobox(List<LogicalModel> modelList) {
		// Model selection list
		String[] saSBML = new String[modelList.size()];
		for (int i = 0; i < modelList.size(); i++) {
			saSBML[i] = Project.getInstance().getProjectFeatures().getModelName(modelList.get(i));
		}
		JComboBox<String> jcb = new JComboWideBox<String>(saSBML);
		jcb.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				@SuppressWarnings("unchecked")
				JComboBox<String> jcb = (JComboBox<String>) e.getSource();
				selModel = Project.getInstance().getProjectFeatures().getModel((String) jcb.getSelectedItem());
				updatePriorityPanel();
			}
		});
		this.selModel = Project.getInstance().getProjectFeatures().getModel((String) jcb.getItemAt(0));
		return jcb;
	}

	private void updatePriorityPanel() {
		BorderLayout layout = (BorderLayout) this.center.getLayout();
		JPanel jpTmp = (JPanel) layout.getLayoutComponent(BorderLayout.CENTER);
		if (jpTmp != null) {
			this.center.remove(jpTmp);
		}
		this.jpPriorityPanel = this.getPriorityClassPanel(this.selModel);
		this.jpPriorityPanel.updatePriorityList();
		this.center.add(this.jpPriorityPanel, BorderLayout.CENTER);
		this.center.revalidate();
		// Repaint
		this.center.repaint();
		this.jpPriorityPanel.repaint();
	}

	@Override
	protected void buttonReset() {
		this.mModel2PCP.clear();
		this.userPriorityClasses = new EpitheliumUpdateSchemeIntra();
		for (LogicalModel m : this.epithelium.getEpitheliumGrid().getModelSet()) {
			this.userPriorityClasses.addModelPriorityClasses(this.epithelium.getPriorityClasses(m).clone());
		}
		this.updatePriorityPanel();
	}

	@Override
	protected void buttonAccept() {
		for (LogicalModel m : this.userPriorityClasses.getModelSet()) {
			ModelGrouping clone = this.userPriorityClasses.getModelPriorityClasses(m).clone();
			this.epithelium.setPriorityClasses(clone);
		}
	}

	@Override
	protected boolean isChanged() {
		for (LogicalModel m : this.epithelium.getEpitheliumGrid().getModelSet()) {
			ModelGrouping mpcGUI = this.userPriorityClasses.getModelPriorityClasses(m);
			ModelGrouping mpcEpi = this.epithelium.getPriorityClasses(m);
			if (!mpcGUI.equals(mpcEpi))
				return true;
		}
		return false;
	}

	@Override
	public void applyChange() {
		List<LogicalModel> modelList = new ArrayList<LogicalModel>(this.epithelium.getEpitheliumGrid().getModelSet());
		EpitheliumUpdateSchemeIntra newPCs = new EpitheliumUpdateSchemeIntra();
		for (LogicalModel m : modelList) {
			if (this.userPriorityClasses.getModelSet().contains(m)) {
				// Already exists
				newPCs.addModelPriorityClasses(this.userPriorityClasses.getModelPriorityClasses(m));
			} else {
				// Adds a new one
				newPCs.addModel(m);
			}
		}
		this.userPriorityClasses = newPCs;
		this.jpNorthLeft.removeAll();
		this.jpNorthLeft.add(this.newModelCombobox(modelList));
		this.mModel2PCP.clear();
		this.updatePriorityPanel();
	}

	@Override
	public void hyperlinkUpdate(HyperlinkEvent event) {
		if (event.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
			Web.openURI(event.getDescription());
		}
	}
}
