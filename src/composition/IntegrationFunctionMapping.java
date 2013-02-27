package composition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.colomoto.logicalmodel.NodeInfo;

/**
 * The mapping of the input components
 * 
 * @author Nuno D. Mendes
 */
public class IntegrationFunctionMapping {
	
	private HashMap<NodeInfo, RegulatoryIntegration> mapping = new HashMap<NodeInfo, RegulatoryIntegration>();

	public IntegrationFunctionMapping() {
	}

	/**
	 * @param input
	 *            An input component being mapped
	 * 
	 * @param properList
	 *            A list of proper components the input is being mapped to
	 * 
	 * @param integrationFunction
	 *            A representation of the logical function that determines the
	 *            value of the input based on the value of the mapped proper
	 *            components
	 */
	public void addMapping(NodeInfo input, RegulatoryIntegration integration) {
		if (!input.isInput()) {
			return; // TODO: throw Exception
		}

		mapping.put(input, integration);

	}

	/**
	 * @param input
	 *            An input components
	 * 
	 * @return The list of proper components the input is mapped to
	 */
	public List<NodeInfo> getProperComponentsForInput(NodeInfo input) {
		RegulatoryIntegration integration = mapping.get(input);
		if (integration == null) {
			return null;
		}
		return integration.getProperComponents();
	}

	public boolean isMapped(NodeInfo node){
		return this.mapping.containsKey(node);
	}
	
}
