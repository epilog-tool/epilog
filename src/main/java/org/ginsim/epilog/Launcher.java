package org.ginsim.epilog;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.ginsim.epilog.core.Epithelium;
import org.ginsim.epilog.core.EpitheliumGrid;
import org.ginsim.epilog.io.FileIO;


public class Launcher {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		if (args != null && args.length > 0) {
			File fPEPS = new File(args[0]);
			Project project = FileIO.loadPEPS(fPEPS);
			
			List<Epithelium> epiList = project.getEpitheliumList();
			Simulation simulator = new Simulation(epiList.get(0));
			
			EpitheliumGrid currGrid = simulator.getCurrentGrid();
			printGrid(currGrid);
			
			simulator.nextStepGrid();
			currGrid = simulator.getCurrentGrid();
			printGrid(currGrid);
			
			simulator.nextStepGrid();
			currGrid = simulator.getCurrentGrid();
			printGrid(currGrid);
		}
	}
	
	private static void printGrid(EpitheliumGrid grid) {
		for (int x = 0; x < grid.getX(); x++) {
			for (int y = 0; y < grid.getY(); y++) {
				byte[] currState = grid.getCellState(x, y);
				System.out.print("["+x+","+y+"]:");
				for (int i = 0; i < currState.length; i++) {
					System.out.print(" " + currState[i]);
				}
				System.out.println();
			}
		}
		System.out.println();
	}
}
