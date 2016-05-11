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
	
	private byte[][] spaceGrid;
	private byte[][][] tensionGrid;
	private Topology topology;
	private int maxDist;
	private Random generator;

	public EpitheliumTopology(Epithelium epi) {
		this.topology = epi.getEpitheliumGrid().getTopology().clone();
		this.maxDist = Math.max(this.topology.getX(), this.topology.getY())/2;
		this.generator = new Random();
		this.buildSpaceGrid(epi.getEpitheliumGrid());
		this.buildTensionGrid();
	}
	
	private EpitheliumTopology(Topology topology, byte[][] 
			spaceGrid, byte[][][] tensionGrid, int maxDist) {
		this.topology = topology;
		this.spaceGrid = spaceGrid;
		this.tensionGrid = tensionGrid;
		this.maxDist = maxDist;
		this.generator = new Random();
	}
	
	
	private void buildSpaceGrid(EpitheliumGrid grid) {
		this.spaceGrid = new byte[this.topology.getX()][this.topology.getY()];
		for (int x = 0; x < grid.getX(); x++) {
			for (int y = 0; y < grid.getY(); y++) {
				if (grid.isEmptyCell(x, y)) {
					this.spaceGrid[x][y] = (byte) 0;
				} else {
					this.spaceGrid[x][y] = (byte) 1;
				}
			}
		}
	}
	
	private void buildTensionGrid() {
		this.tensionGrid = new byte[this.topology.getX()][this.topology.getY()][this.maxDist];
		for (int x = 0; x < this.topology.getX(); x++) {
			for (int y = 0; y < this.topology.getY(); y ++) {
				this.setNeighbours(x, y);
			}
		}
	}
	
	private void setNeighbours(int x, int y){
		for (int distance = 1; distance <= this.maxDist; distance ++) {
			this.setDistanceNeighbours(x, y, distance);
		}
	}
	
	private void setDistanceNeighbours(int x, int y, int distance) {
		if (this.spaceGrid[x][y]==0) {
			this.tensionGrid[x][y][distance-1]= (byte) 0;
			return;
		}
		Set<Tuple2D<Integer>> neighbours = this.topology.getPositionNeighbours(x, y, distance, distance);
		byte totalNeighbours = (byte) (distance*6);
		byte countedNeighbours = (byte) (totalNeighbours-neighbours.size());
		for (Tuple2D<Integer> neighbour: neighbours) {
			if (this.spaceGrid[neighbour.getX()][neighbour.getY()]==1) {
				countedNeighbours +=1;
			}
		}
		this.tensionGrid[x][y][distance-1] = countedNeighbours;
	}
	
	private float getDistanceTension(Tuple2D<Integer> position, int distance) {
		int countedNeighbours = this.tensionGrid[position.getX()][position.getY()][distance];
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
	
	private float getTotalTension(Tuple2D<Integer> position) {
		float totalTension = (float) this.spaceGrid[position.getX()][position.getY()];
		for (int index = 0; index < this.maxDist; index ++) {
			totalTension += this.getDistanceTension(position, index)*this.getDistanceWeight(index+1);
		}
		return totalTension;
	}
	
	private float getMinimumNeighbourTension(Set<Tuple2D<Integer>> neighbourSet, float currTension) {
		for (Tuple2D<Integer> neighbour : neighbourSet) {
			float neighbourTension = this.getTotalTension(neighbour);
			if (neighbourTension < currTension) {
				currTension = neighbourTension;
			}
		}
		return currTension;
	}
	
	private boolean hasEmptySpace(Set<Tuple2D<Integer>> neighbourSet) {
		for (Tuple2D<Integer> tuple : neighbourSet) {
			if (this.spaceGrid[tuple.getX()][tuple.getY()]==0) {
				return true;
			}
		}
		return false;
	}
	
	public Tuple2D<Integer> getDaughterPosition(int x, int y) {
		if (this.spaceGrid[x][y]==0) {
			return null;
		}
		Set<Tuple2D<Integer>> neighbourSet = this.topology.getPositionNeighbours(x, y, 1, 1);
		float motherTension = this.getTotalTension(new Tuple2D<Integer>(x, y));
		float tmpTension = this.getMinimumNeighbourTension(neighbourSet, motherTension);
		List<Tuple2D<Integer>> neighbourList = new ArrayList<Tuple2D<Integer>>(neighbourSet);
		if (tmpTension==motherTension) {
			return neighbourList.get(this.generator.nextInt(neighbourList.size()));
		}
		for (Tuple2D<Integer> neighbour : neighbourSet) {
			if (this.getTotalTension(neighbour) > tmpTension) {
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
		while (!(this.spaceGrid[currPosition.getX()][currPosition.getY()]==0)) {
			Set<Tuple2D<Integer>> neighbourSet = 
					this.topology.getPositionNeighbours(currPosition.getX(), currPosition.getY(), 1, 1);
			neighbourSet.removeAll(path);
			float currTension = this.getTotalTension(currPosition);
			float nextTension = this.getMinimumNeighbourTension(neighbourSet, currTension);
			if (nextTension>=currTension && !this.hasEmptySpace(neighbourSet)) {
				path = this.divisionPathHelper(path);
				currPosition = path.get(path.size()-1);
				continue;
			}
			Set<Tuple2D<Integer>> possiblePaths = new HashSet<Tuple2D<Integer>>();
			for (Tuple2D<Integer> neighbour : neighbourSet) {
				if (this.getTotalTension(neighbour) <= nextTension) {
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
	
	private List<Tuple2D<Integer>> divisionPathHelper(List<Tuple2D<Integer>> path) {
		
		Tuple2D<Integer> originalPos = path.get(path.size()-1);
		float currTension = this.getTotalTension(originalPos);
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
				float tmpTension = this.getTotalTension(neighbour);
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
			if ((this.getTotalTension(neighbour)>currTension)) {
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
				if (this.spaceGrid[neighbour.getX()][neighbour.getY()] == 0) {
					emptySpaceFlag = true;
				}
			}
			neighboursMap.put(distance, tmpSet);
		}
		
		//select empty space to move to, in case there is more than one
		Set<Tuple2D<Integer>> tmpSet = new HashSet<Tuple2D<Integer>>(neighboursMap.get(distance));
		for (Tuple2D<Integer> neighbour: neighboursMap.get(distance)) {
			if (this.spaceGrid[neighbour.getX()][neighbour.getY()]==1) {
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
	
	public void updatePopulationTopology(int x, int y, byte value) {
		if (this.spaceGrid[x][y]==value) {
			return;
		}
		this.spaceGrid[x][y] = value;
		this.setNeighbours(x, y);
		byte sign = (byte) ((value == 0) ? -1 : 1);
		for (int distance = 1; distance <= maxDist; distance ++) {
			Set<Tuple2D<Integer>> tuples2update = this.topology.getPositionNeighbours(x, y, distance, distance);
			for (Tuple2D<Integer> tuple2update : tuples2update) {
				if (this.spaceGrid[tuple2update.getX()][tuple2update.getY()]==0) {
					continue;
				}
				this.tensionGrid[tuple2update.getX()][tuple2update.getY()][distance-1] += sign;
			}
		}
	}
		
	public byte[][] getSpaceGrid() {
		return this.spaceGrid;
	}
	
	public byte[][][] getTensionGrid() {
		return this.tensionGrid;
	}
	
	public EpitheliumTopology clone() {
		return new EpitheliumTopology(this.topology.clone(), 
				this.spaceGrid.clone(), 
				this.tensionGrid.clone(), this.maxDist);
	}

}
