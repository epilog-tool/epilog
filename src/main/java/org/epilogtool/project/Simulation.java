package org.epilogtool.project;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.colomoto.biolqm.LogicalModel;
import org.colomoto.biolqm.NodeInfo;
import org.colomoto.biolqm.modifier.perturbation.AbstractPerturbation;
import org.colomoto.biolqm.tool.simulation.multiplesuccessor.PriorityClasses;
import org.colomoto.biolqm.tool.simulation.multiplesuccessor.PriorityUpdater;
import org.epilogtool.common.EnumRandomSeed;
import org.epilogtool.common.RandCentral;
import org.epilogtool.common.Tuple2D;
import org.epilogtool.common.Txt;
import org.epilogtool.core.Epithelium;
import org.epilogtool.core.EpitheliumGrid;
import org.epilogtool.core.ModelCellularEvent;
import org.epilogtool.core.UpdateCells;
import org.epilogtool.core.algorithms.DijkstraAlgorithm;
import org.epilogtool.core.algorithms.Edge;
import org.epilogtool.core.algorithms.Graph;
import org.epilogtool.core.algorithms.OriginalCompression;
import org.epilogtool.core.algorithms.Vertex;
import org.epilogtool.core.cell.AbstractCell;
import org.epilogtool.core.cell.CellFactory;
import org.epilogtool.core.cell.EmptyCell;
import org.epilogtool.core.cell.LivingCell;
import org.epilogtool.integration.IFEvaluation;
import org.epilogtool.integration.IntegrationFunctionExpression;

/**
 * Initializes and implements the simulation on epilog.
 * 
 * @author Pedro T. Monteiro
 * @author Pedro L. Varela
 * @author Camila V. Ramos
 */
public class Simulation {
	
	private Epithelium epithelium;
	private List<EpitheliumGrid> gridHistory;
	private List<String> gridHashHistory;
	private Random random;
	private Graph graph;
	private EpitheliumGrid nextGrid;

	Map<Tuple2D<Integer>, List<Tuple2D<Integer>>> mTuple2Neighbours;//TO BE REMOVED FROM HERE

	private boolean stable;
	private boolean hasCycle;
	// Perturbed models cache - avoids repeatedly computing perturbations at
	// each step
	private Map<LogicalModel, Map<AbstractPerturbation, PriorityUpdater>> updaterCache;

	/**
	 * Initializes the simulation. It is called after creating and epithelium.
	 * Creates a list of EpitheliumGrids, to allow the user to travel to a
	 * previously calculated (and saved) epitheliumGrid. This list is
	 * initialized with the current EpitheliumGrid (the one defined in the
	 * initialConditions).
	 * 
	 * @param e
	 *            the epithelium the user is working with.
	 * 
	 */
	public Simulation(Epithelium e) {
		this.epithelium = e.clone();
		if (this.epithelium.getUpdateSchemeInter().getRandomSeedType().equals(EnumRandomSeed.RANDOM)) {
			this.random = RandCentral.getInstance().getNewGenerator();
		} else {
			this.random = RandCentral.getInstance()
					.getNewGenerator(this.epithelium.getUpdateSchemeInter().getRandomSeed());
		}
		// Grid History
		this.gridHistory = new ArrayList<EpitheliumGrid>();

		this.nextGrid = this.epithelium.getEpitheliumGrid().clone();
		this.nextGrid.updateNodeValueCounts();
		this.nextGrid.restrictGridWithPerturbations();
		this.gridHistory.add(this.nextGrid);
		
		// Grid Hash History
		this.gridHashHistory = new ArrayList<String>();
		this.gridHashHistory.add(this.nextGrid.hashGrid());
		this.stable = false;
		this.hasCycle = false;
		this.buildPriorityUpdaterCache();

		this.mTuple2Neighbours= new HashMap <Tuple2D<Integer>, List<Tuple2D<Integer>>> (); //TO BE REMOVED FROM HERE
		
		this.graph = initializeGraph();

	}

	private void buildPriorityUpdaterCache() {
		// updaterCache stores the PriorityUpdater to avoid unnecessary
		// computing
		this.updaterCache = new HashMap<LogicalModel, Map<AbstractPerturbation, PriorityUpdater>>();
		for (int y = 0; y < this.getCurrentGrid().getY(); y++) {
			for (int x = 0; x < this.getCurrentGrid().getX(); x++) {
				if (!this.getCurrentGrid().getAbstCell(x, y).isLivingCell()) {
					continue;
				}
				LogicalModel m = this.getCurrentGrid().getModel(x, y);
				AbstractPerturbation ap = this.epithelium.getEpitheliumGrid().getPerturbation(x, y);
				if (!this.updaterCache.containsKey(m)) {
					this.updaterCache.put(m, new HashMap<AbstractPerturbation, PriorityUpdater>());
				}
				if (!this.updaterCache.get(m).containsKey(ap)) {
					// Apply model perturbation
					LogicalModel perturb = (ap == null) ? m : ap.apply(m);
					// Get Priority classes
					PriorityClasses pcs = this.epithelium.getPriorityClasses(m).getPriorities();
					PriorityUpdater updater = new PriorityUpdater(perturb, pcs);
					this.updaterCache.get(m).put(ap, updater);
				}
			}
		}
	}

