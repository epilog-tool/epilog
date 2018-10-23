package org.epilogtool.io;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.antlr.runtime.RecognitionException;
import org.colomoto.biolqm.LogicalModel;
import org.colomoto.biolqm.NodeInfo;
import org.colomoto.biolqm.modifier.perturbation.AbstractPerturbation;
import org.colomoto.biolqm.modifier.perturbation.FixedValuePerturbation;
import org.colomoto.biolqm.modifier.perturbation.MultiplePerturbation;
import org.colomoto.biolqm.modifier.perturbation.RangePerturbation;
import org.colomoto.biolqm.tool.simulation.multiplesuccessor.ModelPriorityClasses;
import org.epilogtool.OptionStore;
import org.epilogtool.common.EnumRandomSeed;
import org.epilogtool.common.RandCentral;
import org.epilogtool.common.Tuple2D;
import org.epilogtool.common.Txt;
import org.epilogtool.core.ComponentIntegrationFunctions;
import org.epilogtool.core.EmptyModel;
import org.epilogtool.core.Epithelium;
import org.epilogtool.core.EpitheliumGrid;
import org.epilogtool.core.UpdateCells;
import org.epilogtool.core.topology.RollOver;
import org.epilogtool.gui.color.ColorUtils;
import org.epilogtool.notification.NotificationManager;
import org.epilogtool.project.Project;
import org.epilogtool.project.ProjectFeatures;
import org.epilogtool.services.TopologyService;

public class Parser {

