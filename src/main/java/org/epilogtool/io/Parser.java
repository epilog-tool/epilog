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

import org.colomoto.logicalmodel.LogicalModel;
import org.colomoto.logicalmodel.NodeInfo;
import org.colomoto.logicalmodel.perturbation.AbstractPerturbation;
import org.colomoto.logicalmodel.perturbation.FixedValuePerturbation;
import org.colomoto.logicalmodel.perturbation.MultiplePerturbation;
import org.colomoto.logicalmodel.perturbation.RangePerturbation;
import org.epilogtool.project.ComponentPair;
import org.epilogtool.project.Project;
import org.epilogtool.project.ProjectFeatures;
import org.epilogtool.OptionStore;
import org.epilogtool.common.Tuple2D;
import org.epilogtool.core.ComponentIntegrationFunctions;
import org.epilogtool.core.Epithelium;
import org.epilogtool.core.EpitheliumGrid;
import org.epilogtool.core.ModelPerturbations;
import org.epilogtool.core.ModelPriorityClasses;
import org.epilogtool.core.topology.RollOver;
import org.epilogtool.gui.color.ColorUtils;

public class Parser {

	public static Project loadConfigurations(File fConfig) throws IOException,
			InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException, ClassNotFoundException {
		FileInputStream fstream = new FileInputStream(fConfig);
		DataInputStream in = new DataInputStream(fstream);
		BufferedReader br = new BufferedReader(new InputStreamReader(in));

		Map<String, String> modelKey2Name = new HashMap<String, String>();
		Project project = new Project();
		Epithelium currEpi = null;
		RollOver rollover = null;
		
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
				project.addModel(fSBML.getName(), FileIO.loadSBMLModel(fSBML));
				modelKey2Name.put(saTmp[1], saTmp[2]);
				Color modelColor = ColorUtils.getColor(saTmp[3], saTmp[4],
						saTmp[5]);
				project.getProjectFeatures().changeModelColor(saTmp[2], modelColor);
			}
			
			if (line.startsWith("CC")){
				saTmp = line.split("\\s+");
				Color componentColor = ColorUtils.getColor(saTmp[2], saTmp[3],
						saTmp[4]);
				project.getComponentFeatures().setNodeColor(saTmp[1],  componentColor);
			}
			
			// Epithelium name
			if (line.startsWith("SN")) {
				epiName = line.split("\\s+")[1];
				currEpi = null;
				rollover = RollOver.NOROLLOVER;
			}

			if (line.startsWith("GD")) {
				saTmp = line.split("\\s+");
				//int x = Integer.parseInt(saTmp[1]);
				//int y = Integer.parseInt(saTmp[2]); 
				x = saTmp[1];
				y = saTmp[2];
				topologyLayout = saTmp[3];
			}
			// RollOver
			if (line.startsWith("RL")) {
				rollover = RollOver.string2RollOver(line.split("\\s+")[1]);
				if (currEpi != null) {
					currEpi.getEpitheliumGrid().setRollOver(rollover);
				}
			}
			// Model grid
			if (line.startsWith("GM")) {
				saTmp = line.split("\\s+");
				LogicalModel m = project.getModel(modelKey2Name.get(saTmp[1]));
				if (currEpi == null) {
					currEpi = project.newEpithelium(Integer.parseInt(x),Integer.parseInt(y),topologyLayout,epiName,
							modelKey2Name.get(saTmp[1]), rollover);
					//System.out.println(Integer.parseInt(x)+Integer.parseInt(y)+topologyLayout+epiName+modelKey2Name.get(saTmp[1]));
				}
				if (saTmp.length > 2) {
					currEpi.setGridWithModel(m,
							currEpi.getEpitheliumGrid().getTopology()
									.instances2Tuples2D(saTmp[2].split(",")));
					currEpi.initPriorityClasses(m);
					currEpi.initComponentFeatures(m);
				}
			}
			// alpha-asynchronous value
			if (line.startsWith("AS")) {
				saTmp = line.split("\\s+");
				currEpi.getUpdateSchemeInter().setAlpha(Float.parseFloat(saTmp[1]));
			}
			// Initial Conditions grid
			if (line.startsWith("IC")) {
				saTmp = line.split("\\s+");
				currEpi.setGridWithComponentValue(saTmp[1],
						Byte.parseByte(saTmp[2]),
						currEpi.getEpitheliumGrid().getTopology()
								.instances2Tuples2D(saTmp[3].split(",")));
			}
			// Component Integration Functions
			if (line.startsWith("IT")) {
				saTmp = line.split("\\s+");
				if (saTmp.length == 4) {
					String input = saTmp[1];
					String proper = saTmp[3].split("\\(")[0];
					for (LogicalModel m : project.getProjectFeatures().getModels()){
						if (project.getProjectFeatures().hasNode(input, m) & 
								project.getProjectFeatures().hasNode(proper, m)) {
							currEpi.setIntegrationFunction(saTmp[1], m, Byte.parseByte(saTmp[2]), 
									(saTmp.length > 3) ? saTmp[3] : "");
						}
					}
				}
				else{
					LogicalModel m = project.getModel(modelKey2Name.get(saTmp[1]));
				currEpi.setIntegrationFunction(saTmp[2], m, Byte.parseByte(saTmp[3]), 
						(saTmp.length > 4) ? saTmp[4] : "");
				}	
			}
			// Model Priority classes
			if (line.startsWith("PR")) {
				saTmp = line.split("\\s+");
				LogicalModel m = project.getModel(modelKey2Name.get(saTmp[1]));
				currEpi.setPriorityClasses(m, saTmp[2]);
			}
			// Model All Perturbations
			if (line.startsWith("PT")) {
				saTmp = line.split("\\s+");
				LogicalModel m = project.getModel(modelKey2Name.get(saTmp[1]));
				String sPerturb = line.substring(line.indexOf("(") + 1,
						line.indexOf(")"));
				AbstractPerturbation ap = string2AbstractPerturbation(
						currEpi.getComponentFeatures(), sPerturb, m);
				currEpi.addPerturbation(m, ap);

				String rest = line.substring(line.indexOf(")") + 1).trim();
				if (!rest.isEmpty()) {
					saTmp = rest.split("\\s+");
					Color c = ColorUtils.getColor(saTmp[0], saTmp[1], saTmp[2]);
					List<Tuple2D<Integer>> lTuple = null;
					if (saTmp.length > 3) {
						lTuple = currEpi.getEpitheliumGrid().getTopology()
								.instances2Tuples2D(saTmp[3].split(","));
					}
					currEpi.applyPerturbation(m, ap, c, lTuple);
				}
			}
			// project add currEpi
		}
