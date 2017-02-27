package org.cellularasynchronyIntegration;

import java.util.List;
import java.util.Set;

import org.colomoto.logicalmodel.NodeInfo;

/**
 * 
 * Interface to provide semantic information to integration grammar parsing tree
 * traversals
 * 
 * @author Nuno D Mendes
 * 
 */
public interface CompositionContext {

	/**
	 * @return list of the NodeInfos of the original module
	 */
	public List<NodeInfo> getLowLevelComponents();

	/**
	 * 
	 * Get neighbours at an exact distance
	 * 
	 * @param instance
	 *            identification of the reference instance
	 * @param distance
	 *            the exact distance from the reference instance
	 * @return the indices identifying neighbours at the given distance from the
	 *         reference instance
	 */
	public List<Integer> getNeighbourIndices(int instance, int distance);
	
	public List<Integer> getNeighbourIndices(int instance, int minDistance,
			int maxDistance);

	/**
	 * Obtain a NodeInfo from the name it had in the original module and an
	 * instance identification
	 * 
	 * @param componentName
	 *            the name of the component in the original module
	 * @param instance
	 *            the identification of the instance
	 * @return the corresponding NodeInfo in the composed model
	 */
	public NodeInfo getLowLevelComponentFromName(String componentName,
			int instance);


}
