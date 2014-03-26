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
			EpitheliumGrid nextGrid = currGrid;
			int i = 0;
			System.out.println("Grid step " + i + "\n" + nextGrid);
			do {
				i++;
				currGrid = nextGrid;
				nextGrid = simulator.nextStepGrid();
				System.out.println("Grid step " + i + "\n" + nextGrid);
			} while (!currGrid.equals(nextGrid));
			
			System.out.println("Reached a stable grid!");
		}
	}
}
