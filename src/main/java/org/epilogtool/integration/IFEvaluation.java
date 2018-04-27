package org.epilogtool.integration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.colomoto.biolqm.NodeInfo;
import org.epilogtool.common.Tuple2D;
import org.epilogtool.core.Epithelium;
import org.epilogtool.core.EpitheliumGrid;

public class IFEvaluation {

	private EpitheliumGrid neighboursGrid;
	private Map<List<Integer>, Map<Boolean, Set<Tuple2D<Integer>>>> relativeNeighboursCache;
	private Epithelium epithelium;

	public IFEvaluation(EpitheliumGrid neighboursGrid, Epithelium epithelium) {
		this.neighboursGrid = neighboursGrid;
		this.epithelium = epithelium;
		this.relativeNeighboursCache = new HashMap<List<Integer>, Map<Boolean, Set<Tuple2D<Integer>>>>();
	}

	public boolean evaluate(int x, int y, IntegrationFunctionExpression expression) {
		return traverseTreeIFEvaluate(x, y, expression);
	}

	private boolean traverseTreeIFEvaluate(int x, int y, IntegrationFunctionExpression expression) {
		// TODO: get rid of instanceof and call a polimorfic evaluate() on
		// subclasses

		if (expression instanceof IntegrationFunctionOperation) {
			List<IntegrationFunctionExpression> listOperands = ((IntegrationFunctionOperation) expression)
					.getOperands();

			LogicalOperator operator = ((IntegrationFunctionOperation) expression).getOperation();

			switch (operator) {
			case AND:
				for (IntegrationFunctionExpression operand : listOperands) {
					if (operand == null)
						continue;
					else if (!traverseTreeIFEvaluate(x, y, operand)) {
						return false;
					}
				}
				return true;
			case OR:
				for (IntegrationFunctionExpression operand : listOperands) {
					if (operand == null)
						continue;
					else if (traverseTreeIFEvaluate(x, y, operand)) {
						return true;
					}
				}
				return false;
			}

		} else if (expression instanceof IntegrationFunctionNOT) {
			return !traverseTreeIFEvaluate(x, y, ((IntegrationFunctionNOT) expression).getNegatedExpression());

		} else if (expression instanceof CardinalityConstraint) {
			CardinalityConstraint cc = (CardinalityConstraint) expression;
			int minCells = cc.getMinCells();
			int maxCells = cc.getMaxCells();

			if (minCells == 0 && maxCells == -1) {
				return true;
			}
			// Check SignalExpression
			Set<Tuple2D<Integer>> neighbours = traverseTreeISEvaluate(x, y, cc.getSignalExpr());

			if (minCells != -1 && neighbours.size() < minCells || maxCells != -1 && neighbours.size() > maxCells) {
				return false;
			}
			return true;
		} else if (expression instanceof IntegrationFunctionFALSE) {
			return false;
		} else if (expression instanceof IntegrationFunctionTRUE) {
			return true;
		}
		// TODO: this last return should not be here.
		return false;
	}

