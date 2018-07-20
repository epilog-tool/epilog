package org.epilogtool.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import org.epilogtool.FileUtils;
import org.epilogtool.io.FileIO;
import org.epilogtool.notification.NotificationManager;
import org.epilogtool.project.Project;
import org.junit.BeforeClass;
import org.junit.Test;

public class LoadProjectTest {

	private final static String filePepsName = "ColorModels.peps";
	private final static String noConfigPepsName = "NoConfig.peps";
	private final static String noSBMLPepsName = "NoSBML.peps";

	// Test if a project (Peps file) is properly loaded

	@BeforeClass
	public static void setUp() {
		NotificationManager.setGUI(false);
	}

	@Test
	public void projectNotFound() {
		try {
			FileIO.loadPEPS("bla");
			fail("Loaded a fake PEPS - should have failed");
		} catch (Exception e) {
		}
	}

	@Test
	public void configNotFound() {
		File noConfigPepsFile = FileUtils.getResource("testProjects", noConfigPepsName);
		try {
			assertFalse(FileIO.loadPEPS(noConfigPepsFile.getAbsolutePath()));
		} catch (Exception e) {
			fail("Loaded a PEPS without config.txt - should have returned false");
		}
	}

	@Test
	public void sbmlNotFound() throws IOException {
		File noSBMLPepsFile = FileUtils.getResource("testProjects", noSBMLPepsName);

		// TODO: 1 peps com config.txt e sem .sbml
		// noSBMLPepsFile
	}

	@Test
	public void loadProjectTest() throws IOException {
		File filePeps = FileUtils.getResource("testProjects", filePepsName);

		// Good PEPS with SBML files
		try {
			assertTrue(FileIO.loadPEPS(filePeps.getAbsolutePath()));
		} catch (Exception e) {
			fail("Loading a PEPS with config.txt - should have returned true");
		}
		Set<String> sModels = Project.getInstance().getModelNames();
		assertEquals(sModels.size(), 3);
		assertTrue(sModels.contains("GreenRed.sbml"));
		assertTrue(sModels.contains("GreenBlue.sbml"));
		assertTrue(sModels.contains("YellowRed.sbml"));
	}
}
