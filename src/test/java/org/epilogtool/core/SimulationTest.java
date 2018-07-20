package org.epilogtool.core;

import java.io.File;
import java.io.IOException;

import org.epilogtool.FileUtils;
import org.epilogtool.io.FileIO;
import org.junit.BeforeClass;
import org.junit.Test;

public class SimulationTest {
	private final static String filePepsName = "ColorModels.peps";

	@BeforeClass
	public static void loadModelBeforeTests() throws IOException {
		File filePeps = FileUtils.getResource("testProjects", filePepsName);
		FileIO.loadPEPS(filePeps.getAbsolutePath());
	}

	@Test
	public void simulationTest() {

		// TODO
		// verify nextStep - Random Seed. everything is different
		// verify terminal cycle
		// verify stable pattern
		// verify perturb model

	}
}
