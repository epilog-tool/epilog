package org.epilogtool.gui.tab;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.tree.TreePath;

import org.colomoto.logicalmodel.LogicalModel;
import org.colomoto.logicalmodel.NodeInfo;
import org.epilogtool.common.Web;
import org.epilogtool.core.ComponentIntegrationFunctions;
import org.epilogtool.core.Epithelium;
import org.epilogtool.core.EpitheliumUpdateSchemeInter;
import org.epilogtool.gui.EpiGUI.EpiTabChanged;
import org.epilogtool.gui.EpiGUI.ProjectChangedInTab;
import org.epilogtool.integration.IntegrationFunctionEvaluation;
import org.epilogtool.integration.IntegrationFunctionSpecification.IntegrationExpression;
import org.epilogtool.project.ComponentPair;
import org.epilogtool.project.ProjectFeatures;

public class EpiTabEpithelialUpdateScheme extends EpiTabDefinitions implements HyperlinkListener {
	private static final long serialVersionUID = 1176575422084167530L;

	private final int SLIDER_MIN = 0;
	private final int SLIDER_MAX = 100;
	private final int SLIDER_STEP = 10;

	private EpitheliumUpdateSchemeInter updateSchemeInter;
	private LogicalModel selectedModel;

	private JPanel jpAlpha;
	private JPanel jpSigmaSliderPanel;
	private JScrollPane jspSigmaSliderScroller;
	private JSlider jAlphaSlide;
	private JLabel jAlphaLabelValue;
	
	private JPanel jpSigma;
	private JPanel jpSigmaModelSelection;
	
	private Map<ComponentPair, JSlider> mCP2Sliders;
	private Map<JSlider, Set<ComponentPair>> mSliders2CP;
	private Map<ComponentPair, JPanel> mCP2Panel;
	private Map<ComponentPair, JLabel> mCP2InfoLabel;

	
	public EpiTabEpithelialUpdateScheme(Epithelium e, TreePath path, ProjectChangedInTab projChanged, EpiTabChanged tabChanged,
			ProjectFeatures projectFeatures) {
		super(e, path, projChanged, tabChanged, projectFeatures);
	}
	
	

