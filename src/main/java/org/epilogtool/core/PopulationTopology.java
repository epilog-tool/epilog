package org.epilogtool.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.epilogtool.common.Tuple2D;
import org.epilogtool.core.topology.Topology;

public class PopulationTopology {
	
	private byte[][] spaceGrid;
	private float[][] tensionGrid;
	private Topology topology;
	private float maxDist;
	private Random generator;

	public PopulationTopology(EpitheliumGrid grid) {
		this.topology = grid.getTopology().clone();
		this.maxDist = (float) Math.min(this.topology.getX(), this.topology.getY())/4;
		this.generator = new Random();
		this.buildSpaceGrid(grid);
		this.buildTensionGrid();
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
				this.setTension(x, y);
			}
		}
	}
	
	private void setTension(int x, int y){
		float totalTension = 0;
		if (this.spaceGrid[x][y]==0) {
			this.tensionGrid[x][y] = totalTension;
			return;
		}
		for (int distance = 1; distance <= this.maxDist; distance ++) {
			float distanceWeight = this.getDistanceWeight(distance);
			float distTension = (float) 0.0;
			Set<Tuple2D<Integer>> neighbours = this.topology.getPositionNeighbours(x, y, distance, distance);
			for (Tuple2D<Integer> neighbour: neighbours) {
				if (this.spaceGrid[neighbour.getX()][neighbour.getY()]==1) {
					distTension +=1;
				}
			}
			totalTension += distTension/neighbours.size()*distanceWeight;
		}
		this.tensionGrid[x][y] = totalTension;
	}
	
	private float getDistanceWeight(int distance) {
		return (1/this.maxDist)*(1-distance)+1;
	}
	
	public List<Tuple2D<Integer>> path2Border(int x, int y) {
		Tuple2D<Integer> currPosition = new Tuple2D<Integer>(x, y);
		List<Tuple2D<Integer>> path = new ArrayList<Tuple2D<Integer>>();
		path.add(currPosition);
		while (!(this.spaceGrid[currPosition.getX()][currPosition.getY()]==0)) {
			float currTension = this.tensionGrid[currPosition.getX()][currPosition.getY()];
			Set<Tuple2D<Integer>> neighbours = 
					this.topology.getPositionNeighbours(currPosition.getX(), currPosition.getY(), 1, 1);
			Set<Tuple2D<Integer>> possiblePaths = new HashSet<Tuple2D<Integer>>();
			for (Tuple2D<Integer> neighbour : neighbours) {
				if (this.tensionGrid[neighbour.getX()][neighbour.getY()] < currTension) {
					possiblePaths.add(neighbour);
				}
			}
			if (possiblePaths.size() == 0) {
				path = this.path2BorderHelper(path);
				currPosition = path.get(path.size()-1);
				continue;
			}
			possiblePaths.removeAll(path);
			if (possiblePaths.size() > 1) {
				int random = this.generator.nextInt(possiblePaths.size());
				currPosition = new ArrayList<Tuple2D<Integer>>(possiblePaths).get(random);
			} else {
				currPosition = new ArrayList<Tuple2D<Integer>>(possiblePaths).get(0);
			}
			path.add(currPosition);
		}
		return path;
	}
	
	private List<Tuple2D<Integer>> path2BorderHelper(List<Tuple2D<Integer>> path) {
		
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
			for (Tuple2D<Integer> neighbour : tmpSet) {
				if (this.tensionGrid[neighbour.getX()][neighbour.getY()] < currTension) {
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
			Tuple2D<Integer> helperPos = helperPath.get(helperPath.size()-1);
			tmpSet = new HashSet<Tuple2D<Integer>>(neighboursMap.get(distance));
			tmpSet.retainAll(this.topology.getPositionNeighbours
					(helperPos.getX(), helperPos.getY(), 1, 1));
			if (tmpSet.size() > 1) {
				int random = this.generator.nextInt(tmpSet.size());
				helperPath.add(new ArrayList<Tuple2D<Integer>>(tmpSet).get(random));
			} else { 
				helperPath.add(new ArrayList<Tuple2D<Integer>>(tmpSet).get(0));
			}
			distance -= 1;
		}
		Collections.reverse(helperPath);
		path.addAll(helperPath);
		return path;
	}
	
	public List<Tuple2D<Integer>> linearPath2Border(int x, int y) {
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
	
	private void updatePopulationTopology(int x, int y, byte value) {
		if (this.spaceGrid[x][y]==value) {
			return;
		}
		this.spaceGrid[x][y] = value;
		for (int distance = 1; distance <= maxDist; distance ++) {
			Set<Tuple2D<Integer>> tuples2update = this.topology.getPositionNeighbours(x, y, distance, distance);
			for (Tuple2D<Integer> tuple2update : tuples2update) {
				this.setTension(tuple2update.getX(), tuple2update.getY());
			}
		}
		
	}
		
	public byte[][] getSpaceGrid() {
		return this.spaceGrid;
	}
	
	public float[][] getTensionGrid() {
		return this.tensionGrid;
	}

}
