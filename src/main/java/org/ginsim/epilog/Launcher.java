package org.ginsim.epilog;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.ginsim.epilog.core.Epithelium;
import org.ginsim.epilog.core.EpitheliumGrid;
import org.ginsim.epilog.gui.EpiGUI;
import org.ginsim.epilog.io.FileIO;

import com.martiansoftware.jsap.FlaggedOption;
import com.martiansoftware.jsap.JSAP;
import com.martiansoftware.jsap.JSAPException;
import com.martiansoftware.jsap.JSAPResult;
import com.martiansoftware.jsap.Parameter;
import com.martiansoftware.jsap.SimpleJSAP;
import com.martiansoftware.jsap.Switch;

public class Launcher {

	/**
	 * @param args
	 * @throws IOException
	 * @throws ClassNotFoundException 
	 * @throws SecurityException 
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	public static void main(String[] args) throws IOException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, ClassNotFoundException {
		SimpleJSAP jsap = null;
		JSAPResult jsapResult = null;

		// Default values
		int maxiter = 10;
		boolean bGUI = false;
		String pepsFile = null;

		try {
			jsap = new SimpleJSAP(
					Launcher.class.getName(),
					"EpiLog is a tool which makes use of LogicalModel to implement "
							+ "a cellular automata, defining a convenient framework for the "
							+ "qualitative modelling of epithelium pattern formation.",
					new Parameter[] {
							new FlaggedOption("max-iter", JSAP.INTEGER_PARSER,
									"" + maxiter, JSAP.NOT_REQUIRED, 'i',
									"max-iter", "Maximum number of iterations."),
							new Switch("gui", JSAP.NO_SHORTFLAG, "gui"),
							new FlaggedOption("peps", JSAP.STRING_PARSER,
									pepsFile, JSAP.REQUIRED, JSAP.NO_SHORTFLAG,
									"peps",
									"PEPS (Project of Epithelium Patterning Simulation) file location."), });
			jsapResult = jsap.parse(args);
			if (jsap.messagePrinted())
				System.exit(0);
			maxiter = jsapResult.getInt("max-iter");
			bGUI = jsapResult.getBoolean("gui");
			pepsFile = jsapResult.getString("peps");
		} catch (JSAPException e) {
			System.err.println(e.getMessage());
			System.exit(1);
		}

		if (!bGUI) {
			// Command line
			Launcher.commandLine(pepsFile, maxiter);
		} else {
			// GUI
			EpiGUI gui = new EpiGUI();
			// gui.initialize();
		}
	}

	private static void commandLine(String pepsFile, int maxiter)
			throws IOException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, ClassNotFoundException {
		File fPEPS = new File(pepsFile);
		Project project = FileIO.loadPEPS(fPEPS);

		List<Epithelium> epiList = project.getEpitheliumList();
		Simulation simulator = new Simulation(epiList.get(0));

		EpitheliumGrid currGrid = simulator.getCurrentGrid();
		EpitheliumGrid nextGrid = currGrid;
		int i = 0;
		System.out.println(nextGrid);
		do {
			i++;
			currGrid = nextGrid;
			nextGrid = simulator.nextStepGrid();
			System.out.println("Grid step " + i + "\n" + nextGrid);
			if (i > maxiter) {
				System.out
						.println("Reached maximum number of iterations! Exiting...");
				System.exit(0);
			}
		} while (!currGrid.equals(nextGrid));

		System.out.println("Reached a stable grid!");

	}
}
