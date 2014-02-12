package org.ginsim.epilog.core;

import org.colomoto.logicalmodel.LogicalModel;
import org.colomoto.logicalmodel.perturbation.AbstractPerturbation;

public class EpitheliumGrid {
	private EpitheliumCell[][] cellGrid;

	private EpitheliumGrid(EpitheliumCell[][] cellGrid) {
		this.cellGrid = cellGrid;
	}

	public EpitheliumGrid(int x, int y, LogicalModel m) {
		cellGrid = new EpitheliumCell[x][y];
		for (int i = 0; i < x; i++) {
			for (int j = 0; j < y; j++) {
				cellGrid[i][j] = new EpitheliumCell(m);
			}
		}
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

	public void setPerturbation(int x, int y, AbstractPerturbation p) {
		cellGrid[x][y].setPerturbation(p);
	}

	public byte[] getCellState(int x, int y) {
		return cellGrid[x][y].getState();
	}

	public LogicalModel getModel(int x, int y) {
		return cellGrid[x][y].getModel();
	}

	public void setModel(int x, int y, LogicalModel m) {
		cellGrid[x][y].setModel(m);
	}
}
