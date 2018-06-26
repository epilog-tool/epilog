package org.epilogtool.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;

import org.colomoto.biolqm.LogicalModel;
import org.colomoto.biolqm.modifier.perturbation.AbstractPerturbation;
import org.colomoto.biolqm.modifier.perturbation.RangePerturbation;
import org.epilogtool.TestHelper;
import org.epilogtool.common.Txt;
import org.epilogtool.io.FileIO;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class EpitheliumCellTest {
	private final static String fileSBML = "simple.sbml";
	private final static String filePepsName = "ColorModels.peps";
	private final static String noConfigPepsName = "NoConfig.peps";
	private final static String noSBMLPepsName = "NoSBML.peps";
	private static EpitheliumCell epicell;
	private static LogicalModel m;

	
	//Test if an sbml is properly loaded
	@BeforeClass
	public static void loadModelBeforeTests() throws IOException {
		File fSBML = TestHelper.getTestResource("testModels", fileSBML);
		m = FileIO.loadSBMLModel(fSBML);
	}
	

	@Before
	public void loadEpiCellBeforeEachTest() {
		epicell = new EpitheliumCell(m);
	}
	
	
	//Test if a project (Peps file) is properly loaded
	@Test
	public void loadProjectTest() throws IOException {
		
		File filePeps = TestHelper.getTestResource("testProjects", filePepsName);
		File noConfigPepsFile = TestHelper.getTestResource("testProjects", noConfigPepsName);
		File noSBMLPepsFile = TestHelper.getTestResource("testProjects", noSBMLPepsName);
		
		try {
			assertTrue(FileIO.loadPEPS(filePeps.getAbsolutePath()));
		} catch (Exception e) {
			fail("");
		}
		
		try {
			assertFalse(FileIO.loadPEPS(noConfigPepsFile.getAbsolutePath()));
		} catch (Exception e) {
			fail("");
		}
		try {
			FileIO.loadPEPS(noSBMLPepsFile.getAbsolutePath());
			fail("should have failed");
		} catch (Exception e) {
			assertEquals("Correctly failed", Txt.get("s_SBML_failed_load"), e.getMessage());
		}
	}

	//Tests if rollOver is being identified
	@Test
	public void rollOverTest() {
		File filePeps = TestHelper.getTestResource("testProjects", filePepsName);
	}
	
	
	@Test
	public void cleanSlateTest() {
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
	public void perturbationTest() {
		AbstractPerturbation rp = new RangePerturbation(m.getComponents().get(0), 0, 1);
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
		for (int i = 0; i < m.getComponents().size(); i++) {
			assertEquals(clone.getState()[i], epicell.getState()[i]);
		}
	}
}
