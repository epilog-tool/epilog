// $ANTLR 3.3 Nov 30, 2010 12:46:29 src/main/java/org/epilogtool/integration/IntegrationGrammar.g 2016-12-26 19:11:54
package org.epilogtool.integration;

import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

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
    public static final int OR=4;
    public static final int AND=5;
    public static final int NOT=6;
    public static final int NUMBER=7;
    public static final int ID=8;
    public static final int SPACE=9;
    public static final int WS=10;

      @Override
      public void reportError(RecognitionException e) {
        throw new RuntimeException("I quit!\n" + e.getMessage()); 
      }


    // delegates
    // delegators

    public IntegrationGrammarLexer() {;} 
    public IntegrationGrammarLexer(CharStream input) {
        this(input, new RecognizerSharedState());
    }
    public IntegrationGrammarLexer(CharStream input, RecognizerSharedState state) {
        super(input,state);

    }
    public String getGrammarFileName() { return "src/main/java/org/epilogtool/integration/IntegrationGrammar.g"; }

    // $ANTLR start "T__11"
    public final void mT__11() throws RecognitionException {
        try {
            int _type = T__11;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // src/main/java/org/epilogtool/integration/IntegrationGrammar.g:10:7: ( '(' )
            // src/main/java/org/epilogtool/integration/IntegrationGrammar.g:10:9: '('
            {
            match('('); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__11"

    // $ANTLR start "T__12"
    public final void mT__12() throws RecognitionException {
        try {
            int _type = T__12;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // src/main/java/org/epilogtool/integration/IntegrationGrammar.g:11:7: ( ')' )
            // src/main/java/org/epilogtool/integration/IntegrationGrammar.g:11:9: ')'
            {
            match(')'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__12"

    // $ANTLR start "T__13"
    public final void mT__13() throws RecognitionException {
        try {
            int _type = T__13;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // src/main/java/org/epilogtool/integration/IntegrationGrammar.g:12:7: ( '{' )
            // src/main/java/org/epilogtool/integration/IntegrationGrammar.g:12:9: '{'
            {
            match('{'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__13"

    // $ANTLR start "T__14"
    public final void mT__14() throws RecognitionException {
        try {
            int _type = T__14;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // src/main/java/org/epilogtool/integration/IntegrationGrammar.g:13:7: ( ',' )
            // src/main/java/org/epilogtool/integration/IntegrationGrammar.g:13:9: ','
            {
            match(','); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__14"

    // $ANTLR start "T__15"
    public final void mT__15() throws RecognitionException {
        try {
            int _type = T__15;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // src/main/java/org/epilogtool/integration/IntegrationGrammar.g:14:7: ( 'min' )
            // src/main/java/org/epilogtool/integration/IntegrationGrammar.g:14:9: 'min'
            {
            match("min"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__15"

    // $ANTLR start "T__16"
    public final void mT__16() throws RecognitionException {
        try {
            int _type = T__16;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // src/main/java/org/epilogtool/integration/IntegrationGrammar.g:15:7: ( '=' )
            // src/main/java/org/epilogtool/integration/IntegrationGrammar.g:15:9: '='
            {
            match('='); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__16"

    // $ANTLR start "T__17"
    public final void mT__17() throws RecognitionException {
        try {
            int _type = T__17;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // src/main/java/org/epilogtool/integration/IntegrationGrammar.g:16:7: ( '}' )
            // src/main/java/org/epilogtool/integration/IntegrationGrammar.g:16:9: '}'
            {
            match('}'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__17"

    // $ANTLR start "T__18"
    public final void mT__18() throws RecognitionException {
        try {
            int _type = T__18;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // src/main/java/org/epilogtool/integration/IntegrationGrammar.g:17:7: ( 'max' )
            // src/main/java/org/epilogtool/integration/IntegrationGrammar.g:17:9: 'max'
            {
            match("max"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__18"

    // $ANTLR start "T__19"
    public final void mT__19() throws RecognitionException {
        try {
            int _type = T__19;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // src/main/java/org/epilogtool/integration/IntegrationGrammar.g:18:7: ( '[' )
            // src/main/java/org/epilogtool/integration/IntegrationGrammar.g:18:9: '['
            {
            match('['); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__19"

    // $ANTLR start "T__20"
    public final void mT__20() throws RecognitionException {
        try {
            int _type = T__20;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // src/main/java/org/epilogtool/integration/IntegrationGrammar.g:19:7: ( ']' )
            // src/main/java/org/epilogtool/integration/IntegrationGrammar.g:19:9: ']'
            {
            match(']'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__20"

    // $ANTLR start "T__21"
    public final void mT__21() throws RecognitionException {
        try {
            int _type = T__21;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // src/main/java/org/epilogtool/integration/IntegrationGrammar.g:20:7: ( ':' )
            // src/main/java/org/epilogtool/integration/IntegrationGrammar.g:20:9: ':'
            {
            match(':'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__21"

    // $ANTLR start "ID"
    public final void mID() throws RecognitionException {
        try {
            int _type = ID;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // src/main/java/org/epilogtool/integration/IntegrationGrammar.g:91:8: ( ( 'a' .. 'z' | 'A' .. 'Z' | '_' ) ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' )* )
            // src/main/java/org/epilogtool/integration/IntegrationGrammar.g:91:10: ( 'a' .. 'z' | 'A' .. 'Z' | '_' ) ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' )*
            {
            if ( (input.LA(1)>='A' && input.LA(1)<='Z')||input.LA(1)=='_'||(input.LA(1)>='a' && input.LA(1)<='z') ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            // src/main/java/org/epilogtool/integration/IntegrationGrammar.g:91:33: ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' )*
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( ((LA1_0>='0' && LA1_0<='9')||(LA1_0>='A' && LA1_0<='Z')||LA1_0=='_'||(LA1_0>='a' && LA1_0<='z')) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // src/main/java/org/epilogtool/integration/IntegrationGrammar.g:
            	    {
            	    if ( (input.LA(1)>='0' && input.LA(1)<='9')||(input.LA(1)>='A' && input.LA(1)<='Z')||input.LA(1)=='_'||(input.LA(1)>='a' && input.LA(1)<='z') ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


            	    }
            	    break;

            	default :
            	    break loop1;
                }
            } while (true);


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "ID"

    // $ANTLR start "NUMBER"
    public final void mNUMBER() throws RecognitionException {
        try {
            int _type = NUMBER;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // src/main/java/org/epilogtool/integration/IntegrationGrammar.g:92:8: ( '0' .. '9' )
            // src/main/java/org/epilogtool/integration/IntegrationGrammar.g:92:10: '0' .. '9'
            {
            matchRange('0','9'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "NUMBER"

    // $ANTLR start "OR"
    public final void mOR() throws RecognitionException {
        try {
            int _type = OR;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // src/main/java/org/epilogtool/integration/IntegrationGrammar.g:93:8: ( '|' )
            // src/main/java/org/epilogtool/integration/IntegrationGrammar.g:93:10: '|'
            {
            match('|'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "OR"

    // $ANTLR start "AND"
    public final void mAND() throws RecognitionException {
        try {
            int _type = AND;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // src/main/java/org/epilogtool/integration/IntegrationGrammar.g:94:8: ( '&' )
            // src/main/java/org/epilogtool/integration/IntegrationGrammar.g:94:10: '&'
            {
            match('&'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "AND"

    // $ANTLR start "NOT"
    public final void mNOT() throws RecognitionException {
        try {
            int _type = NOT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // src/main/java/org/epilogtool/integration/IntegrationGrammar.g:95:8: ( '!' )
            // src/main/java/org/epilogtool/integration/IntegrationGrammar.g:95:10: '!'
            {
            match('!'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "NOT"

    // $ANTLR start "WS"
    public final void mWS() throws RecognitionException {
        try {
            int _type = WS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // src/main/java/org/epilogtool/integration/IntegrationGrammar.g:96:8: ( ( SPACE )+ )
            // src/main/java/org/epilogtool/integration/IntegrationGrammar.g:96:10: ( SPACE )+
            {
            // src/main/java/org/epilogtool/integration/IntegrationGrammar.g:96:10: ( SPACE )+
            int cnt2=0;
            loop2:
            do {
                int alt2=2;
                int LA2_0 = input.LA(1);

                if ( ((LA2_0>='\t' && LA2_0<='\n')||(LA2_0>='\f' && LA2_0<='\r')||LA2_0==' ') ) {
                    alt2=1;
                }


                switch (alt2) {
            	case 1 :
            	    // src/main/java/org/epilogtool/integration/IntegrationGrammar.g:96:10: SPACE
            	    {
            	    mSPACE(); 

            	    }
            	    break;

            	default :
            	    if ( cnt2 >= 1 ) break loop2;
                        EarlyExitException eee =
                            new EarlyExitException(2, input);
                        throw eee;
                }
                cnt2++;
            } while (true);

            skip();

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "WS"

    // $ANTLR start "SPACE"
    public final void mSPACE() throws RecognitionException {
        try {
            // src/main/java/org/epilogtool/integration/IntegrationGrammar.g:97:16: ( '\\t' | ' ' | '\\r' | '\\n' | '\\u000C' )
            // src/main/java/org/epilogtool/integration/IntegrationGrammar.g:
            {
            if ( (input.LA(1)>='\t' && input.LA(1)<='\n')||(input.LA(1)>='\f' && input.LA(1)<='\r')||input.LA(1)==' ' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

        }
        finally {
        }
    }
    // $ANTLR end "SPACE"

    public void mTokens() throws RecognitionException {
        // src/main/java/org/epilogtool/integration/IntegrationGrammar.g:1:8: ( T__11 | T__12 | T__13 | T__14 | T__15 | T__16 | T__17 | T__18 | T__19 | T__20 | T__21 | ID | NUMBER | OR | AND | NOT | WS )
        int alt3=17;
        alt3 = dfa3.predict(input);
        switch (alt3) {
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
                // src/main/java/org/epilogtool/integration/IntegrationGrammar.g:1:76: ID
                {
                mID(); 

                }
                break;
            case 13 :
                // src/main/java/org/epilogtool/integration/IntegrationGrammar.g:1:79: NUMBER
                {
                mNUMBER(); 

                }
                break;
            case 14 :
                // src/main/java/org/epilogtool/integration/IntegrationGrammar.g:1:86: OR
                {
                mOR(); 

                }
                break;
            case 15 :
                // src/main/java/org/epilogtool/integration/IntegrationGrammar.g:1:89: AND
                {
                mAND(); 

                }
                break;
            case 16 :
                // src/main/java/org/epilogtool/integration/IntegrationGrammar.g:1:93: NOT
                {
                mNOT(); 

                }
                break;
            case 17 :
                // src/main/java/org/epilogtool/integration/IntegrationGrammar.g:1:97: WS
                {
                mWS(); 

                }
                break;

        }

    }


    protected DFA3 dfa3 = new DFA3(this);
    static final String DFA3_eotS =
        "\5\uffff\1\13\13\uffff\2\13\1\25\1\26\2\uffff";
    static final String DFA3_eofS =
        "\27\uffff";
    static final String DFA3_minS =
        "\1\11\4\uffff\1\141\13\uffff\1\156\1\170\2\60\2\uffff";
    static final String DFA3_maxS =
        "\1\175\4\uffff\1\151\13\uffff\1\156\1\170\2\172\2\uffff";
    static final String DFA3_acceptS =
        "\1\uffff\1\1\1\2\1\3\1\4\1\uffff\1\6\1\7\1\11\1\12\1\13\1\14\1\15"+
        "\1\16\1\17\1\20\1\21\4\uffff\1\5\1\10";
    static final String DFA3_specialS =
        "\27\uffff}>";
    static final String[] DFA3_transitionS = {
            "\2\20\1\uffff\2\20\22\uffff\1\20\1\17\4\uffff\1\16\1\uffff\1"+
            "\1\1\2\2\uffff\1\4\3\uffff\12\14\1\12\2\uffff\1\6\3\uffff\32"+
            "\13\1\10\1\uffff\1\11\1\uffff\1\13\1\uffff\14\13\1\5\15\13\1"+
            "\3\1\15\1\7",
            "",
            "",
            "",
            "",
            "\1\22\7\uffff\1\21",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\23",
            "\1\24",
            "\12\13\7\uffff\32\13\4\uffff\1\13\1\uffff\32\13",
            "\12\13\7\uffff\32\13\4\uffff\1\13\1\uffff\32\13",
            "",
            ""
    };

    static final short[] DFA3_eot = DFA.unpackEncodedString(DFA3_eotS);
    static final short[] DFA3_eof = DFA.unpackEncodedString(DFA3_eofS);
    static final char[] DFA3_min = DFA.unpackEncodedStringToUnsignedChars(DFA3_minS);
    static final char[] DFA3_max = DFA.unpackEncodedStringToUnsignedChars(DFA3_maxS);
    static final short[] DFA3_accept = DFA.unpackEncodedString(DFA3_acceptS);
    static final short[] DFA3_special = DFA.unpackEncodedString(DFA3_specialS);
    static final short[][] DFA3_transition;

    static {
        int numStates = DFA3_transitionS.length;
        DFA3_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA3_transition[i] = DFA.unpackEncodedString(DFA3_transitionS[i]);
        }
    }

    class DFA3 extends DFA {

        public DFA3(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 3;
            this.eot = DFA3_eot;
            this.eof = DFA3_eof;
            this.min = DFA3_min;
            this.max = DFA3_max;
            this.accept = DFA3_accept;
            this.special = DFA3_special;
            this.transition = DFA3_transition;
        }
        public String getDescription() {
            return "1:1: Tokens : ( T__11 | T__12 | T__13 | T__14 | T__15 | T__16 | T__17 | T__18 | T__19 | T__20 | T__21 | ID | NUMBER | OR | AND | NOT | WS );";
        }
    }
 

}