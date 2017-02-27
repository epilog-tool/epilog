package org.cellularasynchrony;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
//import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;


public class Topology implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3713765674642632574L;

//	static List<Integer> neighbors1 = Collections
//			.synchronizedSet(new HashSet<Integer>());;
//	static List<Integer> neighborsaux1 = Collections
//			.synchronizedSet(new HashSet<Integer>());

	public int width;
	public int height;
	private int[][] neighbors_x = { { -1, +1, 0, 0, -1, -1 },
			{ -1, 1, 0, 0, 1, 1 } };
	private int[] neighbors_y = { 0, 0, -1, 1, -1, 1 };
	private String rollOver;
	public int beta;
	public String updateMode;
	public String onlyUpdatable;
	private Utils utils = null;
	private List<Integer> updatableCells;
	
	Boolean purple = true;
	Boolean green = true;
	Boolean blue = true;
	Boolean red = true;
	Boolean yellow = true;
	Boolean orange = true;
	

	/**
	 * Defines the topology of the epithelium model.
	 * 
	 * @param width
	 *            width of the hexagons grid
	 * @param height
	 *            height of the hexagons grid
	 * 
	 */
	public Topology(int width, int height) {
		super();

		this.width = width;
		this.height = height;
		this.utils = utils;
		List<Integer> updatableCells = new ArrayList<Integer>();

	}
	
	/**
	 * Returns the active roll-over option.
	 * 
	 * @return roll over option as string
	 * 
	 */
	public String getRollOver() {
		return this.rollOver;
	}
	
	public void SetBlue(boolean b){
		blue=b;
	}
	
	public void SetGreen(boolean b){
		green=b;
	}
	
	public void SetRed(boolean b){
		red=b;
	}
	
	public void SetYellow(boolean b){
		yellow=b;
	}
	
	public void SetOrange(boolean b){
		orange=b;
	}
	
	public void SetPurple(boolean b){
		purple=b;
	}

	/**
	 * Sets a new height for the hexagons grid.
	 * 
	 * @param height
	 *            new height
	 * 
	 */
	public void setHeight(int height) {
		if (width == 1 & height == 1)
			this.width = 2;
			this.height = height;

	}

	/**
	 * Sets a new width for the hexagons grid.
	 * 
	 * @param width
	 *            new width
	 * 
	 */
	public void setWidth(int width) {
		if (width == 1 & height == 1)
			this.width = 2;
		else
			this.width = width;
	}

	/**
	 * Returns the grid's width.
	 * 
	 * @return width
	 * 
	 */
	public int getWidth() {
		return this.width;
	}

	/**
	 * Returns the grid's height.
	 * 
	 * @return height
	 * 
	 */
	public int getHeight() {
		return this.height;
	}

	/**
	 * Returns the number of instances.
	 * 
	 * @return number of instances
	 * 
	 */
	public int getNumberInstances() {
		return this.height * this.width;
	}

	/* Methods to transform instance index to coordinates and vice-versa */

	/**
	 * Translates and instance number into a corresponding coordinate of the
	 * x-axis
	 * 
	 * @param instance
	 * @return x axis coordinate
	 * 
	 */
	public int instance2i(int instance) {

		int i = instance % getWidth();
		return i;
	}

	/**
	 * Translates and instance number into a corresponding coordinate of the
	 * y-axis
	 * 
	 * @param instance
	 * @return y axis coordinate
	 * 
	 */
	public int instance2j(int instance) {

		int j = 0;
		if (instance != 0)
			j = instance / getWidth();

		return j;
	}

	/**
	 * Translates into an instance number the x and y coordinates.
	 * 
	 * @param i
	 *            x coordinate
	 * @param j
	 *            y coordinate
	 * @return instance number
	 * 
	 */
	public int coords2Instance(int i, int j) {
		return j * getWidth() + i;
	}

	// Neighbours

	/**
	 * Calls the iterative method to determine the set of neighours at a
	 * distances
	 * 
	 * @param instance
	 *            instance that has neighbours
	 * @param distance
	 *            neighbours distance
	 * @return set of neighbours
	 * @see nDistanceNeighbours(int instance, int distance)
	 * 
	 */
//	public List<Integer> groupNeighbors(int instance, int distance) {
//		return getNeighboursTotal(instance, distance);
//	}

