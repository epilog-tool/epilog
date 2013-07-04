package pt.igc.nmd.epilog;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

import org.colomoto.logicalmodel.LogicalModel;
import org.colomoto.logicalmodel.NodeInfo;
import org.colomoto.logicalmodel.perturbation.AbstractPerturbation;

import pt.igc.nmd.epilog.integrationgrammar.IntegrationFunctionSpecification;
import pt.igc.nmd.epilog.integrationgrammar.IntegrationFunctionSpecification.IntegrationExpression;

public class SphericalEpithelium implements Epithelium {

	public Color colors[] = { Color.orange, Color.green, Color.blue,
			Color.pink, Color.yellow, Color.magenta, Color.cyan, Color.red,
			Color.LIGHT_GRAY, Color.black };

	private LogicalModel unitaryModel;
	private LogicalModel composedModel;
	private String SBMLFilename;
	private String SBMLFilePath;
	private String SBMLLoadPath;

	private boolean[] integrationcomponent = null;

	private Color[] nodeColor = null;
	private boolean[] displayComponents = null;
	private boolean[] definitionsDisplayComponents = null;
	private String[][] integrationFunctionStrings = null;
	private byte[] initialState = null;
	private byte grid[][]; // {instance , {nodeindex , value}}

	private AbstractPerturbation[] perturbations = null;
	private AbstractPerturbation activePerturbation;

	public List loadedPerturbations;
	public List loadedMutations;

	public Hashtable<String, Color> perturbationColor;

	private String selectedPriority;
	private String selectedPerturbation;
	private String selectedInputSet;
	private String selectedInitialSet;

	private Hashtable<String, List<List<NodeInfo>>> prioritiesSet;
	private Hashtable<String, AbstractPerturbation[]> perturbationsSet;
	private Hashtable<String, Grid> initialStateSet;
	private Hashtable<String, Grid> inputsSet;
	private Hashtable<String, Hashtable<NodeInfo, List<String>>> integrationInputsSet;

	private Topology topology = null;

	private boolean newEpithelium;

	public SphericalEpithelium(Topology topology) {

		this.topology = topology;
		prioritiesSet = new Hashtable<String, List<List<NodeInfo>>>();
		perturbationsSet = new Hashtable<String, AbstractPerturbation[]>();
		initialStateSet = new Hashtable<String, Grid>();
		inputsSet = new Hashtable<String, Grid>();
		perturbations = new AbstractPerturbation[topology.getNumberInstances()];
		integrationInputsSet = new Hashtable<String, Hashtable<NodeInfo, List<String>>>();
		loadedPerturbations = new ArrayList<AbstractPerturbation>();
		loadedMutations = new ArrayList<AbstractPerturbation>();

		perturbationColor = new Hashtable<String, Color>();
	}

	// SBML INFORMATION

	public String getSBMLFilename() {
		return SBMLFilename;
	}

	public String getSBMLFilePath() {
		// System.out.println(SBMLFilePath);
		return SBMLFilePath;
	}

	public void setSBMLPath(String file) {
		SBMLFilePath = file;

	}

	public void setSBMLFilename(String string) {
		SBMLFilename = string;
	}

	public String getSBMLLoadPath() {
		return SBMLLoadPath;
	}

	public void setSBMLLoadPath(String s) {
		SBMLLoadPath = s;
	}

	// COLORS

	// TODO: CHange this to random colors
	private void initializeColors() {

		this.nodeColor = new Color[getUnitaryModel().getNodeOrder().size()];
		for (int i = 0; i < this.nodeColor.length; i++)
			this.nodeColor[i] = Color.white;
	}

	public void setColor(NodeInfo node, Color color) {
		this.nodeColor[getUnitaryModel().getNodeOrder().indexOf(node)] = color;
	}

	public Color getColor(NodeInfo node) {
		return this.nodeColor[getUnitaryModel().getNodeOrder().indexOf(node)];
	}

	// DISPLAY COMPONENTS

	private void initializeDisplayComponents() {
		this.displayComponents = new boolean[getUnitaryModel().getNodeOrder()
				.size()];
		for (int i = 0; i < this.displayComponents.length; i++)
			this.displayComponents[i] = false;

	}

