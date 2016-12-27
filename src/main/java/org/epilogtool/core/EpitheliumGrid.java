package org.epilogtool.core;

import java.lang.reflect.InvocationTargetException;
import java.text.DecimalFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.colomoto.logicalmodel.LogicalModel;
import org.colomoto.logicalmodel.NodeInfo;
import org.colomoto.logicalmodel.perturbation.AbstractPerturbation;
import org.epilogtool.common.Tuple2D;
import org.epilogtool.core.topology.RollOver;
import org.epilogtool.core.topology.Topology;
import org.epilogtool.services.TopologyService;

public class EpitheliumGrid {
	private EpitheliumCell[][] gridEpiCell;
	private Topology topology;
	private Set<LogicalModel> modelSet;
	private Map<LogicalModel, Set<Tuple2D<Integer>>> modelPositions;
	private Map<String, Map<Byte, Integer>> compCounts;

	private EpitheliumGrid(EpitheliumCell[][] gridEpiCell, Topology topology, Set<LogicalModel> modelSet,
			Map<LogicalModel, Set<Tuple2D<Integer>>> modelPositions, Map<String, Map<Byte, Integer>> compCounts) {
		this.gridEpiCell = gridEpiCell;
		this.topology = topology;
		this.modelSet = modelSet;
		this.modelPositions = modelPositions;
		this.compCounts = compCounts;
	}

	public void updateEpitheliumGrid(int gridX, int gridY, String topologyID, RollOver rollover)
			throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException, ClassNotFoundException {
		// Create new EpiCell[][]
		EpitheliumCell[][] newGrid = new EpitheliumCell[gridX][gridY];
		for (int y = 0; y < gridY; y++) {
			for (int x = 0; x < gridX; x++) {
				if (x >= this.getX() || y >= this.getY()) {
					newGrid[x][y] = new EpitheliumCell(EmptyModel.getInstance().getModel());
				} else {
					newGrid[x][y] = this.gridEpiCell[x][y];
				}
			}
		}
		this.gridEpiCell = newGrid;
		// Create new Topology
		this.setTopology(topologyID, gridX, gridY, rollover);
		// Update model Set
		this.updateModelSet();
	}

	public EpitheliumGrid(int gridX, int gridY, String topologyID, RollOver rollover, LogicalModel m)
			throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException, ClassNotFoundException {
		this.setTopology(topologyID, gridX, gridY, rollover);
		this.modelPositions = new HashMap<LogicalModel, Set<Tuple2D<Integer>>>();
		this.gridEpiCell = new EpitheliumCell[gridX][gridY];
		for (int y = 0; y < gridY; y++) {
			for (int x = 0; x < gridX; x++) {
				this.gridEpiCell[x][y] = new EpitheliumCell(m);
				if (this.modelPositions.keySet().contains(m)) {
					this.modelPositions.get(m).add(new Tuple2D<Integer>(x, y));
				} else {
					Set<Tuple2D<Integer>> tmpList = new HashSet<Tuple2D<Integer>>();
					tmpList.add(new Tuple2D<Integer>(x, y));
					this.modelPositions.put(m, tmpList);
				}
			}
		}
		this.modelSet = new HashSet<LogicalModel>();
		this.modelSet.add(m);
		this.compCounts = new HashMap<String, Map<Byte, Integer>>();
	}

	private void setTopology(String topologyID, int gridX, int gridY, RollOver rollover)
			throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException, ClassNotFoundException {
		this.topology = TopologyService.getManager().getNewTopology(topologyID, gridX, gridY, rollover);
	}

	public int getX() {
		return this.topology.getX();
	}

	public int getY() {
		return this.topology.getY();
	}

	public Topology getTopology() {
		return this.topology;
	}

	public LogicalModel getModel(int x, int y) {
		return this.gridEpiCell[x][y].getModel();
	}

	public boolean hasModel(LogicalModel m) {
		return this.modelSet.contains(m);
	}