	public static void loadConfigurations(File fConfig) throws IOException, InstantiationException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException,
			SecurityException, ClassNotFoundException, RecognitionException {
		FileInputStream fstream = new FileInputStream(fConfig);
		DataInputStream in = new DataInputStream(fstream);
		BufferedReader br = new BufferedReader(new InputStreamReader(in));

		Map<String, String> modelKey2Name = new HashMap<String, String>();
		Epithelium currEpi = null;
		RollOver rollover = null;
		EnumRandomSeed randomSeedType = null;
		int randomSeed = 0;

		String x = null;
		String y = null;
		String topologyLayout = null;

		String line, epiName = null;
		String[] saTmp;

		while ((line = br.readLine()) != null) {
			line = line.trim();
			if (line.startsWith("#"))
				continue;

			// Load SBML model numerical identifiers and create new project
			if (line.startsWith("SB")) {
				saTmp = line.split("\\s+");

				File fSBML = new File(fConfig.getParent() + "/" + saTmp[2]);
				try {
					Project.getInstance().loadModel(fSBML.getName(), FileIO.loadSBMLModel(fSBML));
				} catch (Exception e) {
					throw new IOException(Txt.get("s_SBML_failed_load"));
				}
				modelKey2Name.put(saTmp[1], saTmp[2]);
				Color modelColor = ColorUtils.getColor(saTmp[3], saTmp[4], saTmp[5]);
				Project.getInstance().getProjectFeatures().setModelColor(saTmp[2], modelColor);
			}

			if (line.startsWith("CC")) {
				saTmp = line.split("\\s+");
				Color componentColor = ColorUtils.getColor(saTmp[2], saTmp[3], saTmp[4]);
				Project.getInstance().getProjectFeatures().setNodeColor(saTmp[1], componentColor);
			}

			// Epithelium name
			if (line.startsWith("SN")) {
				epiName = line.split("\\s+")[1];
				currEpi = null;
				rollover = RollOver.NONE;
				randomSeed = RandCentral.getInstance().nextInt();
				randomSeedType = EnumRandomSeed.RANDOM;
			}

			if (line.startsWith("GD")) {
				saTmp = line.split("\\s+");
				x = saTmp[1];
				y = saTmp[2];
				topologyLayout = saTmp[3];

				if (topologyLayout == "Hexagon-Even-PointyTopped") {
					topologyLayout = "Pointy-Even";
				} else if (topologyLayout == "Hexagon-Odd-PointyTopped") {
					topologyLayout = "Pointy-Odd";
				} else if (topologyLayout == "Hexagon-Even-FlatTopped") {
					topologyLayout = "Flat-Even";
				} else if (topologyLayout == "Hexagon-Odd-FlatTopped") {
					topologyLayout = "Flat-Odd";
				}
			}

			// RollOver
			if (line.startsWith("RL")) {
				rollover = RollOver.string2RollOver(epiName, line.split("\\s+")[1]);
				if (rollover == null) {
					NotificationManager.warning("Parser",
							epiName + ": Loaded border option incorrect. Border set to rectangular.");
					rollover = RollOver.NONE;
				}
				if (currEpi != null) {
					currEpi.getEpitheliumGrid().setRollOver(rollover);
				}
			}

			// Random Seed
			if (line.startsWith("SD")) {
				saTmp = line.split("\\s+");
				EnumRandomSeed rsType = EnumRandomSeed.string2RandomSeed(saTmp[1]);
				if (rsType != null && rsType.equals(EnumRandomSeed.FIXED)) {
					if (saTmp.length == 3) {
						randomSeedType = rsType;
						randomSeed = Integer.parseInt(saTmp[2]);
					} else {
						NotificationManager.warning("Parser", "File with an undefined Fixed Random Seed");
					}
				}
			}

			// Model grid
			if (line.startsWith("GM")) {
				saTmp = line.split("\\s+");
				LogicalModel m = Project.getInstance().getModel(modelKey2Name.get(saTmp[1]));
				if (currEpi == null) {
					currEpi = Project.getInstance().newEpithelium(Integer.parseInt(x), Integer.parseInt(y),
							topologyLayout, epiName, EmptyModel.getInstance().getName(), rollover, randomSeedType,
							randomSeed);
				}
				if (saTmp.length > 2) {
					currEpi.setGridWithModel(m,
							currEpi.getEpitheliumGrid().getTopology().instances2Tuples2D(saTmp[2].split(",")));
					currEpi.initPriorityClasses(m);
				}
			}
			// alpha-asynchronous value
			if (line.startsWith("AS")) {
				saTmp = line.split("\\s+");
				currEpi.getUpdateSchemeInter().setAlpha(Float.parseFloat(saTmp[1]));
			}

			// Cell Update
			if (line.startsWith("CU")) {
				String updateCells = line.substring(line.indexOf(" ") + 1);
				currEpi.getUpdateSchemeInter().setUpdateCells(UpdateCells.fromString(updateCells));
			}

			// Initial Conditions grid
			if (line.startsWith("IC")) {
				saTmp = line.split("\\s+");
				currEpi.setGridWithComponentValue(saTmp[1], Byte.parseByte(saTmp[2]),
						currEpi.getEpitheliumGrid().getTopology().instances2Tuples2D(saTmp[3].split(",")));
			}

			// Component Integration Functions
			// IT #model Node Level {Function}
			// Old Integration function identifier, where an integration function was
			// associated with a model and a component.
			if (line.startsWith("IT")) {
				saTmp = line.split("\\s+");
				byte value = Byte.parseByte(saTmp[3]);
				String nodeID = saTmp[2];
				String function = "";
				if (saTmp.length > 4) {
					int pos = line.indexOf(" ");
					int n = 4;
					while (--n > 0) {
						pos = line.indexOf(" ", pos + 1);
					}
					function = line.substring(pos).trim();
				}
				try {
					currEpi.setIntegrationFunction(nodeID, value, function);
				} catch (RuntimeException re) {
					NotificationManager.warning("Parser",
							"Integration function: " + saTmp[2] + ":" + value + " has invalid expression: " + function);
				}
			}

			// IF #model Node Level {Function}
			if (line.startsWith("IF")) {
				saTmp = line.split("\\s+");
				byte value = Byte.parseByte(saTmp[2]);
				String nodeID = saTmp[1];
				String function = "";
				if (saTmp.length > 3) {
					int pos = line.indexOf(" ");
					int n = 4;
					while (--n > 0) {
						pos = line.indexOf(" ", pos + 1);
					}
					function = line.substring(pos).trim();
				}
				try {
					currEpi.setIntegrationFunction(nodeID, value, function);
				} catch (RuntimeException re) {
					NotificationManager.warning("Parser",
							"Integration function: " + nodeID + ":" + value + " has invalid expression: " + function);
				}
			}

			// Model Priority classes
			// PR #model node1,node2:...:nodei
			if (line.startsWith("PR")) {
				saTmp = line.split("\\s+");
				LogicalModel m = Project.getInstance().getModel(modelKey2Name.get(saTmp[1]));
				currEpi.setPriorityClasses(m, saTmp[2]);
			}

			// Model All Perturbations
			// Old version -> PT #model (Perturbation) R G B cell1-celli,celln,...
			// Old NewVersion -> PT (Perturbation) R G B cell1-celli,celln,...

			if (line.startsWith("PT")) {
				saTmp = line.split("\\s+");
				String sPerturb = line.substring(line.indexOf("(") + 1, line.indexOf(")"));
				AbstractPerturbation ap = string2AbstractPerturbation(Project.getInstance().getProjectFeatures(),
						sPerturb);
				currEpi.addPerturbation(ap);

				String rest = line.substring(line.indexOf(")") + 1).trim();
				if (!rest.isEmpty()) {
					saTmp = rest.split("\\s+");
					Color c = ColorUtils.getColor(saTmp[0], saTmp[1], saTmp[2]);
					List<Tuple2D<Integer>> lTuple = null;
					if (saTmp.length > 3) {
						lTuple = currEpi.getEpitheliumGrid().getTopology().instances2Tuples2D(saTmp[3].split(","));
					}
					currEpi.applyPerturbation(ap, c, lTuple);
				}
			}
		}
		// // Ensure coherence of all epithelia
		for (Epithelium epi : Project.getInstance().getEpitheliumList()) {
			epi.getEpitheliumGrid().updateGrid();
		}

		Project.getInstance().setChanged(false);
		br.close();
		in.close();
		fstream.close();
		NotificationManager.dispatchDialogWarning(true, false);
	}

