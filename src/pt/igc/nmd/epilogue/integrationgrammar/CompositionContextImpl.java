package pt.igc.nmd.epilogue.integrationgrammar;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.colomoto.logicalmodel.NodeInfo;


import pt.igc.nmd.epilogue.Topology;

public class CompositionContextImpl implements CompositionContext {
	private Topology topology = null;
	private List<NodeInfo> nodeOrder = null;
	private Map<Map.Entry<String, Integer>, NodeInfo> translator = null;

	public CompositionContextImpl(Topology topology, List<NodeInfo> nodeOrder,
			Map<Map.Entry<String, Integer>, NodeInfo> translator) {
		this.topology = topology;
		this.nodeOrder = nodeOrder;
		this.translator = translator;
	}

	@Override
	public List<NodeInfo> getLowLevelComponents() {
		return nodeOrder;
	}

	@Override
	public Set<Integer> getNeighbourIndices(int instance, int distance) {
		
		Set<Integer> results = topology.nDistanceNeighbours(instance, distance);
		Set<Integer> frontier = topology.nDistanceNeighbours(instance,
				distance - 1);

		results.removeAll(frontier);
		return results;
	}

	@Override
	public NodeInfo getLowLevelComponentFromName(String componentName,
			int instance) {

		Map.Entry<String, Integer> key = new AbstractMap.SimpleEntry<String, Integer>(
				componentName, new Integer(instance));
		return translator.get(key);

	}

}
