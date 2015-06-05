package org.ginsim.epilog.gui.tab;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.tree.TreePath;

import org.colomoto.logicalmodel.LogicalModel;
import org.ginsim.epilog.common.Web;
import org.ginsim.epilog.core.Epithelium;
import org.ginsim.epilog.core.EpitheliumUpdateSchemeInter;
import org.ginsim.epilog.core.EpitheliumUpdateSchemeIntra;
import org.ginsim.epilog.core.ModelPriorityClasses;
import org.ginsim.epilog.gui.EpiGUI.EpiTabChanged;
import org.ginsim.epilog.io.ButtonFactory;
import org.ginsim.epilog.project.ProjectModelFeatures;

public class EpiTabUpdateScheme extends EpiTabDefinitions implements
		HyperlinkListener {
	private static final long serialVersionUID = 1176575422084167530L;

	private final int JLIST_LINES = 10;
	private final int JLIST_WIDTH = 65;
	private final int JLIST_SPACING = 15;

	private final int SLIDER_MIN = 0;
	private final int SLIDER_MAX = 100;
	private final int SLIDER_STEP = 10;

	private EpitheliumUpdateSchemeInter updateSchemeInter;
	private EpitheliumUpdateSchemeIntra userPriorityClasses;
	private LogicalModel selectedModel;
	private List<JList<String>> guiClasses;

	private JPanel jpIntraCenter;
	private JPanel jpTLeft;
	private JLabel jlabelScheme;
	private JSlider jSlide;

	public EpiTabUpdateScheme(Epithelium e, TreePath path,
			EpiTabChanged tabChanged, ProjectModelFeatures modelFeatures) {
		super(e, path, tabChanged, modelFeatures);
	}

	public void initialize() {
		this.center.setLayout(new BorderLayout());
		List<LogicalModel> modelList = new ArrayList<LogicalModel>(
				this.epithelium.getEpitheliumGrid().getModelSet());
		this.userPriorityClasses = new EpitheliumUpdateSchemeIntra();
		for (LogicalModel m : modelList) {
			this.userPriorityClasses.addModelPriorityClasses(this.epithelium
					.getPriorityClasses(m).clone());
		}
		this.updateSchemeInter = this.epithelium.getUpdateSchemeInter().clone();
		this.guiClasses = new ArrayList<JList<String>>();

		// ******************
		// * Intra-cellular *
		// ******************
		JPanel jpIntra = new JPanel(new BorderLayout());
		jpIntra.setBorder(BorderFactory.createTitledBorder("Intra-cellular"));
		this.center.add(jpIntra, BorderLayout.NORTH);

		JPanel jpIntraTop = new JPanel(new BorderLayout());
		// jpIntraTop.setBorder(BorderFactory.createTitledBorder("Model"));
		jpIntra.add(jpIntraTop, BorderLayout.NORTH);
		this.jpTLeft = new JPanel();
		jpIntraTop.add(this.jpTLeft, BorderLayout.LINE_START);
		JPanel jpTRight = new JPanel(new FlowLayout());
		// jpIntraTop.add(jpTRight, BorderLayout.CENTER);
		jpIntra.add(jpTRight, BorderLayout.SOUTH);

		this.jpTLeft.add(new JLabel("Model:"));
		JComboBox<String> jcbSBML = this.newModelCombobox(modelList);
		this.jpTLeft.add(jcbSBML);

		// Button display options
		JButton jbSplit = ButtonFactory.getNoMargins("Split");
		jbSplit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				splitSelVars();
				// Repaint
				getParent().repaint();
			}
		});
		jpTRight.add(jbSplit);
		JButton jbUnsplit = ButtonFactory.getNoMargins("Unsplit");
		jbUnsplit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				unsplitSelVars();
				// Repaint
				getParent().repaint();
			}
		});
		jpTRight.add(jbUnsplit);
		JButton jbInc = ButtonFactory.getNoMargins("<-");
		jbInc.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				incPriorityOfSelVars();
				// Repaint
				getParent().repaint();
			}
		});
		jpTRight.add(jbInc);
		JButton jbDec = ButtonFactory.getNoMargins("->");
		jbDec.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				decPriorityOfSelVars();
				// Repaint
				getParent().repaint();
			}
		});
		jpTRight.add(jbDec);
		JButton jbSingle = ButtonFactory.getNoMargins("Single class");
		jbSingle.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				createSingleClass();
				// Repaint
				getParent().repaint();
			}
		});
		jpTRight.add(jbSingle);

		this.jpIntraCenter = new JPanel(new FlowLayout());
		// this.jpIntraCenter.setBorder(BorderFactory
		// .createTitledBorder("Priority sets"));
		jpIntra.add(this.jpIntraCenter, BorderLayout.CENTER);

		// Panel just to glue Inter-cellular to Intra-cellular ;)
		JPanel jpCenterBottom = new JPanel(new BorderLayout());
		this.center.add(jpCenterBottom, BorderLayout.CENTER);

		// ******************
		// * Inter-cellular *
		// ******************
		jpCenterBottom.add(new JSeparator(JSeparator.HORIZONTAL),
				BorderLayout.NORTH);
		JPanel jpInter = new JPanel();
		jpInter.setLayout(new BoxLayout(jpInter, BoxLayout.PAGE_AXIS));
		jpInter.setBorder(BorderFactory.createTitledBorder("Inter-cellular"));
		jpCenterBottom.add(jpInter, BorderLayout.CENTER);

		JEditorPane jPane = new JEditorPane();
		jpInter.add(jPane);
		jPane.setContentType("text/html");
		jPane.setEditable(false);
		jPane.setEnabled(true);
		jPane.setBackground(jpInter.getBackground());
		jPane.addHyperlinkListener(this);
		jPane.setText("Here we consider an updating scheme named &alpha;-asyncronism "
				+ "(see <a href=\"http://dx.doi.org/10.1007/978-3-642-40867-0_2\">"
				+ "doi:10.1007/978-3-642-40867-0_2</a>).<br/>"
				+ "It consists in updating each cell with probability &alpha;, the "
				+ "synchrony rate, leaving the state of the cells unchanged otherwise.");

		jpInter.add(new JLabel(" "));

		JPanel jpInterAlpha = new JPanel(new BorderLayout());
		jpInterAlpha
				.add(new JLabel("Current alpha: "), BorderLayout.LINE_START);
		this.jlabelScheme = new JLabel("--");
		jpInterAlpha.add(this.jlabelScheme, BorderLayout.CENTER);
		jpInter.add(jpInterAlpha);

		// JSlider for alpha-asynchronism
		JPanel jpInterSlider = new JPanel(new BorderLayout());
		jpInterSlider.add(new JLabel("Value: "), BorderLayout.LINE_START);
		this.jSlide = new JSlider(JSlider.HORIZONTAL, this.SLIDER_MIN,
				this.SLIDER_MAX, this.SLIDER_MAX);
		this.jSlide.setMajorTickSpacing(this.SLIDER_STEP);
		this.jSlide.setMinorTickSpacing(1);
		this.jSlide.setPaintTicks(true);
		this.jSlide.setPaintLabels(true);
		Hashtable<Integer, JLabel> labelTable = new Hashtable<Integer, JLabel>();
		for (int i = this.SLIDER_MIN; i <= this.SLIDER_MAX; i += this.SLIDER_STEP) {
			labelTable.put(new Integer(i), new JLabel(""
					+ ((float) i / this.SLIDER_MAX)));
		}
		this.jSlide.setLabelTable(labelTable);
		this.jSlide.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				JSlider slide = (JSlider) e.getSource();
				updateAlpha(slide.getValue());
			}
		});
		this.jSlide
				.setValue((int) (this.updateSchemeInter.getAlpha() * SLIDER_MAX));
		updateAlpha(this.jSlide.getValue());
		jpInterSlider.add(this.jSlide, BorderLayout.CENTER);
		jpInter.add(jpInterSlider);

		LogicalModel m = this.modelFeatures.getModel((String) jcbSBML
				.getSelectedItem());
		this.updatePriorityList(m);
		this.isInitialized = true;
	}

	private void updateAlpha(int sliderValue) {
		float value = (float) sliderValue / SLIDER_MAX;
		this.updateSchemeInter.setAlpha(value);
		String sTmp = "" + value;
		if (sliderValue == SLIDER_MIN) {
			sTmp += " (asynchronous)";
		} else if (sliderValue == SLIDER_MAX) {
			sTmp += " (synchronous)";
		}
		jlabelScheme.setText(sTmp);
	}

	private JComboBox<String> newModelCombobox(List<LogicalModel> modelList) {
		// Model selection list
		String[] saSBML = new String[modelList.size()];
		for (int i = 0; i < modelList.size(); i++) {
			saSBML[i] = this.modelFeatures.getName(modelList.get(i));
		}
		JComboBox<String> jcb = new JComboBox<String>(saSBML);
		jcb.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				@SuppressWarnings("unchecked")
				JComboBox<String> jcb = (JComboBox<String>) e.getSource();
				LogicalModel m = modelFeatures.getModel((String) jcb
						.getSelectedItem());
				updatePriorityList(m);
				// Re-Paint
				getParent().repaint();
			}
		});
		return jcb;
	}

	private void splitSelVars() {
		ModelPriorityClasses mpc = this.userPriorityClasses
				.getModelPriorityClasses(this.selectedModel);
		for (int i = 0; i < this.guiClasses.size(); i++) {
			List<String> values = this.guiClasses.get(i)
					.getSelectedValuesList();
			if (!values.isEmpty()) {
				for (String var : values)
					mpc.split(i, var);
				break;
			}
		}
		this.updatePriorityList(this.selectedModel);
	}

	private void unsplitSelVars() {
		ModelPriorityClasses mpc = this.userPriorityClasses
				.getModelPriorityClasses(this.selectedModel);
		for (int i = 0; i < this.guiClasses.size(); i++) {
			List<String> values = this.guiClasses.get(i)
					.getSelectedValuesList();
			if (!values.isEmpty()) {
				for (String var : values)
					mpc.unsplit(i, var);
				break;
			}
		}
		this.updatePriorityList(this.selectedModel);
	}

	private void incPriorityOfSelVars() {
		ModelPriorityClasses mpc = this.userPriorityClasses
				.getModelPriorityClasses(this.selectedModel);
		for (int i = 0; i < this.guiClasses.size(); i++) {
			List<String> values = this.guiClasses.get(i)
					.getSelectedValuesList();
			if (!values.isEmpty()) {
				mpc.incPriorities(i, values);
				break;
			}
		}
		this.updatePriorityList(this.selectedModel);
	}

	private void decPriorityOfSelVars() {
		ModelPriorityClasses mpc = this.userPriorityClasses
				.getModelPriorityClasses(this.selectedModel);
		for (int i = 0; i < this.guiClasses.size(); i++) {
			List<String> values = this.guiClasses.get(i)
					.getSelectedValuesList();
			if (!values.isEmpty()) {
				mpc.decPriorities(i, values);
				break;
			}
		}
		this.updatePriorityList(this.selectedModel);
	}

	private void createSingleClass() {
		this.userPriorityClasses.getModelPriorityClasses(this.selectedModel)
				.singlePriorityClass();
		this.updatePriorityList(this.selectedModel);
	}

	// FIXME
	private void updatePriorityList(LogicalModel m) {
		this.jpIntraCenter.removeAll();
		this.guiClasses.clear();
		this.selectedModel = m;
		ModelPriorityClasses mpc = this.userPriorityClasses
				.getModelPriorityClasses(m);

		for (int idxPC = 0; idxPC < mpc.size(); idxPC++) {
			JPanel jpRankBlock = new JPanel(new BorderLayout());
			JLabel jlTmp = new JLabel("Rank " + (idxPC + 1), SwingConstants.CENTER);
			jpRankBlock.add(jlTmp, BorderLayout.NORTH);

			DefaultListModel<String> lModel = new DefaultListModel<String>();
			List<String> vars = mpc.getClassVars(idxPC);
			// -- Order variables alphabetically
			Collections.sort(vars, String.CASE_INSENSITIVE_ORDER);
			for (String var : vars) {
				lModel.addElement(var);
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
			jpRankBlock.add(new JLabel(label, SwingConstants.CENTER),
					BorderLayout.SOUTH);

			this.jpIntraCenter.add(jpRankBlock);
			this.jpIntraCenter.add(Box.createRigidArea(new Dimension(
					this.JLIST_SPACING, 10)));
		}
	}

	@Override
	protected void buttonReset() {
		this.userPriorityClasses = new EpitheliumUpdateSchemeIntra();
		for (LogicalModel m : this.epithelium.getEpitheliumGrid().getModelSet()) {
			this.userPriorityClasses.addModelPriorityClasses(this.epithelium
					.getPriorityClasses(m).clone());
		}
		this.updatePriorityList(this.selectedModel);
		this.jSlide.setValue((int) (this.epithelium.getUpdateSchemeInter()
				.getAlpha() * SLIDER_MAX));
		this.updateAlpha(this.jSlide.getValue());
		// Repaint
		this.getParent().repaint();
	}

	@Override
	protected void buttonAccept() {
		for (LogicalModel m : this.userPriorityClasses.getModelSet()) {
			ModelPriorityClasses clone = this.userPriorityClasses
					.getModelPriorityClasses(m).clone();
			this.epithelium.setPriorityClasses(clone);
		}
		this.epithelium.getUpdateSchemeInter().setAlpha(
				this.updateSchemeInter.getAlpha());
	}

	@Override
	protected boolean isChanged() {
		for (LogicalModel m : this.epithelium.getEpitheliumGrid().getModelSet()) {
			ModelPriorityClasses clone = this.userPriorityClasses
					.getModelPriorityClasses(m);
			ModelPriorityClasses orig = this.epithelium.getPriorityClasses(m);
			if (!clone.equals(orig))
				return true;
		}
		if (this.epithelium.getUpdateSchemeInter().getAlpha() != this.updateSchemeInter
				.getAlpha())
			return true;
		return false;
	}

	@Override
	public void notifyChange() {
		if (!this.isInitialized)
			return;
		List<LogicalModel> modelList = new ArrayList<LogicalModel>(
				this.epithelium.getEpitheliumGrid().getModelSet());
		EpitheliumUpdateSchemeIntra newPCs = new EpitheliumUpdateSchemeIntra();
		for (LogicalModel m : modelList) {
			if (this.userPriorityClasses.getModelSet().contains(m)) {
				// Already exists
				newPCs.addModelPriorityClasses(this.userPriorityClasses
						.getModelPriorityClasses(m));
			} else {
				// Adds a new one
				newPCs.addModel(m);
			}
		}
		this.userPriorityClasses = newPCs;
		this.jpTLeft.removeAll();
		this.jpTLeft.add(new JLabel("Model:"));
		this.jpTLeft.add(this.newModelCombobox(modelList));
		this.updatePriorityList(modelList.get(0));
	}

	@Override
	public void hyperlinkUpdate(HyperlinkEvent event) {
		if (event.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
			Web.openURI(event.getDescription());
		}
	}
}
