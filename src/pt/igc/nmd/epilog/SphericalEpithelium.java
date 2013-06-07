package pt.igc.nmd.epilog;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

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

	AbstractPerturbation[] perturbations = null;
	private AbstractPerturbation activePerturbation;

	private Grid envGrid;
	private Grid initialGrid;
	private PerturbedGrid perturbedGrid;

	private String selectedPriority;
	private String selectedPerturbation;

	private Hashtable<String, List<List<NodeInfo>>> prioritiesSet;

	// private Hashtable<String, AbstractPerturbation> perturbationsList;

	private Hashtable<String, List<List<NodeInfo>>> perturbationsSet;
	private Hashtable<String, List<List<NodeInfo>>> initialSet;
	private Hashtable<String, List<List<NodeInfo>>> inputSet;

	private Topology topology = null;

	private boolean newEpithelium;

	public SphericalEpithelium(Topology topology) {

		this.topology = topology;
		prioritiesSet = new Hashtable<String, List<List<NodeInfo>>>();
		// perturbationsList = new Hashtable<String, AbstractPerturbation>();
		perturbationsSet = new Hashtable<String, List<List<NodeInfo>>>();
		initialSet = new Hashtable<String, List<List<NodeInfo>>>();
		inputSet = new Hashtable<String, List<List<NodeInfo>>>();
	}

	// SBML INFORMATION

	public String getSBMLFilename() {
		return SBMLFilename;
	}

	public String getSBMLFilePath() {
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

	private void initializeColors() {

		this.nodeColor = new Color[getUnitaryModel().getNodeOrder().size()];
		for (int i = 0; i < this.nodeColor.length; i++)
			this.nodeColor[i] = colors[i < colors.length ? i
					: colors.length - 1];
	}

	public void setColor(NodeInfo node, Color color) {
		this.nodeColor[getUnitaryModel().getNodeOrder().indexOf(node)] = color;
	}

	public Color getColor(NodeInfo node) {
		return nodeColor[getUnitaryModel().getNodeOrder().indexOf(node)];
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
		return this.integrationFunctionStrings[getUnitaryModel().getNodeOrder()
				.indexOf(node)] != null;
	}

	public String getIntegrationFunction(NodeInfo node, byte value) {
		return this.integrationFunctionStrings[getUnitaryModel().getNodeOrder()
				.indexOf(node)][value - 1];
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

	public void setInitialState(int i, int j) {
		List<NodeInfo> listNodes = getUnitaryModel().getNodeOrder();

		int instance = topology.coords2Instance(i, j);
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

	public void createGrids() {

		List<NodeInfo> listEnvNodes = new ArrayList<NodeInfo>();
		List<NodeInfo> listInitialConditions = new ArrayList<NodeInfo>();

		for (NodeInfo node : getUnitaryModel().getNodeOrder()) {
			if (node.isInput())
				listEnvNodes.add(node);
			else
				listInitialConditions.add(node);
		}
		this.envGrid = new Grid(topology.getNumberInstances(), listEnvNodes);
		this.initialGrid = new Grid(topology.getNumberInstances(),
				listInitialConditions);
	}

	public void initializeGrid(Grid grid) {
		for (int i = 0; i < topology.getNumberInstances(); i++) {
			this.grid[i] = new byte[grid.getListNodes().size()];
			for (NodeInfo node : grid.getListNodes())
				this.grid[i][grid.getListNodes().indexOf(node)] = 0;
		}
	}

	public void initializeGrids() {
		createGrids();
		initializeGrid(this.envGrid);
		initializeGrid(this.initialGrid);
	}

	public void setGrid(Integer instance, NodeInfo node, byte value) {
		this.grid[instance][getUnitaryModel().getNodeOrder().indexOf(node)] = value;

	}

	/*
	 * Perturbations
	 */

	public AbstractPerturbation getActivePerturbation() {
		return activePerturbation;
	}

	public void setActivePerturbation(AbstractPerturbation perturbation) {
		activePerturbation = perturbation;
	}

	public void initializePerturbationsGrid() {
		perturbations = new AbstractPerturbation[topology.getNumberInstances()];
	}

	public void setPerturbedInstance(int i, int j) {

		AbstractPerturbation perturbation = getActivePerturbation();
		int instance = topology.coords2Instance(i, j);
		perturbations[instance] = perturbation;
		System.out.println(instance + " " + perturbation);
	}

	public boolean isCellPerturbed(int instance) {
		if (perturbations[instance] == null)
			return false;
		else
			return true;
	}

	public AbstractPerturbation getInstancePerturbation(int instance) {
		return perturbations[instance];
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
