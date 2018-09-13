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
import org.colomoto.biolqm.modifier.perturbation.AbstractPerturbation;
import org.epilogtool.common.Tuple2D;
import org.epilogtool.common.Txt;
import org.epilogtool.core.cell.AbstractCell;
import org.epilogtool.core.cell.CellFactory;
import org.epilogtool.core.cell.EmptyCell;
import org.epilogtool.core.cell.LivingCell;
import org.epilogtool.core.topology.RollOver;
import org.epilogtool.core.topology.Topology;
import org.epilogtool.project.Project;
import org.epilogtool.services.TopologyService;

public class EpitheliumGrid {
	private AbstractCell[][] gridCells;
	private Topology topology;
	private Set<LogicalModel> modelSet;
	private Map<String, Map<Byte, Integer>> compCounts;
	private Map<String, Map<Byte, Float>> compPercents;

	private EpitheliumGrid(AbstractCell[][] gridEpiCell, Topology topology, Set<LogicalModel> modelSet,
			Map<String, Map<Byte, Integer>> compCounts, Map<String, Map<Byte, Float>> compPercents) {
		
		this.gridCells = gridEpiCell;
		this.topology = topology;
		this.modelSet = modelSet;
		this.compCounts = compCounts;
		this.compPercents = compPercents;
	}
	
	//The user may have edited one of the parameters of the grid, meaning that one of the epithelium parameters has changed.
	public void editEpitheliumGrid(int gridX, int gridY, String topologyID, RollOver rollover)
			throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException, ClassNotFoundException {
		
		// Create new EpiCell[][] in case the dimension of the grid has changed
		AbstractCell[][] newGrid = new AbstractCell[gridX][gridY];
		for (int y = 0; y < gridY; y++) {
			for (int x = 0; x < gridX; x++) {
				if (x <= this.getX() || y <= this.getY()) {
					newGrid[x][y] = this.gridCells[x][y];
				}
				else {
					newGrid[x][y] = CellFactory.newEmptyCell();
				}
			}
		}
		this.gridCells = newGrid;
		
		// Create new Topology
		this.setTopology(topologyID, gridX, gridY, rollover);
		// Update grid
		this.updateGrid();
	}

