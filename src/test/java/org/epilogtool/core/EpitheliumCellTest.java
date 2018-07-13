package org.epilogtool.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.colomoto.biolqm.LogicalModel;
import org.colomoto.biolqm.NodeInfo;
import org.colomoto.biolqm.modifier.perturbation.AbstractPerturbation;
import org.colomoto.biolqm.modifier.perturbation.RangePerturbation;
import org.epilogtool.TestHelper;
import org.epilogtool.common.EnumRandomSeed;
import org.epilogtool.common.Tuple2D;
import org.epilogtool.common.Txt;
import org.epilogtool.io.FileIO;
import org.epilogtool.notification.NotificationManager;
import org.epilogtool.project.Project;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class EpitheliumCellTest {
	private final static String YellowRedName = "YellowRed.sbml";
	private final static String GreenRedName = "GreenRed.sbml";
	private final static String GreenBlueName = "GreenBlue.sbml";

	private final static String filePepsName = "ColorModels.peps";
	private final static String noConfigPepsName = "NoConfig.peps";
	private final static String noSBMLPepsName = "NoSBML.peps";

	private static EpitheliumCell epicell;
	private static LogicalModel YellowRed;
	private static LogicalModel GreenRed;

	private Map<Tuple2D<Integer>, Map<Boolean, Set<Tuple2D<Integer>>>> relativeNeighboursCache = new HashMap<Tuple2D<Integer>, Map<Boolean, Set<Tuple2D<Integer>>>>();

	// Test if an sbml is properly loaded
	@BeforeClass
	public static void loadModelBeforeTests() throws IOException {

		File fYellowRed = TestHelper.getTestResource("testModels", YellowRedName);
		File fGreenRed = TestHelper.getTestResource("testModels", GreenRedName);

		YellowRed = FileIO.loadSBMLModel(fYellowRed);
		GreenRed = FileIO.loadSBMLModel(fGreenRed);

	}

	@Before
	public void loadEpiCellBeforeEachTest() {
		epicell = new EpitheliumCell(YellowRed);
	}

	// Test if a project (Peps file) is properly loaded
	@Test
	public void loadProjectTest() throws IOException {

		File filePeps = TestHelper.getTestResource("testProjects", filePepsName);
		File noConfigPepsFile = TestHelper.getTestResource("testProjects", noConfigPepsName);
		File noSBMLPepsFile = TestHelper.getTestResource("testProjects", noSBMLPepsName);

		NotificationManager.setGUI(false);
		try {
			assertFalse(FileIO.loadPEPS(noConfigPepsFile.getAbsolutePath()));
		} catch (Exception e) {
			fail("");
		}

		try {
			assertTrue(FileIO.loadPEPS(filePeps.getAbsolutePath()));
		} catch (Exception e) {
			fail("");
		}
		NotificationManager.setGUI(true);
	}

	// Tests if rollOver is being identified
	// ColorModels EP0 = Torus
	// ColorModels EP1 = Rectangular
	@Test
	public void Ep0Test() throws IOException {

		// RollOver
		assertEquals(Project.getInstance().getEpitheliumList().get(0).getEpitheliumGrid().getTopology().getRollOver()
				.toString(), Txt.get("s_Rollover_B"));

		// Topology
		assertEquals(
				Project.getInstance().getEpitheliumList().get(0).getEpitheliumGrid().getTopology().getDescription(),
				"Pointy-Odd");

		// EpitheliumName
		assertEquals(Project.getInstance().getEpitheliumList().get(0).getName(), "Epi_0");

		// Dimensions
		assertEquals(Project.getInstance().getEpitheliumList().get(0).getX(), 10);
		assertEquals(Project.getInstance().getEpitheliumList().get(0).getY(), 10);

		// Models on Epithelium -> how many models on
		List<LogicalModel> setLogicalModel = new ArrayList<LogicalModel>();
		setLogicalModel.add(YellowRed);
		setLogicalModel.add(GreenRed);

		List<LogicalModel> setLogicalModelFile = new ArrayList<LogicalModel>();
		for (LogicalModel m : Project.getInstance().getEpitheliumList().get(0).getEpitheliumGrid().getModelSet()) {
			Project.getInstance().getProjectFeatures().getModelName(m);
			if (Project.getInstance().getProjectFeatures().getModelName(m).equals(YellowRedName)
					| Project.getInstance().getProjectFeatures().getModelName(m).equals(GreenRedName)) {
				setLogicalModelFile.add(m);
			}
		}
		assertEquals(setLogicalModelFile.size(), setLogicalModel.size());

		// InputDefinitions -> integration functions

	}

	@Test
	public void integrationFunctions() {

		Set<NodeInfo> integrationInputs = new HashSet<NodeInfo>();

		integrationInputs.add(YellowRed.getComponent("Yellow"));
		integrationInputs.add(GreenRed.getComponent("Green"));

		String strIntFunctions = "[Yellow, Green]";
		String strIntFunctions_2 = "[Green, Yellow]";
		boolean bFlag = true;
		
		// Test if the integration inputs are correct
		if ((Project.getInstance().getEpitheliumList().get(0).getIntegrationNodes().toString().equals(strIntFunctions) || 
				(Project.getInstance().getEpitheliumList().get(0).getIntegrationNodes().toString().equals(strIntFunctions_2)))){
			assertTrue(bFlag);
		}

		// Test if the integration functions are correct
		Set<String> strIntegrationFunctions_ctr = new HashSet<String>();
		strIntegrationFunctions_ctr.add("{Red [1]}");
		strIntegrationFunctions_ctr.add("{Red [0:20]}");

		Set<String> strIntegrationFunctions = new HashSet<String>();
		for (NodeInfo nodeInt : integrationInputs) {
			// System.out.println(nodeInt);

			NodeInfo node = Project.getInstance().getEpitheliumList().get(0).getComponentUsed(nodeInt.getNodeID());

			ComponentIntegrationFunctions cif = Project.getInstance().getEpitheliumList().get(0)
					.getIntegrationFunctions().getAllIntegrationFunctions().get(node);
			// System.out.println(Project.getInstance().getEpitheliumList().get(0).getIntegrationFunctions().getAllIntegrationFunctions().get(node));
			for (String strIntFunction : cif.getFunctions()) {
				strIntegrationFunctions.add(strIntFunction);
			}
		}
		assertEquals(strIntegrationFunctions, strIntegrationFunctions_ctr);
	}

	@Test
	public void initialConditions() {
		// Tests 1) if the initial condition is properly read; 2) if the percentage is
		// properly measured

		byte b;

		b = Project.getInstance().getEpitheliumList().get(0).getEpitheliumGrid().getCellValue(0, 0, "Red");
		assertEquals(b, (byte) 0);

		b = Project.getInstance().getEpitheliumList().get(0).getEpitheliumGrid().getCellValue(9, 9, "Red");
		assertEquals(b, (byte) 1);

		String strPercentage = Project.getInstance().getEpitheliumList().get(0).getEpitheliumGrid()
				.getPercentage("Red");
		assertEquals(strPercentage, "(1 : 59%)");
	}

	@Test
	public void perturbations() {
		// Tests 1) if the perturbations are properly defined

		assertEquals(Project.getInstance().getEpitheliumList().get(0).getEpitheliumGrid().getPerturbation(4, 5)
				.getStringRepresentation(), "Red%0");
		assertEquals(Project.getInstance().getEpitheliumList().get(0).getEpitheliumGrid().getPerturbation(5, 4)
				.getStringRepresentation(), "Red%1");
		assertEquals(Project.getInstance().getEpitheliumList().get(0).getEpitheliumGrid().getPerturbation(0, 0), null);

		// TODO: get a model with more than one value to check the range
	}

	@Test
	public void cellularModelUpdate() {
		LogicalModel m = Project.getInstance().getProjectFeatures().getModel(YellowRedName);
		assertEquals(Project.getInstance().getEpitheliumList().get(0).getPriorityClasses(m).getClassVars(0).toString(),
				"[Yellow, Red]");

		// TODO: get a model with more than one component
	}

	@Test
	public void epitheliumModelUpdate() {

		EnumRandomSeed rsType = Project.getInstance().getEpitheliumList().get(0).getUpdateSchemeInter()
				.getRandomSeedType();

		// alpha
		assertEquals(Project.getInstance().getEpitheliumList().get(0).getUpdateSchemeInter().getAlpha(), (float) 0, 96);

		// Random seed
		// assertThat(Project.getInstance().getEpitheliumList().get(0).getUpdateSchemeInter().getRandomSeed(),not(rsType.equals(EnumRandomSeed.FIXED)));

		// Updated cells
		assertEquals(Project.getInstance().getEpitheliumList().get(0).getUpdateSchemeInter().getUpdateCells(),
				(UpdateCells.fromString("Only updatable cells")));
	}

	@Test
	public void simulation() {

		// TODO
		// verify nextStep - Random Seed. everythont is diferentt
		// verify terminal cyle
		// verify stable pattern
		// verify perturb model

	}

	public void neighbours(int index, List<Integer> neighboursNumber, int x, int y, int max) {

		// int minSign = 1;
		for (int maxSign = 0; maxSign < max; maxSign++) {

			// verify cumulative distances
			int minSign = 1;
			Tuple2D<Integer> rangePair = new Tuple2D<Integer>(minSign, maxSign);
			Tuple2D<Integer> rangeList_aux = new Tuple2D<Integer>(0, (minSign - 1 > 0) ? minSign - 1 : 0);

			Set<Tuple2D<Integer>> posNeighbours = Project.getInstance().getEpitheliumList().get(index)
					.getEpitheliumGrid()
					.getPositionNeighbours(this.relativeNeighboursCache, rangeList_aux, rangePair, minSign, x, y);

			int number = 0;
			for (int ind = 0; ind <= maxSign; ind++) {
				number = number + neighboursNumber.get(ind);
			}

			assertEquals(posNeighbours.size(), number);

			// verify exact distances
			// must increase the value of minSig to have exactly the same number of
			// neighbours

			minSign = maxSign;
			rangePair = new Tuple2D<Integer>(minSign, maxSign);
			rangeList_aux = new Tuple2D<Integer>(0, (minSign - 1 > 0) ? minSign - 1 : 0);
			posNeighbours = Project.getInstance().getEpitheliumList().get(index).getEpitheliumGrid()
					.getPositionNeighbours(this.relativeNeighboursCache, rangeList_aux, rangePair, minSign, x, y);

			// System.out.println(posNeighbours);
			number = neighboursNumber.get(maxSign);
			if (minSign == 0)
				number = 1;
			assertEquals(posNeighbours.size(), number);
		}

	}

	public void neighbourRange(int index, int minSign, int maxSign, int x, int y, int expectedNumber) {
		
		Tuple2D<Integer> rangePair = new Tuple2D<Integer>(minSign, maxSign);
		Tuple2D<Integer> rangeList_aux = new Tuple2D<Integer>(0, (minSign - 1 > 0) ? minSign - 1 : 0);
		
		Set<Tuple2D<Integer>> posNeighbours = Project.getInstance().getEpitheliumList().get(index)
				.getEpitheliumGrid()
				.getPositionNeighbours(this.relativeNeighboursCache, rangeList_aux, rangePair, minSign, x, y);
		
		assertEquals(posNeighbours.size(), expectedNumber);
		
	}
	
	public boolean neighbourPresent(int index, int minSign, int maxSign, int x, int y, int Nx, int Ny) {
		Tuple2D<Integer> rangePair = new Tuple2D<Integer>(minSign, maxSign);
		Tuple2D<Integer> rangeList_aux = new Tuple2D<Integer>(0, (minSign - 1 > 0) ? minSign - 1 : 0);
		
		Set<Tuple2D<Integer>> posNeighbours = Project.getInstance().getEpitheliumList().get(index)
				.getEpitheliumGrid()
				.getPositionNeighbours(this.relativeNeighboursCache, rangeList_aux, rangePair, minSign, x, y);
		
		return posNeighbours.contains(new Tuple2D<Integer>(Nx, Ny));
	}
	
	
	@Test
	public void neighbours() {

		// ***************PointyOdd
		neighbours(0, Arrays.asList(0, 6, 12, 18, 24, 23, 12, 4), 0, 0, 8); // Torus
		neighbourRange(0, 0,-1, 0, 0,100); // Torus range[0:]
		neighbourRange(0, 1,-1, 0, 0,99); // Torus range[1:]
		neighbourRange(0, 1,40, 0, 0,99); // Torus range[1:40]
	
		
		neighbours(1, Arrays.asList(0, 2, 4, 5, 7, 8, 10, 11, 13, 14, 9, 7, 5, 3, 1), 0, 0, 14); // rectangular
		neighbours(2, Arrays.asList(0, 4, 7, 10, 13, 15, 14, 13, 12, 11), 0, 0, 9); // Horizontal
		neighbours(3, Arrays.asList(0, 3, 7, 9, 13, 12, 10, 10, 10, 10, 9, 5, 1), 0, 0, 10); // vertical

		// ***************PointyEven
		neighbours(4, Arrays.asList(0, 6, 12, 18, 24, 23, 12, 4), 0, 0, 8); // Torus
		neighbours(5, Arrays.asList(0, 2, 4, 5, 7, 8, 10, 11, 13, 14, 9, 7, 5, 3, 1), 0, 0, 14); // rectangular
		neighbours(6, Arrays.asList(0, 4, 7, 10, 13, 15, 14, 13, 12, 11), 0, 0, 9); // Horizontal
		neighbours(7, Arrays.asList(0, 3, 7, 9, 13, 12, 10, 10, 10, 10, 9, 5, 1), 0, 0, 10); // vertical

		// ***************FlatOdd
		neighbours(8, Arrays.asList(0, 6, 12, 18, 24, 23, 12, 4), 0, 0, 8); // Torus
		neighbours(9, Arrays.asList(0, 2, 4, 5, 7, 8, 10, 11, 13, 14, 9, 7, 5, 3, 1), 0, 0, 14); // rectangular
		neighbours(10, Arrays.asList(0, 4, 7, 10, 13, 15, 14, 13, 12, 11), 0, 0, 9); // Horizontal
		neighbours(11, Arrays.asList(0, 3, 7, 9, 13, 12, 10, 10, 10, 10, 9, 5, 1), 0, 0, 10); // vertical

		// ***************FlatEven
		neighbours(12, Arrays.asList(0, 6, 12, 18, 24, 23, 12, 4), 0, 0, 8); // Torus
		neighbours(13, Arrays.asList(0, 2, 4, 5, 7, 8, 10, 11, 13, 14, 9, 7, 5, 3, 1), 0, 0, 14); // rectangular
		neighbours(14, Arrays.asList(0, 4, 7, 10, 13, 15, 14, 13, 12, 11), 0, 0, 9); // Horizontal
		neighbours(15, Arrays.asList(0, 3, 7, 9, 13, 12, 10, 10, 10, 10, 9, 5, 1), 0, 0, 10); // vertical

		// TODO: verify neighbours for intermediate values
		assertFalse(neighbourPresent(0, 2, 4, 0, 0,0,0)); 
		assertTrue(neighbourPresent(0, 2, 4, 0, 0,0,2)); 

	}

	@Test
	public void cellularModelOnCell() {

		LogicalModel m = Project.getInstance().getEpitheliumList().get(0).getEpitheliumGrid().getModel(0, 0);
		assertEquals(Project.getInstance().getProjectFeatures().getModelName(m), YellowRedName);

		m = Project.getInstance().getEpitheliumList().get(0).getEpitheliumGrid().getModel(6, 0);
		assertEquals(Project.getInstance().getProjectFeatures().getModelName(m), GreenRedName);
	}

	@Test
	public void cleanSlateTest() {
		assertNull(epicell.getPerturbation());
		byte[] state = epicell.getState();
		assertNotNull(state);
		for (int i = 0; i < YellowRed.getComponents().size(); i++) {
			assertEquals(0, state[i]);
		}
		assertNotNull(epicell.getModel());
		assertEquals(YellowRed, epicell.getModel());
	}

	@Test
	public void perturbationTest() {
		AbstractPerturbation rp = new RangePerturbation(YellowRed.getComponents().get(0), 0, 1);
		epicell.setPerturbation(rp);
		assertEquals(rp, epicell.getPerturbation());
	}

	// @Test
	// public void hasNodeTest() {
	// assertEquals(epicell.getNodeIndex("Green"), 0);
	//// assertEquals(epicell.getNodeIndex("Y"), -1);
	// }

	@Test
	public void cloneTest() {
		EpitheliumCell clone = epicell.clone();
		assertEquals(clone.getModel(), epicell.getModel());
		assertEquals(clone.getPerturbation(), epicell.getPerturbation());
		assertNotNull(clone.getState());
		for (int i = 0; i < YellowRed.getComponents().size(); i++) {
			assertEquals(clone.getState()[i], epicell.getState()[i]);
		}
	}
}
