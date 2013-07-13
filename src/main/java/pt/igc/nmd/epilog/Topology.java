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

	/**
	 * Defines the topology of the epithelium model.
	 * 
	 * @param width width of the hexagons grid
	 * @param height height of the hexagons grid
	 * 
	 */
	public Topology(int width, int height) {
		super();
		// width = (width % 2 == 0) ? width : width + 1;
		// height = (height % 2 == 0) ? height : height + 1;
		this.width = width;
		this.height = height;
		// this.rollOver = rollOver;

	}

	/**
	 * Sets a new height for the hexagons grid.
	 * 
	 * @param height new height
	 * 
	 */
	public void setHeight(int height) {
		this.height = height;
	}

	/**
	 * Sets a new width for the hexagons grid.
	 * 
	 * @param width new width
	 * 
	 */
	public void setWidth(int width) {
		this.width = width;
	}

	
	/**
	 * Returns the grid's width.
	 * 
	 * @return width
	 * 
	 */
	public int getWidth() {
		return this.width;
	}

	/**
	 * Returns the grid's height.
	 * 
	 * @return height
	 * 
	 */
	public int getHeight() {
		return this.height;
	}

	
	/**
	 * Returns the number of instances.
	 * 
	 * @return number of instances
	 * 
	 */
	public int getNumberInstances() {
		return this.height * this.width;
	}

	/* Methods to transform instance index to coordinates and vice-versa */
	
	/**
	 * Translates and instance number into a corresponding coordinate of the x-axis
	 * 
	 * @param instance
	 * @return x axis coordinate
	 * 
	 */
	public int instance2i(int instance) {

		int i = instance % getWidth();
		return i;
	}

	
	/**
	 * Translates and instance number into a corresponding coordinate of the y-axis
	 * 
	 * @param instance
	 * @return y axis coordinate
	 * 
	 */
	public int instance2j(int instance) {

		int j = 0;
		if (instance != 0)
			j = instance/getWidth();

		return j;
	}

	
	/**
	 * Translates into an instance number the x and y coordinates.
	 * 
	 * @param i x coordinate
	 * @param j y coordinate
	 * @return instance number
	 * 
	 */
	public int coords2Instance(int i, int j) {
		return j * getWidth() + i;
	}

	// Neighbours

	
	/**
	 * Calls the iterative method to determine the set of neighours at a  distances
	 * 
	 * @param instance instance that has neighbours
	 * @param distance neighbours distance
	 * @return set of neighbours
	 *@see nDistanceNeighbours(int instance, int distance) 
	 * 
	 */
	public Set<Integer> groupNeighbors(int instance, int distance) {
		return nDistanceNeighbours(instance, distance);
	}

	
	/**
	 * Determines if two instances are neighbours at a distance
	 * 
	 * @param instanceA instance to compare
	 * @param instanceB instance to compare
	 * @return true if they are neighbours, false otherwise
	 * 
	 */
	public boolean areNeighbors(int instanceA, int instanceB, int distance) {

		return (groupNeighbors(instanceA, distance).contains(instanceB));

	}

	// ROLL OVER

	public void setRollOver(String rollOver) {
		this.rollOver = rollOver;
	}

	
	/**
	 * Determines the list of neighbours with horizontal roll over
	 * 
	 * @param instance instance that has neighbours
	 * @return list of neighbours
	 *@see nDistanceNeighbours(int instance, int distance) 
	 * 
	 */
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

	/**
	 * Determines the list of neighbours with vertical roll over
	 * 
	 * @param instance instance that has neighbours
	 * @return list of neighbours
	 *@see nDistanceNeighbours(int instance, int distance) 
	 * 
	 */
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
	
	/**
	 * Determines the list of neighbours with no roll over
	 * 
	 * @param instance instance that has neighbours
	 * @return list of neighbours
	 *@see nDistanceNeighbours(int instance, int distance) 
	 * 
	 */
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

	/**
	 * Calls the iterative method to determine the list of neighours at distance 1
	 * 
	 * @param instance instance that has neighbours
	 * @return list of neighbours
	 *@see nDistanceNeighbours(int instance, int distance) 
	 * 
	 */
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
	
	/**
	 * Determines neighbours at a distance of an instance
	 * @param instance instance that has neighbours
	 * @param distance neighbours distance
	 * @return set of neighbours
	 *
	 * 
	 */

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

	/**
	 * Returns the active roll-over option.
	 * 
	 * @return roll over option as string
	 * 
	 */
	public String getRollOver() {
		return this.rollOver;
	}
}
