package org.epilogtool.integration;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.epilogtool.project.Project;

public class IFSpecification {

	public static CardinalityConstraint cardinalityConstraint(IntegrationSignalExpression expr, String minCells,
			String maxCells) throws RecognitionException {
		return new CardinalityConstraint(expr, minCells, maxCells);
	}

	public static IntegrationSignal integrationSignal(String name, String minThreshold, IntegrationDistance distance)
			throws RecognitionException {
		byte max = Project.getInstance().getProjectFeatures().getNodeMax(name);
		if (max == -1) { // NodeID semantic validation
			throw new RecognitionException();
		}
		byte min = (byte) Integer.parseInt(minThreshold);
		if (min > max) { // Threshold semantic validation
			throw new RecognitionException();
		}
		return new IntegrationSignal(name, minThreshold, distance);
	}

	public static IntegrationSignalOperationOR integrationSignalOperationOR(IntegrationSignalExpression expr1,
			IntegrationSignalExpression expr2) {
		return new IntegrationSignalOperationOR(expr1, expr2);
	}

	public static IntegrationFunctionNOT integrationFunctionNOT(IntegrationFunctionExpression expr) {
		return new IntegrationFunctionNOT(expr);
	}

	public static IntegrationFunctionOperationAND integrationFunctionOperationAND(IntegrationFunctionExpression expr1,
			IntegrationFunctionExpression expr2) {
		return new IntegrationFunctionOperationAND(expr1, expr2);
	}

	public static IntegrationFunctionOperationOR integrationFunctionOperationOR(IntegrationFunctionExpression expr1,
			IntegrationFunctionExpression expr2) {
		return new IntegrationFunctionOperationOR(expr1, expr2);
	}

	/**
	 * Function that translates the Integration Function (IF) from string to IntegrationFunctionExpression.
	 * 
	 * @param specificationString
	 * @return
	 * @throws RuntimeException
	 * @throws RecognitionException
	 */
	public IntegrationFunctionExpression parse(String specificationString)
			throws RuntimeException, RecognitionException {
		IntegrationFunctionExpression r = null;
		if (specificationString != null) {
			specificationString.replaceAll("\\s", "");

			ANTLRStringStream in = new ANTLRStringStream(specificationString);
			IntegrationGrammarLexer lexer = new IntegrationGrammarLexer(in);
			CommonTokenStream tokens = new CommonTokenStream(lexer);
			IntegrationGrammarParser parser = new IntegrationGrammarParser(tokens);
			r = parser.eval();
		}
		return r;
	}
}
