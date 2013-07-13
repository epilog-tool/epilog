package pt.igc.nmd.epilog.integrationgrammar;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.colomoto.logicalmodel.NodeInfo;

import pt.igc.nmd.epilog.Topology;

public class CompositionContextImpl implements CompositionContext {
	private Topology topology = null;
	private List<NodeInfo> nodeOrder = null;
	private Map<Map.Entry<String, Integer>, NodeInfo> translator = null;

	/**
	 * Implements the composition context
	 * 
	 * @param topology
	 *            associated topology
	 * @param nodeOrder
	 *            node order associated with the model
	 * @param translator
	 *            relates a node with a string with the nodeID
	 * 
	 */
	public CompositionContextImpl(Topology topology, List<NodeInfo> nodeOrder,
			Map<Map.Entry<String, Integer>, NodeInfo> translator) {
		this.topology = topology;
		this.nodeOrder = nodeOrder;
		this.translator = translator;
	}

	/**
	 * Returns the list of nodes after the composition
	 * 
	 * @return nodeOrder
	 * 
	 */
	@Override
	public List<NodeInfo> getLowLevelComponents() {
		return nodeOrder;
	}

	/**
	 * Returns the indexes of all neighbors, when in a range.
	 * 
	 * @param instance
	 *            instance that has the neighbors
	 * @param minDistance
	 *            minimum distance of neighbors
	 * @param maxDistance
	 *            maximum distance of neighbors
	 * @return results set of neighbours
	 * 
	 */
	@Override
	public Set<Integer> getNeighbourIndices(int instance, int minDistance,
			int maxDistance) {
		// System.err.println("Calculating neighbours at range " + minDistance
		// + ":" + maxDistance);

		Set<Integer> results = topology.nDistanceNeighbours(instance,
				maxDistance);
		Set<Integer> frontier = topology.nDistanceNeighbours(instance,
				minDistance - 1);

		results.removeAll(frontier);
		// System.out.println(instance + " -> " + results);
		return results;
	}

	/**
	 * Returns the indexes of all neighbors, when only at a specified distance.
	 * 
	 * @param instance
	 *            instance that has the neighbors
	 * @param minDistance
	 *            minimum distance of neighbors
	 * @param maxDistance
	 *            maximum distance of neighbors
	 * @return results set of neighbours
	 * 
	 */
	@Override
	public Set<Integer> getNeighbourIndices(int instance, int distance) {
		return getNeighbourIndices(instance, distance, distance);
	}

	/**
	 * Returns node related to a string and an instance
	 * 
	 * @param componentName
	 *            string with the if of the component
	 * @param instance
	 *            instance
	 * 
	 * @return node
	 * 
	 */
	@Override
	public NodeInfo getLowLevelComponentFromName(String componentName,
			int instance) {

		Map.Entry<String, Integer> key = new AbstractMap.SimpleEntry<String, Integer>(
				componentName, new Integer(instance));
		return translator.get(key);

	}

}
