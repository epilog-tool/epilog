package org.ginsim.epilog;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.ginsim.epilog.core.Epithelium;
import org.ginsim.epilog.core.EpitheliumGrid;
import org.ginsim.epilog.io.FileIO;

import com.martiansoftware.jsap.FlaggedOption;
import com.martiansoftware.jsap.JSAP;
import com.martiansoftware.jsap.JSAPException;
import com.martiansoftware.jsap.JSAPResult;
import com.martiansoftware.jsap.Parameter;
import com.martiansoftware.jsap.SimpleJSAP;

public class Launcher {

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		SimpleJSAP jsap = null;
		JSAPResult jsapResult = null;

		// Default values
		int maxiter = 1000;
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
							new FlaggedOption("gui", JSAP.BOOLEAN_PARSER, ""
									+ bGUI, JSAP.NOT_REQUIRED,
									JSAP.NO_SHORTFLAG, "gui",
									"No Graphical User Interface, command line mode"),
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

		}
	}

	private static void commandLine(String pepsFile, int maxiter)
			throws IOException {
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
