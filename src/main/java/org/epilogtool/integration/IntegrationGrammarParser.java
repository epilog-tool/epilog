// $ANTLR 3.5.2 src/main/java/org/epilogtool/integration/IntegrationGrammar.g 2018-04-27 16:02:03

package org.epilogtool.integration;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings("all")
public class IntegrationGrammarParser extends Parser {
	public static final String[] tokenNames = new String[] {
		"<invalid>", "<EOR>", "<DOWN>", "<UP>", "AND", "ID", "NOT", "NUMBER", 
		"OR", "SPACE", "WS", "'('", "')'", "','", "':'", "'='", "'FALSE'", "'TRUE'", 
		"'['", "']'", "'max'", "'min'", "'{'", "'}'"
	};
	public static final int EOF=-1;
	public static final int T__11=11;
	public static final int T__12=12;
	public static final int T__13=13;
	public static final int T__14=14;
	public static final int T__15=15;
	public static final int T__16=16;
	public static final int T__17=17;
	public static final int T__18=18;
	public static final int T__19=19;
	public static final int T__20=20;
	public static final int T__21=21;
	public static final int T__22=22;
	public static final int T__23=23;
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


	public IntegrationGrammarParser(TokenStream input) {
		this(input, new RecognizerSharedState());
	}
	public IntegrationGrammarParser(TokenStream input, RecognizerSharedState state) {
		super(input, state);
	}

	@Override public String[] getTokenNames() { return IntegrationGrammarParser.tokenNames; }
	@Override public String getGrammarFileName() { return "src/main/java/org/epilogtool/integration/IntegrationGrammar.g"; }


	  @Override
	  public void reportError(RecognitionException e) {
	    throw new RuntimeException("I quit!\n" + e.getMessage()); 
	  }



