package pt.gulbenkian.igc.nmd;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.Collection;
import java.lang.Math;

public class Topology {
	

	public int instanceNumber() {
		return MainPanel.getGridHeight() * MainPanel.getGridHeight();
	}

	/* Methods to transform instance index to coordinates and vice-versa */
	public static int matrixRow(int instance, int gridWidth) {

		int row = instance / gridWidth;
		return row;
	}

	public static int matrixColumn(int instance, int gridWidth) {

		int column = instance % gridWidth;
		return column;
	}

	public static int instanceIndex(int row, int column) {
		return row * MainPanel.getGridWidth() + column;
	}

	/* Methods to determine if instances are neighbours and groups of neighbours */

	public boolean areNeighbors(int instanceA, int instanceB) {
		HashSet<Integer> neighbours = null;
		neighbours = groupNeighbors(instanceA, 1);
		if (neighbours.contains(instanceB)) {
			return true;
		} else
			return false;

	}

	public static HashSet<Integer> neighborsOneDistanceAway(int instance) {
		
		
		HashSet<Integer> neighbors = new HashSet<Integer>();


		int row = matrixRow(instance, MainPanel.getGridWidth());
		int column = matrixColumn(instance, MainPanel.getGridWidth());
		
		double e = column%2;
		e = -Math.pow(-1,e);
		
		
		final int[] neighbors_x = { -1, 0, (int) e, 1, (int) e,0 };
		final int[] neighbors_y = { 0, 1, 1, 0, -1, -1 };
		
		// System.out.println(instance + " ->"
		// + matrixRow(instance, MainPanel.getGridWidth()) + " "
		// + matrixColumn(instance, MainPanel.getGridWidth()));
		
		for (int i = 0; i < neighbors_y.length; i++) {
		
				int rowNeighbor = row + neighbors_x[i];
				int columnNeighbor = column + neighbors_y[i];

				if (columnNeighbor > MainPanel.getGridWidth()) {
					columnNeighbor = 0;
				}
				if (columnNeighbor < 0) {
					columnNeighbor = MainPanel.getGridWidth() - 1;
				}
				if (rowNeighbor > MainPanel.getGridHeight()) {
					rowNeighbor = 0;
				}
				if (rowNeighbor < 0) {
					rowNeighbor = MainPanel.getGridHeight() - 1;
				}


			

			int index = instanceIndex(rowNeighbor, columnNeighbor);

			neighbors.add(index);
		
			System.out.println(index + " ->"
					+ matrixRow(index, MainPanel.getGridWidth()) + " "
					+ matrixColumn(index, MainPanel.getGridWidth()));
			System.out.println(" ");
		
		}
		return neighbors;
	}

	public static HashSet<Integer> groupNeighbors(int instance, int distance) {

		System.out.println(instance + " ->"
				+ matrixRow(instance, MainPanel.getGridWidth()) + " "
				+ matrixColumn(instance, MainPanel.getGridWidth()));
		System.out.println(" ");

		HashSet<Integer> notNeighborsOfDistanceN = new HashSet<Integer>();
		HashSet<Integer> newNeighbors = new HashSet<Integer>();
		HashSet<Integer> neighbors = new HashSet<Integer>();
		HashSet<Integer> neighborsAtDistance = new HashSet<Integer>();

		notNeighborsOfDistanceN.add(instance);
		if (distance == 1) {
			neighbors = neighborsOneDistanceAway(instance);
		} else {

			neighbors = neighborsOneDistanceAway(instance);

			for (int i = 0; i < distance - 1; i++) {
				for (int neighbor : neighbors) {

					notNeighborsOfDistanceN.add(neighbor);
					neighborsAtDistance = neighborsOneDistanceAway(neighbor);
					for (int aux : neighborsAtDistance) {
						if (!notNeighborsOfDistanceN.contains(aux)) {
							newNeighbors.add(aux);
						}
					}
				}
				neighbors = newNeighbors;

			}
			// for (int neighbor : neighbors) {
			// notNeighborsOfDistanceN.add(neighbor);
			// neighborsAtDistance = neighborsOneDistanceAway(neighbor);
			// for (int aux : neighborsAtDistance) {
			// if (!notNeighborsOfDistanceN.contains(aux)) {
			// newNeighbors.add(aux);
			// }
			// }
			// }

		}
//		for (int index : neighbors) {
//			System.out.println(index + " ->"
//					+ matrixRow(index, MainPanel.getGridWidth()) + " "
//					+ matrixColumn(index, MainPanel.getGridWidth()));
//			System.out.println(" ");
//		}
		return neighbors;

	}
	
	public static HashSet<Integer> test(HashSet<Integer> a, HashSet<Integer> b){
		// HashSet<Integer> a = new HashSet<Integer>();
		// HashSet<Integer> b = new HashSet<Integer>();
		 HashSet<Integer> c = new HashSet<Integer>();
		// a.add(1);
		// a.add(2);
		// a.add(3);
		// a.add(4);
		// a.add(3);
		// b.add(1);
		// b.add(2);
		 for (int aux: a){
		 if (!b.contains(aux)){
		 c.add(aux);
		 }
		 }
		 for (int aux: b){
		 if (!a.contains(aux)){
		c.add(aux);
		 }
		}
		
		// System.out.println(c);
		return c;
	}

	public static HashSet<Integer> testNeighbors(int instance, int distance) {

		System.out.println(instance + " ->"
				+ matrixRow(instance, MainPanel.getGridWidth()) + " "
				+ matrixColumn(instance, MainPanel.getGridWidth()));
		System.out.println(" ");

		HashSet<Integer> newNeighbors = new HashSet<Integer>();
		HashSet<Integer> neighbors = new HashSet<Integer>();
		HashSet<Integer> neighborsAtDistance = new HashSet<Integer>();

		neighbors = neighborsOneDistanceAway(instance);

		for (int i = 0; i < distance; i++) {
			for (int neighbor : neighbors) {

				neighborsAtDistance = neighborsOneDistanceAway(neighbor);
				for (int aux : neighborsAtDistance) {
				newNeighbors.add(aux);
				}
				
			}
			neighbors.add(instance);
			HashSet<Integer> aa = test(neighbors,newNeighbors);
			neighbors = newNeighbors;
		}

		
		HashSet<Integer> aa = test(neighbors,newNeighbors);
		
		for (int index : aa) {
			System.out.println(index + " ->"
					+ matrixRow(index, MainPanel.getGridWidth()) + " "
					+ matrixColumn(index, MainPanel.getGridWidth()));
			System.out.println(" ");
		}
		return aa;
	}
}
