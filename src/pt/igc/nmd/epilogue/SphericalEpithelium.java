package pt.igc.nmd.epilogue;

import java.awt.Color;
import java.util.Hashtable;

import org.colomoto.logicalmodel.LogicalModel;
import org.colomoto.logicalmodel.NodeInfo;

public class SphericalEpithelium implements Epithelium {


	private LogicalModel unitaryModel = null;
	private LogicalModel composedModel = null;
	private Hashtable<NodeInfo, Color> node2Color;
	private Hashtable<NodeInfo, Boolean> activeComponents;
	private Topology topology;

	
	
	

	public SphericalEpithelium(Topology topology) {

		this.topology = topology;
		node2Color = new Hashtable<NodeInfo, Color>();
		activeComponents = new Hashtable<NodeInfo, Boolean>();

	}

	
	public Topology getTopology(){
		return this.topology;
	}
	@Override
	public void setUnitaryModel(LogicalModel model) {
		this.unitaryModel = model;
	}

	@Override
	public LogicalModel getUnitaryModel() {
		return this.unitaryModel;
	}

	@Override
	public LogicalModel getComposedModel() {
		return this.composedModel;
	}
	public void setComposedModel(LogicalModel composedModel) {
		this.composedModel = composedModel;
	}

	public Hashtable<NodeInfo, Color> getColors() {
		return this.node2Color;
	}
	public void setColor(NodeInfo node, Color color) {
		this.node2Color.put(node, color);
	}
	

	public Hashtable<NodeInfo, Boolean> getComponentsDisplayOn() {
		return this.activeComponents;
	}
	public void setActiveComponents(NodeInfo node, Boolean bool) {
		this.activeComponents.put(node, bool);
	}

	public Color getColor(NodeInfo node) {
		return node2Color.get(node);
	}
	



}