	// $ANTLR start "eval"
	// src/main/java/org/epilogtool/integration/IntegrationGrammar.g:24:1: eval returns [IntegrationFunctionExpression value] : exp= functionexpror EOF ;
	public final IntegrationFunctionExpression eval() throws RecognitionException {
		IntegrationFunctionExpression value = null;


		IntegrationFunctionExpression exp =null;

		try {
			// src/main/java/org/epilogtool/integration/IntegrationGrammar.g:25:2: (exp= functionexpror EOF )
			// src/main/java/org/epilogtool/integration/IntegrationGrammar.g:25:4: exp= functionexpror EOF
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
	// src/main/java/org/epilogtool/integration/IntegrationGrammar.g:28:1: functionexpror returns [IntegrationFunctionExpression value] : o1= functionexprand ( OR o2= functionexprand )* ;
	public final IntegrationFunctionExpression functionexpror() throws RecognitionException {
		IntegrationFunctionExpression value = null;


		IntegrationFunctionExpression o1 =null;
		IntegrationFunctionExpression o2 =null;

		try {
			// src/main/java/org/epilogtool/integration/IntegrationGrammar.g:29:2: (o1= functionexprand ( OR o2= functionexprand )* )
			// src/main/java/org/epilogtool/integration/IntegrationGrammar.g:29:4: o1= functionexprand ( OR o2= functionexprand )*
			{
			pushFollow(FOLLOW_functionexprand_in_functionexpror67);
			o1=functionexprand();
			state._fsp--;

			// src/main/java/org/epilogtool/integration/IntegrationGrammar.g:29:23: ( OR o2= functionexprand )*
			loop1:
			while (true) {
				int alt1=2;
				int LA1_0 = input.LA(1);
				if ( (LA1_0==OR) ) {
					alt1=1;
				}

				switch (alt1) {
				case 1 :
					// src/main/java/org/epilogtool/integration/IntegrationGrammar.g:29:24: OR o2= functionexprand
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

			 value = IFSpecification.integrationFunctionOperationOR(o1,o2); 
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
	// src/main/java/org/epilogtool/integration/IntegrationGrammar.g:33:1: functionexprand returns [IntegrationFunctionExpression value] : a1= functionexprnot ( AND a2= functionexprnot )* ;
	public final IntegrationFunctionExpression functionexprand() throws RecognitionException {
		IntegrationFunctionExpression value = null;


		IntegrationFunctionExpression a1 =null;
		IntegrationFunctionExpression a2 =null;

		try {
			// src/main/java/org/epilogtool/integration/IntegrationGrammar.g:34:2: (a1= functionexprnot ( AND a2= functionexprnot )* )
			// src/main/java/org/epilogtool/integration/IntegrationGrammar.g:34:4: a1= functionexprnot ( AND a2= functionexprnot )*
			{
			pushFollow(FOLLOW_functionexprnot_in_functionexprand98);
			a1=functionexprnot();
			state._fsp--;

			// src/main/java/org/epilogtool/integration/IntegrationGrammar.g:34:23: ( AND a2= functionexprnot )*
			loop2:
			while (true) {
				int alt2=2;
				int LA2_0 = input.LA(1);
				if ( (LA2_0==AND) ) {
					alt2=1;
				}

				switch (alt2) {
				case 1 :
					// src/main/java/org/epilogtool/integration/IntegrationGrammar.g:34:24: AND a2= functionexprnot
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

			 value = IFSpecification.integrationFunctionOperationAND(a1,a2); 
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
	// src/main/java/org/epilogtool/integration/IntegrationGrammar.g:38:1: functionexprnot returns [IntegrationFunctionExpression value] : (expr= functionparen | NOT expr= functionparen );
	public final IntegrationFunctionExpression functionexprnot() throws RecognitionException {
		IntegrationFunctionExpression value = null;


		IntegrationFunctionExpression expr =null;

		try {
			// src/main/java/org/epilogtool/integration/IntegrationGrammar.g:39:2: (expr= functionparen | NOT expr= functionparen )
			int alt3=2;
			int LA3_0 = input.LA(1);
			if ( (LA3_0==11||(LA3_0 >= 16 && LA3_0 <= 17)||LA3_0==22) ) {
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
					// src/main/java/org/epilogtool/integration/IntegrationGrammar.g:39:4: expr= functionparen
					{
					pushFollow(FOLLOW_functionparen_in_functionexprnot128);
					expr=functionparen();
					state._fsp--;

					 value = expr; 
					}
					break;
				case 2 :
					// src/main/java/org/epilogtool/integration/IntegrationGrammar.g:41:4: NOT expr= functionparen
					{
					match(input,NOT,FOLLOW_NOT_in_functionexprnot137); 
					pushFollow(FOLLOW_functionparen_in_functionexprnot141);
					expr=functionparen();
					state._fsp--;

					 value = IFSpecification.integrationFunctionNOT(expr); 
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
	// src/main/java/org/epilogtool/integration/IntegrationGrammar.g:45:1: functionparen returns [IntegrationFunctionExpression value] : ( '(' expr= functionexpror ')' |expr= cardconst | 'TRUE' | 'FALSE' );
	public final IntegrationFunctionExpression functionparen() throws RecognitionException {
		IntegrationFunctionExpression value = null;


		IntegrationFunctionExpression expr =null;

		try {
			// src/main/java/org/epilogtool/integration/IntegrationGrammar.g:46:2: ( '(' expr= functionexpror ')' |expr= cardconst | 'TRUE' | 'FALSE' )
			int alt4=4;
			switch ( input.LA(1) ) {
			case 11:
				{
				alt4=1;
				}
				break;
			case 22:
				{
				alt4=2;
				}
				break;
			case 17:
				{
				alt4=3;
				}
				break;
			case 16:
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
					// src/main/java/org/epilogtool/integration/IntegrationGrammar.g:46:4: '(' expr= functionexpror ')'
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
					// src/main/java/org/epilogtool/integration/IntegrationGrammar.g:48:4: expr= cardconst
					{
					pushFollow(FOLLOW_cardconst_in_functionparen177);
					expr=cardconst();
					state._fsp--;

					 value = expr; 
					}
					break;
				case 3 :
					// src/main/java/org/epilogtool/integration/IntegrationGrammar.g:50:4: 'TRUE'
					{
					match(input,17,FOLLOW_17_in_functionparen186); 
					 value = IFSpecification.integrationFunctionTRUE(); 
					}
					break;
				case 4 :
					// src/main/java/org/epilogtool/integration/IntegrationGrammar.g:52:4: 'FALSE'
					{
					match(input,16,FOLLOW_16_in_functionparen195); 
					 value = IFSpecification.integrationFunctionFALSE(); 
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



	// $ANTLR start "cardconst"
	// src/main/java/org/epilogtool/integration/IntegrationGrammar.g:56:1: cardconst returns [IntegrationFunctionExpression value ] : ( '{' expr= signalexpror '}' | '{' expr= signalexpror ',' 'min' '=' min= NUMBER '}' | '{' expr= signalexpror ',' 'max' '=' max= NUMBER '}' | '{' expr= signalexpror ',' 'min' '=' min= NUMBER ',' 'max' '=' max= NUMBER '}' );
	public final IntegrationFunctionExpression cardconst() throws RecognitionException {
		IntegrationFunctionExpression value = null;


		Token min=null;
		Token max=null;
		IntegrationSignalExpression expr =null;

		try {
			// src/main/java/org/epilogtool/integration/IntegrationGrammar.g:57:2: ( '{' expr= signalexpror '}' | '{' expr= signalexpror ',' 'min' '=' min= NUMBER '}' | '{' expr= signalexpror ',' 'max' '=' max= NUMBER '}' | '{' expr= signalexpror ',' 'min' '=' min= NUMBER ',' 'max' '=' max= NUMBER '}' )
			int alt5=4;
			alt5 = dfa5.predict(input);
			switch (alt5) {
				case 1 :
					// src/main/java/org/epilogtool/integration/IntegrationGrammar.g:57:4: '{' expr= signalexpror '}'
					{
					match(input,22,FOLLOW_22_in_cardconst214); 
					pushFollow(FOLLOW_signalexpror_in_cardconst218);
					expr=signalexpror();
					state._fsp--;

					match(input,23,FOLLOW_23_in_cardconst220); 
					 value = IFSpecification.cardinalityConstraint(expr, "1", "-1"); 
					}
					break;
				case 2 :
					// src/main/java/org/epilogtool/integration/IntegrationGrammar.g:59:4: '{' expr= signalexpror ',' 'min' '=' min= NUMBER '}'
					{
					match(input,22,FOLLOW_22_in_cardconst229); 
					pushFollow(FOLLOW_signalexpror_in_cardconst233);
					expr=signalexpror();
					state._fsp--;

					match(input,13,FOLLOW_13_in_cardconst235); 
					match(input,21,FOLLOW_21_in_cardconst237); 
					match(input,15,FOLLOW_15_in_cardconst239); 
					min=(Token)match(input,NUMBER,FOLLOW_NUMBER_in_cardconst243); 
					match(input,23,FOLLOW_23_in_cardconst245); 
					 value = IFSpecification.cardinalityConstraint(expr, (min!=null?min.getText():null), "-1"); 
					}
					break;
				case 3 :
					// src/main/java/org/epilogtool/integration/IntegrationGrammar.g:61:4: '{' expr= signalexpror ',' 'max' '=' max= NUMBER '}'
					{
					match(input,22,FOLLOW_22_in_cardconst254); 
					pushFollow(FOLLOW_signalexpror_in_cardconst258);
					expr=signalexpror();
					state._fsp--;

					match(input,13,FOLLOW_13_in_cardconst260); 
					match(input,20,FOLLOW_20_in_cardconst262); 
					match(input,15,FOLLOW_15_in_cardconst264); 
					max=(Token)match(input,NUMBER,FOLLOW_NUMBER_in_cardconst268); 
					match(input,23,FOLLOW_23_in_cardconst270); 
					 value = IFSpecification.cardinalityConstraint(expr, "-1", (max!=null?max.getText():null)); 
					}
					break;
				case 4 :
					// src/main/java/org/epilogtool/integration/IntegrationGrammar.g:63:4: '{' expr= signalexpror ',' 'min' '=' min= NUMBER ',' 'max' '=' max= NUMBER '}'
					{
					match(input,22,FOLLOW_22_in_cardconst279); 
					pushFollow(FOLLOW_signalexpror_in_cardconst283);
					expr=signalexpror();
					state._fsp--;

					match(input,13,FOLLOW_13_in_cardconst285); 
					match(input,21,FOLLOW_21_in_cardconst287); 
					match(input,15,FOLLOW_15_in_cardconst289); 
					min=(Token)match(input,NUMBER,FOLLOW_NUMBER_in_cardconst293); 
					match(input,13,FOLLOW_13_in_cardconst295); 
					match(input,20,FOLLOW_20_in_cardconst297); 
					match(input,15,FOLLOW_15_in_cardconst299); 
					max=(Token)match(input,NUMBER,FOLLOW_NUMBER_in_cardconst303); 
					match(input,23,FOLLOW_23_in_cardconst305); 
					 value = IFSpecification.cardinalityConstraint(expr, (min!=null?min.getText():null), (max!=null?max.getText():null)); 
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
	// $ANTLR end "cardconst"



	// $ANTLR start "signalexpror"
	// src/main/java/org/epilogtool/integration/IntegrationGrammar.g:67:1: signalexpror returns [IntegrationSignalExpression value] : o1= signal ( OR o2= signal )* ;
	public final IntegrationSignalExpression signalexpror() throws RecognitionException {
		IntegrationSignalExpression value = null;


		IntegrationSignal o1 =null;
		IntegrationSignal o2 =null;

		try {
			// src/main/java/org/epilogtool/integration/IntegrationGrammar.g:68:2: (o1= signal ( OR o2= signal )* )
			// src/main/java/org/epilogtool/integration/IntegrationGrammar.g:68:4: o1= signal ( OR o2= signal )*
			{
			pushFollow(FOLLOW_signal_in_signalexpror326);
			o1=signal();
			state._fsp--;

			// src/main/java/org/epilogtool/integration/IntegrationGrammar.g:68:14: ( OR o2= signal )*
			loop6:
			while (true) {
				int alt6=2;
				int LA6_0 = input.LA(1);
				if ( (LA6_0==OR) ) {
					alt6=1;
				}

				switch (alt6) {
				case 1 :
					// src/main/java/org/epilogtool/integration/IntegrationGrammar.g:68:15: OR o2= signal
					{
					match(input,OR,FOLLOW_OR_in_signalexpror329); 
					pushFollow(FOLLOW_signal_in_signalexpror333);
					o2=signal();
					state._fsp--;

					}
					break;

				default :
					break loop6;
				}
			}

			 value = IFSpecification.integrationSignalOperationOR(o1,o2); 
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
	// $ANTLR end "signalexpror"



	// $ANTLR start "signal"
	// src/main/java/org/epilogtool/integration/IntegrationGrammar.g:72:1: signal returns [IntegrationSignal value] : (id= ID |id= ID '[' min= NUMBER ']' |id= ID '[' min= NUMBER ':' ']' |id= ID '[' min= NUMBER ':' max= NUMBER ']' |id= ID '[' ':' max= NUMBER ']' |id= ID ':' t= NUMBER |id= ID ':' t= NUMBER '[' min= NUMBER ']' |id= ID ':' t= NUMBER '[' min= NUMBER ':' ']' |id= ID ':' t= NUMBER '[' min= NUMBER ':' max= NUMBER ']' |id= ID ':' t= NUMBER '[' ':' max= NUMBER ']' );
	public final IntegrationSignal signal() throws RecognitionException {
		IntegrationSignal value = null;


		Token id=null;
		Token min=null;
		Token max=null;
		Token t=null;

		try {
			// src/main/java/org/epilogtool/integration/IntegrationGrammar.g:73:2: (id= ID |id= ID '[' min= NUMBER ']' |id= ID '[' min= NUMBER ':' ']' |id= ID '[' min= NUMBER ':' max= NUMBER ']' |id= ID '[' ':' max= NUMBER ']' |id= ID ':' t= NUMBER |id= ID ':' t= NUMBER '[' min= NUMBER ']' |id= ID ':' t= NUMBER '[' min= NUMBER ':' ']' |id= ID ':' t= NUMBER '[' min= NUMBER ':' max= NUMBER ']' |id= ID ':' t= NUMBER '[' ':' max= NUMBER ']' )
			int alt7=10;
			int LA7_0 = input.LA(1);
			if ( (LA7_0==ID) ) {
				switch ( input.LA(2) ) {
				case 18:
					{
					int LA7_2 = input.LA(3);
					if ( (LA7_2==NUMBER) ) {
						int LA7_5 = input.LA(4);
						if ( (LA7_5==19) ) {
							alt7=2;
						}
						else if ( (LA7_5==14) ) {
							int LA7_9 = input.LA(5);
							if ( (LA7_9==19) ) {
								alt7=3;
							}
							else if ( (LA7_9==NUMBER) ) {
								alt7=4;
							}

							else {
								int nvaeMark = input.mark();
								try {
									for (int nvaeConsume = 0; nvaeConsume < 5 - 1; nvaeConsume++) {
										input.consume();
									}
									NoViableAltException nvae =
										new NoViableAltException("", 7, 9, input);
									throw nvae;
								} finally {
									input.rewind(nvaeMark);
								}
							}

						}

						else {
							int nvaeMark = input.mark();
							try {
								for (int nvaeConsume = 0; nvaeConsume < 4 - 1; nvaeConsume++) {
									input.consume();
								}
								NoViableAltException nvae =
									new NoViableAltException("", 7, 5, input);
								throw nvae;
							} finally {
								input.rewind(nvaeMark);
							}
						}

					}
					else if ( (LA7_2==14) ) {
						alt7=5;
					}

					else {
						int nvaeMark = input.mark();
						try {
							for (int nvaeConsume = 0; nvaeConsume < 3 - 1; nvaeConsume++) {
								input.consume();
							}
							NoViableAltException nvae =
								new NoViableAltException("", 7, 2, input);
							throw nvae;
						} finally {
							input.rewind(nvaeMark);
						}
					}

					}
					break;
				case 14:
					{
					int LA7_3 = input.LA(3);
					if ( (LA7_3==NUMBER) ) {
						int LA7_7 = input.LA(4);
						if ( (LA7_7==18) ) {
							int LA7_10 = input.LA(5);
							if ( (LA7_10==NUMBER) ) {
								int LA7_14 = input.LA(6);
								if ( (LA7_14==19) ) {
									alt7=7;
								}
								else if ( (LA7_14==14) ) {
									int LA7_17 = input.LA(7);
									if ( (LA7_17==19) ) {
										alt7=8;
									}
									else if ( (LA7_17==NUMBER) ) {
										alt7=9;
									}

									else {
										int nvaeMark = input.mark();
										try {
											for (int nvaeConsume = 0; nvaeConsume < 7 - 1; nvaeConsume++) {
												input.consume();
											}
											NoViableAltException nvae =
												new NoViableAltException("", 7, 17, input);
											throw nvae;
										} finally {
											input.rewind(nvaeMark);
										}
									}

								}

								else {
									int nvaeMark = input.mark();
									try {
										for (int nvaeConsume = 0; nvaeConsume < 6 - 1; nvaeConsume++) {
											input.consume();
										}
										NoViableAltException nvae =
											new NoViableAltException("", 7, 14, input);
										throw nvae;
									} finally {
										input.rewind(nvaeMark);
									}
								}

							}
							else if ( (LA7_10==14) ) {
								alt7=10;
							}

							else {
								int nvaeMark = input.mark();
								try {
									for (int nvaeConsume = 0; nvaeConsume < 5 - 1; nvaeConsume++) {
										input.consume();
									}
									NoViableAltException nvae =
										new NoViableAltException("", 7, 10, input);
									throw nvae;
								} finally {
									input.rewind(nvaeMark);
								}
							}

						}
						else if ( (LA7_7==OR||LA7_7==13||LA7_7==23) ) {
							alt7=6;
						}

						else {
							int nvaeMark = input.mark();
							try {
								for (int nvaeConsume = 0; nvaeConsume < 4 - 1; nvaeConsume++) {
									input.consume();
								}
								NoViableAltException nvae =
									new NoViableAltException("", 7, 7, input);
								throw nvae;
							} finally {
								input.rewind(nvaeMark);
							}
						}

					}

					else {
						int nvaeMark = input.mark();
						try {
							for (int nvaeConsume = 0; nvaeConsume < 3 - 1; nvaeConsume++) {
								input.consume();
							}
							NoViableAltException nvae =
								new NoViableAltException("", 7, 3, input);
							throw nvae;
						} finally {
							input.rewind(nvaeMark);
						}
					}

					}
					break;
				case OR:
				case 13:
				case 23:
					{
					alt7=1;
					}
					break;
				default:
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 7, 1, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}
			}

			else {
				NoViableAltException nvae =
					new NoViableAltException("", 7, 0, input);
				throw nvae;
			}

			switch (alt7) {
				case 1 :
					// src/main/java/org/epilogtool/integration/IntegrationGrammar.g:73:4: id= ID
					{
					id=(Token)match(input,ID,FOLLOW_ID_in_signal357); 
					 value = IFSpecification.integrationSignal((id!=null?id.getText():null), "1", new IntegrationDistance("1", "1")); 
					}
					break;
				case 2 :
					// src/main/java/org/epilogtool/integration/IntegrationGrammar.g:75:4: id= ID '[' min= NUMBER ']'
					{
					id=(Token)match(input,ID,FOLLOW_ID_in_signal368); 
					match(input,18,FOLLOW_18_in_signal370); 
					min=(Token)match(input,NUMBER,FOLLOW_NUMBER_in_signal374); 
					match(input,19,FOLLOW_19_in_signal376); 
					 value = IFSpecification.integrationSignal((id!=null?id.getText():null), "1", new IntegrationDistance((min!=null?min.getText():null), (min!=null?min.getText():null))); 
					}
					break;
				case 3 :
					// src/main/java/org/epilogtool/integration/IntegrationGrammar.g:77:4: id= ID '[' min= NUMBER ':' ']'
					{
					id=(Token)match(input,ID,FOLLOW_ID_in_signal387); 
					match(input,18,FOLLOW_18_in_signal389); 
					min=(Token)match(input,NUMBER,FOLLOW_NUMBER_in_signal393); 
					match(input,14,FOLLOW_14_in_signal395); 
					match(input,19,FOLLOW_19_in_signal397); 
					 value = IFSpecification.integrationSignal((id!=null?id.getText():null), "1", new IntegrationDistance((min!=null?min.getText():null), "-1")); 
					}
					break;
				case 4 :
					// src/main/java/org/epilogtool/integration/IntegrationGrammar.g:79:4: id= ID '[' min= NUMBER ':' max= NUMBER ']'
					{
					id=(Token)match(input,ID,FOLLOW_ID_in_signal408); 
					match(input,18,FOLLOW_18_in_signal410); 
					min=(Token)match(input,NUMBER,FOLLOW_NUMBER_in_signal414); 
					match(input,14,FOLLOW_14_in_signal416); 
					max=(Token)match(input,NUMBER,FOLLOW_NUMBER_in_signal420); 
					match(input,19,FOLLOW_19_in_signal422); 
					 value = IFSpecification.integrationSignal((id!=null?id.getText():null), "1", new IntegrationDistance((min!=null?min.getText():null), (max!=null?max.getText():null))); 
					}
					break;
				case 5 :
					// src/main/java/org/epilogtool/integration/IntegrationGrammar.g:81:4: id= ID '[' ':' max= NUMBER ']'
					{
					id=(Token)match(input,ID,FOLLOW_ID_in_signal433); 
					match(input,18,FOLLOW_18_in_signal435); 
					match(input,14,FOLLOW_14_in_signal437); 
					max=(Token)match(input,NUMBER,FOLLOW_NUMBER_in_signal441); 
					match(input,19,FOLLOW_19_in_signal443); 
					 value = IFSpecification.integrationSignal((id!=null?id.getText():null), "1", new IntegrationDistance("-1", (max!=null?max.getText():null))); 
					}
					break;
				case 6 :
					// src/main/java/org/epilogtool/integration/IntegrationGrammar.g:83:4: id= ID ':' t= NUMBER
					{
					id=(Token)match(input,ID,FOLLOW_ID_in_signal454); 
					match(input,14,FOLLOW_14_in_signal456); 
					t=(Token)match(input,NUMBER,FOLLOW_NUMBER_in_signal460); 
					 value = IFSpecification.integrationSignal((id!=null?id.getText():null), (t!=null?t.getText():null), new IntegrationDistance("1", "1")); 
					}
					break;
				case 7 :
					// src/main/java/org/epilogtool/integration/IntegrationGrammar.g:85:4: id= ID ':' t= NUMBER '[' min= NUMBER ']'
					{
					id=(Token)match(input,ID,FOLLOW_ID_in_signal471); 
					match(input,14,FOLLOW_14_in_signal473); 
					t=(Token)match(input,NUMBER,FOLLOW_NUMBER_in_signal477); 
					match(input,18,FOLLOW_18_in_signal479); 
					min=(Token)match(input,NUMBER,FOLLOW_NUMBER_in_signal483); 
					match(input,19,FOLLOW_19_in_signal485); 
					 value = IFSpecification.integrationSignal((id!=null?id.getText():null), (t!=null?t.getText():null), new IntegrationDistance((min!=null?min.getText():null), (min!=null?min.getText():null))); 
					}
					break;
				case 8 :
					// src/main/java/org/epilogtool/integration/IntegrationGrammar.g:87:4: id= ID ':' t= NUMBER '[' min= NUMBER ':' ']'
					{
					id=(Token)match(input,ID,FOLLOW_ID_in_signal496); 
					match(input,14,FOLLOW_14_in_signal498); 
					t=(Token)match(input,NUMBER,FOLLOW_NUMBER_in_signal502); 
					match(input,18,FOLLOW_18_in_signal504); 
					min=(Token)match(input,NUMBER,FOLLOW_NUMBER_in_signal508); 
					match(input,14,FOLLOW_14_in_signal510); 
					match(input,19,FOLLOW_19_in_signal512); 
					 value = IFSpecification.integrationSignal((id!=null?id.getText():null), (t!=null?t.getText():null), new IntegrationDistance((min!=null?min.getText():null), "-1")); 
					}
					break;
				case 9 :
					// src/main/java/org/epilogtool/integration/IntegrationGrammar.g:89:4: id= ID ':' t= NUMBER '[' min= NUMBER ':' max= NUMBER ']'
					{
					id=(Token)match(input,ID,FOLLOW_ID_in_signal523); 
					match(input,14,FOLLOW_14_in_signal525); 
					t=(Token)match(input,NUMBER,FOLLOW_NUMBER_in_signal529); 
					match(input,18,FOLLOW_18_in_signal531); 
					min=(Token)match(input,NUMBER,FOLLOW_NUMBER_in_signal535); 
					match(input,14,FOLLOW_14_in_signal537); 
					max=(Token)match(input,NUMBER,FOLLOW_NUMBER_in_signal541); 
					match(input,19,FOLLOW_19_in_signal543); 
					 value = IFSpecification.integrationSignal((id!=null?id.getText():null), (t!=null?t.getText():null), new IntegrationDistance((min!=null?min.getText():null), (max!=null?max.getText():null))); 
					}
					break;
				case 10 :
					// src/main/java/org/epilogtool/integration/IntegrationGrammar.g:91:4: id= ID ':' t= NUMBER '[' ':' max= NUMBER ']'
					{
					id=(Token)match(input,ID,FOLLOW_ID_in_signal554); 
					match(input,14,FOLLOW_14_in_signal556); 
					t=(Token)match(input,NUMBER,FOLLOW_NUMBER_in_signal560); 
					match(input,18,FOLLOW_18_in_signal562); 
					match(input,14,FOLLOW_14_in_signal564); 
					max=(Token)match(input,NUMBER,FOLLOW_NUMBER_in_signal568); 
					match(input,19,FOLLOW_19_in_signal570); 
					 value = IFSpecification.integrationSignal((id!=null?id.getText():null), (t!=null?t.getText():null), new IntegrationDistance("-1", (max!=null?max.getText():null))); 
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
	// $ANTLR end "signal"

	// Delegated rules


	protected DFA5 dfa5 = new DFA5(this);
	static final String DFA5_eotS =
		"\71\uffff";
	static final String DFA5_eofS =
		"\71\uffff";
	static final String DFA5_minS =
		"\1\26\1\5\1\10\2\7\1\5\1\uffff\1\24\1\16\1\7\2\10\1\17\1\uffff\1\10\1"+
		"\7\1\23\4\7\1\10\1\23\1\10\1\16\1\7\1\16\1\7\1\10\1\15\2\10\1\7\1\23\1"+
		"\10\1\7\1\23\1\7\2\uffff\1\10\1\23\2\10\1\23\1\10\1\16\1\7\3\10\1\7\1"+
		"\23\1\10\1\23\2\10";
	static final String DFA5_maxS =
		"\1\26\1\5\1\27\1\16\1\7\1\5\1\uffff\1\25\1\23\1\7\2\27\1\17\1\uffff\1"+
		"\27\2\23\2\16\2\7\1\27\1\23\1\27\1\23\1\7\1\23\1\7\4\27\2\23\1\27\2\23"+
		"\1\16\2\uffff\1\27\1\23\2\27\1\23\1\27\1\23\1\7\3\27\2\23\1\27\1\23\2"+
		"\27";
	static final String DFA5_acceptS =
		"\6\uffff\1\1\6\uffff\1\3\30\uffff\1\2\1\4\21\uffff";
	static final String DFA5_specialS =
		"\71\uffff}>";
	static final String[] DFA5_transitionS = {
			"\1\1",
			"\1\2",
			"\1\5\4\uffff\1\7\1\4\3\uffff\1\3\4\uffff\1\6",
			"\1\10\6\uffff\1\11",
			"\1\12",
			"\1\13",
			"",
			"\1\15\1\14",
			"\1\17\4\uffff\1\16",
			"\1\20",
			"\1\5\4\uffff\1\7\4\uffff\1\21\4\uffff\1\6",
			"\1\5\4\uffff\1\7\1\23\3\uffff\1\22\4\uffff\1\6",
			"\1\24",
			"",
			"\1\5\4\uffff\1\7\11\uffff\1\6",
			"\1\26\13\uffff\1\25",
			"\1\27",
			"\1\30\6\uffff\1\31",
			"\1\32\6\uffff\1\33",
			"\1\34",
			"\1\35",
			"\1\5\4\uffff\1\7\11\uffff\1\6",
			"\1\36",
			"\1\5\4\uffff\1\7\11\uffff\1\6",
			"\1\40\4\uffff\1\37",
			"\1\41",
			"\1\43\4\uffff\1\42",
			"\1\44",
			"\1\5\4\uffff\1\7\4\uffff\1\45\4\uffff\1\6",
			"\1\47\11\uffff\1\46",
			"\1\5\4\uffff\1\7\11\uffff\1\6",
			"\1\5\4\uffff\1\7\11\uffff\1\6",
			"\1\51\13\uffff\1\50",
			"\1\52",
			"\1\5\4\uffff\1\7\11\uffff\1\6",
			"\1\54\13\uffff\1\53",
			"\1\55",
			"\1\56\6\uffff\1\57",
			"",
			"",
			"\1\5\4\uffff\1\7\11\uffff\1\6",
			"\1\60",
			"\1\5\4\uffff\1\7\11\uffff\1\6",
			"\1\5\4\uffff\1\7\11\uffff\1\6",
			"\1\61",
			"\1\5\4\uffff\1\7\11\uffff\1\6",
			"\1\63\4\uffff\1\62",
			"\1\64",
			"\1\5\4\uffff\1\7\11\uffff\1\6",
			"\1\5\4\uffff\1\7\11\uffff\1\6",
			"\1\5\4\uffff\1\7\11\uffff\1\6",
			"\1\66\13\uffff\1\65",
			"\1\67",
			"\1\5\4\uffff\1\7\11\uffff\1\6",
			"\1\70",
			"\1\5\4\uffff\1\7\11\uffff\1\6",
			"\1\5\4\uffff\1\7\11\uffff\1\6"
	};

	static final short[] DFA5_eot = DFA.unpackEncodedString(DFA5_eotS);
	static final short[] DFA5_eof = DFA.unpackEncodedString(DFA5_eofS);
	static final char[] DFA5_min = DFA.unpackEncodedStringToUnsignedChars(DFA5_minS);
	static final char[] DFA5_max = DFA.unpackEncodedStringToUnsignedChars(DFA5_maxS);
	static final short[] DFA5_accept = DFA.unpackEncodedString(DFA5_acceptS);
	static final short[] DFA5_special = DFA.unpackEncodedString(DFA5_specialS);
	static final short[][] DFA5_transition;

	static {
		int numStates = DFA5_transitionS.length;
		DFA5_transition = new short[numStates][];
		for (int i=0; i<numStates; i++) {
			DFA5_transition[i] = DFA.unpackEncodedString(DFA5_transitionS[i]);
		}
	}

	protected class DFA5 extends DFA {

		public DFA5(BaseRecognizer recognizer) {
			this.recognizer = recognizer;
			this.decisionNumber = 5;
			this.eot = DFA5_eot;
			this.eof = DFA5_eof;
			this.min = DFA5_min;
			this.max = DFA5_max;
			this.accept = DFA5_accept;
			this.special = DFA5_special;
			this.transition = DFA5_transition;
		}
		@Override
		public String getDescription() {
			return "56:1: cardconst returns [IntegrationFunctionExpression value ] : ( '{' expr= signalexpror '}' | '{' expr= signalexpror ',' 'min' '=' min= NUMBER '}' | '{' expr= signalexpror ',' 'max' '=' max= NUMBER '}' | '{' expr= signalexpror ',' 'min' '=' min= NUMBER ',' 'max' '=' max= NUMBER '}' );";
		}
	}

	public static final BitSet FOLLOW_functionexpror_in_eval46 = new BitSet(new long[]{0x0000000000000000L});
	public static final BitSet FOLLOW_EOF_in_eval48 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_functionexprand_in_functionexpror67 = new BitSet(new long[]{0x0000000000000102L});
	public static final BitSet FOLLOW_OR_in_functionexpror70 = new BitSet(new long[]{0x0000000000430840L});
	public static final BitSet FOLLOW_functionexprand_in_functionexpror74 = new BitSet(new long[]{0x0000000000000102L});
	public static final BitSet FOLLOW_functionexprnot_in_functionexprand98 = new BitSet(new long[]{0x0000000000000012L});
	public static final BitSet FOLLOW_AND_in_functionexprand101 = new BitSet(new long[]{0x0000000000430840L});
	public static final BitSet FOLLOW_functionexprnot_in_functionexprand105 = new BitSet(new long[]{0x0000000000000012L});
	public static final BitSet FOLLOW_functionparen_in_functionexprnot128 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_NOT_in_functionexprnot137 = new BitSet(new long[]{0x0000000000430800L});
	public static final BitSet FOLLOW_functionparen_in_functionexprnot141 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_11_in_functionparen160 = new BitSet(new long[]{0x0000000000430840L});
	public static final BitSet FOLLOW_functionexpror_in_functionparen164 = new BitSet(new long[]{0x0000000000001000L});
	public static final BitSet FOLLOW_12_in_functionparen166 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_cardconst_in_functionparen177 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_17_in_functionparen186 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_16_in_functionparen195 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_22_in_cardconst214 = new BitSet(new long[]{0x0000000000000020L});
	public static final BitSet FOLLOW_signalexpror_in_cardconst218 = new BitSet(new long[]{0x0000000000800000L});
	public static final BitSet FOLLOW_23_in_cardconst220 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_22_in_cardconst229 = new BitSet(new long[]{0x0000000000000020L});
	public static final BitSet FOLLOW_signalexpror_in_cardconst233 = new BitSet(new long[]{0x0000000000002000L});
	public static final BitSet FOLLOW_13_in_cardconst235 = new BitSet(new long[]{0x0000000000200000L});
	public static final BitSet FOLLOW_21_in_cardconst237 = new BitSet(new long[]{0x0000000000008000L});
	public static final BitSet FOLLOW_15_in_cardconst239 = new BitSet(new long[]{0x0000000000000080L});
	public static final BitSet FOLLOW_NUMBER_in_cardconst243 = new BitSet(new long[]{0x0000000000800000L});
	public static final BitSet FOLLOW_23_in_cardconst245 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_22_in_cardconst254 = new BitSet(new long[]{0x0000000000000020L});
	public static final BitSet FOLLOW_signalexpror_in_cardconst258 = new BitSet(new long[]{0x0000000000002000L});
	public static final BitSet FOLLOW_13_in_cardconst260 = new BitSet(new long[]{0x0000000000100000L});
	public static final BitSet FOLLOW_20_in_cardconst262 = new BitSet(new long[]{0x0000000000008000L});
	public static final BitSet FOLLOW_15_in_cardconst264 = new BitSet(new long[]{0x0000000000000080L});
	public static final BitSet FOLLOW_NUMBER_in_cardconst268 = new BitSet(new long[]{0x0000000000800000L});
	public static final BitSet FOLLOW_23_in_cardconst270 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_22_in_cardconst279 = new BitSet(new long[]{0x0000000000000020L});
	public static final BitSet FOLLOW_signalexpror_in_cardconst283 = new BitSet(new long[]{0x0000000000002000L});
	public static final BitSet FOLLOW_13_in_cardconst285 = new BitSet(new long[]{0x0000000000200000L});
	public static final BitSet FOLLOW_21_in_cardconst287 = new BitSet(new long[]{0x0000000000008000L});
	public static final BitSet FOLLOW_15_in_cardconst289 = new BitSet(new long[]{0x0000000000000080L});
	public static final BitSet FOLLOW_NUMBER_in_cardconst293 = new BitSet(new long[]{0x0000000000002000L});
	public static final BitSet FOLLOW_13_in_cardconst295 = new BitSet(new long[]{0x0000000000100000L});
	public static final BitSet FOLLOW_20_in_cardconst297 = new BitSet(new long[]{0x0000000000008000L});
	public static final BitSet FOLLOW_15_in_cardconst299 = new BitSet(new long[]{0x0000000000000080L});
	public static final BitSet FOLLOW_NUMBER_in_cardconst303 = new BitSet(new long[]{0x0000000000800000L});
	public static final BitSet FOLLOW_23_in_cardconst305 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_signal_in_signalexpror326 = new BitSet(new long[]{0x0000000000000102L});
	public static final BitSet FOLLOW_OR_in_signalexpror329 = new BitSet(new long[]{0x0000000000000020L});
	public static final BitSet FOLLOW_signal_in_signalexpror333 = new BitSet(new long[]{0x0000000000000102L});
	public static final BitSet FOLLOW_ID_in_signal357 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_ID_in_signal368 = new BitSet(new long[]{0x0000000000040000L});
	public static final BitSet FOLLOW_18_in_signal370 = new BitSet(new long[]{0x0000000000000080L});
	public static final BitSet FOLLOW_NUMBER_in_signal374 = new BitSet(new long[]{0x0000000000080000L});
	public static final BitSet FOLLOW_19_in_signal376 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_ID_in_signal387 = new BitSet(new long[]{0x0000000000040000L});
	public static final BitSet FOLLOW_18_in_signal389 = new BitSet(new long[]{0x0000000000000080L});
	public static final BitSet FOLLOW_NUMBER_in_signal393 = new BitSet(new long[]{0x0000000000004000L});
	public static final BitSet FOLLOW_14_in_signal395 = new BitSet(new long[]{0x0000000000080000L});
	public static final BitSet FOLLOW_19_in_signal397 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_ID_in_signal408 = new BitSet(new long[]{0x0000000000040000L});
	public static final BitSet FOLLOW_18_in_signal410 = new BitSet(new long[]{0x0000000000000080L});
	public static final BitSet FOLLOW_NUMBER_in_signal414 = new BitSet(new long[]{0x0000000000004000L});
	public static final BitSet FOLLOW_14_in_signal416 = new BitSet(new long[]{0x0000000000000080L});
	public static final BitSet FOLLOW_NUMBER_in_signal420 = new BitSet(new long[]{0x0000000000080000L});
	public static final BitSet FOLLOW_19_in_signal422 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_ID_in_signal433 = new BitSet(new long[]{0x0000000000040000L});
	public static final BitSet FOLLOW_18_in_signal435 = new BitSet(new long[]{0x0000000000004000L});
	public static final BitSet FOLLOW_14_in_signal437 = new BitSet(new long[]{0x0000000000000080L});
	public static final BitSet FOLLOW_NUMBER_in_signal441 = new BitSet(new long[]{0x0000000000080000L});
	public static final BitSet FOLLOW_19_in_signal443 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_ID_in_signal454 = new BitSet(new long[]{0x0000000000004000L});
	public static final BitSet FOLLOW_14_in_signal456 = new BitSet(new long[]{0x0000000000000080L});
	public static final BitSet FOLLOW_NUMBER_in_signal460 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_ID_in_signal471 = new BitSet(new long[]{0x0000000000004000L});
	public static final BitSet FOLLOW_14_in_signal473 = new BitSet(new long[]{0x0000000000000080L});
	public static final BitSet FOLLOW_NUMBER_in_signal477 = new BitSet(new long[]{0x0000000000040000L});
	public static final BitSet FOLLOW_18_in_signal479 = new BitSet(new long[]{0x0000000000000080L});
	public static final BitSet FOLLOW_NUMBER_in_signal483 = new BitSet(new long[]{0x0000000000080000L});
	public static final BitSet FOLLOW_19_in_signal485 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_ID_in_signal496 = new BitSet(new long[]{0x0000000000004000L});
	public static final BitSet FOLLOW_14_in_signal498 = new BitSet(new long[]{0x0000000000000080L});
	public static final BitSet FOLLOW_NUMBER_in_signal502 = new BitSet(new long[]{0x0000000000040000L});
	public static final BitSet FOLLOW_18_in_signal504 = new BitSet(new long[]{0x0000000000000080L});
	public static final BitSet FOLLOW_NUMBER_in_signal508 = new BitSet(new long[]{0x0000000000004000L});
	public static final BitSet FOLLOW_14_in_signal510 = new BitSet(new long[]{0x0000000000080000L});
	public static final BitSet FOLLOW_19_in_signal512 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_ID_in_signal523 = new BitSet(new long[]{0x0000000000004000L});
	public static final BitSet FOLLOW_14_in_signal525 = new BitSet(new long[]{0x0000000000000080L});
	public static final BitSet FOLLOW_NUMBER_in_signal529 = new BitSet(new long[]{0x0000000000040000L});
	public static final BitSet FOLLOW_18_in_signal531 = new BitSet(new long[]{0x0000000000000080L});
	public static final BitSet FOLLOW_NUMBER_in_signal535 = new BitSet(new long[]{0x0000000000004000L});
	public static final BitSet FOLLOW_14_in_signal537 = new BitSet(new long[]{0x0000000000000080L});
	public static final BitSet FOLLOW_NUMBER_in_signal541 = new BitSet(new long[]{0x0000000000080000L});
	public static final BitSet FOLLOW_19_in_signal543 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_ID_in_signal554 = new BitSet(new long[]{0x0000000000004000L});
	public static final BitSet FOLLOW_14_in_signal556 = new BitSet(new long[]{0x0000000000000080L});
	public static final BitSet FOLLOW_NUMBER_in_signal560 = new BitSet(new long[]{0x0000000000040000L});
	public static final BitSet FOLLOW_18_in_signal562 = new BitSet(new long[]{0x0000000000004000L});
	public static final BitSet FOLLOW_14_in_signal564 = new BitSet(new long[]{0x0000000000000080L});
	public static final BitSet FOLLOW_NUMBER_in_signal568 = new BitSet(new long[]{0x0000000000080000L});
	public static final BitSet FOLLOW_19_in_signal570 = new BitSet(new long[]{0x0000000000000002L});
}
