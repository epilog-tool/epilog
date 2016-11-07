package org.epilogtool.core.cellDynamics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.epilogtool.common.Tuple2D;
import org.epilogtool.core.Epithelium;
import org.epilogtool.core.topology.Topology;
import org.epilogtool.core.EpitheliumGrid;

public class EpitheliumTopology {
	
	private byte[][][] compressionGrid;
	private Topology topology;
	private int maxDist;
	private Random generator;
	private CellLineage cellLineage;

	public EpitheliumTopology(Epithelium epi) {
		this.topology = epi.getEpitheliumGrid().getTopology().clone();
		this.maxDist=3;
		this.generator = new Random();
		this.cellLineage = new CellLineage();
		this.buildGrid(epi.getEpitheliumGrid());
	}
	
	private EpitheliumTopology(Topology topology,  byte[][][] compressionGrid, int maxDist, CellLineage cellLineage) {
		this.topology = topology;
		this.compressionGrid = compressionGrid;
		this.maxDist = maxDist;
		this.generator = new Random();
		this.cellLineage = cellLineage;
	}
	
	
	private void buildGrid(EpitheliumGrid grid) {
		this.compressionGrid = new byte[this.topology.getX()][this.topology.getY()][this.maxDist+1];
		for (int x = 0; x < grid.getX(); x++) {
			for (int y = 0; y < grid.getY(); y++) {
				if (grid.isEmptyCell(x, y)) {
					this.compressionGrid[x][y][0] = (byte) 0;
				} else {
					this.compressionGrid[x][y][0] = (byte) 1;
				}
			}
		}
		this.fillGrid();
	}
	
	private void fillGrid() {
		for (int x = 0; x < this.topology.getX(); x++) {
			for (int y = 0; y < this.topology.getY(); y ++) {
				this.setNeighbours(x, y);
			}
		}
	}
	
	private void setNeighbours(int x, int y){
		Set<Tuple2D<Integer>> connectedNeighbours = new HashSet<Tuple2D<Integer>>();
		connectedNeighbours.add(new Tuple2D<Integer>(x,y));
		for (int distance = 1; distance <= this.maxDist; distance ++) {
			connectedNeighbours = this.setDistanceNeighbours(x, y, distance, connectedNeighbours);
		}
	}
	
	private Set<Tuple2D<Integer>> setDistanceNeighbours(int x, int y, int distance, Set<Tuple2D<Integer>> connectedNeighbours) {
		if (this.compressionGrid[x][y][0]==0) {
			this.compressionGrid[x][y][distance]= (byte) 0;
			return null;
		}
		Set<Tuple2D<Integer>> newConnectedNeighbours = new HashSet<Tuple2D<Integer>>();
		Set<Tuple2D<Integer>> neighbours = this.topology.getPositionNeighbours(x, y, distance, distance);
		byte totalNeighbours = (byte) (distance*6);
		byte countedNeighbours = (byte) (totalNeighbours-neighbours.size());
		for (Tuple2D<Integer> neighbour: neighbours) {
			if (this.isOccupied(neighbour)) {
				for (Tuple2D<Integer> connectedNeighbour : connectedNeighbours) {
					if (this.topology.getPositionNeighbours(connectedNeighbour.getX(), connectedNeighbour.getY(), 1, 1).contains(neighbour)) {
						countedNeighbours += 1;
						newConnectedNeighbours.add(neighbour);
						break;
					}
				}
			}
		}
		this.compressionGrid[x][y][distance] = countedNeighbours;
		return newConnectedNeighbours;
	}
	
	private boolean isOccupied(Tuple2D<Integer> position) {
		return this.compressionGrid[position.getX()][position.getY()][0]==1;
	}
	
	private float getDistanceCompression(Tuple2D<Integer> position, int distance) {
		int countedNeighbours = this.compressionGrid[position.getX()][position.getY()][distance];
		int totalNeighbours = distance*6;
		if (countedNeighbours==0) {
			return (float) 0;
		}
		return 1 / (1 + ((float) Math.exp(-countedNeighbours + (totalNeighbours*0.9))));
	}
	
