package org.epilogtool.core;

import java.lang.reflect.InvocationTargetException;
import java.text.DecimalFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.colomoto.biolqm.LogicalModel;
import org.colomoto.biolqm.NodeInfo;
import org.colomoto.biolqm.modifier.perturbation.AbstractPerturbation;
import org.epilogtool.common.Tuple2D;
import org.epilogtool.core.topology.RollOver;
import org.epilogtool.core.topology.Topology;
import org.epilogtool.services.TopologyService;

public class EpitheliumGrid {
	private EpitheliumCell[][] gridEpiCell;
	private Topology topology;
	private Set<LogicalModel> modelSet;
	private Map<String, Map<Byte, Integer>> compCounts;
	private Map<String, Map<Byte, Float>> compPercents;

	private EpitheliumGrid(EpitheliumCell[][] gridEpiCell, Topology topology, Set<LogicalModel> modelSet,
			Map<String, Map<Byte, Integer>> compCounts, Map<String, Map<Byte, Float>> compPercents) {
		this.gridEpiCell = gridEpiCell;
		this.topology = topology;
		this.modelSet = modelSet;
		this.compCounts = compCounts;
		this.compPercents = compPercents;
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
		this.gridEpiCell = new EpitheliumCell[gridX][gridY];
		for (int y = 0; y < gridY; y++) {
			for (int x = 0; x < gridX; x++) {
				this.gridEpiCell[x][y] = new EpitheliumCell(m);
			}
		}
		this.modelSet = new HashSet<LogicalModel>();
		this.modelSet.add(m);
		this.compCounts = new HashMap<String, Map<Byte, Integer>>();
		this.compPercents = new HashMap<String, Map<Byte, Float>>();
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

	public void restrictGridWithPerturbations() {
		for (int y = 0; y < this.getY(); y++) {
			for (int x = 0; x < this.getX(); x++) {
				this.restrictCellWithPerturbation(x, y);
			}
		}
	}

	private void restrictCellWithPerturbation(int x, int y) {
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

	public Map<LogicalModel, Set<AbstractPerturbation>> getAppliedPerturb() {
		Map<LogicalModel, Set<AbstractPerturbation>> map = new HashMap<LogicalModel, Set<AbstractPerturbation>>();
		for (int y = 0; y < this.getY(); y++) {
			for (int x = 0; x < this.getX(); x++) {
				AbstractPerturbation ap = this.getPerturbation(x, y);
				if (ap != null) {
					LogicalModel m = this.getModel(x, y);
					if (!map.containsKey(m))
						map.put(m, new HashSet<AbstractPerturbation>());
					map.get(m).add(ap);
				}
			}
		}
		return map;
	}

	public int getNodeIndex(int x, int y, String nodeID) {
		return this.gridEpiCell[x][y].getNodeIndex(nodeID);
	}

	public byte getCellComponentValue(int x, int y, String nodeID) {
		return this.gridEpiCell[x][y].getNodeValue(nodeID);
	}

	public Set<LogicalModel> getModelSet() {
		return Collections.unmodifiableSet(this.modelSet);
	}

	public boolean isEmptyCell(int x, int y) {
		return this.gridEpiCell[x][y].isEmptyCell();
	}

	public boolean hasEmptyModel(int x, int y) {
		return this.gridEpiCell[x][y].hasEmptyModel();
	}

	public void updateModelSet() {
		this.modelSet.clear();
		for (int y = 0; y < this.getY(); y++) {
			for (int x = 0; x < this.getX(); x++) {
				if (this.isEmptyCell(x, y)) {
					continue;
				}
				this.modelSet.add(this.gridEpiCell[x][y].getModel());
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
		gridEpiCell[x][y].setModel(m);
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

	public EpitheliumCell getEpitheliumCell(int x, int y) {
		return this.gridEpiCell[x][y];
	}

	public void setEpitheliumCell(int x, int y, EpitheliumCell epiCell) {
		this.gridEpiCell[x][y] = epiCell;
	}

	protected void cloneEpitheliumCellTo(int x1, int y1, int x2, int y2) {
		EpitheliumCell epiCell = this.cloneEpitheliumCellAt(x1, y1);
		this.gridEpiCell[x2][y2] = epiCell;
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
		if (a == null || !(a instanceof EpitheliumGrid)) {
			System.out.println("EpiGrid.equals: Not a grid");
			return false;
		}
		if (a == this) {
			System.out.println("EpiGrid.equals: Same ref");
			return true;
		}
		EpitheliumGrid o = (EpitheliumGrid) a;
		if (!this.topology.equals(o.topology)) {
			System.out.println("EpiGrid.equals: =/= topology");
			return false;
		}
		for (int y = 0; y < this.getY(); y++) {
			for (int x = 0; x < this.getX(); x++) {
				if (!this.gridEpiCell[x][y].equals(o.gridEpiCell[x][y])) {
					System.out.println("EpiGrid.equals: =/= epicell (" +x+","+ y+")");
					return false;
				}
			}
		}
		System.out.println("EpiGrid.equals: same grid");
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
		Map<String, Map<Byte, Integer>> newCompCounts = new HashMap<String, Map<Byte, Integer>>(this.compCounts);
		Map<String, Map<Byte, Float>> newCompPercents = new HashMap<String, Map<Byte, Float>>(this.compPercents);
		return new EpitheliumGrid(newGrid, newTop, newModelSet, newCompCounts, newCompPercents);
	}

	public String getPercentage(String nodeID) {
		DecimalFormat perc = new DecimalFormat();
		perc.setMaximumFractionDigits(2);
		String output = "";
		for (byte val : this.compCounts.get(nodeID).keySet()) {
			if (val == 0)
				continue;
			float percentage = this.getPercentage(nodeID, val);
			output = output + "(" + val + " : " + perc.format(percentage) + "%)";
		}
		return output;
	}

	public float getPercentage(String nodeID, byte value) {
		float count = 0;
		if (this.compCounts.get(nodeID).containsKey(value)) {
			count = this.compCounts.get(nodeID).get(value);
		}
		int nCells = this.getX() * this.getY();
		float percentage = (count / nCells) * 100;
		return percentage;
	}

	public void updateNodeValueCounts() {
		// Compute component/value counts
		this.compCounts.clear();
		for (int x = 0; x < this.getX(); x++) {
			for (int y = 0; y < this.getY(); y++) {
				LogicalModel model = this.getModel(x, y);
				for (NodeInfo node : model.getNodeOrder()) {
					String nodeID = node.getNodeID();
					byte val = this.getCellValue(x, y, nodeID);
					if (!this.compCounts.containsKey(nodeID)) {
						this.compCounts.put(nodeID, new HashMap<Byte, Integer>());
						for (byte i = 0; i <= node.getMax(); i++) {
							this.compCounts.get(nodeID).put(i, 0);
						}
					}
					int count = this.compCounts.get(nodeID).get(val) + 1;
					this.compCounts.get(nodeID).put(val, count);
				}
			}
		}
		// Compute corresponding percentages
		this.compPercents.clear();
		int nCells = this.getX() * this.getY();
		for (String nodeID : this.compCounts.keySet()) {
			this.compPercents.put(nodeID, new HashMap<Byte, Float>());
			for (Byte value : this.compCounts.get(nodeID).keySet()) {
				float count = this.compCounts.get(nodeID).get(value);
				float percent = (count / nCells) * 100;
				this.compPercents.get(nodeID).put(value, percent);
			}
		}
	}
}
