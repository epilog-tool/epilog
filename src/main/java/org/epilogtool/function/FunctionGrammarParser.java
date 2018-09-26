// $ANTLR 3.5.2 ./src/main/java/org/epilogtool/function/FunctionGrammar.g 2018-09-26 15:19:19

package org.epilogtool.function;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings("all")
public class FunctionGrammarParser extends Parser {
	public static final String[] tokenNames = new String[] {
		"<invalid>", "<EOR>", "<DOWN>", "<UP>", "AND", "ID", "NOT", "NUMBER", 
		"OR", "SPACE", "WS", "'('", "')'", "':'", "'FALSE'", "'TRUE'"
	};
	public static final int EOF=-1;
	public static final int T__11=11;
	public static final int T__12=12;
	public static final int T__13=13;
	public static final int T__14=14;
	public static final int T__15=15;
	public static final int AND=4;
	public static final int ID=5;
	public static final int NOT=6;
	public static final int NUMBER=7;
	public static final int OR=8;
	public static final int SPACE=9;
	public static final int WS=10;

	// delegates
	public Parser[] getDelegates() {
		return new Parser[] {};
	}

	// delegators


	public FunctionGrammarParser(TokenStream input) {
		this(input, new RecognizerSharedState());
	}
	public FunctionGrammarParser(TokenStream input, RecognizerSharedState state) {
		super(input, state);
	}

	@Override public String[] getTokenNames() { return FunctionGrammarParser.tokenNames; }
	@Override public String getGrammarFileName() { return "./src/main/java/org/epilogtool/function/FunctionGrammar.g"; }


	  @Override
	  public void reportError(RecognitionException e) {
	    throw new RuntimeException("I quit!\n" + e.getMessage()); 
	  }



