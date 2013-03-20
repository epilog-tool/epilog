package pt.igc.nmd.epilogue;

import java.awt.Color;
import java.util.Hashtable;

import org.colomoto.logicalmodel.LogicalModel;

public class SphericalEpithelium implements Epithelium {

	private int width;
	private int height;
	private LogicalModel unitaryModel = null;
	private LogicalModel composedModel = null;
	private Hashtable<String, Color> node2Color;
	private Hashtable<String, Boolean> activeComponents;
	private Topology topology;
	private Hashtable<String, Integer> initialState;
	private Hashtable<String, Integer> composedInitialState;
	
	
	

	public SphericalEpithelium(Topology topology) {
		this.width = 0;
		this.height = 0;
		this.topology = topology;
		node2Color = new Hashtable<String, Color>();
		activeComponents = new Hashtable<String, Boolean>();
		initialState = new Hashtable<String, Integer>();
		 composedInitialState= new Hashtable<String, Integer>();
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
	
	public void setInitialState(String nodeID, Integer initialStateValue){
		this.initialState.put(nodeID, initialStateValue);
	}
	
	public int getInitialState(String nodeID){
		return (int) this.initialState.get(nodeID);
	}

	public void setComposedInitialState(String composedNodeID, int ComposedInitialState){
		this.composedInitialState.put(composedNodeID, ComposedInitialState);
	}
	public Hashtable<String,Integer> getComposedInitialState(){
		return this.composedInitialState;
	}
}
