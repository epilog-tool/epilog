package org.epilogtool.core.algorithms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.epilogtool.common.Tuple2D;

public class Graph {
	private final List<Vertex> vertexes;
	private final List<Edge> edges;
	private 	Map<Tuple2D<Integer>, Vertex> mTuple2Vertex;


	public Graph(List<Vertex> vertexes, List<Edge> edges) {
		this.vertexes = vertexes;
		this.edges = edges;
		this.mTuple2Vertex = new HashMap<Tuple2D<Integer>, Vertex>();
	}

	public List<Vertex> getVertexes() {
		return vertexes;
	}

	public List<Edge> getEdges() {
		return edges;
	}

	public void setMTuple2Vertex(Tuple2D<Integer> tuple,Vertex vertex) {
		this.mTuple2Vertex.put(tuple, vertex);
	}

	public void setMTuple2VertexAll(Map<Tuple2D<Integer>, Vertex> mTuple2Vertex) {
		this.mTuple2Vertex = mTuple2Vertex;
	}

	public Vertex getMTuple2Vertex(Tuple2D<Integer> tuple) {
		return this.mTuple2Vertex.get(tuple);
	}

	private List<Tuple2D<Integer>> getNeighbors(Tuple2D<Integer> tuple) {
		//Make sure that this is shuffled
		Vertex node = this.mTuple2Vertex.get(tuple);
		List<Tuple2D<Integer>> neighbours = new ArrayList<Tuple2D<Integer>>();
		for (Edge edge : this.edges) {
			if (edge.getSource().equals(node)) {
				neighbours.add(edge.getDestination().getTuple());
			}
		}
		return neighbours;
	}

	public List<Tuple2D<Integer>> getNeighbours(Tuple2D<Integer> tuple, int distance){
//		System.out.println("getting neighbrous at distance: " + distance);
		List<Tuple2D<Integer>> neighbours = new ArrayList<Tuple2D<Integer>>();
		//    		List<Tuple2D<Integer>> neighbours_aux = new ArrayList<Tuple2D<Integer>>();

		neighbours.addAll(getNeighbors(tuple));
		//    		neighbours_aux.addAll(getNeighbors(tuple));

		for (int i = 2; i<=distance; i++) {
			List<Tuple2D<Integer>> neighbours_aux = new ArrayList<Tuple2D<Integer>>();
			for (Tuple2D<Integer> tuple_aux: neighbours) {
				for (Tuple2D<Integer> neiTuple: getNeighbors(tuple_aux)) {
					if (!neighbours_aux.contains(neiTuple)) neighbours_aux.add(neiTuple);
				}
			}

			neighbours_aux.remove(neighbours);
			for (Tuple2D<Integer> tuple_aux: neighbours) {
				if (neighbours_aux.contains(tuple_aux)) neighbours_aux.remove(tuple_aux);
			}
			if (neighbours_aux.contains(tuple)) neighbours_aux.remove(tuple);
//			System.out.println("neighbours: " + neighbours);
//			System.out.println("neighbours_aux: " + neighbours_aux);
			neighbours = neighbours_aux;
		}

		if (neighbours.contains(tuple)) neighbours.remove(tuple);

//		System.out.println(tuple);
//		System.out.println(neighbours);
		return neighbours;
	}

}