package org.epilogtool.core;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;

import org.colomoto.biolqm.LogicalModel;
import org.epilogtool.FileUtils;
import org.epilogtool.common.EnumRandomSeed;
import org.epilogtool.io.FileIO;
import org.epilogtool.project.Project;
import org.junit.BeforeClass;
import org.junit.Test;

public class ModelUpdateTest {
	private final static String filePepsName = "ColorModels.peps";

	@BeforeClass
	public static void loadModelBeforeTests() throws IOException {
		File filePeps = FileUtils.getResource("testProjects", filePepsName);
		FileIO.loadPEPS(filePeps.getAbsolutePath());
	}

	@Test
	public void cellularModelUpdate() {
		Epithelium epi = Project.getInstance().getEpitheliumList().get(0);
		LogicalModel m = epi.getModel(0, 0);
		assertEquals(epi.getPriorityClasses(m).getClassVars(0).toString(), "[Yellow, Red]");

		// TODO: get a model with more than one component
	}

	@Test
	public void epitheliumModelUpdate() {
		// alpha
		assertEquals(Project.getInstance().getEpitheliumList().get(0).getUpdateSchemeInter().getAlpha(), (float) 0, 96);

		// Random seed
		assertEquals(Project.getInstance().getEpitheliumList().get(0).getUpdateSchemeInter().getRandomSeedType(),
				EnumRandomSeed.RANDOM);

		// Updated cells
		assertEquals(Project.getInstance().getEpitheliumList().get(0).getUpdateSchemeInter().getUpdateCells(),
				(UpdateCells.fromString("Only updatable cells")));
	}
}
