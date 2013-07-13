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
	private String selectedPerturbationSet;
	private String selectedInputSet;
	private String selectedInitialSet;

	private Hashtable<String, List<List<NodeInfo>>> prioritiesSet;
	private Hashtable<String, AbstractPerturbation[]> perturbationsSet;
	private Hashtable<String, Grid> initialStateSet;
	private Hashtable<String, Grid> inputsSet;
	public Hashtable<String, Hashtable<NodeInfo, List<String>>> integrationInputsSet;

	private Topology topology = null;

	private boolean newEpithelium;

	/**
	 * Generates the epithelium and all related definitions.
	 * 
	 * @param topology
	 */
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

	/**
	 * Returns the SBML original filename.
	 * 
	 * @return
	 */
	public String getSBMLFilename() {
		return SBMLFilename;
	}

	/**
	 * Returns the SBML original path.
	 * 
	 * @return
	 */
	public String getSBMLFilePath() {
		// System.out.println(SBMLFilePath);
		return SBMLFilePath;
	}

	/**
	 * Sets the SBML original path.
	 * 
	 * @param file
	 */
	public void setSBMLPath(String file) {
		SBMLFilePath = file;

	}

	/**
	 * Sets the SBML original filename.
	 * 
	 * @param string
	 */
	public void setSBMLFilename(String string) {
		SBMLFilename = string;
	}

	/**
	 * Gets the SBML load path.
	 * 
	 * @return
	 */
	public String getSBMLLoadPath() {
		return SBMLLoadPath;
	}

	/**
	 * Sets the SBML load path.
	 * 
	 * @param
	 */
	public void setSBMLLoadPath(String s) {
		SBMLLoadPath = s;
	}

	// COLORS

	// TODO: CHange this to random colors
	/**
	 * Initialize colors associated with the components.
	 */
	private void initializeColors() {

		this.nodeColor = new Color[getUnitaryModel().getNodeOrder().size()];
		for (int i = 0; i < this.nodeColor.length; i++)
			this.nodeColor[i] = Color.white;
	}

	/**
	 * Sets the color associated with a component.
	 * 
	 * @param node
	 *            component
	 * @param color
	 *            color
	 */
	public void setColor(NodeInfo node, Color color) {
		this.nodeColor[getUnitaryModel().getNodeOrder().indexOf(node)] = color;
	}

	/**
	 * Returns the color associated with a node
	 * 
	 * @return color
	 */
	public Color getColor(NodeInfo node) {
		return this.nodeColor[getUnitaryModel().getNodeOrder().indexOf(node)];
	}

	// DISPLAY COMPONENTS

	/**
	 * Sets all components as not displaying in the hexagons grid.
	 */
	private void initializeDisplayComponents() {
		this.displayComponents = new boolean[getUnitaryModel().getNodeOrder()
				.size()];
		for (int i = 0; i < this.displayComponents.length; i++)
			this.displayComponents[i] = false;

	}

	/**
	 * Initializes all input components as environment inputs.
	 */
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

	/**
	 * Returns true if component is set to display, false otherwise.
	 * 
	 * @param node
	 *            node
	 * @return boolean value
	 */
	public boolean isDisplayComponentOn(NodeInfo node) {
		return this.displayComponents[getUnitaryModel().getNodeOrder().indexOf(
				node)];
	}

	/**
	 * Set node as set to display if bool true, inactive otherwise.
	 * 
	 * @param node
	 * @param bool
	 */
	public void setActiveComponent(NodeInfo node, boolean bool) {
		this.displayComponents[getUnitaryModel().getNodeOrder().indexOf(node)] = bool;
	}

	/**
	 * Set node as set to display.
	 * 
	 * @param node
	 * @see setActiveComponent(NodeInfo node, boolean bool)
	 */
	public void setActiveComponent(NodeInfo node) {
		setActiveComponent(node, true);
	}

	/**
	 * Set node associate with index i as active if b true, inactive otherwise.
	 * 
	 * @param i
	 *            index related to a node
	 * @param b
	 *            true if set to display, false otherwise
	 */
	public void setDefinitionsComponentDisplay(int i, boolean b) {
		this.definitionsDisplayComponents[i] = b;
	}

	/**
	 * Returns true if component is set to display, false otherwise.
	 * 
	 * @param i  index related to a node
	 * @return boolean value
	 */
	public boolean isDefinitionComponentDisplayOn(int i) {
		return this.definitionsDisplayComponents[i];
	}

	// INTEGRATION COMPONENTS

