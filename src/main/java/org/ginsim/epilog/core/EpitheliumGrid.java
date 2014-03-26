package org.ginsim.epilog.core;

import java.util.ArrayList;
import java.util.List;

import org.colomoto.logicalmodel.LogicalModel;
import org.colomoto.logicalmodel.perturbation.AbstractPerturbation;
import org.ginsim.epilog.Tuple2D;
import org.ginsim.epilog.core.topology.RollOver;
import org.ginsim.epilog.core.topology.Topology;
import org.ginsim.epilog.core.topology.TopologyHexagon;

public class EpitheliumGrid {
	private EpitheliumCell[][] cellGrid;
	private Topology topology;

	private EpitheliumGrid(EpitheliumCell[][] cellGrid, Topology topology) {
		this.cellGrid = cellGrid;
		this.topology = topology;
	}

	public EpitheliumGrid(int x, int y, LogicalModel m) {
		this.cellGrid = new EpitheliumCell[x][y];
		this.topology = new TopologyHexagon(x, y, RollOver.NOROLLOVER);
		for (int i = 0; i < this.getX(); i++) {
			for (int j = 0; j < this.getY(); j++) {
				this.cellGrid[i][j] = new EpitheliumCell(m);
			}
		}
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
		return new EpitheliumGrid(newGrid, newTop);
	}

	public AbstractPerturbation getPerturbation(int x, int y) {
		return cellGrid[x][y].getPerturbation();
	}

	public void setPerturbation(int x, int y, AbstractPerturbation ap) {
		cellGrid[x][y].setPerturbation(ap);
	}

	public void setPerturbation(List<Tuple2D> lTuples, AbstractPerturbation ap) {
		for (Tuple2D tuple : lTuples) {
			this.setPerturbation(tuple.getX(), tuple.getY(), ap);
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
		for (int x = 0; x < this.getX(); x++) {
			s+=(x+1)+"|";
			for (int y = 0; y < this.getY(); y++) {
				byte[] currState = this.getCellState(x, y);
				for (int i = 0; i < currState.length; i++) {
					s += currState[i];
				}
				s+="|";
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
