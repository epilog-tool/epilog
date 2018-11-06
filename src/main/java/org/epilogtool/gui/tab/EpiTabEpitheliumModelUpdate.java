package org.epilogtool.gui.tab;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

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
	private EpitheliumEvents epiEventClone;
	private LogicalModel selectedModel;
	private TabProbablyChanged tpc;

	private JPanel jpAlpha;
	private JScrollPane jspAlpha;
	private JSlider jAlphaSlide;
	private JLabel jAlphaLabelValue;

	private JComboBox<UpdateCells> jcbUpdateCells;
	private JComboBox<EnumRandomSeed> jcbRandomSeedType;
	
	private Map<String, JRadioButton> mName2JrbCell;
	private Map<String, JRadioButton> mName2JrbEventOrder;
	private Map<String, JRadioButton> mName2JrbDeath;

	public EpiTabEpitheliumModelUpdate(Epithelium e, TreePath path, TabChangeNotifyProj tabChanged) {
		super(e, path, tabChanged);
	}

	public void initialize() {

		this.center.setLayout(new BoxLayout(this.center, BoxLayout.Y_AXIS));
		this.tpc = new TabProbablyChanged();
		
		this.mName2JrbCell = new HashMap<String, JRadioButton>();
		this.mName2JrbEventOrder = new HashMap<String, JRadioButton>();
		this.mName2JrbDeath= new HashMap<String, JRadioButton>();

		this.updateSchemeInter = this.epithelium.getUpdateSchemeInter().clone();
		this.epiEventClone = this.epithelium.getEpitheliumEvents().clone();

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
		triggerOrderOptions.add(Txt.get("s_TAB_EPIUPDATE_ORDER_DIVDEATH"));
		triggerOrderOptions.add(Txt.get("s_TAB_EPIUPDATE_ORDER_DEATHDIV"));
		triggerOrderOptions.add(Txt.get("s_TAB_EPIUPDATE_ORDER_RANDOM"));
		
		jpOrder.setBorder(BorderFactory.createTitledBorder(Txt.get("s_TAB_EPIUPDATE_ORDER")));
		ButtonGroup groupOrder = new ButtonGroup();
		
		for (String triggerOption: triggerOrderOptions) {
			JRadioButton jrb = new JRadioButton(triggerOption);
			jrb.setName(triggerOption);
			this.mName2JrbEventOrder.put(triggerOption,jrb);
			if(triggerOption.equals(this.epiEventClone.getEventOrder()))
				jrb.setSelected(true);
				jrb.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						JRadioButton jrb = (JRadioButton)e.getSource();
						updateEventOrder(jrb.getName());
						tpc.setChanged();
					}
				});
				jpOrder.add(jrb);
				groupOrder.add(jrb);
		}
		
		boolean control = false;
		for (String jrbName : mName2JrbEventOrder.keySet()) {
			if(jrbName.equals(this.epiEventClone.getEventOrder())) {
				mName2JrbEventOrder.get(jrbName).setSelected(true);
				control = true;
			}
		}
		if (!control)
			mName2JrbEventOrder.get(Txt.get("s_TAB_EPIUPDATE_ORDER_DIVDEATH")).setSelected(true);
		
		this.center.add(jpOrder);

		//New Cell Options
		
		JPanel jpNewCell = new JPanel ();

		List<String> triggerCellOptions = new ArrayList<String>();
		
		triggerCellOptions.add(Txt.get("s_TAB_EPIUPDATE_NEWCELLSTATE_SAME"));
		triggerCellOptions.add(Txt.get("s_TAB_EPIUPDATE_NEWCELLSTATE_NAIVE"));
		triggerCellOptions.add(Txt.get("s_TAB_EPIUPDATE_NEWCELLSTATE_PREDEFINED"));
		triggerCellOptions.add(Txt.get("s_TAB_EPIUPDATE_NEWCELLSTATE_RANDOM"));
		
		jpNewCell.setBorder(BorderFactory.createTitledBorder(Txt.get("s_TAB_EPIUPDATE_NEWCELLSTATE")));
		ButtonGroup groupCell = new ButtonGroup();
		
		for (String triggerOption: triggerCellOptions) {
			JRadioButton jrb = new JRadioButton(triggerOption);
			jrb.setName(triggerOption);
			if(triggerOption.equals(this.epiEventClone.getDivisionOption()))
				jrb.setSelected(true);
			this.mName2JrbCell.put(triggerOption,jrb);
				jrb.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						JRadioButton jrb = (JRadioButton)e.getSource();
						updateNewCellState(jrb.getName());
						tpc.setChanged();
					}
				});
				jpNewCell.add(jrb);
				groupCell.add(jrb);
		}
		
		control = false;
		for (String jrbName : mName2JrbCell.keySet()) {
			if(jrbName.equals(this.epiEventClone.getDivisionOption())) {
				mName2JrbCell.get(jrbName).setSelected(true);
				control = true;
			}
		}
		if (!control)
			mName2JrbCell.get(Txt.get("s_TAB_EPIUPDATE_NEWCELLSTATE_RANDOM")).setSelected(true);
		
		this.center.add(jpNewCell);
		
		// *******************
		// CELL DEATH OPTIONS
		// *******************
		
		JPanel jpCellDeath= new JPanel ();

		List<String> triggerDeathOptions = new ArrayList<String>();
		triggerDeathOptions.add(Txt.get("s_TAB_EPIUPDATE_CELLDEATH_EMPTY"));
		triggerDeathOptions.add(Txt.get("s_TAB_EPIUPDATE_CELLDEATH_PERMANENT"));
		triggerDeathOptions.add(Txt.get("s_TAB_EPIUPDATE_CELLDEATH_RANDOM"));

		
		jpCellDeath.setBorder(BorderFactory.createTitledBorder(Txt.get("s_TAB_EPIUPDATE_CELLDEATH")));
		ButtonGroup groupDeath = new ButtonGroup();
		
		control = false;
		for (String triggerOption: triggerDeathOptions) {
			JRadioButton jrb = new JRadioButton(triggerOption);
			jrb.setName(triggerOption);
			if(triggerOption.equals(this.epiEventClone.getDeathOption()))
				jrb.setSelected(true);
			this.mName2JrbDeath.put(triggerOption,jrb);
				jrb.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						JRadioButton jrb = (JRadioButton)e.getSource();
						updateCellDeath(jrb.getName());
						tpc.setChanged();
					}
				});
				jpCellDeath.add(jrb);
				groupDeath.add(jrb);
		}
		
		for (String jrbName : mName2JrbDeath.keySet()) {
			if(jrbName.equals(this.epiEventClone.getDeathOption())) {
				mName2JrbDeath.get(jrbName).setSelected(true);
				control = true;
			}	
		}
		if (!control)
			mName2JrbDeath.get(Txt.get("s_TAB_EPIUPDATE_CELLDEATH_EMPTY")).setSelected(true);
		
		JLabel jlDeathNeighboursRange = new JLabel();
		jlDeathNeighboursRange.setText(Txt.get("s_TAB_EPIUPDATE_DEATHRANGE"));
		jpCellDeath.add(jlDeathNeighboursRange);
		
		JComboBox<Integer> jcbNeighboursRange = new JComboBox<Integer>();
		for (int d = 1; d <= Math.max(this.epithelium.getEpitheliumGrid().getX(),
				this.epithelium.getEpitheliumGrid().getY()); d++) {
			jcbNeighboursRange.addItem(d);
		}
		jcbNeighboursRange.setSelectedItem(this.epiEventClone.getDeathNeighbourRange());

		jcbNeighboursRange.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				@SuppressWarnings("unchecked")
				JComboBox jcb = (JComboBox) e.getSource();
				updateNeighboursRange((int) jcb.getSelectedItem());
			}
		});
		
		jpCellDeath.add(jcbNeighboursRange);
		this.center.add(jpCellDeath);

		this.isInitialized = true;
	}

	protected void updateNeighboursRange(int selectedItem) {
		if (this.isInitialized)
			tpc.setChanged();
		
		this.epiEventClone.setDeathNeighbourRange(selectedItem);
		
	}

	private void updateEventOrder(String string) {
		this.epiEventClone.setEventOrder(string);
	}
	
	private void updateNewCellState(String string) {
		this.epiEventClone.setDivisionOption(string);
	}
	
	private void updateCellDeath(String string) {
		this.epiEventClone.setDeathOption(string);
	}
	
	

	private void updateJCBUpdateCells() {
		UpdateCells upCells = this.updateSchemeInter.getUpdateCells();
		for (int i = 0; i < this.jcbUpdateCells.getItemCount(); i++) {
			if (upCells != null && upCells.equals(this.jcbUpdateCells.getItemAt(i)))
				this.jcbUpdateCells.setSelectedIndex(i);
		}
	}
	
	private void updateJRBEventOrder() {
		String eventOrder = this.epiEventClone.getEventOrder();
		for (String s : this.mName2JrbEventOrder.keySet()) {
			if (s.equals(eventOrder)) {
				this.mName2JrbEventOrder.get(s).setSelected(true);}
		}
	}
	
	private void updateJRBDeath() {
		String death = this.epiEventClone.getDeathOption();
		for (String s : this.mName2JrbDeath.keySet()) {
			if (s.equals(death)) {
				this.mName2JrbDeath.get(s).setSelected(true);}
		}
	}
	
	private void updateJRBNewCell() {
		String newCellType = this.epiEventClone.getDivisionOption();
		for (String s : this.mName2JrbCell.keySet()) {
			if (s.equals(newCellType)) {
				this.mName2JrbCell.get(s).setSelected(true);}
		}
	}

	
	private void updateJCBRandomSeedType() {
		EnumRandomSeed seedType = this.updateSchemeInter.getRandomSeedType();
		for (int i = 0; i < this.jcbRandomSeedType.getItemCount(); i++) {
			if (seedType != null && seedType.equals(this.jcbRandomSeedType.getItemAt(i))) {
				this.jcbRandomSeedType.setSelectedIndex(i);
			break;
			}
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
		this.updateSchemeInter.setRandomSeedType(this.epithelium.getUpdateSchemeInter().getRandomSeedType());
		
		this.updateEventOrder(this.epithelium.getEpitheliumEvents().getEventOrder());
		this.updateCellDeath(this.epithelium.getEpitheliumEvents().getDeathOption());
		this.updateNewCellState(this.epithelium.getEpitheliumEvents().getDivisionOption());
		
		this.updateNeighboursRange(this.epithelium.getEpitheliumEvents().getDeathNeighbourRange());
		
		
		this.updateJCBUpdateCells();
		this.updateJCBRandomSeedType();
		this.updateJRBEventOrder();
		this.updateJRBNewCell();
		this.updateJRBDeath();
	
		// Repaint
		this.getParent().repaint();
	}

	@Override
	protected void buttonAccept() {
		this.epithelium.getUpdateSchemeInter().setAlpha(this.updateSchemeInter.getAlpha());
		this.epithelium.getUpdateSchemeInter().setUpdateCells(this.updateSchemeInter.getUpdateCells());
		this.epithelium.getUpdateSchemeInter().setRandomSeedType(this.updateSchemeInter.getRandomSeedType());
		this.epithelium.getUpdateSchemeInter().setRandomSeed(this.updateSchemeInter.getRandomSeed());
		
		this.epithelium.getEpitheliumEvents().setEventOrder(this.epiEventClone.getEventOrder());
		this.epithelium.getEpitheliumEvents().setDivisionOption(this.epiEventClone.getDivisionOption());
		this.epithelium.getEpitheliumEvents().setDeathOption(this.epiEventClone.getDeathOption());
		this.epithelium.getEpitheliumEvents().setDeathNeighbourRange(this.epiEventClone.getDeathNeighbourRange());
		

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
				.equals(this.epiEventClone.getEventOrder()))
			return true;
		if (!this.epithelium.getEpitheliumEvents().getDivisionOption()
				.equals(this.epiEventClone.getDivisionOption()))
			return true;
		if (!this.epithelium.getEpitheliumEvents().getDeathOption()
				.equals(this.epiEventClone.getDeathOption()))
			return true;
		if (this.epithelium.getEpitheliumEvents().getDeathNeighbourRange()
				!=(this.epiEventClone.getDeathNeighbourRange()))
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
		
		this.epiEventClone.setEventOrder(this.epithelium.getEpitheliumEvents().getEventOrder());
		this.epiEventClone.setDivisionOption(this.epithelium.getEpitheliumEvents().getDivisionOption());
		this.epiEventClone.setDeathOption(this.epithelium.getEpitheliumEvents().getDeathOption());
		this.epiEventClone.setDeathNeighbourRange(this.epithelium.getEpitheliumEvents().getDeathNeighbourRange());

		this.getParent().repaint();
	}

	@Override
	public void hyperlinkUpdate(HyperlinkEvent event) {
		if (event.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
			Web.openURI(event.getDescription());
		}
	}
}