	/**
	 * This function retrieves the next step in the simulation. The first step
	 * in this
	 */
	public EpitheliumGrid nextStepGrid() {
		EpitheliumGrid currGrid = this.getCurrentGrid();

		if (this.stable) {
			return currGrid;
		}

		this.nextGrid = currGrid.clone();

		Set<NodeInfo> sNodes = this.epithelium.getIntegrationNodes();
		IFEvaluation evaluator = new IFEvaluation(this.nextGrid,this.epithelium);

		//list of tuples with LivingCells
		List<LivingCell> livingCells =this.nextGrid.getAllLivingCells();

		// Gets the set of cells that can be updated
		// And builds the default next grid (= current grid)
		HashMap<Tuple2D<Integer>, byte[]> cells2update = new HashMap<Tuple2D<Integer>, byte[]>();
		List<Tuple2D<Integer>> keys = new ArrayList<Tuple2D<Integer>>();
		List<Tuple2D<Integer>> changedKeys = new ArrayList<Tuple2D<Integer>>();

		//		System.out.println("*************");
		//		System.out.println("the set of living cells is : " + livingCells);
		//		System.out.println("The Grid state before everythiog is: ");
		//		for (LivingCell cell : nextGrid.getAllLivingCells()) {
		//			System.out.println("tuple:  " + cell.getTuple().toString());
		//			System.out.println("state: " + Arrays.toString(cell.getState()));
		//		}
		for (LivingCell livingCell: livingCells) {
			//			System.out.println("updating Cell: " + livingCell.getTuple());
			Tuple2D<Integer> tuple = livingCell.getTuple();
			int x = (int) tuple.getX();
			int y = (int) tuple.getY();

			//			if (!nextGrid.getAbstCell(x,y).isLivingCell()) {
			//				continue;
			//			}
			byte[] currState = livingCell.getState();

			// Compute next state
			byte[] nextState = this.nextCellValue(x, y, currGrid, evaluator, sNodes);
			//			System.out.println("updating Cell next state: " + Arrays.toString(nextState));

			// If the cell state changed then add it to the pool
			Tuple2D<Integer> key = new Tuple2D<Integer>(x, y);
			cells2update.put(key, nextState);
			keys.add(key);

			if (!Arrays.equals(currState, nextState)) {
				changedKeys.add(key);
			}
		}

		if (this.epithelium.getUpdateSchemeInter().getUpdateCells().equals(UpdateCells.UPDATABLECELLS)) {
			keys = changedKeys;
		}

		// Internal updates
		if (changedKeys.size() > 0) {
			// Inter-cellular alpha-asynchronism
			float alphaProb = this.epithelium.getUpdateSchemeInter().getAlpha();
			int nToChange = (int) Math.floor(alphaProb * keys.size());
			if (nToChange == 0) {
				nToChange = 1;
			}
			// Create the initial shuffled array of cells
			Collections.shuffle(keys, this.random);

			for (int i = 0; i < nToChange; i++) {
				// Update cell state
				this.nextGrid.setCellState(keys.get(i).getX(), keys.get(i).getY(), cells2update.get(keys.get(i)));
			}
		}

		boolean control = true;
		if (this.nextGrid.getAllLivingCells().size()==0) {
			this.stable = true;
			return currGrid;
		}
		else {
			if (changedKeys.isEmpty()) {
				for (LogicalModel model: this.epithelium.getEpitheliumEvents().getModels()){
					if (this.epithelium.getEpitheliumEvents().getMCE(model).getDeathTrigger().equals(Txt.get("s_TAB_EVE_TRIGGER_RANDOM"))&& this.epithelium.getEpitheliumEvents().getMCE(model).getDeathValue()>0) {
						control = false;}
					else if (this.epithelium.getEpitheliumEvents().getMCE(model).getDivisionTrigger().equals(Txt.get("s_TAB_EVE_TRIGGER_RANDOM")) && this.nextGrid.getEmptyCells().size()!=0){
						control = false;}
				}
				if (control) {
					this.stable = true;
					return currGrid;
				}}
		}

		//************* TRIGGER EVENTS
		//		System.out.println("The next Grid state before actions is: ");
		//		for (LivingCell cell : nextGrid.getAllLivingCells()) {
		//			System.out.println("tuple:  " + cell.getTuple().toString());
		//			System.out.println("state: " + Arrays.toString(cell.getState()));
		//		}
		//		System.out.println("There are things changed on the grid");
		//		System.out.println("Iteration:  " + this.gridHashHistory.size());
		//		System.out.println("1 " + nextGrid.getAllLivingCells().size());
		//		System.out.println("2 " + nextGrid.getEmptyCells().size());
		//		System.out.println("3 " + (nextGrid.getEmptyCells().size() + nextGrid.getAllLivingCells().size()));



		List<LivingCell> deathCells = new ArrayList<LivingCell>();
		List<LivingCell> divisionCells = new ArrayList<LivingCell>();

		for (LogicalModel model: this.nextGrid.getModelSet()) {
			ModelCellularEvent mce =  this.epithelium.getEpitheliumEvents().getMCE(model);
			List<LivingCell> availableLivingCells = this.nextGrid.getLivingCells(model);

			String deathTrigger = mce.getDeathTrigger();
			String divisionTrigger = mce.getDivisionTrigger();
			//Validation of patterns is made at the eEpiTabEvents level

			if (deathTrigger.equals(Txt.get("s_TAB_EVE_TRIGGER_PATTERN"))) {
				//System.out.println(model + " death is by pattern 7: retrieve a set of cells to be marked as set to die");
				//update Living Cells
				//deathCells = getComplyingCells(availableLivingCells, this.epithelium.getEpitheliumEvents().getMCE(model).getDeathPattern());
				//availableLivingCells.removeAll(deathCells);
			}
			if (divisionTrigger.equals(Txt.get("s_TAB_EVE_TRIGGER_PATTERN"))) {
				//System.out.println(model + " division is by pattern: retrieve a set of cells to be marked as set to divide");
				//Update living Cells
				//divisionCells = getComplyingCells(availableLivingCells, this.epithelium.getEpitheliumEvents().getMCE(model).getDivisionPattern());
				//availableLivingCells.removeAll(divisionCells);
			}

			int numberOfLivingCells = availableLivingCells.size();

			if (deathTrigger.equals(Txt.get("s_TAB_EVE_TRIGGER_RANDOM")) && divisionTrigger.equals(Txt.get("s_TAB_EVE_TRIGGER_RANDOM"))) {
				//								System.out.println("Everything is random");

				float deathVal = this.epithelium.getEpitheliumEvents().getMCE(model).getDeathValue();
				float divVal = this.epithelium.getEpitheliumEvents().getMCE(model).getDivisionValue();

				//				System.out.println("There are these available cells to divide: " + availableLivingCells.size());
				if (availableLivingCells.size()>1 | deathVal!=0 | divVal!=0) {
					if (numberOfLivingCells*deathVal>=1 && numberOfLivingCells*divVal>=1) { //No issue here
						System.out.println("Both Probs are above 1");
						System.out.println("Size of the availableLivingCells: " + numberOfLivingCells);
						System.out.println("deathVal: " + deathVal);
						System.out.println("divVal: " + divVal);
						System.out.println("numberOfLivingCells*deathVal: " + numberOfLivingCells*deathVal);
						System.out.println("numberOfLivingCells*divVal: " + numberOfLivingCells*divVal);

						deathCells = getProportionOfCells(availableLivingCells,(int) (numberOfLivingCells*deathVal));
						availableLivingCells.removeAll(deathCells);
						System.out.println("I am killing: " + deathCells);
						divisionCells = getProportionOfCells(availableLivingCells,(int) (numberOfLivingCells*divVal));
						availableLivingCells.removeAll(divisionCells);
						//						System.out.println("I am dividing: " + divisionCells);
					} 
					else if (numberOfLivingCells*deathVal<1 && numberOfLivingCells*divVal<1) {
						//						System.out.println("Both Probs are bellow 1");
						List<List<LivingCell>> cells = getBothProbabilities(availableLivingCells,deathVal, divVal);
						deathCells= cells.get(0);
						divisionCells = cells.get(1);
						availableLivingCells= cells.get(2);
					}
					else if (numberOfLivingCells*deathVal<1){
						//						System.out.println("only death bellow 1");
						divisionCells = getProportionOfCells(availableLivingCells,(int) (numberOfLivingCells*divVal));
						availableLivingCells.removeAll(divisionCells);
						//						System.out.println("I am dividing: " + divisionCells);
						deathCells = getProbabilityOfCells(availableLivingCells,deathVal);
						//						System.out.println("I am killing: " + deathCells);
						availableLivingCells.removeAll(deathCells);

					}
					else if (numberOfLivingCells*divVal<1){
						deathCells = getProportionOfCells(availableLivingCells,(int) (numberOfLivingCells*deathVal));
						availableLivingCells.removeAll(deathCells);
						divisionCells = getProbabilityOfCells(availableLivingCells,divVal);
						availableLivingCells.removeAll(divisionCells);
					}

				}
				else if (availableLivingCells.size()==1){
					//Does it make sense to kill a cell if at the beginning there is only one?
					//since both are random, nothing will happen, and we could force the system to be either one of them, even though the cellular system could change

					if (this.random.nextBoolean())	{
						deathCells = getProbabilityOfCells(availableLivingCells,deathVal);
						availableLivingCells.removeAll(deathCells);
					}
					else {
						divisionCells = getProbabilityOfCells(availableLivingCells,divVal);
						availableLivingCells.removeAll(divisionCells);
					}
				}
			}
			else if (deathTrigger.equals(Txt.get("s_TAB_EVE_TRIGGER_RANDOM"))) {

				float deathVal = this.epithelium.getEpitheliumEvents().getMCE(model).getDeathValue();
				if (numberOfLivingCells*deathVal>=1){
					deathCells = getProportionOfCells(availableLivingCells,(int) (numberOfLivingCells*deathVal));
					availableLivingCells.removeAll(deathCells);
					//System.out.println(" death is by random: retrieve a set of cells to be marked as set to die");
					//System.out.println("deathCells: " + deathCells);
				}

				else if (numberOfLivingCells*deathVal<1){
					deathCells = getProbabilityOfCells(availableLivingCells,deathVal);
					availableLivingCells.removeAll(deathCells);

				}
			}

			else if (divisionTrigger.equals(Txt.get("s_TAB_EVE_TRIGGER_RANDOM")) & availableLivingCells.size()>0) {
				//				System.out.println("Only division is random");
				float divVal = this.epithelium.getEpitheliumEvents().getMCE(model).getDivisionValue();

				if (numberOfLivingCells*divVal>=1){
					divisionCells = getProportionOfCells(availableLivingCells,(int) (numberOfLivingCells*divVal));
					availableLivingCells.removeAll(divisionCells);
					//System.out.println(" death is by random: retrieve a set of cells to be marked as set to die");
					//System.out.println("deathCells: " + deathCells);
				}

				else if (numberOfLivingCells*divVal<1){
					divisionCells = getProbabilityOfCells(availableLivingCells,divVal);
					availableLivingCells.removeAll(divisionCells);

				}
			}
		}


		//************* Event Action

		//TODO: TEST EVERYTHING
		String order = this.epithelium.getEpitheliumEvents().getEventOrder();
		List<LivingCell> orderedCells = new ArrayList<LivingCell>();

		if (order.equals(Txt.get("s_TAB_EPIUPDATE_ORDER_DIVDEATH"))) {
			orderedCells.addAll(divisionCells);
			orderedCells.addAll(deathCells);
		}
		else if (order.equals(Txt.get("s_TAB_EPIUPDATE_ORDER_DEATHDIV"))) {
			orderedCells.addAll(deathCells);
			orderedCells.addAll(divisionCells);
		}
		else if (order.equals(Txt.get("s_TAB_EPIUPDATE_ORDER_RANDOM"))) {
			orderedCells.addAll(deathCells);
			orderedCells.addAll(divisionCells);
			Collections.shuffle(orderedCells, this.random);
		}

		//TODO FIX-ME
		for (LivingCell lCell: orderedCells) {
			if (deathCells.contains(lCell)) {//If this cell is about to die
				if (this.epithelium.getEpitheliumEvents().getDeathOption().equals(Txt.get("s_TAB_EPIUPDATE_CELLDEATH_EMPTY"))) {
					this.nextGrid.setAbstractCell(CellFactory.newEmptyCell(lCell.getTuple().clone()));
				}
				else if (this.epithelium.getEpitheliumEvents().getDeathOption().equals(Txt.get("s_TAB_EPIUPDATE_CELLDEATH_PERMANENT")))
					this.nextGrid.setAbstractCell(CellFactory.newDeadCell(lCell.getTuple().clone()));
				else if (this.epithelium.getEpitheliumEvents().getDeathOption().equals(Txt.get("s_TAB_EPIUPDATE_CELLDEATH_RANDOM"))) {

					if (this.random.nextBoolean())	
						this.nextGrid.setAbstractCell(CellFactory.newDeadCell(lCell.getTuple().clone()));
					else 
						this.nextGrid.setAbstractCell(CellFactory.newEmptyCell(lCell.getTuple().clone()));
				}
			}
			else if (divisionCells.contains(lCell)) {//if the cell is about to divide
				if (this.nextGrid.getEmptyCells().size()>0) {//If there are any emptyCells
					if (this.epithelium.getEpitheliumEvents().getMCE(lCell.getModel()).getDivisionAlgorithm().equals(Txt.get("s_TAB_EVE_ALGORITHM_RANDOM"))){
						randomDivision(lCell);}
					else if (this.epithelium.getEpitheliumEvents().getMCE(lCell.getModel()).getDivisionAlgorithm().equals(Txt.get("s_TAB_EVE_ALGORITHM_MINIMUM_DISTANCE"))){
						minimumDistance(this.graph, lCell);
					}
					else if (this.epithelium.getEpitheliumEvents().getMCE(lCell.getModel()).getDivisionAlgorithm().equals(Txt.get("s_TAB_EVE_ALGORITHM_COMPRESSION"))){
						System.out.println("I am calling to divide the cell: " + lCell.getTuple() + "with the compression algorithm"+ "(" + lCell+")");
						OriginalCompression compression = new OriginalCompression();
						System.out.println("w0.9: " + this.nextGrid.getAbstCell(lCell.getTuple()));
						LinkedList<Vertex> path = compression.originalCompression(this.graph, lCell,this.nextGrid, this.epithelium.getEpitheliumEvents().getMCE(lCell.getModel()).getDivisionRange(), this.random);
						System.out.println("w1: " + this.nextGrid.getAbstCell(path.get(0).getTuple()));
						if (path.size()>0)
						this.displaceCells(path);
						else
							System.out.println("A cell should have divided but compression failed to find an empty space");
					}
				}
			}
		}

		this.gridHistory.add(this.nextGrid);
		if (this.gridHashHistory != null) {
			this.gridHashHistory.add(this.nextGrid.hashGrid());
		}
		return this.nextGrid;
	}


