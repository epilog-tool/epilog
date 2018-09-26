package org.epilogtool.function;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.epilogtool.project.Project;

public class FSpecification {

	public static ComponentExpression functionComponent(String nodeID, String minThreshold) throws RecognitionException {
		byte max = Project.getInstance().getProjectFeatures().getNodeMax(nodeID);
		if (max == -1) { // NodeID semantic validation
			throw new RecognitionException();
		}
		byte min = (byte) Integer.parseInt(minThreshold);
		if (min > max) { // Threshold semantic validation
			throw new RecognitionException();
		}
		return new ComponentExpression(nodeID, min);
	}

	public static FunctionNOT functionNOT(FunctionExpression expr) {
		return new FunctionNOT(expr);
	}

	public static FunctionOperationAND functionOperationAND(FunctionExpression expr1, FunctionExpression expr2) {
		return new FunctionOperationAND(expr1, expr2);
	}

	public static FunctionOperationOR functionOperationOR(FunctionExpression expr1, FunctionExpression expr2) {
		return new FunctionOperationOR(expr1, expr2);
	}

	public static FunctionTRUE functionTRUE() {
		return new FunctionTRUE();
	}

	public static FunctionFALSE functionFALSE() {
		return new FunctionFALSE();
	}

	/**
	 * Method that translates the Function from string to FunctionExpression.
	 * 
	 * @param specificationString
	 * @return
	 * @throws RuntimeException
	 * @throws RecognitionException
	 */
	public static FunctionExpression parse(String specificationString) throws RuntimeException, RecognitionException {
		FunctionExpression r = null;
		if (specificationString != null) {
			specificationString.replaceAll("\\s", "");

			ANTLRStringStream in = new ANTLRStringStream(specificationString);
			FunctionGrammarLexer lexer = new FunctionGrammarLexer(in);
			CommonTokenStream tokens = new CommonTokenStream(lexer);
			FunctionGrammarParser parser = new FunctionGrammarParser(tokens);
			r = parser.eval();
		}
		return r;
	}
}
