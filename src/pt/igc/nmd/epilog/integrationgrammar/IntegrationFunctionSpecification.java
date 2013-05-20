package pt.igc.nmd.epilog.integrationgrammar;

import java.util.ArrayList;
import java.util.List;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;

public class IntegrationFunctionSpecification {

	public static abstract interface IntegrationExpression {
	}

	public static abstract class IntegrationOperation implements
			IntegrationExpression {
		private IntegrationLogicalOperator operator = null;
		private IntegrationExpression expression1 = null;
		private IntegrationExpression expression2 = null;

		public IntegrationOperation(IntegrationExpression exp1,
				IntegrationExpression exp2, IntegrationLogicalOperator operator) {
			this.expression1 = exp1;
			this.expression2 = exp2;
			this.operator = operator;
		}

		public IntegrationLogicalOperator getOperation() {
			return this.operator;
		}

		public List<IntegrationExpression> getOperands() {
			List<IntegrationExpression> listExpression = new ArrayList<IntegrationExpression>();
			listExpression.add(this.expression1);
			listExpression.add(this.expression2);

			return listExpression;
		}

	}

	public static class IntegrationDisjunction extends IntegrationOperation {

		public IntegrationDisjunction(IntegrationExpression exp1,
				IntegrationExpression exp2) {
			super(exp1, exp2, IntegrationLogicalOperator.OR);
		}

	}

	public static class IntegrationConjunction extends IntegrationOperation {

		public IntegrationConjunction(IntegrationExpression exp1,
				IntegrationExpression exp2) {
			super(exp1, exp2, IntegrationLogicalOperator.AND);
		}

	}

	public static class IntegrationAtom implements IntegrationExpression {
		private String componentName = null;
		private byte threshold = 0;
		private int minNeighbours = 0;
		private int maxNeighbours = 0;
		private int minDistance = 1;
		private int maxDistance = 1;

		public IntegrationAtom(String componentName, byte threshold,
				int minNeighbours, int maxNeighbours) {
			this.componentName = componentName;
			this.threshold = threshold;
			this.minNeighbours = minNeighbours;
			this.maxNeighbours = maxNeighbours;
			this.minDistance = 1;
			this.maxDistance = 1;
		}

		public IntegrationAtom(String componentName, byte threshold,
				int minNeighbours, int maxNeighbours, int distance) {
			this.componentName = componentName;
			this.threshold = threshold;
			this.minNeighbours = minNeighbours;
			this.maxNeighbours = maxNeighbours;
			this.minDistance = distance;
			this.maxDistance = distance;

		}

		public IntegrationAtom(String componentName, byte threshold,
				int minNeighbours, int maxNeighbours, int minDistance,
				int maxDistance) {
			this.componentName = componentName;
			this.threshold = threshold;
			this.minNeighbours = minNeighbours;
			this.maxNeighbours = maxNeighbours;
			this.minDistance = minDistance;
			this.maxDistance = maxDistance;

		}

		public String getComponentName() {
			return this.componentName;
		}

		public byte getThreshold() {
			return this.threshold;
		}

		public int getMinNeighbours() {
			return this.minNeighbours;
		}

		public int getMaxNeighbours() {
			return this.maxNeighbours;
		}

		public int getMinDistance() {
			return this.minDistance;
		}

		public int getMaxDistance() {
			return this.maxDistance;
		}

	}

	public static class IntegrationNegation implements IntegrationExpression {
		private IntegrationExpression atom = null;

		public IntegrationNegation(IntegrationExpression atom) {
			this.atom = atom;
		}

		public IntegrationExpression getNegatedExpression() {
			return this.atom;
		}
	}

	public static IntegrationExpression createNegation(
			IntegrationExpression atom) {
		return new IntegrationNegation(atom);
	}

	public static IntegrationExpression createAtom(String componentName,
			String thresholdString, String minString, String maxString) {

		byte threshold;
		if (thresholdString.equals("_"))
			threshold = -1;
		else
			threshold = (byte) Integer.parseInt(thresholdString);
		int min;
		if (minString.equals("_"))
			min = -1;
		else
			min = Integer.parseInt(minString);
		int max;
		if (maxString.equals("_"))
			max = -1;
		else
			max = Integer.parseInt(maxString);
		return new IntegrationAtom(componentName, threshold, min, max);
	}

	public static IntegrationExpression createAtom(String componentName,
			String thresholdString, String minString, String maxString,
			String distString) {

		byte threshold;
		int distance;
		int minDistance;
		int maxDistance;

		if (thresholdString.equals("_"))
			threshold = -1;
		else
			threshold = (byte) Integer.parseInt(thresholdString);
		int min;
		if (minString.equals("_"))
			min = -1;
		else
			min = Integer.parseInt(minString);
		int max;
		if (maxString.equals("_"))
			max = -1;
		else
			max = Integer.parseInt(maxString);

		if (distString.equals("_")) {
			minDistance = 1;
			maxDistance = 1;
		} else if (distString.contains(":")) {
			String rangeStr[] = new String[2];
			rangeStr = distString.split(":");

			int range[] = new int[2];
			range[0] = Integer.parseInt(rangeStr[0]);
			range[1] = Integer.parseInt(rangeStr[1]);

			if (range[0] <= range[1]) {
				minDistance = range[0];
				maxDistance = range[1];
			} else {
				minDistance = range[1];
				maxDistance = range[0];
			}

		} else {
			distance = Integer.parseInt(distString);
			minDistance = distance;
			maxDistance = distance;
		}

		return new IntegrationAtom(componentName, threshold, min, max,
				minDistance, maxDistance);
	}

	public static IntegrationExpression createAtom(
			IntegrationExpression expression) {
		return expression;
	}

	public static IntegrationExpression createConjunction(
			IntegrationExpression e1, IntegrationExpression e2) {
		return new IntegrationConjunction(e1, e2);
	}

	public static IntegrationExpression createDisjunction(
			IntegrationExpression e1, IntegrationExpression e2) {
		return new IntegrationDisjunction(e1, e2);
	}

	public IntegrationExpression parse(String specificationString)
			throws RecognitionException {

		specificationString.replaceAll("\\s", "");

		ANTLRStringStream in = new ANTLRStringStream(specificationString);
		IntegrationGrammarLexer lexer = new IntegrationGrammarLexer(in);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		IntegrationGrammarParser parser = new IntegrationGrammarParser(tokens);
		return parser.eval();
	}

}