//	/**
//	 * Determines if two instances are neighbours at a distance
//	 * 
//	 * @param instanceA
//	 *            instance to compare
//	 * @param instanceB
//	 *            instance to compare
//	 * @return true if they are neighbours, false otherwise
//	 * 
//	 */
//	public boolean areNeighbors(int instanceA, int instanceB, int distance) {
//
//		return (groupNeighbors(instanceA, distance).contains(instanceB));
//
//	}

	// ROLL OVER

	public void setRollOver(String rollOver) {
		this.rollOver = rollOver;
	}

	
	public boolean isEven(int x) {
		return x%2==0? true:false;
	}

	/**
	 * Determines neighbours at a distance of an instance
	 * 
	 * @param instance
	 *            instance that has neighbours
	 * @param distance
	 *            neighbours distance
	 * @return set of neighbours
	 * 
	 * 
	 */

	public List<Integer> getNeighboursTotal(int instance, int distance) {

		List<Integer> neighboursTotal = new ArrayList<Integer>();
		
		for (int d = 1;d<=distance;d++){
//			System.out.println("Para uma distancia de: "+d);
			List<Integer> neighbours= this.getNeighbours(instance,
					d);
			for (int index = 0;index<neighbours.size();index ++){
				
				if (!neighboursTotal.contains(neighbours.get(index)) && neighbours.get(index) != instance)
					neighboursTotal.add(neighbours.get(index));
			}
		}
		
//		System.out.println("A instancia : "+instance +  "  tem como vizinhos: "+neighboursTotal);
//		
//		System.out.println("Size of neighboursTotal: " + neighboursTotal);
		neighboursTotal = betaNeighboursReduction(neighboursTotal);
		return neighboursTotal;

	}

	private List<Integer> betaNeighboursReduction(List<Integer> completeSetofNeighbours) {
		
		List<Integer> betaNeighbours = new ArrayList<Integer>();
		List<Integer> betaSetofNeighbours = new ArrayList<Integer>();
		
		betaNeighbours = getBetaNeighbours();
//		System.out.println("betaNeighbours: "+betaNeighbours.size());
		
		for (int instance:completeSetofNeighbours){
			if (betaNeighbours.contains(instance))
				betaSetofNeighbours.add(instance);
		}
		
//		System.out.println("beta set of neihbout"+betaSetofNeighbours);
		return betaSetofNeighbours;
	}

	private List<Integer> getBetaNeighbours() {
		List<Integer> updatableCellsList = new ArrayList<Integer>();

		int numberCells;
		if (this.onlyUpdatable =="Updatable Cells"){
			numberCells = (int) Math.ceil(beta * updatableCells.size()/100);
			if (numberCells ==0)
				numberCells =1;
			updatableCellsList = utils.shuffleAndSelect(updatableCells, numberCells);
		}
		else{
			numberCells = (int) Math.ceil(beta * getNumberInstances()/100);
			if (numberCells ==0)
				numberCells =1;
			updatableCellsList = utils.shuffleAndSelect(getNumberInstances(), numberCells);
		}
		return updatableCellsList;
	}

	private List<Integer> getNeighbours(int instance, int distance) {
		
		List<Integer> neighbours =  new ArrayList<Integer>();
		
		int i = instance2i(instance);
		int j = instance2j(instance);
		
		if (isEven(i))
			neighbours = getEvenNeighbours(i,j,distance);
		else
			neighbours = getOddNeighbours(i,j,distance);
		
		return neighbours;
	}

	private List<Integer> getEvenNeighbours(int i, int j, int distance) {
		List<Integer> neighbours =  new ArrayList<Integer>();
		
		int t = 0;
		int s = 0;
	//(2)
		
		if (green){
		//Green Hexagons
//		System.out.println("Para a instancia: "+coords2Instance(i,j) +" "+i+" "+j);
		for (int d=1; d<=distance; d++)
		{	
			t = d;
			s = 0;
			
			if (isEven(d))
				s = -distance+d/2;
			else
				s = -distance + (int) (Math.ceil((d)/2));

//			System.out.println("Para o nivel " + d + " o s e t são: "+s +" "+t);
			
			int instance = rollOverTransform(i+t,j+s);

			if (instance != -1)
				if (!neighbours.contains(instance))
					neighbours.add(instance);

		}}
		
		//Yellow Hexagons
		if (yellow){
//		System.out.println("Para a instancia: "+coords2Instance(i,j));
		int s_inc = 0;
		for (int d=1; d<=distance; d++)
		{	
			
			t = distance;
			
			s = (int) (Math.floor((distance)/2))-s_inc;
			s_inc = s_inc+1;
			

//			System.out.println("Para o nivel " + d + " o s e t são: "+s +" "+t);
			
			int instance = rollOverTransform(i+t,j+s);

			if (instance != -1)
				if (!neighbours.contains(instance))
					neighbours.add(instance);
	
		}}
		
		//Orange Hexagons
		if (orange){
//		System.out.println("Para a instancia: "+coords2Instance(i,j));
		t = -1;
		int sOrange = 0;
		for (int d=1; d<=distance; d++)
		{	
			
			t = t+1;
			s = distance -sOrange;
			if (!isEven(d))
				sOrange = sOrange+1;
			

//			System.out.println("Para o nivel " + d + " o s e t são: "+s +" "+t);
			
			int instance = rollOverTransform(i+t,j+s);

			if (instance != -1)
				if (!neighbours.contains(instance))
					neighbours.add(instance);
	
		}}
		
		//Red Hexagons
		if (red){
//		System.out.println("Para a instancia: "+coords2Instance(i,j));
		int tRed = 0;
		int sRed = 0;
		for (int d=1; d<=distance; d++)
		{	
			
			t = tRed-distance;
			tRed = tRed + 1;
			
			s = (int) (Math.floor((distance)/2))+sRed;
			if (!isEven(d) && !isEven(distance))
				sRed = sRed+1;
			if (isEven(d) && isEven(distance))
				sRed = sRed+1;
			

//			System.out.println("Para o nivel " + d + " o s e t são: "+s +" "+t);
			
			int instance = rollOverTransform(i+t,j+s);

			if (instance != -1)
				if (!neighbours.contains(instance))
					neighbours.add(instance);
		}
		}
		
		
		//Purple Hexagons
//		System.out.println("Para a instancia: "+coords2Instance(i,j));
		if (purple){
		int tPurple = 0;
		int sPurple = 0;
		s = 0;
		for (int d=1; d<=distance; d++)
		{	
			
			t = -distance;
			
			if (isEven(distance))
				s = -distance/2+sPurple;
			else
				s = -distance/2+sPurple-1;
			sPurple = sPurple+1;
			

//			System.out.println("Para o nivel " + d + " o s e t são: "+s +" "+t);
			
			int instance = rollOverTransform(i+t,j+s);

			if (instance != -1)
				if (!neighbours.contains(instance))
					neighbours.add(instance);
	
		}}
		
		//Blue Hexagons
		if (blue){
//		System.out.println("Para a instancia: "+coords2Instance(i,j));
		int tBlue = 1;
		int sBlue = 0;
		for (int d=1; d<=distance; d++)
		{	
			
			t = tBlue -1;
			t = -t;
			tBlue = tBlue +1;
			s = -distance + sBlue;
			if (isEven(d))
				sBlue = sBlue +1;

//			System.out.println("Para o nivel " + d + " o s e t são: "+s +" "+t);
			
			int instance = rollOverTransform(i+t,j+s);

			if (instance != -1)
				if (!neighbours.contains(instance))
					neighbours.add(instance);
	
		}}
		
		return neighbours;
	}

	private List<Integer> getOddNeighbours(int i, int j, int distance) {
//(1)
		List<Integer> neighbours =  new ArrayList<Integer>();
		
		int t = 0;
		int s = 0;

		
		if (green){
		//Green Hexagons

		
		for (int d=1; d<=distance; d++)
		{	
			t = d;
			s = 0;
			
			if (isEven(d))
				s = -distance+d/2;
			else
				s = -distance + 1+(int) (Math.floor((d)/2));
			
//			System.out.println("Para o nivel " + d + " o s e t são: "+s +" "+t);
			
			int instance = rollOverTransform(i+t,j+s);
			
			if (instance != -1)
				if (!neighbours.contains(instance))
					neighbours.add(instance);
//			if ((i==3) && (j==2))
//				System.out.println("The green neighbours are: "+ neighbours);
	
		}}
		
//		//Yellow Hexagons
		if (yellow){
//		System.out.println("Para a instancia: "+coords2Instance(i,j));
		int s_inc = 0;
		for (int d=1; d<=distance; d++)
		{	
			
			t = distance;
			s = 0;
			
			if (!isEven(distance))
				s = distance/2-s_inc+1;
			if (isEven(distance))
				s = distance/2-s_inc;
			s_inc = s_inc+1;
			

//			System.out.println("Para o nivel " + d + " o s e t são: "+s +" "+t);
			
			int instance = rollOverTransform(i+t,j+s);
			
			if (instance != -1)
				if (!neighbours.contains(instance))
					neighbours.add(instance);
		}
		}
		
		//Orange Hexagons
		if (orange){
//		System.out.println("Para a instancia: "+coords2Instance(i,j));
		t = -1;
		for (int d=1; d<=distance; d++)
		{		
			t = t+1;
			s = distance;

//			System.out.println("Para o nivel " + d + " o s e t são: "+s +" "+t);
			
			int instance = rollOverTransform(i+t,j+s);

			if (instance != -1)
				if (!neighbours.contains(instance))
					neighbours.add(instance);
	
		}
		}
		//Red Hexagons
		if (red){
//		System.out.println("Para a instancia: "+coords2Instance(i,j));
		int tRed = 0;
		int sRed = 0;
		for (int d=1; d<=distance; d++)
		{	
			
			t = tRed-distance;
			tRed = tRed + 1;
			if (!isEven(d) && !isEven(distance))
				sRed = sRed+1;
			if (isEven(d) && isEven(distance))
				sRed = sRed+1;
			s = (int) (Math.ceil(distance/2))+sRed;
			
			

//			System.out.println("Para o nivel " + d + " o s e t são: "+s +" "+t);
			
			int instance = rollOverTransform(i+t,j+s);

			if (instance != -1)
				if (!neighbours.contains(instance))
					neighbours.add(instance);
	
		}}
		
		
		//Purple Hexagons
		if (purple){
//		System.out.println("Para a instancia: "+coords2Instance(i,j));
		int tPurple = 0;
		int sPurple = 0;
		
		for (int d=1; d<=distance; d++)
		{	
			
			t = -distance;
			

			s = -distance/2+sPurple;
			sPurple = sPurple+1;
			

//			System.out.println("Para o nivel " + d + " o s e t são: "+s +" "+t);
			
			int instance = rollOverTransform(i+t,j+s);

			if (instance != -1)
				if (!neighbours.contains(instance))
					neighbours.add(instance);
	
		}}
		
		//Blue Hexagons
		if (blue){
//		System.out.println("Para a instancia: "+coords2Instance(i,j));
		int tBlue = 1;
		int sBlue = 0;
		for (int d=1; d<=distance; d++)
		{	
			
			t = tBlue -1;
			t = -t;
			tBlue = tBlue +1;
			s = -distance + sBlue;
			if (!isEven(d))
				sBlue = sBlue +1;

//			System.out.println("Para o nivel " + d + " o s e t são: "+s +" "+t);
			
			int instance = rollOverTransform(i+t,j+s);

			if (instance != -1)
				if (!neighbours.contains(instance))
					neighbours.add(instance);
	
		}}
		return neighbours;
	}

	private int rollOverTransform(int s, int t) {
		
		int x = getWidth();
		int y = getHeight();
			
		String option = getRollOver();
		
		if ((s>=0) && (t>=0) && (t<y) && (s<x)){
			int instance = coords2Instance(s, t);
			return instance;
		}
		
		if (option == "No Roll-Over" || option == null){
			if ((s<0) || (t<0) || (t>=y) || (s>=x)){
//				System.out.println ("Teste:"+s+" "+t);
			return -1;
		}}
		

		else if (option == "Vertical Roll-Over"){
			
			if ((s<0) || (s>=x))
				return -1;
			else if (t<0)
				t=t+x;
			else if (t>=x)
				t=t-x;
		}

		else if (option == "Double Roll-Over"){
			if (t<0)
				t=t+x;
			else if (t>=x)
				t=t-x;
			
			if (s<0)
				s=s+y;
			else if (s>=y)
				s=s-y;
		}

		else if (option == "Horizontal Roll-Over"){
			if ((t<0) || (t>=y))
				return -1;
			else if (s<0)
				s=s+y;
			else if (s>=y)
				s=s-y;
		}

		int instance = coords2Instance(s, t);
		
		return instance;
	}

	public void setBeta(int optionint) {
		beta = optionint;	
	}

	public void setUpdateMode(String aux) {
		this.updateMode = aux;
	}

	public void setOnlyUpdatable(String optionString) {
		this.onlyUpdatable = optionString;
		
	}

	
}
