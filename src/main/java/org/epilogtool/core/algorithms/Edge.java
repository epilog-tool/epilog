package org.epilogtool.core.algorithms;

import org.epilogtool.common.Tuple2D;

public class Edge  {
    private Tuple2D<Integer>  id;
    private final Vertex source;
    private final Vertex destination;
    private final int weight;

    public Edge(Tuple2D<Integer> id, Vertex source, Vertex destination, int weight) {
        this.id = id;
        this.source = source;
        this.destination = destination;
        this.weight = weight;
    }

    public Tuple2D<Integer> getId() {
        return id;
    }
    public Vertex getDestination() {
        return destination;
    }

    public Vertex getSource() {
        return source;
    }
    public int getWeight() {
        return weight;
    }

    @Override
    public String toString() {
        return source + " " + destination;
    }


}