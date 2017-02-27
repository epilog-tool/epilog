package org.cellularasynchrony;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.colomoto.logicalmodel.LogicalModel;
import org.colomoto.logicalmodel.LogicalModelImpl;
import org.colomoto.logicalmodel.NodeInfo;
import org.colomoto.logicalmodel.tool.reduction.ModelReducer;
import org.colomoto.mddlib.MDDManager;
import org.colomoto.mddlib.MDDManagerFactory;
import org.colomoto.mddlib.MDDVariable;
import org.colomoto.mddlib.MDDVariableFactory;
import org.colomoto.mddlib.PathSearcher;
import org.colomoto.mddlib.operators.MDDBaseOperators;

import org.cellularasynchronyIntegration.CompositionContext;
import org.cellularasynchronyIntegration.CompositionContextImpl;
import org.cellularasynchronyIntegration.IntegrationFunctionMDDFactory;
import org.cellularasynchronyIntegration.IntegrationFunctionMapping;
import org.cellularasynchronyIntegration.CompositionContext;
import org.cellularasynchronyIntegration.CompositionContextImpl;
import org.cellularasynchronyIntegration.IntegrationFunctionMDDFactory;

public class LogicalModelComposition {

	private MainFrame mainFrame;
	private IntegrationFunctionMapping mapping = null;
	private LogicalModel composedModel = null;
	private Map<Map.Entry<NodeInfo, Integer>, NodeInfo> old2New = new HashMap<Map.Entry<NodeInfo, Integer>, NodeInfo>();
	private Map<Map.Entry<String, Integer>, NodeInfo> oldString2New = new HashMap<Map.Entry<String, Integer>, NodeInfo>();
	public Map<NodeInfo, Map.Entry<NodeInfo, Integer>> new2Old = new HashMap<NodeInfo, Map.Entry<NodeInfo, Integer>>();
	private Map<Integer, List<NodeInfo>> instanceNodes = new HashMap<Integer, List<NodeInfo>>();

	/**
	 * Initializes the creation of the composed model.
	 * 
	 * @param mainPanel
	 */
	public LogicalModelComposition(MainFrame mainPanel) {
		this.mainFrame = mainPanel;

	}

	/**
	 * Get the mapping used.
	 * 
	 * @return mapping
	 */
	public IntegrationFunctionMapping getMapping() {
		return this.mapping;
	}

	/**
	 * Set the mapping.
	 * 
	 * @param mapping
	 */
	public void setMapping(IntegrationFunctionMapping mapping) {
		this.mapping = mapping;
	}

