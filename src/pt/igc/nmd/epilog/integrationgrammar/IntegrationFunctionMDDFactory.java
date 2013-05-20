package pt.igc.nmd.epilog.integrationgrammar;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.colomoto.logicalmodel.NodeInfo;
import org.colomoto.mddlib.MDDManager;
import org.colomoto.mddlib.MDDVariable;
import org.colomoto.mddlib.operators.MDDBaseOperators;

import pt.igc.nmd.epilog.integrationgrammar.IntegrationFunctionSpecification.IntegrationAtom;
import pt.igc.nmd.epilog.integrationgrammar.IntegrationFunctionSpecification.IntegrationExpression;
import pt.igc.nmd.epilog.integrationgrammar.IntegrationFunctionSpecification.IntegrationNegation;
import pt.igc.nmd.epilog.integrationgrammar.IntegrationFunctionSpecification.IntegrationOperation;

public class IntegrationFunctionMDDFactory {

	private CompositionContext context = null;
	private MDDManager manager = null;

	public IntegrationFunctionMDDFactory(CompositionContext context,
			MDDManager manager) {
		this.context = context;
		this.manager = manager;
	}

	public int getMDD(IntegrationExpression expression, int instance) {
		return getMDD(expression, instance, true);
	}

	public int getMDD(IntegrationExpression expression, int instance,
			boolean optimize) {

		if (expression instanceof IntegrationOperation) {
			List<IntegrationExpression> listOperands = ((IntegrationOperation) expression)
					.getOperands();

			IntegrationLogicalOperator operator = ((IntegrationOperation) expression)
					.getOperation();

			switch (operator) {
			case AND: {
				int intermediate = 1;
				for (IntegrationExpression operand : listOperands)
					if (operand != null)
						intermediate = MDDBaseOperators.AND.combine(manager,
								intermediate, getMDD(operand, instance));

				return intermediate;
			}
			case OR: {
				int intermediate = 0;
				for (IntegrationExpression operand : listOperands)
					if (operand != null)
						intermediate = MDDBaseOperators.OR.combine(manager,
								intermediate, getMDD(operand, instance));

				return intermediate;
			}
			default:
				return 0;

			}
		} else if (expression instanceof IntegrationNegation) {

			return manager.not(getMDD(
					((IntegrationNegation) expression).getNegatedExpression(),
					instance, false));

		} else if (expression instanceof IntegrationAtom) {
			IntegrationAtom atom = (IntegrationAtom) expression;

			byte threshold = atom.getThreshold();
			int min = atom.getMinNeighbours();
			int max = atom.getMaxNeighbours();
			int minDistance = atom.getMinDistance();
			int maxDistance = atom.getMaxDistance();

			Set<Integer> neighbours = context.getNeighbourIndices(instance,
					minDistance, maxDistance);
			List<Integer> nList = new ArrayList<Integer>();
			for (Integer neighbour : neighbours)
				nList.add(neighbour);

			if (min == -1)
				min = neighbours.size();

			if (max == -1 || max > neighbours.size())
				max = neighbours.size();

			int result = 0;

			if (min > neighbours.size() || min > max) {
				// condition is trivially impossible to satisfy
				return 0;
			} else if (threshold == 0 && max < neighbours.size()) {
				// condition is trivially impossible to satisfy
				return 0;
			} else if (min == 0 && max == neighbours.size()) {
				// condition is trivially tautological
				return 1;
			} else if (threshold == 0 && max == neighbours.size()) {
				// condition is trivially tautological
				return 1;
			}

			if (max == neighbours.size() && optimize) {
				// need only guarantee the mininum number of neighbours
				List<boolean[]> masks = generateNeighboursMask(min, nList);
				for (boolean[] mask : masks) {
					int conjunction = 1;

					for (int i = 0; i < mask.length; i++) {

						if (mask[i]) {

							NodeInfo node = context
									.getLowLevelComponentFromName(
											atom.getComponentName(),
											nList.get(i).intValue());
							int disjunction = 0;

							for (byte l = threshold; l <= node.getMax(); l++) {
								IntegrationFunctionClause clause = new IntegrationFunctionClause();

								clause.addConstraint(node, l);
								disjunction = MDDBaseOperators.OR
										.combine(
												manager,
												disjunction,
												buildMDDPath(manager, clause
														.toByteArray(context),
														1));

							}

							conjunction = MDDBaseOperators.AND.combine(manager,
									conjunction, disjunction);

						} // else we don't care if more neighbours are
							// habilitated

					}

					result = MDDBaseOperators.OR.combine(manager, result,
							conjunction);
				}

			} else {
				// needs to guarantee strict respect for the minimum and maximum
				// number of habilitated neighbours
				for (int v = min; v <= max; v++) {
					List<boolean[]> masks = generateNeighboursMask(v, nList);

					for (boolean[] mask : masks) {
						int conjunction = 1;

						for (int i = 0; i < mask.length; i++) {

							NodeInfo node = context
									.getLowLevelComponentFromName(
											atom.getComponentName(),
											nList.get(i).intValue());

							int disjunction = 0;

							if (mask[i]) {
								for (byte l = threshold; l <= node.getMax(); l++) {
									IntegrationFunctionClause clause = new IntegrationFunctionClause();

									clause.addConstraint(node, l);
									disjunction = MDDBaseOperators.OR
											.combine(
													manager,
													disjunction,
													buildMDDPath(
															manager,
															clause.toByteArray(context),
															1));

								}
							} else {
								for (byte l = 0; l < threshold; l++) {
									IntegrationFunctionClause clause = new IntegrationFunctionClause();

									clause.addConstraint(node, l);
									disjunction = MDDBaseOperators.OR
											.combine(
													manager,
													disjunction,
													buildMDDPath(
															manager,
															clause.toByteArray(context),
															1));

								}

							}
							conjunction = MDDBaseOperators.AND.combine(manager,
									conjunction, disjunction);

						}
						result = MDDBaseOperators.OR.combine(manager, result,
								conjunction);
					}

				}
			}

			return result;

		} else { // this should never happen
			return -1;
		}

	}

