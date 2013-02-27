package pt.gulbenkian.igc.nmd;

import java.util.ArrayList;

/*
 * 
 * 
 * 
 * 
 */

public class Neighbours {

	private static final int[][] neighbors_x = { { -1, +1, 0, 0, -1, -1 },
			{ -1, 1, 0, 0, 1, 1 } };
	private static final int[] neighbors_y = { 0, 0, -1, 1, -1, 1 };

	
	
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

	public static ArrayList<Integer> horizontalRollOver(int instance) {

		ArrayList<Integer> neighbors = new ArrayList<Integer>();

		int columnNeighbor;
		int rowNeighbor;

		int row = matrixRow(instance, MainPanel.getGridWidth());
		int column = matrixColumn(instance, MainPanel.getGridWidth());
		// System.out.println(instance + " ->"
		// + matrixRow(instance, MainPanel.getGridWidth()) + " "
		// + matrixColumn(instance, MainPanel.getGridWidth()));

		for (int i = 0; i < neighbors_y.length; i++) {
			if (column % 2 == 0) {

				rowNeighbor = row + neighbors_x[0][i];
				columnNeighbor = column + neighbors_y[i];

				if (columnNeighbor >= MainPanel.getGridWidth()) {
					columnNeighbor = 0;
				}
				if (columnNeighbor < 0) {
					columnNeighbor = MainPanel.getGridWidth() - 1;
				}
			} else {
				columnNeighbor = column + neighbors_y[i];
				rowNeighbor = row + neighbors_x[1][i];
				if (columnNeighbor >= MainPanel.getGridWidth()) {
					columnNeighbor = 0;
				}
				if (columnNeighbor < 0) {
					columnNeighbor = MainPanel.getGridWidth() - 1;
				}
			}
			if (columnNeighbor >= 0 && rowNeighbor >= 0 && columnNeighbor<MainPanel.getGridWidth() && rowNeighbor<MainPanel.getGridHeight()) {
			int index = instanceIndex(rowNeighbor, columnNeighbor);
			

			neighbors.add(index);

//			System.out.println(index + " ->"
//					+ matrixRow(index, MainPanel.getGridWidth()) + " "
//					+ matrixColumn(index, MainPanel.getGridWidth()));
//			System.out.println(" ");
		}}
		return neighbors;
	}

	public static ArrayList<Integer> verticalRollOver(int instance) {

		ArrayList<Integer> neighbors = new ArrayList<Integer>();

		int columnNeighbor;
		int rowNeighbor;

		int row = matrixRow(instance, MainPanel.getGridWidth());
		int column = matrixColumn(instance, MainPanel.getGridWidth());
		// System.out.println(instance + " ->"
		// + matrixRow(instance, MainPanel.getGridWidth()) + " "
		// + matrixColumn(instance, MainPanel.getGridWidth()));

		for (int i = 0; i < neighbors_y.length; i++) {
			if (column % 2 == 0) {

				rowNeighbor = row + neighbors_x[0][i];
				columnNeighbor = column + neighbors_y[i];
				//System.out.println(rowNeighbor+ " " + columnNeighbor);
				if (rowNeighbor >= MainPanel.getGridHeight()) {
					rowNeighbor = 0;
				}
				if (rowNeighbor < 0) {
					rowNeighbor = MainPanel.getGridHeight() - 1;
					
				}
			} else {
				columnNeighbor = column + neighbors_y[i];
				rowNeighbor = row + neighbors_x[1][i];
				if (rowNeighbor >= MainPanel.getGridHeight()) {
					rowNeighbor = 0;
				}
				if (rowNeighbor < 0) {
					rowNeighbor = MainPanel.getGridHeight() - 1;
				}
			}
			if (columnNeighbor >= 0 && rowNeighbor >= 0 && columnNeighbor<MainPanel.getGridWidth() && rowNeighbor<MainPanel.getGridHeight()) {
				int index = instanceIndex(rowNeighbor, columnNeighbor);

			neighbors.add(index);

//			System.out.println(index + " ->"
//					+ matrixRow(index, MainPanel.getGridWidth()) + " "
//					+ matrixColumn(index, MainPanel.getGridWidth()));
//			System.out.println(" ");
		}}
		return neighbors;
	}

	public static ArrayList<Integer> noRollOver(int instance) {

		String a = MainPanelDescription.setupRollOverPanel();
	//	System.out.print(a);
		ArrayList<Integer> neighbors = new ArrayList<Integer>();

		int columnNeighbor;
		int rowNeighbor;

		int row = matrixRow(instance, MainPanel.getGridWidth());
		int column = matrixColumn(instance, MainPanel.getGridWidth());
		// System.out.println(instance + " ->"
		// + matrixRow(instance, MainPanel.getGridWidth()) + " "
		// + matrixColumn(instance, MainPanel.getGridWidth()));

		for (int i = 0; i < neighbors_y.length; i++) {
			if (column % 2 == 0) {

				rowNeighbor = row + neighbors_x[0][i];
				columnNeighbor = column + neighbors_y[i];

			} else {
				columnNeighbor = column + neighbors_y[i];
				rowNeighbor = row + neighbors_x[1][i];
			}
			if (columnNeighbor >= 0 && rowNeighbor >= 0 && columnNeighbor<MainPanel.getGridWidth() && rowNeighbor<MainPanel.getGridHeight()) {
				int index = instanceIndex(rowNeighbor, columnNeighbor);

				neighbors.add(index);

//				System.out.println(index + " ->"
//						+ matrixRow(index, MainPanel.getGridWidth()) + " "
//						+ matrixColumn(index, MainPanel.getGridWidth()));
//				System.out.println(" ");
			}
		}
		return neighbors;
	}
	
	public static ArrayList<Integer> oneDistanceNeighbours(int instance){
		
		ArrayList<Integer> neighbors = new ArrayList<Integer>();
		
		String option = MainPanelDescription.getRollOver();
		if (option == "Vertical Roll-Over")
			neighbors = verticalRollOver(instance);
		if (option == "No Roll-Over")
			neighbors = noRollOver(instance);
		if (option == "Horizontal Roll-Over")
			neighbors = horizontalRollOver(instance);
		return null;
		
	}

}
