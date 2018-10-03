package org.epilogtool.project;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
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

		for (LivingCell livingCell: livingCells) {
			Tuple2D<Integer> tuple = livingCell.getTuple();
			int x = (int) tuple.getX();
			int y = (int) tuple.getY();
			if (!nextGrid.getAbstCell(x,y).isLivingCell()) {
				continue;
			}
			byte[] currState = currGrid.getCellState(x, y);

			// Compute next state
			byte[] nextState = this.nextCellValue(x, y, currGrid, evaluator, sNodes);

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
		//TODO if this state is stable should we stop here? Stability is maintained? what about different number of cells
		if (changedKeys.isEmpty()) {
			this.stable = true;
			return currGrid;
		}

		//************* TRIGGER EVENTS


		List<LivingCell> deathCells = new ArrayList<LivingCell>();
		List<LivingCell> divisionCells = new ArrayList<LivingCell>();

		for (LogicalModel model: nextGrid.getModelSet()) {
			ModelCellularEvent mce =  this.epithelium.getEpitheliumEvents().getMCE(model);
			List<LivingCell> availableLivingCells = nextGrid.getLivingCells(model);
			String deathTrigger = mce.getDeathTrigger();
			String divisionTrigger = mce.getDivisionTrigger();
			//Validation of patterns is made at the eEpiTabEvents level

			if (deathTrigger.equals(Txt.get("s_TAB_EVE_TRIGGER_PATTERN"))) {
				//				System.out.println(model + " death is by pattern 7: retrieve a set of cells to be marked as set to die");
				//update Living Cells
			}
			if (divisionTrigger.equals(Txt.get("s_TAB_EVE_TRIGGER_PATTERN"))) {
				//				System.out.println(model + " division is by pattern: retrieve a set of cells to be marked as set to divide");
				//Update living Cells
			}

			if (deathTrigger.equals(Txt.get("s_TAB_EVE_TRIGGER_RANDOM"))) {

				deathCells = getProportionOfCells(availableLivingCells, this.epithelium.getEpitheliumEvents().getMCE(model).getDeathValue());
				availableLivingCells.removeAll(deathCells);
				//				System.out.println(model + " death is by random: retrieve a set of cells to be marked as set to die");
				//				System.out.println("deathCells: " + deathCells);
			}

			if (divisionTrigger.equals(Txt.get("s_TAB_EVE_TRIGGER_RANDOM"))) {
				//				System.out.println(model + " division is by random: retrieve a set of cells to be marked as set to die");
				divisionCells = getProportionOfCells(availableLivingCells, this.epithelium.getEpitheliumEvents().getMCE(model).getDivisionValue());
				availableLivingCells.removeAll(divisionCells);
				//				System.out.println("divideCells: " + divisionCells);
			}
		}

		//************* Event Action

		//TODO: TEST EVERYTHING
		String order = this.epithelium.getEpitheliumEvents().getEventOrder();
		List<LivingCell> orderedCells = new ArrayList<LivingCell>();

		//		System.out.println("order" + order);
		if (order.equals(Txt.get("s_TAB_EPIUPDATE_ORDER_DIVDEATH"))) {
			orderedCells.addAll(divisionCells);
			orderedCells.addAll(deathCells);
			//			System.out.println("orderedCells: " + orderedCells);
		}
		else if (order.equals(Txt.get("s_TAB_EPIUPDATE_ORDER_DEATHDIV"))) {
			orderedCells.addAll(deathCells);
			orderedCells.addAll(divisionCells);
			//			System.out.println("orderedCells: " + orderedCells);
		}
		else if (order.equals(Txt.get("s_TAB_EPIUPDATE_ORDER_RANDOM"))) {
			orderedCells.addAll(deathCells);
			orderedCells.addAll(divisionCells);
			Collections.shuffle(orderedCells, this.random);
			//			System.out.println("orderedCells: " + orderedCells);

		}

		//TODO FIX-ME
		for (LivingCell lCell: orderedCells) {
			
			System.out.println(currGrid.getEmptyCells().size());
			if (deathCells.contains(lCell)) {//If this cell is about to die
				if (this.epithelium.getEpitheliumEvents().getDeathOption().equals(Txt.get("s_TAB_EPIUPDATE_CELLDEATH_EMPTY"))) {
					nextGrid.setAbstractCell(CellFactory.newEmptyCell(lCell.getTuple().clone()));
				}
				else if (this.epithelium.getEpitheliumEvents().getDeathOption().equals(Txt.get("s_TAB_EPIUPDATE_CELLDEATH_PERMANENT")))
					nextGrid.setAbstractCell(CellFactory.newDeadCell(lCell.getTuple().clone()));
				else if (this.epithelium.getEpitheliumEvents().getDeathOption().equals(Txt.get("s_TAB_EPIUPDATE_CELLDEATH_RANDOM"))) {
					List<Boolean> randomSel = new ArrayList<Boolean>();
					randomSel.add(true);
					randomSel.add(false);
					Collections.shuffle(randomSel, this.random);
					if (randomSel.get(0))	
						nextGrid.setAbstractCell(CellFactory.newDeadCell(lCell.getTuple().clone()));
					else 
						nextGrid.setAbstractCell(CellFactory.newEmptyCell(lCell.getTuple().clone()));
				}
			}
			else if (divisionCells.contains(lCell)) {//if the cell is about to divide
				if (nextGrid.getEmptyCells().size()>0) {//If there are any emptyCells
					if (this.epithelium.getEpitheliumEvents().getMCE(lCell.getModel()).getDivisionAlgorithm().equals(Txt.get("s_TAB_EPIUPDATE_ALGORITHM_RANDOM"))){
						randomDivision(lCell,nextGrid);
					}
				}
			}}


		this.gridHistory.add(nextGrid);
		if (this.gridHashHistory != null) {
			this.gridHashHistory.add(nextGrid.hashGrid());
			//			System.out.println("added a new grid");
		}
		return nextGrid;
	}

	private void randomDivision(LivingCell lCell, EpitheliumGrid nextGrid) {
		
		Tuple2D<Integer> originalTuple = lCell.getTuple().clone();
		Tuple2D<Integer> sisterTuple = getSisterPosition(originalTuple, nextGrid.getEmptyCells());

		LivingCell sisterCell = CellFactory.newLivingCell(sisterTuple, lCell.getModel());
		LivingCell originalCell = CellFactory.newLivingCell(originalTuple, lCell.getModel());
		
		System.out.println(sisterCell.getTuple());
		System.out.println(originalCell.getTuple());

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
			//			System.out.println("RANDOMN STATE");
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
		return emptyCells.get(0).getTuple() ;
	}

	private List<LivingCell> getProportionOfCells(List<LivingCell> livingCells, float value) {

		List<LivingCell> cells = new ArrayList<LivingCell>();

		int nToChange = (int) Math.floor(value * livingCells.size());

		// Create the initial shuffled array of cells
		Collections.shuffle(livingCells, this.random);

		for (int i = 0; i < nToChange; i++) {
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
		byte[] currState = currGrid.getCellState(x, y).clone();
		LogicalModel m = currGrid.getModel(x, y);
		AbstractPerturbation ap = currGrid.getPerturbation(x, y);
		PriorityUpdater updater = this.updaterCache.get(m).get(ap);


		// 2. Update integration components
		for (NodeInfo node :this.epithelium.getIntegrationNodes()) {
			if (m.getComponents().contains(node)) {
				if (node.isInput() && sNodeInfos.contains(node)) {

					List<IntegrationFunctionExpression> lExpressions = this.epithelium
							.getIntegrationFunctionsForComponent(node).getComputedExpressions();
					byte target = 0;
					for (int i = 0; i < lExpressions.size(); i++) {
						if (evaluator.evaluate(x, y, lExpressions.get(i))) {
							target = (byte) (i + 1);
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
		if (this.isSynchronous()) {
			String sGrid = this.gridHashHistory.get(this.gridHashHistory.size() - 1);
			// Tmp
			List<String> lTmp = new ArrayList<String>(this.gridHashHistory);
			lTmp.remove(this.gridHashHistory.size()-1);
			int posBeforeLast = lTmp.lastIndexOf(sGrid);

			if (posBeforeLast > 0) {
				return (this.gridHashHistory.size() - 1) - posBeforeLast;
			}
		}
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
