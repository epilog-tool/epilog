// $ANTLR 3.3 Nov 30, 2010 12:46:29 src/main/java/org/epilogtool/cellularevent/CEGrammar.g 2016-12-22 14:50:06
package org.epilogtool.cellularevent;

import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

public class CEGrammarLexer extends Lexer {
    public static final int EOF=-1;
    public static final int T__9=9;
    public static final int OR=4;
    public static final int AND=5;
    public static final int NOT=6;
    public static final int ID=7;
    public static final int NUMBER=8;

    // delegates
    // delegators

    public CEGrammarLexer() {;} 
    public CEGrammarLexer(CharStream input) {
        this(input, new RecognizerSharedState());
    }
    public CEGrammarLexer(CharStream input, RecognizerSharedState state) {
        super(input,state);

    }
    public String getGrammarFileName() { return "src/main/java/org/epilogtool/cellularevent/CEGrammar.g"; }

    // $ANTLR start "T__9"
    public final void mT__9() throws RecognitionException {
        try {
            int _type = T__9;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // src/main/java/org/epilogtool/cellularevent/CEGrammar.g:3:6: ( ':' )
            // src/main/java/org/epilogtool/cellularevent/CEGrammar.g:3:8: ':'
            {
            match(':'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__9"

    // $ANTLR start "ID"
    public final void mID() throws RecognitionException {
        try {
            int _type = ID;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // src/main/java/org/epilogtool/cellularevent/CEGrammar.g:39:8: ( ( 'a' .. 'z' | 'A' .. 'Z' | '_' ) ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' )* )
            // src/main/java/org/epilogtool/cellularevent/CEGrammar.g:39:10: ( 'a' .. 'z' | 'A' .. 'Z' | '_' ) ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' )*
            {
            if ( (input.LA(1)>='A' && input.LA(1)<='Z')||input.LA(1)=='_'||(input.LA(1)>='a' && input.LA(1)<='z') ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            // src/main/java/org/epilogtool/cellularevent/CEGrammar.g:39:33: ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' )*
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( ((LA1_0>='0' && LA1_0<='9')||(LA1_0>='A' && LA1_0<='Z')||LA1_0=='_'||(LA1_0>='a' && LA1_0<='z')) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // src/main/java/org/epilogtool/cellularevent/CEGrammar.g:
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
            // src/main/java/org/epilogtool/cellularevent/CEGrammar.g:40:8: ( '0' .. '9' )
            // src/main/java/org/epilogtool/cellularevent/CEGrammar.g:40:10: '0' .. '9'
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
            // src/main/java/org/epilogtool/cellularevent/CEGrammar.g:41:8: ( '|' )
            // src/main/java/org/epilogtool/cellularevent/CEGrammar.g:41:10: '|'
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
            // src/main/java/org/epilogtool/cellularevent/CEGrammar.g:42:8: ( '&' )
            // src/main/java/org/epilogtool/cellularevent/CEGrammar.g:42:10: '&'
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
            // src/main/java/org/epilogtool/cellularevent/CEGrammar.g:43:8: ( '!' )
            // src/main/java/org/epilogtool/cellularevent/CEGrammar.g:43:10: '!'
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
        // src/main/java/org/epilogtool/cellularevent/CEGrammar.g:1:8: ( T__9 | ID | NUMBER | OR | AND | NOT )
        int alt2=6;
        switch ( input.LA(1) ) {
        case ':':
            {
            alt2=1;
            }
            break;
        case 'A':
        case 'B':
        case 'C':
        case 'D':
        case 'E':
        case 'F':
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
        case 'T':
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
            alt2=2;
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
            alt2=3;
            }
            break;
        case '|':
            {
            alt2=4;
            }
            break;
        case '&':
            {
            alt2=5;
            }
            break;
        case '!':
            {
            alt2=6;
            }
            break;
        default:
            NoViableAltException nvae =
                new NoViableAltException("", 2, 0, input);

            throw nvae;
        }

        switch (alt2) {
            case 1 :
                // src/main/java/org/epilogtool/cellularevent/CEGrammar.g:1:10: T__9
                {
                mT__9(); 

                }
                break;
            case 2 :
                // src/main/java/org/epilogtool/cellularevent/CEGrammar.g:1:15: ID
                {
                mID(); 

                }
                break;
            case 3 :
                // src/main/java/org/epilogtool/cellularevent/CEGrammar.g:1:18: NUMBER
                {
                mNUMBER(); 

                }
                break;
            case 4 :
                // src/main/java/org/epilogtool/cellularevent/CEGrammar.g:1:25: OR
                {
                mOR(); 

                }
                break;
            case 5 :
                // src/main/java/org/epilogtool/cellularevent/CEGrammar.g:1:28: AND
                {
                mAND(); 

                }
                break;
            case 6 :
                // src/main/java/org/epilogtool/cellularevent/CEGrammar.g:1:32: NOT
                {
                mNOT(); 

                }
                break;

        }

    }


 

}