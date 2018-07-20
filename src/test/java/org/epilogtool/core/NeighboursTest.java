package org.epilogtool.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.epilogtool.FileUtils;
import org.epilogtool.common.Tuple2D;
import org.epilogtool.io.FileIO;
import org.epilogtool.project.Project;
import org.junit.BeforeClass;
import org.junit.Test;

public class NeighboursTest {
	private final static String filePepsName = "ColorModels.peps";
	
	private Map<Tuple2D<Integer>, Map<Boolean, Set<Tuple2D<Integer>>>> relativeNeighboursCache = new HashMap<Tuple2D<Integer>, Map<Boolean, Set<Tuple2D<Integer>>>>();

	@BeforeClass
	public static void loadModelBeforeTests() throws IOException {
		File filePeps = FileUtils.getResource("testProjects", filePepsName);
		FileIO.loadPEPS(filePeps.getAbsolutePath());
	}

	public void neighbours(int index, List<Integer> neighboursNumber, int x, int y, int max) {

		// int minSign = 1;
		for (int maxSign = 0; maxSign < max; maxSign++) {

			// verify cumulative distances
			int minSign = 1;
			Tuple2D<Integer> rangePair = new Tuple2D<Integer>(minSign, maxSign);
			Tuple2D<Integer> rangeList_aux = new Tuple2D<Integer>(0, (minSign - 1 > 0) ? minSign - 1 : 0);

			Set<Tuple2D<Integer>> posNeighbours = Project.getInstance().getEpitheliumList().get(index)
					.getEpitheliumGrid()
					.getPositionNeighbours(this.relativeNeighboursCache, rangeList_aux, rangePair, minSign, x, y);

			int number = 0;
			for (int ind = 0; ind <= maxSign; ind++) {
				number = number + neighboursNumber.get(ind);
			}

			assertEquals(posNeighbours.size(), number);

			// verify exact distances
			// must increase the value of minSig to have exactly the same number of
			// neighbours

			minSign = maxSign;
			rangePair = new Tuple2D<Integer>(minSign, maxSign);
			rangeList_aux = new Tuple2D<Integer>(0, (minSign - 1 > 0) ? minSign - 1 : 0);
			posNeighbours = Project.getInstance().getEpitheliumList().get(index).getEpitheliumGrid()
					.getPositionNeighbours(this.relativeNeighboursCache, rangeList_aux, rangePair, minSign, x, y);

			// System.out.println(posNeighbours);
			number = neighboursNumber.get(maxSign);
			if (minSign == 0)
				number = 1;
			assertEquals(posNeighbours.size(), number);
		}

	}

	public void neighbourRange(int index, int minSign, int maxSign, int x, int y, int expectedNumber) {

		Tuple2D<Integer> rangePair = new Tuple2D<Integer>(minSign, maxSign);
		Tuple2D<Integer> rangeList_aux = new Tuple2D<Integer>(0, (minSign - 1 > 0) ? minSign - 1 : 0);

		Set<Tuple2D<Integer>> posNeighbours = Project.getInstance().getEpitheliumList().get(index).getEpitheliumGrid()
				.getPositionNeighbours(this.relativeNeighboursCache, rangeList_aux, rangePair, minSign, x, y);

		assertEquals(posNeighbours.size(), expectedNumber);

	}

	public boolean neighbourPresent(int index, int minSign, int maxSign, int x, int y, int Nx, int Ny) {
		Tuple2D<Integer> rangePair = new Tuple2D<Integer>(minSign, maxSign);
		Tuple2D<Integer> rangeList_aux = new Tuple2D<Integer>(0, (minSign - 1 > 0) ? minSign - 1 : 0);

		Set<Tuple2D<Integer>> posNeighbours = Project.getInstance().getEpitheliumList().get(index).getEpitheliumGrid()
				.getPositionNeighbours(this.relativeNeighboursCache, rangeList_aux, rangePair, minSign, x, y);

		return posNeighbours.contains(new Tuple2D<Integer>(Nx, Ny));
	}

	@Test
	public void neighbours() {

		// ***************PointyOdd
		neighbours(0, Arrays.asList(0, 6, 12, 18, 24, 23, 12, 4), 0, 0, 8); // Torus
		neighbourRange(0, 0, -1, 0, 0, 100); // Torus range[0:]
		neighbourRange(0, 1, -1, 0, 0, 99); // Torus range[1:]
		neighbourRange(0, 1, 40, 0, 0, 99); // Torus range[1:40]

		neighbours(1, Arrays.asList(0, 2, 4, 5, 7, 8, 10, 11, 13, 14, 9, 7, 5, 3, 1), 0, 0, 14); // rectangular
		neighbours(2, Arrays.asList(0, 4, 7, 10, 13, 15, 14, 13, 12, 11), 0, 0, 9); // Horizontal
		neighbours(3, Arrays.asList(0, 3, 7, 9, 13, 12, 10, 10, 10, 10, 9, 5, 1), 0, 0, 10); // vertical

		// ***************PointyEven
		neighbours(4, Arrays.asList(0, 6, 12, 18, 24, 23, 12, 4), 0, 0, 8); // Torus
		neighbours(5, Arrays.asList(0, 2, 4, 5, 7, 8, 10, 11, 13, 14, 9, 7, 5, 3, 1), 0, 0, 14); // rectangular
		neighbours(6, Arrays.asList(0, 4, 7, 10, 13, 15, 14, 13, 12, 11), 0, 0, 9); // Horizontal
		neighbours(7, Arrays.asList(0, 3, 7, 9, 13, 12, 10, 10, 10, 10, 9, 5, 1), 0, 0, 10); // vertical

		// ***************FlatOdd
		neighbours(8, Arrays.asList(0, 6, 12, 18, 24, 23, 12, 4), 0, 0, 8); // Torus
		neighbours(9, Arrays.asList(0, 2, 4, 5, 7, 8, 10, 11, 13, 14, 9, 7, 5, 3, 1), 0, 0, 14); // rectangular
		neighbours(10, Arrays.asList(0, 4, 7, 10, 13, 15, 14, 13, 12, 11), 0, 0, 9); // Horizontal
		neighbours(11, Arrays.asList(0, 3, 7, 9, 13, 12, 10, 10, 10, 10, 9, 5, 1), 0, 0, 10); // vertical

		// ***************FlatEven
		neighbours(12, Arrays.asList(0, 6, 12, 18, 24, 23, 12, 4), 0, 0, 8); // Torus
		neighbours(13, Arrays.asList(0, 2, 4, 5, 7, 8, 10, 11, 13, 14, 9, 7, 5, 3, 1), 0, 0, 14); // rectangular
		neighbours(14, Arrays.asList(0, 4, 7, 10, 13, 15, 14, 13, 12, 11), 0, 0, 9); // Horizontal
		neighbours(15, Arrays.asList(0, 3, 7, 9, 13, 12, 10, 10, 10, 10, 9, 5, 1), 0, 0, 10); // vertical

		// TODO: verify neighbours for intermediate values
		assertFalse(neighbourPresent(0, 2, 4, 0, 0, 0, 0));
		assertTrue(neighbourPresent(0, 2, 4, 0, 0, 0, 2));

	}
}
