package pt.gulbenkian.igc.nmd;

import java.util.ArrayList;

public class HexTopology {



	/* Methods to determine if instances are neighbours and groups of neighbours */

	public boolean areNeighbors(int instanceA, int instanceB) {
		ArrayList<Integer> neighbours = null;
		//neighbours = Neighbours.groupNeighbors(instanceA, 1);
		if (neighbours.contains(instanceB)) {
			return true;
		} else
			return false;

	}

	
}