	private Set<Tuple2D<Integer>> traverseTreeISEvaluate(int x, int y, IntegrationSignalExpression expression) {
		Set<Tuple2D<Integer>> result = new HashSet<Tuple2D<Integer>>();

		if (expression instanceof IntegrationSignalOperation) {
			List<IntegrationSignalExpression> listOperands = ((IntegrationSignalOperation) expression).getOperands();

			LogicalOperator operator = ((IntegrationSignalOperation) expression).getOperation();

			switch (operator) {
			case AND:
				// This should not happen
				// TODO: Through exception
				break;
			case OR:
				for (IntegrationSignalExpression operand : listOperands) {
					if (operand == null) {
						continue;
					} else {
						result.addAll(traverseTreeISEvaluate(x, y, operand));
					}
				}
				break;
			}

		} else if (expression instanceof IntegrationSignal) {
			IntegrationSignal signal = (IntegrationSignal) expression;

			List<Integer> rangeList = new ArrayList<Integer>();
			rangeList.add(signal.getDistance().getMin());
			rangeList.add(signal.getDistance().getMax());

			List<Integer> rangeList_aux = new ArrayList<Integer>();
			rangeList_aux.add(0);
			if (signal.getDistance().getMin()-1>0)
				rangeList_aux.add(signal.getDistance().getMin()-1);
			else
				rangeList_aux.add(0);

			if (!this.relativeNeighboursCache.containsKey(rangeList_aux)) {
				Map<Boolean, Set<Tuple2D<Integer>>> neighboursOutskirts = new HashMap<Boolean, Set<Tuple2D<Integer>>>();
				neighboursOutskirts.put(true, this.neighboursGrid.getTopology().getRelativeNeighbours(true, rangeList_aux.get(0),
						rangeList_aux.get(1)));
				neighboursOutskirts.put(false, this.neighboursGrid.getTopology().getRelativeNeighbours(false, rangeList_aux.get(0),
						rangeList_aux.get(1)));
				this.relativeNeighboursCache.put(rangeList_aux, neighboursOutskirts);
			}

			if (!this.relativeNeighboursCache.containsKey(rangeList)) {
				Map<Boolean, Set<Tuple2D<Integer>>> neighbours = new HashMap<Boolean, Set<Tuple2D<Integer>>>();
				neighbours.put(true, this.neighboursGrid.getTopology().getRelativeNeighbours(true, rangeList.get(0),
						rangeList.get(1)));
				neighbours.put(false, this.neighboursGrid.getTopology().getRelativeNeighbours(false, rangeList.get(0),
						rangeList.get(1)));
				this.relativeNeighboursCache.put(rangeList, neighbours);
			}

			boolean even = this.neighboursGrid.getTopology().isEven(x, y);
			
			Set<Tuple2D<Integer>> neighbours = this.neighboursGrid.getTopology().getPositionNeighbours(x, y,
					this.relativeNeighboursCache.get(rangeList).get(even));
			Set<Tuple2D<Integer>> neighboursOutskirts = this.neighboursGrid.getTopology().getPositionNeighbours(x, y,
					this.relativeNeighboursCache.get(rangeList_aux).get(even));
//			
//			System.out.println("IFEvaluation-> Neighbours: " + neighbours);
//			System.out.println("IFEvaluation-> neighboursOutskirts: " + neighboursOutskirts);
			
			Set<Tuple2D<Integer>> finalNeighbours = new HashSet<Tuple2D<Integer>>();
			for (Tuple2D<Integer> nei: neighbours) {
				if (!neighboursOutskirts.contains(nei)) {
//					System.out.println(nei);
					finalNeighbours.add(nei);
				}
			}
			
			
			System.out.println("IFEvaluation-> x: " + x+ " y: "+ y);
			System.out.println("IFEvaluation-> " + finalNeighbours);
			
			
			NodeInfo node = this.epithelium.getComponentUsed(signal.getComponentName());

			if (node == null) {
				// Invalid node
				// TODO: launch exception
			}
			byte minThreshold = signal.getMinThreshold();
			if (minThreshold < 0 || minThreshold > node.getMax()) {
				return result;
			}

			for (Tuple2D<Integer> tuple : finalNeighbours) {
				List<NodeInfo> lNodes = this.neighboursGrid.getModel(tuple.getX(), tuple.getY()).getComponents();
				for (int n = 0; n < lNodes.size(); n++) {
					if (node.getNodeID().equals(lNodes.get(n).getNodeID())) {
						byte state = this.neighboursGrid.getCellState(tuple.getX(), tuple.getY())[n];
						if (minThreshold <= state) {
							result.add(tuple);
							break;
						}
					}
				}
			}
		}
		return result;
	}

	public Set<String> traverseIFTreeRegulators(IntegrationFunctionExpression expr) {
		Set<String> sNodeIDs = new HashSet<String>();
		if (expr instanceof IntegrationFunctionOperation) {
			for (IntegrationFunctionExpression operand : ((IntegrationFunctionOperation) expr).getOperands()) {
				if (operand != null) {
					sNodeIDs.addAll(traverseIFTreeRegulators(operand));
				}
			}
		} else if (expr instanceof IntegrationFunctionNOT) {
			sNodeIDs.addAll(traverseIFTreeRegulators(((IntegrationFunctionNOT) expr).getNegatedExpression()));
		} else if (expr instanceof CardinalityConstraint) {
			sNodeIDs.addAll(traverseISTreeRegulators(((CardinalityConstraint) expr).getSignalExpr()));
		}
		return sNodeIDs;
	}

	public Set<String> traverseISTreeRegulators(IntegrationSignalExpression expr) {
		Set<String> sNodeIDs = new HashSet<String>();
		if (expr instanceof IntegrationSignalOperation) {
			for (IntegrationSignalExpression operand : ((IntegrationSignalOperation) expr).getOperands()) {
				if (operand != null) {
					sNodeIDs.addAll(traverseISTreeRegulators(operand));
				}
			}
		} else if (expr instanceof IntegrationSignal) {
			sNodeIDs.add(((IntegrationSignal) expr).getComponentName());
		}
		return sNodeIDs;
	}
}