	public void restrictCellWithPerturbation(int x, int y) {
		this.gridEpiCell[x][y].restrictValuesWithPerturbation();
	}

	public byte[] getCellState(int x, int y) {
		return this.gridEpiCell[x][y].getState();
	}

	public byte getCellValue(int x, int y, String nodeID) {
		return this.gridEpiCell[x][y].getValue(nodeID);
	}

	public AbstractPerturbation getPerturbation(int x, int y) {
		return this.gridEpiCell[x][y].getPerturbation();
	}

	public Map<LogicalModel, Set<Tuple2D<Integer>>> getModelPositions() {
		return this.modelPositions;
	}

	public int getNodeIndex(int x, int y, String nodeID) {
		return this.gridEpiCell[x][y].getNodeIndex(nodeID);
	}

	public Set<LogicalModel> getModelSet() {
		return Collections.unmodifiableSet(this.modelSet);
	}

	public boolean hasEmptyModel(int x, int y) {
		return this.gridEpiCell[x][y].hasEmptyModel();
	}

	public void updateModelSet() {
		this.modelSet = new HashSet<LogicalModel>();
		this.modelPositions = new HashMap<LogicalModel, Set<Tuple2D<Integer>>>();
		for (int y = 0; y < this.getY(); y++) {
			for (int x = 0; x < this.getX(); x++) {
				if (this.hasEmptyModel(x, y)) {
					continue;
				}
				LogicalModel m = this.gridEpiCell[x][y].getModel();
				this.modelSet.add(m);
				Tuple2D<Integer> tmpTuple = new Tuple2D<Integer>(x, y);
				if (this.modelPositions.containsKey(m)) {
					this.modelPositions.get(m).add(tmpTuple);
				} else {
					Set<Tuple2D<Integer>> tmpList = new HashSet<Tuple2D<Integer>>();
					tmpList.add(tmpTuple);
					this.modelPositions.put(m, tmpList);
				}
			}
		}
	}

	public void setRollOver(RollOver r) {
		this.topology.setRollOver(r);
	}

	/**
	 * Sets a given cell(x,y) with model m
	 * 
	 * @param x
	 * @param y
	 * @param m
	 */
	public void setModel(int x, int y, LogicalModel m) {
		Tuple2D<Integer> tmpTuple = new Tuple2D<Integer>(x, y);
		if (this.gridEpiCell[x][y].getModel() != m) {
			if (!EmptyModel.getInstance().isEmptyModel(this.gridEpiCell[x][y].getModel())) {
				if (this.modelPositions.get(this.gridEpiCell[x][y].getModel()).contains(tmpTuple)) {
					this.modelPositions.get(this.gridEpiCell[x][y].getModel()).remove(tmpTuple);
				}
			}
		}
		gridEpiCell[x][y].setModel(m);
		if (!this.modelPositions.containsKey(m)) {
			Set<Tuple2D<Integer>> tmpList = new HashSet<Tuple2D<Integer>>();
			tmpList.add(tmpTuple);
			this.modelPositions.put(m, tmpList);
		} else {
			this.modelPositions.get(m).add(tmpTuple);
		}
	}

	public void setPerturbation(LogicalModel m, List<Tuple2D<Integer>> lTuples, AbstractPerturbation ap) {
		for (Tuple2D<Integer> tuple : lTuples) {
			if (this.gridEpiCell[tuple.getX()][tuple.getY()].getModel().equals(m)) {
				this.setPerturbation(tuple.getX(), tuple.getY(), ap);
			}
		}
	}

	public void setPerturbation(int x, int y, AbstractPerturbation ap) {
		gridEpiCell[x][y].setPerturbation(ap);
	}

	public void setCellState(int x, int y, byte[] state) {
		gridEpiCell[x][y].setState(state);
	}

	public void setCellComponentValue(int x, int y, String nodeID, byte value) {
		gridEpiCell[x][y].setValue(nodeID, value);
	}

	public EpitheliumCell cloneEpitheliumCellAt(int x, int y) {
		return this.gridEpiCell[x][y].clone();
	}

