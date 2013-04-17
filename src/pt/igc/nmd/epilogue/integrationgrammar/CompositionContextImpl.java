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
	public Set<Integer> getNeighbourIndices(int instance, int minDistance, int maxDistance) {
		
		Set<Integer> results = topology.nDistanceNeighbours(instance, maxDistance);
		Set<Integer> frontier = topology.nDistanceNeighbours(instance,
				minDistance - 1);

		results.removeAll(frontier);
		return results;
	}

	@Override
	public Set<Integer> getNeighbourIndices(int instance, int distance){
		return getNeighbourIndices(instance, distance, distance);
	}
	
	@Override
	public NodeInfo getLowLevelComponentFromName(String componentName,
			int instance) {

		Map.Entry<String, Integer> key = new AbstractMap.SimpleEntry<String, Integer>(
				componentName, new Integer(instance));
		return translator.get(key);

	}

	
	
//	package pt.igc.nmd.epilogue.integrationgrammar;
//
//	import java.util.AbstractMap;
//	import java.util.HashSet;
//	import java.util.List;
//	import java.util.Map;
//	import java.util.Set;
//
//	import org.colomoto.logicalmodel.NodeInfo;
//
//	import composition.CompositionSpecificationDialog;
//
//	import pt.igc.nmd.epilogue.Topology;
//
//	public class CompositionContextImpl implements CompositionContext {
//		private Topology topology = null;
//		private List<NodeInfo> nodeOrder = null;
//		private Map<Map.Entry<String, Integer>, NodeInfo> translator = null;
//		private CompositionSpecificationDialog dialog = null;
//
//		public CompositionContextImpl(Topology topology, List<NodeInfo> nodeOrder,
//				Map<Map.Entry<String, Integer>, NodeInfo> translator) {
//			this.topology = topology;
//			this.nodeOrder = nodeOrder;
//			this.translator = translator;
//			this.dialog = dialog;
//			this.nodeOrder = nodeOrder;
//		}
//
//		@Override
//		public List<NodeInfo> getLowLevelComponents() {
//			return nodeOrder;
//		}
//
//		@Override
//		public NodeInfo getLowLevelComponentFromName(String componentName,
//				int instance) {
//
//			Map.Entry<String, Integer> key = new AbstractMap.SimpleEntry<String, Integer>(
//					componentName, new Integer(instance));
//			return translator.get(key);
//		}
//
//		@Override
//		public Set<Integer> getNeighbourIndices(int instance, int distance) {
//			Set<Integer> results = this.getAllNeighbourIndicesCloserThan(instance,
//					distance);
//			Set<Integer> frontier = this.getAllNeighbourIndicesCloserThan(instance,
//					distance - 1);
//
//			results.removeAll(frontier);
//			return results;
//		}
//
//		private Set<Integer> getAllNeighbourIndicesCloserThan(int instance,
//				int distance) {
//			Set<Integer> results = new HashSet<Integer>();
//
//			if (distance <= 0) {
//				results.add(new Integer(instance));
//				return results;
//			} else if (distance == 1) {
//				for (int n = 1; n <= dialog.getNumberInstances(); n++) {
//					if (dialog.areNeighbours(instance, n)) {
//						results.add(new Integer(n));
//					}
//				}
//				return results;
//			} else {
//				Set<Integer> frontier = this.getAllNeighbourIndicesCloserThan(
//						instance, distance - 1);
//				for (Integer v : frontier) {
//					for (int n = 1; n <= dialog.getNumberInstances(); n++)
//						if (dialog.areNeighbours(v.intValue(), n))
//							results.add(n);
//				}
//				return results;
//			}
//		}
//
//	}

	
}
