package org.epilogtool.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.colomoto.biolqm.LogicalModel;
import org.epilogtool.FileUtils;
import org.epilogtool.core.EpitheliumPhenotypes.Phenotype;
import org.epilogtool.io.FileIO;
import org.epilogtool.project.Project;
import org.junit.BeforeClass;
import org.junit.Test;

public class EpitheliumPhenotypesTest {
	@BeforeClass
	public static void loadProject() throws IOException {
		File projectFile = FileUtils.getResource("testProjects", "ColorModels.peps");
		FileIO.loadPEPS(projectFile.getAbsolutePath());
	}

	@Test
	public void wildcardPatternsMatchGridStates() {
		Phenotype phenotype = new Phenotype("middle value", "1*2");

		assertTrue(phenotype.match(new byte[] { 1, 0, 2 }));
		assertTrue(phenotype.match(new byte[] { 1, 1, 2 }));
		assertFalse(phenotype.match(new byte[] { 0, 1, 2 }));
		assertFalse(phenotype.match(new byte[] { 1, 2 }));
	}

	@Test
	public void gridReportsPhenotypePercentByModel() throws Exception {
		Epithelium epithelium = Project.getInstance().getEpitheliumList().get(0).clone();
		LogicalModel model = epithelium.getModel(0, 0);
		String nodeID = model.getComponents().get(0).getNodeID();
		for (int x = 0; x < epithelium.getX(); x++) {
			for (int y = 0; y < epithelium.getY(); y++) {
				epithelium.getEpitheliumGrid().setCellComponentValue(x, y, nodeID, (byte) 0);
			}
		}
		epithelium.getEpitheliumGrid().setCellComponentValue(0, 0, nodeID, (byte) 1);
		epithelium.getPhenotypes().addPhenotype(model, "active", "1" + repeatWildcard(model.getComponents().size() - 1));

		float percentage = epithelium.getEpitheliumGrid()
				.getPhenotypePercentages(epithelium.getPhenotypes()).get(model).get("active");

		assertEquals(100.0f / (epithelium.getX() * epithelium.getY()), percentage, 0.001f);
	}

	@Test
	public void phenotypeDefinitionsSurvivePepsRoundTrip() throws Exception {
		Epithelium epithelium = Project.getInstance().getEpitheliumList().get(0);
		LogicalModel model = epithelium.getModel(0, 0);
		epithelium.getPhenotypes().addPhenotype(model, "round trip", repeatWildcard(model.getComponents().size()));
		File savedProject = File.createTempFile("epilog-phenotypes", ".peps");
		try {
			FileIO.savePEPS(savedProject.getAbsolutePath());
			assertTrue(FileIO.loadPEPS(savedProject.getAbsolutePath()));
			Epithelium loadedEpithelium = Project.getInstance().getEpitheliumList().get(0);
			LogicalModel loadedModel = loadedEpithelium.getModel(0, 0);
			assertTrue(loadedEpithelium.getPhenotypes().getPhenotypes(loadedModel).stream()
					.anyMatch(phenotype -> "round trip".equals(phenotype.getName())));
		} finally {
			Files.deleteIfExists(savedProject.toPath());
		}
	}

	private String repeatWildcard(int count) {
		StringBuilder pattern = new StringBuilder();
		for (int index = 0; index < count; index++) {
			pattern.append('*');
		}
		return pattern.toString();
	}
}