/**
 * Returns true if node is integration component, false otherwise
 * @param node
 */
	public boolean isIntegrationComponent(NodeInfo node) {
		return isIntegrationComponent(getUnitaryModel().getNodeOrder().indexOf(
				node));
	}

	/**
	 * Returns the integration function in the form of a string.
	 * @param node node related to the integration function
	 * @param value value related to the integration function of the node
	 * @return integration functions
	 */
	public String getIntegrationFunction(NodeInfo node, byte value) {

		if (integrationFunctionStrings[getUnitaryModel().getNodeOrder()
				.indexOf(node)] != null)
			return this.integrationFunctionStrings[getUnitaryModel()
					.getNodeOrder().indexOf(node)][value - 1];
		else
			return "";
	}

	/**
	 * Translates an integration function from string to expression.
	 * @param integrationfunctionString
	 * @return expression
	 */
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

	/**
	 * Returns the integration expressions related to a node.
	 * @param node
	 * @return expressions
	 */
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

	/**
	 * Set a integration function related to a value of a component.
	 * @param node
	 * @param value
	 * @param expression
	 */
	public void setIntegrationFunctions(NodeInfo node, byte value,
			String expression) {
		if (this.integrationFunctionStrings[getUnitaryModel().getNodeOrder()
				.indexOf(node)] == null)
			this.integrationFunctionStrings[getUnitaryModel().getNodeOrder()
					.indexOf(node)] = new String[node.getMax()];
		this.integrationFunctionStrings[getUnitaryModel().getNodeOrder()
				.indexOf(node)][value - 1] = expression;
	}

	/**
	 * Reset all integration functions of a component.
	 * @param node
	 */
	public void resetIntegrationNode(NodeInfo node) {
		this.integrationFunctionStrings[getUnitaryModel().getNodeOrder()
				.indexOf(node)] = null;
	}

	/**
	 * Initialize integration functions.
	 */
	private void initializeIntegrationFunctions() {
		this.integrationFunctionStrings = new String[getUnitaryModel()
				.getNodeOrder().size()][];
	}

	/**
	 * Set component, related to the index of a node as integration input or environment.
	 * @param i
	 * @param bool
	 */
	public void setIntegrationComponent(int i, boolean bool) {
		this.integrationcomponent[i] = bool;
	}
	/**
	 * Returns true if component, related to the index of a node is an integration input.
	 * @param i
	 * @return
	 */
	

	public boolean isIntegrationComponent(int i) {
		return this.integrationcomponent[i];
	}

	// UNITARY AND COMPOSED MODEL

	/**
	 * Sets an unitary regulatory model.
	 */
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
			noInitialState();
			noIntegrationFunctions();
		}

	}

	/**
	 * Returns the unitary model.
	 */
	@Override
	public LogicalModel getUnitaryModel() {
		return this.unitaryModel;
	}

	/**
	 * Returns the composed model.
	 */
	@Override
	public LogicalModel getComposedModel() {
		return this.composedModel;
	}

	/**
	 * Sets the composed model.
	 * @param composedModel
	 */
	public void setComposedModel(LogicalModel composedModel) {
		this.composedModel = composedModel;
	}

	/**
	 * Returns the epithelium.
	 * @return
	 */
	public SphericalEpithelium getEpithelium() {
		return this;
	}

	/**
	 * Resets the unitary model.
	 */
	public void reset() {
		setUnitaryModel(null);
	}

	/**
	 * Returns true if the user is creting a new epithelium.
	 * @return
	 */
	public boolean isNewEpithelium() {
		return newEpithelium;
	}

	/**
	 * Load a new epithelium.
	 * @param b
	 */
	public void setNewEpithelium(boolean b) {
		newEpithelium = b;
	}

	// GRIDS

	/**
	 * Sets an initial state for an instance.
	 * @param instance
	 */
	public void setInitialState(int instance) {
		List<NodeInfo> listNodes = getUnitaryModel().getNodeOrder();

		for (NodeInfo node : listNodes) {

			if (isDefinitionComponentDisplayOn(listNodes.indexOf(node))) {
				setGrid(instance, node, getInitialState(node));
			}
		}
	}

	/**
	 * Initializes grid with all components at zero.
	 */
	public void initializeGrid() {

		this.grid = new byte[topology.getNumberInstances()][];
		for (int i = 0; i < topology.getNumberInstances(); i++) {
			this.grid[i] = new byte[getUnitaryModel().getNodeOrder().size()];
			for (NodeInfo node : getUnitaryModel().getNodeOrder())
				this.grid[i][getUnitaryModel().getNodeOrder().indexOf(node)] = 0;
		}
	}

	/**
	 * Returns the value of a node at an instance.
	 */
	public byte getGridValue(Integer instance, NodeInfo node) {
		return this.grid[instance][getUnitaryModel().getNodeOrder().indexOf(
				node)];
	}

	/**
	 * Initialize initial state.
	 */
	private void initializeInitialState() {
		this.initialState = new byte[getUnitaryModel().getNodeOrder().size()];
	}

	/**
	 * Set initial state of a node with a value.
	 * @param node
	 * @param value
	 */
	public void setInitialState(NodeInfo node, byte value) {
		this.initialState[getUnitaryModel().getNodeOrder().indexOf(node)] = value;
	}

	/**
	 * Returns the initial State of a node.
	 * @param node
	 * @return
	 */
	public byte getInitialState(NodeInfo node) {
		return this.initialState[getUnitaryModel().getNodeOrder().indexOf(node)];
	}

	/**
	 * Sets the value of a node at instance in the grid.
	 * @param instance
	 * @param node
	 * @param value
	 */
	public void setGrid(Integer instance, NodeInfo node, byte value) {
		if (this.grid.length != topology.getNumberInstances()) {

			initializeGrid();
		}
		this.grid[instance][getUnitaryModel().getNodeOrder().indexOf(node)] = value;

	}

	/*
	 * Perturbations
	 */

	/**
	 * Returns the color associated with a perturbation.
	 * @param perturbation
	 * @return
	 */
	public Color getPerturbationColor(String perturbation) {
		return perturbationColor.get(perturbation);
	}

	/**
	 * Sets the color associated with a perturbation.
	 * @param perturbation
	 * @param color
	 */
	public void setPerturbationColor(String perturbation, Color color) {
		perturbationColor.put(perturbation, color);
	}

	/**
	 * Sets all instances without perturbations.
	 */
	public void noPerturbations() {
		perturbationsSet.put("none",
				new AbstractPerturbation[topology.getNumberInstances()]);
	}

	/**
	 * Returns the active perturbation.
	 * @return
	 */
	public AbstractPerturbation getActivePerturbation() {
		return activePerturbation;
	}

	/**
	 * Sets the active perturbation.
	 * @param perturbation
	 */
	public void setActivePerturbation(AbstractPerturbation perturbation) {
		activePerturbation = perturbation;
	}

	/**
	 * Initializes the perturbations array.
	 */
	public void initializePerturbationsGrid() {
		perturbations = new AbstractPerturbation[topology.getNumberInstances()];
	}

	/**
	 * Sets an instance as perturbed with the active perturbation.
	 * @param instance
	 */
	public void setPerturbedInstance(int instance) {
		AbstractPerturbation perturbation = getActivePerturbation();
		perturbations[instance] = perturbation;
	}

	/**
	 * Returns true if instance is perturbed, false otherwise.
	 * @param instance
	 * @return
	 */
	public boolean isCellPerturbed(int instance) {
		if (getInstancePerturbation(instance) == null)
			return false;
		else
			return true;
	}

	/**
	 * Returns true if instance is perturbed, false otherwise. Applicable only when drawing.
	 * @param instance
	 * @return
	 */
	public boolean isCellPerturbedDraw(int instance) {
		if (getInstancePerturbationDraw(instance) == null)
			return false;
		else
			return true;
	}