	// $ANTLR start "eval"
	// ./src/main/java/org/epilogtool/function/FunctionGrammar.g:24:1: eval returns [FunctionExpression value] : exp= functionexpror EOF ;
	public final FunctionExpression eval() throws RecognitionException {
		FunctionExpression value = null;


		FunctionExpression exp =null;

		try {
			// ./src/main/java/org/epilogtool/function/FunctionGrammar.g:25:2: (exp= functionexpror EOF )
			// ./src/main/java/org/epilogtool/function/FunctionGrammar.g:25:4: exp= functionexpror EOF
			{
			pushFollow(FOLLOW_functionexpror_in_eval46);
			exp=functionexpror();
			state._fsp--;

			match(input,EOF,FOLLOW_EOF_in_eval48); 
			 value = exp; 
			}

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return value;
	}
	// $ANTLR end "eval"



	// $ANTLR start "functionexpror"
	// ./src/main/java/org/epilogtool/function/FunctionGrammar.g:28:1: functionexpror returns [FunctionExpression value] : o1= functionexprand ( OR o2= functionexprand )* ;
	public final FunctionExpression functionexpror() throws RecognitionException {
		FunctionExpression value = null;


		FunctionExpression o1 =null;
		FunctionExpression o2 =null;

		try {
			// ./src/main/java/org/epilogtool/function/FunctionGrammar.g:29:2: (o1= functionexprand ( OR o2= functionexprand )* )
			// ./src/main/java/org/epilogtool/function/FunctionGrammar.g:29:4: o1= functionexprand ( OR o2= functionexprand )*
			{
			pushFollow(FOLLOW_functionexprand_in_functionexpror67);
			o1=functionexprand();
			state._fsp--;

			// ./src/main/java/org/epilogtool/function/FunctionGrammar.g:29:23: ( OR o2= functionexprand )*
			loop1:
			while (true) {
				int alt1=2;
				int LA1_0 = input.LA(1);
				if ( (LA1_0==OR) ) {
					alt1=1;
				}

				switch (alt1) {
				case 1 :
					// ./src/main/java/org/epilogtool/function/FunctionGrammar.g:29:24: OR o2= functionexprand
					{
					match(input,OR,FOLLOW_OR_in_functionexpror70); 
					pushFollow(FOLLOW_functionexprand_in_functionexpror74);
					o2=functionexprand();
					state._fsp--;

					}
					break;

				default :
					break loop1;
				}
			}

			 value = FSpecification.functionOperationOR(o1,o2); 
			}

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return value;
	}
	// $ANTLR end "functionexpror"



	// $ANTLR start "functionexprand"
	// ./src/main/java/org/epilogtool/function/FunctionGrammar.g:33:1: functionexprand returns [FunctionExpression value] : a1= functionexprnot ( AND a2= functionexprnot )* ;
	public final FunctionExpression functionexprand() throws RecognitionException {
		FunctionExpression value = null;


		FunctionExpression a1 =null;
		FunctionExpression a2 =null;

		try {
			// ./src/main/java/org/epilogtool/function/FunctionGrammar.g:34:2: (a1= functionexprnot ( AND a2= functionexprnot )* )
			// ./src/main/java/org/epilogtool/function/FunctionGrammar.g:34:4: a1= functionexprnot ( AND a2= functionexprnot )*
			{
			pushFollow(FOLLOW_functionexprnot_in_functionexprand98);
			a1=functionexprnot();
			state._fsp--;

			// ./src/main/java/org/epilogtool/function/FunctionGrammar.g:34:23: ( AND a2= functionexprnot )*
			loop2:
			while (true) {
				int alt2=2;
				int LA2_0 = input.LA(1);
				if ( (LA2_0==AND) ) {
					alt2=1;
				}

				switch (alt2) {
				case 1 :
					// ./src/main/java/org/epilogtool/function/FunctionGrammar.g:34:24: AND a2= functionexprnot
					{
					match(input,AND,FOLLOW_AND_in_functionexprand101); 
					pushFollow(FOLLOW_functionexprnot_in_functionexprand105);
					a2=functionexprnot();
					state._fsp--;

					}
					break;

				default :
					break loop2;
				}
			}

			 value = FSpecification.functionOperationAND(a1,a2); 
			}

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return value;
	}
	// $ANTLR end "functionexprand"



	// $ANTLR start "functionexprnot"
	// ./src/main/java/org/epilogtool/function/FunctionGrammar.g:38:1: functionexprnot returns [FunctionExpression value] : (expr= functionparen | NOT expr= functionparen );
	public final FunctionExpression functionexprnot() throws RecognitionException {
		FunctionExpression value = null;


		FunctionExpression expr =null;

		try {
			// ./src/main/java/org/epilogtool/function/FunctionGrammar.g:39:2: (expr= functionparen | NOT expr= functionparen )
			int alt3=2;
			int LA3_0 = input.LA(1);
			if ( (LA3_0==ID||LA3_0==11||(LA3_0 >= 14 && LA3_0 <= 15)) ) {
				alt3=1;
			}
			else if ( (LA3_0==NOT) ) {
				alt3=2;
			}

			else {
				NoViableAltException nvae =
					new NoViableAltException("", 3, 0, input);
				throw nvae;
			}

			switch (alt3) {
				case 1 :
					// ./src/main/java/org/epilogtool/function/FunctionGrammar.g:39:4: expr= functionparen
					{
					pushFollow(FOLLOW_functionparen_in_functionexprnot128);
					expr=functionparen();
					state._fsp--;

					 value = expr; 
					}
					break;
				case 2 :
					// ./src/main/java/org/epilogtool/function/FunctionGrammar.g:41:4: NOT expr= functionparen
					{
					match(input,NOT,FOLLOW_NOT_in_functionexprnot137); 
					pushFollow(FOLLOW_functionparen_in_functionexprnot141);
					expr=functionparen();
					state._fsp--;

					 value = FSpecification.functionNOT(expr); 
					}
					break;

			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return value;
	}
	// $ANTLR end "functionexprnot"



	// $ANTLR start "functionparen"
	// ./src/main/java/org/epilogtool/function/FunctionGrammar.g:45:1: functionparen returns [FunctionExpression value] : ( '(' expr= functionexpror ')' |expr= componentexpr | 'TRUE' | 'FALSE' );
	public final FunctionExpression functionparen() throws RecognitionException {
		FunctionExpression value = null;


		FunctionExpression expr =null;

		try {
			// ./src/main/java/org/epilogtool/function/FunctionGrammar.g:46:2: ( '(' expr= functionexpror ')' |expr= componentexpr | 'TRUE' | 'FALSE' )
			int alt4=4;
			switch ( input.LA(1) ) {
			case 11:
				{
				alt4=1;
				}
				break;
			case ID:
				{
				alt4=2;
				}
				break;
			case 15:
				{
				alt4=3;
				}
				break;
			case 14:
				{
				alt4=4;
				}
				break;
			default:
				NoViableAltException nvae =
					new NoViableAltException("", 4, 0, input);
				throw nvae;
			}
			switch (alt4) {
				case 1 :
					// ./src/main/java/org/epilogtool/function/FunctionGrammar.g:46:4: '(' expr= functionexpror ')'
					{
					match(input,11,FOLLOW_11_in_functionparen160); 
					pushFollow(FOLLOW_functionexpror_in_functionparen164);
					expr=functionexpror();
					state._fsp--;

					match(input,12,FOLLOW_12_in_functionparen166); 
					 value = expr; 
					}
					break;
				case 2 :
					// ./src/main/java/org/epilogtool/function/FunctionGrammar.g:48:4: expr= componentexpr
					{
					pushFollow(FOLLOW_componentexpr_in_functionparen177);
					expr=componentexpr();
					state._fsp--;

					 value = expr; 
					}
					break;
				case 3 :
					// ./src/main/java/org/epilogtool/function/FunctionGrammar.g:50:4: 'TRUE'
					{
					match(input,15,FOLLOW_15_in_functionparen186); 
					 value = FSpecification.functionTRUE(); 
					}
					break;
				case 4 :
					// ./src/main/java/org/epilogtool/function/FunctionGrammar.g:52:4: 'FALSE'
					{
					match(input,14,FOLLOW_14_in_functionparen195); 
					 value = FSpecification.functionFALSE(); 
					}
					break;

			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return value;
	}
	// $ANTLR end "functionparen"



	// $ANTLR start "componentexpr"
	// ./src/main/java/org/epilogtool/function/FunctionGrammar.g:56:1: componentexpr returns [FunctionExpression value ] : (id= ID |id= ID ':' t= NUMBER );
	public final FunctionExpression componentexpr() throws RecognitionException {
		FunctionExpression value = null;


		Token id=null;
		Token t=null;

		try {
			// ./src/main/java/org/epilogtool/function/FunctionGrammar.g:57:2: (id= ID |id= ID ':' t= NUMBER )
			int alt5=2;
			int LA5_0 = input.LA(1);
			if ( (LA5_0==ID) ) {
				int LA5_1 = input.LA(2);
				if ( (LA5_1==13) ) {
					alt5=2;
				}
				else if ( (LA5_1==EOF||LA5_1==AND||LA5_1==OR||LA5_1==12) ) {
					alt5=1;
				}

				else {
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 5, 1, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

			}

			else {
				NoViableAltException nvae =
					new NoViableAltException("", 5, 0, input);
				throw nvae;
			}

			switch (alt5) {
				case 1 :
					// ./src/main/java/org/epilogtool/function/FunctionGrammar.g:57:4: id= ID
					{
					id=(Token)match(input,ID,FOLLOW_ID_in_componentexpr216); 
					 value = FSpecification.functionComponent((id!=null?id.getText():null), "1"); 
					}
					break;
				case 2 :
					// ./src/main/java/org/epilogtool/function/FunctionGrammar.g:59:4: id= ID ':' t= NUMBER
					{
					id=(Token)match(input,ID,FOLLOW_ID_in_componentexpr227); 
					match(input,13,FOLLOW_13_in_componentexpr229); 
					t=(Token)match(input,NUMBER,FOLLOW_NUMBER_in_componentexpr233); 
					 value = FSpecification.functionComponent((id!=null?id.getText():null), (t!=null?t.getText():null)); 
					}
					break;

			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return value;
	}
	// $ANTLR end "componentexpr"

	// Delegated rules



	public static final BitSet FOLLOW_functionexpror_in_eval46 = new BitSet(new long[]{0x0000000000000000L});
	public static final BitSet FOLLOW_EOF_in_eval48 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_functionexprand_in_functionexpror67 = new BitSet(new long[]{0x0000000000000102L});
	public static final BitSet FOLLOW_OR_in_functionexpror70 = new BitSet(new long[]{0x000000000000C860L});
	public static final BitSet FOLLOW_functionexprand_in_functionexpror74 = new BitSet(new long[]{0x0000000000000102L});
	public static final BitSet FOLLOW_functionexprnot_in_functionexprand98 = new BitSet(new long[]{0x0000000000000012L});
	public static final BitSet FOLLOW_AND_in_functionexprand101 = new BitSet(new long[]{0x000000000000C860L});
	public static final BitSet FOLLOW_functionexprnot_in_functionexprand105 = new BitSet(new long[]{0x0000000000000012L});
	public static final BitSet FOLLOW_functionparen_in_functionexprnot128 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_NOT_in_functionexprnot137 = new BitSet(new long[]{0x000000000000C820L});
	public static final BitSet FOLLOW_functionparen_in_functionexprnot141 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_11_in_functionparen160 = new BitSet(new long[]{0x000000000000C860L});
	public static final BitSet FOLLOW_functionexpror_in_functionparen164 = new BitSet(new long[]{0x0000000000001000L});
	public static final BitSet FOLLOW_12_in_functionparen166 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_componentexpr_in_functionparen177 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_15_in_functionparen186 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_14_in_functionparen195 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_ID_in_componentexpr216 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_ID_in_componentexpr227 = new BitSet(new long[]{0x0000000000002000L});
	public static final BitSet FOLLOW_13_in_componentexpr229 = new BitSet(new long[]{0x0000000000000080L});
	public static final BitSet FOLLOW_NUMBER_in_componentexpr233 = new BitSet(new long[]{0x0000000000000002L});
}
