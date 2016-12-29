package org.epilogtool.project;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.colomoto.logicalmodel.NodeInfo;
import org.epilogtool.common.Tuple3D;
import org.epilogtool.core.Epithelium;
import org.epilogtool.core.EpitheliumGrid;

/**
 * MonteCarlo
 * 
 * @author Pedro Varela
 * 
 */
public class MonteCarlo {

	private Epithelium epithelium;

	private int numberRuns;
	private int maxNumberIterations;
	private boolean randomIniC;

	private List<EpitheliumGrid> stableStates;
	private List<EpitheliumGrid> uniqueStableStates;
	private Map<EpitheliumGrid, Integer> stablestate2iteration;
	private Map<EpitheliumGrid, List<Integer>> uniqueStableStates2frequency;

	public MonteCarlo(Epithelium e) {
		this.epithelium = e;
	}

	public Map<EpitheliumGrid, Integer> getStableState2Iteration() {
		return this.stablestate2iteration;
	}

	public List<EpitheliumGrid> getStableStates() {
		return this.stableStates;
	}

	public boolean getMonteCarloInitialConditions() {
		return this.randomIniC;
	}

	public void setMonteCarloInitialConditions(boolean mcInic) {
		this.randomIniC = mcInic;
	}

	public int getNumberRuns() {
		return this.numberRuns;
	}

	public void setNumberRuns(int nRuns) {
		this.numberRuns = nRuns;
	}

	public int getMaxIter() {
		return this.maxNumberIterations;
	}

	public void setMaxIter(int maxIter) {
		this.maxNumberIterations = maxIter;
	}

	public Epithelium getEpithelium() {
		return this.epithelium;
	}

	public void run(Epithelium epi) {

		this.epithelium = epi;
		this.stablestate2iteration = new HashMap<EpitheliumGrid, Integer>();
		this.uniqueStableStates2frequency = new HashMap<EpitheliumGrid, List<Integer>>();
		this.stableStates = new ArrayList<EpitheliumGrid>();
		this.uniqueStableStates = new ArrayList<EpitheliumGrid>();

		for (int i = 0; i < this.numberRuns; i++) {
			Epithelium clonedEpi = this.epithelium.clone();

			if (randomIniC)
				clonedEpi.setRandomInitialConditions();

			Simulation sim = new Simulation(clonedEpi);

			for (int indexIteration = 0; indexIteration < this.maxNumberIterations; indexIteration++) {

				EpitheliumGrid nextGrid = sim.getGridAt(indexIteration + 1);

				if (sim.isStableAt(indexIteration + 1)) {
					stableStates.add(nextGrid);
					stablestate2iteration.put(nextGrid, indexIteration);
					stableStateExists(nextGrid);

					break;
				}
			}
		}
	}

	private boolean stableStateExists(EpitheliumGrid stableState) {

		if (this.stableStates.size() == 0)
			return false;
		else {
			for (EpitheliumGrid sState : this.stableStates) {
				if (sState.equals(stableState)) {

					List<Integer> freq = new ArrayList<Integer>();

					if (this.uniqueStableStates2frequency.containsKey(sState)) {
						freq = this.uniqueStableStates2frequency.get(sState);
						freq.add(stablestate2iteration.get(stableState));
						this.uniqueStableStates2frequency.put(sState, freq);
					} else {
						freq.add(stablestate2iteration.get(stableState));
						this.uniqueStableStates2frequency.put(sState, freq);
						this.uniqueStableStates.add(sState);
					}
					return true;
				}
			}
			return false;
		}
	}

	public List<EpitheliumGrid> getUniqueStableStates() {
		return this.uniqueStableStates;
	}

	public Map<EpitheliumGrid, List<Integer>> getUniqueStableStates2Frequency() {
		return this.uniqueStableStates2frequency;
	}

	public Map<Tuple3D<Integer>, Float> createCumulative() {
		// TODO Should I add the value? I am just

		Map<Tuple3D<Integer>, Float> cellNode2Count = new HashMap<Tuple3D<Integer>, Float>(); // Ncells
																			// with
																			// cell
																			// node
		Map<Tuple3D<Integer>, Float> cellNode2Average = new HashMap<Tuple3D<Integer>, Float>();

		if ((this.stableStates != null) && (this.stableStates.size() > 0)) {
			for (EpitheliumGrid stableState : this.stableStates) {
				for (int x = 0; x < stableState.getX(); x++) {
					for (int y = 0; y < stableState.getY(); y++) {
						for (NodeInfo node : stableState.getModel(x, y).getNodeOrder()) {
							Tuple3D<Integer> cellNode = new Tuple3D<Integer>(x, y, node.getNodeID());
							if (!cellNode2Count.keySet().contains(cellNode)) {
								cellNode2Count.put(cellNode, (float) 1);
							} else {
								int count = stableState.getCellValue(x, y, node.getNodeID());
								cellNode2Count.put(cellNode, cellNode2Count.get(cellNode) + count);
							}
						}
					}
				}

				for (Tuple3D<Integer> t : cellNode2Count.keySet()) {
					int numCells = this.epithelium.getX() * this.epithelium.getY();
					float res = cellNode2Count.get(t) / numCells;
					cellNode2Average.put(t, res);
				}
			}
		}
		return cellNode2Count;
	}

}