	private List<List<LivingCell>> getBothProbabilities(List<LivingCell> availableLivingCells, float deathVal,
			float divVal) {

		List<List<LivingCell>> cells = new ArrayList<List<LivingCell>>();
		List<LivingCell> deathList =  new ArrayList<LivingCell>();
		List<LivingCell> divisionList =  new ArrayList<LivingCell>();

		for (LivingCell lCell: availableLivingCells) {

			if (this.random.nextBoolean())	{
				if (this.random.nextDouble()<=deathVal) {
					deathList.add(lCell);
				}
			}
			else {
				if (this.random.nextDouble()<=divVal) {
					divisionList.add(lCell);
				}
			}
		}

		availableLivingCells.removeAll(divisionList);
		availableLivingCells.removeAll(deathList);

		cells.add(0,deathList);
		cells.add(1,divisionList);
		cells.add(2,availableLivingCells);

		return cells;
	}

	private List<Tuple2D<Integer>>  getEmptyCellsNeighbours(LivingCell lCell) {


		List<Tuple2D<Integer>> lstNeighbours = new ArrayList<Tuple2D<Integer>>();
		//Get all neighbours within the distance Range
		int maximumDistance = this.epithelium.getEpitheliumEvents().getMCE(lCell.getModel()).getDivisionRange();
		if (!this.mTuple2Neighbours.containsKey(lCell.getTuple())) {
			for (int i=1; i<= this.epithelium.getEpitheliumEvents().getMCE(lCell.getModel()).getDivisionRange();i++){
				lstNeighbours.addAll(this.nextGrid.getNeighbours(i,i,lCell.getTuple()));
				if (this.epithelium.getEpitheliumEvents().getDivisionOption().equals(Txt.get("s_TAB_EVE_ALGORITHM_MINIMUM_DISTANCE"))){
					//IF an empty cell at distance 1 do not go to the next
					//If bigger than that if no invalid do no go to the next
					if  (i==1 && lstNeighbours.size()>0) {
						maximumDistance = 1;
					}
						
				}
			}
			this.mTuple2Neighbours.put(lCell.getTuple(),lstNeighbours);
		}
		else 
			lstNeighbours.addAll(this.mTuple2Neighbours.get(lCell.getTuple()));

		//Remove all neighbours that are not empty cells
		List<Tuple2D<Integer>> lstNeighbours2Remove =  new ArrayList<Tuple2D<Integer>>();
		for (Tuple2D<Integer> tuple: this.mTuple2Neighbours.get(lCell.getTuple())) {
			if (!this.nextGrid.getAbstCell(tuple.getX(),tuple.getY()).isEmptyCell()) {
				lstNeighbours2Remove.add(tuple);
			}	
		}

		lstNeighbours.removeAll(lstNeighbours2Remove);

		//		System.out.println("*****************");
		//
		//		System.out.println(lCell.getTuple());
		//		System.out.println(lstNeighbours);

		return lstNeighbours;
	}

