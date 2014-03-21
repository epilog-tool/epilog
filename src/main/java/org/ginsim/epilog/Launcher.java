package org.ginsim.epilog;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.ginsim.epilog.core.Epithelium;
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
			
		}
	}

}
