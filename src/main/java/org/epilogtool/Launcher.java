package org.epilogtool;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JOptionPane;

import org.apache.maven.artifact.versioning.DefaultArtifactVersion;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.epilogtool.common.RandCentral;
import org.epilogtool.common.Txt;
import org.epilogtool.core.Epithelium;
import org.epilogtool.core.EpitheliumGrid;
import org.epilogtool.gui.EpiGUI;
import org.epilogtool.gui.widgets.VisualGridSimulation;
import org.epilogtool.io.FileIO;
import org.epilogtool.project.Project;
import org.epilogtool.project.Simulation;

import com.apple.eawt.Application;
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
		int maxiter = 0;
		boolean bnoGUI = false;
		String pepsFile = null;
		String vizVars = null;
		String pngName = null;
		String epiName = null;
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
							new Switch("no-gui", JSAP.NO_SHORTFLAG, "no-gui"),
							new Switch("dev", JSAP.NO_SHORTFLAG, "dev"),
							new FlaggedOption("seed", JSAP.LONG_PARSER, "" + seed, JSAP.NOT_REQUIRED, JSAP.NO_SHORTFLAG,
									"seed", "Random generator seed number."),
							new FlaggedOption("epi-name", JSAP.STRING_PARSER, epiName, JSAP.NOT_REQUIRED,
									JSAP.NO_SHORTFLAG, "epi-name",
									"Epithelium name to be selected for command line simulation."),
							new FlaggedOption("png-file", JSAP.STRING_PARSER, pngName, JSAP.NOT_REQUIRED,
									JSAP.NO_SHORTFLAG, "png-file", "PNG file name to store the last grid."),
							new FlaggedOption("viz-vars", JSAP.STRING_PARSER, vizVars, JSAP.NOT_REQUIRED,
									JSAP.NO_SHORTFLAG, "viz-vars",
									"Components to be visualised on the Epithelium grid image."),
							new FlaggedOption("file", JSAP.STRING_PARSER, pepsFile, JSAP.NOT_REQUIRED, 'f', "file",
									"PEPS (Project of Epithelium Patterning Simulation) file location."), });
			jsapResult = jsap.parse(args);
			if (jsap.messagePrinted())
				System.exit(0);
			maxiter = jsapResult.getInt("max-iter");
			bnoGUI = jsapResult.getBoolean("no-gui");
			pepsFile = jsapResult.getString("file");
			pngName = jsapResult.getString("png-file");
			epiName = jsapResult.getString("epi-name");
			vizVars = jsapResult.getString("viz-vars");
			seed = jsapResult.getLong("seed");
			dev = jsapResult.getBoolean("dev");

		} catch (JSAPException e) {
			System.err.println(e.getMessage());
			System.exit(1);
		}

		// Check Java version
		checkJavaVersion(bnoGUI);

		// Check Number Generator Seed number
		if (seed != -1) {
			RandCentral.getInstance().setSeed(seed);
		}

		try {
			Txt.push("org.epilogtool.txt");
			OptionStore.init(Launcher.class.getPackage().getName());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (bnoGUI) {
			// Command line
			if (pepsFile == null || maxiter <= 0 || !(new File(pepsFile)).isFile()) {
				System.err.println(Txt.get("s_CMD_NOGUI"));
			} else {
				Launcher.commandLine(pepsFile, maxiter, pngName, vizVars, epiName);
			}
		} else {
			// GUI
			EpiGUI gui = EpiGUI.getInstance();
			gui.setDeveloperMode(dev);

			try {
				MavenXpp3Reader reader = new MavenXpp3Reader();
				Model project;
				if ((new File("pom.xml")).exists()) {
					project = reader.read(new FileReader("pom.xml"));
				} else {
					project = reader.read(new InputStreamReader(
							Application.class.getResourceAsStream("/META-INF/maven/org.epilogtool/EpiLog/pom.xml")));
				}
				gui.setVersion(project.getVersion());
			} catch (Exception e) {
			}

			if (pepsFile != null && (new File(pepsFile).exists())) {
				gui.loadPEPS(pepsFile);
			} else {
				gui.newProject();
			}
		}
	}

	private static void checkJavaVersion(boolean bnoGUI) {
		DefaultArtifactVersion currVersion = new DefaultArtifactVersion(System.getProperty("java.version"));
		if (currVersion.compareTo(MIN_JAVA_VERSION) < 0) {
			String msg = "You're using Java version " + currVersion + ". EpiLog needs Java version >= "
					+ MIN_JAVA_VERSION;
			if (bnoGUI) {
				System.err.println(msg);
			} else {
				JOptionPane.showMessageDialog(null, msg);
			}
			System.exit(0);
		}

	}

	private static void commandLine(String pepsFile, int maxiter, String pngName, String vizVars, String epiName)
			throws IOException, InstantiationException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException, ClassNotFoundException {
		FileIO.loadPEPS(pepsFile);

		Epithelium epi = null;
		if (epiName == null) {
			epi = Project.getInstance().getEpitheliumList().get(0);
		} else {
			for (Epithelium e : Project.getInstance().getEpitheliumList()) {
				if (e.getName().equals(epiName)) {
					epi = e;
					break;
				}
			}
		}
		if (epi == null) {
			System.out.println("Invalid Epithelium name: " + epiName);
			System.exit(0);
		}

		Simulation simulation = new Simulation(epi);

		EpitheliumGrid currGrid = simulation.getCurrentGrid();
		EpitheliumGrid nextGrid = currGrid;
		int i = 0;
		System.out.print("Step:" + i + "\n" + nextGrid);
		while (!simulation.isStableAt(i) && i < maxiter && simulation.getTerminalCycleLen() < 0) {
			currGrid = nextGrid;
			nextGrid = simulation.nextStepGrid();
			i++;
			System.out.print("Step:" + i + "\n" + nextGrid);
		}

		if (simulation.isStableAt(i)) {
			System.out.println(Txt.get("s_CMD_STABLE"));
		} else if (simulation.getTerminalCycleLen() > 0) {
			System.out.println(Txt.get("s_CMD_CYCLE") + simulation.getTerminalCycleLen());
		} else {
			System.out.println(Txt.get("s_CMD_MAXITER"));
		}

		if (pngName != null) {
			Set<String> sComps = null;
			if (vizVars != null) {
				sComps = new HashSet<String>(Arrays.asList(vizVars.split(",")));
			} else {
				sComps = nextGrid.getComponents();
			}

			VisualGridSimulation visualGridSim = new VisualGridSimulation(nextGrid, sComps, null);
			FileIO.writeEpitheliumGrid2File(pngName, visualGridSim, "png");
		}
	}
}
