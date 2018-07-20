package org.epilogtool;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import javax.swing.JOptionPane;

import org.epilogtool.core.Epithelium;
import org.epilogtool.core.EpitheliumGrid;
import org.epilogtool.gui.EpiGUI;
import org.epilogtool.io.FileIO;
import org.epilogtool.project.Project;
import org.epilogtool.project.Simulation;
import org.apache.maven.artifact.versioning.DefaultArtifactVersion;
import org.epilogtool.common.RandCentral;
import org.epilogtool.common.Txt;

import com.martiansoftware.jsap.FlaggedOption;
import com.martiansoftware.jsap.JSAP;
import com.martiansoftware.jsap.JSAPException;
import com.martiansoftware.jsap.JSAPResult;
import com.martiansoftware.jsap.Parameter;
import com.martiansoftware.jsap.SimpleJSAP;
import com.martiansoftware.jsap.Switch;

public class Launcher {

	private static DefaultArtifactVersion MIN_JAVA_VERSION = new DefaultArtifactVersion("1.7");

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args)
			throws IOException, InstantiationException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException, ClassNotFoundException {
		SimpleJSAP jsap = null;
		JSAPResult jsapResult = null;

		// Default values
		int maxiter = 10;
		boolean bCMD = false;
		String pepsFile = null;
		long seed = -1;
		boolean dev = false;

		try {
			jsap = new SimpleJSAP(Launcher.class.getName(),
					"EpiLog is a tool which makes use of LogicalModel to implement "
							+ "a cellular automata, defining a convenient framework for the "
							+ "qualitative modelling of epithelium pattern formation.",
					new Parameter[] {
							new FlaggedOption("max-iter", JSAP.INTEGER_PARSER, "" + maxiter, JSAP.NOT_REQUIRED, 'i',
									"max-iter", "Maximum number of iterations."),
							new Switch("cmd", JSAP.NO_SHORTFLAG, "cmd"), new Switch("dev", JSAP.NO_SHORTFLAG, "dev"),
							new FlaggedOption("seed", JSAP.LONG_PARSER, "" + seed, JSAP.NOT_REQUIRED, JSAP.NO_SHORTFLAG,
									"seed", "Random generator seed number."),
							new FlaggedOption("file", JSAP.STRING_PARSER, pepsFile, JSAP.NOT_REQUIRED, 'f', "file",
									"PEPS (Project of Epithelium Patterning Simulation) file location."), });
			jsapResult = jsap.parse(args);
			if (jsap.messagePrinted())
				System.exit(0);
			maxiter = jsapResult.getInt("max-iter");
			bCMD = jsapResult.getBoolean("cmd");
			pepsFile = jsapResult.getString("peps");
			seed = jsapResult.getLong("seed");
			dev = jsapResult.getBoolean("dev");

		} catch (JSAPException e) {
			System.err.println(e.getMessage());
			System.exit(1);
		}

		// Check Java version
		checkJavaVersion(bCMD);

		// Check Number Generator Seed number
		if (seed != -1) {
			RandCentral.getInstance().setSeed(seed);
		}

		if (bCMD) {
			// Command line
			if (pepsFile != null) {
				Launcher.commandLine(pepsFile, maxiter);
			} else {
				System.err.println(Txt.get("s_LAUNCHER_NOGUI"));
			}
		} else {
			// GUI
			try {
				Txt.push("org.epilogtool.txt");
				OptionStore.init(Launcher.class.getPackage().getName());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			EpiGUI gui = EpiGUI.getInstance();
			gui.setDeveloperMode(dev);

			if (pepsFile != null && (new File(pepsFile).exists())) {
				gui.loadPEPS(pepsFile);
			} else {
				gui.newProject();
			}
		}
	}

	private static void checkJavaVersion(boolean cmd) {
		DefaultArtifactVersion currVersion = new DefaultArtifactVersion(System.getProperty("java.version"));
		if (currVersion.compareTo(MIN_JAVA_VERSION) < 0) {
			String msg = "You're using Java version " + currVersion + ". EpiLog needs Java version >= " + MIN_JAVA_VERSION;
			if (cmd) {
				System.err.println(msg);
			} else {
				JOptionPane.showMessageDialog(null, msg);
			}
			System.exit(0);
		}

	}

	private static void commandLine(String pepsFile, int maxiter)
			throws IOException, InstantiationException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException, ClassNotFoundException {
		FileIO.loadPEPS(pepsFile);

		List<Epithelium> epiList = Project.getInstance().getEpitheliumList();
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
				System.out.println(Txt.get("s_CMD_MAXITER"));
				System.exit(0);
			}
		} while (!simulator.isStableAt(i));

		System.out.println(Txt.get("s_CMD_STABLE"));
	}
}