	/**
	 * Create the composed model.
	 * 
	 * @return composed model
	 */
	public LogicalModel createComposedModel() {

		

		mainFrame.setUserMessage(MsgStatus.STATUS, "Starting to create the composed model");

		List<NodeInfo> nodeOrder = new ArrayList<NodeInfo>();
		List<NodeInfo> newIntegrationNodes = new ArrayList<NodeInfo>();

		byte max = 0;

		// Creates all new NodeInfos for all instances
		for (int i = 0; i < mainFrame.topology.getNumberInstances(); i++) {
			for (NodeInfo node : mainFrame.getEpithelium().getUnitaryModel()
					.getNodeOrder()) {
				NodeInfo newNode = new NodeInfo(computeNewName(
						node.getNodeID(), i), node.getMax());

				// System.out.println(mainPanel.getEpithelium().getIntegrationComponents()
				// .get(node));

				// integration inputs are no longer inputs
				if (node.isInput()
						&& mainFrame.getEpithelium().isIntegrationComponent(
								node)) {

					// System.out.println("Old Integrative Node that is about to not be an input "+node.getNodeID());
					newNode.setInput(false);
					newIntegrationNodes.add(newNode);
				} else {
					newNode.setInput(node.isInput());
				}

				nodeOrder.add(newNode);
				// System.err.println("Just created node " +
				// newNode.getNodeID());

				if (newNode.getMax() > max)
					max = newNode.getMax();

				this.new2Old.put(newNode, new SimpleEntry<NodeInfo, Integer>(
						node, new Integer(i)));
				if (this.instanceNodes.get(new Integer(i)) == null)
					this.instanceNodes.put(new Integer(i),
							new ArrayList<NodeInfo>());

				this.instanceNodes.get(new Integer(i)).add(newNode);
				this.old2New.put(new SimpleEntry<NodeInfo, Integer>(node,
						new Integer(i)), newNode);
				this.oldString2New.put(
						new SimpleEntry<String, Integer>(node.getNodeID(),
								new Integer(i)), newNode);
			}
		}

		// Create Composition Context

		CompositionContext context = new CompositionContextImpl(
				mainFrame.topology, nodeOrder, this.oldString2New);

		// Create MDD variables
		MDDVariableFactory mvf = new MDDVariableFactory();
		for (NodeInfo node : nodeOrder) {
			mvf.add(node, (byte) (node.getMax() + 1));

		}

		// Creates ddmanager with created variables, indicating the maximum
		// number of leaves (largest maxValue plus '0')
		MDDManager ddmanager = MDDManagerFactory.getManager(mvf, max + 1);

		int[] kMDDs = new int[nodeOrder.size()];

		// Create MDDs for proper components (copy from old ones)

		for (int i = 0; i < kMDDs.length; i++) {
			// System.err.println("Processing node with index " + i);
			NodeInfo node = nodeOrder.get(i);
			if (newIntegrationNodes.contains(node)) {
				continue;
			}

			NodeInfo oldNode = this.new2Old.get(node).getKey();
			Integer instance = this.new2Old.get(node).getValue();

			PathSearcher searcher = new PathSearcher(mainFrame.getEpithelium()
					.getUnitaryModel().getMDDManager(), 1, oldNode.getMax());

			int path[] = searcher.getPath();
			int okMDDs[] = mainFrame.getEpithelium().getUnitaryModel()
					.getLogicalFunctions();
			int nodeIndex = mainFrame.getEpithelium().getUnitaryModel()
					.getNodeOrder().indexOf(oldNode);
			searcher.setNode(okMDDs[nodeIndex]);

			for (int value : searcher) {
				if (value == 0)
					continue;
				int[] newPath = new int[nodeOrder.size()];
				for (int j = 0; j < newPath.length; j++)
					newPath[j] = -1;
				for (int j = 0; j < path.length; j++) {
					NodeInfo currentOldNode = mainFrame.getEpithelium()
							.getUnitaryModel().getNodeOrder().get(j);
					NodeInfo currentNewNode = this.old2New
							.get(new SimpleEntry<NodeInfo, Integer>(
									currentOldNode, instance));
					newPath[nodeOrder.indexOf(currentNewNode)] = path[j];
				}

				// System.err.println("Node " + oldNode.getNodeID() + "@"
				// + instance + " takes value " + value + " when :");
				// for (int p = 0; p < newPath.length; p++)
				// if (newPath[p] != -1)
				// System.err.print(nodeOrder.get(p).getNodeID() + "="
				// + newPath[p] + " ");
				// System.err.println();

				int pathMDD = buildPathMDD(ddmanager, newPath, value);

				kMDDs[i] = MDDBaseOperators.OR.combine(ddmanager, kMDDs[i],
						pathMDD);
			}
		}

		// Create MDDs for integration components

		for (NodeInfo oldeNodeIdIntegrationComponent : mainFrame.epithelium
				.getUnitaryModel().getNodeOrder()) {

			if (mainFrame.epithelium
					.isIntegrationComponent(oldeNodeIdIntegrationComponent)) {

				for (byte targetValue = 1; targetValue < oldeNodeIdIntegrationComponent
						.getMax() + 1; targetValue++) {

					// System.out.println(oldeNodeIdIntegrationComponent + " " +
					// targetValue);

					for (int i = 0; i < mainFrame.topology.getNumberInstances(); i++) {
						IntegrationFunctionMDDFactory factory = new IntegrationFunctionMDDFactory(
								context, ddmanager);

						String function = mainFrame.epithelium
								.getIntegrationFunction(
										oldeNodeIdIntegrationComponent,
										targetValue);

						int integrationMDD = factory.getMDD(
								mainFrame.epithelium
										.string2Expression(function), i);

						// get all Paths and build them for targetValue
						PathSearcher searcher = new PathSearcher(ddmanager, 1,
								oldeNodeIdIntegrationComponent.getMax());

						int path[] = searcher.getPath();
						searcher.setNode(integrationMDD);

						for (int value : searcher) {

							if (value == 0)
								continue;

							int pathMDD = buildPathMDD(ddmanager, path,
									targetValue);
							NodeInfo integrationComponent = this.oldString2New
									.get(new SimpleEntry<String, Integer>(
											oldeNodeIdIntegrationComponent
													.getNodeID(), i));
							// System.out.println(integrationComponent);
							int index = nodeOrder.indexOf(integrationComponent);
							kMDDs[index] = MDDBaseOperators.OR.combine(
									ddmanager, kMDDs[index], pathMDD);
						}
					}
				}
			}
		}
		composedModel = new LogicalModelImpl(nodeOrder, ddmanager, kMDDs);

		// LogicalModel2GINML exporter = new LogicalModel2GINML(composedModel);
		// try {
		// exporter.export(new FileOutputStream("test_beforeReduction.ginml"));
		// } catch (FileNotFoundException e) {

		// e.printStackTrace();
		// } catch (IOException e) {

		// e.printStackTrace();
		// }

		// Perform reduction of integration components
		ModelReducer reducer = new ModelReducer(composedModel);
		// System.err.println("New Integration Nodes: "+ newIntegrationNodes);
		for (NodeInfo integrationNode : newIntegrationNodes) {
			// System.err.println("Reducing " + integrationNode.getNodeID());
			reducer.remove(nodeOrder.indexOf(integrationNode));
		}

		composedModel = reducer.getModel();

		// System.out.println(composedModel.getNodeOrder());
		// System.out.println(composedModel.getExtraComponents());
		// for (NodeInfo node : mainPanel.getEpithelium().getUnitaryModel()
		// .getNodeOrder())
		// System.out.println(node + " -> " +mainPanel.getEpithelium()
		// .isIntegrationComponent(node));

		// exporter = new LogicalModel2GINML(composedModel);
		// try {
		// exporter.export(new FileOutputStream("test_afterReduction.ginml"));
		// } catch (FileNotFoundException e) {

		// e.printStackTrace();
		// } catch (IOException e) {

		// e.printStackTrace();
		// }

		mainFrame.getEpithelium().setComposedModel(composedModel);
		mainFrame.setUserMessage(MsgStatus.STATUS, "Composed model created");
		return composedModel;

	}




