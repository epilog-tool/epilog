package org.epilogtool.integration;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.DefaultErrorStrategy;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.InputMismatchException;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.misc.ParseCancellationException;

import org.epilogtool.project.Project;

public class IFSpecification {

	public static CardinalityConstraint cardinalityConstraint(IntegrationSignalExpression expr, String minCells,
			String maxCells) throws RuntimeException {
		return new CardinalityConstraint(expr, minCells, maxCells);
	}

	public static IntegrationSignal integrationSignal(String name, String minThreshold, IntegrationDistance distance)
			throws RuntimeException {
		byte max = Project.getInstance().getProjectFeatures().getNodeMax(name);
		if (max == -1) { // NodeID semantic validation
			throw new RuntimeException("Invalid NodeID");
		}
		byte min = (byte) Integer.parseInt(minThreshold);
		if (min > max) { // Threshold semantic validation
			throw new RuntimeException("Invalid threshold");
		}
		return new IntegrationSignal(name, minThreshold, distance);
	}

	public static IntegrationSignalBinOpOR integrationSignalBinOpOR(IntegrationSignalExpression expr1,
			IntegrationSignalExpression expr2) {
		return new IntegrationSignalBinOpOR(expr1, expr2);
	}

	public static IntegrationSignalBinOpAND integrationSignalBinOpAND(IntegrationSignalExpression expr1,
			IntegrationSignalExpression expr2) {
		return new IntegrationSignalBinOpAND(expr1, expr2);
	}

	public static IntegrationFunctionNOT integrationFunctionNOT(IntegrationFunctionExpression expr) {
		return new IntegrationFunctionNOT(expr);
	}

	public static IntegrationFunctionBinOpAND integrationFunctionBinOpAND(IntegrationFunctionExpression expr1,
			IntegrationFunctionExpression expr2) {
		return new IntegrationFunctionBinOpAND(expr1, expr2);
	}

	public static IntegrationFunctionBinOpOR integrationFunctionBinOpOR(IntegrationFunctionExpression expr1,
			IntegrationFunctionExpression expr2) {
		return new IntegrationFunctionBinOpOR(expr1, expr2);
	}

	public static IntegrationFunctionTRUE integrationFunctionTRUE() {
		return new IntegrationFunctionTRUE();
	}

	public static IntegrationFunctionFALSE integrationFunctionFALSE() {
		return new IntegrationFunctionFALSE();
	}

	/**
	 * Function that translates the Integration Function (IF) from string to
	 * IntegrationFunctionExpression.
	 * 
	 * @param specificationString
	 * @return
	 * @throws RuntimeException
	 */
	public IntegrationFunctionExpression parse(String specificationString)
			throws RuntimeException {
		IntegrationFunctionExpression r = null;
		if (specificationString != null) {
//			specificationString.replaceAll("\\s", "");

			IntegrationGrammarLexer lexer = new IntegrationGrammarLexer(CharStreams.fromString(specificationString));
			CommonTokenStream tokens = new CommonTokenStream(lexer);
			IntegrationGrammarParser parser = new IntegrationGrammarParser(tokens);
			parser.setErrorHandler(
					new DefaultErrorStrategy()
				    {
				        /** Instead of recovering from exception {@code e}, re-throw it wrapped
				         *  in a {@link ParseCancellationException} so it is not caught by the
				         *  rule function catches.  Use {@link Exception#getCause()} to get the
				         *  original {@link RecognitionException}.
				         */
				        @Override
				        public void recover(Parser recognizer, RecognitionException e) {
				            for (ParserRuleContext context = recognizer.getContext(); context != null; context = context.getParent()) {
				                context.exception = e;
				            }

				            throw new ParseCancellationException(e);
				        }

				        /** Make sure we don't attempt to recover inline; if the parser
				         *  successfully recovers, it won't throw an exception.
				         */
				        @Override
				        public Token recoverInline(Parser recognizer)
				            throws RecognitionException
				        {
				            InputMismatchException e = new InputMismatchException(recognizer);
				            for (ParserRuleContext context = recognizer.getContext(); context != null; context = context.getParent()) {
				                context.exception = e;
				            }

				            throw new ParseCancellationException(e);
				        }
				    }
			);
			r = parser.eval().value;
		}
		return r;
	}
}
