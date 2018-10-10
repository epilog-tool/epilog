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
	
	private 	Map<Tuple2D<Integer>, Vertex> mTuple2Vertex;

	private Map<Tuple2D<Integer>, Map<Boolean, Set<Tuple2D<Integer>>>> relativeNeighboursCache; //TO BE REMOVED FROM HERE
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
		EpitheliumGrid firstGrid = this.epithelium.getEpitheliumGrid().clone();
		firstGrid.updateNodeValueCounts();
		firstGrid.restrictGridWithPerturbations();
		this.gridHistory.add(firstGrid);
		// Grid Hash History
		this.gridHashHistory = new ArrayList<String>();
		this.gridHashHistory.add(firstGrid.hashGrid());
		this.stable = false;
		this.hasCycle = false;
		this.buildPriorityUpdaterCache();

		this.relativeNeighboursCache = new HashMap<Tuple2D<Integer>, Map<Boolean, Set<Tuple2D<Integer>>>>(); //TO BE REMOVED FROM HERE
		this.mTuple2Neighbours= new HashMap <Tuple2D<Integer>, List<Tuple2D<Integer>>> (); //TO BE REMOVED FROM HERE
		

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

		EpitheliumGrid nextGrid = currGrid.clone();

		Set<NodeInfo> sNodes = this.epithelium.getIntegrationNodes();
		IFEvaluation evaluator = new IFEvaluation(nextGrid,this.epithelium);

		//list of tuples with LivingCells
		List<LivingCell> livingCells = nextGrid.getAllLivingCells();

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
				nextGrid.setCellState(keys.get(i).getX(), keys.get(i).getY(), cells2update.get(keys.get(i)));
			}
		}

		boolean control = true;
		if (nextGrid.getAllLivingCells().size()==0) {
			this.stable = true;
			return currGrid;
		}
		else {
		if (changedKeys.isEmpty()) {
			for (LogicalModel model: this.epithelium.getEpitheliumEvents().getModels()){
				if (this.epithelium.getEpitheliumEvents().getMCE(model).getDeathTrigger().equals(Txt.get("s_TAB_EVE_TRIGGER_RANDOM"))&& this.epithelium.getEpitheliumEvents().getMCE(model).getDeathValue()>0) {
					control = false;}
				else if (this.epithelium.getEpitheliumEvents().getMCE(model).getDivisionTrigger().equals(Txt.get("s_TAB_EVE_TRIGGER_RANDOM")) && nextGrid.getEmptyCells().size()!=0){
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

		for (LogicalModel model: nextGrid.getModelSet()) {
			ModelCellularEvent mce =  this.epithelium.getEpitheliumEvents().getMCE(model);
			List<LivingCell> availableLivingCells = nextGrid.getLivingCells(model);

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
				if (availableLivingCells.size()>1 | deathVal==0 | divVal==0) {
					if (numberOfLivingCells*deathVal>1 && numberOfLivingCells*divVal>1) { //No issue here
//						System.out.println("Both Probs are above 1");
						//						System.out.println("Size of the availableLivingCells: " + numberOfLivingCells);
						//						System.out.println("deathVal: " + deathVal);
						//						System.out.println("divVal: " + divVal);
						//						System.out.println("numberOfLivingCells*deathVal: " + numberOfLivingCells*deathVal);
						//						System.out.println("numberOfLivingCells*divVal: " + numberOfLivingCells*divVal);

						deathCells = getProportionOfCells(availableLivingCells,(int) (numberOfLivingCells*deathVal));
						availableLivingCells.removeAll(deathCells);
						divisionCells = getProportionOfCells(availableLivingCells,(int) (numberOfLivingCells*divVal));
						availableLivingCells.removeAll(divisionCells);
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
						deathCells = getProbabilityOfCells(availableLivingCells,deathVal);
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
					nextGrid.setAbstractCell(CellFactory.newEmptyCell(lCell.getTuple().clone()));
				}
				else if (this.epithelium.getEpitheliumEvents().getDeathOption().equals(Txt.get("s_TAB_EPIUPDATE_CELLDEATH_PERMANENT")))
					nextGrid.setAbstractCell(CellFactory.newDeadCell(lCell.getTuple().clone()));
				else if (this.epithelium.getEpitheliumEvents().getDeathOption().equals(Txt.get("s_TAB_EPIUPDATE_CELLDEATH_RANDOM"))) {

					if (this.random.nextBoolean())	
						nextGrid.setAbstractCell(CellFactory.newDeadCell(lCell.getTuple().clone()));
					else 
						nextGrid.setAbstractCell(CellFactory.newEmptyCell(lCell.getTuple().clone()));
				}
			}
			else if (divisionCells.contains(lCell)) {//if the cell is about to divide
				if (nextGrid.getEmptyCells().size()>0) {//If there are any emptyCells
					if (this.epithelium.getEpitheliumEvents().getMCE(lCell.getModel()).getDivisionAlgorithm().equals(Txt.get("s_TAB_EVE_ALGORITHM_RANDOM"))){
						randomDivision(lCell,nextGrid);}
					else if (this.epithelium.getEpitheliumEvents().getMCE(lCell.getModel()).getDivisionAlgorithm().equals(Txt.get("s_TAB_EVE_ALGORITHM_MINIMUM_DISTANCE"))){
//						System.out.println("Cells are trying to divide");
						minimumDistance(lCell,nextGrid);
							
						}
				}
			}
		}

//		System.out.println("The next Grid state  is: ");
//		for (LivingCell cell : nextGrid.getAllLivingCells()) {
//			System.out.println("tuple:  " + cell.getTuple().toString());
//			System.out.println("state: " + Arrays.toString(cell.getState()));
//		}
		this.gridHistory.add(nextGrid);
		if (this.gridHashHistory != null) {
			this.gridHashHistory.add(nextGrid.hashGrid());
		}
		return nextGrid;
	}

	private List<List<LivingCell>> getBothProbabilities(List<LivingCell> availableLivingCells, float deathVal,
			float divVal) {
		
		List<List<LivingCell>> cells = new ArrayList<List<LivingCell>>();
		
		List<LivingCell> deathList =  new ArrayList<LivingCell>();

		List<LivingCell> divisionList =  new ArrayList<LivingCell>();
		
//		List<LivingCell> aux =  new ArrayList<LivingCell>();
		
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
	
	private List<Tuple2D<Integer>>  getEmptyCellsNeighbours(LivingCell lCell, EpitheliumGrid nextGrid) {
		

		List<Tuple2D<Integer>> lstNeighbours = new ArrayList<Tuple2D<Integer>>();
		//Get all neighbours within the distance Range
		if (!this.mTuple2Neighbours.containsKey(lCell.getTuple())) {
			for (int i=1; i<= this.epithelium.getEpitheliumEvents().getMCE(lCell.getModel()).getDivisionRange();i++){
				lstNeighbours.addAll(this.getNeighbours(i,lCell.getTuple()));
			}
			this.mTuple2Neighbours.put(lCell.getTuple(),lstNeighbours);
		}
		else 
			lstNeighbours = this.mTuple2Neighbours.get(lCell.getTuple());
		

		//Remove all neighbours that are not empty cells
		List<Tuple2D<Integer>> lstNeighbours2Remove =  new ArrayList<Tuple2D<Integer>>();
		for (Tuple2D<Integer> tuple: this.mTuple2Neighbours.get(lCell.getTuple())) {
			if (!nextGrid.getAbstCell(tuple.getX(),tuple.getY()).isEmptyCell()) {
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
	
	private void minimumDistance(LivingCell lCell, EpitheliumGrid nextGrid) {
		
		List<Tuple2D<Integer>> lstPossibleDestinations = getEmptyCellsNeighbours(lCell, nextGrid);
		
		List<LinkedList<Vertex>> lstPath = new ArrayList<LinkedList<Vertex>>();
//		
		Graph graph = initializeGraph(nextGrid);
		int minSize = 1000;
		
		for (Tuple2D<Integer> destination: lstPossibleDestinations) {
			LinkedList<Vertex> path = getPath(lCell.getTuple(), destination ,graph, this.mTuple2Vertex);
			if (path.size()<=minSize) {
				lstPath.add(path);
				minSize = path.size();
			}
		}
		
		if (lstPath.size()>1) {
			LinkedList<Vertex> path = lstPath.get(random.nextInt(lstPath.size()));
			this.displaceCells(path, nextGrid);
		}
		else if (lstPath.size()>0){
			
			this.displaceCells(lstPath.get(0), nextGrid);
		}
		
	}
	

	private void displaceCells(LinkedList<Vertex> path, EpitheliumGrid nextGrid) {
		
//        System.out.println("start: " + path.getFirst().getTuple().toString());
//        System.out.println("end: " +  path.getLast().getTuple().toString());
//        for (Vertex node: path) {
//        	System.out.println("node: " + node.getTuple().toString());
//        }
		
		Tuple2D<Integer> tupleDestination = path.getLast().getTuple();
		
		for (int index = (path.size()-1); index ==1; index--) {
//			System.out.println(index);
			Tuple2D<Integer> tuple = path.get(index).getTuple();
			AbstractCell cell = nextGrid.getAbstCell(tuple.getX(), tuple.getY());
			cell.setTuple(tupleDestination);
			tupleDestination = tuple;
		}
		updateCellSister(path.get(0).getTuple(), tupleDestination, nextGrid);
		
	}


	//OLD VERSION
//	private void minimumDistanceDivision(LivingCell lCell, EpitheliumGrid nextGrid) {
//		//We already know that there is at least an empty cell on the grid
//
//		
//		System.out.println("minimumDistanceDivision");
//		Tuple2D<Integer> originalTuple = lCell.getTuple().clone();
//		Tuple2D<Integer> sisterTuple = getSisterPosition(originalTuple, nextGrid.getEmptyCells());
//
//		LivingCell sisterCell = CellFactory.newLivingCell(sisterTuple, lCell.getModel());
//		LivingCell originalCell = CellFactory.newLivingCell(originalTuple, lCell.getModel());
//
//		//MaximumDistance = getDivisionRange()
//		for (int i=1; i<= this.epithelium.getEpitheliumEvents().getMCE(lCell.getModel()).getDivisionRange();i++){
//
//			//list of nearest neighbours
//			List<Tuple2D<Integer>> lstNeighbours = new ArrayList<Tuple2D<Integer>>();
//			lstNeighbours.addAll(this.getNeighbours(i,lCell.getTuple()));
//
//			for (int index = lstNeighbours.size()-1; index==0; index--) {
//				if (!nextGrid.getAbstCell(lstNeighbours.get(index).getX(), lstNeighbours.get(index).getY()).isEmptyCell()) {
//					lstNeighbours.remove(index);
//				}	
//			}
//			
//			System.out.println(lCell.getTuple().toString());
//			System.out.println(lstNeighbours);
//			
//			if (lstNeighbours.size()>0) {
//				
//				Collections.shuffle(lstNeighbours, this.random);
//				sisterCell.setTuple(lstNeighbours.get(0));
//
//				initializeGraph(lCell.getTuple(),sisterCell.getTuple());
//				updateCellSister( lCell,  originalCell,  sisterCell, nextGrid);
//
//				//				List<Tuple2D<Integer>> path = this.getPath(lCell.getTuple(), lstNeighbours.get(0));
//				//				this.displaceCells(path);
//				break;
//			}
//		}
//
//	}

	private Graph initializeGraph(EpitheliumGrid nextGrid) {
		
		this.mTuple2Vertex = new HashMap<Tuple2D<Integer>, Vertex>();
		List<Vertex> nodes = new ArrayList<Vertex>();
		List<Edge>   edges = new ArrayList<Edge>();
		
		//create vertex List of living and dead cells
		for (int x=0; x<this.epithelium.getX(); x++) {
			for(int y = 0; y<this.epithelium.getY(); y++){
				Tuple2D<Integer> tuple = new Tuple2D<Integer>(x,y);
				if (!nextGrid.getAbstCell(x, y).isInvalidCell()){
				Vertex v = new Vertex(tuple);
				this.mTuple2Vertex.put(tuple, v);
				nodes.add(v);
			}
}}
		
		//create Edges
		for (Tuple2D<Integer> tuple: this.mTuple2Vertex.keySet()) {
			Set<Tuple2D<Integer>> neighbours = getNeighbours(1, tuple);
			for (Tuple2D<Integer> tupleNei :neighbours) {
				if (nodes.contains(this.mTuple2Vertex.get(tupleNei))) {
				Edge edge = new Edge(tuple,this.mTuple2Vertex.get(tuple), this.mTuple2Vertex.get(tupleNei), 1 );
				edges.add(edge);
				}
			}
			
		}
		return new Graph(nodes, edges);
	}
		
		private LinkedList<Vertex> getPath (Tuple2D<Integer> start,Tuple2D<Integer> end , Graph graph,Map<Tuple2D<Integer>, Vertex> mTuple2Vertex) {
			
			
	    DijkstraAlgorithm dijkstra = new DijkstraAlgorithm(graph);
        dijkstra.execute(mTuple2Vertex.get(start));
        LinkedList<Vertex> path = dijkstra.getPath(mTuple2Vertex.get(end));
        return path;
		
	}


	private Set<Tuple2D<Integer>> getNeighbours(int i, Tuple2D<Integer> tuple) {

		Tuple2D<Integer> rangePair = new Tuple2D<Integer>(i,i);
		Tuple2D<Integer> rangeList_aux = new Tuple2D<Integer>(0,(i - 1 > 0) ? i - 1 : 0);

		Set<Tuple2D<Integer>> positionNeighbours = this.epithelium.getEpitheliumGrid().getPositionNeighbours(
				this.relativeNeighboursCache, rangeList_aux, rangePair,i, tuple.getX(), tuple.getY());

		return positionNeighbours;
	}

	private void randomDivision(LivingCell lCell, EpitheliumGrid nextGrid) {

//		System.out.println("randomDivision");
		Tuple2D<Integer> originalTuple = lCell.getTuple().clone();
		Tuple2D<Integer> sisterTuple = getSisterPosition(originalTuple, nextGrid.getEmptyCells());

		LivingCell sisterCell = CellFactory.newLivingCell(sisterTuple, lCell.getModel());
		LivingCell originalCell = CellFactory.newLivingCell(originalTuple, lCell.getModel());

		updateCellSister( lCell.getTuple(), sisterCell.getTuple(),  nextGrid) ;
	}

	
	//change the original cell the daughters
	private void updateCellSister(Tuple2D<Integer> originalTuple, Tuple2D<Integer> sisterTuple,EpitheliumGrid nextGrid) {
		
		LivingCell lCell = (LivingCell) nextGrid.getAbstCell(originalTuple.getX(), originalTuple.getY());
		LivingCell sisterCell = CellFactory.newLivingCell(sisterTuple, lCell.getModel());
		LivingCell originalCell = CellFactory.newLivingCell(lCell.getTuple(), lCell.getModel());


		if (this.epithelium.getEpitheliumEvents().getDivisionOption().equals(Txt.get("s_TAB_EPIUPDATE_NEWCELLSTATE_SAME"))) {
			sisterCell.setState(lCell.getState());
			originalCell.setState(lCell.getState());
			nextGrid.setAbstractCell(sisterCell);
			nextGrid.setAbstractCell(originalCell);
		}
		else if (this.epithelium.getEpitheliumEvents().getDivisionOption().equals(Txt.get("s_TAB_EPIUPDATE_NEWCELLSTATE_NAIVE"))) {
			nextGrid.setAbstractCell(sisterCell);
			nextGrid.setAbstractCell(originalCell);
		}
		else if (this.epithelium.getEpitheliumEvents().getDivisionOption().equals(Txt.get("s_TAB_EPIUPDATE_NEWCELLSTATE_PREDEFINED"))) {
			sisterCell.setState(this.epithelium.getEpitheliumEvents().getDivisionNewState(lCell.getModel()));
			originalCell.setState(this.epithelium.getEpitheliumEvents().getDivisionNewState(lCell.getModel()));
			nextGrid.setAbstractCell(sisterCell);
			nextGrid.setAbstractCell(originalCell);
		}
		else if (this.epithelium.getEpitheliumEvents().getDivisionOption().equals(Txt.get("s_TAB_EPIUPDATE_NEWCELLSTATE_RANDOM"))) {
			List<Boolean> randomSel = new ArrayList<Boolean>();
			randomSel.add(true);
			randomSel.add(false);
			Collections.shuffle(randomSel, this.random);
			if (randomSel.get(0))	{//NAIVE
				nextGrid.setAbstractCell(sisterCell);
				nextGrid.setAbstractCell(originalCell);
			}
			else 
			{//SAME
				sisterCell.setState(lCell.getState());
				originalCell.setState(lCell.getState());
				nextGrid.setAbstractCell(sisterCell);
				nextGrid.setAbstractCell(originalCell);
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
			// Update cell state
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
