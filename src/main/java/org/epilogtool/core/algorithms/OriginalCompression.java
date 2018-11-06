package org.epilogtool.core.algorithms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;
import java.util.List;

import org.epilogtool.common.Tuple2D;
import org.epilogtool.core.EpitheliumGrid;
import org.epilogtool.core.cell.AbstractCell;
import org.epilogtool.core.cell.LivingCell;


public class OriginalCompression extends AbstractAlgorithm {
	
	private Map<Tuple2D<Integer>, Double> mTuple2Compression;
	private int neighboursRange;
	protected String name;
	private Random random;
	private EpitheliumGrid nextGrid;
	private Graph graph; 
	private float compressionParameter;
	private int deadNeighboursRange;
	
	
	public LinkedList<Vertex> originalCompression(Graph compressionGraph, Graph graph, LivingCell lCell, EpitheliumGrid nextGrid, int range, int deadNeighboursRange,float compressionParameter,Random random){
		
		this.neighboursRange = range;
		this.mTuple2Compression  = new HashMap<Tuple2D<Integer>, Double>();
		this.random = random; 
		this.nextGrid = nextGrid;
		this.graph = graph;
		this.compressionParameter = compressionParameter;
		this.deadNeighboursRange = deadNeighboursRange;
		
		
		LinkedList<Vertex> path = new LinkedList<Vertex>();
		path.add(0,graph.getMTuple2Vertex(lCell.getTuple()));

		
		Tuple2D<Integer> succ = getNextCellInPath(compressionGraph,lCell,path, graph);
		
		if (succ.equals(lCell.getTuple())) {
			return new LinkedList<Vertex>();
		}
		
		path.add(graph.getMTuple2Vertex(succ));
		
		while (!nextGrid.getAbstCell(succ).isEmptyCell()) {
			succ = getNextCellInPath( compressionGraph,nextGrid.getAbstCell(succ),path,graph);
//			if (succ.getX()==lCell.getTuple().getX() &&  succ.getY()==lCell.getTuple().getY()) {
//				path = new LinkedList<Vertex>();;
//			}
//			else { 
			path.add(graph.getMTuple2Vertex(succ));
//			System.out.println("path.size(): " + path.size());
			if (this.nextGrid.getAbstCell(succ).isEmptyCell()) break;
			if (path.size()>1000) break;
//			}
		}
		return path;
	}
	
	private Tuple2D<Integer> getNextCellInPath(Graph compressionGraph,AbstractCell cell, LinkedList<Vertex> path,Graph graph) {

		
		double minCompression = -1;
		List<Tuple2D<Integer>> lstSucessor = new ArrayList<Tuple2D<Integer>>();
		for (Tuple2D<Integer> neiTuple: this.graph.getNeighbours(cell.getTuple(),1)) {
			if (path.contains(graph.getMTuple2Vertex(neiTuple))) {
				continue;
			}
			
			if (!this.mTuple2Compression.containsKey(neiTuple)) {
				this.mTuple2Compression.put(neiTuple, getCompressionValue(compressionGraph,neiTuple));
			}
			
			if (minCompression==-1)
				minCompression = this.mTuple2Compression.get(neiTuple);
			
			//add all the neighbours with the minimum compression value
			if (this.mTuple2Compression.get(neiTuple)<minCompression) {
				lstSucessor = new ArrayList<Tuple2D<Integer>>();
				minCompression = this.mTuple2Compression.get(neiTuple);
			}
			if (this.mTuple2Compression.get(neiTuple)<=minCompression) {
//				System.out.println("Tbe new sucessor is: " + neiTuple);
				lstSucessor.add(neiTuple);
			}
				
		}
//		System.out.println("minCompression: " + minCompression);
//		System.out.println("number of possible destinations is: " + lstSucessor.size());
		if (lstSucessor.size()>0) {
			Collections.shuffle(lstSucessor, this.random);
			return lstSucessor.get(0);
		}
//		System.out.println("There are no solutions. Returning: " +cell.getTuple());
		return cell.getTuple();
	}
	
	private double getCompressionValue(Graph compressionGraph,Tuple2D<Integer> tuple) {

		AbstractCell cell = this.nextGrid.getAbstCell(tuple.getX(), tuple.getY());
		
		if (cell.isLivingCell()) {
//			System.out.println("I am a living cell");
			return getCompressionlevel(compressionGraph,this.neighboursRange, tuple);
		}
		else if (cell.isDeadCell())
			return getCompressionlevel(compressionGraph,this.deadNeighboursRange, tuple);
		return 0;
	}
	
	private double getCompressionlevel(Graph compressionGraph, int range, Tuple2D<Integer> tuple) {

		double compression = 0;
		if (this.nextGrid.getAbstCell(tuple).isEmptyCell())
			return 0;
		for (int d = 1; d<=range; d++) {
			List <Tuple2D<Integer>> aux = compressionGraph.getNeighbours(tuple, d);
			int cardinalNumber =aux.size();
//			System.out.println("distance: " + d + " -> The cardinal number for tuple "+ tuple + " is: " + cardinalNumber);
			float w = (1-d)/(float) range + 1;
			double f = Math.exp(this.compressionParameter*(6*d)-cardinalNumber);
//			System.out.println("distance: " + d + " -> w for tuple "+ tuple + " is: " + w);
//			System.out.println("distance: " + d + " -> f for tuple "+ tuple + " is: " + f);
//			System.out.println("compression at : " + d + " -> c for tuple "+ tuple + " is: " + w/f);
			compression = compression + w/f;
		}
//		System.out.println("compression for the tuple: " + tuple + " is: " + compression);
		return compression;
	}
	
	public String getName() {
		return this.name;
	}

	
	
	
	
	
	@Override
	public AbstractAlgorithm clone() {
		// TODO Auto-generated method stub
		return null;
	}
}
