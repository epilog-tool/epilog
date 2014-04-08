package org.ginsim.epilog.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.colomoto.logicalmodel.LogicalModel;
import org.colomoto.logicalmodel.perturbation.AbstractPerturbation;
import org.ginsim.epilog.Tuple2D;
import org.ginsim.epilog.core.topology.RollOver;
import org.ginsim.epilog.core.topology.Topology;
import org.ginsim.epilog.core.topology.TopologyHexagon;

public class EpitheliumGrid {
	private EpitheliumCell[][] cellGrid;
	private Topology topology;
	private Set<LogicalModel> modelSet;

	private EpitheliumGrid(EpitheliumCell[][] cellGrid, Topology topology,
			Set<LogicalModel> modelSet) {
		this.cellGrid = cellGrid;
		this.topology = topology;
		this.modelSet = modelSet;
	}

	public EpitheliumGrid(int x, int y, LogicalModel m) {
		this.cellGrid = new EpitheliumCell[x][y];
		this.topology = new TopologyHexagon(x, y, RollOver.NOROLLOVER);
		for (int i = 0; i < this.getX(); i++) {
			for (int j = 0; j < this.getY(); j++) {
				this.cellGrid[i][j] = new EpitheliumCell(m);
			}
		}
		this.modelSet = new HashSet<LogicalModel>();
		this.modelSet.add(m);
	}

	public boolean hasModel(LogicalModel m) {
		return this.modelSet.contains(m);
	}

	public void updateModelSet() {
		this.modelSet = new HashSet<LogicalModel>();
		for (int x = 0; x < this.cellGrid.length; x++) {
			for (int y = 0; y < this.cellGrid[0].length; y++) {
				this.modelSet.add(this.cellGrid[x][y].getModel());
			}
		}
	}

	public Set<LogicalModel> getModelSet() {
		return Collections.unmodifiableSet(this.modelSet);
	}

	public void setRollOver(RollOver r) {
		this.topology.setRollOver(r);
	}

	// TODO: simplify getX between classes
	public int getX() {
		return this.cellGrid.length;
	}

	public int getY() {
		return this.cellGrid[0].length;
	}

	public Topology getTopology() {
		return this.topology;
	}

	public EpitheliumGrid clone() {
		int x = this.cellGrid.length;
		int y = this.cellGrid[0].length;
		EpitheliumCell[][] newGrid = new EpitheliumCell[x][y];
		for (int i = 0; i < x; i++) {
			for (int j = 0; j < y; j++) {
				newGrid[i][j] = cellGrid[i][j].clone();
			}
		}
		Topology newTop = this.topology.clone();
		Set<LogicalModel> newModelSet = new HashSet<LogicalModel>(this.modelSet);
		return new EpitheliumGrid(newGrid, newTop, newModelSet);
	}

	public AbstractPerturbation getPerturbation(int x, int y) {
		return cellGrid[x][y].getPerturbation();
	}

	public void setPerturbation(int x, int y, AbstractPerturbation ap) {
		cellGrid[x][y].setPerturbation(ap);
	}

	public void setPerturbation(LogicalModel m, List<Tuple2D> lTuples,
			AbstractPerturbation ap) {
		for (Tuple2D tuple : lTuples) {
			if (this.cellGrid[tuple.getX()][tuple.getY()].getModel().equals(m)) {
				this.setPerturbation(tuple.getX(), tuple.getY(), ap);
			}
		}
	}

	public byte[] getCellState(int x, int y) {
		return cellGrid[x][y].getState();
	}

	public void setCellState(int x, int y, byte[] state) {
		cellGrid[x][y].setState(state);
	}

	public void setCellComponentValue(int x, int y, String nodeID, byte value) {
		cellGrid[x][y].setValue(nodeID, value);
	}

	public LogicalModel getModel(int x, int y) {
		return cellGrid[x][y].getModel();
	}

	public void setModel(int x, int y, LogicalModel m) {
		cellGrid[x][y].setModel(m);
	}

	public List<EpitheliumCell> getNeighbours(int x, int y, int minDist,
			int maxDist) {
		List<EpitheliumCell> l = new ArrayList<EpitheliumCell>();

		for (Tuple2D tuple : this.topology
				.getNeighbours(x, y, minDist, maxDist)) {
			l.add(cellGrid[tuple.getX()][tuple.getY()]);
		}
		return l;
	}

	public List<EpitheliumCell> filterNeighboursByComponent(
			List<EpitheliumCell> neighbours, String nodeID) {
		List<EpitheliumCell> filteredCells = new ArrayList<EpitheliumCell>();
		for (EpitheliumCell cell : neighbours) {
			if (cell.hasNode(nodeID))
				filteredCells.add(cell);
		}
		return filteredCells;
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

	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof EpitheliumGrid))
			return false;
		if (obj == this)
			return true;
		EpitheliumGrid o = (EpitheliumGrid) obj;

		if (this.getX() != o.getX())
			return false;
		if (this.getY() != o.getY())
			return false;
		for (int x = 0; x < this.getX(); x++) {
			for (int y = 0; y < this.getY(); y++) {
				if (!this.getModel(x, y).equals(o.getModel(x, y)))// FIXME <-
																	// can be
																	// diff?
					return false;
				if (this.getPerturbation(x, y) != o.getPerturbation(x, y))
					return false;
				byte[] thisState = this.getCellState(x, y);
				byte[] oState = o.getCellState(x, y);
				if (thisState.length != oState.length)
					return false;
				for (int i = 0; i < thisState.length; i++) {
					if (thisState[i] != oState[i])
						return false;
				}
			}
		}
		return true;
	}
}