	protected void cloneEpitheliumCellTo(int x1, int y1, int x2, int y2) {
		EpitheliumCell epiCell = this.cloneEpitheliumCellAt(x1, y1);
		this.gridEpiCell[x2][y2] = epiCell;
	}

	protected int emptyModelNumber() {
		int gridSize = this.getX() * this.getY();
		int cellNumber = 0;
		for (LogicalModel m : this.modelPositions.keySet()) {
			cellNumber += this.modelPositions.get(m).size();
		}
		return gridSize - cellNumber;
	}

	public String hashGrid() {
		String hash = "";
		for (int y = 0; y < this.getY(); y++) {
			for (int x = 0; x < this.getX(); x++) {
				hash += this.gridEpiCell[x][y].hashState();
			}
		}
		return hash;
	}

	public String toString() {
		String s = "";
		for (int y = 0; y < this.getY(); y++) {
			s += (y + 1) + "|";
			for (int x = 0; x < this.getX(); x++) {
				byte[] currState = this.getCellState(x, y);
				for (int i = 0; i < currState.length; i++) {
					s += currState[i];
				}
				s += "|";
			}
			s += "\n";
		}
		return s;
	}

	public boolean equals(Object a) {
		if (a == null || !(a instanceof EpitheliumGrid))
			return false;
		if (a == this)
			return true;
		EpitheliumGrid o = (EpitheliumGrid) a;

		if (!this.topology.equals(o.topology))
			return false;
		for (int y = 0; y < this.getY(); y++) {
			for (int x = 0; x < this.getX(); x++) {
				if (!this.gridEpiCell[x][y].equals(o.gridEpiCell[x][y]))
					return false;
			}
		}
		return true;
	}

	public EpitheliumGrid clone() {
		EpitheliumCell[][] newGrid = new EpitheliumCell[this.getX()][this.getY()];
		for (int y = 0; y < this.getY(); y++) {
			for (int x = 0; x < this.getX(); x++) {
				newGrid[x][y] = this.gridEpiCell[x][y].clone();
			}
		}
		Topology newTop = this.topology.clone();
		Set<LogicalModel> newModelSet = new HashSet<LogicalModel>(this.modelSet);
		Map<LogicalModel, Set<Tuple2D<Integer>>> newModelPositions = new HashMap<LogicalModel, Set<Tuple2D<Integer>>>(
				this.modelPositions);
		Map<String, Map<Byte, Integer>> newCompCounts = new HashMap<String, Map<Byte, Integer>>(this.compCounts);
		return new EpitheliumGrid(newGrid, newTop, newModelSet, newModelPositions, newCompCounts);
	}

	public String getPercentage(String nodeID) {
		DecimalFormat perc = new DecimalFormat();
		perc.setMaximumFractionDigits(2);

		String output = "";
		int nCells = this.getX() * this.getY();
		for (byte val : this.compCounts.get(nodeID).keySet()) {
			if (val == 0)
				continue;
			float count = this.compCounts.get(nodeID).get(val);
			float percentage = (count / nCells) * 100;
			output = output + "(" + val + " : " + perc.format(percentage) + "%)";
		}
		return output;
	}

	public void updateNodeValueCounts() {
		// Init component counts
		this.compCounts.clear();
		for (int x = 0; x < this.getX(); x++) {
			for (int y = 0; y < this.getY(); y++) {
				LogicalModel model = this.getModel(x, y);
				for (NodeInfo node : model.getNodeOrder()) {
					String nodeID = node.getNodeID();
					byte val = this.getCellValue(x, y, nodeID);
					if (!this.compCounts.containsKey(nodeID)) {
						this.compCounts.put(nodeID, new HashMap<Byte, Integer>());
					}
					int count = 1;
					if (this.compCounts.get(nodeID).containsKey(val)) {
						count += this.compCounts.get(nodeID).get(val);
					}
					this.compCounts.get(nodeID).put(val, count);
				}
			}
		}
	}
}