	private void minimumDistance(Graph graph, LivingCell lCell) {

		List<Tuple2D<Integer>> lstPossibleDestinations = getEmptyCellsNeighbours(lCell);
		List<LinkedList<Vertex>> lstPath = new ArrayList<LinkedList<Vertex>>();
		int minSize = 1000;

		for (Tuple2D<Integer> destination: lstPossibleDestinations) {
			LinkedList<Vertex> path = getPath(lCell.getTuple(), destination ,graph);
			if (path.size()>0 && path.size()<=minSize) {
				lstPath.add(path);
				minSize = path.size();
			}
		}

		if (lstPath.size()>1) {
			LinkedList<Vertex> path = lstPath.get(random.nextInt(lstPath.size()));
			this.displaceCells(path);
		}
		else if (lstPath.size()>0){
			this.displaceCells(lstPath.get(0));
		}

	}


	private void displaceCells(LinkedList<Vertex> path) {
		//TODO: IT IS WRONG
		
		System.out.println("w2: " + this.nextGrid.getAbstCell(path.get(0).getTuple()));

		System.out.println("start: " + path.getFirst().getTuple().toString());
		System.out.println("end: " +  path.getLast().getTuple().toString());
		for (Vertex node: path) {
			System.out.println("node: " + node.getTuple().toString());
		}
		
		System.out.println("start: " + this.nextGrid.getAbstCell(path.getFirst().getTuple()));
		System.out.println("end: " +  this.nextGrid.getAbstCell(path.getLast().getTuple()));
		for (Vertex node: path) {
			System.out.println("node: " +  this.nextGrid.getAbstCell(node.getTuple()));
		}


		for (int i = 0; i<path.size()-2; i++) {
			Tuple2D<Integer> tupleDestination = path.getLast().getTuple();
			System.out.println("The tuple to be removed is an " + this.nextGrid.getAbstCell(tupleDestination) + " at tuple:" + tupleDestination);
			path.removeLast();

			//displace last cell
			Tuple2D<Integer> origin = path.getLast().getTuple();
			System.out.println("The tuple to be added is an " + this.nextGrid.getAbstCell(origin) + " at tuple:" + origin);
			AbstractCell cell = this.nextGrid.getAbstCell(origin.getX(), origin.getY());
			cell.setTuple(tupleDestination);
			this.nextGrid.setAbstractCell(cell);
			EmptyCell c = CellFactory.newEmptyCell(origin);
			this.nextGrid.setAbstractCell(c);
		}

		
		//		for (int i = index; i <1; i = i-1) {
		//			System.out.println("index");
		//			Tuple2D<Integer> tuple = path.get(index).getTuple();
		//			System.out.println(tuple);
		//			AbstractCell cell = nextGrid.getAbstCell(tuple.getX(), tuple.getY());
		//			cell.setTuple(tupleDestination);
		//			nextGrid.setAbstractCell(cell);
		//			tupleDestination = tuple;
		//			System.out.println(tuple);
		//		}
		
		System.out.println("start: " + this.nextGrid.getAbstCell(path.getFirst().getTuple()));
		System.out.println("end: " +  this.nextGrid.getAbstCell(path.getLast().getTuple()));
		for (Vertex node: path) {
			System.out.println("node: " +  this.nextGrid.getAbstCell(node.getTuple()));
		}
		
		System.out.println("w3: " + this.nextGrid.getAbstCell(path.get(0).getTuple()));
		updateCellSister(path.get(0).getTuple(), path.get(1).getTuple());

	}




