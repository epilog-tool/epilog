package org.ginsim.epilog;

import java.util.ArrayList;
import java.util.List;

import org.colomoto.logicalmodel.LogicalModel;
import org.colomoto.logicalmodel.NodeInfo;
import org.ginsim.epilog.core.Epithelium;
import org.ginsim.epilog.core.EpitheliumGrid;
import org.ginsim.epilog.core.EpitheliumIntegrationFunctions;
import org.ginsim.epilog.core.ModelPriorityClasses;

/**
 * Initializes and implements the simulation on epilog. 
 * 
 * @author Pedro Monteiro and Pedro Varela
 *
 */
public class Simulation {
	private Epithelium epithelium;
	private List<EpitheliumGrid> stateHistory;

	/** 
	 * Initializes the simulation. It is called after creating and epithelium. Creates a list of EpitheliumGrids, to allow the user to travel to a previously calculated (and saved) epitheliumGrid.
	 * This list is initialized with the current EpitheliumGrid (the one defined in the initialConditions).
	 * 
	 * @param e the epithelium the user is working with.
	 * 
	 */
	public Simulation(Epithelium e) {
		this.epithelium = e;
		this.stateHistory = new ArrayList<EpitheliumGrid>();
		this.stateHistory.add(this.epithelium.getEpitheliumGrid());
	}

	
	/**
	 * This function retrieves the next step in the simulation. The first step in this 
	 */
	public void nextStep() {
		//--BEGIN integration
		
		
		
		
		//--END integration
		EpitheliumIntegrationFunctions eif = this.epithelium.getEpitheliumIntegrationFunctions();
		for (int i = 0; i < this.epithelium.getX(); i++) {
			for (int j = 0; j < this.epithelium.getY(); j++) {
				LogicalModel m = this.epithelium.getModel(i,j);
				for (NodeInfo node : m.getNodeOrder()) {
					if (node.isInput() && eif.containsKey(node.getNodeID())) {
						
					}
				}
			}
		}
		
		//--END grandeFesta
		int x = this.epithelium.getX(), y = this.epithelium.getY();
		EpitheliumGrid newGrid = new EpitheliumGrid(x, y,
				this.epithelium.getModel(0, 0));

		for (int i = 0; i < this.epithelium.getX(); i++) {
			for (int j = 0; j < this.epithelium.getY(); j++) {
				LogicalModel m = this.epithelium.getModel(i,j);
				ModelPriorityClasses mpc = this.epithelium.getPriorityClasses(m);
				for (int p = 0; p < mpc.size(); p++) {
					
				}
				
				for (int k = 0; k < m.getNodeOrder().size(); k++) {
					byte next = this.nextComponentValue()
				}
				
			}
		}
		this.stateHistory.add(newGrid);
	}
	
	private byte nextComponentValue() {
		
	}
}
