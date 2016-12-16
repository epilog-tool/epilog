package org.epilogtool.core.cellDynamics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.epilogtool.common.RandomFactory;
import org.epilogtool.common.Tuple2D;
import org.epilogtool.core.topology.Topology;
import org.epilogtool.core.EpitheliumGrid;

public class GridTopology {
	
	private byte[][][] compressionGrid;
	private Topology topology;
	private int maxDist;

	public GridTopology(EpitheliumGrid epi) {
		this.topology = epi.getTopology().clone();
		this.maxDist=3;
		this.buildGrid(epi);
	}
	
	private GridTopology(Topology topology,  byte[][][] compressionGrid, int maxDist) {
		this.topology = topology;
		this.compressionGrid = compressionGrid;
		this.maxDist = maxDist;
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
	
	private Set<Tuple2D<Integer>> setDistanceNeighbours(int x, int y, int distance, Set<Tuple2D<Integer>> currConnectedNeighbours) {
		if (this.compressionGrid[x][y][0]==0) {
			this.compressionGrid[x][y][distance]= (byte) 0;
			return null;
		}
		Set<Tuple2D<Integer>> newDistancePositions = new HashSet<Tuple2D<Integer>>();
		Set<Tuple2D<Integer>> newConnectedNeighbours = new HashSet<Tuple2D<Integer>>();
		Set<Tuple2D<Integer>> distanceNeighbours = this.topology.getPositionNeighbours(x, y, distance);
		byte totalNeighbours = (byte) (distance*6);
		byte countedNeighbours = (byte) (totalNeighbours-distanceNeighbours.size());
		for (Tuple2D<Integer> currConnectedNeighbour : currConnectedNeighbours) {
			newDistancePositions.addAll(this.topology.getPositionNeighbours
					(currConnectedNeighbour.getX(), currConnectedNeighbour.getY(), 1));
		}
		newDistancePositions.retainAll(distanceNeighbours);
		for (Tuple2D<Integer> position : newDistancePositions) {
			if (this.isOccupied(position)) {
				newConnectedNeighbours.add(position);
			}
		}
		this.compressionGrid[x][y][distance] = (byte) (newConnectedNeighbours.size() + countedNeighbours);
		return newDistancePositions;
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
	
	public float getCompression(Tuple2D<Integer> position) {
		float compression = (float) this.compressionGrid[position.getX()][position.getY()][0];
		for (int index = 1; index <= this.maxDist; index ++) {
			compression += this.getDistanceCompression(position, index)*this.getDistanceWeight(index);
		}
		return compression;
	}
	
	public Tuple2D<Integer> getEmptyPosition(Tuple2D<Integer> originalPos) {
		//Selects the direction of cell displacement for division of cell in the position (x, y)
		Tuple2D<Integer> currPosition = originalPos.clone();
		float currCompression = this.getCompression(currPosition);
		List<Tuple2D<Integer>> nextPositionsList = new ArrayList<Tuple2D<Integer>>();
		int currDistance = 1;
		
		//first search: accounts for absence of measurable differences in compression
		while (nextPositionsList.isEmpty()) {
			Set<Tuple2D<Integer>> neighbourSet = 
					this.topology.getPositionNeighbours(currPosition.getX(), currPosition.getY(),
							currDistance);
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
		
		List<Tuple2D<Integer>> emptyPositionsList = new ArrayList<Tuple2D<Integer>>();
		int occupiedNum = 0;
		
		if (currCompression == 0) {
			for (Tuple2D<Integer> emptyPosition : nextPositionsList) {
				int tmpOccupiedNum = 0;
				for (Tuple2D<Integer> tmpTuple : this.topology.getPositionNeighbours(emptyPosition.getX(), emptyPosition.getY(), 1)) {
					if (this.isOccupied(tmpTuple)) {
						tmpOccupiedNum += 1;
					}
				}
				if (tmpOccupiedNum == occupiedNum) {
					emptyPositionsList.add(emptyPosition);
				}
				if (tmpOccupiedNum > occupiedNum) {
					emptyPositionsList.clear();
					emptyPositionsList.add(emptyPosition);
					occupiedNum = tmpOccupiedNum;
				}
			}
			return emptyPositionsList.get(RandomFactory.getInstance().nextInt(emptyPositionsList.size())).clone();
		}
		
		//second search: there are measurable differences in compression, find path to border
		while (emptyPositionsList.isEmpty()) {
			Tuple2D<Integer> nextPosition = nextPositionsList.get(
					RandomFactory.getInstance().nextInt(nextPositionsList.size())).clone();
			nextPositionsList.clear();
			Set<Tuple2D<Integer>> neighbourSet = 
					this.topology.getPositionNeighbours(nextPosition.getX(), nextPosition.getY(),1);
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
		
		List<Tuple2D<Integer>> emptyPositionsListClone = new ArrayList<Tuple2D<Integer>>();
		for (Tuple2D<Integer> emptyPosition : emptyPositionsList) {
			int tmpOccupiedNum = 0;
			for (Tuple2D<Integer> tmpTuple : this.topology.getPositionNeighbours(emptyPosition.getX(), emptyPosition.getY(), 1)) {
				if (this.isOccupied(tmpTuple)) {
					tmpOccupiedNum += 1;
				}
			}
			if (tmpOccupiedNum == occupiedNum) {
				emptyPositionsListClone.add(emptyPosition);
			}
			if (tmpOccupiedNum > occupiedNum) {
				emptyPositionsListClone.clear();
				emptyPositionsListClone.add(emptyPosition);
				occupiedNum = tmpOccupiedNum;
			}
		}
		
		Tuple2D<Integer> endPosition = emptyPositionsListClone.get(RandomFactory.getInstance().nextInt(emptyPositionsListClone.size()));
		return endPosition;
	}
	
	public int distance(Tuple2D<Integer> init, Tuple2D<Integer> end) {
		int i = 0;
		Set<Tuple2D<Integer>> tmpSet = new HashSet<Tuple2D<Integer>>();
		tmpSet.add(init);
		while (!tmpSet.contains(end)) {
			i+= 1;
			tmpSet = this.topology.getPositionNeighbours(init.getX(), init.getY(), i);
		}
		return i;
	}
	
	public void addCell(Tuple2D<Integer> cell) {
		//Set cell to occupied
		int x = cell.getX();
		int y = cell.getY();
		this.compressionGrid[x][y][0] = (byte) 1;
		
		//Store connected cells from one distance to the next
		Set<Tuple2D<Integer>> currConnectedNeighbours = new HashSet<Tuple2D<Integer>>();
		currConnectedNeighbours.add(cell);
		for (int distance = 1; distance <= this.maxDist; distance ++) {
			currConnectedNeighbours = this.setDistanceNeighbours(x, y, distance, currConnectedNeighbours);
			for (Tuple2D<Integer> currConnectedNeighbour : currConnectedNeighbours) {
				//TODO: this may be improved
				this.setNeighbours(currConnectedNeighbour.getX(), currConnectedNeighbour.getY());
			}
		}
	}
	
	public void removeCell(Tuple2D<Integer> cell) {
		int x = cell.getX();
		int y = cell.getY();
		this.compressionGrid[x][y][0] = (byte) 0;
		this.setNeighbours(x, y);
		for (int distance = 1; distance <= this.maxDist; distance ++) {
			Set<Tuple2D<Integer>> cells2UpdateSet = this.topology.getPositionNeighbours(x, y, distance);
			for (Tuple2D<Integer> cell2Update : cells2UpdateSet) {
				if (!this.isOccupied(cell2Update)) {
					continue;
				}
				this.setNeighbours(cell2Update.getX(), cell2Update.getY());
			}
		}
	}
	
	public byte[][][] getCompressionGrid() {
		return this.compressionGrid;
	}
	
	public GridTopology clone() {
		byte[][][] newCompressionGrid = new byte[this.topology.getX()][this.topology.getY()][this.maxDist+1];
		for (int x = 0; x < this.topology.getX(); x ++) {
			for (int y = 0; y < this.topology.getY(); y ++) {
				newCompressionGrid[x][y] = Arrays.copyOf(this.compressionGrid[x][y], this.maxDist+1);
			}
		}
		return new GridTopology(this.topology.clone(), newCompressionGrid, this.maxDist);
	}
	
	public boolean equals(Object o) {
		GridTopology oET = (GridTopology) o;
		return Arrays.equals(this.compressionGrid, oET.compressionGrid)
				&& this.maxDist==oET.maxDist 
				&& this.topology.equals(oET.topology);
	}

}
