package composition;

import java.util.AbstractMap;
import java.util.Collection;
import java.util.List;

import org.colomoto.logicalmodel.NodeInfo;

//import org.ginsim.core.graph.regulatorygraph.RegulatoryGraph;
//import org.ginsim.core.graph.regulatorygraph.RegulatoryNode;
//import org.ginsim.service.tool.composition.IntegrationFunctionMapping;

/**
 * Interface for the Specification of Composition Parameters
 * 
 * @author Nuno D. Mendes
 */

public interface CompositionSpecificationDialog {

	public int getNumberInstances();

	public void updateNumberInstances(int instances);



	public void addNeighbour(int m, int n);

	public void removeNeighbour(int m, int n);

	public boolean hasNeihgbours(int m);

	public boolean areNeighbours(int m, int n);



	public Collection<AbstractMap.Entry<NodeInfo, Integer>> getInfluencedModuleInputs(
			NodeInfo proper, int moduleIndex);

	public Collection<AbstractMap.Entry<NodeInfo, Integer>> getMappedToModuleArguments(
			NodeInfo input, int moduleIndex);

	public List<NodeInfo> getNodeOrder();
}
