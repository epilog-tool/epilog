package pt.igc.nmd.epilogue;

import org.colomoto.logicalmodel.LogicalModel;
import org.colomoto.logicalmodel.NodeInfo;

public class Simulation {

	private int iterationNumber = 0;
	private LogicalModel model = null;
	private byte[] state = null;

	public Simulation(LogicalModel model, byte[] initialState) {
		this.model = model;
		this.state = initialState;
	}

	public void run() {
		byte[] currentState;
		do {
			currentState = this.state;
			step();
		} while (hasChanged(currentState, this.state));
	}


	public void step() {
		for (NodeInfo node : model.getNodeOrder()){
			int index = model.getNodeOrder().indexOf(node);
			byte current = this.state[index];
			byte target = model.getTargetValue(index, this.state);
			byte next = (byte) (current + (target-current/Math.abs((int) target-current)));
			
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
	
	private boolean hasChanged(byte[] previous, byte[] current){
		if (previous.length != current.length)
			return true;
		
		for (int i = 0; i < previous.length; i++)
			if (previous[i] != current[i])
				return true;
		
		return false;
				
	}

}