	private void initializeDefinitionsDisplayComponents() {
		this.definitionsDisplayComponents = new boolean[getUnitaryModel()
				.getNodeOrder().size()];
		this.integrationcomponent = new boolean[getUnitaryModel()
				.getNodeOrder().size()];
		for (int i = 0; i < this.definitionsDisplayComponents.length; i++) {
			this.definitionsDisplayComponents[i] = false;
			this.integrationcomponent[i] = false;
		}
	}

	public boolean isDisplayComponentOn(NodeInfo node) {
		return this.displayComponents[getUnitaryModel().getNodeOrder().indexOf(
				node)];
	}

	public void setActiveComponent(NodeInfo node, boolean bool) {
		this.displayComponents[getUnitaryModel().getNodeOrder().indexOf(node)] = bool;
	}

	public void setActiveComponent(NodeInfo node) {
		setActiveComponent(node, true);
	}

	public void setDefinitionsComponentDisplay(int i, boolean b) {
		this.definitionsDisplayComponents[i] = b;
	}

	public boolean isDefinitionComponentDisplayOn(int i) {
		return this.definitionsDisplayComponents[i];
	}

	// INTEGRATION COMPONENTS

	public boolean isIntegrationComponent(NodeInfo node) {
		return isIntegrationComponent(getUnitaryModel().getNodeOrder().indexOf(node));
	}

	public String getIntegrationFunction(NodeInfo node, byte value) {
	
		
		if (integrationFunctionStrings[getUnitaryModel().getNodeOrder()
		                              				.indexOf(node)] !=null)
		return this.integrationFunctionStrings[getUnitaryModel().getNodeOrder()
				.indexOf(node)][value - 1];
		else
			return "";
	}

	public IntegrationExpression string2Expression(
			String integrationfunctionString) {

		IntegrationFunctionSpecification spec = new IntegrationFunctionSpecification();
		IntegrationExpression expression = null;

		try {
			expression = spec.parse(integrationfunctionString);
		} catch (org.antlr.runtime.RecognitionException e) {

			e.printStackTrace();
		}

		return expression;

	}

	public IntegrationExpression[] getIntegrationExpressionsForInput(
			NodeInfo node) {
		IntegrationExpression expressions[] = new IntegrationExpression[node
				.getMax()];

		for (byte value = 1; value <= node.getMax(); value++) {
			expressions[value - 1] = string2Expression(getIntegrationFunction(
					node, value));
		}

		return expressions;
	}

	public void setIntegrationFunctions(NodeInfo node, byte value,
			String expression) {
		if (this.integrationFunctionStrings[getUnitaryModel().getNodeOrder()
				.indexOf(node)] == null)
			this.integrationFunctionStrings[getUnitaryModel().getNodeOrder()
					.indexOf(node)] = new String[node.getMax()];
		this.integrationFunctionStrings[getUnitaryModel().getNodeOrder()
				.indexOf(node)][value - 1] = expression;
	}

	public void resetIntegrationNode(NodeInfo node) {
		this.integrationFunctionStrings[getUnitaryModel().getNodeOrder()
				.indexOf(node)] = null;
	}

	private void initializeIntegrationFunctions() {
		this.integrationFunctionStrings = new String[getUnitaryModel()
				.getNodeOrder().size()][];
	}

	public void setIntegrationComponent(int i, boolean bool) {
		this.integrationcomponent[i] = bool;
	}

	public boolean isIntegrationComponent(int i) {
		return this.integrationcomponent[i];
	}

	// UNITARY AND COMPOSED MODEL

	@Override
	public void setUnitaryModel(LogicalModel model) {
		this.unitaryModel = model;
		if (model != null) {
			initializeColors();
			initializeDisplayComponents();
			initializeDefinitionsDisplayComponents();
			initializeIntegrationFunctions();
			initializeInitialState();
			initializeGrid();
			initializePerturbationsGrid();
			noPerturbations();
		}

	}

	@Override
	public LogicalModel getUnitaryModel() {
		return this.unitaryModel;
	}

	@Override
	public LogicalModel getComposedModel() {
		return this.composedModel;
	}

	public void setComposedModel(LogicalModel composedModel) {
		this.composedModel = composedModel;
	}

	public SphericalEpithelium getEpithelium() {
		return this;
	}

	public void reset() {
		setUnitaryModel(null);
	}

	public boolean isNewEpithelium() {
		return newEpithelium;
	}

	public void setNewEpithelium(boolean b) {
		newEpithelium = b;
	}

	// GRIDS

