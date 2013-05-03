package pt.igc.nmd.epilogue;

import java.awt.Color;
import java.io.Serializable;
import java.util.Hashtable;

import org.colomoto.logicalmodel.LogicalModel;
import org.colomoto.logicalmodel.NodeInfo;

import pt.igc.nmd.epilogue.integrationgrammar.IntegrationFunctionSpecification;
import pt.igc.nmd.epilogue.integrationgrammar.IntegrationFunctionSpecification.IntegrationExpression;

public class SphericalEpithelium implements Epithelium, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Color colors[] = { Color.orange, Color.green, Color.blue,
			Color.pink, Color.yellow, Color.magenta, Color.cyan, Color.red,
			Color.LIGHT_GRAY, Color.black };

	private transient LogicalModel unitaryModel;
	private transient LogicalModel composedModel;
	private Hashtable<NodeInfo, Color> node2Color;
	private Hashtable<NodeInfo, Boolean> activeComponents;
	private Hashtable<Integer, Boolean> perturbedInstances;
	private Hashtable<NodeInfo, Boolean> integrationComponents;
	private Hashtable<NodeInfo, Hashtable<Byte, String>> integrationFunctionStrings;

	private Hashtable<NodeInfo, Integer> initialState;

	private Topology topology;

	public SphericalEpithelium(Topology topology) {

		this.topology = topology;
		this.node2Color = new Hashtable<NodeInfo, Color>();
		this.activeComponents = new Hashtable<NodeInfo, Boolean>();
		this.perturbedInstances = new Hashtable<Integer, Boolean>();
		this.integrationFunctionStrings = new Hashtable<NodeInfo, Hashtable<Byte, String>>();
		this.integrationComponents = new Hashtable<NodeInfo, Boolean>();
		this.unitaryModel = unitaryModel;
		this.initialState = new Hashtable<NodeInfo, Integer>();
	}

	public void setInitialState(NodeInfo nodeInfo, Integer initialStateValue) {
		this.initialState.put(nodeInfo, initialStateValue);
	}

	public int getInitialState(NodeInfo a2) {
		return (int) this.initialState.get(a2);
	}

	public void initializeColors() {
		System.out.println("Unitary Model @initializeColors" + unitaryModel);
		for (int i = 0; i < unitaryModel.getNodeOrder().size(); i++) {
			int j = 0;
			if (i < colors.length)
				j = i;
			else
				j = colors.length - 1;

			setColor(unitaryModel.getNodeOrder().get(i), colors[j]);
			// System.out.println(colors[j]);
		}
	}

	public void setIntegrationComponents(NodeInfo node, boolean b) {
		this.integrationComponents.put(node, b);
	}

	public Hashtable<NodeInfo, Boolean> getIntegrationComponents() {
		return this.integrationComponents;
	}

	public Hashtable<Byte, String> getIntegrationFunctions(NodeInfo node) {
		return this.integrationFunctionStrings.get(node);
	}

	public IntegrationExpression string2Expression(
			String integrationfunctionString) {

		IntegrationFunctionSpecification spec = new IntegrationFunctionSpecification();
		IntegrationExpression expression = null;

		try {
			expression = spec.parse(integrationfunctionString);
		} catch (org.antlr.runtime.RecognitionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return expression;

	}

	public void setIntegrationFunctions(NodeInfo node,
			Hashtable<Byte, String> integrationFunctions) {

		if (this.integrationFunctionStrings.get(node) == null)
			this.integrationFunctionStrings.put(node,
					new Hashtable<Byte, String>());

		for (Byte i = 1; i <= integrationFunctions.size(); i++)
			this.integrationFunctionStrings.get(node).put(i,
					integrationFunctions.get(i));
		//
		// System.out.println(integrationFunctionStrings);

	}

	public Topology getTopology() {
		return this.topology;
	}

	@Override
	public void setUnitaryModel(LogicalModel model) {
		this.unitaryModel = model;
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

	public Hashtable<NodeInfo, Color> getColors() {
		return this.node2Color;
	}

	public void setColor(NodeInfo node, Color color) {
		this.node2Color.put(node, color);
	}

	public Hashtable<NodeInfo, Boolean> getComponentsDisplayOn() {
		return this.activeComponents;
	}

	public void setActiveComponents(NodeInfo node, Boolean bool) {
		// System.out.println(this.activeComponents);
		this.activeComponents.put(node, bool);
	}

	public Color getColor(NodeInfo node) {
		return node2Color.get(node);
	}

	public void setPerturbedInstance(int i, int j, boolean b) {
		int instance = topology.coords2Instance(i, j);
		perturbedInstances.put(instance, b);

	}

	public boolean getPerturbedInstance(int instance) {
		return perturbedInstances.get(instance);

	}

	public void setPerturbedInstance(int instance, boolean b) {
		perturbedInstances.put(instance, b);

	}

}
