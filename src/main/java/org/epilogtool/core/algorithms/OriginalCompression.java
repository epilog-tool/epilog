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
	private int range;
	protected String name;
	private Random random;
	private EpitheliumGrid nextGrid;
	
	
	public LinkedList<Vertex> originalCompression(Graph graph, LivingCell lCell, EpitheliumGrid nextGrid, int range, Random random){
		
		this.range = range;
		this.mTuple2Compression  = new HashMap<Tuple2D<Integer>, Double>();
		this.random = random; 
		this.nextGrid = nextGrid;
		
		LinkedList<Vertex> path = new LinkedList<Vertex>();
		System.out.println("w: " + lCell);
		System.out.println("w: " + lCell.getTuple());
		path.add(0,graph.getMTuple2Vertex(lCell.getTuple()));
		System.out.println("w0.0: " + path.get(0).getTuple());
		System.out.println("w0.1: " + this.nextGrid.getAbstCell(path.get(0).getTuple()));
		
		Tuple2D<Integer> succ = getNextCellInPath(lCell,path, graph);
		if (succ.equals(lCell.getTuple())) {
			return new LinkedList<Vertex>();
		}
		path.add(graph.getMTuple2Vertex(succ));
		
		while (!nextGrid.getAbstCell(succ).isEmptyCell()) {
			succ = getNextCellInPath(nextGrid.getAbstCell(succ),path,graph);
			if (succ.equals(lCell.getTuple())) {
				return new LinkedList<Vertex>();
			}
			path.add(graph.getMTuple2Vertex(succ));
//			System.out.println("succ: " + succ);
			if (this.nextGrid.getAbstCell(succ).isEmptyCell()) break;
		}

//		System.out.print ("path: " );
//		for (Vertex v: path) {
//			System.out.print (v.getTuple() +" ");
//		}
////		Collections.reverse(path);
//		System.out.println(" ");
		System.out.println("w0: " + this.nextGrid.getAbstCell(path.get(0).getTuple()));
		return path;
	}
	
	private Tuple2D<Integer> getNextCellInPath(AbstractCell cell, LinkedList<Vertex> path,Graph graph) {
		
//		System.out.print ("path: " );
//		for (Vertex v: path) {
//			System.out.print (v.getTuple() +" ");
//		}
//		System.out.println("");
		
		double minCompression = -1;
		List<Tuple2D<Integer>> lstSucessor = new ArrayList<Tuple2D<Integer>>();
//		System.out.println("Measuring compression for tuple: " + cell.getTuple());
		
		for (Tuple2D<Integer> neiTuple: this.nextGrid.getNeighbours(1, 1, cell.getTuple())) {
			
			if (this.nextGrid.getAbstCell(neiTuple).isInvalidCell()) continue;
			if (path.contains(graph.getMTuple2Vertex(neiTuple))) {
//				System.out.println("this tuple is already on the path: " + neiTuple);
				continue;
			}
			
			if (!this.mTuple2Compression.containsKey(neiTuple)) {
				this.mTuple2Compression.put(neiTuple, getCompressionValue(neiTuple));
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
		if (lstSucessor.size()>0) {
			Collections.shuffle(lstSucessor, this.random);
			return lstSucessor.get(0);
		}
		return cell.getTuple();
	}
	
	private double getCompressionValue(Tuple2D<Integer> tuple) {

		AbstractCell cell = this.nextGrid.getAbstCell(tuple.getX(), tuple.getY());
		int deadRange = 2;
		
		if (cell.isLivingCell()) {
//			System.out.println("I am a living cell");
			return getCompressionlevel(this.range, tuple);
		}
		else if (cell.isDeadCell())
			return getCompressionlevel(deadRange, tuple);
		return 0;
	}
	
	private double getCompressionlevel(int range, Tuple2D<Integer> tuple) {

		double p = 0.3;
		double compression = 0;
		for (int d = 1; d<=range; d++) {
			int cardinalNumber = 0;
			//TODO: Remove the cells that are not connected
			for (Tuple2D<Integer> nei: this.nextGrid.getNeighbours(d, d,tuple)) {
				if (this.nextGrid.getAbstCell(nei.getX(), nei.getY()).isDeadCell() || this.nextGrid.getAbstCell(nei.getX(), nei.getY()).isLivingCell()) {
					cardinalNumber = cardinalNumber + 1;
				}
			}
//			System.out.println("distance: " + d + " -> The cardinal number for tuple "+ tuple + " is: " + cardinalNumber);
			float w = (1-d)/(float) range + 1;
			double f = Math.exp(p*(6*d)-cardinalNumber);
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