	public void setInitialState(int instance) {
		List<NodeInfo> listNodes = getUnitaryModel().getNodeOrder();

		for (NodeInfo node : listNodes) {

			if (isDefinitionComponentDisplayOn(listNodes.indexOf(node))) {
				setGrid(instance, node, getInitialState(node));
			}
		}
	}

	public void initializeGrid() {

		this.grid = new byte[topology.getNumberInstances()][];
		for (int i = 0; i < topology.getNumberInstances(); i++) {
			this.grid[i] = new byte[getUnitaryModel().getNodeOrder().size()];
			for (NodeInfo node : getUnitaryModel().getNodeOrder())
				this.grid[i][getUnitaryModel().getNodeOrder().indexOf(node)] = 0;
		}
	}

	public byte getGridValue(Integer instance, NodeInfo node) {
		return this.grid[instance][getUnitaryModel().getNodeOrder().indexOf(
				node)];
	}

	private void initializeInitialState() {
		this.initialState = new byte[getUnitaryModel().getNodeOrder().size()];
	}

	public void setInitialState(NodeInfo node, byte value) {
		this.initialState[getUnitaryModel().getNodeOrder().indexOf(node)] = value;
	}

	public byte getInitialState(NodeInfo node) {
		return this.initialState[getUnitaryModel().getNodeOrder().indexOf(node)];
	}

	public void setGrid(Integer instance, NodeInfo node, byte value) {
		if (this.grid.length != topology.getNumberInstances()) {

			initializeGrid();
		}
		this.grid[instance][getUnitaryModel().getNodeOrder().indexOf(node)] = value;

	}

	/*
	 * Perturbations
	 */

	public Color getPerturbationColor(String perturbation) {
		return perturbationColor.get(perturbation);
	}

	public void setPerturbationColor(String perturbation, Color color) {
		perturbationColor.put(perturbation, color);
	}

	public void noPerturbations() {
		perturbationsSet.put("none",
				new AbstractPerturbation[topology.getNumberInstances()]);
	}

	public AbstractPerturbation getActivePerturbation() {
		return activePerturbation;
	}

	public void setActivePerturbation(AbstractPerturbation perturbation) {
		activePerturbation = perturbation;
	}

	public void initializePerturbationsGrid() {
		perturbations = new AbstractPerturbation[topology.getNumberInstances()];
	}

	public void setPerturbedInstance(int instance) {
		AbstractPerturbation perturbation = getActivePerturbation();
		perturbations[instance] = perturbation;

	}

	public boolean isCellPerturbed(int instance) {
		if (getInstancePerturbation(instance) == null)
			return false;
		else
			return true;
	}

	public boolean isCellPerturbedDraw(int instance) {
		if (getInstancePerturbationDraw(instance) == null)
			return false;
		else
			return true;
	}

	public AbstractPerturbation getInstancePerturbation(int instance) {
		if (selectedPerturbation != null && getPerturbationsSet().get(selectedPerturbation) != null)
			return getPerturbationsSet().get(selectedPerturbation)[instance];
		else
			return null;

	}

	public AbstractPerturbation getInstancePerturbationDraw(int instance) {

		if (perturbations.length != topology.getNumberInstances())
			initializePerturbationsGrid();
		return perturbations[instance];
	}

	public void setPerturbationSet(String name) {
		AbstractPerturbation[] perturbations_aux = new AbstractPerturbation[topology
				.getNumberInstances()];
		for (int instance = 0; instance < topology.getNumberInstances(); instance++) {
			perturbations_aux[instance] = perturbations[instance];
		}

		perturbationsSet.put(name, perturbations_aux);
	}

	public void setPerturbationSet(String name,
			AbstractPerturbation[] perturbations_1) {
		perturbationsSet.put(name, perturbations_1);
	}

	public Hashtable<String, AbstractPerturbation[]> getPerturbationsSet() {
		return perturbationsSet;
	}

	public String getSelectedPerturbation() {
		return selectedPerturbation;
	}

	public void setLoadedPerturbations(List a) {
		loadedPerturbations = a;
	}

	public void setLoadedMutations(List a) {
		loadedMutations = a;
	}

	// Sets the selected perturbation from the saved Set
	public void setSelectedPerturbation(String string) {
		selectedPerturbation = string;
	}

	/*
	 * Initial State
	 */
	public Hashtable<String, Grid> getInitialStateSet() {
		return initialStateSet;
	}

