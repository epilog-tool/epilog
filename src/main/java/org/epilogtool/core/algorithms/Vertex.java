package org.epilogtool.core.algorithms;

import org.epilogtool.common.Tuple2D;

public class Vertex {
    final private Tuple2D<Integer> tuple;


    public Vertex(Tuple2D<Integer> tuple) {
        this.tuple = tuple;
 
    }
    public Tuple2D<Integer> getTuple() {
        return this.tuple;
    }


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.tuple == null) ? 0 : this.tuple.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Vertex other = (Vertex) obj;
        if (this.tuple == null) {
            if (other.getTuple() != null)
                return false;
        } else if (!this.tuple.equals(other.getTuple()))
            return false;
            
        return true;
    }

}