	private static AbstractPerturbation string2AbstractPerturbation(ProjectFeatures features, String sExpr) {
		String[] saExpr = sExpr.split(", ");
		List<AbstractPerturbation> lPerturb = new ArrayList<AbstractPerturbation>();

		for (String sTmp : saExpr) {
			AbstractPerturbation ap;
			String name = sTmp.split(" ")[0];
			NodeInfo node = features.getNodeInfo(name);
			String perturb = sTmp.split(" ")[1];

			if (perturb.equals("KO")) {
				ap = new FixedValuePerturbation(node, 0);
			} else if (perturb.startsWith("E")) {
				ap = new FixedValuePerturbation(node, Integer.parseInt(perturb.substring(1)));
			} else {
				String[] saTmp = perturb.split(",");
				ap = new RangePerturbation(node, Integer.parseInt(saTmp[0].replace("[", "")),
						Integer.parseInt(saTmp[1].replace("]", "")));
			}
			lPerturb.add(ap);
		}
		if (lPerturb.size() == 1) {
			return lPerturb.get(0);
		} else {
			return new MultiplePerturbation<AbstractPerturbation>(lPerturb);
		}
	}

	public static void saveConfigurations(PrintWriter w) throws IOException {
		// Grid dimensions
		// w.println("GD " + project.getX() + " " + project.getY() + " "
		// + project.getTopologyLayout());

		// SBML numerical identifiers

		OptionStore.setOption("EM", ColorUtils.getColorCode(EmptyModel.getInstance().getColor()));

		int i = 0;
		Map<LogicalModel, Integer> model2Key = new HashMap<LogicalModel, Integer>();
		for (String sbml : Project.getInstance().getModelNames()) {
			LogicalModel m = Project.getInstance().getModel(sbml);
			model2Key.put(m, i);
			Color c = Project.getInstance().getProjectFeatures().getModelColor(m);
			w.println("SB " + i + " " + sbml + " " + c.getRed() + " " + c.getGreen() + " " + c.getBlue());
			i++;
		}
		w.println();

		// Component colors
		for (String nodeID : Project.getInstance().getProjectFeatures().getNodeIDs()) {
			Color c = Project.getInstance().getProjectFeatures().getNodeColor(nodeID);
			w.println("CC " + nodeID + " " + c.getRed() + " " + c.getGreen() + " " + c.getBlue());
			OptionStore.setOption("CC " + nodeID, ColorUtils.getColorCode(c));
		}

		for (Epithelium epi : Project.getInstance().getEpitheliumList()) {
			writeEpithelium(epi, model2Key, w);
		}
	}