	private int buildMDDPath(MDDManager ddm, byte[] state, int leaf) {
		MDDVariable[] ddVariables = ddm.getAllVariables();
		int mddPath = leaf;
		for (int i = ddVariables.length - 1; i >= 0; i--) {
			int[] children = new int[ddVariables[i].nbval];
			if (state[i] >= 0) {
				children[state[i]] = mddPath;
				mddPath = ddVariables[i].getNode(children);
			}
		}
		return mddPath;
	}

	private static List<boolean[]> generateNeighboursMask(int v,
			List<Integer> neighbours) {
		boolean[] mask = new boolean[neighbours.size()];
		for (int i = 0; i < mask.length; i++)
			mask[i] = false;
		return generateNeighboursMask(v, neighbours.size(), 0, mask);
	}

	private static List<boolean[]> generateNeighboursMask(int n, int m,
			int offset, boolean[] frozen) {
		List<boolean[]> masks = new ArrayList<boolean[]>();

		int places = m - offset;

		if (n == 0) {
			boolean[] mask = new boolean[m];
			for (int i = 0; i < m; i++)
				mask[i] = frozen[i];
			masks.add(mask);
		} else if (places == n) {
			boolean[] mask = new boolean[m];
			for (int i = 0; i < m; i++) {
				if (i < offset) {
					mask[i] = frozen[i];
				} else {
					mask[i] = true;
				}
			}

			masks.add(mask);
		} else {
			boolean[] mask = new boolean[m];
			for (int i = 0; i < m; i++) {
				if (i < offset) {
					mask[i] = frozen[i];
				}
			}

			mask[offset] = true;
			masks.addAll(generateNeighboursMask(n - 1, m, offset + 1, mask));
			mask[offset] = false;
			masks.addAll(generateNeighboursMask(n, m, offset + 1, mask));

		}

		return masks;
	}

}
