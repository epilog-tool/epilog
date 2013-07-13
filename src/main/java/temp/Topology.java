package temp;

/**
 * Represents the topological relationships between Logical Regulatory Modules
 * being composed.
 * 
 * @author Nuno D. Mendes
 */

public class Topology {

	private int _numberInstances = 0;
	private boolean neighbourhoodRelation[][];

	/**
	 * The topology
	 * 
	 * @param numberInstances
	 *            the number of identical modules
	 */
	public Topology(int numberInstances) {
		this.set_numberInstances(numberInstances);
		initNeighbours();
	}

	/**
	 * @return Number of module instances in the Topology.
	 */
	public int getNumberInstances() {
		return _numberInstances;
	}

	/**
	 * 
	 * Specifies that module neighindex2 is a neighbour of module neighindex1,
	 * but not the other way around.
	 * 
	 * @param neighindex1
	 *            indicates the index of the first module
	 * 
	 * @param neighindex2
	 *            indicates the index of the second module
	 */
	public void addNeighbour(int neighindex1, int neighindex2) {
		if (neighindex1 < _numberInstances && neighindex2 < _numberInstances)
			neighbourhoodRelation[neighindex1][neighindex2] = true;
	}

	/**
	 * @param neighindex1
	 *            indicates the index of the first module
	 * 
	 * @param neighindex2
	 *            indicates the index of the second module
	 */
	public void removeNeighbour(int neighindex1, int neighindex2) {
		if (neighindex1 < _numberInstances && neighindex2 < _numberInstances)
			neighbourhoodRelation[neighindex1][neighindex2] = false;
	}

	/**
	 * 
	 * Method determining whether one module in neighbour of another in the
	 * defined Topology
	 * 
	 * @param neighindex1
	 *            Index of first module (starting in 0)
	 * @param neighindex2
	 *            Index of second module (starting in 0)
	 * @return TRUE if the first module is a neighbour of the second module,
	 *         FALSE otherwise
	 */
	public boolean areNeighbours(int neighindex1, int neighindex2) {
		int maxIndex = neighbourhoodRelation.length - 1;
		if (neighindex1 > maxIndex || neighindex2 > maxIndex)
			return false;
		return neighbourhoodRelation[neighindex1][neighindex2];
	}

	/**
	 * @param index
	 *            A module index
	 * @return TRUE if the module has neighbours, FALSE otherwise
	 */
	public boolean hasNeighbours(int index) {
		boolean result = false;
		if (index >= _numberInstances)
			return false;

		for (int i = 0; i < neighbourhoodRelation.length; i++) {
			if (neighbourhoodRelation[index][i] == true) {
				result = true;
				break;
			}
		}
		return result;
	}

	/**
	 * @param index
	 *            A module index
	 * 
	 * @return the number of neighbours of the given module
	 */

	public int getNumberNeighbours(int index) {
		int count = 0;
		if (index >= _numberInstances)
			return 0;
		for (int i = 0; i < neighbourhoodRelation.length; i++)
			if (neighbourhoodRelation[index][i] == true)
				count++;

		return count;

	}

	/**
	 * @param numberInstances
	 *            the new number of module instances
	 */
	private void set_numberInstances(int numberInstances) {
		this._numberInstances = numberInstances;
	}

	/**
	 * Sets up the adjacency matrix representing the neighbourhood relation
	 * between Module instances
	 */
	private void initNeighbours() {
		int i, j;
		neighbourhoodRelation = new boolean[_numberInstances][_numberInstances];
		for (i = 0; i < _numberInstances; i++) {
			for (j = 0; j < _numberInstances; j++) {
				neighbourhoodRelation[i][j] = false;
			}
		}

	}
}
