package org.epilogtool.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.io.IOException;

import org.colomoto.biolqm.LogicalModel;
import org.epilogtool.FileUtils;
import org.epilogtool.core.cell.LivingCell;
import org.epilogtool.io.FileIO;
import org.epilogtool.project.Project;
import org.junit.BeforeClass;
import org.junit.Test;

public class EpitheliumCellTest {
	private final static String YellowRedName = "YellowRed.sbml";
	private final static String GreenRedName = "GreenRed.sbml";
	private final static String filePepsName = "ColorModels.peps";

	@BeforeClass
	public static void loadModelBeforeTests() throws IOException {
		File filePeps = FileUtils.getResource("testProjects", filePepsName);
		FileIO.loadPEPS(filePeps.getAbsolutePath());
	}

	@Test
	public void cellularModelOnCell() {
		Epithelium epi = Project.getInstance().getEpitheliumList().get(0);
		LogicalModel m = epi.getModel(0, 0);
		assertEquals(Project.getInstance().getProjectFeatures().getModelName(m), YellowRedName);

		m = epi.getModel(6, 0);
		assertEquals(Project.getInstance().getProjectFeatures().getModelName(m), GreenRedName);
	}

	@Test
	public void cleanSlateTest() {
		Epithelium epi = Project.getInstance().getEpitheliumList().get(0);
		LogicalModel m = epi.getModel(0, 0);
		LivingCell epicell = new LivingCell(m);
		
		assertNull(epicell.getPerturbation());
		byte[] state = epicell.getState();
		assertNotNull(state);
		for (int i = 0; i < m.getComponents().size(); i++) {
			assertEquals(0, state[i]);
		}
		assertNotNull(epicell.getModel());
		assertEquals(m, epicell.getModel());
	}

	@Test
	public void cloneTest() {
		Epithelium epi = Project.getInstance().getEpitheliumList().get(0);
		LogicalModel m = epi.getModel(0, 0);
		LivingCell epicell = new LivingCell(m);

		LivingCell clone = epicell.clone();
		assertEquals(clone.getModel(), epicell.getModel());
		assertEquals(clone.getPerturbation(), epicell.getPerturbation());
		assertNotNull(clone.getState());
		for (int i = 0; i < m.getComponents().size(); i++) {
			assertEquals(clone.getState()[i], epicell.getState()[i]);
		}
	}
}
