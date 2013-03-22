package pt.igc.nmd.epilogue;

import java.awt.Color;
import java.util.Hashtable;

import org.colomoto.logicalmodel.LogicalModel;

public class SphericalEpithelium implements Epithelium {


	private LogicalModel unitaryModel = null;
	private Hashtable<String, Color> node2Color;
	private Hashtable<String, Boolean> activeComponents;
	private Topology topology;

	
	
	

	public SphericalEpithelium(Topology topology) {

		this.topology = topology;
		node2Color = new Hashtable<String, Color>();
		activeComponents = new Hashtable<String, Boolean>();

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
		return null;
	}

	public Hashtable<String, Color> getColors() {
		return this.node2Color;
	}
	public void setColor(String nodeID, Color color) {
		this.node2Color.put(nodeID, color);
	}
	

	public Hashtable<String, Boolean> getComponentsDisplayOn() {
		return this.activeComponents;
	}
	public void setActiveComponents(String nodeID, Boolean bool) {
		this.activeComponents.put(nodeID, bool);
	}
	



}
