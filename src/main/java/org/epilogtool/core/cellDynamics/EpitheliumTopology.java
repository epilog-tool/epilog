package org.epilogtool.core.cellDynamics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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

	public EpitheliumTopology(Epithelium epi) {
		this.topology = epi.getEpitheliumGrid().getTopology().clone();
		this.maxDist=3;
		this.generator = new Random();
		this.buildGrid(epi.getEpitheliumGrid());
		this.fillGrid();
	}
	
	private EpitheliumTopology(Topology topology,  byte[][][] compressionGrid, int maxDist) {
		this.topology = topology;
		this.compressionGrid = compressionGrid;
		this.maxDist = maxDist;
		this.generator = new Random();
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
	}
	
	private void fillGrid() {
		for (int x = 0; x < this.topology.getX(); x++) {
			for (int y = 0; y < this.topology.getY(); y ++) {
				this.setNeighbours(x, y);
			}
		}
	}
	
	private boolean isGridPosition(int x, int y) {
		return this.compressionGrid[x][y][0]==1;
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
			if(connectedNeighbours.contains(neighbour) && this.isGridPosition(neighbour.getX(), neighbour.getY())) {
				countedNeighbours += 1;
				newConnectedNeighbours.add(neighbour);
			}
		}
		this.compressionGrid[x][y][distance] = countedNeighbours;
		return newConnectedNeighbours;
	}
	
	private float getDistanceCompression(Tuple2D<Integer> position, int distance) {
		int countedNeighbours = this.compressionGrid[position.getX()][position.getY()][distance];
		int totalNeighbours = distance*6;
		if (countedNeighbours==0) {
			return (float) 0;
		}
		return 1 / (1 + ((float) Math.exp(2*(-countedNeighbours + (totalNeighbours*0.9)))));
	}
	
	private float getDistanceWeight(int distance) {
		return ((float) 1- distance) / this.maxDist + 1;
		//return ((float) 1) / (1 + ((float) Math.exp(-distance + 1.5)) + 1);
	}
	
	private float getCompression(Tuple2D<Integer> position) {
		float compression = (float) this.compressionGrid[position.getX()][position.getY()][0];
		for (int index = 1; index <= this.maxDist; index ++) {
			compression += this.getDistanceCompression(position, index)*this.getDistanceWeight(index);
		}
		return compression;
	}
	
	private float getMinCompression(Set<Tuple2D<Integer>> neighbourSet, float currCompression) {
		for (Tuple2D<Integer> neighbour : neighbourSet) {
			float neighbourCompression = this.getCompression(neighbour);
			if (neighbourCompression < currCompression) {
				currCompression = neighbourCompression;
			}
		}
		return currCompression;
	}
	
	private boolean hasEmptySpace(Set<Tuple2D<Integer>> neighbourSet) {
		for (Tuple2D<Integer> tuple : neighbourSet) {
			if (this.compressionGrid[tuple.getX()][tuple.getY()][0]==0) {
				return true;
			}
		}
		return false;
	}
	
	public Tuple2D<Integer> getDaughterPosition(int x, int y) {
		if (!this.isGridPosition(x, y)) {
			return null;
		}
		Set<Tuple2D<Integer>> neighbourSet = this.topology.getPositionNeighbours(x, y, 1, 1);
		float motherTension = this.getCompression(new Tuple2D<Integer>(x, y));
		float tmpTension = this.getMinCompression(neighbourSet, motherTension);
		List<Tuple2D<Integer>> neighbourList = new ArrayList<Tuple2D<Integer>>(neighbourSet);
		if (tmpTension==motherTension) {
			return neighbourList.get(this.generator.nextInt(neighbourList.size()));
		}
		for (Tuple2D<Integer> neighbour : neighbourSet) {
			if (this.getCompression(neighbour) > tmpTension) {
				neighbourList.remove(neighbour);
			}
		}
		return neighbourList.get(this.generator.nextInt(neighbourList.size()));
	}
	
	public List<Tuple2D<Integer>> divisionPath(int x, int y) {
		Tuple2D<Integer> motherPosition = new Tuple2D<Integer>(x, y);
		List<Tuple2D<Integer>> path = new ArrayList<Tuple2D<Integer>>();
		path.add(motherPosition);
		Tuple2D<Integer> currPosition = this.getDaughterPosition(x, y);
		path.add(currPosition);
		while (this.isGridPosition(currPosition.getX(),currPosition.getY())) {
			Set<Tuple2D<Integer>> neighbourSet = 
					this.topology.getPositionNeighbours(currPosition.getX(), currPosition.getY(), 1, 1);
			neighbourSet.removeAll(path);
			float currTension = this.getCompression(currPosition);
			float nextTension = this.getMinCompression(neighbourSet, currTension);
			if (nextTension>=currTension && !this.hasEmptySpace(neighbourSet)) {
				path = this.pathHelper(path);
				currPosition = path.get(path.size()-1);
				continue;
			}
			Set<Tuple2D<Integer>> possiblePaths = new HashSet<Tuple2D<Integer>>();
			for (Tuple2D<Integer> neighbour : neighbourSet) {
				if (this.getCompression(neighbour) <= nextTension) {
					possiblePaths.add(neighbour);
				}
			}
			int random = this.generator.nextInt(possiblePaths.size());
			currPosition = new ArrayList<Tuple2D<Integer>>(possiblePaths).get(random);
			path.add(currPosition);
		}
		Collections.reverse(path);
		return path;
	}
		
	private List<Tuple2D<Integer>> pathHelper(List<Tuple2D<Integer>> path) {
		
		Tuple2D<Integer> originalPos = path.get(path.size()-1);
		float currTension = this.getCompression(originalPos);
		Map<Byte, Set<Tuple2D<Integer>>> neighboursMap = 
				new HashMap<Byte, Set<Tuple2D<Integer>>>();
		boolean smallerTensionFlag = false;
		byte distance = 0;
		while (!smallerTensionFlag) {
			distance += 1;
			Set<Tuple2D<Integer>> tmpSet = this.topology.
					getPositionNeighbours(originalPos.getX(), originalPos.getY(), 
										distance, distance);
			tmpSet.removeAll(path);
			float nextTension = (float) this.maxDist;
			for (Tuple2D<Integer> neighbour : tmpSet) {
				float tmpTension = this.getCompression(neighbour);
				if (tmpTension < currTension) {
					smallerTensionFlag = true;
				}
				else {
					nextTension = Math.min(tmpTension, nextTension);
				}
			}
			currTension = nextTension;
			neighboursMap.put(distance, tmpSet);
		}
		
		//select space to move to, in case there is more than one

		List<Tuple2D<Integer>> helperPath = new ArrayList<Tuple2D<Integer>>();
		Set<Tuple2D<Integer>> tmpSet = new HashSet<Tuple2D<Integer>>(neighboursMap.get(distance));
		for (Tuple2D<Integer> neighbour: neighboursMap.get(distance)) {
			if ((this.getCompression(neighbour)>currTension)) {
				tmpSet.remove(neighbour);
			}
		}
		if (tmpSet.size() > 1) {
			int random = this.generator.nextInt(tmpSet.size());
			helperPath.add(new ArrayList<Tuple2D<Integer>>(tmpSet).get(random));
		} else {
			helperPath.add(new ArrayList<Tuple2D<Integer>>(tmpSet).get(0));
		}
		
		//backtrack to original cell and add to path
		distance -= 1;
		while (distance >= 1) {
			Tuple2D<Integer> helperPos = helperPath.get(helperPath.size()-1).clone();
			tmpSet = new HashSet<Tuple2D<Integer>>(neighboursMap.get(distance));
			tmpSet.retainAll(this.topology.getPositionNeighbours
					(helperPos.getX(), helperPos.getY(), 1, 1));
			if (tmpSet.size() > 1) {
				int random = this.generator.nextInt(tmpSet.size());
				helperPath.add(new ArrayList<Tuple2D<Integer>>(tmpSet).get(random));
			} else if (tmpSet.size() == 1) { 
				helperPath.add(new ArrayList<Tuple2D<Integer>>(tmpSet).get(0));
			} else {
				distance += 1;
				tmpSet = new HashSet<Tuple2D<Integer>>(neighboursMap.get(distance));
				tmpSet.retainAll(this.topology.getPositionNeighbours
						(helperPos.getX(), helperPos.getY(), 1, 1));
				if (tmpSet.size() > 1) {
					int random = this.generator.nextInt(tmpSet.size());
					helperPath.add(new ArrayList<Tuple2D<Integer>>(tmpSet).get(random));
				} else if (tmpSet.size() == 1) { 
					helperPath.add(new ArrayList<Tuple2D<Integer>>(tmpSet).get(0));
				}
				distance -=1;
			}
			distance -= 1;
		}
		Collections.reverse(helperPath);
		path.addAll(helperPath);
		return path;
	}
	
	public List<Tuple2D<Integer>> apoptosisPath(int x, int y) {
		List<Tuple2D<Integer>> path = this.divisionPath(x, y);
		Collections.reverse(path);
		return path;
	}
	
	public List<Tuple2D<Integer>> getEventPath(int x, int y, CellTrigger trigger) {
		if (trigger.equals(CellTrigger.PROLIFERATION)) {
			return this.divisionPath(x, y);
		}
		if (trigger.equals(CellTrigger.APOPTOSIS)) {
			return this.apoptosisPath(x, y);
		}
		return null;
	}
	
	public List<Tuple2D<Integer>> divisionLinearPath(int x, int y) {
		List<Tuple2D<Integer>> path = new ArrayList<Tuple2D<Integer>>();
		
		//Check cell surroundings until empty spot is found
		Map<Byte, Set<Tuple2D<Integer>>> neighboursMap = 
				new HashMap<Byte, Set<Tuple2D<Integer>>>();
		boolean emptySpaceFlag = false;
		byte distance = 0;
		while (!emptySpaceFlag) {
			distance += 1;
			Set<Tuple2D<Integer>> tmpSet = 
					this.topology.getPositionNeighbours(x, y, distance, distance);
			for (Tuple2D<Integer> neighbour : tmpSet) {
				if (!this.isGridPosition(neighbour.getX(), neighbour.getY())) {
					emptySpaceFlag = true;
				}
			}
			neighboursMap.put(distance, tmpSet);
		}
		
		//select empty space to move to, in case there is more than one
		Set<Tuple2D<Integer>> tmpSet = new HashSet<Tuple2D<Integer>>(neighboursMap.get(distance));
		for (Tuple2D<Integer> neighbour: neighboursMap.get(distance)) {
			if (this.isGridPosition(neighbour.getX(), neighbour.getY())) {
				tmpSet.remove(neighbour);
			}
		}
		if (tmpSet.size() > 1) {
			int random = this.generator.nextInt(tmpSet.size());
			path.add(new ArrayList<Tuple2D<Integer>>(tmpSet).get(random));
		} else {
			path.add(new ArrayList<Tuple2D<Integer>>(tmpSet).get(0));
		}
		
		//backtrack to original cell and add to path
		distance -= 1;
		while (distance >= 1) {
			Tuple2D<Integer> currPosition = path.get(path.size()-1);
			tmpSet = new HashSet<Tuple2D<Integer>>(neighboursMap.get(distance));
			tmpSet.
			retainAll(this.topology.getPositionNeighbours(currPosition.getX(), currPosition.getY(), 1, 1));
			if (tmpSet.size() > 1) {
				int random = this.generator.nextInt(tmpSet.size());
				path.add(new ArrayList<Tuple2D<Integer>>(tmpSet).get(random));
			} else { 
				path.add(new ArrayList<Tuple2D<Integer>>(tmpSet).get(0));
			}
			distance -= 1;
		}
		path.add(new Tuple2D<Integer>(x, y));
		return path;
	}

	public List<Tuple2D<Integer>> collapsingPath(int x, int y) {
		List<Tuple2D<Integer>> path = new ArrayList<Tuple2D<Integer>>();
		
		return path;
	}
	
	public void updatePopulationTopology(Tuple2D<Integer> shiftedCell, byte value) {
		int x = shiftedCell.getX();
		int y = shiftedCell.getY();
		if (this.compressionGrid[x][y][0]==value) {
			return;
		}
		this.compressionGrid[x][y][0] = value;
		this.setNeighbours(x, y);
		byte sign = (byte) ((value == 0) ? -1 : 1);
		for (int distance = 1; distance <= this.maxDist; distance ++) {
			Set<Tuple2D<Integer>> tuples2update = this.topology.getPositionNeighbours(x, y, distance, distance);
			for (Tuple2D<Integer> tuple2update : tuples2update) {
				if (!this.isGridPosition(tuple2update.getX(), tuple2update.getY())) {
					continue;
				}
				this.compressionGrid[tuple2update.getX()][tuple2update.getY()][distance] += sign;
			}
		}
	}
	
	public byte[][][] getCompressionGrid() {
		return this.compressionGrid;
	}
	
	public EpitheliumTopology clone() {
		return new EpitheliumTopology(this.topology.clone(), this.compressionGrid.clone(), this.maxDist);
	}

}
