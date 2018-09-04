package org.epilogtool.integration;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.colomoto.biolqm.NodeInfo;
import org.epilogtool.common.Tuple2D;
import org.epilogtool.core.Epithelium;
import org.epilogtool.core.EpitheliumGrid;

public class IntegrationSignal implements IntegrationSignalExpression {
	private String componentName;
	private byte minThreshold;
	private IntegrationDistance distance;

	public IntegrationSignal(String name, String minThreshold, IntegrationDistance distance) {
		this.componentName = name;
		this.minThreshold = (byte) Integer.parseInt(minThreshold);
		this.distance = distance;
	}

	public String getComponentName() {
		return componentName;
	}

	public byte getMinThreshold() {
		return minThreshold;
	}

	@Override
	public Set<Tuple2D<Integer>> evaluate(int x, int y, Epithelium epi, EpitheliumGrid epiGrid,
			Map<Tuple2D<Integer>, Map<Boolean, Set<Tuple2D<Integer>>>> relativeNeighboursCache) {
		Set<Tuple2D<Integer>> result = new HashSet<Tuple2D<Integer>>();

		// Verify existence of node and threshold within limits
		NodeInfo node = epi.getComponentUsed(this.componentName);
		byte minThreshold = this.getMinThreshold();
		if (node == null || minThreshold < 0 || minThreshold > node.getMax()) {
			return result;
		}

		// Get neighbours
		Tuple2D<Integer> rangePair = new Tuple2D<Integer>(this.distance.getMin(), this.distance.getMax());

		// Shouldn't this be outside of the grammar ?

		Tuple2D<Integer> rangeList_aux = new Tuple2D<Integer>(0,
				(this.distance.getMin() - 1 > 0) ? this.distance.getMin() - 1 : 0);

		Set<Tuple2D<Integer>> positionNeighbours = epiGrid.getPositionNeighbours(relativeNeighboursCache, rangeList_aux,
				rangePair, this.distance.getMin(), x, y);

		for (Tuple2D<Integer> tuple : positionNeighbours) {
			List<NodeInfo> lNodes = epiGrid.getModel(tuple.getX(), tuple.getY()).getComponents();
			for (int n = 0; n < lNodes.size(); n++) {
				if (node.getNodeID().equals(lNodes.get(n).getNodeID())) {
					byte state = epiGrid.getCellState(tuple.getX(), tuple.getY())[n];
					if (minThreshold <= state) {
						result.add(tuple);
						break;
					}
				}
			}
		}
		return result;
	}

	@Override
	public Set<String> getRegulators() {
		Set<String> sNodeIDs = new HashSet<String>();
		sNodeIDs.add(this.componentName);
		return sNodeIDs;
	}
}