//		// Ensure coherence of all epithelia
		for (Epithelium epi : project.getEpitheliumList()) {
			epi.getEpitheliumGrid().updateModelSet();
		}
		//System.out.println("Final: " + project);
		project.setChanged(false);
		br.close();
		in.close();
		fstream.close();
		return project;
	}

	private static AbstractPerturbation string2AbstractPerturbation(
			ProjectFeatures features, String sExpr, LogicalModel m) {
		String[] saExpr = sExpr.split(", ");
		List<AbstractPerturbation> lPerturb = new ArrayList<AbstractPerturbation>();

		for (String sTmp : saExpr) {
			AbstractPerturbation ap;
			String name = sTmp.split(" ")[0];
			NodeInfo node = features.getNodeInfo(name, m);
			String perturb = sTmp.split(" ")[1];

			if (perturb.equals("KO")) {
				ap = new FixedValuePerturbation(node, 0);
			} else if (perturb.startsWith("E")) {
				ap = new FixedValuePerturbation(node, Integer.parseInt(perturb
						.substring(1)));
			} else {
				String[] saTmp = perturb.split(",");
				ap = new RangePerturbation(node, Integer.parseInt(saTmp[0]
						.substring(1)), Integer.parseInt(saTmp[0].substring(0,
						-1)));
			}
			lPerturb.add(ap);
		}
		if (lPerturb.size() == 1) {
			return lPerturb.get(0);
		} else {
			return new MultiplePerturbation<AbstractPerturbation>(lPerturb);
		}
	}

	public static void saveConfigurations(Project project, PrintWriter w)
			throws IOException {
		// Grid dimensions
//		w.println("GD " + project.getX() + " " + project.getY() + " "
//				+ project.getTopologyLayout());

		// SBML numerical identifiers
		int i = 0;
		Map<LogicalModel, Integer> model2Key = new HashMap<LogicalModel, Integer>();
		for (String sbml : project.getModelNames()) {
			LogicalModel m = project.getModel(sbml);
			model2Key.put(m, i);
			Color c = project.getProjectFeatures().getModelColor(m);
			w.println("SB " + i + " " + sbml + " " + c.getRed() + " "
					+ c.getGreen() + " " + c.getBlue());
			i++;
		}
		w.println();
		
		// Component colors
		for(String nodeID : project.getComponentFeatures().getComponents()){
			Color c = project.getComponentFeatures().getNodeColor(nodeID);
			w.println("CC " + nodeID + " " + c.getRed() + " " 
					+ c.getBlue() + " " + c.getGreen());
			OptionStore.setOption("CC " + nodeID,  ColorUtils.getColorCode(c));
		}

		for (Epithelium epi : project.getEpitheliumList()) {
			writeEpithelium(epi, model2Key, w);
		}
	}

	private static void writeEpithelium(Epithelium epi,
			Map<LogicalModel, Integer> model2Key, PrintWriter w)
			throws IOException {
		w.println();

		// Epithelium name
		w.println("SN " + epi.getName());
		w.println("GD " + epi.getX() + " " + epi.getY() + " "
				+ epi.getTopologyLayout());

		// Rollover
		w.println("RL " + epi.getEpitheliumGrid().getTopology().getRollOver());

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
			w.println("GM " + model2Key.get(m) + " "
					+ join(modelInst.get(m), ","));
		}
		
		// Alpha asynchronism
		w.println("AS " + epi.getUpdateSchemeInter().getAlpha());
		w.println();

		// Initial Conditions
		// varA value 1-2,3,4-6
		Map<LogicalModel, Map<String, Map<Byte, List<Integer>>>> valueInst = new HashMap<LogicalModel, Map<String, Map<Byte, List<Integer>>>>();
		for (int y = 0, inst = 0; y < grid.getY(); y++) {
			for (int x = 0; x < grid.getX(); x++, inst++) {
				LogicalModel currM = grid.getModel(x, y);
				if (!valueInst.containsKey(currM))
					valueInst.put(currM,
							new HashMap<String, Map<Byte, List<Integer>>>());

				List<NodeInfo> nodeOrder = currM.getNodeOrder();
				byte[] currState = grid.getCellState(x, y);
				for (int n = 0; n < nodeOrder.size(); n++) {
					String nodeID = nodeOrder.get(n).getNodeID();
					if (!valueInst.get(currM).containsKey(nodeID))
						valueInst.get(currM).put(nodeID,
								new HashMap<Byte, List<Integer>>());
					byte value = currState[n];
					if (!valueInst.get(currM).get(nodeID).containsKey(value))
						valueInst.get(currM).get(nodeID)
								.put(value, new ArrayList<Integer>());

					List<Integer> iTmp = valueInst.get(currM).get(nodeID)
							.get(value);
					iTmp.add(inst);
					valueInst.get(currM).get(nodeID).put(value, iTmp);
				}
			}
		}
		for (LogicalModel m : valueInst.keySet()) {
			for (String nodeID : valueInst.get(m).keySet()) {
				for (byte value : valueInst.get(m).get(nodeID).keySet()) {
					List<String> sInsts = compactIntegerSequences(valueInst
							.get(m).get(nodeID).get(value));
					w.println("IC " + nodeID + " " + value + " "
							+ join(sInsts, ","));
				}
			}
		}
		w.println();

		// Component Integration Functions
		for (ComponentPair cp : epi.getIntegrationComponentPairs()) {
			ComponentIntegrationFunctions cif = epi
					.getIntegrationFunctionsForComponent(cp);
			List<String> lFunctions = cif.getFunctions();
			for (int i = 0; i < lFunctions.size(); i++) {
				int modelIndex = model2Key.get(cp.getModel());
				w.println("IT " + modelIndex + " " + cp.getNodeInfo().getNodeID() + " " + (i + 1) + " "
						+ lFunctions.get(i));
			}
		}
		w.println();

		// Model Priority classes
		for (LogicalModel m : model2Key.keySet()) {
			ModelPriorityClasses mpc = epi.getPriorityClasses(m);
//			if (mpc == null)
//				continue; // This should never occur
			String sPCs = "";
			for (int idxPC = 0; idxPC < mpc.size(); idxPC++) {
				if (!sPCs.isEmpty())
					sPCs += ":";
				List<String> pcVars = mpc.getClassVars(idxPC);
				sPCs += join(pcVars, ",");
			}
			w.println("PR " + model2Key.get(m) + " " + sPCs);
		}
		w.println();

		// Model All Perturbations
		Map<AbstractPerturbation, List<String>> apInst = new HashMap<AbstractPerturbation, List<String>>();
		AbstractPerturbation lastAP = grid.getPerturbation(0, 0);
		for (int y = 0, currI = 0, lastI = 0; y < grid.getY(); y++) {
			for (int x = 0; x < grid.getX(); x++, currI++) {
				AbstractPerturbation currAP = grid.getPerturbation(x, y);
				if (lastAP == null) {
					lastI = currI;
				} else if (currAP == null || !currAP.equals(lastAP)) {
					if (!apInst.containsKey(lastAP))
						apInst.put(lastAP, new ArrayList<String>());
					List<String> lTmp = apInst.get(lastAP);
					if ((currI - 1) == lastI) {
						lTmp.add("" + lastI);
					} else {
						lTmp.add(lastI + "-" + (currI - 1));
					}
					lastI = currI;
				}
				lastAP = currAP;
			}
		}
		for (LogicalModel m : model2Key.keySet()) {
			ModelPerturbations mp = epi.getModelPerturbations(m);
			if (mp == null)
				continue;
			for (AbstractPerturbation ap : mp.getAllPerturbations()) {
				w.print("PT " + model2Key.get(m) + " (" + ap + ")");
				Color c = mp.getPerturbationColor(ap);
				if (c != null) {
					w.print(" " + c.getRed() + " " + c.getGreen() + " "
							+ c.getBlue());
					if (apInst.containsKey(ap)) {
						w.print(" " + join(apInst.get(ap), ","));
					}
				}
				w.println();
			}
		}
	}

	private static List<String> compactIntegerSequences(List<Integer> iInsts) {
		List<String> sInsts = new ArrayList<String>();
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
