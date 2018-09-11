package org.epilogtool.core;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;

import org.colomoto.biolqm.LogicalModel;
import org.colomoto.biolqm.modifier.perturbation.AbstractPerturbation;
import org.colomoto.biolqm.modifier.perturbation.RangePerturbation;
import org.epilogtool.FileUtils;
import org.epilogtool.core.cell.LivingCell;
import org.epilogtool.io.FileIO;
import org.epilogtool.project.Project;
import org.junit.BeforeClass;
import org.junit.Test;

public class PerturbationTest {
	private final static String filePepsName = "ColorModels.peps";

	@BeforeClass
	public static void loadModelBeforeTests() throws IOException {
		File filePeps = FileUtils.getResource("testProjects", filePepsName);
		FileIO.loadPEPS(filePeps.getAbsolutePath());
	}

	@Test
	public void setPerturbation() {
		Epithelium epi = Project.getInstance().getEpitheliumList().get(0);
		LogicalModel m = epi.getModel(0, 0);
		LivingCell epicell = new LivingCell(m);
		AbstractPerturbation rp = new RangePerturbation(m.getComponents().get(0), 0, 1);
		epicell.setPerturbation(rp);
		assertEquals(rp, epicell.getPerturbation());
	}

	@Test
	public void perturbations() {
		// Tests 1) if the perturbations are properly defined
		Epithelium epi = Project.getInstance().getEpitheliumList().get(0);

		assertEquals(epi.getEpitheliumGrid().getPerturbation(4, 5).getStringRepresentation(), "Red%0");
		assertEquals(epi.getEpitheliumGrid().getPerturbation(5, 4).getStringRepresentation(), "Red%1");
		assertEquals(epi.getEpitheliumGrid().getPerturbation(0, 0), null);

		// TODO: get a model with more than one value to check the range
	}

}
