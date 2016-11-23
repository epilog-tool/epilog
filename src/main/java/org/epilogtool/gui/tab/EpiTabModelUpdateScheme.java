package org.epilogtool.gui.tab;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.Box;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.tree.TreePath;

import org.colomoto.logicalmodel.LogicalModel;
import org.epilogtool.common.Web;
import org.epilogtool.core.Epithelium;
import org.epilogtool.core.EpitheliumUpdateSchemeIntra;
import org.epilogtool.core.ModelPriorityClasses;
import org.epilogtool.gui.EpiGUI.TabChangeNotifyProj;
import org.epilogtool.gui.EpiGUI.ProjChangeNotifyTab;
import org.epilogtool.gui.widgets.JComboWideBox;
import org.epilogtool.io.ButtonFactory;
import org.epilogtool.project.ProjectFeatures;

public class EpiTabModelUpdateScheme extends EpiTabDefinitions implements HyperlinkListener {
	private static final long serialVersionUID = 1176575422084167530L;

	private final int JLIST_LINES = 15;
	private final int JLIST_WIDTH = 65;
	private final int JLIST_SPACING = 15;

	private EpitheliumUpdateSchemeIntra userPriorityClasses;
	private LogicalModel selectedModel;
	private List<JList<String>> guiClasses;
	private TabProbablyChanged tpc;

	private JPanel jpNorth;
	private JPanel jpNorthLeft;
	private JPanel jpSouth;
	private JPanel jpIntraCenter;

	public EpiTabModelUpdateScheme(Epithelium e, TreePath path, ProjChangeNotifyTab projChanged, TabChangeNotifyProj tabChanged,
			ProjectFeatures projectFeatures) {
		super(e, path, projChanged, tabChanged, projectFeatures);
	}