	private float getDistanceWeight(int distance) {
		return ((float) 1- distance) / this.maxDist + 1;
	}
	
	private float getCompression(Tuple2D<Integer> position) {
		float compression = (float) this.compressionGrid[position.getX()][position.getY()][0];
		for (int index = 1; index <= this.maxDist; index ++) {
			compression += this.getDistanceCompression(position, index)*this.getDistanceWeight(index);
		}
		return compression;
	}
	
	public Tuple2D<Integer> selectEmptyPosition(Tuple2D<Integer> originalPos) {
		//Selects the direction of cell displacement for division of cell in the position (x, y)
		Tuple2D<Integer> currPosition = originalPos.clone();
		float currCompression = this.getCompression(currPosition);
		List<Tuple2D<Integer>> nextPositionsList = new ArrayList<Tuple2D<Integer>>();
		int currDistance = 1;
		
		//first search: accounts for absence of measurable differences in compression
		while (nextPositionsList.isEmpty()) {
			Set<Tuple2D<Integer>> neighbourSet = 
					this.topology.getPositionNeighbours(currPosition.getX(), currPosition.getY(),
							currDistance, currDistance);
			for (Tuple2D<Integer> neighbour : neighbourSet) {
				float neighbourCompression = this.getCompression(neighbour);
				if (neighbourCompression == currCompression) {
					nextPositionsList.add(neighbour.clone());
				} 
				else if (neighbourCompression < currCompression) {
					nextPositionsList.clear();
					nextPositionsList.add(neighbour.clone());
					currCompression = neighbourCompression;
				} 
			}
			currDistance += 1;
		}
		
		if (currCompression == 0) {
			return nextPositionsList.get(this.generator.nextInt(nextPositionsList.size())).clone();
		}
		
		List<Tuple2D<Integer>> emptyPositionsList = new ArrayList<Tuple2D<Integer>>();
		
		//second search: there are measurable differences in compression, find path to border
		while (emptyPositionsList.isEmpty()) {
			Tuple2D<Integer> nextPosition = nextPositionsList.get(
					this.generator.nextInt(nextPositionsList.size())).clone();
			nextPositionsList.clear();
			Set<Tuple2D<Integer>> neighbourSet = 
					this.topology.getPositionNeighbours(nextPosition.getX(), nextPosition.getY(),
							1,1);
			Set<Tuple2D<Integer>> neighbourSetClone = new HashSet<Tuple2D<Integer>>(neighbourSet);
			for (Tuple2D<Integer> neighbour : neighbourSet) {
				if (!this.isOccupied(neighbour)) {
					emptyPositionsList.add(neighbour);
					neighbourSetClone.remove(neighbour);
					for (Tuple2D<Integer> neighbourClone : neighbourSetClone) {
						if (!this.isOccupied(neighbourClone)) {
							emptyPositionsList.add(neighbourClone);
						}
					}
					break;
				}
				float neighbourCompression = this.getCompression(neighbour);
				if (neighbourCompression == currCompression) {
					nextPositionsList.add(neighbour.clone()); }
				else if (neighbourCompression < currCompression) {
					nextPositionsList.clear();
					nextPositionsList.add(neighbour.clone());
					currCompression = neighbourCompression;
				}
				neighbourSetClone.remove(neighbour);
			}
		}
		Tuple2D<Integer> endPosition = emptyPositionsList.get(this.generator.nextInt(emptyPositionsList.size()));
		return endPosition;
	}
	
	private Tuple2D<Integer> distanceVector(Tuple2D<Integer> init, Tuple2D<Integer> end) {
		return new Tuple2D<Integer>(end.getX()-init.getX(), 
						end.getY()-init.getY());
	}
	
