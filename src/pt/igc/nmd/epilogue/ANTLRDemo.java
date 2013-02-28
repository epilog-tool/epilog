package pt.igc.nmd.epilogue;
import java.util.List;
import java.util.Map;

import org.antlr.runtime.*;
import org.colomoto.logicalmodel.NodeInfo;
import org.ginsim.servicegui.tool.composition.integrationgrammar.IntegrationFunctionSpecification.IntegrationExpression;

import composition.integrationgrammar.IntegrationGrammarLexer;
import composition.integrationgrammar.IntegrationGrammarParser;

public class ANTLRDemo {
	public static void Aeval(String[] args) throws Exception {
		//System.out.println(args);
		ANTLRStringStream in = new ANTLRStringStream();
		IntegrationGrammarLexer lexer = new IntegrationGrammarLexer(in);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		IntegrationGrammarParser parser = new IntegrationGrammarParser(tokens);
		parser.eval();
	}
	
	public static void teste(String args) throws Exception {
		System.out.println(args);
		ANTLRStringStream in = new ANTLRStringStream(args);
		IntegrationGrammarLexer lexer = new IntegrationGrammarLexer(in);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		IntegrationGrammarParser parser = new IntegrationGrammarParser(tokens);
		parser.eval();

	}
}