	public void setInitialStateSet(String name) {

		List<NodeInfo> initialStateComponents = new ArrayList();
		for (NodeInfo node : getUnitaryModel().getNodeOrder()) {
			if (!isIntegrationComponent(node)) {
				initialStateComponents.add(node);
			}
		}

		Grid initialStateGrid = new Grid(topology.getNumberInstances(),
				initialStateComponents);

		for (int instance = 0; instance < topology.getNumberInstances(); instance++) {
			for (NodeInfo node : initialStateComponents) {
				byte value = getGridValue(instance, node);
				initialStateGrid.setGrid(instance, node, value);
			}
		}
		initialStateSet.put(name, initialStateGrid);
	}

	public void setSelectedInitialSet(String string) {
		selectedInitialSet = string;
		Grid initial_aux = initialStateSet.get(string);
		if (initial_aux != null)
			combineGrids(initial_aux);
	}

	/*
	 * Inputs
	 */
//	public Hashtable<String, Grid> getInputsSet() {
//		return inputsSet;
//	}

	public Hashtable<String, Hashtable<NodeInfo, List<String>>> getInputsIntegrationSet() {
		return integrationInputsSet;
	}

	public void setInputsSet(String name) {

//		List<NodeInfo> inputs = new ArrayList();
//		for (NodeInfo node : getUnitaryModel().getNodeOrder()) {
//			if (node.isInput() & !isIntegrationComponent(node)) {
//				inputs.add(node);
//			}
//		}
//
//		Grid inputsGrid = new Grid(topology.getNumberInstances(), inputs);
//
//		for (int instance = 0; instance < topology.getNumberInstances(); instance++) {
//			for (NodeInfo node : inputs) {
//				byte value = getGridValue(instance, node);
//				inputsGrid.setGrid(instance, node, value);
//			}
//		}
//		inputsSet.put(name, inputsGrid);
		setInputsIntegrationSet(name);
	}

	public void setInputsIntegrationSet(String name) {

		
		Hashtable<NodeInfo, List<String>> test = new Hashtable<NodeInfo, List<String>>();

		for (NodeInfo node : getUnitaryModel().getNodeOrder()) {
			if (isIntegrationComponent(node)) {
				
				List<String> aux = new ArrayList<String>();
				for (byte value = 1; value < node.getMax() + 1; value++) {
					aux.add(getIntegrationFunction(node, value));
				}
				test.put(node, aux);
			}
		}
		integrationInputsSet.put(name, test);
	}

	public void combineGrids(Grid grid) {
		for (int instance = 0; instance < topology.getNumberInstances(); instance++) {
			for (NodeInfo node : grid.getListNodes()) {
				this.grid[instance][getUnitaryModel().getNodeOrder().indexOf(
						node)] = grid.getValue(instance, node);
			}
		}
	}

	public void setSelectedInputSet(String string) {
		
		selectedInputSet = string;
//		Grid input_aux = inputsSet.get(string);

//		if (input_aux != null){
//			combineGrids(input_aux);
//			for (NodeInfo node: input_aux.getListNodes())
//				setIntegrationComponent(getUnitaryModel().getNodeOrder()
//						.indexOf(node), false);
//		}
		Hashtable<NodeInfo, List<String>> aux = integrationInputsSet
				.get(string);
		

		if (aux != null){
			Set<NodeInfo> aux_nodes = aux.keySet();
			for (NodeInfo node : getUnitaryModel().getNodeOrder()) {
				if (aux_nodes.contains(node)) {
					
					setIntegrationComponent(getUnitaryModel().getNodeOrder()
							.indexOf(node), true);
					for (int j = 0; j < aux.get(node).size(); j++) {
						String expression = aux.get(node).get(j);
						byte value = (byte) (j + 1);
						setIntegrationFunctions(node, value, expression);
					}
				} else if (node.isInput()){
		
					setIntegrationComponent(getUnitaryModel().getNodeOrder()
							.indexOf(node), false);
					
				}
			}
		}
	}

	/*
	 * Priorities
	 */

	public void setPrioritiesSet(String name,
			List<List<NodeInfo>> prioritiesClass) {
		prioritiesSet.put(name, prioritiesClass);
	}

	public Hashtable<String, List<List<NodeInfo>>> getPrioritiesSet() {
		return prioritiesSet;
	}

	public String getSelectedPriority() {
		return selectedPriority;
	}

	public void setSelectedPriority(String string) {
		selectedPriority = string;
	}
}
