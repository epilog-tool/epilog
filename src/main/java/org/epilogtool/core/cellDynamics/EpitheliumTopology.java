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
		this.maxDist = (int) Math.max(this.topology.getX(), this.topology.getY())/3;
		this.generator = new Random();
		this.buildSpaceGrid(epi.getEpitheliumGrid());
		this.buildTensionGrid();
	}
	
	private EpitheliumTopology(Topology topology, byte[][] 
			spaceGrid, float[][] tensionGrid) {
		this.topology = topology;
		this.spaceGrid = spaceGrid;
		this.tensionGrid = tensionGrid;
		this.maxDist = (int) Math.max(this.topology.getX(), this.topology.getY())/4;
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
		float totalTension = 0;
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
	
	public List<Tuple2D<Integer>> divisionPath(int x, int y) {
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
			if (possiblePaths.size()==0) {
				path = this.divisionPathHelper(path);
				currPosition = path.get(path.size()-1);
				continue;
			}
			possiblePaths.removeAll(path);
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
		this.setTotalTension(x, y);
		this.spaceGrid[x][y] = value;
		int sign = (value == 0) ? -1 : 1;
		for (int distance = 1; distance <= maxDist; distance ++) {
			float weight = this.getDistanceWeight(distance);
			float effect = sign/(6*distance)*weight;
			Set<Tuple2D<Integer>> tuples2update = this.topology.getPositionNeighbours(x, y, distance, distance);
			for (Tuple2D<Integer> tuple2update : tuples2update) {
				this.tensionGrid[tuple2update.getX()][tuple2update.getY()] += effect;
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
