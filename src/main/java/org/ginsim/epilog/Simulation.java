package org.ginsim.epilog;

import java.util.ArrayList;
import java.util.List;

import org.ginsim.epilog.core.Epithelium;
import org.ginsim.epilog.core.EpitheliumGrid;

public class Simulation {
	private Epithelium epithelium;
	private List<EpitheliumGrid> stateHistory;
	
	public Simulation(Epithelium e) {
		this.epithelium = e;
		this.stateHistory = new ArrayList<EpitheliumGrid>();
		this.stateHistory.add(this.epithelium.getEpitheliumGrid());
	}

}