	private static void writeEpithelium(Epithelium epi, Map<LogicalModel, Integer> model2Key, PrintWriter w)
			throws IOException {
		w.println();

		// Epithelium name
		w.println("SN " + epi.getName());
		w.println("GD " + epi.getX() + " " + epi.getY() + " "
				+ TopologyService.getManager().getTopologyID(epi.getEpitheliumGrid().getTopology().getDescription()));

		// Rollover
		w.println("RL " + epi.getEpitheliumGrid().getTopology().getRollOver());

		// Random Seed
		EnumRandomSeed rsType = epi.getUpdateSchemeInter().getRandomSeedType();
		w.print("SD " + rsType.toString());
		if (rsType.equals(EnumRandomSeed.FIXED)) {
			w.print(" " + epi.getUpdateSchemeInter().getRandomSeed());
		}
		w.println();

		// Models in the grid
		EpitheliumGrid grid = epi.getEpitheliumGrid();
		Map<LogicalModel, List<String>> modelInst = new HashMap<LogicalModel, List<String>>();
		LogicalModel lastM = grid.getModel(0, 0);
		for (int y = 0, currI = 0, lastI = 0; y < grid.getY(); y++) {
			for (int x = 0; x < grid.getX(); x++, currI++) {
				LogicalModel currM = grid.getModel(x, y);
				if (!currM.equals(lastM)) {
					if (!modelInst.containsKey(lastM))
						modelInst.put(lastM, new ArrayList<String>());
					List<String> lTmp = modelInst.get(lastM);
					if ((currI - 1) == lastI) {
						lTmp.add("" + lastI);
					} else {
						lTmp.add(lastI + "-" + (currI - 1));
					}
					lastI = currI;
				}
				lastM = currM;
				if (x == (grid.getX() - 1) && y == (grid.getY() - 1)) {
					if (!modelInst.containsKey(lastM))
						modelInst.put(lastM, new ArrayList<String>());
					List<String> lTmp = modelInst.get(lastM);
					if (currI == lastI) {
						lTmp.add("" + lastI);
					} else {
						lTmp.add(lastI + "-" + currI);
					}
				}
			}
		}
		for (LogicalModel m : modelInst.keySet()) {
			if (model2Key.containsKey(m)) {
				w.println("GM " + model2Key.get(m) + " " + join(modelInst.get(m), ","));
			}
		}

		// Alpha asynchronism
		w.println("AS " + epi.getUpdateSchemeInter().getAlpha());
		w.println();

		// Cell Update
		w.println("CU " + epi.getUpdateSchemeInter().getUpdateCells());
		w.println();

		// Initial Conditions
		// varA value 1-2,3,4-6
		Map<LogicalModel, Map<String, Map<Byte, List<Integer>>>> valueInst = new HashMap<LogicalModel, Map<String, Map<Byte, List<Integer>>>>();
		for (int y = 0, inst = 0; y < grid.getY(); y++) {
			for (int x = 0; x < grid.getX(); x++, inst++) {
				LogicalModel currM = grid.getModel(x, y);
				if (!valueInst.containsKey(currM))
					valueInst.put(currM, new HashMap<String, Map<Byte, List<Integer>>>());

				List<NodeInfo> nodeOrder = currM.getComponents();
				byte[] currState = grid.getCellState(x, y);
				for (int n = 0; n < nodeOrder.size(); n++) {
					String nodeID = nodeOrder.get(n).getNodeID();
					if (!valueInst.get(currM).containsKey(nodeID))
						valueInst.get(currM).put(nodeID, new HashMap<Byte, List<Integer>>());
					byte value = currState[n];
					if (!valueInst.get(currM).get(nodeID).containsKey(value))
						valueInst.get(currM).get(nodeID).put(value, new ArrayList<Integer>());

					List<Integer> iTmp = valueInst.get(currM).get(nodeID).get(value);
					iTmp.add(inst);
					valueInst.get(currM).get(nodeID).put(value, iTmp);
				}
			}
		}
		for (LogicalModel m : valueInst.keySet()) {
			for (String nodeID : valueInst.get(m).keySet()) {
				for (byte value : valueInst.get(m).get(nodeID).keySet()) {
					List<String> sInsts = compactIntegerSequences(valueInst.get(m).get(nodeID).get(value));
					if (!sInsts.isEmpty()) {
						w.println("IC " + nodeID + " " + value + " " + join(sInsts, ","));
					}
				}
			}
		}
		w.println();

		// Component Integration Functions
		// IT #model Node Level {Function}
		// for (NodeInfo node : epi.getIntegrationNodes()) {
		// ComponentIntegrationFunctions cif =
		// epi.getIntegrationFunctionsForComponent(node);
		// List<String> lFunctions = cif.getFunctions();
		// for (int i = 0; i < lFunctions.size(); i++) {
		// int modelIndex = model2Key.get(node.getModel());
		// w.println("IT " + modelIndex + " " + cp.getNodeInfo().getNodeID() + " " + (i
		// + 1) + " "
		// + lFunctions.get(i));
		// }
		// }
		// w.println();

		// IF Node Level {Function}
		for (NodeInfo node : epi.getIntegrationNodes()) {
			ComponentIntegrationFunctions cif = epi.getIntegrationFunctionsForComponent(node);
			List<String> lFunctions = cif.getFunctions();
			for (int i = 0; i < lFunctions.size(); i++) {
				w.println("IF " + " " + node.getNodeID() + " " + (i + 1) + " " + lFunctions.get(i));
			}
		}
		w.println();

		// Model Priority classes
		// PR #model node1,node2:...:nodei
		for (LogicalModel m : model2Key.keySet()) {
			if (epi.hasModel(m)) {
				ModelPriorityClasses mpc = epi.getPriorityClasses(m);
				String sPCs = "";
				for (int idxPC = 0; idxPC < mpc.size(); idxPC++) {
					if (!sPCs.isEmpty())
						sPCs += ":";
					List<String> pcVars = mpc.getClassVars(idxPC).get(0);
					sPCs += join(pcVars, ",");
				}
				w.println("PR " + model2Key.get(m) + " " + sPCs);
			}
			w.println();
		}

		// Model All Perturbations
		// old -> PT #model (Perturbation) R G B cell1-celli,celln,...
		// new -> PT (Perturbation) R G B cell1-celli,celln,...
		Map<AbstractPerturbation, List<Integer>> apInst = new HashMap<AbstractPerturbation, List<Integer>>();
		for (int y = 0, currI = 0; y < grid.getY(); y++) {
			for (int x = 0; x < grid.getX(); x++, currI++) {
				AbstractPerturbation currAP = grid.getPerturbation(x, y);
				if (currAP == null) {
					continue;
				} else {
					if (!apInst.containsKey(currAP)) {
						apInst.put(currAP, new ArrayList<Integer>());
					}
					apInst.get(currAP).add(currI);
				}
			}
		}
		w.println();
		for (AbstractPerturbation ap : epi.getEpitheliumPerturbations().getAllCreatedPerturbations()) {
			w.print("PT " + "(" + ap + ")");
			Color c = epi.getEpitheliumPerturbations().getPerturbationColor(ap);
			if (c != null) {
				w.print(" " + c.getRed() + " " + c.getGreen() + " " + c.getBlue());
				if (apInst.containsKey(ap)) {
					w.print(" " + join(compactIntegerSequences(apInst.get(ap)), ","));
				}
			}
			w.println();

		}

		w.println("\n\n");
		// EpitheliumCell Connections
	}

