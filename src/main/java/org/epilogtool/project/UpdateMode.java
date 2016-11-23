package org.epilogtool.project;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.epilogtool.common.Tuple2D;

/**
 * Abstract method that defines the inter-cellular updating policy
 * 
 * 
 */

public abstract class UpdateMode {
	
	
	public abstract String getDescription();

	  public static List<Integer> shuffleAndSelect(int numberCellsGrid, int numberCellsToUpdate) {
		  List<Integer> arrayNumbers = new ArrayList<Integer>(); //array with numberCellsGrid boxes
		  List<Integer> shufledAndCutArray = new ArrayList<Integer>();  // arrayNumbers shufled and cut up to numberCellsToUpdate
		  
		  for (int instance = 0; instance < numberCellsGrid; instance++){
			  arrayNumbers.add(instance);
		  }
		  
		  Collections.shuffle(arrayNumbers);

		  for (int n =0; n<numberCellsToUpdate; n++){
			  shufledAndCutArray.add(arrayNumbers.get(n));
		  }
		  return shufledAndCutArray;
	  }
	  
		public static List<Integer> selectInstances(List<Integer> schuffledInstances, int numberCells) {

			List<Integer> b = schuffledInstances;
			List<Integer> a = new ArrayList<Integer>();
			
			for (int n = 0; n< numberCells;n++){
				a.add(b.get(n));
			}
			return a;
		}
		
		public static List<Integer> findMinIdx(List<Double> exponentialInstances, int numberCells) {
		    if (exponentialInstances == null || exponentialInstances.size() == 0) return null; // Saves time for empty array
		    List<Integer> b =  new ArrayList<Integer>();

		    Double minVal =  Collections.max(exponentialInstances);// Keeps a running count of the smallest value so far
		    Double minVal_Aux = (double) 0; 
		    for (int n = 0; n<numberCells; n++){
		    	int minIdx = 0;
		    	minVal =  Collections.max(exponentialInstances);
			    for(int idx=0; idx<exponentialInstances.size(); idx++) {
			    	
			        if(exponentialInstances.get(idx) <= minVal && exponentialInstances.get(idx)>minVal_Aux) {
			            minVal = exponentialInstances.get(idx);
			            minIdx = idx;
			        }
			    }
			    minVal_Aux = minVal;
			    b.add(minIdx);
		    }
		    return b;
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

	