	private Graph initializeGraph() {

		List<Vertex> nodes = new ArrayList<Vertex>();
		List<Edge>   edges = new ArrayList<Edge>();
		Map<Tuple2D<Integer>, Vertex> mTuple2Vertex = new HashMap<Tuple2D<Integer>, Vertex>();
		
		//create vertex List of living and dead cells
		for (int x=0; x<this.epithelium.getX(); x++) {
			for(int y = 0; y<this.epithelium.getY(); y++){
				Tuple2D<Integer> tuple = new Tuple2D<Integer>(x,y);
				if (!this.nextGrid.getAbstCell(x, y).isInvalidCell()){
					Vertex v = new Vertex(tuple);
					mTuple2Vertex.put(tuple, v);
					nodes.add(v);
				}
			}}
		System.out.println("Initialized a graph with: " + nodes.size() + " nodes");
		//create Edges
		Edge edge;
		int compressionRange = 1;
		
			for (Tuple2D<Integer> tuple: mTuple2Vertex.keySet()) {
				Set<Tuple2D<Integer>> neighbours = this.nextGrid.getNeighbours(1, 1,tuple);
				for (Tuple2D<Integer> tupleNei :neighbours) {
					if (nodes.contains(mTuple2Vertex.get(tupleNei))) {
//						AbstractCell cell = this.epithelium.getEpitheliumGrid().getAbstCell(tuple.getX(), tuple.getY());
						edge = new Edge(tuple,mTuple2Vertex.get(tuple), mTuple2Vertex.get(tupleNei), 1 );
						edges.add(edge);
					}
			}}
			System.out.println("Initialized a graph with: " + edges.size() + " edges");
			
		Graph graph = new Graph(nodes, edges);
		graph.setMTuple2VertexAll(mTuple2Vertex);
		
		return graph;
	}

	
//	//alternative version
//	private void compression(Graph graph, LivingCell lCell, EpitheliumGrid nextGrid) {
//		
//		List<Tuple2D<Integer>> lstPossibleDestinations = getEmptyCellsNeighbours(lCell, nextGrid);
//		List<LinkedList<Vertex>> lstPath = new ArrayList<LinkedList<Vertex>>();
//		//		
//		int minSize = 1000;
//
//		for (Tuple2D<Integer> destination: lstPossibleDestinations) {
//			LinkedList<Vertex> path = getPath(lCell.getTuple(), destination ,graph, this.mTuple2Vertex);
//			if (path.size()>0){
//				if (path.size()<minSize) {
//					lstPath = new ArrayList<LinkedList<Vertex>>();
//					minSize = path.size();
//				}
//				if (path.size()==minSize) {
//				lstPath.add(path);
//			}}
//			
//		}
//		
//		System.out.println("There are " + lstPath.size() + " possible paths");
//		
//		List<LinkedList<Vertex>> lstCompressedPath = new ArrayList<LinkedList<Vertex>>();
//		
//		double minCompression = 1000000;
//		Map<Tuple2D<Integer>, Double> mTuple2CompressionValue = new HashMap<Tuple2D<Integer>, Double>();
//		for ( LinkedList<Vertex> path : lstPath) {
//			double compression = 0;
//			for (Vertex v: path) {
//				if (!mTuple2CompressionValue.containsKey(v.getTuple())) {
//					mTuple2CompressionValue.put(v.getTuple(), getCompressionValue(nextGrid,v.getTuple()));
//				}
//				compression = compression + mTuple2CompressionValue.get(v.getTuple());
//			}
//			if (compression < minCompression) {
//				lstCompressedPath = new ArrayList<LinkedList<Vertex>>();
//				minCompression = compression;
//				System.out.println("Minimum compression is now set to: " + minCompression);
//			}
//			if (compression==minCompression) {
//				lstCompressedPath.add(path);
//		}
//		}
//		System.out.println("There are " + lstCompressedPath.size() + " possible compressed paths");
//		
//		if (lstCompressedPath.size()>1) {
//			LinkedList<Vertex> path = lstCompressedPath.get(random.nextInt(lstCompressedPath.size()));
//			this.displaceCells(path, nextGrid);
//		}
//		else if (lstCompressedPath.size()>0){
//			this.displaceCells(lstCompressedPath.get(0), nextGrid);
//		}
//		
//	}



