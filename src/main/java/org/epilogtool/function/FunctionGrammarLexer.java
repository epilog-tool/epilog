// $ANTLR 3.5.2 ./src/main/java/org/epilogtool/function/FunctionGrammar.g 2018-09-26 15:19:19
package org.epilogtool.function;

import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings("all")
public class FunctionGrammarLexer extends Lexer {
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

	  @Override
	  public void reportError(RecognitionException e) {
	    throw new RuntimeException("I quit!\n" + e.getMessage()); 
	  }


	// delegates
	// delegators
	public Lexer[] getDelegates() {
		return new Lexer[] {};
	}

	public FunctionGrammarLexer() {} 
	public FunctionGrammarLexer(CharStream input) {
		this(input, new RecognizerSharedState());
	}
	public FunctionGrammarLexer(CharStream input, RecognizerSharedState state) {
		super(input,state);
	}
	@Override public String getGrammarFileName() { return "./src/main/java/org/epilogtool/function/FunctionGrammar.g"; }

	// $ANTLR start "T__11"
	public final void mT__11() throws RecognitionException {
		try {
			int _type = T__11;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// ./src/main/java/org/epilogtool/function/FunctionGrammar.g:9:7: ( '(' )
			// ./src/main/java/org/epilogtool/function/FunctionGrammar.g:9:9: '('
			{
			match('('); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__11"

	// $ANTLR start "T__12"
	public final void mT__12() throws RecognitionException {
		try {
			int _type = T__12;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// ./src/main/java/org/epilogtool/function/FunctionGrammar.g:10:7: ( ')' )
			// ./src/main/java/org/epilogtool/function/FunctionGrammar.g:10:9: ')'
			{
			match(')'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__12"

	// $ANTLR start "T__13"
	public final void mT__13() throws RecognitionException {
		try {
			int _type = T__13;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// ./src/main/java/org/epilogtool/function/FunctionGrammar.g:11:7: ( ':' )
			// ./src/main/java/org/epilogtool/function/FunctionGrammar.g:11:9: ':'
			{
			match(':'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__13"

	// $ANTLR start "T__14"
	public final void mT__14() throws RecognitionException {
		try {
			int _type = T__14;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// ./src/main/java/org/epilogtool/function/FunctionGrammar.g:12:7: ( 'FALSE' )
			// ./src/main/java/org/epilogtool/function/FunctionGrammar.g:12:9: 'FALSE'
			{
			match("FALSE"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__14"

	// $ANTLR start "T__15"
	public final void mT__15() throws RecognitionException {
		try {
			int _type = T__15;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// ./src/main/java/org/epilogtool/function/FunctionGrammar.g:13:7: ( 'TRUE' )
			// ./src/main/java/org/epilogtool/function/FunctionGrammar.g:13:9: 'TRUE'
			{
			match("TRUE"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__15"

	// $ANTLR start "ID"
	public final void mID() throws RecognitionException {
		try {
			int _type = ID;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// ./src/main/java/org/epilogtool/function/FunctionGrammar.g:67:8: ( ( 'a' .. 'z' | 'A' .. 'Z' | '_' ) ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' )* )
			// ./src/main/java/org/epilogtool/function/FunctionGrammar.g:67:10: ( 'a' .. 'z' | 'A' .. 'Z' | '_' ) ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' )*
			{
			if ( (input.LA(1) >= 'A' && input.LA(1) <= 'Z')||input.LA(1)=='_'||(input.LA(1) >= 'a' && input.LA(1) <= 'z') ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			// ./src/main/java/org/epilogtool/function/FunctionGrammar.g:67:33: ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' )*
			loop1:
			while (true) {
				int alt1=2;
				int LA1_0 = input.LA(1);
				if ( ((LA1_0 >= '0' && LA1_0 <= '9')||(LA1_0 >= 'A' && LA1_0 <= 'Z')||LA1_0=='_'||(LA1_0 >= 'a' && LA1_0 <= 'z')) ) {
					alt1=1;
				}

				switch (alt1) {
				case 1 :
					// ./src/main/java/org/epilogtool/function/FunctionGrammar.g:
					{
					if ( (input.LA(1) >= '0' && input.LA(1) <= '9')||(input.LA(1) >= 'A' && input.LA(1) <= 'Z')||input.LA(1)=='_'||(input.LA(1) >= 'a' && input.LA(1) <= 'z') ) {
						input.consume();
					}
					else {
						MismatchedSetException mse = new MismatchedSetException(null,input);
						recover(mse);
						throw mse;
					}
					}
					break;

				default :
					break loop1;
				}
			}

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "ID"

	// $ANTLR start "NUMBER"
	public final void mNUMBER() throws RecognitionException {
		try {
			int _type = NUMBER;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// ./src/main/java/org/epilogtool/function/FunctionGrammar.g:66:8: ( ( '0' .. '9' )+ )
			// ./src/main/java/org/epilogtool/function/FunctionGrammar.g:66:10: ( '0' .. '9' )+
			{
			// ./src/main/java/org/epilogtool/function/FunctionGrammar.g:66:10: ( '0' .. '9' )+
			int cnt2=0;
			loop2:
			while (true) {
				int alt2=2;
				int LA2_0 = input.LA(1);
				if ( ((LA2_0 >= '0' && LA2_0 <= '9')) ) {
					alt2=1;
				}

				switch (alt2) {
				case 1 :
					// ./src/main/java/org/epilogtool/function/FunctionGrammar.g:
					{
					if ( (input.LA(1) >= '0' && input.LA(1) <= '9') ) {
						input.consume();
					}
					else {
						MismatchedSetException mse = new MismatchedSetException(null,input);
						recover(mse);
						throw mse;
					}
					}
					break;

				default :
					if ( cnt2 >= 1 ) break loop2;
					EarlyExitException eee = new EarlyExitException(2, input);
					throw eee;
				}
				cnt2++;
			}

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "NUMBER"

	// $ANTLR start "OR"
	public final void mOR() throws RecognitionException {
		try {
			int _type = OR;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// ./src/main/java/org/epilogtool/function/FunctionGrammar.g:67:8: ( '|' )
			// ./src/main/java/org/epilogtool/function/FunctionGrammar.g:67:10: '|'
			{
			match('|'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "OR"

	// $ANTLR start "AND"
	public final void mAND() throws RecognitionException {
		try {
			int _type = AND;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// ./src/main/java/org/epilogtool/function/FunctionGrammar.g:68:8: ( '&' )
			// ./src/main/java/org/epilogtool/function/FunctionGrammar.g:68:10: '&'
			{
			match('&'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "AND"

	// $ANTLR start "NOT"
	public final void mNOT() throws RecognitionException {
		try {
			int _type = NOT;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// ./src/main/java/org/epilogtool/function/FunctionGrammar.g:69:8: ( '!' )
			// ./src/main/java/org/epilogtool/function/FunctionGrammar.g:69:10: '!'
			{
			match('!'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "NOT"

	// $ANTLR start "WS"
	public final void mWS() throws RecognitionException {
		try {
			int _type = WS;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// ./src/main/java/org/epilogtool/function/FunctionGrammar.g:70:8: ( ( SPACE )+ )
			// ./src/main/java/org/epilogtool/function/FunctionGrammar.g:70:10: ( SPACE )+
			{
			// ./src/main/java/org/epilogtool/function/FunctionGrammar.g:70:10: ( SPACE )+
			int cnt3=0;
			loop3:
			while (true) {
				int alt3=2;
				int LA3_0 = input.LA(1);
				if ( ((LA3_0 >= '\t' && LA3_0 <= '\n')||(LA3_0 >= '\f' && LA3_0 <= '\r')||LA3_0==' ') ) {
					alt3=1;
				}

				switch (alt3) {
				case 1 :
					// ./src/main/java/org/epilogtool/function/FunctionGrammar.g:
					{
					if ( (input.LA(1) >= '\t' && input.LA(1) <= '\n')||(input.LA(1) >= '\f' && input.LA(1) <= '\r')||input.LA(1)==' ' ) {
						input.consume();
					}
					else {
						MismatchedSetException mse = new MismatchedSetException(null,input);
						recover(mse);
						throw mse;
					}
					}
					break;

				default :
					if ( cnt3 >= 1 ) break loop3;
					EarlyExitException eee = new EarlyExitException(3, input);
					throw eee;
				}
				cnt3++;
			}

			skip();
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "WS"

	// $ANTLR start "SPACE"
	public final void mSPACE() throws RecognitionException {
		try {
			// ./src/main/java/org/epilogtool/function/FunctionGrammar.g:71:16: ( '\\t' | ' ' | '\\r' | '\\n' | '\\u000C' )
			// ./src/main/java/org/epilogtool/function/FunctionGrammar.g:
			{
			if ( (input.LA(1) >= '\t' && input.LA(1) <= '\n')||(input.LA(1) >= '\f' && input.LA(1) <= '\r')||input.LA(1)==' ' ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "SPACE"

	@Override
	public void mTokens() throws RecognitionException {
		// ./src/main/java/org/epilogtool/function/FunctionGrammar.g:1:8: ( T__11 | T__12 | T__13 | T__14 | T__15 | ID | NUMBER | OR | AND | NOT | WS )
		int alt4=11;
		switch ( input.LA(1) ) {
		case '(':
			{
			alt4=1;
			}
			break;
		case ')':
			{
			alt4=2;
			}
			break;
		case ':':
			{
			alt4=3;
			}
			break;
		case 'F':
			{
			int LA4_4 = input.LA(2);
			if ( (LA4_4=='A') ) {
				int LA4_12 = input.LA(3);
				if ( (LA4_12=='L') ) {
					int LA4_14 = input.LA(4);
					if ( (LA4_14=='S') ) {
						int LA4_16 = input.LA(5);
						if ( (LA4_16=='E') ) {
							int LA4_18 = input.LA(6);
							if ( ((LA4_18 >= '0' && LA4_18 <= '9')||(LA4_18 >= 'A' && LA4_18 <= 'Z')||LA4_18=='_'||(LA4_18 >= 'a' && LA4_18 <= 'z')) ) {
								alt4=6;
							}

							else {
								alt4=4;
							}

						}

						else {
							alt4=6;
						}

					}

					else {
						alt4=6;
					}

				}

				else {
					alt4=6;
				}

			}

			else {
				alt4=6;
			}

			}
			break;
		case 'T':
			{
			int LA4_5 = input.LA(2);
			if ( (LA4_5=='R') ) {
				int LA4_13 = input.LA(3);
				if ( (LA4_13=='U') ) {
					int LA4_15 = input.LA(4);
					if ( (LA4_15=='E') ) {
						int LA4_17 = input.LA(5);
						if ( ((LA4_17 >= '0' && LA4_17 <= '9')||(LA4_17 >= 'A' && LA4_17 <= 'Z')||LA4_17=='_'||(LA4_17 >= 'a' && LA4_17 <= 'z')) ) {
							alt4=6;
						}

						else {
							alt4=5;
						}

					}

					else {
						alt4=6;
					}

				}

				else {
					alt4=6;
				}

			}

			else {
				alt4=6;
			}

			}
			break;
		case 'A':
		case 'B':
		case 'C':
		case 'D':
		case 'E':
		case 'G':
		case 'H':
		case 'I':
		case 'J':
		case 'K':
		case 'L':
		case 'M':
		case 'N':
		case 'O':
		case 'P':
		case 'Q':
		case 'R':
		case 'S':
		case 'U':
		case 'V':
		case 'W':
		case 'X':
		case 'Y':
		case 'Z':
		case '_':
		case 'a':
		case 'b':
		case 'c':
		case 'd':
		case 'e':
		case 'f':
		case 'g':
		case 'h':
		case 'i':
		case 'j':
		case 'k':
		case 'l':
		case 'm':
		case 'n':
		case 'o':
		case 'p':
		case 'q':
		case 'r':
		case 's':
		case 't':
		case 'u':
		case 'v':
		case 'w':
		case 'x':
		case 'y':
		case 'z':
			{
			alt4=6;
			}
			break;
		case '0':
		case '1':
		case '2':
		case '3':
		case '4':
		case '5':
		case '6':
		case '7':
		case '8':
		case '9':
			{
			alt4=7;
			}
			break;
		case '|':
			{
			alt4=8;
			}
			break;
		case '&':
			{
			alt4=9;
			}
			break;
		case '!':
			{
			alt4=10;
			}
			break;
		case '\t':
		case '\n':
		case '\f':
		case '\r':
		case ' ':
			{
			alt4=11;
			}
			break;
		default:
			NoViableAltException nvae =
				new NoViableAltException("", 4, 0, input);
			throw nvae;
		}
		switch (alt4) {
			case 1 :
				// ./src/main/java/org/epilogtool/function/FunctionGrammar.g:1:10: T__11
				{
				mT__11(); 

				}
				break;
			case 2 :
				// ./src/main/java/org/epilogtool/function/FunctionGrammar.g:1:16: T__12
				{
				mT__12(); 

				}
				break;
			case 3 :
				// ./src/main/java/org/epilogtool/function/FunctionGrammar.g:1:22: T__13
				{
				mT__13(); 

				}
				break;
			case 4 :
				// ./src/main/java/org/epilogtool/function/FunctionGrammar.g:1:28: T__14
				{
				mT__14(); 

				}
				break;
			case 5 :
				// ./src/main/java/org/epilogtool/function/FunctionGrammar.g:1:34: T__15
				{
				mT__15(); 

				}
				break;
			case 6 :
				// ./src/main/java/org/epilogtool/function/FunctionGrammar.g:1:40: ID
				{
				mID(); 

				}
				break;
			case 7 :
				// ./src/main/java/org/epilogtool/function/FunctionGrammar.g:1:43: NUMBER
				{
				mNUMBER(); 

				}
				break;
			case 8 :
				// ./src/main/java/org/epilogtool/function/FunctionGrammar.g:1:50: OR
				{
				mOR(); 

				}
				break;
			case 9 :
				// ./src/main/java/org/epilogtool/function/FunctionGrammar.g:1:53: AND
				{
				mAND(); 

				}
				break;
			case 10 :
				// ./src/main/java/org/epilogtool/function/FunctionGrammar.g:1:57: NOT
				{
				mNOT(); 

				}
				break;
			case 11 :
				// ./src/main/java/org/epilogtool/function/FunctionGrammar.g:1:61: WS
				{
				mWS(); 

				}
				break;

		}
	}



}
