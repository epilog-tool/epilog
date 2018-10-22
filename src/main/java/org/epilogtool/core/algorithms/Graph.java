package org.epilogtool.core.algorithms;

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


}