	private LinkedList<Vertex> getPath (Tuple2D<Integer> start,Tuple2D<Integer> end , Graph graph) {


		DijkstraAlgorithm dijkstra = new DijkstraAlgorithm(graph);
		dijkstra.execute(graph.getMTuple2Vertex(start));
		LinkedList<Vertex> path = dijkstra.getPath(graph.getMTuple2Vertex(end));
		return path;

	}



	private void randomDivision(LivingCell lCell) {

		//		System.out.println("randomDivision");
		Tuple2D<Integer> originalTuple = lCell.getTuple().clone();
		Tuple2D<Integer> sisterTuple = getSisterPosition(originalTuple, this.nextGrid.getEmptyCells());

		LivingCell sisterCell = CellFactory.newLivingCell(sisterTuple, lCell.getModel());
		LivingCell originalCell = CellFactory.newLivingCell(originalTuple, lCell.getModel());

		updateCellSister(lCell.getTuple(), sisterCell.getTuple());
	}


	//change the original cell the daughters
	private void updateCellSister(Tuple2D<Integer> originalTuple, Tuple2D<Integer> sisterTuple) {

		System.out.println("updated SisterCell");
		LivingCell lCell = (LivingCell) this.nextGrid.getAbstCell(originalTuple.getX(), originalTuple.getY());
		LivingCell sisterCell = CellFactory.newLivingCell(sisterTuple, lCell.getModel());
		LivingCell originalCell = CellFactory.newLivingCell(lCell.getTuple(), lCell.getModel());


		if (this.epithelium.getEpitheliumEvents().getDivisionOption().equals(Txt.get("s_TAB_EPIUPDATE_NEWCELLSTATE_SAME"))) {
			sisterCell.setState(lCell.getState());
			originalCell.setState(lCell.getState());
			this.nextGrid.setAbstractCell(sisterCell);
			this.nextGrid.setAbstractCell(originalCell);
		}
		else if (this.epithelium.getEpitheliumEvents().getDivisionOption().equals(Txt.get("s_TAB_EPIUPDATE_NEWCELLSTATE_NAIVE"))) {
			this.nextGrid.setAbstractCell(sisterCell);
			this.nextGrid.setAbstractCell(originalCell);
		}
		else if (this.epithelium.getEpitheliumEvents().getDivisionOption().equals(Txt.get("s_TAB_EPIUPDATE_NEWCELLSTATE_PREDEFINED"))) {
			sisterCell.setState(this.epithelium.getEpitheliumEvents().getDivisionNewState(lCell.getModel()));
			originalCell.setState(this.epithelium.getEpitheliumEvents().getDivisionNewState(lCell.getModel()));
			this.nextGrid.setAbstractCell(sisterCell);
			this.nextGrid.setAbstractCell(originalCell);
		}
		else if (this.epithelium.getEpitheliumEvents().getDivisionOption().equals(Txt.get("s_TAB_EPIUPDATE_NEWCELLSTATE_RANDOM"))) {
			List<Boolean> randomSel = new ArrayList<Boolean>();
			randomSel.add(true);
			randomSel.add(false);
			Collections.shuffle(randomSel, this.random);
			if (randomSel.get(0))	{//NAIVE
				this.nextGrid.setAbstractCell(sisterCell);
				this.nextGrid.setAbstractCell(originalCell);
			}
			else 
			{//SAME
				sisterCell.setState(lCell.getState());
				originalCell.setState(lCell.getState());
				this.nextGrid.setAbstractCell(sisterCell);
				this.nextGrid.setAbstractCell(originalCell);
			}
		}


	}