	public List<Tuple2D<Integer>> divisionPath(Tuple2D<Integer> cell2Divide) {
		List<Tuple2D<Integer>> path = new ArrayList<Tuple2D<Integer>>();
		
		Tuple2D<Integer> currPosition = cell2Divide.clone();
		Tuple2D<Integer> endPosition = this.selectEmptyPosition(cell2Divide);
		Tuple2D<Integer> distanceTuple = this.distanceVector(currPosition, endPosition);
		
		path.add(cell2Divide);
		
		List<Tuple2D<Integer>> possiblePaths = new ArrayList<Tuple2D<Integer>>();
		
		while (this.isOccupied(currPosition)) {
			Set<Tuple2D<Integer>> neighbours = this.topology.getPositionNeighbours(currPosition.getX(), currPosition.getY(), 1, 1);
			for (Tuple2D<Integer> cell : neighbours) {
				Tuple2D<Integer> cellDistance = this.distanceVector(cell, endPosition);
				if (cellDistance.euclideanDistance() == distanceTuple.euclideanDistance()) {
					possiblePaths.add(cell.clone());
				}
				else if (cellDistance.euclideanDistance() < distanceTuple.euclideanDistance()) {
					possiblePaths.clear();
					possiblePaths.add(cell);
					distanceTuple = cellDistance.clone();
				}
			}
			currPosition = possiblePaths.get(this.generator.nextInt(possiblePaths.size()));
			path.add(currPosition);
		}
		
		Collections.reverse(path);
		this.update4Division(path.get(0));
		return path;
	}
	
	public List<Tuple2D<Integer>> apoptosisPath(int x, int y) {
		List<Tuple2D<Integer>> path = new ArrayList<Tuple2D<Integer>>();
		Collections.reverse(path);
		return path;
	}
	
	public void update4Division(Tuple2D<Integer> cell) {
		int x = cell.getX();
		int y = cell.getY();
		this.compressionGrid[x][y][0] = (byte) 1;
		byte sign = (byte) 1;
		Set<Tuple2D<Integer>> currConnectedCellsSet = new HashSet<Tuple2D<Integer>>();
		currConnectedCellsSet.add(cell);
		Set<Tuple2D<Integer>> nextConnectedCellsSet = new HashSet<Tuple2D<Integer>>();
		for (int distance = 1; distance <= this.maxDist; distance ++) {
			Set<Tuple2D<Integer>> distanceCellsSet = this.topology.getPositionNeighbours(x, y, distance, distance);
			
			int borderNeighbours = distance*6 - distanceCellsSet.size();
			this.compressionGrid[x][y][distance] = (byte) (nextConnectedCellsSet.size() + borderNeighbours);
			
			for (Tuple2D<Integer> distanceCell : distanceCellsSet) {
				if (this.isOccupied(distanceCell)) {
					for (Tuple2D<Integer> connectedCell : currConnectedCellsSet) {
						if (this.topology.getPositionNeighbours(connectedCell.getX(), connectedCell.getY(), 1, 1).contains(distanceCell)) {
							nextConnectedCellsSet.add(distanceCell);
							this.compressionGrid[x][y][distance] += 1;
							break;
						}
					}
				}
			}
			
			for (Tuple2D<Integer> nextCell : nextConnectedCellsSet) {
				//There is a bug somewhere in the original code, this is the slowest way
				this.setNeighbours(nextCell.getX(), nextCell.getY());
			}
			currConnectedCellsSet = new HashSet<Tuple2D<Integer>>(nextConnectedCellsSet);
			nextConnectedCellsSet.clear();
		}
	}
	
	public void update4Death(Tuple2D<Integer> cell) {
		int x = cell.getX();
		int y = cell.getY();
		this.compressionGrid[x][y][0] = (byte) 0;
		this.setNeighbours(x, y);
		byte sign = (byte) -1;
		for (int distance = 1; distance <= this.maxDist; distance ++) {
			Set<Tuple2D<Integer>> cells2UpdateSet = this.topology.getPositionNeighbours(x, y, distance, distance);
			for (Tuple2D<Integer> cell2Update : cells2UpdateSet) {
				if (!this.isOccupied(cell2Update)) {
					continue;
				}
				this.compressionGrid[cell2Update.getX()][cell2Update.getY()][distance] += sign;
			}
		}
	}
	
	public byte[][][] getCompressionGrid() {
		return this.compressionGrid;
	}
	
	public EpitheliumTopology clone() {
		return new EpitheliumTopology(this.topology.clone(), this.compressionGrid.clone(), this.maxDist, this.cellLineage.clone());
	}

}
