package pt.igc.nmd.epilog;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class Topology implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3713765674642632574L;

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
		// width = (width % 2 == 0) ? width : width + 1;
		// height = (height % 2 == 0) ? height : height + 1;
		this.width = width;
		this.height = height;
		// this.rollOver = rollOver;

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
	public int instance2i(int instance) {

		int i = instance % getWidth();
		return i;
	}

	public int instance2j(int instance) {

		int j = 0;
		if (instance != 0)
			j = instance/getWidth();

		return j;
	}

	public int coords2Instance(int i, int j) {
		return j * getWidth() + i;
	}

	// Neighbours

	public Set<Integer> groupNeighbors(int instance, int distance) {
		return nDistanceNeighbours(instance, distance);
	}

	public boolean areNeighbors(int instanceA, int instanceB, int distance) {

		return (groupNeighbors(instanceA, distance).contains(instanceB));

	}

	// ROLL OVER

	public void setRollOver(String rollOver) {
		this.rollOver = rollOver;
	}

	public List<Integer> horizontalRollOver(int instance) {
		List<Integer> neighbors = new ArrayList<Integer>();

		int iNeighbor;
		int jNeighbor;

		int i = instance2i(instance);
		int j = instance2j(instance);

		// System.out.println(instance + " ->" + instance2i(instance,
		// getWidth())
		// + " " + instance2j(instance, getWidth()));

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

	public List<Integer> verticalRollOver(int instance) {

		List<Integer> neighbors = new ArrayList<Integer>();

		int iNeighbor;
		int jNeighbor;

		int i = instance2i(instance);
		int j = instance2j(instance);

		// System.out.println(instance + " ->" + instance2i(instance,
		// getWidth())
		// + " " + instance2j(instance, getWidth()));

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

	public List<Integer> noRollOver(int instance) {

		List<Integer> neighbors = new ArrayList<Integer>();

		int iNeighbor;
		int jNeighbor;

		int i = instance2i(instance);
		int j = instance2j(instance);

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

	private List<Integer> oneDistanceNeighbours(int instance) {

		List<Integer> neighbours = new ArrayList<Integer>();

		String option = getRollOver();

		if (option == "Vertical Roll-Over")
			neighbours = verticalRollOver(instance);
		else if (option == "No Roll-Over" || option == null)
			neighbours = noRollOver(instance);
		else if (option == "Horizontal Roll-Over")
			neighbours = horizontalRollOver(instance);

		return neighbours;

	}

	public Set<Integer> nDistanceNeighbours(int instance, int distance) {

		Set<Integer> neighbours = new TreeSet<Integer>();

		// System.err.println("\tCalculating neigh of instance " + instance
		// + " up until distance " + distance);

		if (distance <= 0)
			neighbours.add(new Integer(instance));
		else if (distance == 1)
			for (int k : oneDistanceNeighbours(instance))
				neighbours.add(new Integer(k));

		else if (distance > 1) {

			Set<Integer> frontier = this.nDistanceNeighbours(instance,
					distance - 1);

			for (Integer v : frontier)
				for (Integer k : oneDistanceNeighbours(v))
					neighbours.add(k);

		} // distance < 0 falls thru

		// System.err.println("\t results(" + instance + "," + distance + ") = "
		// + neighbours);
		return neighbours;

	}

	public String getRollOver() {
		return this.rollOver;
	}
}
