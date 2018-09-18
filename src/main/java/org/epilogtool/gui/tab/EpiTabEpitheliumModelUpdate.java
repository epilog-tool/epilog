package org.epilogtool.gui.tab;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.tree.TreePath;

import org.colomoto.biolqm.LogicalModel;
import org.epilogtool.common.EnumRandomSeed;
import org.epilogtool.common.RandCentral;
import org.epilogtool.common.Txt;
import org.epilogtool.common.Web;
import org.epilogtool.core.Epithelium;
import org.epilogtool.core.EpitheliumEvents;
import org.epilogtool.core.EpitheliumUpdateSchemeInter;
import org.epilogtool.core.UpdateCells;
import org.epilogtool.gui.EpiGUI.TabChangeNotifyProj;
import org.epilogtool.gui.widgets.JComboWideBox;

public class EpiTabEpitheliumModelUpdate extends EpiTabDefinitions implements HyperlinkListener {
	private static final long serialVersionUID = 1176575422084167530L;

	private final int SLIDER_MIN = 0;
	private final int SLIDER_MAX = 100;
	private final int SLIDER_STEP = 10;

	private EpitheliumUpdateSchemeInter updateSchemeInter;
	private EpitheliumEvents epitheliumEvents;
	private LogicalModel selectedModel;
	private TabProbablyChanged tpc;

	private JPanel jpAlpha;
	private JScrollPane jspAlpha;
	private JSlider jAlphaSlide;
	private JLabel jAlphaLabelValue;

	private JComboBox<UpdateCells> jcbUpdateCells;
	private JComboBox<EnumRandomSeed> jcbRandomSeedType;

	public EpiTabEpitheliumModelUpdate(Epithelium e, TreePath path, TabChangeNotifyProj tabChanged) {
		super(e, path, tabChanged);
	}

	public void initialize() {

		this.center.setLayout(new BoxLayout(this.center, BoxLayout.Y_AXIS));
		this.tpc = new TabProbablyChanged();

		this.updateSchemeInter = this.epithelium.getUpdateSchemeInter().clone();
		this.epitheliumEvents = this.epithelium.getEpitheliumEvents().clone();

		// Alpha asynchronism panel
		this.jpAlpha = new JPanel(new BorderLayout());
		this.jpAlpha.setPreferredSize(new Dimension(400, 100));
		this.jpAlpha.setMaximumSize(new Dimension(5000, 100));
		this.jpAlpha.setMinimumSize(new Dimension(100, 100));
		this.jspAlpha = new JScrollPane(this.jpAlpha);
		this.jspAlpha.setBorder(BorderFactory.createEmptyBorder());
		this.jpAlpha.setBorder(BorderFactory.createTitledBorder(Txt.get("s_TAB_ALPHA_TITLE")));
		
		this.center.add(this.jspAlpha);

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

		this.center.add(jpUpdateCells);
		this.center.add(jpRandomSeedType);
		
		
		//Empty Panel
		
		JPanel jpEmpty = new JPanel ();
		jpEmpty.setPreferredSize(new Dimension(100,100));
		this.center.add(jpEmpty);
		//Event order Panel
		
		
		JPanel jpOrder = new JPanel ();

		List<String> triggerOrderOptions = new ArrayList<String>();
		triggerOrderOptions.add("Division first");
		triggerOrderOptions.add("Death first");
		triggerOrderOptions.add("Random order");
		
		jpOrder.setBorder(BorderFactory.createTitledBorder("Event Order"));
		ButtonGroup groupOrder = new ButtonGroup();
		
		for (String triggerOption: triggerOrderOptions) {
			JRadioButton jrb = new JRadioButton(triggerOption);
			jrb.setName(triggerOption);
//			triggerDivision2Radio.put(triggerOption,jrb);
				jrb.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						updateEventOrder(jrb);
						tpc.setChanged();
					}
				});
				jpOrder.add(jrb);
				groupOrder.add(jrb);
		}
		this.center.add(jpOrder);

		//New Cell Options
		
		JPanel jpNewCell = new JPanel ();

		List<String> triggerCellOptions = new ArrayList<String>();
		triggerCellOptions.add("Random");
		triggerCellOptions.add("Same");
		triggerCellOptions.add("Naive");
		triggerCellOptions.add("Predefined");
		
		jpNewCell.setBorder(BorderFactory.createTitledBorder("New Cell State"));
		ButtonGroup groupCell = new ButtonGroup();
		
		for (String triggerOption: triggerCellOptions) {
			JRadioButton jrb = new JRadioButton(triggerOption);
			jrb.setName(triggerOption);
				jrb.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						updateNewCellState(jrb);
						tpc.setChanged();
					}
				});
				jpNewCell.add(jrb);
				groupCell.add(jrb);
		}
		this.center.add(jpNewCell);
		
		//Cell death Options
		
		JPanel jpCellDeath= new JPanel ();

		List<String> triggerDeathOptions = new ArrayList<String>();
		triggerDeathOptions.add("Empty position");
		triggerDeathOptions.add("Permanent death");
		triggerDeathOptions.add("Random");

		
		jpCellDeath.setBorder(BorderFactory.createTitledBorder("Cell death options"));
		ButtonGroup groupDeath = new ButtonGroup();
		
		for (String triggerOption: triggerDeathOptions) {
			JRadioButton jrb = new JRadioButton(triggerOption);
			jrb.setName(triggerOption);
				jrb.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						updateCellDeath(jrb);
						tpc.setChanged();
					}
				});
				jpCellDeath.add(jrb);
				groupDeath.add(jrb);
		}
		this.center.add(jpCellDeath);

		this.isInitialized = true;
	}

	private void updateEventOrder(JRadioButton jrb) {
		this.epitheliumEvents.setEventOrder(jrb.getName());
	}
	
	private void updateNewCellState(JRadioButton jrb) {
		this.epitheliumEvents.setNewCellState(jrb.getName());
	}
	
	private void updateCellDeath(JRadioButton jrb) {
		this.epitheliumEvents.setDeathOption(jrb.getName());
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
		jpAlpha.add(this.jAlphaSlide, BorderLayout.SOUTH);
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
		
		this.epithelium.getEpitheliumEvents().setEventOrder(this.epitheliumEvents.getEventOrder());
		this.epithelium.getEpitheliumEvents().setNewCellState(this.epitheliumEvents.getNewCellState());
		this.epithelium.getEpitheliumEvents().setDeathOption(this.epitheliumEvents.getDeathOption());

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
		
		if (!this.epithelium.getEpitheliumEvents().getEventOrder()
				.equals(this.epitheliumEvents.getEventOrder()))
			return true;
		if (!this.epithelium.getEpitheliumEvents().getNewCellState()
				.equals(this.epitheliumEvents.getNewCellState()))
			return true;
		if (!this.epithelium.getEpitheliumEvents().getDeathOption()
				.equals(this.epitheliumEvents.getDeathOption()))
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
		
		this.epitheliumEvents.setEventOrder(this.epithelium.getEpitheliumEvents().getEventOrder());
		this.epitheliumEvents.setNewCellState(this.epithelium.getEpitheliumEvents().getNewCellState());
		this.epitheliumEvents.setDeathOption(this.epithelium.getEpitheliumEvents().getDeathOption());

		this.getParent().repaint();
	}

	@Override
	public void hyperlinkUpdate(HyperlinkEvent event) {
		if (event.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
			Web.openURI(event.getDescription());
		}
	}
}
