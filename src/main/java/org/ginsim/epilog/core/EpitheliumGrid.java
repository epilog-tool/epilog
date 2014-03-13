package org.ginsim.epilog.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.colomoto.logicalmodel.LogicalModel;
import org.colomoto.logicalmodel.NodeInfo;
import org.colomoto.logicalmodel.perturbation.AbstractPerturbation;
import org.ginsim.epilog.Tuple2D;
import org.ginsim.epilog.core.topology.RollOver;
import org.ginsim.epilog.core.topology.Topology;
import org.ginsim.epilog.core.topology.TopologyHexagon;

public class EpitheliumGrid {
	private EpitheliumCell[][] cellGrid;
	private Topology topology;

	private EpitheliumGrid(EpitheliumCell[][] cellGrid) {
		this.cellGrid = cellGrid;
	}

	public EpitheliumGrid(int x, int y, LogicalModel m) {
		this(new EpitheliumCell[x][y]);
		this.topology = new TopologyHexagon(x, y, RollOver.NOROLLOVER);
		Map<String, NodeInfo> tmp = new HashMap<String, NodeInfo>();
		for (NodeInfo node : m.getNodeOrder()) {
			tmp.put(node.getNodeID(), node);
		}
	}

	public void setRollOver(RollOver r) {
		this.topology.setRollOver(r);
	}

	public void setModelGrid(LogicalModel m) {
		for (int i = 0; i < this.getX(); i++) {
			for (int j = 0; j < this.getY(); j++) {
				this.cellGrid[i][j] = new EpitheliumCell(m);
			}
		}
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
		return new EpitheliumGrid(newGrid);
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
}