/**
 * Return perturbation associated with an instance
 * @param instance
 * @return perturbation
 */
	public AbstractPerturbation getInstancePerturbation(int instance) {

		// if (selectedPerturbationSet != null &&
		// getPerturbationsSet().get(selectedPerturbationSet) == null){
		// System.out.println("getPerturbationsSet().get(selectedPerturbation) "
		// + instance);
		// for (String name: getPerturbationsSet().keySet())
		// System.out.println(name);
		// }

		if (selectedPerturbationSet != null
				&& getPerturbationsSet().get(selectedPerturbationSet) != null)
			return getPerturbationsSet().get(selectedPerturbationSet)[instance];
		else {
			return null;
		}
	}

	/**
	 * 
	 * @param instance
	 * @return
	 */
	public AbstractPerturbation getInstancePerturbationDraw(int instance) {

		if (perturbations.length != topology.getNumberInstances())
			initializePerturbationsGrid();
		return perturbations[instance];
	}

	/**
	 * Add perturbation set.
	 * @param name
	 */
	public void setPerturbationSet(String name) {

		AbstractPerturbation[] perturbations_aux = new AbstractPerturbation[topology
				.getNumberInstances()];
		for (int instance = 0; instance < topology.getNumberInstances(); instance++) {

			perturbations_aux[instance] = perturbations[instance];
		}
		perturbationsSet.put(name, perturbations_aux);

	}

	/**
	 * Add perturbation set.
	 * @param name
	 * @param perturbations_1
	 */
	public void setPerturbationSet(String name,
			AbstractPerturbation[] perturbations_1) {
		perturbationsSet.put(name, perturbations_1);
	}

	/**
	 * Return perturbations sets.
	 * @return
	 */
	public Hashtable<String, AbstractPerturbation[]> getPerturbationsSet() {
		return perturbationsSet;
	}

	/**
	 * Return the selected perturbation set.
	 * @return
	 */
	public String getSelectedPerturbation() {
		return selectedPerturbationSet;
	}

	/**
	 * Set the perturbations set loaded from file.
	 * @param a
	 */
	public void setLoadedPerturbations(List a) {
		loadedPerturbations = a;
	}

	/**
	 * Set the mutations set loaded from file.
	 * @param a
	 */	
	public void setLoadedMutations(List a) {
		loadedMutations = a;
	}

	// Sets the selected perturbation from the saved Set
	/**
	 * Set the perturbations set selected by the user.
	 * @param a
	 */
	public void setSelectedPerturbation(String string) {
		selectedPerturbationSet = string;
	}

	/*
	 * Initial State
	 */
	
	/**
	 * Returns the set of initial conditions
	 * @return
	 */
	public Hashtable<String, Grid> getInitialStateSet() {
		return initialStateSet;
	}
