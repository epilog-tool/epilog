package pt.igc.nmd.epilog;

import java.util.List;

import org.colomoto.logicalmodel.NodeInfo;

public class Grid {

	private byte[][] grid;
	private List<NodeInfo> listNodes = null;

	/**
	 * Creates an array of arrays. An array with the same length as the total
	 * number of instances, where each term is an array with the length of the
	 * number of nodes.
	 * 
	 * @see init()
	 * @param instancesTotalNumber
	 *            number of instances
	 * @return listNodes list of components
	 */
	public Grid(int instancesTotalNumber, List<NodeInfo> listNodes) {
		
		System.out.println("building a new grid (Grid(int instancesTotalNumber, List<NodeInfo> listNodes)) with : "+ instancesTotalNumber);
		this.grid = new byte[instancesTotalNumber][];
		this.listNodes = listNodes;
		init();
	}

	/**
	 * Initializes the grid with zero value for all components at each instance.
	 */
	private void init() {
		for (int i = 0; i < this.grid.length; i++) {
			this.grid[i] = new byte[this.listNodes.size()];
			for (NodeInfo node : this.listNodes)
				this.grid[i][this.listNodes.indexOf(node)] = 0;
		}
	}

	/**
	 * Updates the value of a node in a instance.
	 * 
	 * @param instance
	 *            instance to be updated
	 * @param node
	 *            node to be updated
	 * @param value
	 *            value to update a node in a instance with
	 */
	public void setGrid(int instance, NodeInfo node, byte value) {
		this.grid[instance][listNodes.indexOf(node)] = value;
	}

	/**
	 * Returns the value of a node in a instance. If the node is not present in
	 * this instance, returns -1.
	 * 
	 * @param instance
	 *            instance to be updated
	 * @param node
	 *            node to be updated
	 * @return value value to update a node in a instance with
	 */
	public byte getValue(int instance, NodeInfo node) {
		// TODO: re-think this method, particularly exception handling
		if (listNodes.contains(node))
			return this.grid[instance][listNodes.indexOf(node)];
		else
			return -1;
	}

	/**
	 * Returns the number of instances of this grid.
	 * 
	 * @return grid length
	 */
	public int getNumberInstances() {
		return this.grid.length;
	}

	/**
	 * Returns the list of nodes of this grid.
	 * 
	 * @return nodes list
	 */
	public List<NodeInfo> getListNodes() {
		return this.listNodes;
	}

	/**
	 * Returns the grid as a Byte Matrix.
	 * 
	 * @return grid
	 */
	public byte[][] asByteMatrix() {
		return grid;
	}

	/**
	 * Returns the value of all nodes at an instance.
	 * 
	 * @param instance
	 *            instance to be updated
	 * @return array with nodes values
	 */
	public byte[] asByteArray(int instance) {
		return grid[instance];
	}

	
	
	/**
	 * Compares if two grids are equal.
	 * 
	 * @param other grid to compare with this
	 * @return true if grids are equal, false otherwise
	 */
	public boolean equals(Grid other) {
		if (this.getListNodes().equals(other.getListNodes())
				&& this.getNumberInstances() == other.getNumberInstances()) {
			for (int instance = 0; instance < this.getNumberInstances(); instance++)
				for (NodeInfo node : this.getListNodes())
					if (this.getValue(instance, node) != other.getValue(
							instance, node))
						return false;
		} else
			return false;

		return true;
	}

}
