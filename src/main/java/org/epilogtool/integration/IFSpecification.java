package org.epilogtool.integration;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;

public class IFSpecification {

	public static CardinalityConstraint cardinalityConstraint(IntegrationSignalExpression expr, String minCells,
			String maxCells) {
		return new CardinalityConstraint(expr, minCells, maxCells);
	}

	public static IntegrationSignal integrationSignal(String name, String minThreshold, IntegrationDistance distance) {
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

	public IntegrationFunctionExpression parse(String specificationString) throws RecognitionException {
		System.out.println("String: " + specificationString);
		IntegrationFunctionExpression r = null;
		if (specificationString != null) {
			specificationString.replaceAll("\\s", "");

			ANTLRStringStream in = new ANTLRStringStream(specificationString);
			IntegrationGrammarLexer lexer = new IntegrationGrammarLexer(in);
			CommonTokenStream tokens = new CommonTokenStream(lexer);
			IntegrationGrammarParser parser = new IntegrationGrammarParser(tokens);
			r = parser.eval();
		}
		System.out.println("eval: " + r);
		return r;
	}

}
