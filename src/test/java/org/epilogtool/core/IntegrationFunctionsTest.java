package org.epilogtool.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.colomoto.biolqm.LogicalModel;
import org.colomoto.biolqm.NodeInfo;
import org.colomoto.biolqm.NodeInfoHolder;
import org.epilogtool.FileUtils;
import org.epilogtool.io.FileIO;
import org.epilogtool.project.Project;
import org.junit.BeforeClass;
import org.junit.Test;

public class IntegrationFunctionsTest {
	private final static String filePepsName = "ColorModels.peps";
	private final static String YellowRedName = "YellowRed.sbml";
	private final static String GreenRedName = "GreenRed.sbml";
	private static LogicalModel mYellowRed;
	private static LogicalModel mGreenRed;

	@BeforeClass
	public static void loadModelBeforeTests() throws Exception {
		File filePeps = FileUtils.getResource("testProjects", filePepsName);
		FileIO.loadPEPS(filePeps.getAbsolutePath());
		mYellowRed = FileIO.loadSBMLModel(FileUtils.getResource("testModels", YellowRedName));
		mGreenRed = FileIO.loadSBMLModel(FileUtils.getResource("testModels", GreenRedName));
	}

	@Test
	public void testIntNodes() {

		Set<NodeInfo> setNodes = Project.getInstance().getEpitheliumList().get(0).getIntegrationNodes();
		assertEquals(setNodes.size(), 2);

		NodeInfo y = mYellowRed.getComponent("Yellow");
		for (NodeInfo n : setNodes) {
			if (n instanceof NodeInfo) {
//				System.out.println("NodeInfo: " + n);
			} else if (n instanceof NodeInfoHolder) {
//				System.out.println("NodeInfoHolder: " + n);
			}
//			if (n.getNodeID().equals(y.getNodeID()) && n.getMax() == y.getMax() && n.isInput() == y.isInput()) {
//				System.out.println("-------------------\nN: " + n + "\nY: " + y + "\n-----------------------");
//			}
		}

//		if (setNodes.contains(y)) {
//			System.out.println("-Yellow");
//		} else {
//			System.out.println("-MEH");
//		}
//		assertFalse(setNodes.contains(mYellowRed.getComponent("Red")));
//		assertTrue(setNodes.contains(mGreenRed.getComponent("Green")));
//		assertFalse(setNodes.contains(mGreenRed.getComponent("Red")));
	}

	@Test
	public void testSyntax() {
		// NodeInfo nodeYellow = mYellowRed.getComponent("Yellow");
		// ComponentIntegrationFunctions cif =
		// Project.getInstance().getEpitheliumList().get(0)
		// .getIntegrationFunctionsForComponent(nodeYellow);

		// Invalid function expressions
		// try {
		// cif.setFunctionAtLevel((byte) 1, "Red");
		// fail();
		// } catch (RuntimeException re) {
		// }
		// try {
		// cif.setFunctionAtLevel((byte) 1, "TRUE && FALSE");
		// fail();
		// } catch (RuntimeException re) {
		// }
		// try {
		// cif.setFunctionAtLevel((byte) 1, "{TRUE}");
		// fail();
		// } catch (RuntimeException re) {
		// }
		// try {
		// cif.setFunctionAtLevel((byte) 1, "TRUE AND FALSE");
		// fail();
		// } catch (RuntimeException re) {
		// }
		// try {
		// cif.setFunctionAtLevel((byte) 1, "{Red,}");
		// fail();
		// } catch (RuntimeException re) {
		// }
		// try {
		// cif.setFunctionAtLevel((byte) 1, "{Red,min=a}");
		// fail();
		// } catch (RuntimeException re) {
		// }
		// try {
		// cif.setFunctionAtLevel((byte) 1, "{Red,min=2,max=1}");
		// fail();
		// } catch (RuntimeException re) {
		// }

		// Valid function expressions
		try {
			// cif.setFunctionAtLevel((byte) 1, "TRUE");
			// cif.setFunctionAtLevel((byte) 1, "FALSE");
			// cif.setFunctionAtLevel((byte) 1, "!TRUE");
			// cif.setFunctionAtLevel((byte) 1, "!FALSE");
			//
			// cif.setFunctionAtLevel((byte) 1, "{Red, min=1}");
			// cif.setFunctionAtLevel((byte) 1, "{Red}");
		} catch (RuntimeException re) {
			re.printStackTrace();
			fail();
		}
	}

}