	private Tuple2D<Integer> getSisterPosition(Tuple2D<Integer> tuple, List<EmptyCell> emptyCells) {
		// TODO Random Algorithm
		//ADD viewing range (make sure that there is an empty cell in a given range
		Collections.shuffle(emptyCells, this.random);
		return emptyCells.get(0).getTuple();
	}

	private List<LivingCell> getProbabilityOfCells(List<LivingCell> livingCells, float value) {

		List<LivingCell> cells = new ArrayList<LivingCell>();

		for (LivingCell livingCell: livingCells) {
			if (this.random.nextDouble()<=value) {
				cells.add(livingCell);
			}
		}

		return cells;
	}

	private List<LivingCell> getProportionOfCells(List<LivingCell> livingCells, int numberCells) {
		List<LivingCell> cells = new ArrayList<LivingCell>();

		// Create the initial shuffled array of cells
		Collections.shuffle(livingCells, this.random);

		for (int i = 0; i < numberCells; i++) {
			cells.add(livingCells.get(i));
		}
		return cells;
	}

	public boolean hasCycle() {
		if (!this.hasCycle) {
			Set<String> sStateHistory = new HashSet<String>(this.gridHashHistory);
			this.hasCycle = (sStateHistory.size() < this.gridHashHistory.size());
		}
		return this.hasCycle;
	}

