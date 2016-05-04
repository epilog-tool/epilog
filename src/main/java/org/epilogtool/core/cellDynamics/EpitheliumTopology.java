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
	private float[][] tensionGrid;
	private Topology topology;
	private int maxDist;
	private Random generator;

	public EpitheliumTopology(Epithelium epi) {
		this.topology = epi.getEpitheliumGrid().getTopology().clone();
		this.maxDist = (int) Math.max(this.topology.getX(), this.topology.getY())/4;
		this.generator = new Random();
		this.buildSpaceGrid(epi.getEpitheliumGrid());
		this.buildTensionGrid();
	}
	
	private EpitheliumTopology(Topology topology, byte[][] 
			spaceGrid, float[][] tensionGrid) {
		this.topology = topology;
		this.spaceGrid = spaceGrid;
		this.tensionGrid = tensionGrid;
		this.maxDist = (int) Math.max(this.topology.getX(), this.topology.getY());
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
		this.tensionGrid = new float[this.topology.getX()][this.topology.getY()];
		for (int x = 0; x < this.topology.getX(); x++) {
			for (int y = 0; y < this.topology.getY(); y ++) {
				this.setTotalTension(x, y);
			}
		}
	}
	
	private void setTotalTension(int x, int y){
		float totalTension = (this.spaceGrid[x][y]==1) ? 1 : 0;
		if (totalTension == 0) {
			this.tensionGrid[x][y] = totalTension;
			return;
		}
		for (int distance = 1; distance <= this.maxDist; distance ++) {
			float distanceWeight = this.getDistanceWeight(distance);
			float distTension = (float) 0.0;
			Set<Tuple2D<Integer>> neighbours = this.topology.getPositionNeighbours(x, y, distance, distance);
			int falseNeighbours = 6*distance - neighbours.size();
			for (Tuple2D<Integer> neighbour: neighbours) {
				if (this.spaceGrid[neighbour.getX()][neighbour.getY()]==1) {
					distTension +=1;
				}
			}
			distTension += falseNeighbours;
			totalTension += distTension/(neighbours.size()+falseNeighbours)*distanceWeight;
		}
		this.tensionGrid[x][y] = totalTension;
	}
	
	private float getDistanceWeight(int distance) {
		return (1/this.maxDist)*(1-distance)+1;
	}
	
	private float getMinimumNeighbourTension(Set<Tuple2D<Integer>> neighbourSet, float currTension) {
		for (Tuple2D<Integer> neighbour : neighbourSet) {
			if (this.tensionGrid[neighbour.getX()][neighbour.getY()] < currTension) {
				currTension = this.tensionGrid[neighbour.getX()][neighbour.getY()];
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
		Tuple2D<Integer> daughterPosition = new Tuple2D<Integer>(x, y);
		for (Tuple2D<Integer> neighbour : neighbourSet) {
			if (this.tensionGrid[neighbour.getX()][neighbour.getY()] <= 
					this.tensionGrid[daughterPosition.getX()][daughterPosition.getY()]) {
				daughterPosition = neighbour.clone();
			}
		}
		return daughterPosition;
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
			float currTension = this.tensionGrid[currPosition.getX()][currPosition.getY()];
			float nextTension = this.getMinimumNeighbourTension(neighbourSet, currTension);
			if (nextTension==currTension && !this.hasEmptySpace(neighbourSet)) {
				path = this.divisionPathHelper(path);
				currPosition = path.get(path.size()-1);
				continue;
			}
			
			Set<Tuple2D<Integer>> possiblePaths = new HashSet<Tuple2D<Integer>>();
			
			for (Tuple2D<Integer> neighbour : neighbourSet) {
				if (this.tensionGrid[neighbour.getX()][neighbour.getY()] == nextTension) {
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
		float currTension = this.tensionGrid[originalPos.getX()][originalPos.getY()];
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
			for (Tuple2D<Integer> neighbour : tmpSet) {
				if (this.tensionGrid[neighbour.getX()][neighbour.getY()] < currTension
						|| this.spaceGrid[neighbour.getX()][neighbour.getY()]==0) {
					smallerTensionFlag = true;
				}
			}
			neighboursMap.put(distance, tmpSet);
		}
		
		//select space to move to, in case there is more than one

		List<Tuple2D<Integer>> helperPath = new ArrayList<Tuple2D<Integer>>();
		Set<Tuple2D<Integer>> tmpSet = new HashSet<Tuple2D<Integer>>(neighboursMap.get(distance));
		for (Tuple2D<Integer> neighbour: neighboursMap.get(distance)) {
			if ((this.tensionGrid[neighbour.getX()][neighbour.getY()]>=currTension)) {
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
			System.out.println("Distance to origin = " + distance);
			Tuple2D<Integer> helperPos = helperPath.get(helperPath.size()-1).clone();
			System.out.println("Current helper position = " + helperPos);
			tmpSet = new HashSet<Tuple2D<Integer>>(neighboursMap.get(distance));
			System.out.println("Distance set size = " + tmpSet.size());
			tmpSet.retainAll(this.topology.getPositionNeighbours
					(helperPos.getX(), helperPos.getY(), 1, 1));
			System.out.println("Distance set size after intersection = " + tmpSet.size());
			if (tmpSet.size() > 1) {
				int random = this.generator.nextInt(tmpSet.size());
				helperPath.add(new ArrayList<Tuple2D<Integer>>(tmpSet).get(random));
				System.out.println("Selected random OK");
			} else if (tmpSet.size() == 1) { 
				helperPath.add(new ArrayList<Tuple2D<Integer>>(tmpSet).get(0));
				System.out.println("Selected one OK");
			} else {
				System.out.println("Traceback");
				distance += 1;
				tmpSet = new HashSet<Tuple2D<Integer>>(neighboursMap.get(distance));
				System.out.println("Traceback - Distance set size = " + tmpSet.size());
				tmpSet.retainAll(this.topology.getPositionNeighbours
						(helperPos.getX(), helperPos.getY(), 1, 1));
				System.out.println("Traceback - Distance set size after intersection = " + tmpSet.size());
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
		this.setTotalTension(x, y);
		float sign = (value == 0) ? -1 : 1;
		for (int distance = 1; distance <= maxDist; distance ++) {
			float weight = this.getDistanceWeight(distance);
			float effect = sign/(6*distance)*weight;
			Set<Tuple2D<Integer>> tuples2update = this.topology.getPositionNeighbours(x, y, distance, distance);
			for (Tuple2D<Integer> tuple2update : tuples2update) {
				this.tensionGrid[tuple2update.getX()][tuple2update.getY()] += effect;
				if (this.tensionGrid[tuple2update.getX()][tuple2update.getY()] < 0) {
					this.tensionGrid[tuple2update.getX()][tuple2update.getY()] = 0;
					break;
				}
			}
		}
	}
		
	public byte[][] getSpaceGrid() {
		return this.spaceGrid;
	}
	
	public float[][] getTensionGrid() {
		return this.tensionGrid;
	}
	
	public EpitheliumTopology clone() {
		return new EpitheliumTopology(this.topology.clone(), 
				this.spaceGrid.clone(), 
				this.tensionGrid.clone());
	}

}
