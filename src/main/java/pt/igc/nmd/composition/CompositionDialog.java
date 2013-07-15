package pt.igc.nmd.composition;

import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;

import org.colomoto.logicalmodel.NodeInfo;



public class CompositionDialog implements CompositionSpecificationDialog {

	Topology topology = null;

	public CompositionDialog() {
	}

	@Override
	public int getNumberInstances() {
		return this.topology.getNumberInstances();
	}

	@Override
	public void updateNumberInstances(int instances) {
		this.topology = new Topology(instances);
	}

	@Override
	public void addNeighbour(int m, int n) {
		this.topology.addNeighbour(m, n);

	}

	@Override
	public void removeNeighbour(int m, int n) {
		this.topology.removeNeighbour(m, n);
	}

	@Override
	public boolean hasNeihgbours(int m) {
		return this.topology.hasNeighbours(m);
	}

	@Override
	public boolean areNeighbours(int m, int n) {
		return this.topology.areNeighbours(m, n);
	}

	@Override
	public Collection<Entry<NodeInfo, Integer>> getInfluencedModuleInputs(
			NodeInfo proper, int moduleIndex) {
		
		return null;
	}

	@Override
	public Collection<Entry<NodeInfo, Integer>> getMappedToModuleArguments(
			NodeInfo input, int moduleIndex) {
		
		return null;
	}

	@Override
	public List<NodeInfo> getNodeOrder() {
		
		return null;
	}

}
