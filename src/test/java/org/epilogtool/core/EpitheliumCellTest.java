package org.epilogtool.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.io.IOException;

import org.colomoto.logicalmodel.LogicalModel;
import org.colomoto.logicalmodel.perturbation.AbstractPerturbation;
import org.colomoto.logicalmodel.perturbation.RangePerturbation;
import org.epilogtool.io.FileIO;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class EpitheliumCellTest {
	private final static String dirSBML = "testmodels/";
	private final static String fileSBML = "simple.sbml";
	private static EpitheliumCell epicell;
	private static LogicalModel m;

	@BeforeClass
	public static void loadModelBeforeTests() throws IOException {
		File fSBML = new File(dirSBML + fileSBML);
		m = FileIO.loadSBMLModel(fSBML);
	}

	@Before
	public void loadEpiCellBeforeEachTest() {
		epicell = new EpitheliumCell(m);
	}

	@Test
	public void cleanSlateTest() {
		assertNull(epicell.getPerturbation());
		byte[] state = epicell.getState();
		assertNotNull(state);
		for (int i = 0; i < m.getNodeOrder().size(); i++) {
			assertEquals(0, state[i]);
		}
		assertNotNull(epicell.getModel());
		assertEquals(m, epicell.getModel());
	}

	@Test
	public void perturbationTest() {
		AbstractPerturbation rp = new RangePerturbation(
				m.getNodeOrder().get(0), 0, 1);
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
		for (int i = 0; i < m.getNodeOrder().size(); i++) {
			assertEquals(clone.getState()[i], epicell.getState()[i]);
		}
	}
}