	private static List<String> compactIntegerSequences(List<Integer> iInsts) {
		List<String> sInsts = new ArrayList<String>();
		if (iInsts.size() == 1) {
			sInsts.add(iInsts.get(0).toString());
			return sInsts;
		}
		for (int currI = 1, lastI = 0; currI < iInsts.size(); currI++) {
			if ((iInsts.get(currI - 1) + 1) == iInsts.get(currI)) {
				if ((currI + 1) == iInsts.size()) { // It's at the last position
					if ((iInsts.get(currI - 1) + 1) == iInsts.get(currI))
						sInsts.add(iInsts.get(lastI) + "-" + iInsts.get(currI));
					else {
						sInsts.add("" + iInsts.get(currI));
					}
				}
				continue;
			}
			if ((currI - 1) == lastI)
				sInsts.add("" + iInsts.get(lastI));
			else
				sInsts.add(iInsts.get(lastI) + "-" + iInsts.get(currI - 1));
			if ((currI + 1) == iInsts.size()) { // It's at the last position
				if ((iInsts.get(currI - 1) + 1) == iInsts.get(currI))
					sInsts.add(iInsts.get(lastI) + "-" + iInsts.get(currI));
				else {
					sInsts.add("" + iInsts.get(currI));
				}
			}
			lastI = currI;
		}
		return sInsts;
	}

	private static String join(List<String> list, String sep) {
		String s = "";
		for (int i = 0; i < list.size(); i++) {
			if (i > 0)
				s += sep;
			s += list.get(i);
		}
		return s;
	}
}
