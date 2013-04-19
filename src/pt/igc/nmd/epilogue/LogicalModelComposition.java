package pt.igc.nmd.epilogue;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.colomoto.logicalmodel.LogicalModel;
import org.colomoto.logicalmodel.LogicalModelImpl;
import org.colomoto.logicalmodel.NodeInfo;
import org.colomoto.logicalmodel.io.ginml.LogicalModel2GINML;
import org.colomoto.logicalmodel.tool.reduction.ModelReducer;
import org.colomoto.mddlib.MDDManager;
import org.colomoto.mddlib.MDDManagerFactory;
import org.colomoto.mddlib.MDDVariable;
import org.colomoto.mddlib.MDDVariableFactory;
import org.colomoto.mddlib.PathSearcher;
import org.colomoto.mddlib.operators.MDDBaseOperators;

import pt.igc.nmd.epilogue.integrationgrammar.CompositionContext;
import pt.igc.nmd.epilogue.integrationgrammar.CompositionContextImpl;
import pt.igc.nmd.epilogue.integrationgrammar.IntegrationFunctionClause;
import pt.igc.nmd.epilogue.integrationgrammar.IntegrationFunctionClauseSet;
import pt.igc.nmd.epilogue.integrationgrammar.IntegrationFunctionDNFFactory;

import composition.IntegrationFunctionMapping;
//import org.ginsim.service.tool.composition.Topology;

//import composition.IntegrationFunctionMapping;

public class LogicalModelComposition {

	private MainPanel mainPanel;
	private IntegrationFunctionMapping mapping = null;
	private LogicalModel composedModel = null;
	private Map<Map.Entry<NodeInfo, Integer>, NodeInfo> old2New = new HashMap<Map.Entry<NodeInfo, Integer>, NodeInfo>();
	private Map<Map.Entry<String, Integer>, NodeInfo> oldString2New = new HashMap<Map.Entry<String, Integer>, NodeInfo>();
	private Map<NodeInfo, Map.Entry<NodeInfo, Integer>> new2Old = new HashMap<NodeInfo, Map.Entry<NodeInfo, Integer>>();
	private Map<Integer, List<NodeInfo>> instanceNodes = new HashMap<Integer, List<NodeInfo>>();

	public LogicalModelComposition(MainPanel mainPanel) {
		this.mainPanel = mainPanel;

	}

	public IntegrationFunctionMapping getMapping() {
		return this.mapping;
	}

	public void setMapping(IntegrationFunctionMapping mapping) {
		this.mapping = mapping;
	}