	public void initialize() {
		this.center.setLayout(new BorderLayout());
		this.mCP2Sliders = new HashMap<ComponentPair, JSlider>();
		this.mSliders2CP = new HashMap<JSlider, Set<ComponentPair>>();
		this.mCP2InfoLabel = new HashMap<ComponentPair, JLabel>();
		this.mCP2Panel = new HashMap<ComponentPair, JPanel>();
		
		List<LogicalModel> modelList = new ArrayList<LogicalModel>(this.epithelium.getEpitheliumGrid().getModelSet());
		this.updateSchemeInter = this.epithelium.getUpdateSchemeInter().clone();
		if (this.updateSchemeInter.getCPSigmas().size()>0){
			for (ComponentPair cp : this.updateSchemeInter.getCPSigmas().keySet()){
				this.addRegCP(cp);
			}
		}
		
		// Alpha asynchronism panel
		this.jpAlpha = new JPanel(new BorderLayout());
		this.center.add(this.jpAlpha, BorderLayout.NORTH);
		jpAlpha.setBorder(BorderFactory.createTitledBorder("Alpha - Asynchronism"));

		// alpha asynchronism information
		JEditorPane jAlphaPane = new JEditorPane();
		jpAlpha.add(jAlphaPane, BorderLayout.NORTH);
		jAlphaPane.setContentType("text/html");
		jAlphaPane.setEditable(false);
		jAlphaPane.setEnabled(true);
		jAlphaPane.setBackground(jpAlpha.getBackground());
		jAlphaPane.addHyperlinkListener(this);
		jAlphaPane.setText("\n\r"
				+ "Here we consider an updating scheme named &alpha;-asyncronism "
				+ "(see <a href=\"http://dx.doi.org/10.1007/978-3-642-40867-0_2\">"
				+ "doi:10.1007/978-3-642-40867-0_2</a>).<br/>"
				+ "It consists in updating each cell with probability &alpha;, the "
				+ "synchrony rate, leaving the state of the cells unchanged otherwise."
				+ "\n\r");

		jpAlpha.add(new JLabel(" "));

		// JSlider for alpha-asynchronism
		this.generateAlphaSlider();
		
		//Sigma asynchronism panel
		this.jpSigma = new JPanel(new BorderLayout());
		this.center.add(jpSigma, BorderLayout.CENTER);
		this.jpSigma.setBorder(BorderFactory.createTitledBorder("Sigma - Asynchronism"));
		
		//Model selection JPanel
		JPanel jpSigmaNorth = new JPanel(new BorderLayout());
		this.jpSigma.add(jpSigmaNorth, BorderLayout.NORTH);
		this.jpSigmaModelSelection = new JPanel(new BorderLayout());
		jpSigmaNorth.add(this.jpSigmaModelSelection, BorderLayout.WEST);
		this.jpSigmaModelSelection.add(new JLabel("Model: "), BorderLayout.WEST);
		JComboBox<String> jcbSBML = this.newModelCombobox(modelList);
		this.jpSigmaModelSelection.add(jcbSBML);
		
		//Sigma sliders JPanels
		this.selectedModel = this.projectFeatures.getModel((String) jcbSBML.getSelectedItem());
		updateSigmaSlidersPanel(this.selectedModel);
		this.isInitialized = true;
	}
	

	
	private void generateAlphaSlider() {
		JPanel jpAlphaInfo = new JPanel(new BorderLayout());
		jpAlphaInfo.add(new JLabel("Current alpha: "), BorderLayout.LINE_START);
		this.jAlphaLabelValue = new JLabel("--");
		jpAlphaInfo.add(this.jAlphaLabelValue, BorderLayout.CENTER);
		jpAlpha.add(jpAlphaInfo, BorderLayout.CENTER);
		
		JPanel jpAlphaSlider = new JPanel(new BorderLayout());
		jpAlphaSlider.add(new JLabel("Value: "), BorderLayout.LINE_START);
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
		this.jAlphaSlide.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				JSlider slide = (JSlider) e.getSource();
				updateAlpha(slide.getValue());
			}
		});
		this.jAlphaSlide.setValue((int) (this.updateSchemeInter.getAlpha() * SLIDER_MAX));
		updateAlpha(this.jAlphaSlide.getValue());
		jpAlphaSlider.add(this.jAlphaSlide, BorderLayout.CENTER);
		jpAlpha.add(jpAlphaSlider, BorderLayout.SOUTH);
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
		jAlphaLabelValue.setText(sTmp);
	}
	
	
	
	private void addRegCP(ComponentPair cp){
		if (!this.updateSchemeInter.containsCPSigma(cp)){
			this.updateSchemeInter.addCP(cp);
		}
		JLabel jSigmaLabelValue = new JLabel("--");
		this.mCP2InfoLabel.put(cp, jSigmaLabelValue);
		this.generateCPSigmaSlider(cp);
		this.generateCPSigmaPanel(cp);
	}
	
	
	
	private void removeRegCP(ComponentPair cp){
		this.updateSchemeInter.removeCPSigma(cp);
		this.mCP2InfoLabel.remove(cp);
		this.mCP2Panel.remove(cp);
		this.mCP2Sliders.remove(cp);
		Map<JSlider, Set<ComponentPair>> sliderCopy = new HashMap<JSlider, Set<ComponentPair>>(this.mSliders2CP);
		for (JSlider jSlider : sliderCopy.keySet()){
			if (sliderCopy.get(jSlider).contains(cp)){
				this.mSliders2CP.get(jSlider).remove(cp);
				if (this.mSliders2CP.get(jSlider).size() == 0){
					this.mSliders2CP.remove(jSlider);
				}
			}
		}
	}
	
	
	
	private Set<ComponentPair> generateRegulatorCP(){
		Set<ComponentPair> sRegulatorComponents = new HashSet<ComponentPair>();
		Map<ComponentPair, ComponentIntegrationFunctions> mIntegrationFunctions = 
				this.epithelium.getIntegrationFunctions().getAllIntegrationFunctions();
		IntegrationFunctionEvaluation ifEvaluator = new IntegrationFunctionEvaluation
				(this.epithelium.getEpitheliumGrid(), this.epithelium.getProjectFeatures());
		for (ComponentPair cp : mIntegrationFunctions.keySet()){
			LogicalModel m = cp.getModel();
			List<IntegrationExpression> lExpressions = this.epithelium
					.getIntegrationFunctionsForComponent(cp)
					.getComputedExpressions();
			for (IntegrationExpression ie : lExpressions){
				Set<String> sRegNodeIDs = ifEvaluator.traverseTreeRegulators(ie);
				for (String nodeID : sRegNodeIDs) {
					NodeInfo node = this.epithelium.getProjectFeatures().getNodeInfo(nodeID, m);
					if (node != null){
						ComponentPair regCompPair = new ComponentPair(m, node);
						sRegulatorComponents.add(regCompPair);
					}
				}
			}
		}
		return sRegulatorComponents;
	}

	
	
	private void generateCPSigmaSlider(ComponentPair cp){
		JSlider sigmaSlider = new JSlider(JSlider.HORIZONTAL, 
				this.SLIDER_MIN, this.SLIDER_MAX, this.SLIDER_MAX);
		sigmaSlider.setMajorTickSpacing(this.SLIDER_STEP);
		sigmaSlider.setMinorTickSpacing(1);
		sigmaSlider.setPaintTicks(true);
		sigmaSlider.setPaintLabels(true);
		
		//Store sigma slider
				this.mCP2Sliders.put(cp, sigmaSlider);
				if (this.mSliders2CP.containsKey(sigmaSlider)){
					this.mSliders2CP.get(sigmaSlider).add(cp);
				}
				else{
					Set<ComponentPair> tmpSet = new HashSet<ComponentPair>();
					tmpSet.add(cp);
					this.mSliders2CP.put(sigmaSlider, tmpSet);
				}

		Hashtable<Integer, JLabel> labelTable = new Hashtable<Integer, JLabel>();
		for (int i = this.SLIDER_MIN; i <= this.SLIDER_MAX; i += this.SLIDER_STEP) {
			labelTable.put(new Integer(i), new JLabel("" + ((float) i / this.SLIDER_MAX)));
		}
		sigmaSlider.setLabelTable(labelTable);
		sigmaSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				JSlider slide = (JSlider) e.getSource();	
				updateSigmaSlider(slide, slide.getValue());
			}
		});
		sigmaSlider.setValue((int) (this.updateSchemeInter.getCPSigma(cp) * SLIDER_MAX));
	}
	
	
	
	private void generateCPSigmaPanel(ComponentPair cp) {
		
		JPanel componentPanel = new JPanel(new BorderLayout());
		JLabel componentSigmaLabel = this.mCP2InfoLabel.get(cp);
		JSlider componentSlider = this.mCP2Sliders.get(cp);
		
		this.mCP2Panel.put(cp, componentPanel);
		
		JPanel jpComponentNorthInfo = new JPanel(new BorderLayout());
		componentPanel.add(jpComponentNorthInfo, BorderLayout.NORTH);
		jpComponentNorthInfo.add(new JLabel("Component: " + cp.getNodeInfo().getNodeID()),
				BorderLayout.CENTER);
		
		JPanel jpComponentCenterInfo = new JPanel(new BorderLayout());
		componentPanel.add(jpComponentCenterInfo, BorderLayout.WEST);
		jpComponentCenterInfo.add(new JLabel("Current sigma: "), BorderLayout.LINE_START);
		jpComponentCenterInfo.add(componentSigmaLabel, BorderLayout.CENTER);
		
		
		JPanel jpComponentSliderPanel = new JPanel(new BorderLayout());
		componentPanel.add(jpComponentSliderPanel, BorderLayout.SOUTH);
		jpComponentSliderPanel.add(new JLabel("Value: "), BorderLayout.LINE_START);
		jpComponentSliderPanel.add(componentSlider, BorderLayout.CENTER);
		
		}

	
	
	private void updateSigmaSlider(JSlider sigmaSlider, int sliderValue) {
		float sigma = (float) sliderValue / SLIDER_MAX;
		LogicalModel m = this.selectedModel;
		for (ComponentPair cp : this.mSliders2CP.get(sigmaSlider)){
			if (cp.getModel() == m) {
				this.updateSchemeInter.setCPSigma(cp, sigma);
				this.mCP2InfoLabel.get(cp).setText(""+ sigma);
			}
		}
	}
	
	
	
	private void updateCPSigma(ComponentPair cp, int value) {
		this.updateSchemeInter.setCPSigma(cp, value);
		this.mCP2InfoLabel.get(cp).setText(""+ value);
	}
	
	
	
	private void updateAllCPSigma(){
		Set<ComponentPair> sNewRegComponents = this.generateRegulatorCP();
		
		if (sNewRegComponents.size() == 0) {
			this.mCP2Sliders.clear();
			this.mCP2Panel.clear();
			this.mCP2InfoLabel.clear();
			this.mSliders2CP.clear();
			this.updateSchemeInter.clearAllCPSigma();
		}
		else{
			Set<ComponentPair> sOldRegComponents = new HashSet<ComponentPair>
							(this.updateSchemeInter.getCPSigmas().keySet());
			Set <ComponentPair> sCommonRegComponents = new HashSet<ComponentPair>(sOldRegComponents);
			sCommonRegComponents.retainAll(sNewRegComponents);
			sCommonRegComponents.addAll(sNewRegComponents);
			for (ComponentPair oldCP : sOldRegComponents) {
				if (!sCommonRegComponents.contains(oldCP)){
					this.removeRegCP(oldCP);
				}
			}
			for (ComponentPair newCP : sCommonRegComponents) {
				if (this.updateSchemeInter.containsCPSigma(newCP))
					continue;
				else{
					this.addRegCP(newCP);
				}
			}
		}
	}
	
	
	
	private void updateSigmaSlidersPanel(LogicalModel m) {
		this.updateAllCPSigma();
		this.selectedModel = m;
		Set<ComponentPair> sModelRegComponents = new HashSet<ComponentPair>();
		for (ComponentPair cp : this.updateSchemeInter.getCPSigmas().keySet()){
			if (cp.getModel() == m) {
				sModelRegComponents.add(cp);
			}
		}
		if (sModelRegComponents.size() == 0){
			JEditorPane jSigmaPane = new JEditorPane();
			this.jpSigmaSliderPanel.add(jSigmaPane, BorderLayout.CENTER);
			jSigmaPane.setContentType("text/html");
			jSigmaPane.setEditable(false);
			jSigmaPane.setEnabled(true);
			jSigmaPane.setBackground(jpSigma.getBackground());
			jSigmaPane.addHyperlinkListener(this);
			jSigmaPane.setText("\n\r"
					+ "There are no Integration Functions defined for this Model "
					+ "to allow for the setting of &sigma asynchronism."
					+ "\n\r");
		}
		else {
			this.jpSigmaSliderPanel = 
					new JPanel(new GridLayout(sModelRegComponents.size(), 1));
			this.jspSigmaSliderScroller = new JScrollPane(this.jpSigmaSliderPanel);
			this.jspSigmaSliderScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
			this.jpSigma.add(this.jspSigmaSliderScroller, BorderLayout.CENTER);
			for (ComponentPair cp : sModelRegComponents){
				JPanel componentPanel = this.mCP2Panel.get(cp);
				componentPanel.setBorder(BorderFactory.createEtchedBorder());
				this.jpSigmaSliderPanel.add(componentPanel);
			}
		}
	}

	
	
	private JComboBox<String> newModelCombobox(List<LogicalModel> modelList) {
		// Model selection list
		String[] saSBML = new String[modelList.size()];
		for (int i = 0; i < modelList.size(); i++) {
			saSBML[i] = this.projectFeatures.getModelName(modelList.get(i));
		}
		JComboBox<String> jcb = new JComboBox<String>(saSBML);
		jcb.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				@SuppressWarnings("unchecked")
				JComboBox<String> jcb = (JComboBox<String>) e.getSource();
				LogicalModel m = projectFeatures.getModel((String) jcb.getSelectedItem());
				updateSigmaSlidersPanel(m);
				// Re-Paint
				getParent().repaint();
			}
		});
		return jcb;
	}

	
	
	@Override
	protected void buttonReset() {
		for (ComponentPair cp : this.mCP2Sliders.keySet()) {
			float sigmaReset = (int) this.epithelium.getUpdateSchemeInter().getCPSigma(cp);
			this.mCP2Sliders.get(cp).setValue((int) (sigmaReset * this.SLIDER_MAX));
			for (JSlider jSlide : this.mSliders2CP.keySet()){
				if (this.mSliders2CP.get(jSlide) == cp){
					jSlide.setValue((int) (sigmaReset * this.SLIDER_MAX));
				}
			}
			this.updateCPSigma(cp, (int) (sigmaReset * this.SLIDER_MAX));
		}
		this.jAlphaSlide.setValue((int) (this.epithelium.getUpdateSchemeInter().getAlpha() * SLIDER_MAX));
		this.updateAlpha(this.jAlphaSlide.getValue());
		// Repaint
		this.getParent().repaint();
	}

	
	
	@Override
	protected void buttonAccept() {
		this.epithelium.getUpdateSchemeInter().setAlpha(this.updateSchemeInter.getAlpha());
		for (ComponentPair cp : this.mCP2Sliders.keySet()) {
			this.epithelium.getUpdateSchemeInter().
			setCPSigma(cp, this.updateSchemeInter.getCPSigma(cp));
		}
	}

	
	
	@Override
	protected boolean isChanged() {
		if (this.epithelium.getUpdateSchemeInter().getAlpha() != this.updateSchemeInter.getAlpha())
			return true;
		if (this.mCP2Sliders != null){
			for (ComponentPair cp : this.mCP2Sliders.keySet()){
				if (!this.epithelium.getUpdateSchemeInter().containsCPSigma(cp) & this.updateSchemeInter.containsCPSigma(cp))
					return true;
				if (this.updateSchemeInter.getCPSigma(cp) != this.epithelium.getUpdateSchemeInter().getCPSigma(cp))
					return true;
		}
	}
		return false;
	}

	
	
	@Override
	public void notifyChange() {
		if (!this.isInitialized)
			return;
		List<LogicalModel> modelList = new ArrayList<LogicalModel>(this.epithelium.getEpitheliumGrid().getModelSet());
		if (!modelList.contains(this.selectedModel)){
			this.selectedModel = modelList.get(0);
		}
		this.jpSigmaModelSelection.removeAll();
		this.jpSigmaModelSelection.add(new JLabel("Model: "), BorderLayout.WEST);
		this.jpSigmaModelSelection.add(this.newModelCombobox(modelList));
		this.updateAllCPSigma();
		this.updateSigmaSlidersPanel(this.selectedModel);
		this.getParent().repaint();
	}
	
	@Override
	public void hyperlinkUpdate(HyperlinkEvent event) {
		if (event.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
			Web.openURI(event.getDescription());
		}
	}
}
