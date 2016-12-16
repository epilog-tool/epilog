package org.epilogtool.project;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import org.epilogtool.common.Tuple2D;

/**
 * Abstract method that defines the inter-cellular updating policy
 * 
 * 
 */

public abstract class UpdateMode {
	
	
	public abstract String getDescription();

	
	public static List<Tuple2D> shuffle(Set<Tuple2D<Integer>> cells2update) {
		
		List<Tuple2D> listTuples = new ArrayList<Tuple2D>(); //array with numberCellsGrid boxes
		
		  for (Tuple2D key: cells2update){
			  listTuples.add(key);
		  }
		  
		  Collections.shuffle(listTuples);

		  return listTuples;
	  }
	
	public static Map<Tuple2D<Integer>, byte[]> shuffleAndSelect(HashMap<Tuple2D<Integer>, byte[]> cells2update, int numberCellsToUpdate) {
		
		List<Tuple2D> listTuples = new ArrayList<Tuple2D>(); //array with numberCellsGrid boxes
		Map<Tuple2D<Integer>, byte[]> shufledAndCutArray = new HashMap<Tuple2D<Integer>, byte[]>();  // arrayNumbers shufled and cut up to numberCellsToUpdate
		  
		  for (Tuple2D key: cells2update.keySet()){
			  listTuples.add(key);
		  }
		  
		  Collections.shuffle(listTuples);

		  for (int n =0; n<numberCellsToUpdate; n++){
			  Tuple2D key = listTuples.get(n);
			  shufledAndCutArray.put(key,cells2update.get(key));
		  }
		  return shufledAndCutArray;
	  }
	  

		
	
	public static Map<Tuple2D, Double> findMinIdx(Map<Tuple2D, Double> exponentialInstances, int numberCellsCalledToUpdate,
			Stack<Tuple2D<Integer>> keys) {
			
		// TODO Auto-generated method stub
		
		 if (exponentialInstances == null || exponentialInstances.size() == 0) return null;
		 
		 //We will look for cells that are in the key cells (cells called to update)
		 Map<Tuple2D, Double> exponentialInstancesCalledToUpdate  = new HashMap<Tuple2D, Double>();
		 Map<Tuple2D, Double> cutExponentialInstancesCalledToUpdate = new HashMap<Tuple2D, Double>();
		 
		 for (Tuple2D t: keys){
			 exponentialInstancesCalledToUpdate.put(t,exponentialInstances.get(t));
		 }
		 
		 Double minVal =  Collections.max(exponentialInstancesCalledToUpdate.values());// Keeps a running count of the smallest value so far
		 Double minVal_Aux = (double) 0; 
		 
		 for (int n = 0; n<numberCellsCalledToUpdate; n++){
			 Tuple2D<Integer> minIdx = new Tuple2D(0,0);
			 minVal =  Collections.max(exponentialInstancesCalledToUpdate.values());
			    for(Tuple2D<Integer> t: exponentialInstancesCalledToUpdate.keySet()) {
		    	
		        if(exponentialInstancesCalledToUpdate.get(t) <= minVal && exponentialInstancesCalledToUpdate.get(t)>minVal_Aux) {
		            minVal = exponentialInstancesCalledToUpdate.get(t);
		            minIdx = t;
		        }
		    }
			    minVal_Aux = minVal;
			    cutExponentialInstancesCalledToUpdate.put(minIdx, exponentialInstancesCalledToUpdate.get(minIdx));
		 }
		
		 
		return cutExponentialInstancesCalledToUpdate;
		}
		
	
	}

	