	public LogicalModel createComposedModel() {
		// if (composedModel != null)
		// return composedModel;

		List<NodeInfo> nodeOrder = new ArrayList<NodeInfo>();
		List<NodeInfo> newIntegrationNodes = new ArrayList<NodeInfo>();

		byte max = 0;

		// Creates all new NodeInfos for all instances
		for (int i = 0; i < mainPanel.getTopology().getNumberInstances(); i++) {
			for (NodeInfo node : mainPanel.getEpithelium().getUnitaryModel()
					.getNodeOrder()) {
				NodeInfo newNode = new NodeInfo(computeNewName(
						node.getNodeID(), i), node.getMax());

				// integration inputs are no longer inputs
				if (node.isInput()
						&& mainPanel.getIntegrationComponents().contains(
								node.getNodeID())) {

					newNode.setInput(false);
					newIntegrationNodes.add(newNode);
				} else {
					newNode.setInput(node.isInput());
				}

				nodeOrder.add(newNode);
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
				mainPanel.getTopology(), nodeOrder, this.oldString2New);

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

			PathSearcher searcher = new PathSearcher(mainPanel.getEpithelium()
					.getUnitaryModel().getMDDManager(), 1, oldNode.getMax());

			int path[] = searcher.getPath();
			int okMDDs[] = mainPanel.getEpithelium().getUnitaryModel()
					.getLogicalFunctions();
			int nodeIndex = mainPanel.getEpithelium().getUnitaryModel()
					.getNodeOrder().indexOf(oldNode);
			searcher.setNode(okMDDs[nodeIndex]);

			for (int value : searcher) {
				if (value == 0)
					continue;
				int[] newPath = new int[nodeOrder.size()];
				for (int j = 0; j < newPath.length; j++)
					newPath[j] = -1;
				for (int j = 0; j < path.length; j++) {
					NodeInfo currentOldNode = mainPanel.getEpithelium()
							.getUnitaryModel().getNodeOrder().get(j);
					NodeInfo currentNewNode = this.old2New
							.get(new SimpleEntry<NodeInfo, Integer>(
									currentOldNode, instance));
					newPath[nodeOrder.indexOf(currentNewNode)] = path[j];

				}

				int pathMDD = buildPathMDD(ddmanager, newPath, value);
				kMDDs[i] = MDDBaseOperators.OR.combine(ddmanager, kMDDs[i],
						pathMDD);
			}
		}

		// Create MDDs for integration components

		ArrayList<String> integrationComponents = mainPanel
				.getIntegrationComponents();

		for (String oldeNodeIdIntegrationComponent : integrationComponents) {

			System.out.println(mainPanel.getIntegrationFunction());
			for (byte targetValue : mainPanel.getIntegrationFunction().keySet()) {

				for (int i = 0; i < mainPanel.getTopology()
						.getNumberInstances(); i++) {
					IntegrationFunctionDNFFactory factory = new IntegrationFunctionDNFFactory(
							context);
					IntegrationFunctionClauseSet clauseSet = factory
							.getClauseSet(mainPanel.getIntegrationFunction()
									.get(targetValue), i);

//					System.err.println("FINAL for instance " + i + " :\n"
//							+ clauseSet);

					if (!clauseSet.isImpossible()) {
						NodeInfo integrationComponent = this.oldString2New
								.get(new SimpleEntry<String, Integer>(
										oldeNodeIdIntegrationComponent, i));
						int index = nodeOrder.indexOf(integrationComponent);

						buildIntegrationPaths(ddmanager, index, kMDDs, context,
								clauseSet, targetValue);
 
					}
				}
			}
		}
		composedModel = new LogicalModelImpl(nodeOrder, ddmanager, kMDDs);

		LogicalModel2GINML exporter = new LogicalModel2GINML(composedModel);
		try {
			exporter.export(new FileOutputStream("test_beforeReduction.ginml"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// Perform reduction of integration components
		ModelReducer reducer = new ModelReducer(composedModel);
		for (NodeInfo integrationNode : newIntegrationNodes) {
			//System.err.println("Reducing " + integrationNode.getNodeID());
			reducer.remove(nodeOrder.indexOf(integrationNode));
		}

		composedModel = reducer.getModel();

		exporter = new LogicalModel2GINML(composedModel);
//		try {
//			exporter.export(new FileOutputStream("test_afterReduction.ginml"));
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

		mainPanel.getEpithelium().setComposedModel(composedModel);
		return composedModel;

	}

	/**
	 * 
	 * @param originalName
	 * @param moduleId
	 * @return newName
	 */
	public String computeNewName(String original, int moduleId) {
		// moduleId starts at 1, as all iterations begin at 0, we add 1 here
		return original + "_" + (moduleId);
	}

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

	public void resetComposedModel() {
		composedModel = null;
		// TODO Auto-generated method stub

	}

	public void buildIntegrationPaths(MDDManager ddmanager, int index,
			int[] kMDDs, CompositionContext context,
			IntegrationFunctionClauseSet clauseSet, byte targetValue) {

		for (IntegrationFunctionClause myClause : clauseSet.getClauses()) {

			byte[] clause = myClause.asByteArray(context);

			// for (int w = 0; w < clause.length; w++)
			// if (clause[w] != -1)
			// System.err.print("(" + w + ") " + clause[w] + " ");
			// System.err.println();
			// System.err.println();

			int[] path = new int[clause.length];
			for (int z = 0; z < path.length; z++)
				path[z] = (int) clause[z];

			int pathMDD = buildPathMDD(ddmanager, path, targetValue);
			kMDDs[index] = MDDBaseOperators.OR.combine(ddmanager, kMDDs[index],
					pathMDD);
		}
	}

	public void resetLogicalModelComposition() {
		mapping = null;
		composedModel = null;
		old2New = new HashMap<Map.Entry<NodeInfo, Integer>, NodeInfo>();
		oldString2New = new HashMap<Map.Entry<String, Integer>, NodeInfo>();
		new2Old = new HashMap<NodeInfo, Map.Entry<NodeInfo, Integer>>();
		instanceNodes = new HashMap<Integer, List<NodeInfo>>();
	}

}
