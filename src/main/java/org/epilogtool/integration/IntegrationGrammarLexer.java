package org.epilogtool.integration;

import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings("all")
public class IntegrationGrammarLexer extends Lexer {
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

	  @Override
	  public void reportError(RecognitionException e) {
	    throw new RuntimeException("I quit!\n" + e.getMessage()); 
	  }


	// delegates
	// delegators
	public Lexer[] getDelegates() {
		return new Lexer[] {};
	}

	public IntegrationGrammarLexer() {} 
	public IntegrationGrammarLexer(CharStream input) {
		this(input, new RecognizerSharedState());
	}
	public IntegrationGrammarLexer(CharStream input, RecognizerSharedState state) {
		super(input,state);
	}
	@Override public String getGrammarFileName() { return "src/main/java/org/epilogtool/integration/IntegrationGrammar.g"; }

	// $ANTLR start "T__11"
	public final void mT__11() throws RecognitionException {
		try {
			int _type = T__11;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// src/main/java/org/epilogtool/integration/IntegrationGrammar.g:9:7: ( '(' )
			// src/main/java/org/epilogtool/integration/IntegrationGrammar.g:9:9: '('
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
			// src/main/java/org/epilogtool/integration/IntegrationGrammar.g:10:7: ( ')' )
			// src/main/java/org/epilogtool/integration/IntegrationGrammar.g:10:9: ')'
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
			// src/main/java/org/epilogtool/integration/IntegrationGrammar.g:11:7: ( ',' )
			// src/main/java/org/epilogtool/integration/IntegrationGrammar.g:11:9: ','
			{
			match(','); 
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
			// src/main/java/org/epilogtool/integration/IntegrationGrammar.g:12:7: ( ':' )
			// src/main/java/org/epilogtool/integration/IntegrationGrammar.g:12:9: ':'
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
	// $ANTLR end "T__14"

	// $ANTLR start "T__15"
	public final void mT__15() throws RecognitionException {
		try {
			int _type = T__15;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// src/main/java/org/epilogtool/integration/IntegrationGrammar.g:13:7: ( '=' )
			// src/main/java/org/epilogtool/integration/IntegrationGrammar.g:13:9: '='
			{
			match('='); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__15"

	// $ANTLR start "T__16"
	public final void mT__16() throws RecognitionException {
		try {
			int _type = T__16;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// src/main/java/org/epilogtool/integration/IntegrationGrammar.g:14:7: ( 'FALSE' )
			// src/main/java/org/epilogtool/integration/IntegrationGrammar.g:14:9: 'FALSE'
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
	// $ANTLR end "T__16"

	// $ANTLR start "T__17"
	public final void mT__17() throws RecognitionException {
		try {
			int _type = T__17;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// src/main/java/org/epilogtool/integration/IntegrationGrammar.g:15:7: ( 'TRUE' )
			// src/main/java/org/epilogtool/integration/IntegrationGrammar.g:15:9: 'TRUE'
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
	// $ANTLR end "T__17"

	// $ANTLR start "T__18"
	public final void mT__18() throws RecognitionException {
		try {
			int _type = T__18;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// src/main/java/org/epilogtool/integration/IntegrationGrammar.g:16:7: ( '[' )
			// src/main/java/org/epilogtool/integration/IntegrationGrammar.g:16:9: '['
			{
			match('['); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__18"

	// $ANTLR start "T__19"
	public final void mT__19() throws RecognitionException {
		try {
			int _type = T__19;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// src/main/java/org/epilogtool/integration/IntegrationGrammar.g:17:7: ( ']' )
			// src/main/java/org/epilogtool/integration/IntegrationGrammar.g:17:9: ']'
			{
			match(']'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__19"

	// $ANTLR start "T__20"
	public final void mT__20() throws RecognitionException {
		try {
			int _type = T__20;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// src/main/java/org/epilogtool/integration/IntegrationGrammar.g:18:7: ( 'max' )
			// src/main/java/org/epilogtool/integration/IntegrationGrammar.g:18:9: 'max'
			{
			match("max"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__20"

	// $ANTLR start "T__21"
	public final void mT__21() throws RecognitionException {
		try {
			int _type = T__21;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// src/main/java/org/epilogtool/integration/IntegrationGrammar.g:19:7: ( 'min' )
			// src/main/java/org/epilogtool/integration/IntegrationGrammar.g:19:9: 'min'
			{
			match("min"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__21"

	// $ANTLR start "T__22"
	public final void mT__22() throws RecognitionException {
		try {
			int _type = T__22;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// src/main/java/org/epilogtool/integration/IntegrationGrammar.g:20:7: ( '{' )
			// src/main/java/org/epilogtool/integration/IntegrationGrammar.g:20:9: '{'
			{
			match('{'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__22"

	// $ANTLR start "T__23"
	public final void mT__23() throws RecognitionException {
		try {
			int _type = T__23;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// src/main/java/org/epilogtool/integration/IntegrationGrammar.g:21:7: ( '}' )
			// src/main/java/org/epilogtool/integration/IntegrationGrammar.g:21:9: '}'
			{
			match('}'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__23"

	// $ANTLR start "ID"
	public final void mID() throws RecognitionException {
		try {
			int _type = ID;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// src/main/java/org/epilogtool/integration/IntegrationGrammar.g:99:8: ( ( 'a' .. 'z' | 'A' .. 'Z' | '_' ) ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' )* )
			// src/main/java/org/epilogtool/integration/IntegrationGrammar.g:99:10: ( 'a' .. 'z' | 'A' .. 'Z' | '_' ) ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' )*
			{
			if ( (input.LA(1) >= 'A' && input.LA(1) <= 'Z')||input.LA(1)=='_'||(input.LA(1) >= 'a' && input.LA(1) <= 'z') ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			// src/main/java/org/epilogtool/integration/IntegrationGrammar.g:99:33: ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' )*
			loop1:
			while (true) {
				int alt1=2;
				int LA1_0 = input.LA(1);
				if ( ((LA1_0 >= '0' && LA1_0 <= '9')||(LA1_0 >= 'A' && LA1_0 <= 'Z')||LA1_0=='_'||(LA1_0 >= 'a' && LA1_0 <= 'z')) ) {
					alt1=1;
				}

				switch (alt1) {
				case 1 :
					// src/main/java/org/epilogtool/integration/IntegrationGrammar.g:
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
			// src/main/java/org/epilogtool/integration/IntegrationGrammar.g:98:8: ( ( '0' .. '9' )+ )
			// src/main/java/org/epilogtool/integration/IntegrationGrammar.g:98:10: ( '0' .. '9' )+
			{
			// src/main/java/org/epilogtool/integration/IntegrationGrammar.g:98:10: ( '0' .. '9' )+
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
					// src/main/java/org/epilogtool/integration/IntegrationGrammar.g:
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
			// src/main/java/org/epilogtool/integration/IntegrationGrammar.g:99:8: ( '|' )
			// src/main/java/org/epilogtool/integration/IntegrationGrammar.g:99:10: '|'
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
			// src/main/java/org/epilogtool/integration/IntegrationGrammar.g:100:8: ( '&' )
			// src/main/java/org/epilogtool/integration/IntegrationGrammar.g:100:10: '&'
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
			// src/main/java/org/epilogtool/integration/IntegrationGrammar.g:101:8: ( '!' )
			// src/main/java/org/epilogtool/integration/IntegrationGrammar.g:101:10: '!'
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
			// src/main/java/org/epilogtool/integration/IntegrationGrammar.g:102:8: ( ( SPACE )+ )
			// src/main/java/org/epilogtool/integration/IntegrationGrammar.g:102:10: ( SPACE )+
			{
			// src/main/java/org/epilogtool/integration/IntegrationGrammar.g:102:10: ( SPACE )+
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
					// src/main/java/org/epilogtool/integration/IntegrationGrammar.g:
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
			// src/main/java/org/epilogtool/integration/IntegrationGrammar.g:103:16: ( '\\t' | ' ' | '\\r' | '\\n' | '\\u000C' )
			// src/main/java/org/epilogtool/integration/IntegrationGrammar.g:
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
		// src/main/java/org/epilogtool/integration/IntegrationGrammar.g:1:8: ( T__11 | T__12 | T__13 | T__14 | T__15 | T__16 | T__17 | T__18 | T__19 | T__20 | T__21 | T__22 | T__23 | ID | NUMBER | OR | AND | NOT | WS )
		int alt4=19;
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
		case ',':
			{
			alt4=3;
			}
			break;
		case ':':
			{
			alt4=4;
			}
			break;
		case '=':
			{
			alt4=5;
			}
			break;
		case 'F':
			{
			int LA4_6 = input.LA(2);
			if ( (LA4_6=='A') ) {
				int LA4_19 = input.LA(3);
				if ( (LA4_19=='L') ) {
					int LA4_23 = input.LA(4);
					if ( (LA4_23=='S') ) {
						int LA4_27 = input.LA(5);
						if ( (LA4_27=='E') ) {
							int LA4_31 = input.LA(6);
							if ( ((LA4_31 >= '0' && LA4_31 <= '9')||(LA4_31 >= 'A' && LA4_31 <= 'Z')||LA4_31=='_'||(LA4_31 >= 'a' && LA4_31 <= 'z')) ) {
								alt4=14;
							}

							else {
								alt4=6;
							}

						}

						else {
							alt4=14;
						}

					}

					else {
						alt4=14;
					}

				}

				else {
					alt4=14;
				}

			}

			else {
				alt4=14;
			}

			}
			break;
		case 'T':
			{
			int LA4_7 = input.LA(2);
			if ( (LA4_7=='R') ) {
				int LA4_20 = input.LA(3);
				if ( (LA4_20=='U') ) {
					int LA4_24 = input.LA(4);
					if ( (LA4_24=='E') ) {
						int LA4_28 = input.LA(5);
						if ( ((LA4_28 >= '0' && LA4_28 <= '9')||(LA4_28 >= 'A' && LA4_28 <= 'Z')||LA4_28=='_'||(LA4_28 >= 'a' && LA4_28 <= 'z')) ) {
							alt4=14;
						}

						else {
							alt4=7;
						}

					}

					else {
						alt4=14;
					}

				}

				else {
					alt4=14;
				}

			}

			else {
				alt4=14;
			}

			}
			break;
		case '[':
			{
			alt4=8;
			}
			break;
		case ']':
			{
			alt4=9;
			}
			break;
		case 'm':
			{
			switch ( input.LA(2) ) {
			case 'a':
				{
				int LA4_21 = input.LA(3);
				if ( (LA4_21=='x') ) {
					int LA4_25 = input.LA(4);
					if ( ((LA4_25 >= '0' && LA4_25 <= '9')||(LA4_25 >= 'A' && LA4_25 <= 'Z')||LA4_25=='_'||(LA4_25 >= 'a' && LA4_25 <= 'z')) ) {
						alt4=14;
					}

					else {
						alt4=10;
					}

				}

				else {
					alt4=14;
				}

				}
				break;
			case 'i':
				{
				int LA4_22 = input.LA(3);
				if ( (LA4_22=='n') ) {
					int LA4_26 = input.LA(4);
					if ( ((LA4_26 >= '0' && LA4_26 <= '9')||(LA4_26 >= 'A' && LA4_26 <= 'Z')||LA4_26=='_'||(LA4_26 >= 'a' && LA4_26 <= 'z')) ) {
						alt4=14;
					}

					else {
						alt4=11;
					}

				}

				else {
					alt4=14;
				}

				}
				break;
			default:
				alt4=14;
			}
			}
			break;
		case '{':
			{
			alt4=12;
			}
			break;
		case '}':
			{
			alt4=13;
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
			alt4=14;
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
			alt4=15;
			}
			break;
		case '|':
			{
			alt4=16;
			}
			break;
		case '&':
			{
			alt4=17;
			}
			break;
		case '!':
			{
			alt4=18;
			}
			break;
		case '\t':
		case '\n':
		case '\f':
		case '\r':
		case ' ':
			{
			alt4=19;
			}
			break;
		default:
			NoViableAltException nvae =
				new NoViableAltException("", 4, 0, input);
			throw nvae;
		}
		switch (alt4) {
			case 1 :
				// src/main/java/org/epilogtool/integration/IntegrationGrammar.g:1:10: T__11
				{
				mT__11(); 

				}
				break;
			case 2 :
				// src/main/java/org/epilogtool/integration/IntegrationGrammar.g:1:16: T__12
				{
				mT__12(); 

				}
				break;
			case 3 :
				// src/main/java/org/epilogtool/integration/IntegrationGrammar.g:1:22: T__13
				{
				mT__13(); 

				}
				break;
			case 4 :
				// src/main/java/org/epilogtool/integration/IntegrationGrammar.g:1:28: T__14
				{
				mT__14(); 

				}
				break;
			case 5 :
				// src/main/java/org/epilogtool/integration/IntegrationGrammar.g:1:34: T__15
				{
				mT__15(); 

				}
				break;
			case 6 :
				// src/main/java/org/epilogtool/integration/IntegrationGrammar.g:1:40: T__16
				{
				mT__16(); 

				}
				break;
			case 7 :
				// src/main/java/org/epilogtool/integration/IntegrationGrammar.g:1:46: T__17
				{
				mT__17(); 

				}
				break;
			case 8 :
				// src/main/java/org/epilogtool/integration/IntegrationGrammar.g:1:52: T__18
				{
				mT__18(); 

				}
				break;
			case 9 :
				// src/main/java/org/epilogtool/integration/IntegrationGrammar.g:1:58: T__19
				{
				mT__19(); 

				}
				break;
			case 10 :
				// src/main/java/org/epilogtool/integration/IntegrationGrammar.g:1:64: T__20
				{
				mT__20(); 

				}
				break;
			case 11 :
				// src/main/java/org/epilogtool/integration/IntegrationGrammar.g:1:70: T__21
				{
				mT__21(); 

				}
				break;
			case 12 :
				// src/main/java/org/epilogtool/integration/IntegrationGrammar.g:1:76: T__22
				{
				mT__22(); 

				}
				break;
			case 13 :
				// src/main/java/org/epilogtool/integration/IntegrationGrammar.g:1:82: T__23
				{
				mT__23(); 

				}
				break;
			case 14 :
				// src/main/java/org/epilogtool/integration/IntegrationGrammar.g:1:88: ID
				{
				mID(); 

				}
				break;
			case 15 :
				// src/main/java/org/epilogtool/integration/IntegrationGrammar.g:1:91: NUMBER
				{
				mNUMBER(); 

				}
				break;
			case 16 :
				// src/main/java/org/epilogtool/integration/IntegrationGrammar.g:1:98: OR
				{
				mOR(); 

				}
				break;
			case 17 :
				// src/main/java/org/epilogtool/integration/IntegrationGrammar.g:1:101: AND
				{
				mAND(); 

				}
				break;
			case 18 :
				// src/main/java/org/epilogtool/integration/IntegrationGrammar.g:1:105: NOT
				{
				mNOT(); 

				}
				break;
			case 19 :
				// src/main/java/org/epilogtool/integration/IntegrationGrammar.g:1:109: WS
				{
				mWS(); 

				}
				break;

		}
	}



}
