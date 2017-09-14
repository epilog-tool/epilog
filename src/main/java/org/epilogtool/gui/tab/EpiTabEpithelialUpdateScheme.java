package org.epilogtool.gui.tab;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.tree.TreePath;

import org.colomoto.logicalmodel.LogicalModel;
import org.epilogtool.common.RandCentral;
import org.epilogtool.common.Txt;
import org.epilogtool.common.EnumRandomSeed;
import org.epilogtool.common.Web;
import org.epilogtool.core.Epithelium;
import org.epilogtool.core.EpitheliumUpdateSchemeInter;
import org.epilogtool.core.UpdateCells;
import org.epilogtool.gui.EpiGUI.ProjChangeNotifyTab;
import org.epilogtool.gui.EpiGUI.TabChangeNotifyProj;
import org.epilogtool.gui.widgets.JComboWideBox;

public class EpiTabEpithelialUpdateScheme extends EpiTabDefinitions implements HyperlinkListener {
	private static final long serialVersionUID = 1176575422084167530L;

	private final int SLIDER_MIN = 0;
	private final int SLIDER_MAX = 100;
	private final int SLIDER_STEP = 10;

	private EpitheliumUpdateSchemeInter updateSchemeInter;
	private LogicalModel selectedModel;
	private TabProbablyChanged tpc;

	private JPanel jpAlpha;
	private JScrollPane jspAlpha;
	private JSlider jAlphaSlide;
	private JLabel jAlphaLabelValue;

	private JComboBox<UpdateCells> jcbUpdateCells;
	private JComboBox<EnumRandomSeed> jcbRandomSeedType;

	public EpiTabEpithelialUpdateScheme(Epithelium e, TreePath path, ProjChangeNotifyTab projChanged,
			TabChangeNotifyProj tabChanged) {
		super(e, path, projChanged, tabChanged);
	}

