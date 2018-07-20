package org.epilogtool.core;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.colomoto.biolqm.LogicalModel;
import org.epilogtool.FileUtils;
import org.epilogtool.common.Txt;
import org.epilogtool.io.FileIO;
import org.epilogtool.project.Project;
import org.junit.BeforeClass;
import org.junit.Test;

public class EpitheliumTest {
	private final static String YellowRedName = "YellowRed.sbml";
	private final static String GreenRedName = "GreenRed.sbml";

	private static LogicalModel yellowRed;
	private static LogicalModel greenRed;

	// Test if an sbml is properly loaded
	@BeforeClass
	public static void loadModelBeforeTests() throws IOException {
		yellowRed = FileIO.loadSBMLModel(FileUtils.getResource("testModels", YellowRedName));
		greenRed = FileIO.loadSBMLModel(FileUtils.getResource("testModels", GreenRedName));
	}

	@Test
	public void EpiListTest() throws IOException {
		assertEquals(Project.getInstance().getEpitheliumList().size(), 16);
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
		List<LogicalModel> lModels = new ArrayList<LogicalModel>();
		lModels.add(yellowRed);
		lModels.add(greenRed);

		List<LogicalModel> setLogicalModelFile = new ArrayList<LogicalModel>();
		for (LogicalModel m : Project.getInstance().getEpitheliumList().get(0).getEpitheliumGrid().getModelSet()) {
			Project.getInstance().getProjectFeatures().getModelName(m);
			if (Project.getInstance().getProjectFeatures().getModelName(m).equals(YellowRedName)
					| Project.getInstance().getProjectFeatures().getModelName(m).equals(GreenRedName)) {
				setLogicalModelFile.add(m);
			}
		}
		assertEquals(setLogicalModelFile.size(), lModels.size());

		// TODO
		// InputDefinitions -> integration functions
	}

	@Test
	public void Ep1Test() throws IOException {

		// RollOver
		assertEquals(Project.getInstance().getEpitheliumList().get(1).getEpitheliumGrid().getTopology().getRollOver()
				.toString(), Txt.get("s_Rollover_N"));

	}
}
