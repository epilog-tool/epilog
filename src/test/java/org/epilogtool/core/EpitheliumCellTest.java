package org.epilogtool.core;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.colomoto.biolqm.LogicalModel;
import org.colomoto.biolqm.NodeInfo;
import org.colomoto.biolqm.modifier.perturbation.AbstractPerturbation;
import org.colomoto.biolqm.modifier.perturbation.RangePerturbation;
import org.epilogtool.TestHelper;
import org.epilogtool.common.EnumRandomSeed;
import org.epilogtool.common.Txt;
import org.epilogtool.io.FileIO;
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

	
	//Test if an sbml is properly loaded
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
	
	
	//Test if a project (Peps file) is properly loaded
	@Test
	public void loadProjectTest() throws IOException {
		
		File filePeps = TestHelper.getTestResource("testProjects", filePepsName);
		File noConfigPepsFile = TestHelper.getTestResource("testProjects", noConfigPepsName);
		File noSBMLPepsFile = TestHelper.getTestResource("testProjects", noSBMLPepsName);
		
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
	

	}

	//Tests if rollOver is being identified
	//ColorModels EP0 = Torus
	//ColorModels EP1 = Rectangular
	@Test
	public void Ep0Test() throws IOException {
		
		//RollOver
		assertEquals(Project.getInstance().getEpitheliumList().get(0).getEpitheliumGrid().getTopology().getRollOver().toString(),Txt.get("s_Rollover_B"));
		
		//Topology
		assertEquals(Project.getInstance().getEpitheliumList().get(0).getEpitheliumGrid().getTopology().getDescription(),"Pointy-Odd");
		
		//EpitheliumName
		assertEquals(Project.getInstance().getEpitheliumList().get(0).getName(),"Epi_0");
		
		//Dimensions
		assertEquals(Project.getInstance().getEpitheliumList().get(0).getX(),10);
		assertEquals(Project.getInstance().getEpitheliumList().get(0).getY(),10);
		
		//Models on Epithelium -> how many models on 
		List<LogicalModel> setLogicalModel = new ArrayList<LogicalModel>();
		setLogicalModel.add(YellowRed);
		setLogicalModel.add(GreenRed);
		
		List<LogicalModel> setLogicalModelFile = new ArrayList<LogicalModel>();
		for (LogicalModel m : Project.getInstance().getEpitheliumList().get(0).getEpitheliumGrid().getModelSet()) {
			Project.getInstance().getProjectFeatures().getModelName(m);
			if (Project.getInstance().getProjectFeatures().getModelName(m).equals(YellowRedName)| Project.getInstance().getProjectFeatures().getModelName(m).equals(GreenRedName)) {
				setLogicalModelFile.add(m);
			}
		}
		assertEquals(setLogicalModelFile.size(), setLogicalModel.size());
		
		
		//InputDefinitions -> integration functions
		
	}
	
	@Test
	public void integrationFunctions() {
		
	Set<NodeInfo> integrationInputs = new HashSet<NodeInfo>();	
	
	integrationInputs.add(YellowRed.getComponent("Yellow"));
	integrationInputs.add(GreenRed.getComponent("Green"));
	
		//Test if the integration inputs are correct
		assertEquals(Project.getInstance().getEpitheliumList().get(0).getIntegrationNodes().toString(),integrationInputs.toString());
		
		
		//Test if the integration functions are correct
		Set<String> strIntegrationFunctions_ctr = new HashSet<String>();
		strIntegrationFunctions_ctr.add("{Red [1]}");
		strIntegrationFunctions_ctr.add("{Red [0:20]}");
		
		Set<String> strIntegrationFunctions = new HashSet<String>();
		for (NodeInfo nodeInt: integrationInputs) {
//			System.out.println(nodeInt);
			
			NodeInfo node = Project.getInstance().getEpitheliumList().get(0).getComponentUsed(nodeInt.getNodeID());
			
			ComponentIntegrationFunctions cif = Project.getInstance().getEpitheliumList().get(0).getIntegrationFunctions().getAllIntegrationFunctions().get(node);
//			System.out.println(Project.getInstance().getEpitheliumList().get(0).getIntegrationFunctions().getAllIntegrationFunctions().get(node));
			for (String strIntFunction: cif.getFunctions()) {
				strIntegrationFunctions.add(strIntFunction);
			}
		}
		assertEquals(strIntegrationFunctions,strIntegrationFunctions_ctr);
	}
	
	
	@Test
	public void initialConditions() {
		//Tests 1) if the initial condition is properly read; 2) if the percentage is properly measured
		
		byte b ;
		
		b = Project.getInstance().getEpitheliumList().get(0).getEpitheliumGrid().getCellValue(0, 0, "Red");
		assertEquals(b,(byte) 0);
		
		b = Project.getInstance().getEpitheliumList().get(0).getEpitheliumGrid().getCellValue(9, 9, "Red");
		assertEquals(b,(byte) 1);
		
		String strPercentage = Project.getInstance().getEpitheliumList().get(0).getEpitheliumGrid().getPercentage("Red");
		assertEquals(strPercentage, "(1 : 59%)");
	}
	
	@Test
	public void perturbations() {
		//Tests 1) if the perturbations are properly defined

		assertEquals(Project.getInstance().getEpitheliumList().get(0).getEpitheliumGrid().getPerturbation(4,5).getStringRepresentation(), "Red%0");
		assertEquals(Project.getInstance().getEpitheliumList().get(0).getEpitheliumGrid().getPerturbation(5,4).getStringRepresentation(), "Red%1");
		assertEquals(Project.getInstance().getEpitheliumList().get(0).getEpitheliumGrid().getPerturbation(0,0),null);
		
		//TODO: get a model with more than one value to check the range
	}
	
	@Test
	public void cellularModelUpdate() {
		LogicalModel m = Project.getInstance().getProjectFeatures().getModel(YellowRedName);
		assertEquals(Project.getInstance().getEpitheliumList().get(0).getPriorityClasses(m).getClassVars(0).toString(),"[Yellow, Red]");
		
		//TODO: get a model with more than one component
	}
	
	
	@Test
	public void epitheliumModelUpdate() {
		
		EnumRandomSeed rsType = Project.getInstance().getEpitheliumList().get(0).getUpdateSchemeInter().getRandomSeedType();
		
		//alpha
		assertEquals(Project.getInstance().getEpitheliumList().get(0).getUpdateSchemeInter().getAlpha(), (float) 0,96);
		
		//Random seed
		assertThat(Project.getInstance().getEpitheliumList().get(0).getUpdateSchemeInter().getRandomSeed(),not(rsType.equals(EnumRandomSeed.FIXED)));
	
		//Updated cells
		assertEquals(Project.getInstance().getEpitheliumList().get(0).getUpdateSchemeInter().getUpdateCells(),(UpdateCells.fromString("Only updatable cells")));
	}
	
	@Test
	public void simulation() {
		
		//TODO
		
		
		//verify nextStep
		
		//verify terminal cyle
		
		//verify stable pattern
		
		//verify perturb model
		
	}
	
	@Test
	public void neighbours() {
		
		//TODO
		
		
		//verify neighbours Number
		
		//verify neighbours for a given cell (2)
	
	}
	
	
	@Test
	public void cellularModelOnCell() {
		
		 LogicalModel m = Project.getInstance().getEpitheliumList().get(0).getEpitheliumGrid().getModel(0, 0);
		 assertEquals(Project.getInstance().getProjectFeatures().getModelName(m),YellowRedName);
		 
		 m = Project.getInstance().getEpitheliumList().get(0).getEpitheliumGrid().getModel(6, 0);
		 assertEquals(Project.getInstance().getProjectFeatures().getModelName(m),GreenRedName);
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

	@Test
	public void hasNodeTest() {
		assertEquals(epicell.getNodeIndex("G0"), 0);
		assertEquals(epicell.getNodeIndex("XPTO"), -1);
	}

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
