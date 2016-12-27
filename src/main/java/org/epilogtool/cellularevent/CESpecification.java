package org.epilogtool.cellularevent;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;

public class CESpecification {

	public static CellularEventNode cellularEventNode(String nodeID, String value) {
		return new CellularEventNode(nodeID, value);
	}

	public static CellularEventNOT cellularEventNOT(CellularEventExpression expr) {
		return new CellularEventNOT(expr);
	}

	public static CellularEventOperationAND cellularEventOperationAND(CellularEventExpression expr1,
			CellularEventExpression expr2) {
		return new CellularEventOperationAND(expr1, expr2);
	}

	public static CellularEventOperationOR cellularEventOperationOR(CellularEventExpression expr1,
			CellularEventExpression expr2) {
		return new CellularEventOperationOR(expr1, expr2);
	}

	public CellularEventExpression parse(String specificationString) throws RecognitionException, RuntimeException {
		System.out.println("Specification string: " + specificationString);
		CellularEventExpression r = null;
		if (specificationString != null) {
			specificationString.replaceAll("\\s", "");

			ANTLRStringStream in = new ANTLRStringStream(specificationString);
			CEGrammarLexer lexer = new CEGrammarLexer(in);
			CommonTokenStream tokens = new CommonTokenStream(lexer);
			CEGrammarParser parser = new CEGrammarParser(tokens);
			r = parser.eval();
		}
		System.out.println("Specification eval: " + r);
		return r;
	}

}
