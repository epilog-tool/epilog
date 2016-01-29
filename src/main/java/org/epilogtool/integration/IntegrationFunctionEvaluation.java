package org.epilogtool.integration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.colomoto.logicalmodel.NodeInfo;
import org.epilogtool.common.Tuple2D;
import org.epilogtool.core.EpitheliumGrid;
import org.epilogtool.integration.IntegrationFunctionSpecification.IntegrationAtom;
import org.epilogtool.integration.IntegrationFunctionSpecification.IntegrationExpression;
import org.epilogtool.integration.IntegrationFunctionSpecification.IntegrationNegation;
import org.epilogtool.integration.IntegrationFunctionSpecification.IntegrationOperation;
import org.epilogtool.project.ProjectFeatures;

public class IntegrationFunctionEvaluation {

	private EpitheliumGrid neighboursGrid;
	private ProjectFeatures features;
	private Map<List<Integer>, Map<Boolean, Set<Tuple2D<Integer>>>> relativeNeighboursCache;

	/**
	 * Evaluates an expression.
	 * 
	 * @param expression
	 *            integration expression
	 * @param context
	 *            composition context
	 * 
	 * 
	 */
	public IntegrationFunctionEvaluation(EpitheliumGrid neighboursGrid, 
			ProjectFeatures features) {
		this.neighboursGrid = neighboursGrid;
		this.features = features;
		this.relativeNeighboursCache = new 
				HashMap<List<Integer>, Map<Boolean, Set<Tuple2D<Integer>>>>();
	}

	/**
	 * Evaluates an instance.
	 * 
	 * @param instance
	 *            instance to evaluate
	 * @param state
	 *            state of the world
	 * @return state of the instance
	 * 
	 * 
	 */
	public boolean evaluate(int x, int y, IntegrationExpression expression) {
		return traverseTreeEvaluate(x, y, expression);
	}

	/**
	 * Evaluates an instance
	 * 
	 * @param instance
	 *            instance to evaluate
	 * @param state
	 *            state of the world
	 * @param expression
	 *            integration expression
	 * @return true if result exists
	 * 
	 * 
	 */
	private boolean traverseTreeEvaluate(int x, int y,
			IntegrationExpression expression) {

		boolean result = false;
		// TODO: get rid of instanceof and call a polimorfic evaluate() on
		// subclasses

		if (expression instanceof IntegrationOperation) {
			List<IntegrationExpression> listOperands = ((IntegrationOperation) expression)
					.getOperands();

			IntegrationLogicalOperator operator = ((IntegrationOperation) expression)
					.getOperation();

			switch (operator) {
			case AND:
				result = true;
				for (IntegrationExpression operand : listOperands)
					if (operand == null)
						continue;
					else if (!traverseTreeEvaluate(x, y, operand)) {
						return false;
					}
				break;
			case OR:
				result = false;
				for (IntegrationExpression operand : listOperands)
					if (operand == null)
						continue;
					else if (traverseTreeEvaluate(x, y, operand)) {
						return true;
					}
				break;
			}
		} else if (expression instanceof IntegrationNegation) {
			return !traverseTreeEvaluate(x, y,
					((IntegrationNegation) expression).getNegatedExpression());

		} else if (expression instanceof IntegrationAtom) {
			IntegrationAtom atom = (IntegrationAtom) expression;
			
			List<Integer> rangeList = this.getIntegrationAtomRange(atom);
			
			//TODO: temporary solution, should have its own method
			if (!this.relativeNeighboursCache.containsKey(rangeList)) {
				Map<Boolean, Set<Tuple2D<Integer>>> neighbours = new HashMap<Boolean, Set<Tuple2D<Integer>>>();
				neighbours.put(true, this.neighboursGrid.getTopology().
							getRelativeNeighbours(true, rangeList.get(0), rangeList.get(1)));
				neighbours.put(false, this.neighboursGrid.getTopology().
						getRelativeNeighbours(false, rangeList.get(0), rangeList.get(1)));
				this.relativeNeighboursCache.put(rangeList, neighbours);
			}
			//End
			
			boolean even = this.neighboursGrid.getTopology().isEven(x, y);
			Set<Tuple2D<Integer>> neighbours = this.neighboursGrid.getTopology()
					.getPositionNeighbours(x, y, this.relativeNeighboursCache.get(rangeList).get(even));
			NodeInfo node = this.features.getNodeInfo(atom.getComponentName(),
					this.neighboursGrid.getModel(x, y));

			byte minThreshold = atom.getMinThreshold();
			if (minThreshold < 0)
				minThreshold = node.getMax();
			byte maxThreshold = atom.getMaxThreshold();
			if (maxThreshold < 0)
				maxThreshold = node.getMax();
			if (minThreshold > maxThreshold || minThreshold > node.getMax())
				return false;

			int min = atom.getMinNeighbours();
			if (min < 0)
				min = neighbours.size();
			int max = atom.getMaxNeighbours();
			if (max < 0)
				max = neighbours.size();
			if (min > neighbours.size() || min > max)
				return false;
			if (min == 0 && max == neighbours.size())
				return true;

			int habilitations = 0;
			for (Tuple2D<Integer> tuple : neighbours) {
				List<NodeInfo> lNodes = this.neighboursGrid.getModel(tuple.getX(),
						tuple.getY()).getNodeOrder();
				for (int n = 0; n < lNodes.size(); n++) {
					if (node.getNodeID().equals(lNodes.get(n).getNodeID())) {
						byte state = this.neighboursGrid.getCellState(tuple.getX(),
								tuple.getY())[n];
						if (minThreshold <= state && state <= maxThreshold) {
							habilitations++;
							break;
						}
					}
				}
			}
			if (habilitations >= min && habilitations <= max)
				return true;
			else
				return false;

		}
		return result;
	}

	public Set<String> traverseTreeRegulators(IntegrationExpression expr) {
		Set<String> sNodeIDs = new HashSet<String>();
		if (expr instanceof IntegrationOperation) {
			for (IntegrationExpression operand : ((IntegrationOperation) expr)
					.getOperands()) {
				if (operand != null) {
					sNodeIDs.addAll(traverseTreeRegulators(operand));
				}
			}
		}
		if (expr instanceof IntegrationNegation) {
			sNodeIDs.addAll(traverseTreeRegulators(((IntegrationNegation) expr)
					.getNegatedExpression()));
		} else if (expr instanceof IntegrationAtom) {
			sNodeIDs.add(((IntegrationAtom) expr).getComponentName());
		}
		return sNodeIDs;
	}
	
	public List<Integer> getIntegrationAtomRange(IntegrationAtom atom) {
		List<Integer> rangeList = new ArrayList<Integer>();
		rangeList.add(atom.getMinDistance());
		rangeList.add(atom.getMaxDistance());
		return rangeList;
	}
}