	public void initialize() {

		this.center.setLayout(new BorderLayout());
		this.tpc = new TabProbablyChanged();

		this.updateSchemeInter = this.epithelium.getUpdateSchemeInter().clone();

		// Alpha asynchronism panel
		this.jpAlpha = new JPanel(new BorderLayout());
		this.jpAlpha.setPreferredSize(new Dimension(400, 100));
		this.jpAlpha.setMaximumSize(new Dimension(5000, 100));
		this.jpAlpha.setMinimumSize(new Dimension(100, 100));
		this.jspAlpha = new JScrollPane(this.jpAlpha);
		this.jspAlpha.setBorder(BorderFactory.createEmptyBorder());
		this.center.add(this.jspAlpha, BorderLayout.NORTH);
		this.jpAlpha.setBorder(BorderFactory.createTitledBorder(Txt.get("s_TAB_ALPHA_TITLE")));

		// JSlider for alpha-asynchronism
		this.generateAlphaSlider();

		// Update all/updatable cells
		JPanel jpUpdateCells = new JPanel(new BorderLayout());
		jpUpdateCells.setBorder(BorderFactory.createTitledBorder(UpdateCells.title()));
		this.jcbUpdateCells = new JComboWideBox<UpdateCells>(
				new UpdateCells[] { UpdateCells.UPDATABLECELLS, UpdateCells.ALLCELLS });
		this.updateJCBUpdateCells();
		this.jcbUpdateCells.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				@SuppressWarnings("unchecked")
				JComboBox<UpdateCells> jcCells2Update = (JComboBox<UpdateCells>) e.getSource();
				updateSchemeInter.setUpdateCells((UpdateCells) jcCells2Update.getSelectedItem());
				tpc.setChanged();
			}
		});
		jpUpdateCells.add(this.jcbUpdateCells);

		// Random seed
		JPanel jpRandomSeedType = new JPanel(new BorderLayout());
		jpRandomSeedType.setBorder(BorderFactory.createTitledBorder(EnumRandomSeed.title()));
		this.jcbRandomSeedType = new JComboWideBox<EnumRandomSeed>(
				new EnumRandomSeed[] { EnumRandomSeed.FIXED, EnumRandomSeed.RANDOM });
		this.updateJCBRandomSeedType();
		this.jcbRandomSeedType.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				@SuppressWarnings("unchecked")
				JComboBox<EnumRandomSeed> jcRandomSeedType = (JComboBox<EnumRandomSeed>) e.getSource();
				updateSchemeInter.setRandomSeedType((EnumRandomSeed) jcRandomSeedType.getSelectedItem());
				int seed = RandCentral.getInstance().nextInt();
				updateSchemeInter.setRandomSeed(seed >> 1 + seed);
				tpc.setChanged();
			}
		});
		jpRandomSeedType.add(this.jcbRandomSeedType);

		JPanel southPanel = new JPanel(new BorderLayout());
		southPanel.add(jpUpdateCells, BorderLayout.NORTH);
		southPanel.add(jpRandomSeedType, BorderLayout.SOUTH);

		this.center.add(southPanel, BorderLayout.SOUTH);
		this.isInitialized = true;
	}

	private void updateJCBUpdateCells() {
		UpdateCells upCells = this.updateSchemeInter.getUpdateCells();
		for (int i = 0; i < this.jcbUpdateCells.getItemCount(); i++) {
			if (upCells != null && upCells.equals(this.jcbUpdateCells.getItemAt(i)))
				this.jcbUpdateCells.setSelectedIndex(i);
		}
	}

	private void updateJCBRandomSeedType() {
		EnumRandomSeed seedType = this.updateSchemeInter.getRandomSeedType();
		for (int i = 0; i < this.jcbRandomSeedType.getItemCount(); i++) {
			if (seedType != null && seedType.equals(this.jcbRandomSeedType.getItemAt(i)))
				this.jcbRandomSeedType.setSelectedIndex(i);
		}
	}

	private void generateAlphaSlider() {
		JPanel jpAlphaInfo = new JPanel(new BorderLayout());
		jpAlphaInfo.add(new JLabel(Txt.get("s_TAB_ALPHA_CURR")), BorderLayout.LINE_START);
		this.jAlphaLabelValue = new JLabel("--");
		jpAlphaInfo.add(this.jAlphaLabelValue, BorderLayout.CENTER);
		jpAlpha.add(jpAlphaInfo, BorderLayout.CENTER);

		JPanel jpAlphaSlider = new JPanel(new BorderLayout());
//		jpAlphaSlider.add(new JLabel("Value: "), BorderLayout.LINE_START);
		this.jAlphaSlide = new JSlider(JSlider.HORIZONTAL, this.SLIDER_MIN, this.SLIDER_MAX, this.SLIDER_MAX);
		this.jAlphaSlide.setMajorTickSpacing(this.SLIDER_STEP);
		this.jAlphaSlide.setMinorTickSpacing(1);
		this.jAlphaSlide.setPaintTicks(true);
		this.jAlphaSlide.setPaintLabels(true);
		Hashtable<Integer, JLabel> labelTable = new Hashtable<Integer, JLabel>();
		for (int i = this.SLIDER_MIN; i <= this.SLIDER_MAX; i += this.SLIDER_STEP) {
			labelTable.put(new Integer(i), new JLabel("" + ((float) i / this.SLIDER_MAX)));
		}
		this.jAlphaSlide.setLabelTable(labelTable);
		this.jAlphaSlide.setValue((int) (this.updateSchemeInter.getAlpha() * SLIDER_MAX));
		this.jAlphaSlide.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				JSlider slide = (JSlider) e.getSource();
				updateAlpha(slide.getValue());
				tpc.setChanged();
			}
		});
		updateAlpha(this.jAlphaSlide.getValue());
		jpAlphaSlider.add(this.jAlphaSlide, BorderLayout.CENTER);
		jpAlpha.add(jpAlphaSlider, BorderLayout.SOUTH);
	}

	private void updateAlpha(int sliderValue) {
		float value = (float) sliderValue / SLIDER_MAX;
		this.updateSchemeInter.setAlpha(value);
		String sTmp = "" + value;
		if (sliderValue == SLIDER_MIN) {
			sTmp += " " + Txt.get("s_TAB_ALPHA_ASYNC");
		} else if (sliderValue == SLIDER_MAX) {
			sTmp += " " + Txt.get("s_TAB_ALPHA_SYNC");
		}
		jAlphaLabelValue.setText(sTmp);
	}

	@Override
	protected void buttonReset() {
		this.jAlphaSlide.setValue((int) (this.epithelium.getUpdateSchemeInter().getAlpha() * SLIDER_MAX));
		this.updateAlpha(this.jAlphaSlide.getValue());
		this.updateSchemeInter.setUpdateCells(this.epithelium.getUpdateSchemeInter().getUpdateCells());
		this.updateJCBUpdateCells();
		// Repaint
		this.getParent().repaint();
	}

	@Override
	protected void buttonAccept() {
		this.epithelium.getUpdateSchemeInter().setAlpha(this.updateSchemeInter.getAlpha());
		this.epithelium.getUpdateSchemeInter().setUpdateCells(this.updateSchemeInter.getUpdateCells());
		this.epithelium.getUpdateSchemeInter().setRandomSeedType(this.updateSchemeInter.getRandomSeedType());
		this.epithelium.getUpdateSchemeInter().setRandomSeed(this.updateSchemeInter.getRandomSeed());
	}

	@Override
	protected boolean isChanged() {
		if (this.epithelium.getUpdateSchemeInter().getAlpha() != this.updateSchemeInter.getAlpha())
			return true;
		if (!this.epithelium.getUpdateSchemeInter().getUpdateCells().equals(this.updateSchemeInter.getUpdateCells()))
			return true;
		if (!this.epithelium.getUpdateSchemeInter().getRandomSeedType()
				.equals(this.updateSchemeInter.getRandomSeedType()))
			return true;
		return false;
	}

	@Override
	public void applyChange() {
		List<LogicalModel> modelList = new ArrayList<LogicalModel>(this.epithelium.getEpitheliumGrid().getModelSet());
		if (!modelList.contains(this.selectedModel)) {
			this.selectedModel = modelList.get(0);
		}
		this.updateSchemeInter.setUpdateCells(this.epithelium.getUpdateSchemeInter().getUpdateCells());
		this.updateSchemeInter.setRandomSeedType(this.epithelium.getUpdateSchemeInter().getRandomSeedType());
		this.updateSchemeInter.setRandomSeed(this.epithelium.getUpdateSchemeInter().getRandomSeed());
		this.getParent().repaint();
	}

	@Override
	public void hyperlinkUpdate(HyperlinkEvent event) {
		if (event.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
			Web.openURI(event.getDescription());
		}
	}
}
