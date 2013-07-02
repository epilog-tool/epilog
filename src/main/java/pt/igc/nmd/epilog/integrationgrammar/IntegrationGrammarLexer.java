// $ANTLR 3.5 /Users/nuno/projects/nmd/ginsim-dev/src/main/java/org/ginsim/servicegui/tool/composition/integrationgrammar/IntegrationGrammar.g 2013-04-09 19:00:47
package pt.igc.nmd.epilog.integrationgrammar;

import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings("all")
public class IntegrationGrammarLexer extends Lexer {
	public static final int EOF=-1;
	public static final int T__10=10;
	public static final int T__11=11;
	public static final int T__12=12;
	public static final int AND=4;
	public static final int ENUMBER=5;
	public static final int ID=6;
	public static final int NOT=7;
	public static final int OR=8;
	public static final int RANGE=9;

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
	@Override public String getGrammarFileName() { return "/Users/nuno/projects/nmd/ginsim-dev/src/main/java/org/ginsim/servicegui/tool/composition/integrationgrammar/IntegrationGrammar.g"; }

	// $ANTLR start "T__10"
	public final void mT__10() throws RecognitionException {
		try {
			int _type = T__10;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /Users/nuno/projects/nmd/ginsim-dev/src/main/java/org/ginsim/servicegui/tool/composition/integrationgrammar/IntegrationGrammar.g:2:7: ( '(' )
			// /Users/nuno/projects/nmd/ginsim-dev/src/main/java/org/ginsim/servicegui/tool/composition/integrationgrammar/IntegrationGrammar.g:2:9: '('
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
	// $ANTLR end "T__10"

	// $ANTLR start "T__11"
	public final void mT__11() throws RecognitionException {
		try {
			int _type = T__11;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /Users/nuno/projects/nmd/ginsim-dev/src/main/java/org/ginsim/servicegui/tool/composition/integrationgrammar/IntegrationGrammar.g:3:7: ( ')' )
			// /Users/nuno/projects/nmd/ginsim-dev/src/main/java/org/ginsim/servicegui/tool/composition/integrationgrammar/IntegrationGrammar.g:3:9: ')'
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
	// $ANTLR end "T__11"

	// $ANTLR start "T__12"
	public final void mT__12() throws RecognitionException {
		try {
			int _type = T__12;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /Users/nuno/projects/nmd/ginsim-dev/src/main/java/org/ginsim/servicegui/tool/composition/integrationgrammar/IntegrationGrammar.g:4:7: ( ',' )
			// /Users/nuno/projects/nmd/ginsim-dev/src/main/java/org/ginsim/servicegui/tool/composition/integrationgrammar/IntegrationGrammar.g:4:9: ','
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
	// $ANTLR end "T__12"

	// $ANTLR start "ENUMBER"
	public final void mENUMBER() throws RecognitionException {
		try {
			int _type = ENUMBER;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /Users/nuno/projects/nmd/ginsim-dev/src/main/java/org/ginsim/servicegui/tool/composition/integrationgrammar/IntegrationGrammar.g:41:9: ( ( '0' .. '9' )+ | '_' )
			int alt2=2;
			int LA2_0 = input.LA(1);
			if ( ((LA2_0 >= '0' && LA2_0 <= '9')) ) {
				alt2=1;
			}
			else if ( (LA2_0=='_') ) {
				alt2=2;
			}

			else {
				NoViableAltException nvae =
					new NoViableAltException("", 2, 0, input);
				throw nvae;
			}

			switch (alt2) {
				case 1 :
					// /Users/nuno/projects/nmd/ginsim-dev/src/main/java/org/ginsim/servicegui/tool/composition/integrationgrammar/IntegrationGrammar.g:41:11: ( '0' .. '9' )+
					{
					// /Users/nuno/projects/nmd/ginsim-dev/src/main/java/org/ginsim/servicegui/tool/composition/integrationgrammar/IntegrationGrammar.g:41:11: ( '0' .. '9' )+
					int cnt1=0;
					loop1:
					while (true) {
						int alt1=2;
						int LA1_0 = input.LA(1);
						if ( ((LA1_0 >= '0' && LA1_0 <= '9')) ) {
							alt1=1;
						}

						switch (alt1) {
						case 1 :
							// /Users/nuno/projects/nmd/ginsim-dev/src/main/java/org/ginsim/servicegui/tool/composition/integrationgrammar/IntegrationGrammar.g:
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
							if ( cnt1 >= 1 ) break loop1;
							EarlyExitException eee = new EarlyExitException(1, input);
							throw eee;
						}
						cnt1++;
					}

					}
					break;
				case 2 :
					// /Users/nuno/projects/nmd/ginsim-dev/src/main/java/org/ginsim/servicegui/tool/composition/integrationgrammar/IntegrationGrammar.g:41:27: '_'
					{
					match('_'); 
					}
					break;

			}
			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "ENUMBER"

	// $ANTLR start "RANGE"
	public final void mRANGE() throws RecognitionException {
		try {
			int _type = RANGE;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /Users/nuno/projects/nmd/ginsim-dev/src/main/java/org/ginsim/servicegui/tool/composition/integrationgrammar/IntegrationGrammar.g:42:7: ( ( '0' .. '9' )+ ':' ( '0' .. '9' )+ )
			// /Users/nuno/projects/nmd/ginsim-dev/src/main/java/org/ginsim/servicegui/tool/composition/integrationgrammar/IntegrationGrammar.g:42:9: ( '0' .. '9' )+ ':' ( '0' .. '9' )+
			{
			// /Users/nuno/projects/nmd/ginsim-dev/src/main/java/org/ginsim/servicegui/tool/composition/integrationgrammar/IntegrationGrammar.g:42:9: ( '0' .. '9' )+
			int cnt3=0;
			loop3:
			while (true) {
				int alt3=2;
				int LA3_0 = input.LA(1);
				if ( ((LA3_0 >= '0' && LA3_0 <= '9')) ) {
					alt3=1;
				}

				switch (alt3) {
				case 1 :
					// /Users/nuno/projects/nmd/ginsim-dev/src/main/java/org/ginsim/servicegui/tool/composition/integrationgrammar/IntegrationGrammar.g:
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
					if ( cnt3 >= 1 ) break loop3;
					EarlyExitException eee = new EarlyExitException(3, input);
					throw eee;
				}
				cnt3++;
			}

			match(':'); 
			// /Users/nuno/projects/nmd/ginsim-dev/src/main/java/org/ginsim/servicegui/tool/composition/integrationgrammar/IntegrationGrammar.g:42:27: ( '0' .. '9' )+
			int cnt4=0;
			loop4:
			while (true) {
				int alt4=2;
				int LA4_0 = input.LA(1);
				if ( ((LA4_0 >= '0' && LA4_0 <= '9')) ) {
					alt4=1;
				}

				switch (alt4) {
				case 1 :
					// /Users/nuno/projects/nmd/ginsim-dev/src/main/java/org/ginsim/servicegui/tool/composition/integrationgrammar/IntegrationGrammar.g:
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
					if ( cnt4 >= 1 ) break loop4;
					EarlyExitException eee = new EarlyExitException(4, input);
					throw eee;
				}
				cnt4++;
			}

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "RANGE"

	// $ANTLR start "ID"
	public final void mID() throws RecognitionException {
		try {
			int _type = ID;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /Users/nuno/projects/nmd/ginsim-dev/src/main/java/org/ginsim/servicegui/tool/composition/integrationgrammar/IntegrationGrammar.g:43:4: ( ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' | '-' )+ )
			// /Users/nuno/projects/nmd/ginsim-dev/src/main/java/org/ginsim/servicegui/tool/composition/integrationgrammar/IntegrationGrammar.g:43:6: ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' | '-' )+
			{
			// /Users/nuno/projects/nmd/ginsim-dev/src/main/java/org/ginsim/servicegui/tool/composition/integrationgrammar/IntegrationGrammar.g:43:6: ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' | '-' )+
			int cnt5=0;
			loop5:
			while (true) {
				int alt5=2;
				int LA5_0 = input.LA(1);
				if ( (LA5_0=='-'||(LA5_0 >= '0' && LA5_0 <= '9')||(LA5_0 >= 'A' && LA5_0 <= 'Z')||LA5_0=='_'||(LA5_0 >= 'a' && LA5_0 <= 'z')) ) {
					alt5=1;
				}

				switch (alt5) {
				case 1 :
					// /Users/nuno/projects/nmd/ginsim-dev/src/main/java/org/ginsim/servicegui/tool/composition/integrationgrammar/IntegrationGrammar.g:
					{
					if ( input.LA(1)=='-'||(input.LA(1) >= '0' && input.LA(1) <= '9')||(input.LA(1) >= 'A' && input.LA(1) <= 'Z')||input.LA(1)=='_'||(input.LA(1) >= 'a' && input.LA(1) <= 'z') ) {
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
					if ( cnt5 >= 1 ) break loop5;
					EarlyExitException eee = new EarlyExitException(5, input);
					throw eee;
				}
				cnt5++;
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

	// $ANTLR start "OR"
	public final void mOR() throws RecognitionException {
		try {
			int _type = OR;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /Users/nuno/projects/nmd/ginsim-dev/src/main/java/org/ginsim/servicegui/tool/composition/integrationgrammar/IntegrationGrammar.g:44:4: ( '|' )
			// /Users/nuno/projects/nmd/ginsim-dev/src/main/java/org/ginsim/servicegui/tool/composition/integrationgrammar/IntegrationGrammar.g:44:6: '|'
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
			// /Users/nuno/projects/nmd/ginsim-dev/src/main/java/org/ginsim/servicegui/tool/composition/integrationgrammar/IntegrationGrammar.g:45:5: ( '&' )
			// /Users/nuno/projects/nmd/ginsim-dev/src/main/java/org/ginsim/servicegui/tool/composition/integrationgrammar/IntegrationGrammar.g:45:7: '&'
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
			// /Users/nuno/projects/nmd/ginsim-dev/src/main/java/org/ginsim/servicegui/tool/composition/integrationgrammar/IntegrationGrammar.g:46:5: ( '!' )
			// /Users/nuno/projects/nmd/ginsim-dev/src/main/java/org/ginsim/servicegui/tool/composition/integrationgrammar/IntegrationGrammar.g:46:7: '!'
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

	@Override
	public void mTokens() throws RecognitionException {
		// /Users/nuno/projects/nmd/ginsim-dev/src/main/java/org/ginsim/servicegui/tool/composition/integrationgrammar/IntegrationGrammar.g:1:8: ( T__10 | T__11 | T__12 | ENUMBER | RANGE | ID | OR | AND | NOT )
		int alt6=9;
		alt6 = dfa6.predict(input);
		switch (alt6) {
			case 1 :
				// /Users/nuno/projects/nmd/ginsim-dev/src/main/java/org/ginsim/servicegui/tool/composition/integrationgrammar/IntegrationGrammar.g:1:10: T__10
				{
				mT__10(); 

				}
				break;
			case 2 :
				// /Users/nuno/projects/nmd/ginsim-dev/src/main/java/org/ginsim/servicegui/tool/composition/integrationgrammar/IntegrationGrammar.g:1:16: T__11
				{
				mT__11(); 

				}
				break;
			case 3 :
				// /Users/nuno/projects/nmd/ginsim-dev/src/main/java/org/ginsim/servicegui/tool/composition/integrationgrammar/IntegrationGrammar.g:1:22: T__12
				{
				mT__12(); 

				}
				break;
			case 4 :
				// /Users/nuno/projects/nmd/ginsim-dev/src/main/java/org/ginsim/servicegui/tool/composition/integrationgrammar/IntegrationGrammar.g:1:28: ENUMBER
				{
				mENUMBER(); 

				}
				break;
			case 5 :
				// /Users/nuno/projects/nmd/ginsim-dev/src/main/java/org/ginsim/servicegui/tool/composition/integrationgrammar/IntegrationGrammar.g:1:36: RANGE
				{
				mRANGE(); 

				}
				break;
			case 6 :
				// /Users/nuno/projects/nmd/ginsim-dev/src/main/java/org/ginsim/servicegui/tool/composition/integrationgrammar/IntegrationGrammar.g:1:42: ID
				{
				mID(); 

				}
				break;
			case 7 :
				// /Users/nuno/projects/nmd/ginsim-dev/src/main/java/org/ginsim/servicegui/tool/composition/integrationgrammar/IntegrationGrammar.g:1:45: OR
				{
				mOR(); 

				}
				break;
			case 8 :
				// /Users/nuno/projects/nmd/ginsim-dev/src/main/java/org/ginsim/servicegui/tool/composition/integrationgrammar/IntegrationGrammar.g:1:48: AND
				{
				mAND(); 

				}
				break;
			case 9 :
				// /Users/nuno/projects/nmd/ginsim-dev/src/main/java/org/ginsim/servicegui/tool/composition/integrationgrammar/IntegrationGrammar.g:1:52: NOT
				{
				mNOT(); 

				}
				break;

		}
	}


	protected DFA6 dfa6 = new DFA6(this);
	static final String DFA6_eotS =
		"\4\uffff\2\12\6\uffff";
	static final String DFA6_eofS =
		"\14\uffff";
	static final String DFA6_minS =
		"\1\41\3\uffff\2\55\6\uffff";
	static final String DFA6_maxS =
		"\1\174\3\uffff\2\172\6\uffff";
	static final String DFA6_acceptS =
		"\1\uffff\1\1\1\2\1\3\2\uffff\1\6\1\7\1\10\1\11\1\4\1\5";
	static final String DFA6_specialS =
		"\14\uffff}>";
	static final String[] DFA6_transitionS = {
			"\1\11\4\uffff\1\10\1\uffff\1\1\1\2\2\uffff\1\3\1\6\2\uffff\12\4\7\uffff"+
			"\32\6\4\uffff\1\5\1\uffff\32\6\1\uffff\1\7",
			"",
			"",
			"",
			"\1\6\2\uffff\12\4\1\13\6\uffff\32\6\4\uffff\1\6\1\uffff\32\6",
			"\1\6\2\uffff\12\6\7\uffff\32\6\4\uffff\1\6\1\uffff\32\6",
			"",
			"",
			"",
			"",
			"",
			""
	};

	static final short[] DFA6_eot = DFA.unpackEncodedString(DFA6_eotS);
	static final short[] DFA6_eof = DFA.unpackEncodedString(DFA6_eofS);
	static final char[] DFA6_min = DFA.unpackEncodedStringToUnsignedChars(DFA6_minS);
	static final char[] DFA6_max = DFA.unpackEncodedStringToUnsignedChars(DFA6_maxS);
	static final short[] DFA6_accept = DFA.unpackEncodedString(DFA6_acceptS);
	static final short[] DFA6_special = DFA.unpackEncodedString(DFA6_specialS);
	static final short[][] DFA6_transition;

	static {
		int numStates = DFA6_transitionS.length;
		DFA6_transition = new short[numStates][];
		for (int i=0; i<numStates; i++) {
			DFA6_transition[i] = DFA.unpackEncodedString(DFA6_transitionS[i]);
		}
	}

	protected class DFA6 extends DFA {

		public DFA6(BaseRecognizer recognizer) {
			this.recognizer = recognizer;
			this.decisionNumber = 6;
			this.eot = DFA6_eot;
			this.eof = DFA6_eof;
			this.min = DFA6_min;
			this.max = DFA6_max;
			this.accept = DFA6_accept;
			this.special = DFA6_special;
			this.transition = DFA6_transition;
		}
		@Override
		public String getDescription() {
			return "1:1: Tokens : ( T__10 | T__11 | T__12 | ENUMBER | RANGE | ID | OR | AND | NOT );";
		}
	}

}
