// $ANTLR 3.3 Nov 30, 2010 12:46:29 src/main/java/org/epilogtool/integration/IntegrationGrammar.g 2016-12-19 16:49:25
package org.epilogtool.integration;

import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

public class IntegrationGrammarLexer extends Lexer {
    public static final int EOF=-1;
    public static final int T__9=9;
    public static final int T__10=10;
    public static final int T__11=11;
    public static final int T__12=12;
    public static final int T__13=13;
    public static final int T__14=14;
    public static final int T__15=15;
    public static final int T__16=16;
    public static final int OR=4;
    public static final int AND=5;
    public static final int NOT=6;
    public static final int NUMBER=7;
    public static final int ID=8;

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

    // $ANTLR start "T__9"
    public final void mT__9() throws RecognitionException {
        try {
            int _type = T__9;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // src/main/java/org/epilogtool/integration/IntegrationGrammar.g:3:6: ( '{' )
            // src/main/java/org/epilogtool/integration/IntegrationGrammar.g:3:8: '{'
            {
            match('{'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__9"

    // $ANTLR start "T__10"
    public final void mT__10() throws RecognitionException {
        try {
            int _type = T__10;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // src/main/java/org/epilogtool/integration/IntegrationGrammar.g:4:7: ( ',' )
            // src/main/java/org/epilogtool/integration/IntegrationGrammar.g:4:9: ','
            {
            match(','); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__10"

    // $ANTLR start "T__11"
    public final void mT__11() throws RecognitionException {
        try {
            int _type = T__11;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // src/main/java/org/epilogtool/integration/IntegrationGrammar.g:5:7: ( 'min=' )
            // src/main/java/org/epilogtool/integration/IntegrationGrammar.g:5:9: 'min='
            {
            match("min="); 


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
            // src/main/java/org/epilogtool/integration/IntegrationGrammar.g:6:7: ( '}' )
            // src/main/java/org/epilogtool/integration/IntegrationGrammar.g:6:9: '}'
            {
            match('}'); 

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
            // src/main/java/org/epilogtool/integration/IntegrationGrammar.g:7:7: ( 'max=' )
            // src/main/java/org/epilogtool/integration/IntegrationGrammar.g:7:9: 'max='
            {
            match("max="); 


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
            // src/main/java/org/epilogtool/integration/IntegrationGrammar.g:8:7: ( '[' )
            // src/main/java/org/epilogtool/integration/IntegrationGrammar.g:8:9: '['
            {
            match('['); 

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
            // src/main/java/org/epilogtool/integration/IntegrationGrammar.g:9:7: ( ']' )
            // src/main/java/org/epilogtool/integration/IntegrationGrammar.g:9:9: ']'
            {
            match(']'); 

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
            // src/main/java/org/epilogtool/integration/IntegrationGrammar.g:10:7: ( ':' )
            // src/main/java/org/epilogtool/integration/IntegrationGrammar.g:10:9: ':'
            {
            match(':'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__16"

    // $ANTLR start "ID"
    public final void mID() throws RecognitionException {
        try {
            int _type = ID;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // src/main/java/org/epilogtool/integration/IntegrationGrammar.g:81:8: ( ( 'a' .. 'z' | 'A' .. 'Z' | '_' ) ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' )* )
            // src/main/java/org/epilogtool/integration/IntegrationGrammar.g:81:10: ( 'a' .. 'z' | 'A' .. 'Z' | '_' ) ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' )*
            {
            if ( (input.LA(1)>='A' && input.LA(1)<='Z')||input.LA(1)=='_'||(input.LA(1)>='a' && input.LA(1)<='z') ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            // src/main/java/org/epilogtool/integration/IntegrationGrammar.g:81:33: ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' )*
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
            // src/main/java/org/epilogtool/integration/IntegrationGrammar.g:82:8: ( '0' .. '9' )
            // src/main/java/org/epilogtool/integration/IntegrationGrammar.g:82:10: '0' .. '9'
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
            // src/main/java/org/epilogtool/integration/IntegrationGrammar.g:83:8: ( '|' )
            // src/main/java/org/epilogtool/integration/IntegrationGrammar.g:83:10: '|'
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
            // src/main/java/org/epilogtool/integration/IntegrationGrammar.g:84:8: ( '&' )
            // src/main/java/org/epilogtool/integration/IntegrationGrammar.g:84:10: '&'
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
            // src/main/java/org/epilogtool/integration/IntegrationGrammar.g:85:8: ( '!' )
            // src/main/java/org/epilogtool/integration/IntegrationGrammar.g:85:10: '!'
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

    public void mTokens() throws RecognitionException {
        // src/main/java/org/epilogtool/integration/IntegrationGrammar.g:1:8: ( T__9 | T__10 | T__11 | T__12 | T__13 | T__14 | T__15 | T__16 | ID | NUMBER | OR | AND | NOT )
        int alt2=13;
        alt2 = dfa2.predict(input);
        switch (alt2) {
            case 1 :
                // src/main/java/org/epilogtool/integration/IntegrationGrammar.g:1:10: T__9
                {
                mT__9(); 

                }
                break;
            case 2 :
                // src/main/java/org/epilogtool/integration/IntegrationGrammar.g:1:15: T__10
                {
                mT__10(); 

                }
                break;
            case 3 :
                // src/main/java/org/epilogtool/integration/IntegrationGrammar.g:1:21: T__11
                {
                mT__11(); 

                }
                break;
            case 4 :
                // src/main/java/org/epilogtool/integration/IntegrationGrammar.g:1:27: T__12
                {
                mT__12(); 

                }
                break;
            case 5 :
                // src/main/java/org/epilogtool/integration/IntegrationGrammar.g:1:33: T__13
                {
                mT__13(); 

                }
                break;
            case 6 :
                // src/main/java/org/epilogtool/integration/IntegrationGrammar.g:1:39: T__14
                {
                mT__14(); 

                }
                break;
            case 7 :
                // src/main/java/org/epilogtool/integration/IntegrationGrammar.g:1:45: T__15
                {
                mT__15(); 

                }
                break;
            case 8 :
                // src/main/java/org/epilogtool/integration/IntegrationGrammar.g:1:51: T__16
                {
                mT__16(); 

                }
                break;
            case 9 :
                // src/main/java/org/epilogtool/integration/IntegrationGrammar.g:1:57: ID
                {
                mID(); 

                }
                break;
            case 10 :
                // src/main/java/org/epilogtool/integration/IntegrationGrammar.g:1:60: NUMBER
                {
                mNUMBER(); 

                }
                break;
            case 11 :
                // src/main/java/org/epilogtool/integration/IntegrationGrammar.g:1:67: OR
                {
                mOR(); 

                }
                break;
            case 12 :
                // src/main/java/org/epilogtool/integration/IntegrationGrammar.g:1:70: AND
                {
                mAND(); 

                }
                break;
            case 13 :
                // src/main/java/org/epilogtool/integration/IntegrationGrammar.g:1:74: NOT
                {
                mNOT(); 

                }
                break;

        }

    }


    protected DFA2 dfa2 = new DFA2(this);
    static final String DFA2_eotS =
        "\3\uffff\1\10\11\uffff\4\10\2\uffff";
    static final String DFA2_eofS =
        "\23\uffff";
    static final String DFA2_minS =
        "\1\41\2\uffff\1\141\11\uffff\1\156\1\170\2\75\2\uffff";
    static final String DFA2_maxS =
        "\1\175\2\uffff\1\151\11\uffff\1\156\1\170\2\75\2\uffff";
    static final String DFA2_acceptS =
        "\1\uffff\1\1\1\2\1\uffff\1\4\1\6\1\7\1\10\1\11\1\12\1\13\1\14\1"+
        "\15\4\uffff\1\3\1\5";
    static final String DFA2_specialS =
        "\23\uffff}>";
    static final String[] DFA2_transitionS = {
            "\1\14\4\uffff\1\13\5\uffff\1\2\3\uffff\12\11\1\7\6\uffff\32"+
            "\10\1\5\1\uffff\1\6\1\uffff\1\10\1\uffff\14\10\1\3\15\10\1\1"+
            "\1\12\1\4",
            "",
            "",
            "\1\16\7\uffff\1\15",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\17",
            "\1\20",
            "\1\21",
            "\1\22",
            "",
            ""
    };

    static final short[] DFA2_eot = DFA.unpackEncodedString(DFA2_eotS);
    static final short[] DFA2_eof = DFA.unpackEncodedString(DFA2_eofS);
    static final char[] DFA2_min = DFA.unpackEncodedStringToUnsignedChars(DFA2_minS);
    static final char[] DFA2_max = DFA.unpackEncodedStringToUnsignedChars(DFA2_maxS);
    static final short[] DFA2_accept = DFA.unpackEncodedString(DFA2_acceptS);
    static final short[] DFA2_special = DFA.unpackEncodedString(DFA2_specialS);
    static final short[][] DFA2_transition;

    static {
        int numStates = DFA2_transitionS.length;
        DFA2_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA2_transition[i] = DFA.unpackEncodedString(DFA2_transitionS[i]);
        }
    }

    class DFA2 extends DFA {

        public DFA2(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 2;
            this.eot = DFA2_eot;
            this.eof = DFA2_eof;
            this.min = DFA2_min;
            this.max = DFA2_max;
            this.accept = DFA2_accept;
            this.special = DFA2_special;
            this.transition = DFA2_transition;
        }
        public String getDescription() {
            return "1:1: Tokens : ( T__9 | T__10 | T__11 | T__12 | T__13 | T__14 | T__15 | T__16 | ID | NUMBER | OR | AND | NOT );";
        }
    }
 

}