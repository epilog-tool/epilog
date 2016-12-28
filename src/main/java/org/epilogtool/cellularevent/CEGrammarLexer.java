// $ANTLR 3.3 Nov 30, 2010 12:46:29 src/main/java/org/epilogtool/cellularevent/CEGrammar.g 2016-12-27 15:04:23
package org.epilogtool.cellularevent;

import org.antlr.runtime.*;

public class CEGrammarLexer extends Lexer {
    public static final int EOF=-1;
    public static final int T__11=11;
    public static final int T__12=12;
    public static final int T__13=13;
    public static final int OR=4;
    public static final int AND=5;
    public static final int NOT=6;
    public static final int ID=7;
    public static final int NUMBER=8;
    public static final int SPACE=9;
    public static final int WS=10;

      @Override
      public void reportError(RecognitionException e) {
        throw new RuntimeException("I quit!\n" + e.getMessage());
      }


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

    // $ANTLR start "T__11"
    public final void mT__11() throws RecognitionException {
        try {
            int _type = T__11;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // src/main/java/org/epilogtool/cellularevent/CEGrammar.g:10:7: ( '(' )
            // src/main/java/org/epilogtool/cellularevent/CEGrammar.g:10:9: '('
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
            // src/main/java/org/epilogtool/cellularevent/CEGrammar.g:11:7: ( ')' )
            // src/main/java/org/epilogtool/cellularevent/CEGrammar.g:11:9: ')'
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
            // src/main/java/org/epilogtool/cellularevent/CEGrammar.g:12:7: ( ':' )
            // src/main/java/org/epilogtool/cellularevent/CEGrammar.g:12:9: ':'
            {
            match(':'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__13"

    // $ANTLR start "ID"
    public final void mID() throws RecognitionException {
        try {
            int _type = ID;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // src/main/java/org/epilogtool/cellularevent/CEGrammar.g:61:8: ( ( 'a' .. 'z' | 'A' .. 'Z' | '_' ) ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' )* )
            // src/main/java/org/epilogtool/cellularevent/CEGrammar.g:61:10: ( 'a' .. 'z' | 'A' .. 'Z' | '_' ) ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' )*
            {
            if ( (input.LA(1)>='A' && input.LA(1)<='Z')||input.LA(1)=='_'||(input.LA(1)>='a' && input.LA(1)<='z') ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            // src/main/java/org/epilogtool/cellularevent/CEGrammar.g:61:33: ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' )*
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
            // src/main/java/org/epilogtool/cellularevent/CEGrammar.g:62:8: ( '0' .. '9' )
            // src/main/java/org/epilogtool/cellularevent/CEGrammar.g:62:10: '0' .. '9'
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
            // src/main/java/org/epilogtool/cellularevent/CEGrammar.g:63:8: ( '|' )
            // src/main/java/org/epilogtool/cellularevent/CEGrammar.g:63:10: '|'
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
            // src/main/java/org/epilogtool/cellularevent/CEGrammar.g:64:8: ( '&' )
            // src/main/java/org/epilogtool/cellularevent/CEGrammar.g:64:10: '&'
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
            // src/main/java/org/epilogtool/cellularevent/CEGrammar.g:65:8: ( '!' )
            // src/main/java/org/epilogtool/cellularevent/CEGrammar.g:65:10: '!'
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
            // src/main/java/org/epilogtool/cellularevent/CEGrammar.g:66:8: ( ( SPACE )+ )
            // src/main/java/org/epilogtool/cellularevent/CEGrammar.g:66:10: ( SPACE )+
            {
            // src/main/java/org/epilogtool/cellularevent/CEGrammar.g:66:10: ( SPACE )+
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
            	    // src/main/java/org/epilogtool/cellularevent/CEGrammar.g:66:10: SPACE
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
            // src/main/java/org/epilogtool/cellularevent/CEGrammar.g:67:16: ( '\\t' | ' ' | '\\r' | '\\n' | '\\u000C' )
            // src/main/java/org/epilogtool/cellularevent/CEGrammar.g:
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
        // src/main/java/org/epilogtool/cellularevent/CEGrammar.g:1:8: ( T__11 | T__12 | T__13 | ID | NUMBER | OR | AND | NOT | WS )
        int alt3=9;
        switch ( input.LA(1) ) {
        case '(':
            {
            alt3=1;
            }
            break;
        case ')':
            {
            alt3=2;
            }
            break;
        case ':':
            {
            alt3=3;
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
            alt3=4;
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
            alt3=5;
            }
            break;
        case '|':
            {
            alt3=6;
            }
            break;
        case '&':
            {
            alt3=7;
            }
            break;
        case '!':
            {
            alt3=8;
            }
            break;
        case '\t':
        case '\n':
        case '\f':
        case '\r':
        case ' ':
            {
            alt3=9;
            }
            break;
        default:
            NoViableAltException nvae =
                new NoViableAltException("", 3, 0, input);

            throw nvae;
        }

        switch (alt3) {
            case 1 :
                // src/main/java/org/epilogtool/cellularevent/CEGrammar.g:1:10: T__11
                {
                mT__11(); 

                }
                break;
            case 2 :
                // src/main/java/org/epilogtool/cellularevent/CEGrammar.g:1:16: T__12
                {
                mT__12(); 

                }
                break;
            case 3 :
                // src/main/java/org/epilogtool/cellularevent/CEGrammar.g:1:22: T__13
                {
                mT__13(); 

                }
                break;
            case 4 :
                // src/main/java/org/epilogtool/cellularevent/CEGrammar.g:1:28: ID
                {
                mID(); 

                }
                break;
            case 5 :
                // src/main/java/org/epilogtool/cellularevent/CEGrammar.g:1:31: NUMBER
                {
                mNUMBER(); 

                }
                break;
            case 6 :
                // src/main/java/org/epilogtool/cellularevent/CEGrammar.g:1:38: OR
                {
                mOR(); 

                }
                break;
            case 7 :
                // src/main/java/org/epilogtool/cellularevent/CEGrammar.g:1:41: AND
                {
                mAND(); 

                }
                break;
            case 8 :
                // src/main/java/org/epilogtool/cellularevent/CEGrammar.g:1:45: NOT
                {
                mNOT(); 

                }
                break;
            case 9 :
                // src/main/java/org/epilogtool/cellularevent/CEGrammar.g:1:49: WS
                {
                mWS(); 

                }
                break;

        }

    }


 

}