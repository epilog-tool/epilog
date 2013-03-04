package pt.igc.nmd.epilogue;

import java.util.Hashtable;

import org.colomoto.logicalmodel.LogicalModel;
import org.colomoto.logicalmodel.NodeInfo;

public class Simulation {

	private int iterationNumber = 0;
	private LogicalModel model = null;
	private byte[] state = null;
	private SphericalEpithelium epithelium;
	public Hashtable<String, Integer> environmentCells;

	public Simulation(SphericalEpithelium epithelium) {
		this.epithelium = epithelium;
		this.state = null;
		this.environmentCells = new Hashtable<String, Integer>();
	}

	// public Simulation(LogicalModel model, byte[] initialState) {
	// this.model = model;
	// this.state = initialState;
	// }

	private static String computeNewName(String nodeID, int instanceIndex) {
		// moduleId starts at 1, as all iterations begin at 0, we add 1 here
		// (NUNO)
		return nodeID + "_" + (instanceIndex + 1);
	}

	public void initializeEnvironmentCells(String nodeID) {
		int totalInstances = this.epithelium.getTopology().getTotalInstances();
		for (int instance = 0; instance < totalInstances; instance++) {

			this.environmentCells.put(computeNewName(nodeID, instance), 0);
		}
	}
	
	public void setEnvironmentCell(int row, int column, String nodeID, int status){
		int instanceIndex = this.epithelium.getTopology().coords2instance(row, column);
		
		this.environmentCells.put(computeNewName (nodeID, instanceIndex), status);
	}

	public void run() {
		byte[] currentState;
		do {
			currentState = this.state;
			step();
		} while (hasChanged(currentState, this.state));
	}

	public void step() {
		for (NodeInfo node : model.getNodeOrder()) {
			int index = model.getNodeOrder().indexOf(node);
			byte current = this.state[index];
			byte target = model.getTargetValue(index, this.state);
			byte next = (byte) (current + (target - current
					/ Math.abs((int) target - current)));

			this.state[index] = next;
		}

		this.iterationNumber++;
	}

	public int getIterationNumber() {
		return this.iterationNumber;
	}

	public byte[] getCurrentState() {
		return this.state;
	}

	private boolean hasChanged(byte[] previous, byte[] current) {
		if (previous.length != current.length)
			return true;

		for (int i = 0; i < previous.length; i++)
			if (previous[i] != current[i])
				return true;

		return false;

	}

}
