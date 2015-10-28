package org.ginsim.epilog;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.ginsim.epilog.core.Epithelium;
import org.ginsim.epilog.core.EpitheliumGrid;
import org.ginsim.epilog.gui.EpiGUI;
import org.ginsim.epilog.io.FileIO;
import org.ginsim.epilog.project.Project;
import org.ginsim.epilog.project.Simulation;

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
	 * @throws Exception
	 */
	public static void main(String[] args) throws IOException,
			InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException, ClassNotFoundException {
		SimpleJSAP jsap = null;
		JSAPResult jsapResult = null;

		// Default values
		int maxiter = 10;
		boolean bCMD = false;
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
							new Switch("cmd", JSAP.NO_SHORTFLAG, "cmd"),
							new FlaggedOption("peps", JSAP.STRING_PARSER,
									pepsFile, JSAP.NOT_REQUIRED,
									JSAP.NO_SHORTFLAG, "peps",
									"PEPS (Project of Epithelium Patterning Simulation) file location."), });
			jsapResult = jsap.parse(args);
			if (jsap.messagePrinted())
				System.exit(0);
			maxiter = jsapResult.getInt("max-iter");
			bCMD = jsapResult.getBoolean("cmd");
			pepsFile = jsapResult.getString("peps");

		} catch (JSAPException e) {
			System.err.println(e.getMessage());
			System.exit(1);
		}

		if (bCMD) {
			// Command line
			if (pepsFile != null) {
				Launcher.commandLine(pepsFile, maxiter);
			} else {
				System.err
						.println("Epilog needs a PEPS file when called non-gui mode.");
			}
		} else {
			// GUI
			try {
				OptionStore.init(Launcher.class.getPackage().getName());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			EpiGUI gui = EpiGUI.getInstance();

			if (pepsFile != null) {
				File f = new File(pepsFile);
				if (f.exists())
					gui.loadPEPS(pepsFile);
			}
		}
	}

	private static void commandLine(String pepsFile, int maxiter)
			throws IOException, InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException, ClassNotFoundException {
		Project project = FileIO.loadPEPS(pepsFile);

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
		} while (!simulator.isStableAt(i));

		System.out.println("Reached a stable grid!");

	}
}
