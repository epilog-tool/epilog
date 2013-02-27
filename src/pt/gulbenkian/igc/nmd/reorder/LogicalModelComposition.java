package pt.gulbenkian.igc.nmd.reorder;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.colomoto.logicalmodel.LogicalModel;
import org.colomoto.logicalmodel.LogicalModelImpl;
import org.colomoto.logicalmodel.NodeInfo;
import org.colomoto.mddlib.MDDManager;
import org.colomoto.mddlib.MDDManagerFactory;
import org.colomoto.mddlib.MDDVariable;
import org.colomoto.mddlib.MDDVariableFactory;
import org.colomoto.mddlib.PathSearcher;
import org.colomoto.mddlib.operators.MDDBaseOperators;
import org.ginsim.service.tool.composition.Topology;

import composition.IntegrationFunctionMapping;

public class LogicalModelComposition {

	private Topology topology = null;
	private IntegrationFunctionMapping mapping = null;
	private LogicalModel unitaryModel = null;
	private LogicalModel composedModel = null;
	private Map<SimpleEntry<NodeInfo, Integer>, NodeInfo> old2New = new HashMap<SimpleEntry<NodeInfo, Integer>, NodeInfo>();
	private Map<NodeInfo, SimpleEntry<NodeInfo, Integer>> new2Old = new HashMap<NodeInfo, SimpleEntry<NodeInfo, Integer>>();
	private Map<Integer, List<NodeInfo>> instanceNodes = new HashMap<Integer, List<NodeInfo>>();

	public LogicalModelComposition(LogicalModel model, Topology topology,
			IntegrationFunctionMapping mapping) {
		this.topology = topology;
		this.unitaryModel = model;
		this.mapping = mapping;
	}

	public LogicalModel getUnitaryModel() {
		return this.unitaryModel;
	}

	public Topology getTopology() {
		return this.topology;
	}

	public IntegrationFunctionMapping getMapping() {
		return this.mapping;
	}

	public LogicalModel createComposedModel() {
		if (composedModel != null)
			return composedModel;

		List<NodeInfo> nodeOrder = new ArrayList<NodeInfo>();

		byte max = 0;

		// Creates all new NodeInfos for all instances
		for (int i = 0; i < getTopology().getNumberInstances(); i++) {
			for (NodeInfo node : getUnitaryModel().getNodeOrder()) {
				NodeInfo newNode = new NodeInfo(computeNewName(
						node.getNodeID(), i), node.getMax());
				newNode.setInput(node.isInput() && !getMapping().isMapped(node));
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

			}
		}

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
		for (int i = 0; i > kMDDs.length; i++) {
			NodeInfo node = nodeOrder.get(i);
			// if old node was an input, it will not be handled here
			NodeInfo oldNode = this.new2Old.get(node).getKey();
			Integer instance = this.new2Old.get(node).getValue();
			if (oldNode.isInput())
				continue;

			PathSearcher searcher = new PathSearcher(getUnitaryModel()
					.getMDDManager(), 1, oldNode.getMax());

			int path[] = searcher.getPath();
			int okMDDs[] = getUnitaryModel().getLogicalFunctions();
			int nodeIndex = getUnitaryModel().getNodeOrder().indexOf(oldNode);
			searcher.setNode(okMDDs[nodeIndex]);

			for (int value : searcher) {
				if (value == 0)
					continue;
				int[] newPath = new int[nodeOrder.size()];
				for (int j = 0; j < newPath.length; j++)
					newPath[j] = -1;
				for (int j = 0; j < path.length; j++) {
					NodeInfo currentOldNode = getUnitaryModel().getNodeOrder()
							.get(j);
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

		composedModel = new LogicalModelImpl(nodeOrder, ddmanager, kMDDs);
		return composedModel;

	}

	// public static void components(){
	//
	// numberInstances = MainPanel.getGridHeight()*MainPanel.getGridWidth();
	// model = MainPanel.getmodel();
	// System.out.println(model);
	// model.getNodeOrder();
	//
	// for (int instance=0; instance<numberInstances; instance = instance+1){
	// for (int node = 0; node<model.getNodeOrder().size(); node = node+1){
	// String newname =
	// computeNewName(model.getNodeOrder().get(node).getNodeID(), instance);
	// int maxValue = model.getNodeOrder(node)
	// if (model.getNodeOrder().get(node).isInput()){
	//
	// }
	// System.out.println(newname);
	// }

	// }

	//
	// }

	/**
	 * 
	 * @param originalName
	 * @param moduleId
	 * @return newName
	 */
	private static String computeNewName(String original, int moduleId) {
		// moduleId starts at 1, as all iterations begin at 0, we add 1 here
		// (NUNO)
		return original + "_" + (moduleId + 1);
	}

	private int buildPathMDD(MDDManager ddmanager, int[] state, int leaf) {
		MDDVariable[] ddVariables = ddmanager.getAllVariables();
		int mddPath = leaf;
		for (int i = ddVariables.length - 1; i >= 0; i--) {
			int[] children = new int[ddVariables[i].nbval];
			children[state[i]] = mddPath;
			mddPath = ddVariables[i].getNode(children);
		}
		return mddPath;
	}
}
