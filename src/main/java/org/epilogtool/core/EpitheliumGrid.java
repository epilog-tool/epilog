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

import org.colomoto.logicalmodel.LogicalModel;
import org.colomoto.logicalmodel.NodeInfo;
import org.colomoto.logicalmodel.perturbation.AbstractPerturbation;
import org.epilogtool.common.RandomFactory;
import org.epilogtool.common.Tuple2D;
import org.epilogtool.core.cellDynamics.CellularEvent;
import org.epilogtool.core.cellDynamics.EpitheliumCellConnections;
import org.epilogtool.core.cellDynamics.GridTopology;
import org.epilogtool.core.topology.RollOver;
import org.epilogtool.core.topology.Topology;
import org.epilogtool.services.TopologyService;

public class EpitheliumGrid {
	private EpitheliumCell[][] gridEpiCell;
	private Topology topology;
	private Set<LogicalModel> modelSet;
	private Map<LogicalModel, Set<Tuple2D<Integer>>> modelPositions;
	private Map<String, Map<Byte, Integer>> compCounts;
	// TODO:maybe this should not be here, but we need to store dynamic
	// properties of the grid
	private GridTopology gridTopology;
	private EpitheliumCellConnections epiCellConnections;

	private EpitheliumGrid(EpitheliumCell[][] gridEpiCell, Topology topology, Set<LogicalModel> modelSet,
			Map<LogicalModel, Set<Tuple2D<Integer>>> modelPositions, Map<String, Map<Byte, Integer>> compCounts,
			GridTopology gridTopology, EpitheliumCellConnections epiCellConnections) {
		this.gridEpiCell = gridEpiCell;
		this.topology = topology;
		this.modelSet = modelSet;
		this.modelPositions = modelPositions;
		this.compCounts = compCounts;
		this.gridTopology = gridTopology;
		this.epiCellConnections = epiCellConnections;
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
				// root id is number of columns times row index plus (column
				// index + one)
				if (!EmptyModel.getInstance().isEmptyModel(m)) {
					this.gridEpiCell[x][y].getID().setRoot(this.getY() * y + x);
				}
				if (this.modelPositions.keySet().contains(m)) {
					this.modelPositions.get(m).add(new Tuple2D<Integer>(x, y));
				} else {
					Set<Tuple2D<Integer>> tmpSet = new HashSet<Tuple2D<Integer>>();
					tmpSet.add(new Tuple2D<Integer>(x, y));
					this.modelPositions.put(m, tmpSet);
				}
			}
		}
		this.modelSet = new HashSet<LogicalModel>();
		this.modelSet.add(m);
		this.compCounts = new HashMap<String, Map<Byte, Integer>>();
		this.gridTopology = new GridTopology(this);
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

	public byte getCellComponentValue(int x, int y, String nodeID) {
		return this.gridEpiCell[x][y].getNodeValue(nodeID);
	}

	public Set<LogicalModel> getModelSet() {
		return Collections.unmodifiableSet(this.modelSet);
	}

	public CellularEvent getCellEvent(int x, int y) {
		return this.gridEpiCell[x][y].getCellEvent();
	}

	public EpitheliumCellIdentifier getCellID(int x, int y) {
		return this.gridEpiCell[x][y].getID();
	}

	public boolean isEmptyCell(int x, int y) {
		return this.gridEpiCell[x][y].isEmptyCell();
	}

	public boolean hasEmptyModel(int x, int y) {
		return this.gridEpiCell[x][y].hasEmptyModel();
	}

	public void updateModelSet() {
		this.modelSet.clear();
		this.modelPositions.clear();
		this.epiCellConnections = new EpitheliumCellConnections();
		for (int y = 0; y < this.getY(); y++) {
			for (int x = 0; x < this.getX(); x++) {
				if (this.isEmptyCell(x, y)) {
					continue;
				}
				LogicalModel m = this.gridEpiCell[x][y].getModel();
				this.modelSet.add(m);
				Tuple2D<Integer> tmpTuple = new Tuple2D<Integer>(x, y);
				if (this.gridEpiCell[x][y].getID().getRoot() == -1) {
					this.gridEpiCell[x][y].getID().setRoot(this.getY() * y + x);
				}
				this.epiCellConnections.addCellIdentifier(this.gridEpiCell[x][y].getID());
				if (!this.modelPositions.containsKey(m)) {
					this.modelPositions.put(m, new HashSet<Tuple2D<Integer>>());
				}
				this.modelPositions.get(m).add(tmpTuple);
			}
		}
		// TODO: this should be dynamically updated
		this.gridTopology = new GridTopology(this);
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
		if (!EmptyModel.getInstance().isEmptyModel(m)) {
			this.gridEpiCell[x][y].getID().setRoot(this.getY() * y + x);
			if (!this.modelPositions.containsKey(m)) {
				this.modelPositions.put(m, new HashSet<Tuple2D<Integer>>());
			}
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

	public void setCellEvent(int x, int y, CellularEvent event) {
		this.gridEpiCell[x][y].setCellEvent(event);
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

	public int emptyModelNumber() {
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
		if (this.epiCellConnections == null) {
			this.initEpiCellConnections();
		}
		Topology newTop = this.topology.clone();
		Set<LogicalModel> newModelSet = new HashSet<LogicalModel>(this.modelSet);
		Map<LogicalModel, Set<Tuple2D<Integer>>> newModelPositions = new HashMap<LogicalModel, Set<Tuple2D<Integer>>>(
				this.modelPositions);
		GridTopology newEpiTop = this.gridTopology.clone();
		EpitheliumCellConnections newEpiCellConnections = this.epiCellConnections.clone();
		Map<String, Map<Byte, Integer>> newCompCounts = new HashMap<String, Map<Byte, Integer>>(this.compCounts);
		return new EpitheliumGrid(newGrid, newTop, newModelSet, newModelPositions, newCompCounts, newEpiTop,
				newEpiCellConnections);
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

	// TODO:this should not be here
	public EpitheliumCellConnections getEpiCellConnections() {
		return this.epiCellConnections;
	}

	public void divideCell(int x1, int y1, int x2, int y2) {
		List<EpitheliumCell> daughterList = this.gridEpiCell[x1][y1].daughterCells();

		EpitheliumCell daughterCell1 = daughterList.get(0);
		this.setEpitheliumCell(x1, y1, daughterCell1);

		EpitheliumCell daughterCell2 = daughterList.get(1);
		this.setEpitheliumCell(x2, y2, daughterCell2);
		this.modelPositions.get(this.gridEpiCell[x1][y1].getModel()).add(new Tuple2D<Integer>(x2, y2));
		this.modelPositions.get(this.gridEpiCell[x1][y1].getModel()).add(new Tuple2D<Integer>(x1, y1));
	}

	public void removeCell(int x, int y) {
		Tuple2D<Integer> tmpTuple = new Tuple2D<Integer>(x, y);
		EpitheliumCellIdentifier cellID = this.getCellID(x, y);
		this.modelPositions.get(this.getModel(x, y)).remove(tmpTuple);
		this.epiCellConnections.delete(cellID);
		this.setModel(x, y, EmptyModel.getInstance().getModel());
	}

	private Set<EpitheliumCellIdentifier> getCellIDSet(Set<Tuple2D<Integer>> positionSet) {
		Set<EpitheliumCellIdentifier> cellIDSet = new HashSet<EpitheliumCellIdentifier>();
		for (Tuple2D<Integer> position : positionSet) {
			if (!this.isEmptyCell(position.getX(), position.getY())) {
				cellIDSet.add(this.getCellID(position.getX(), position.getY()));
			}
		}
		return cellIDSet;
	}

	private Set<EpitheliumCellIdentifier> getContactsIDSet(Tuple2D<Integer> position) {
		return this.getCellIDSet(this.getTopology().getPositionNeighbours(position.getX(), position.getY(), 1));
	}

	private void shiftCellPosition(Tuple2D<Integer> initPos, Tuple2D<Integer> endPos) {
		LogicalModel initModel = this.getModel(initPos.getX(), initPos.getY());
		LogicalModel endModel = this.getModel(endPos.getX(), endPos.getY());

		if (this.isEmptyCell(initPos.getX(), initPos.getY())) {
			// if the initial position is empty its epithelium shrinkage
			this.modelPositions.get(endModel).remove(endPos);
			this.gridEpiCell[endPos.getX()][endPos.getY()] = new EpitheliumCell(initModel);
			return;
		}
		this.modelPositions.get(initModel).add(endPos);
		if (!this.isEmptyCell(endPos.getX(), endPos.getY()) && !initModel.equals(endModel)) {
			// if the final position is empty it is epithelium growth
			this.modelPositions.get(endModel).remove(endPos);
		}
		this.gridEpiCell[endPos.getX()][endPos.getY()] = this.gridEpiCell[initPos.getX()][initPos.getY()].clone();
	}

	public void shiftCells(List<Tuple2D<Integer>> path) {
		for (int index = 1; index < path.size(); index++) {
			this.shiftCellPosition(path.get(index), path.get(index - 1));
		}
	}

	public void updateEpiCellConnections(List<Tuple2D<Integer>> path) {
		Collections.reverse(path);
		Tuple2D<Integer> motherPos = path.get(0);
		Tuple2D<Integer> daughterPos = path.get(1);
		Map<EpitheliumCellIdentifier, Set<EpitheliumCellIdentifier>> daughters2neighboursMap = new HashMap<EpitheliumCellIdentifier, Set<EpitheliumCellIdentifier>>();
		daughters2neighboursMap.put(this.getCellID(motherPos.getX(), motherPos.getY()),
				this.getContactsIDSet(motherPos));
		daughters2neighboursMap.put(this.getCellID(daughterPos.getX(), daughterPos.getY()),
				this.getContactsIDSet(daughterPos));
		this.epiCellConnections.replaceMother(daughters2neighboursMap);

		for (int index = 2; index < path.size(); index++) {
			Tuple2D<Integer> currPosition = path.get(index);
			EpitheliumCellIdentifier cellID = this.getCellID(currPosition.getX(), currPosition.getY());
			Set<EpitheliumCellIdentifier> connectedSet = this.getContactsIDSet(currPosition);
			this.epiCellConnections.retain(cellID, connectedSet);
		}
	}

	public void initEpiCellConnections() {
		this.epiCellConnections = new EpitheliumCellConnections();
		for (int x = 0; x < this.getX(); x++) {
			for (int y = 0; y < this.getY(); y++) {
				if (!this.isEmptyCell(x, y)) {
					this.epiCellConnections.addCellIdentifier(this.getCellID(x, y));
				}
			}
		}
	}

	// TODO: division method should not be here
	public List<Tuple2D<Integer>> divisionPath(Tuple2D<Integer> initPosition) {
		List<Tuple2D<Integer>> path = new ArrayList<Tuple2D<Integer>>();
		Tuple2D<Integer> endPosition = this.gridTopology.getEmptyPosition(initPosition);
		path.add(initPosition);
		Tuple2D<Integer> currPosition = initPosition.clone();
		int distance2end;
		List<Tuple2D<Integer>> possiblePaths = new ArrayList<Tuple2D<Integer>>();

		while (!this.isEmptyCell(currPosition.getX(), currPosition.getY())) {
			distance2end = this.gridTopology.distance(currPosition, endPosition);
			Set<Tuple2D<Integer>> contactSet = this.getTopology().getPositionNeighbours(currPosition.getX(),
					currPosition.getY(), 1);
			for (Tuple2D<Integer> contact : contactSet) {
				if (path.contains(contact)) {
					continue;
				}
				if (this.gridTopology.distance(contact, endPosition) < distance2end) {
					possiblePaths.add(contact);
				}
			}
			currPosition = possiblePaths.get(RandomFactory.getInstance().nextInt(possiblePaths.size()));
			path.add(currPosition);
			possiblePaths.clear();
		}
		this.gridTopology.addCell(path.get(path.size() - 1));
		Collections.reverse(path);
		possiblePaths.clear();
		return path;
	}

}