	public void initialize() {
		this.center.setLayout(new BorderLayout());
		List<LogicalModel> modelList = new ArrayList<LogicalModel>(this.epithelium.getEpitheliumGrid().getModelSet());
		this.userPriorityClasses = new EpitheliumUpdateSchemeIntra();
		for (LogicalModel m : modelList) {
			this.userPriorityClasses.addModelPriorityClasses(this.epithelium.getPriorityClasses(m).clone());
		}
		this.guiClasses = new ArrayList<JList<String>>();
		this.tpc = new TabProbablyChanged();

		this.jpNorth = new JPanel(new BorderLayout());
		this.center.add(this.jpNorth, BorderLayout.NORTH);
		
		// Model selection JPanel
		this.jpNorthLeft = new JPanel();
		this.jpNorth.add(this.jpNorthLeft, BorderLayout.WEST);
		this.jpNorthLeft.add(new JLabel("Model: "));
		JComboBox<String> jcbSBML = this.newModelCombobox(modelList);
		this.jpNorthLeft.add(jcbSBML);
		
		this.jpSouth = new JPanel(new BorderLayout());
		this.center.add(jpSouth, BorderLayout.SOUTH);
		
		// Button display options
		JPanel jpSouthCenter = new JPanel(new FlowLayout());
		this.jpSouth.add(jpSouthCenter, BorderLayout.CENTER);

		JButton jbSplit = ButtonFactory.getNoMargins("Split");
		jbSplit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				splitSelVars();
				// Repaint
				getParent().repaint();
			}
		});
		jpSouthCenter.add(jbSplit);
		JButton jbUnsplit = ButtonFactory.getNoMargins("Unsplit");
		jbUnsplit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				unsplitSelVars();
				// Repaint
				getParent().repaint();
			}
		});
		jpSouthCenter.add(jbUnsplit);
		JButton jbInc = ButtonFactory.getNoMargins("<-");
		jbInc.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				incPriorityOfSelVars();
				// Repaint
				getParent().repaint();
			}
		});
		jpSouthCenter.add(jbInc);
		JButton jbDec = ButtonFactory.getNoMargins("->");
		jbDec.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				decPriorityOfSelVars();
				// Repaint
				getParent().repaint();
			}
		});
		jpSouthCenter.add(jbDec);
		JButton jbSingle = ButtonFactory.getNoMargins("Single class");
		jbSingle.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				createSingleClass();
				// Repaint
				getParent().repaint();
			}
		});
		jpSouthCenter.add(jbSingle);
		
		// Model Components panel
		this.jpIntraCenter = new JPanel(new FlowLayout());
		this.center.add(this.jpIntraCenter, BorderLayout.CENTER);

		LogicalModel m = this.projectFeatures.getModel((String) jcbSBML.getSelectedItem());
		this.updatePriorityList(m);
		this.isInitialized = true;
	}

	private JComboBox<String> newModelCombobox(List<LogicalModel> modelList) {
		// Model selection list
		String[] saSBML = new String[modelList.size()];
		for (int i = 0; i < modelList.size(); i++) {
			saSBML[i] = this.projectFeatures.getModelName(modelList.get(i));
		}
		JComboBox<String> jcb = new JComboWideBox(saSBML);
		jcb.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				@SuppressWarnings("unchecked")
				JComboBox<String> jcb = (JComboBox<String>) e.getSource();
				LogicalModel m = projectFeatures.getModel((String) jcb.getSelectedItem());
				updatePriorityList(m);
				// Re-Paint
				getParent().repaint();
			}
		});
		return jcb;
	}

	private void splitSelVars() {
		ModelPriorityClasses mpc = this.userPriorityClasses.getModelPriorityClasses(this.selectedModel);
		for (int i = 0; i < this.guiClasses.size(); i++) {
			List<String> values = this.guiClasses.get(i).getSelectedValuesList();
			if (!values.isEmpty()) {
				for (String var : values)
					mpc.split(i, var);
				tpc.setChanged();
				break;
			}
		}
		this.updatePriorityList(this.selectedModel);
	}

	private void unsplitSelVars() {
		ModelPriorityClasses mpc = this.userPriorityClasses.getModelPriorityClasses(this.selectedModel);
		for (int i = 0; i < this.guiClasses.size(); i++) {
			List<String> values = this.guiClasses.get(i).getSelectedValuesList();
			if (!values.isEmpty()) {
				for (String var : values)
					mpc.unsplit(i, var);
				tpc.setChanged();
				break;
			}
		}
		this.updatePriorityList(this.selectedModel);
	}

	private void incPriorityOfSelVars() {
		ModelPriorityClasses mpc = this.userPriorityClasses.getModelPriorityClasses(this.selectedModel);
		for (int i = 0; i < this.guiClasses.size(); i++) {
			List<String> values = this.guiClasses.get(i).getSelectedValuesList();
			if (!values.isEmpty()) {
				mpc.incPriorities(i, values);
				tpc.setChanged();
				break;
			}
		}
		this.updatePriorityList(this.selectedModel);
	}

	private void decPriorityOfSelVars() {
		ModelPriorityClasses mpc = this.userPriorityClasses.getModelPriorityClasses(this.selectedModel);
		for (int i = 0; i < this.guiClasses.size(); i++) {
			List<String> values = this.guiClasses.get(i).getSelectedValuesList();
			if (!values.isEmpty()) {
				mpc.decPriorities(i, values);
				tpc.setChanged();
				break;
			}
		}
		this.updatePriorityList(this.selectedModel);
	}

	private void createSingleClass() {
		this.userPriorityClasses.getModelPriorityClasses(this.selectedModel).singlePriorityClass();
		tpc.setChanged();
		this.updatePriorityList(this.selectedModel);
	}

	// FIXME
	private void updatePriorityList(LogicalModel m) {
		this.jpIntraCenter.removeAll();
		this.guiClasses.clear();
		this.selectedModel = m;
		this.mergeInputPriorities();
		ModelPriorityClasses mpc = this.userPriorityClasses.getModelPriorityClasses(m);

		for (int idxPC = 0; idxPC < mpc.size(); idxPC++) {
			JPanel jpRankBlock = new JPanel(new BorderLayout());
			JLabel jlTmp = new JLabel("Rank " + (idxPC + 1), SwingConstants.CENTER);
			jpRankBlock.add(jlTmp, BorderLayout.NORTH);

			DefaultListModel<String> lModel = new DefaultListModel<String>();
			List<String> vars = mpc.getClassVars(idxPC);
			// -- Order variables alphabetically
			Collections.sort(vars, String.CASE_INSENSITIVE_ORDER);
			for (String var : vars) {
				String tmpVar = var;
				if (var.contains("+") | var.contains("-")) {
					tmpVar = var.split("\\[")[0];
				}
				if (!this.projectFeatures.getNodeInfo(tmpVar, m).isInput()){
					lModel.addElement(var);
				}
			}
			JList<String> jList = new JList<String>(lModel);
			jList.setVisibleRowCount(this.JLIST_LINES);
			jList.setFixedCellWidth(this.JLIST_WIDTH);
			jList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
			jList.addMouseListener(new MouseListener() {
				@Override
				public void mouseReleased(MouseEvent e) {
					@SuppressWarnings("unchecked")
					JList<String> selJList = (JList<String>) e.getSource();
					for (JList<String> list : guiClasses) {
						if (!list.equals(selJList)) {
							list.clearSelection();
						}
					}
				}

				@Override
				public void mousePressed(MouseEvent e) {
				}

				@Override
				public void mouseExited(MouseEvent e) {
				}

				@Override
				public void mouseEntered(MouseEvent e) {
				}

				@Override
				public void mouseClicked(MouseEvent e) {
				}
			});
			this.guiClasses.add(jList);

			JScrollPane jScroll = new JScrollPane(jList);
			jScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
			jpRankBlock.add(jScroll, BorderLayout.CENTER);

			String label = "  ";
			if (mpc.size() > 1) {
				if (idxPC == 0)
					label = "Fastest";
				else if (idxPC == (mpc.size() - 1))
					label = "Slowest";
			}
			jpRankBlock.add(new JLabel(label, SwingConstants.CENTER), BorderLayout.SOUTH);

			this.jpIntraCenter.add(jpRankBlock);
			this.jpIntraCenter.add(Box.createRigidArea(new Dimension(this.JLIST_SPACING, 10)));
		}
	}
	
	private void mergeInputPriorities(){
		LogicalModel m = this.selectedModel;
		ModelPriorityClasses mpc = this.userPriorityClasses.getModelPriorityClasses(m);
		List<String> vars = mpc.getClassVars(0);
		boolean allInputFlag = true;
		for (String var : vars) {
			String tmpVar = var;
			if (var.contains("+") | var.contains("-")) {
				tmpVar = var.split("\\[")[0];
			}
			if (!this.projectFeatures.getNodeInfo(tmpVar, m).isInput()) {
				allInputFlag = false;
			}
		}
		if (allInputFlag==true & mpc.size() > 1) {
			List<String> class2Merge = mpc.getClassVars(1);
			this.userPriorityClasses.getModelPriorityClasses(m).incPriorities(1, class2Merge);
		}
	}

	@Override
	protected void buttonReset() {
		this.userPriorityClasses = new EpitheliumUpdateSchemeIntra();
		for (LogicalModel m : this.epithelium.getEpitheliumGrid().getModelSet()) {
			this.userPriorityClasses.addModelPriorityClasses(this.epithelium.getPriorityClasses(m).clone());
		}
		this.updatePriorityList(this.selectedModel);
		// Repaint
		this.getParent().repaint();
	}

	@Override
	protected void buttonAccept() {
		for (LogicalModel m : this.userPriorityClasses.getModelSet()) {
			ModelPriorityClasses clone = this.userPriorityClasses.getModelPriorityClasses(m).clone();
			this.epithelium.setPriorityClasses(clone);
		}
	}

	@Override
	protected boolean isChanged() {
		for (LogicalModel m : this.epithelium.getEpitheliumGrid().getModelSet()) {
			ModelPriorityClasses clone = this.userPriorityClasses.getModelPriorityClasses(m);
			ModelPriorityClasses orig = this.epithelium.getPriorityClasses(m);
			if (!clone.equals(orig))
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
		this.jpNorthLeft.add(new JLabel("Model:"));
		this.jpNorthLeft.add(this.newModelCombobox(modelList));
		this.updatePriorityList(modelList.get(0));
	}

	@Override
	public void hyperlinkUpdate(HyperlinkEvent event) {
		if (event.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
			Web.openURI(event.getDescription());
		}
	}
}
