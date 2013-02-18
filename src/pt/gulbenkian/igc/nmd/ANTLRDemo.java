package pt.gulbenkian.igc.nmd;
import org.antlr.runtime.*;

import composition.integrationgrammar.IntegrationGrammarLexer;
import composition.integrationgrammar.IntegrationGrammarParser;

public class ANTLRDemo {
	public static void main(String[] args) throws Exception {
		ANTLRStringStream in = new ANTLRStringStream("HB(4,3,4,1)");
		IntegrationGrammarLexer lexer = new IntegrationGrammarLexer(in);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		IntegrationGrammarParser parser = new IntegrationGrammarParser(tokens);
		parser.eval();
	}
}