	public EpitheliumGrid(int gridX, int gridY, String topologyID, RollOver rollover, AbstractCell c)
			throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException, ClassNotFoundException {
		this.setTopology(topologyID, gridX, gridY, rollover);
		
		this.gridCells = new AbstractCell[gridX][gridY];
		for (int y = 0; y < gridY; y++) {
			for (int x = 0; x < gridX; x++) {
				this.gridCells[x][y] = c;
			}
		}
		this.modelSet = new HashSet<LogicalModel>();
		if (c.isLivingCell()) {
		this.modelSet.add(((LivingCell) c).getModel());
		}
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

	//ISSUE: 
	public LogicalModel getModel(int x, int y) {
		if (this.gridCells[x][y].isLivingCell()) {
			LivingCell lCell = (LivingCell) this.gridCells[x][y];
			return lCell.getModel();
		}
		else return null;
			
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
		if (this.gridCells[x][y].isLivingCell()) {
			LivingCell lCell = (LivingCell) this.gridCells[x][y];
			lCell.restrictValuesWithPerturbation();
		}

	}

	public byte[] getCellState(int x, int y) {
		if (this.gridCells[x][y].isLivingCell()) {
			LivingCell lCell = (LivingCell) this.gridCells[x][y];
			return lCell.getState();
	}
		else return null;
	}

	@SuppressWarnings("null")
	public byte getCellValue(int x, int y, String nodeID) {
		if (this.gridCells[x][y].isLivingCell()) {
			LivingCell lCell = (LivingCell) this.gridCells[x][y];
			return lCell.getValue(nodeID);
	}
		else return (Byte) null;
	}

	public AbstractPerturbation getPerturbation(int x, int y) {
		if (this.gridCells[x][y].isLivingCell()) {
			LivingCell lCell = (LivingCell) this.gridCells[x][y];
			return lCell.getPerturbation();
	}
		else return null;
	}
	
	public AbstractCell getAbstCell(int x, int y) {
		return this.gridCells[x][y];
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
		if (this.gridCells[x][y].isLivingCell()) {
			LivingCell lCell = (LivingCell) this.gridCells[x][y];
			return lCell.getNodeIndex(nodeID);
	}
		else return -1;
	}


	public byte getCellComponentValue(int x, int y, String nodeID) {
		if (this.gridCells[x][y].isLivingCell()) {
			LivingCell lCell = (LivingCell) this.gridCells[x][y];
			return lCell.getNodeValue(nodeID);
	}
		else return -1;
	}

	public Set<LogicalModel> getModelSet() {
		return Collections.unmodifiableSet(this.modelSet);
	}

	public void updateGrid() {
		this.updateModelSet();
		this.updateNodeValueCounts();
	}

	public void updateModelSet() {
		//TODO TOD OT
		
		this.modelSet.clear();
		for (int y = 0; y < this.getY(); y++) {
			for (int x = 0; x < this.getX(); x++) {
				if (this.gridCells[x][y].isLivingCell()) {
					LivingCell lCell = (LivingCell) this.gridCells[x][y];
					this.modelSet.add(lCell.getModel());
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
		this.gridCells[x][y] = CellFactory.newLivingCell(m);
	}

	
	public void setPerturbation(List<Tuple2D<Integer>> lTuples, AbstractPerturbation ap) {
		for (Tuple2D<Integer> tuple : lTuples) {
			if (this.gridCells[tuple.getX()][tuple.getY()].isLivingCell()) {
				LogicalModel model = ((LivingCell) this.gridCells[tuple.getX()][tuple.getY()]).getModel();
				if (apBelongsToModel(model, ap)) {
				this.setPerturbation(tuple.getX(), tuple.getY(), ap);
			}
		}}
	}

	private boolean apBelongsToModel(LogicalModel model, AbstractPerturbation ap) {
		// TODO Auto-generated method stub

		String sExpr = ap.getStringRepresentation();
		String[] saExpr = sExpr.split(", ");
		List<NodeInfo> nodes = new ArrayList<NodeInfo>();
		
		for (String sTmp : saExpr) {
			String name = sTmp.split("%")[0];
//			System.out.println("EpitheliumGrid: name -> " + name);
			NodeInfo node = Project.getInstance().getProjectFeatures().getNodeInfo(name);
			nodes.add(node);
//			System.out.println("EpitheliumGrid: " + node);
		}
		
		if (nodes.size()==1) {
			NodeInfo node = nodes.get(0);
			if  (model.getComponents().contains(node))
				return true;
		}
		else if (nodes.size()>1){
		for (NodeInfo node : nodes) {
			if  (!model.getComponents().contains(node))
				return false;
		}
		}
			return false;
	}

	public void setPerturbation(int x, int y, AbstractPerturbation ap) {
		if (this.gridCells[x][y].isLivingCell()) {
			((LivingCell) this.gridCells[x][y]).setPerturbation(ap);
	}
	}

	public void setCellState(int x, int y, byte[] state) {
		if (this.gridCells[x][y].isLivingCell()) {
			((LivingCell) this.gridCells[x][y]).setState(state);
	}}

	public void setCellComponentValue(int x, int y, String nodeID, byte value) {
		if (this.gridCells[x][y].isLivingCell()) {
			((LivingCell) this.gridCells[x][y]).setValue(nodeID, value);
	}}

	//TODO: THIS IS WRONG
//	public AbstractCell cloneEpitheliumCellAt(int x, int y) {
//		return this.gridCells[x][y].clone();
//	}

	public AbstractCell getEpitheliumCell(int x, int y) {
		return this.gridCells[x][y];
	}

	public void setEpitheliumCell(int x, int y, AbstractCell absCell) {
		this.gridCells[x][y] = absCell;
	}

//	protected void cloneEpitheliumCellTo(int x1, int y1, int x2, int y2) {
//		AbstractCell epiCell = this.cloneEpitheliumCellAt(x1, y1);
//		this.gridCells[x2][y2] = epiCell;
//	}

	public String hashGrid() {
		String hash = "";
		for (int y = 0; y < this.getY(); y++) {
			for (int x = 0; x < this.getX(); x++) {
				if (this.gridCells[x][y].isLivingCell()) {
					hash += ((LivingCell) this.gridCells[x][y]).hashState();
			}
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
				if (!this.gridCells[x][y].equals(o.gridCells[x][y])) {
					return false;
				}
			}
		}
		return true;
	}

	public EpitheliumGrid clone() {
		AbstractCell[][] newGrid = new AbstractCell[this.getX()][this.getY()];
		for (int y = 0; y < this.getY(); y++) {
			for (int x = 0; x < this.getX(); x++) {
				newGrid[x][y] = this.gridCells[x][y].clone();
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