/**
 * Generates a set of inital conditions with all values as zero.
 */
	public void noInitialState() {

		List<NodeInfo> initialStateComponents = new ArrayList();
		for (NodeInfo node : getUnitaryModel().getNodeOrder()) {
			if (!isIntegrationComponent(node)) {
				initialStateComponents.add(node);
			}
		}

		initialStateSet.put("none", new Grid(topology.getNumberInstances(),
				initialStateComponents));
	}

	/**
	 * Adds an initial set.
	 * @param name
	 */
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

	/**
	 * Sets the selected initial conditions set as active.
	 * @param string
	 */
	public void setSelectedInitialSet(String string) {
		selectedInitialSet = string;
		Grid initial_aux = initialStateSet.get(string);
		if (initial_aux != null)
			combineGrids(initial_aux);
	}

	/*
	 * Integration Functions and Inputs Definitions
	 */

	/**
	 * Returns the inputs set.
	 * @return
	 */
	public Hashtable<String, Hashtable<NodeInfo, List<String>>> getInputsIntegrationSet() {
		return integrationInputsSet;
	}

	
	public void noIntegrationFunctions() {

		// TODO: Solve this issue
	}

	/*
	 * This method adds an input set to the saved set of inputs
	 * 
	 * Input : String with the IntegrationInputSet name Output:
	 */

	/**
	 * Adds an inputs set.
	 * @param name
	 */
	public void setIntegrationInputsSet(String name) {

		Hashtable<NodeInfo, List<String>> integrationFunctions = new Hashtable<NodeInfo, List<String>>();

		for (NodeInfo node : getUnitaryModel().getNodeOrder()) {
			if (isIntegrationComponent(node)) {

				List<String> aux = new ArrayList<String>();
				for (byte value = 1; value < node.getMax() + 1; value++) {
					aux.add(getIntegrationFunction(node, value));
				}
				integrationFunctions.put(node, aux);
			}
		}
		integrationInputsSet.put(name, integrationFunctions);
	}

	/**
	 * Combine grids of environmental inputs and initial state inputs.
	 * @param grid
	 */
	public void combineGrids(Grid grid) {
		for (int instance = 0; instance < topology.getNumberInstances(); instance++) {
			for (NodeInfo node : grid.getListNodes()) {
				this.grid[instance][getUnitaryModel().getNodeOrder().indexOf(
						node)] = grid.getValue(instance, node);
			}
		}
	}

	/**
	 * Set the selected input set as active.
	 * @param string
	 */
	public void setSelectedInputSet(String string) {

		selectedInputSet = string;

		Hashtable<NodeInfo, List<String>> aux = integrationInputsSet
				.get(string);

		if (aux != null) {
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
				} else if (node.isInput()) {

					setIntegrationComponent(getUnitaryModel().getNodeOrder()
							.indexOf(node), false);

				}
			}
		}
	}

	/*
	 * Priorities
	 */

	/**
	 * Add a priorities set.
	 * @param name
	 * @param prioritiesClass
	 */
	public void setPrioritiesSet(String name,
			List<List<NodeInfo>> prioritiesClass) {
		prioritiesSet.put(name, prioritiesClass);
	}

	/**
	 * Returns the priority sets.
	 * @return
	 */
	public Hashtable<String, List<List<NodeInfo>>> getPrioritiesSet() {
		return prioritiesSet;
	}

	/**
	 * Return the selected priority set.
	 * @return
	 */
	public String getSelectedPriority() {
		return selectedPriority;
	}

	/**
	 * Set the selected priority set.
	 * @param string
	 */
	public void setSelectedPriority(String string) {
		selectedPriority = string;
	}
}