	private byte[] nextCellValue(int x, int y, EpitheliumGrid currGrid, IFEvaluation evaluator,
			Set<NodeInfo> sNodeInfos) {
		//		System.out.println("inside the next state calculator");
		byte[] currState = currGrid.getCellState(x, y).clone();
		//		System.out.println("the currstate is: " + Arrays.toString(currState));
		LogicalModel m = currGrid.getModel(x, y);
		AbstractPerturbation ap = currGrid.getPerturbation(x, y);
		PriorityUpdater updater = this.updaterCache.get(m).get(ap);


		// 2. Update integration components
		for (NodeInfo node :this.epithelium.getIntegrationNodes()) {
			if (m.getComponents().contains(node)) {
				if (node.isInput() && sNodeInfos.contains(node)) {
					//					System.out.println("Just found the input: " + node.getNodeID());
					List<IntegrationFunctionExpression> lExpressions = this.epithelium
							.getIntegrationFunctionsForComponent(node).getComputedExpressions();
					byte target = 0;
					for (int i = 0; i < lExpressions.size(); i++) {
						//						System.out.println("There are expressions");
						if (evaluator.evaluate(x, y, lExpressions.get(i))) {
							target = (byte) (i + 1);
							//							System.out.println("updated the input: " + node.getNodeID());
							break; // The lowest value being satisfied
						}
					}
					currState[m.getComponents().indexOf(node)] = target;
				}
			}}
		List<byte[]> succ = updater.getSuccessors(currState);
		if (succ == null) {
			return currState;
		} else if (succ.size() > 1) {
			// FIXME
			// throw new Exception("Argh");
		}
		return succ.get(0);
	}

	public boolean isStableAt(int i) {
		return (i >= this.gridHistory.size() && this.stable);
	}

	private boolean isSynchronous() {
		return this.epithelium.getUpdateSchemeInter().getAlpha() == 1.0;
	}

	public int getTerminalCycleLen() {
		//TODO: Change taking into consideration that cells may die and divide

		//		if (this.isSynchronous()) {
		//			String sGrid = this.gridHashHistory.get(this.gridHashHistory.size() - 1);
		//			// Tmp
		//			List<String> lTmp = new ArrayList<String>(this.gridHashHistory);
		//			lTmp.remove(this.gridHashHistory.size()-1);
		//			int posBeforeLast = lTmp.lastIndexOf(sGrid);
		//
		//			if (posBeforeLast > 0) {
		//				return (this.gridHashHistory.size() - 1) - posBeforeLast;
		//			}
		//		}
		return -1;
	}

	public EpitheliumGrid getGridAt(int i) {
		if (i < this.gridHistory.size()) {
			return this.gridHistory.get(i);
		}
		return this.nextStepGrid();
	}

	public EpitheliumGrid getCurrentGrid() {
		return gridHistory.get(gridHistory.size() - 1);
	}

	public Epithelium getEpithelium() {
		return this.epithelium;
	}

	public List<Map<String, Float>> getCell2Percentage() {
		List<Map<String, Float>> percentageHistory = new ArrayList<Map<String, Float>>();
		int index = 0;
		for (EpitheliumGrid grid : this.gridHistory) {
			percentageHistory.add(new HashMap<String, Float>());
			for (LogicalModel model : grid.getModelSet()) {
				for (NodeInfo node : model.getComponents()) {
					for (byte i = 1; i <= node.getMax(); i++) {
						String nodeID = node.getNodeID() + " " + i;
						percentageHistory.get(index).put(nodeID, grid.getPercentage(node.getNodeID(), i));
					}
				}
			}
			index = index + 1;
		}
		return percentageHistory;
	}


}
