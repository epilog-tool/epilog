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
import java.util.Arrays;
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
import org.epilogtool.OptionStore;
import org.epilogtool.common.EnumRandomSeed;
import org.epilogtool.common.RandCentral;
import org.epilogtool.common.Tuple2D;
import org.epilogtool.common.Txt;
import org.epilogtool.core.ComponentIntegrationFunctions;
import org.epilogtool.core.Epithelium;
import org.epilogtool.core.EpitheliumGrid;
import org.epilogtool.core.ModelCellularEvent;
import org.epilogtool.core.ModelPriorityClasses;
import org.epilogtool.core.UpdateCells;
import org.epilogtool.core.cell.AbstractCell;
import org.epilogtool.core.cell.CellFactory;
import org.epilogtool.core.cell.LivingCell;
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
//				System.out.println(line);
				saTmp = line.split("\\s+");
				
				if (saTmp[1].equals("-1")){
					Color modelColor = ColorUtils.getColor(saTmp[3], saTmp[4], saTmp[5]);
					Project.getInstance().getProjectFeatures().setAbstCellColor(Txt.get("s_INVALID_CELL"), modelColor);
				}
				else if (saTmp[1].equals("-2")){
					Color modelColor = ColorUtils.getColor(saTmp[3], saTmp[4], saTmp[5]);
					Project.getInstance().getProjectFeatures().setAbstCellColor(Txt.get("s_DEAD_CELL"), modelColor);
				}
				else if (saTmp[1].equals("-3")){
					Color modelColor = ColorUtils.getColor(saTmp[3], saTmp[4], saTmp[5]);
					Project.getInstance().getProjectFeatures().setAbstCellColor(Txt.get("s_EMPTY_CELL"), modelColor);
				}
				else {

				File fSBML = new File(fConfig.getParent() + "/" + saTmp[2]);
				try {
					Project.getInstance().loadModel(fSBML.getName(), FileIO.loadSBMLModel(fSBML));
					
				} catch (IOException e) {
					throw new IOException(Txt.get("s_SBML_failed_load"));
				}
				modelKey2Name.put(saTmp[1], saTmp[2]);
				Color modelColor = ColorUtils.getColor(saTmp[3], saTmp[4], saTmp[5]);
				Project.getInstance().getProjectFeatures().setModelColor(saTmp[2], modelColor);
				}
				
			}

			if (line.startsWith("CC")) {
//				System.out.println(line);
				saTmp = line.split("\\s+");
				Color componentColor = ColorUtils.getColor(saTmp[2], saTmp[3], saTmp[4]);
				Project.getInstance().getProjectFeatures().setNodeColor(saTmp[1], componentColor);
			}

			// Epithelium name
			if (line.startsWith("SN")) {
//				System.out.println(line);
				epiName = line.split("\\s+")[1];
				currEpi = null;
				rollover = RollOver.NONE;
				randomSeed = RandCentral.getInstance().nextInt();
				randomSeedType = EnumRandomSeed.RANDOM;
			}

			if (line.startsWith("GD")) {
//				System.out.println(line);
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
//				System.out.println(line);
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
//				System.out.println(line);
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
				
//				System.out.println(line);
				
				saTmp = line.split("\\s+");
				
				// The model grid is now created. The epithelium is suposed to be null at this stage, and a new epithelium 
				// is created with all cells as empty cells.	
				
				if (currEpi == null) {
		
					currEpi = Project.getInstance().newEpithelium(Integer.parseInt(x), Integer.parseInt(y),
							topologyLayout, epiName, CellFactory.newEmptyCell(new Tuple2D<Integer>(0,0)), rollover, randomSeedType,
							randomSeed);
				}
				
				if (saTmp.length > 2) {
					
					AbstractCell c;

					if (saTmp[1].equals("-1")) {
						c = CellFactory.newInvalidCell(new Tuple2D<Integer>(0,0));
					}
					else if (saTmp[1].equals("-2")) {
						c = CellFactory.newDeadCell(new Tuple2D<Integer>(0,0));
					}
					else if (saTmp[1].equals("-3")) {
						c = CellFactory.newEmptyCell(new Tuple2D<Integer>(0,0));
					}
					else{
						LogicalModel m = Project.getInstance().getModel(modelKey2Name.get(saTmp[1]));
						c = CellFactory.newLivingCell(new Tuple2D<Integer>(0,0),m);
						currEpi.initPriorityClasses(m);
					}
				
					// At least one living cell is added to the grid at this stage.
					currEpi.setGridWithCell(c,
							currEpi.getEpitheliumGrid().getTopology().instances2Tuples2D(saTmp[2].split(",")));
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
//				System.out.println(line);
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
//				System.out.println(line);
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
//				System.out.println(line);
				saTmp = line.split("\\s+");
				LogicalModel m = Project.getInstance().getModel(modelKey2Name.get(saTmp[1]));
				currEpi.setPriorityClasses(m, saTmp[2]);
			}

			// Model All Perturbations
			// Old version -> PT #model (Perturbation) R G B cell1-celli,celln,...
			// Old NewVersion -> PT (Perturbation) R G B cell1-celli,celln,...
			
			if (line.startsWith("PT")) {
//				System.out.println(line);
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
			
			if (line.startsWith("ME")) {
				saTmp = line.split(";");
				
				String eventOrder = saTmp[0].replace("ME [","");
				String newCellState = saTmp[1];
				String cellDeathOption = saTmp[2].replace("]","");
				
				currEpi.getEpitheliumEvents().setEventOrder(eventOrder);
				currEpi.getEpitheliumEvents().setDivisionOption(newCellState);
				currEpi.getEpitheliumEvents().setDeathOption(cellDeathOption);
				
			}
			
			if (line.startsWith("CE")) {
				saTmp = line.split("\\s+");
				LogicalModel m = Project.getInstance().getModel(modelKey2Name.get(saTmp[1]));
				
				String s="";
				for (int index =2; index<saTmp.length;index++) {
					s = s+ " "+saTmp[index];
					
				}
				String[] sArray = s.split(";");
			
				
				currEpi.getEpitheliumEvents().setDeathTrigger(m, sArray[0].replace("[","").replace(" ",""));
				currEpi.getEpitheliumEvents().setDeathValue(m,Float.parseFloat(sArray[1]));
				currEpi.getEpitheliumEvents().setDeathPattern(m, sArray[2]);
				
				currEpi.getEpitheliumEvents().setDivisionTrigger(m, sArray[3]);
				currEpi.getEpitheliumEvents().setDivisionValue(m, Float.parseFloat(sArray[4]));
				currEpi.getEpitheliumEvents().setDivisionPattern(m, sArray[5]);
				currEpi.getEpitheliumEvents().setDivisionNewState(m, new byte[m.getComponents().size()]);
				
				currEpi.getEpitheliumEvents().setDeathAlgorithm(m, sArray[7]);
				currEpi.getEpitheliumEvents().setDivisionAlgorithm(m, sArray[8]);
				currEpi.getEpitheliumEvents().setDivisionRange(m,Integer.parseInt( sArray[9].replaceAll("]", "")));

				
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
		
		Map<String, Integer> model2Key = new HashMap<String, Integer>();

		// SBML numerical identifiers

		OptionStore.setOption(Txt.get("s_INVALID_CELL"), ColorUtils.getColorCode(Color.black));
		OptionStore.setOption(Txt.get("s_EMPTY_CELL"), ColorUtils.getColorCode(Color.gray));
		OptionStore.setOption(Txt.get("s_DEAD_CELL"), ColorUtils.getColorCode(Color.gray));

		int i= -3;
		Color c = Project.getInstance().getProjectFeatures().getAbstCellColor(Txt.get("s_EMPTY_CELL"));
		
		w.println("SB " + i + " " + "EMPTYCELL" + " " + c.getRed() + " " + c.getGreen() + " " + c.getBlue());
		model2Key.put(Txt.get("s_EMPTY_CELL"), i);
		
		i= -2;
		c = Project.getInstance().getProjectFeatures().getAbstCellColor(Txt.get("s_DEAD_CELL"));
		w.println("SB " + i + " " + "DEADCELL" + " " + c.getRed() + " " + c.getGreen() + " " + c.getBlue());
		model2Key.put(Txt.get("s_DEAD_CELL"), i);
		
		i= -1;
		c = Project.getInstance().getProjectFeatures().getAbstCellColor(Txt.get("s_INVALID_CELL"));
		w.println("SB " + i + " " + "INVALIDCELL" + " " + c.getRed() + " " + c.getGreen() + " " + c.getBlue());
		model2Key.put(Txt.get("s_INVALID_CELL"), i);
		
		i = 0;
	
		for (String sbml : Project.getInstance().getModelNames()) {
			LogicalModel m = Project.getInstance().getModel(sbml);
			model2Key.put(sbml, i);
			c = Project.getInstance().getProjectFeatures().getModelColor(m);
			w.println("SB " + i + " " + sbml + " " + c.getRed() + " " + c.getGreen() + " " + c.getBlue());
			i++;
		}
		w.println();

		// Component colors
		for (String nodeID : Project.getInstance().getProjectFeatures().getNodeIDs()) {
			c = Project.getInstance().getProjectFeatures().getNodeColor(nodeID);
			w.println("CC " + nodeID + " " + c.getRed() + " " + c.getGreen() + " " + c.getBlue());
			OptionStore.setOption("CC " + nodeID, ColorUtils.getColorCode(c));
		}

		for (Epithelium epi : Project.getInstance().getEpitheliumList()) {
			writeEpithelium(epi, model2Key, w);
		}
	}

	private static void writeEpithelium(Epithelium epi, Map<String, Integer> model2Key, PrintWriter w)
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
		Map<Integer, List<String>> indexInst = new HashMap<Integer, List<String>>();
		int lastIndex = 0;
		int currIndex;
	

		for (int y = 0, currI = 0, lastI = 0; y < grid.getY(); y++) {
			for (int x = 0; x < grid.getX(); x++, currI++) {
				if (grid.getAbstCell(x, y).isLivingCell()) {
					String modelName  = Project.getInstance().getProjectFeatures().getModelName(grid.getModel(x, y));
					currIndex = model2Key.get(modelName);
				}
				else {
					currIndex = model2Key.get(grid.getAbstCell(x,y).getName());
				}
				
				if (currIndex!=lastIndex) {
					if (!indexInst.containsKey(lastIndex))
						indexInst.put(lastIndex, new ArrayList<String>());
					List<String> lTmp = indexInst.get(lastIndex);
					if ((currI - 1) == lastI) {
						lTmp.add("" + lastI);
					} else {
						lTmp.add(lastI + "-" + (currI - 1));
					}
					lastI = currI;
				}
				lastIndex = currIndex;
				if (x == (grid.getX() - 1) && y == (grid.getY() - 1)) {
					if (!indexInst.containsKey(lastIndex))
						indexInst.put(lastIndex, new ArrayList<String>());
					List<String> lTmp = indexInst.get(lastIndex);
					if (currI == lastI) {
						lTmp.add("" + lastI);
					} else {
						lTmp.add(lastI + "-" + currI);
					}
				
			}}
		}
		for (int i : indexInst.keySet()) {
			//TODO: the replace is temporary, fix the algorithm above
				w.println("GM " + i + " " + join(indexInst.get(i), ",").replaceAll("--", "-"));
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
				if (grid.getAbstCell(x, y).isLivingCell()) {
					
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
			}}
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
		for (String name : model2Key.keySet()) {
			if (model2Key.get(name)<0)
				continue;
			LogicalModel m = Project.getInstance().getProjectFeatures().getModel(name);
			
			if (epi.hasModel(m)) {
				ModelPriorityClasses mpc = epi.getPriorityClasses(m);
				String sPCs = "";
				for (int idxPC = 0; idxPC < mpc.size(); idxPC++) {
					if (!sPCs.isEmpty())
						sPCs += ":";
					List<String> pcVars = mpc.getClassVars(idxPC);
					sPCs += join(pcVars, ",");
				}
				w.println("PR " + model2Key.get(name) + " " + sPCs);
			}
			w.println();
		}

		// Model All Perturbations
		// old -> PT #model (Perturbation) R G B cell1-celli,celln,...
		// new -> PT (Perturbation) R G B cell1-celli,celln,...
		
		Map<AbstractPerturbation, List<Integer>> apInst = new HashMap<AbstractPerturbation, List<Integer>>();
		for (int y = 0, currI = 0; y < grid.getY(); y++) {
			for (int x = 0; x < grid.getX(); x++, currI++) {
				if (grid.getAbstCell(x, y).isLivingCell()) {
				AbstractPerturbation currAP = grid.getPerturbation(x, y);
				if (currAP == null) {
					continue;
				} else {
					if (!apInst.containsKey(currAP)) {
						apInst.put(currAP, new ArrayList<Integer>());
					}
					apInst.get(currAP).add(currI);
				}}
			}
		}
		w.println();
			for (AbstractPerturbation ap : epi.getEpitheliumPerturbations().getAllCreatedPerturbations())
			{
				w.print("PT " +"(" + ap + ")");
				Color c = epi.getEpitheliumPerturbations().getPerturbationColor(ap);
				if (c != null) {
					w.print(" " + c.getRed() + " " + c.getGreen() + " " + c.getBlue());
					if (apInst.containsKey(ap)) {
						w.print(" " + join(compactIntegerSequences(apInst.get(ap)), ","));
					}
				}
				w.println();
			
		}

			
		//ModelEvents
			
			List<String> lst = new ArrayList<String>();
			
			lst.add(epi.getEpitheliumEvents().getEventOrder());
			lst.add(epi.getEpitheliumEvents().getDivisionOption());
			lst.add(epi.getEpitheliumEvents().getDeathOption());
			
//			System.out.println("ME " + "[" + join(lst,";") + "]");
			w.println("ME " + "[" + join(lst,";") + "]");
			
		//CellularEvents
		//
			
			for (String name : model2Key.keySet()) {
				if (model2Key.get(name)<0)
					continue;

			LogicalModel m = Project.getInstance().getProjectFeatures().getModel(name);

			if (epi.getEpitheliumGrid().getModelSet().contains(m)) {

			List<String> paramList = new ArrayList<String>();
			
			ModelCellularEvent mce = epi.getEpitheliumEvents().getMCE(m);
			
			paramList.add(mce.getDeathTrigger());
			paramList.add(""+mce.getDeathValue());
			paramList.add(mce.getDeathPattern());
			
			paramList.add(mce.getDivisionTrigger());
			paramList.add(""+mce.getDivisionValue());
			paramList.add(mce.getDivisionPattern());
			paramList.add(Arrays.toString(mce.getNewCellState()));
			
			paramList.add(mce.getDeathAlgorithm());
			paramList.add(mce.getDivisionAlgorithm());
			paramList.add("" + mce.getDivisionRange());
	
			w.println("CE " + model2Key.get(name) + " [" + join(paramList,";") +"]");
		}
			}
				
		// EpitheliumCell Connections
		w.println("\n\n");
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
