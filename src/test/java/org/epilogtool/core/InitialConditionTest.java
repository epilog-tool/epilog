package org.epilogtool.core;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;

import org.epilogtool.FileUtils;
import org.epilogtool.io.FileIO;
import org.epilogtool.project.Project;
import org.junit.BeforeClass;
import org.junit.Test;

public class InitialConditionTest {
	private final static String filePepsName = "ColorModels.peps";

	@BeforeClass
	public static void loadModelBeforeTests() throws IOException {
		File filePeps = FileUtils.getResource("testProjects", filePepsName);
		FileIO.loadPEPS(filePeps.getAbsolutePath());
	}

	@Test
	public void initialConditions() {
		// Tests 1) if the initial condition is properly read; 2) if the percentage is
		// properly measured
		Epithelium epi = Project.getInstance().getEpitheliumList().get(0);

		byte b;

		b = epi.getEpitheliumGrid().getCellValue(0, 0, "Red");
		assertEquals(b, (byte) 0);

		b = epi.getEpitheliumGrid().getCellValue(9, 9, "Red");
		assertEquals(b, (byte) 1);

		String strPercentage = epi.getEpitheliumGrid().getPercentage("Red");
		assertEquals(strPercentage, "(1 : 59%)");
	}
}
