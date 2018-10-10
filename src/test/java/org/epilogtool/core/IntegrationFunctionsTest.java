package org.epilogtool.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.colomoto.biolqm.LogicalModel;
import org.colomoto.biolqm.NodeInfo;
import org.epilogtool.FileUtils;
import org.epilogtool.io.FileIO;
import org.epilogtool.project.Project;
import org.junit.BeforeClass;
import org.junit.Test;

public class IntegrationFunctionsTest {
	private final static String YellowRedName = "YellowRed.sbml";
	private final static String GreenRedName = "GreenRed.sbml";
	private static LogicalModel YellowRed;
	private static LogicalModel GreenRed;

	@BeforeClass
	public static void loadModelBeforeTests() throws IOException {

		File fYellowRed = FileUtils.getResource("testModels", YellowRedName);
		File fGreenRed = FileUtils.getResource("testModels", GreenRedName);

		YellowRed = FileIO.loadSBMLModel(fYellowRed);
		GreenRed = FileIO.loadSBMLModel(fGreenRed);

	}

	@Test
	public void test1() {

		Set<NodeInfo> integrationInputs = new HashSet<NodeInfo>();

		integrationInputs.add(YellowRed.getComponent("Yellow"));
		integrationInputs.add(GreenRed.getComponent("Green"));

		String strIntFunctions = "[Yellow, Green]";
		String strIntFunctions_2 = "[Green, Yellow]";
		boolean bFlag = true;

		// Test if the integration inputs are correct
		if ((Project.getInstance().getEpitheliumList().get(0).getIntegrationNodes().toString().equals(strIntFunctions)
				|| (Project.getInstance().getEpitheliumList().get(0).getIntegrationNodes().toString()
						.equals(strIntFunctions_2)))) {
			assertTrue(bFlag);
		}

		// Test if the integration functions are correct
		Set<String> strIntegrationFunctions_ctr = new HashSet<String>();
		strIntegrationFunctions_ctr.add("{Red [1]}");
		strIntegrationFunctions_ctr.add("{Red [0:20]}");

		Set<String> strIntegrationFunctions = new HashSet<String>();
		for (NodeInfo nodeInt : integrationInputs) {
			// System.out.println(nodeInt);

			NodeInfo node = Project.getInstance().getProjectFeatures().getNodeInfo(nodeInt.getNodeID());

			ComponentIntegrationFunctions cif = Project.getInstance().getEpitheliumList().get(0)
					.getIntegrationFunctions().getAllIntegrationFunctions().get(node);
			// System.out.println(Project.getInstance().getEpitheliumList().get(0).getIntegrationFunctions().getAllIntegrationFunctions().get(node));
			for (String strIntFunction : cif.getFunctions()) {
				strIntegrationFunctions.add(strIntFunction);
			}
		}
		assertEquals(strIntegrationFunctions, strIntegrationFunctions_ctr);
	}

}
