package org.ginsim.epilog.core;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.colomoto.logicalmodel.LogicalModel;
import org.colomoto.logicalmodel.perturbation.AbstractPerturbation;
import org.ginsim.epilog.gui.color.ColorUtils;

public class EpitheliumGrid {
	private EpitheliumCell[][] cellGrid;
	private Map<String, Color> nodeColor;

	private EpitheliumGrid(EpitheliumCell[][] cellGrid) {
		this.cellGrid = cellGrid;
		this.nodeColor = new HashMap<String, Color>();
	}

	public EpitheliumGrid(int x, int y, LogicalModel m) {
		this(new EpitheliumCell[x][y]);
		for (int i = 0; i < x; i++) {
			for (int j = 0; j < y; j++) {
				this.cellGrid[i][j] = new EpitheliumCell(m);
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

	public Color getComponentColor(String component) {
		return this.nodeColor.get(component);
	}

	public void setComponentColor(String component, Color color) {
		this.nodeColor.put(component, color);
	}

	public Color getCellColor(int x, int y, List<String> components) {
		List<Color> cellColors = new ArrayList<Color>();
		for (String comp : components) {
			byte value = cellGrid[x][y].getComponentValue(comp);
			byte max = cellGrid[x][y].getComponentMax(comp);
			cellColors.add(ColorUtils.getColorAtValue(this.nodeColor.get(comp),
					max, value));
		}
		return ColorUtils.combine(cellColors);
	}
	
	public List<EpitheliumCell> getNeighbours(int x, int y, int minDist, int MaxDist) {
		List<EpitheliumCell> l = new ArrayList<EpitheliumCell>();
		
		// TODO 
		
		return l;
	}
}
