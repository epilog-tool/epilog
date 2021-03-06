package org.epilogtool.core;

import java.lang.reflect.InvocationTargetException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.colomoto.biolqm.LogicalModel;
import org.colomoto.biolqm.NodeInfo;
import org.colomoto.biolqm.modifier.perturbation.LogicalModelPerturbation;
import org.epilogtool.common.Tuple2D;
import org.epilogtool.core.topology.RollOver;
import org.epilogtool.core.topology.Topology;
import org.epilogtool.project.Project;
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

	public void editEpitheliumGrid(int gridX, int gridY, String topologyID, RollOver rollover)
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
		// Update grid
		this.updateGrid();
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
		this.compPercents = new HashMap<String, Map<Byte, Float>>();// ptgm
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

	public Set<String> getComponents() {
		Set<String> sComps = new HashSet<String>();
		for (LogicalModel m : this.modelSet) {
			for (NodeInfo n : m.getComponents()) {
				sComps.add(n.getNodeID());
			}
		}
		return sComps;
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

	public LogicalModelPerturbation getPerturbation(int x, int y) {
		return this.gridEpiCell[x][y].getPerturbation();
	}

	public Map<LogicalModel, Set<LogicalModelPerturbation>> getAppliedPerturb() {
		Map<LogicalModel, Set<LogicalModelPerturbation>> map = new HashMap<LogicalModel, Set<LogicalModelPerturbation>>();
		for (int y = 0; y < this.getY(); y++) {
			for (int x = 0; x < this.getX(); x++) {
				LogicalModelPerturbation ap = this.getPerturbation(x, y);
				if (ap != null) {
					LogicalModel m = this.getModel(x, y);
					if (!map.containsKey(m))
						map.put(m, new HashSet<LogicalModelPerturbation>());
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

	public void updateGrid() {
		this.updateModelSet();
		this.updateNodeValueCounts();
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

	public void setPerturbation(List<Tuple2D<Integer>> lTuples, LogicalModelPerturbation ap) {
		for (Tuple2D<Integer> tuple : lTuples) {
			LogicalModel model = this.gridEpiCell[tuple.getX()][tuple.getY()].getModel();
			if (apBelongsToModel(model, ap)) {
				this.setPerturbation(tuple.getX(), tuple.getY(), ap);
			}
		}
	}

	private boolean apBelongsToModel(LogicalModel model, LogicalModelPerturbation ap) {
		// TODO Auto-generated method stub

		String sExpr = ap.getStringRepresentation();
		String[] saExpr = sExpr.split(", ");
		List<NodeInfo> nodes = new ArrayList<NodeInfo>();

		for (String sTmp : saExpr) {
			String name = sTmp.split("%")[0];
			// System.out.println("EpitheliumGrid: name -> " + name);
			NodeInfo node = Project.getInstance().getProjectFeatures().getNodeInfo(name);
			nodes.add(node);
			// System.out.println("EpitheliumGrid: " + node);
		}

		if (nodes.size() == 1) {
			NodeInfo node = nodes.get(0);
			if (model.getComponents().contains(node))
				return true;
		} else if (nodes.size() > 1) {
			for (NodeInfo node : nodes) {
				if (!model.getComponents().contains(node))
					return false;
			}
		}
		return false;
	}

	public void setPerturbation(int x, int y, LogicalModelPerturbation ap) {
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
			String line = "";
			for (int x = 0; x < this.getX(); x++) {
				if (line != "")
					line += ",";
				byte[] currState = this.getCellState(x, y);
				for (int i = 0; i < currState.length; i++) {
					line += currState[i];
				}
			}
			s += line + "\n";
		}
		return s;
	}

	public boolean equals(Object a) {
		if (a == null || !(a instanceof EpitheliumGrid)) {
			return false;
		}
		if (a == this) {
			return true;
		}
		EpitheliumGrid o = (EpitheliumGrid) a;
		if (!this.topology.equals(o.topology)) {
			return false;
		}
		for (int y = 0; y < this.getY(); y++) {
			for (int x = 0; x < this.getX(); x++) {
				if (!this.gridEpiCell[x][y].equals(o.gridEpiCell[x][y])) {
					return false;
				}
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
				for (NodeInfo node : model.getComponents()) {
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

	public Set<Tuple2D<Integer>> getPositionNeighbours(
			Map<Tuple2D<Integer>, Map<Boolean, Set<Tuple2D<Integer>>>> relativeNeighboursCache,
			Tuple2D<Integer> rangeList_aux, Tuple2D<Integer> rangePair, int minSigDist, int x, int y) {
		if (!relativeNeighboursCache.containsKey(rangeList_aux)) {
			Map<Boolean, Set<Tuple2D<Integer>>> neighboursOutskirts = new HashMap<Boolean, Set<Tuple2D<Integer>>>();
			neighboursOutskirts.put(true,
					this.getTopology().getRelativeNeighbours(true, rangeList_aux.getX(), rangeList_aux.getY()));
			neighboursOutskirts.put(false,
					this.getTopology().getRelativeNeighbours(false, rangeList_aux.getX(), rangeList_aux.getY()));
			relativeNeighboursCache.put(rangeList_aux, neighboursOutskirts);
		}

		if (!relativeNeighboursCache.containsKey(rangePair)) {
			Map<Boolean, Set<Tuple2D<Integer>>> relativeNeighbours = new HashMap<Boolean, Set<Tuple2D<Integer>>>();
			relativeNeighbours.put(true,
					this.getTopology().getRelativeNeighbours(true, rangePair.getX(), rangePair.getY()));
			relativeNeighbours.put(false,
					this.getTopology().getRelativeNeighbours(false, rangePair.getX(), rangePair.getY()));
			relativeNeighboursCache.put(rangePair, relativeNeighbours);
		}

		boolean even = this.getTopology().isEven(x, y);

		Set<Tuple2D<Integer>> positionNeighbours = this.getTopology().getPositionNeighbours(x, y,
				relativeNeighboursCache.get(rangePair).get(even));
		Set<Tuple2D<Integer>> neighboursOutskirts = this.getTopology().getPositionNeighbours(x, y,
				relativeNeighboursCache.get(rangeList_aux).get(even));

		if (minSigDist > 0) {
			positionNeighbours.removeAll(neighboursOutskirts);
		}

		return positionNeighbours;
	}
}
