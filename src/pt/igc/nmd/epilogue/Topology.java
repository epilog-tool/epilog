package pt.igc.nmd.epilogue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Topology {

	// private Set<Integer> neighbors = Collections.synchronizedSet(new
	// HashSet<Integer>());;
	// private Set<Integer> neighborsaux = Collections.synchronizedSet(new
	// HashSet<Integer>());
	static Set<Integer> neighbors1 = Collections
			.synchronizedSet(new HashSet<Integer>());;
	static Set<Integer> neighborsaux1 = Collections
			.synchronizedSet(new HashSet<Integer>());

	public int width;
	public int height;
	private int[][] neighbors_x = { { -1, +1, 0, 0, -1, -1 },
			{ -1, 1, 0, 0, 1, 1 } };
	private int[] neighbors_y = { 0, 0, -1, 1, -1, 1 };
	private String rollOver;

	public Topology(int width, int height) {
		super();
		width = (width % 2 == 0) ? width : width + 1;
		height = (height % 2 == 0) ? height : height + 1;
		this.width = width;
		this.height = height;
		this.rollOver = rollOver;
		
		// neighbors = Collections.synchronizedSet(new HashSet<Integer>());;
		// neighborsaux = Collections.synchronizedSet(new HashSet<Integer>());;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getWidth() {
		return this.width;
	}

	public int getHeight() {
		return this.height;
	}

	public int getNumberInstances() {
		return this.height * this.width;
	}

	/* Methods to transform instance index to coordinates and vice-versa */
	public int instance2i(int instance, int width) {

		int i = instance % width;
		return i;
	}

	public int instance2j(int instance, int width) {

		int j = instance / width;
		return j;
	}

	public int coords2Instance(int i, int j) {
		return j * this.width + i;
	}

	// Neighbours

	public Set<Integer> groupNeighbors(int instance, int distance) {
		return nDistanceNeighbours(instance, distance);
	}

	public boolean areNeighbors(int instanceA, int instanceB, int distance) {
		ArrayList<Integer> neighbours = null;

		if (groupNeighbors(instanceA, distance).contains(instanceB)) {
			return true;
		} else
			return false;

	}

	// ROLL OVER

	public void setRollOver(String rollOver) {
		this.rollOver = rollOver;
	}

	public ArrayList<Integer> horizontalRollOver(int instance) {
		ArrayList<Integer> neighbors = new ArrayList<Integer>();

		int iNeighbor;
		int jNeighbor;

		int i = instance2i(instance, getWidth());
		int j = instance2j(instance, getWidth());

		System.out.println(instance + " ->" + instance2i(instance, getWidth())
				+ " " + instance2j(instance, getWidth()));

		for (int k = 0; k < neighbors_y.length; k++) {
			if (i % 2 == 0) {

				jNeighbor = j + neighbors_x[0][k];
				iNeighbor = i + neighbors_y[k];

				if (iNeighbor >= getWidth()) {
					iNeighbor = 0;
				}
				if (iNeighbor < 0) {
					iNeighbor = getWidth() - 1;
				}
			} else {
				iNeighbor = i + neighbors_y[k];
				jNeighbor = j + neighbors_x[1][k];

				if (iNeighbor >= getWidth()) {
					iNeighbor = 0;
				}
				if (iNeighbor < 0) {
					iNeighbor = getWidth() - 1;
				}
			}
			if (iNeighbor >= 0 && jNeighbor >= 0 && iNeighbor < getWidth()
					&& jNeighbor < getHeight()) {
				int index = coords2Instance(iNeighbor, jNeighbor);

				neighbors.add(index);

			}
		}
		return neighbors;
	}

	public ArrayList<Integer> verticalRollOver(int instance) {

		ArrayList<Integer> neighbors = new ArrayList<Integer>();

		int iNeighbor;
		int jNeighbor;

		int i = instance2i(instance, getWidth());
		int j = instance2j(instance, getWidth());

		System.out.println(instance + " ->" + instance2i(instance, getWidth())
				+ " " + instance2j(instance, getWidth()));

		for (int k = 0; k < neighbors_y.length; k++) {
			if (i % 2 == 0) {

				jNeighbor = j + neighbors_x[0][k];
				iNeighbor = i + neighbors_y[k];

				if (jNeighbor >= getHeight()) {
					jNeighbor = 0;
				}
				if (jNeighbor < 0) {
					jNeighbor = getHeight() - 1;
				}
			} else {
				iNeighbor = i + neighbors_y[k];
				jNeighbor = j + neighbors_x[1][k];

				if (jNeighbor >= getHeight()) {
					jNeighbor = 0;
				}
				if (jNeighbor < 0) {
					jNeighbor = getHeight() - 1;
				}
			}
			if (iNeighbor >= 0 && jNeighbor >= 0 && iNeighbor < getWidth()
					&& jNeighbor < getHeight()) {
				int index = coords2Instance(iNeighbor, jNeighbor);

				neighbors.add(index);

			}
		}
		return neighbors;
	}

	public ArrayList<Integer> noRollOver(int instance) {

		ArrayList<Integer> neighbors = new ArrayList<Integer>();

		int iNeighbor;
		int jNeighbor;

		int i = instance2i(instance, getWidth());
		int j = instance2j(instance, getWidth());

		// System.out.println(instance + " ->" + instance2i(instance,
		// getWidth())
		// + " " + instance2j(instance, getWidth()));

		for (int k = 0; k < neighbors_y.length; k++) {
			if (i % 2 == 0) {

				jNeighbor = j + neighbors_x[0][k];
				iNeighbor = i + neighbors_y[k];

			} else {
				iNeighbor = i + neighbors_y[k];
				jNeighbor = j + neighbors_x[1][k];
			}
			if (iNeighbor >= 0 && jNeighbor >= 0 && iNeighbor < getWidth()
					&& jNeighbor < getHeight()) {
				int index = coords2Instance(iNeighbor, jNeighbor);

				neighbors.add(index);

			}
		}
		return neighbors;
	}

	public ArrayList<Integer> oneDistanceNeighbours(int instance) {

		ArrayList<Integer> neighbours = new ArrayList<Integer>();

		String option = getRollOver();

		if (option == "Vertical Roll-Over")
			neighbours = verticalRollOver(instance);
		if (option == "No Roll-Over" | option==null)
			neighbours = noRollOver(instance);
		if (option == "Horizontal Roll-Over")
			neighbours = horizontalRollOver(instance);
		return neighbours;

	}

	public Set<Integer> nDistanceNeighbours(int instance, int distance) {

//		Set<Integer> neighbors = new HashSet<Integer>(neighbors1);
//		Set<Integer> neighborsaux = new HashSet<Integer>(neighborsaux1);
//
		Set<Integer> neighbours = new HashSet<Integer>();
		// System.out.println(neighbors1);
		if (distance > 0) {
			for (int k : oneDistanceNeighbours(instance)) {

				neighbours.add(k);
			}
		}
//
//			Iterator it = neighborsaux.iterator();
//			while (it.hasNext()) {
//				Integer item = (Integer) it.next();
//				// System.out.println("item " + item);
//				neighborsaux1.add(item);
//				neighbors1.add(item);
//
//			}
//
//			Iterator it1 = neighborsaux.iterator();
//			while (it1.hasNext()) {
//				Integer item = (Integer) it1.next();
//				nDistanceNeighbours(item, distance - 1);
//			}
//
//		}
		return neighbours;
	}

	public String getRollOver() {
		return this.rollOver;
	}
}