	/**
	 * Generate new name for the composed component
	 * 
	 * @param originalName
	 *            modeID
	 * @param moduleId
	 *            instance number
	 * @return newName
	 */
	public String computeNewName(String original, int moduleId) {
		// moduleId starts at 1, as all iterations begin at 0, we add 1 here
		return original + "_" + (moduleId);
	}

	/**
	 * Build the new MDD Path.
	 * 
	 * @param ddmanager
	 *            MDD manager
	 * @param state
	 *            state of the world
	 * @param leaf
	 *            leaf
	 * @return MDD path
	 */
	private int buildPathMDD(MDDManager ddmanager, int[] state, int leaf) {
		MDDVariable[] ddVariables = ddmanager.getAllVariables();
		int mddPath = leaf;
		for (int i = ddVariables.length - 1; i >= 0; i--) {
			int[] children = new int[ddVariables[i].nbval];
			// System.out.println(ddVariables[i].nbval + " " + state[i]);
			if (state[i] >= 0) {
				children[state[i]] = mddPath;
				mddPath = ddVariables[i].getNode(children);
			}
		}
		return mddPath;
	}

	/**
	 * Resets the composed model.
	 */
	public void resetComposedModel() {
		composedModel = null;

	}

	/**
	 * Resets the logical model composition parameters.
	 */
	public void resetLogicalModelComposition() {
		mapping = null;
		composedModel = null;
		old2New = new HashMap<Map.Entry<NodeInfo, Integer>, NodeInfo>();
		oldString2New = new HashMap<Map.Entry<String, Integer>, NodeInfo>();
		new2Old = new HashMap<NodeInfo, Map.Entry<NodeInfo, Integer>>();
		instanceNodes = new HashMap<Integer, List<NodeInfo>>();
	}

	/**
	 * Returns the composed node related to an instance and a node from the
	 * original regulatory model.
	 * 
	 * @param node
	 *            original node
	 * @param instance
	 *            instance
	 * @return newNode
	 */
	public NodeInfo getComposedNode(NodeInfo node, int instance) {
		return this.old2New.get(new SimpleEntry<NodeInfo, Integer>(node,
				new Integer(instance)));
	}

	/**
	 * Returns the original node retrieved from a composed node.
	 * 
	 * @param node
	 *            composed node
	 * @return Old node
	 */
	public NodeInfo getOriginalNode(NodeInfo node) {
		return this.new2Old.get(node).getKey();
	}

	/**
	 * Returns the instance related to a composed node
	 * 
	 * @param node
	 *            composed node
	 * @return instance
	 */
	public Integer getOriginalInstance(NodeInfo node) {
		return this.new2Old.get(node).getValue();
	}

}