// $ANTLR 3.3 Nov 30, 2010 12:46:29 src/main/java/org/epilogtool/integration/IntegrationGrammar.g 2016-12-19 16:49:25

package org.epilogtool.integration;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

public class IntegrationGrammarParser extends Parser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "OR", "AND", "NOT", "NUMBER", "ID", "'{'", "','", "'min='", "'}'", "'max='", "'['", "']'", "':'"
    };
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


        public IntegrationGrammarParser(TokenStream input) {
            this(input, new RecognizerSharedState());
        }
        public IntegrationGrammarParser(TokenStream input, RecognizerSharedState state) {
            super(input, state);
             
        }
        

    public String[] getTokenNames() { return IntegrationGrammarParser.tokenNames; }
    public String getGrammarFileName() { return "src/main/java/org/epilogtool/integration/IntegrationGrammar.g"; }



    // $ANTLR start "eval"
    // src/main/java/org/epilogtool/integration/IntegrationGrammar.g:7:1: eval returns [IntegrationFunctionExpression value] : exp= functionexpror ;
    public final IntegrationFunctionExpression eval() throws RecognitionException {
        IntegrationFunctionExpression value = null;

        IntegrationFunctionExpression exp = null;


        try {
            // src/main/java/org/epilogtool/integration/IntegrationGrammar.g:11:2: (exp= functionexpror )
            // src/main/java/org/epilogtool/integration/IntegrationGrammar.g:11:4: exp= functionexpror
            {
            pushFollow(FOLLOW_functionexpror_in_eval28);
            exp=functionexpror();

            state._fsp--;

             value = exp; 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return value;
    }
    // $ANTLR end "eval"


    // $ANTLR start "functionexpror"
    // src/main/java/org/epilogtool/integration/IntegrationGrammar.g:25:1: functionexpror returns [IntegrationFunctionExpression value] : o1= functionexprand ( OR o2= functionexprand )* ;
    public final IntegrationFunctionExpression functionexpror() throws RecognitionException {
        IntegrationFunctionExpression value = null;

        IntegrationFunctionExpression o1 = null;

        IntegrationFunctionExpression o2 = null;


        try {
            // src/main/java/org/epilogtool/integration/IntegrationGrammar.g:26:2: (o1= functionexprand ( OR o2= functionexprand )* )
            // src/main/java/org/epilogtool/integration/IntegrationGrammar.g:26:4: o1= functionexprand ( OR o2= functionexprand )*
            {
            pushFollow(FOLLOW_functionexprand_in_functionexpror48);
            o1=functionexprand();

            state._fsp--;

            // src/main/java/org/epilogtool/integration/IntegrationGrammar.g:26:23: ( OR o2= functionexprand )*
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( (LA1_0==OR) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // src/main/java/org/epilogtool/integration/IntegrationGrammar.g:26:24: OR o2= functionexprand
            	    {
            	    match(input,OR,FOLLOW_OR_in_functionexpror51); 
            	    pushFollow(FOLLOW_functionexprand_in_functionexpror55);
            	    o2=functionexprand();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop1;
                }
            } while (true);

             value = IFSpecification.integrationFunctionOperationOR(o1,o2); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return value;
    }
    // $ANTLR end "functionexpror"


    // $ANTLR start "functionexprand"
    // src/main/java/org/epilogtool/integration/IntegrationGrammar.g:30:1: functionexprand returns [IntegrationFunctionExpression value] : a1= functionexprnot ( AND a2= functionexprnot )* ;
    public final IntegrationFunctionExpression functionexprand() throws RecognitionException {
        IntegrationFunctionExpression value = null;

        IntegrationFunctionExpression a1 = null;

        IntegrationFunctionExpression a2 = null;


        try {
            // src/main/java/org/epilogtool/integration/IntegrationGrammar.g:31:2: (a1= functionexprnot ( AND a2= functionexprnot )* )
            // src/main/java/org/epilogtool/integration/IntegrationGrammar.g:31:4: a1= functionexprnot ( AND a2= functionexprnot )*
            {
            pushFollow(FOLLOW_functionexprnot_in_functionexprand79);
            a1=functionexprnot();

            state._fsp--;

            // src/main/java/org/epilogtool/integration/IntegrationGrammar.g:31:23: ( AND a2= functionexprnot )*
            loop2:
            do {
                int alt2=2;
                int LA2_0 = input.LA(1);

                if ( (LA2_0==AND) ) {
                    alt2=1;
                }


                switch (alt2) {
            	case 1 :
            	    // src/main/java/org/epilogtool/integration/IntegrationGrammar.g:31:24: AND a2= functionexprnot
            	    {
            	    match(input,AND,FOLLOW_AND_in_functionexprand82); 
            	    pushFollow(FOLLOW_functionexprnot_in_functionexprand86);
            	    a2=functionexprnot();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop2;
                }
            } while (true);

             value = IFSpecification.integrationFunctionOperationAND(a1,a2); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return value;
    }
    // $ANTLR end "functionexprand"


    // $ANTLR start "functionexprnot"
    // src/main/java/org/epilogtool/integration/IntegrationGrammar.g:35:1: functionexprnot returns [IntegrationFunctionExpression value] : (expr= cardconst | NOT expr= cardconst );
    public final IntegrationFunctionExpression functionexprnot() throws RecognitionException {
        IntegrationFunctionExpression value = null;

        IntegrationFunctionExpression expr = null;


        try {
            // src/main/java/org/epilogtool/integration/IntegrationGrammar.g:36:2: (expr= cardconst | NOT expr= cardconst )
            int alt3=2;
            int LA3_0 = input.LA(1);

            if ( (LA3_0==9) ) {
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
                    // src/main/java/org/epilogtool/integration/IntegrationGrammar.g:36:4: expr= cardconst
                    {
                    pushFollow(FOLLOW_cardconst_in_functionexprnot109);
                    expr=cardconst();

                    state._fsp--;

                     value = expr; 

                    }
                    break;
                case 2 :
                    // src/main/java/org/epilogtool/integration/IntegrationGrammar.g:38:4: NOT expr= cardconst
                    {
                    match(input,NOT,FOLLOW_NOT_in_functionexprnot118); 
                    pushFollow(FOLLOW_cardconst_in_functionexprnot122);
                    expr=cardconst();

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
        }
        return value;
    }
    // $ANTLR end "functionexprnot"


    // $ANTLR start "cardconst"
    // src/main/java/org/epilogtool/integration/IntegrationGrammar.g:42:1: cardconst returns [IntegrationFunctionExpression value ] : ( '{' expr= signalexpror ',' 'min=' min= NUMBER '}' | '{' expr= signalexpror ',' 'max=' max= NUMBER '}' | '{' expr= signalexpror ',' 'min=' min= NUMBER ',' 'max=' max= NUMBER '}' );
    public final IntegrationFunctionExpression cardconst() throws RecognitionException {
        IntegrationFunctionExpression value = null;

        Token min=null;
        Token max=null;
        IntegrationSignalExpression expr = null;


        try {
            // src/main/java/org/epilogtool/integration/IntegrationGrammar.g:43:2: ( '{' expr= signalexpror ',' 'min=' min= NUMBER '}' | '{' expr= signalexpror ',' 'max=' max= NUMBER '}' | '{' expr= signalexpror ',' 'min=' min= NUMBER ',' 'max=' max= NUMBER '}' )
            int alt4=3;
            alt4 = dfa4.predict(input);
            switch (alt4) {
                case 1 :
                    // src/main/java/org/epilogtool/integration/IntegrationGrammar.g:43:4: '{' expr= signalexpror ',' 'min=' min= NUMBER '}'
                    {
                    match(input,9,FOLLOW_9_in_cardconst141); 
                    pushFollow(FOLLOW_signalexpror_in_cardconst145);
                    expr=signalexpror();

                    state._fsp--;

                    match(input,10,FOLLOW_10_in_cardconst147); 
                    match(input,11,FOLLOW_11_in_cardconst149); 
                    min=(Token)match(input,NUMBER,FOLLOW_NUMBER_in_cardconst153); 
                    match(input,12,FOLLOW_12_in_cardconst155); 
                     value = IFSpecification.cardinalityConstraint(expr, (min!=null?min.getText():null), "-1"); 

                    }
                    break;
                case 2 :
                    // src/main/java/org/epilogtool/integration/IntegrationGrammar.g:45:4: '{' expr= signalexpror ',' 'max=' max= NUMBER '}'
                    {
                    match(input,9,FOLLOW_9_in_cardconst164); 
                    pushFollow(FOLLOW_signalexpror_in_cardconst168);
                    expr=signalexpror();

                    state._fsp--;

                    match(input,10,FOLLOW_10_in_cardconst170); 
                    match(input,13,FOLLOW_13_in_cardconst172); 
                    max=(Token)match(input,NUMBER,FOLLOW_NUMBER_in_cardconst176); 
                    match(input,12,FOLLOW_12_in_cardconst178); 
                     value = IFSpecification.cardinalityConstraint(expr, "-1", (max!=null?max.getText():null)); 

                    }
                    break;
                case 3 :
                    // src/main/java/org/epilogtool/integration/IntegrationGrammar.g:47:4: '{' expr= signalexpror ',' 'min=' min= NUMBER ',' 'max=' max= NUMBER '}'
                    {
                    match(input,9,FOLLOW_9_in_cardconst187); 
                    pushFollow(FOLLOW_signalexpror_in_cardconst191);
                    expr=signalexpror();

                    state._fsp--;

                    match(input,10,FOLLOW_10_in_cardconst193); 
                    match(input,11,FOLLOW_11_in_cardconst195); 
                    min=(Token)match(input,NUMBER,FOLLOW_NUMBER_in_cardconst199); 
                    match(input,10,FOLLOW_10_in_cardconst201); 
                    match(input,13,FOLLOW_13_in_cardconst203); 
                    max=(Token)match(input,NUMBER,FOLLOW_NUMBER_in_cardconst207); 
                    match(input,12,FOLLOW_12_in_cardconst209); 
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
        }
        return value;
    }
    // $ANTLR end "cardconst"


    // $ANTLR start "signalexpror"
    // src/main/java/org/epilogtool/integration/IntegrationGrammar.g:51:1: signalexpror returns [IntegrationSignalExpression value] : o1= signal ( OR o2= signal )* ;
    public final IntegrationSignalExpression signalexpror() throws RecognitionException {
        IntegrationSignalExpression value = null;

        IntegrationSignal o1 = null;

        IntegrationSignal o2 = null;


        try {
            // src/main/java/org/epilogtool/integration/IntegrationGrammar.g:52:2: (o1= signal ( OR o2= signal )* )
            // src/main/java/org/epilogtool/integration/IntegrationGrammar.g:52:4: o1= signal ( OR o2= signal )*
            {
            pushFollow(FOLLOW_signal_in_signalexpror230);
            o1=signal();

            state._fsp--;

            // src/main/java/org/epilogtool/integration/IntegrationGrammar.g:52:14: ( OR o2= signal )*
            loop5:
            do {
                int alt5=2;
                int LA5_0 = input.LA(1);

                if ( (LA5_0==OR) ) {
                    alt5=1;
                }


                switch (alt5) {
            	case 1 :
            	    // src/main/java/org/epilogtool/integration/IntegrationGrammar.g:52:15: OR o2= signal
            	    {
            	    match(input,OR,FOLLOW_OR_in_signalexpror233); 
            	    pushFollow(FOLLOW_signal_in_signalexpror237);
            	    o2=signal();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop5;
                }
            } while (true);

             value = IFSpecification.integrationSignalOperationOR(o1,o2); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return value;
    }
    // $ANTLR end "signalexpror"


    // $ANTLR start "signal"
    // src/main/java/org/epilogtool/integration/IntegrationGrammar.g:56:1: signal returns [IntegrationSignal value] : (id= ID | id= ID '[' min= NUMBER ']' | id= ID '[' min= NUMBER ':' ']' | id= ID '[' min= NUMBER ':' max= NUMBER ']' | id= ID '[' ':' max= NUMBER ']' | id= ID ':' t= NUMBER | id= ID ':' t= NUMBER '[' min= NUMBER ']' | id= ID ':' t= NUMBER '[' min= NUMBER ':' ']' | id= ID ':' t= NUMBER '[' min= NUMBER ':' max= NUMBER ']' | id= ID ':' t= NUMBER '[' ':' max= NUMBER ']' );
    public final IntegrationSignal signal() throws RecognitionException {
        IntegrationSignal value = null;

        Token id=null;
        Token min=null;
        Token max=null;
        Token t=null;

        try {
            // src/main/java/org/epilogtool/integration/IntegrationGrammar.g:57:2: (id= ID | id= ID '[' min= NUMBER ']' | id= ID '[' min= NUMBER ':' ']' | id= ID '[' min= NUMBER ':' max= NUMBER ']' | id= ID '[' ':' max= NUMBER ']' | id= ID ':' t= NUMBER | id= ID ':' t= NUMBER '[' min= NUMBER ']' | id= ID ':' t= NUMBER '[' min= NUMBER ':' ']' | id= ID ':' t= NUMBER '[' min= NUMBER ':' max= NUMBER ']' | id= ID ':' t= NUMBER '[' ':' max= NUMBER ']' )
            int alt6=10;
            alt6 = dfa6.predict(input);
            switch (alt6) {
                case 1 :
                    // src/main/java/org/epilogtool/integration/IntegrationGrammar.g:57:4: id= ID
                    {
                    id=(Token)match(input,ID,FOLLOW_ID_in_signal261); 
                     value = IFSpecification.integrationSignal((id!=null?id.getText():null), "1", new IntegrationDistance("1", "1")); 

                    }
                    break;
                case 2 :
                    // src/main/java/org/epilogtool/integration/IntegrationGrammar.g:59:4: id= ID '[' min= NUMBER ']'
                    {
                    id=(Token)match(input,ID,FOLLOW_ID_in_signal272); 
                    match(input,14,FOLLOW_14_in_signal274); 
                    min=(Token)match(input,NUMBER,FOLLOW_NUMBER_in_signal278); 
                    match(input,15,FOLLOW_15_in_signal280); 
                     value = IFSpecification.integrationSignal((id!=null?id.getText():null), "1", new IntegrationDistance((min!=null?min.getText():null), (min!=null?min.getText():null))); 

                    }
                    break;
                case 3 :
                    // src/main/java/org/epilogtool/integration/IntegrationGrammar.g:61:4: id= ID '[' min= NUMBER ':' ']'
                    {
                    id=(Token)match(input,ID,FOLLOW_ID_in_signal291); 
                    match(input,14,FOLLOW_14_in_signal293); 
                    min=(Token)match(input,NUMBER,FOLLOW_NUMBER_in_signal297); 
                    match(input,16,FOLLOW_16_in_signal299); 
                    match(input,15,FOLLOW_15_in_signal301); 
                     value = IFSpecification.integrationSignal((id!=null?id.getText():null), "1", new IntegrationDistance((min!=null?min.getText():null), "-1")); 

                    }
                    break;
                case 4 :
                    // src/main/java/org/epilogtool/integration/IntegrationGrammar.g:63:4: id= ID '[' min= NUMBER ':' max= NUMBER ']'
                    {
                    id=(Token)match(input,ID,FOLLOW_ID_in_signal312); 
                    match(input,14,FOLLOW_14_in_signal314); 
                    min=(Token)match(input,NUMBER,FOLLOW_NUMBER_in_signal318); 
                    match(input,16,FOLLOW_16_in_signal320); 
                    max=(Token)match(input,NUMBER,FOLLOW_NUMBER_in_signal324); 
                    match(input,15,FOLLOW_15_in_signal326); 
                     value = IFSpecification.integrationSignal((id!=null?id.getText():null), "1", new IntegrationDistance((min!=null?min.getText():null), (max!=null?max.getText():null))); 

                    }
                    break;
                case 5 :
                    // src/main/java/org/epilogtool/integration/IntegrationGrammar.g:65:4: id= ID '[' ':' max= NUMBER ']'
                    {
                    id=(Token)match(input,ID,FOLLOW_ID_in_signal337); 
                    match(input,14,FOLLOW_14_in_signal339); 
                    match(input,16,FOLLOW_16_in_signal341); 
                    max=(Token)match(input,NUMBER,FOLLOW_NUMBER_in_signal345); 
                    match(input,15,FOLLOW_15_in_signal347); 
                     value = IFSpecification.integrationSignal((id!=null?id.getText():null), "1", new IntegrationDistance("-1", (max!=null?max.getText():null))); 

                    }
                    break;
                case 6 :
                    // src/main/java/org/epilogtool/integration/IntegrationGrammar.g:67:4: id= ID ':' t= NUMBER
                    {
                    id=(Token)match(input,ID,FOLLOW_ID_in_signal358); 
                    match(input,16,FOLLOW_16_in_signal360); 
                    t=(Token)match(input,NUMBER,FOLLOW_NUMBER_in_signal364); 
                     value = IFSpecification.integrationSignal((id!=null?id.getText():null), (t!=null?t.getText():null), new IntegrationDistance("1", "1")); 

                    }
                    break;
                case 7 :
                    // src/main/java/org/epilogtool/integration/IntegrationGrammar.g:69:4: id= ID ':' t= NUMBER '[' min= NUMBER ']'
                    {
                    id=(Token)match(input,ID,FOLLOW_ID_in_signal375); 
                    match(input,16,FOLLOW_16_in_signal377); 
                    t=(Token)match(input,NUMBER,FOLLOW_NUMBER_in_signal381); 
                    match(input,14,FOLLOW_14_in_signal383); 
                    min=(Token)match(input,NUMBER,FOLLOW_NUMBER_in_signal387); 
                    match(input,15,FOLLOW_15_in_signal389); 
                     value = IFSpecification.integrationSignal((id!=null?id.getText():null), (t!=null?t.getText():null), new IntegrationDistance((min!=null?min.getText():null), (min!=null?min.getText():null))); 

                    }
                    break;
                case 8 :
                    // src/main/java/org/epilogtool/integration/IntegrationGrammar.g:71:4: id= ID ':' t= NUMBER '[' min= NUMBER ':' ']'
                    {
                    id=(Token)match(input,ID,FOLLOW_ID_in_signal400); 
                    match(input,16,FOLLOW_16_in_signal402); 
                    t=(Token)match(input,NUMBER,FOLLOW_NUMBER_in_signal406); 
                    match(input,14,FOLLOW_14_in_signal408); 
                    min=(Token)match(input,NUMBER,FOLLOW_NUMBER_in_signal412); 
                    match(input,16,FOLLOW_16_in_signal414); 
                    match(input,15,FOLLOW_15_in_signal416); 
                     value = IFSpecification.integrationSignal((id!=null?id.getText():null), (t!=null?t.getText():null), new IntegrationDistance((min!=null?min.getText():null), "-1")); 

                    }
                    break;
                case 9 :
                    // src/main/java/org/epilogtool/integration/IntegrationGrammar.g:73:4: id= ID ':' t= NUMBER '[' min= NUMBER ':' max= NUMBER ']'
                    {
                    id=(Token)match(input,ID,FOLLOW_ID_in_signal427); 
                    match(input,16,FOLLOW_16_in_signal429); 
                    t=(Token)match(input,NUMBER,FOLLOW_NUMBER_in_signal433); 
                    match(input,14,FOLLOW_14_in_signal435); 
                    min=(Token)match(input,NUMBER,FOLLOW_NUMBER_in_signal439); 
                    match(input,16,FOLLOW_16_in_signal441); 
                    max=(Token)match(input,NUMBER,FOLLOW_NUMBER_in_signal445); 
                    match(input,15,FOLLOW_15_in_signal447); 
                     value = IFSpecification.integrationSignal((id!=null?id.getText():null), (t!=null?t.getText():null), new IntegrationDistance((min!=null?min.getText():null), (max!=null?max.getText():null))); 

                    }
                    break;
                case 10 :
                    // src/main/java/org/epilogtool/integration/IntegrationGrammar.g:75:4: id= ID ':' t= NUMBER '[' ':' max= NUMBER ']'
                    {
                    id=(Token)match(input,ID,FOLLOW_ID_in_signal458); 
                    match(input,16,FOLLOW_16_in_signal460); 
                    t=(Token)match(input,NUMBER,FOLLOW_NUMBER_in_signal464); 
                    match(input,14,FOLLOW_14_in_signal466); 
                    match(input,16,FOLLOW_16_in_signal468); 
                    max=(Token)match(input,NUMBER,FOLLOW_NUMBER_in_signal472); 
                    match(input,15,FOLLOW_15_in_signal474); 
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
        }
        return value;
    }
    // $ANTLR end "signal"

    // Delegated rules


    protected DFA4 dfa4 = new DFA4(this);
    protected DFA6 dfa6 = new DFA6(this);
    static final String DFA4_eotS =
        "\67\uffff";
    static final String DFA4_eofS =
        "\67\uffff";
    static final String DFA4_minS =
        "\1\11\1\10\1\4\2\7\1\10\1\13\1\17\1\7\2\4\1\7\1\uffff\1\4\1\7\1"+
        "\17\3\7\1\12\1\4\1\17\1\4\1\17\1\7\1\17\1\7\1\4\2\uffff\2\4\1\7"+
        "\1\17\1\4\1\7\1\17\1\7\1\4\1\17\2\4\1\17\1\4\1\17\1\7\3\4\1\7\1"+
        "\17\1\4\1\17\2\4";
    static final String DFA4_maxS =
        "\1\11\1\10\2\20\1\7\1\10\1\15\1\20\1\7\1\16\1\20\1\7\1\uffff\1\12"+
        "\2\17\2\20\1\7\1\14\1\12\1\17\1\12\1\20\1\7\1\20\1\7\1\16\2\uffff"+
        "\2\12\2\17\1\12\2\17\1\20\1\12\1\17\2\12\1\17\1\12\1\20\1\7\3\12"+
        "\2\17\1\12\1\17\2\12";
    static final String DFA4_acceptS =
        "\14\uffff\1\2\17\uffff\1\1\1\3\31\uffff";
    static final String DFA4_specialS =
        "\67\uffff}>";
    static final String[] DFA4_transitionS = {
            "\1\1",
            "\1\2",
            "\1\5\5\uffff\1\6\3\uffff\1\3\1\uffff\1\4",
            "\1\7\10\uffff\1\10",
            "\1\11",
            "\1\12",
            "\1\13\1\uffff\1\14",
            "\1\15\1\16",
            "\1\17",
            "\1\5\5\uffff\1\6\3\uffff\1\20",
            "\1\5\5\uffff\1\6\3\uffff\1\21\1\uffff\1\22",
            "\1\23",
            "",
            "\1\5\5\uffff\1\6",
            "\1\25\7\uffff\1\24",
            "\1\26",
            "\1\27\10\uffff\1\30",
            "\1\31\10\uffff\1\32",
            "\1\33",
            "\1\35\1\uffff\1\34",
            "\1\5\5\uffff\1\6",
            "\1\36",
            "\1\5\5\uffff\1\6",
            "\1\37\1\40",
            "\1\41",
            "\1\42\1\43",
            "\1\44",
            "\1\5\5\uffff\1\6\3\uffff\1\45",
            "",
            "",
            "\1\5\5\uffff\1\6",
            "\1\5\5\uffff\1\6",
            "\1\47\7\uffff\1\46",
            "\1\50",
            "\1\5\5\uffff\1\6",
            "\1\52\7\uffff\1\51",
            "\1\53",
            "\1\54\10\uffff\1\55",
            "\1\5\5\uffff\1\6",
            "\1\56",
            "\1\5\5\uffff\1\6",
            "\1\5\5\uffff\1\6",
            "\1\57",
            "\1\5\5\uffff\1\6",
            "\1\60\1\61",
            "\1\62",
            "\1\5\5\uffff\1\6",
            "\1\5\5\uffff\1\6",
            "\1\5\5\uffff\1\6",
            "\1\64\7\uffff\1\63",
            "\1\65",
            "\1\5\5\uffff\1\6",
            "\1\66",
            "\1\5\5\uffff\1\6",
            "\1\5\5\uffff\1\6"
    };

    static final short[] DFA4_eot = DFA.unpackEncodedString(DFA4_eotS);
    static final short[] DFA4_eof = DFA.unpackEncodedString(DFA4_eofS);
    static final char[] DFA4_min = DFA.unpackEncodedStringToUnsignedChars(DFA4_minS);
    static final char[] DFA4_max = DFA.unpackEncodedStringToUnsignedChars(DFA4_maxS);
    static final short[] DFA4_accept = DFA.unpackEncodedString(DFA4_acceptS);
    static final short[] DFA4_special = DFA.unpackEncodedString(DFA4_specialS);
    static final short[][] DFA4_transition;

    static {
        int numStates = DFA4_transitionS.length;
        DFA4_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA4_transition[i] = DFA.unpackEncodedString(DFA4_transitionS[i]);
        }
    }

    class DFA4 extends DFA {

        public DFA4(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 4;
            this.eot = DFA4_eot;
            this.eof = DFA4_eof;
            this.min = DFA4_min;
            this.max = DFA4_max;
            this.accept = DFA4_accept;
            this.special = DFA4_special;
            this.transition = DFA4_transition;
        }
        public String getDescription() {
            return "42:1: cardconst returns [IntegrationFunctionExpression value ] : ( '{' expr= signalexpror ',' 'min=' min= NUMBER '}' | '{' expr= signalexpror ',' 'max=' max= NUMBER '}' | '{' expr= signalexpror ',' 'min=' min= NUMBER ',' 'max=' max= NUMBER '}' );";
        }
    }
    static final String DFA6_eotS =
        "\24\uffff";
    static final String DFA6_eofS =
        "\24\uffff";
    static final String DFA6_minS =
        "\1\10\1\4\2\7\1\uffff\1\17\1\uffff\1\4\1\uffff\2\7\3\uffff\1\17"+
        "\2\uffff\1\7\2\uffff";
    static final String DFA6_maxS =
        "\1\10\2\20\1\7\1\uffff\1\20\1\uffff\1\16\1\uffff\1\17\1\20\3\uffff"+
        "\1\20\2\uffff\1\17\2\uffff";
    static final String DFA6_acceptS =
        "\4\uffff\1\1\1\uffff\1\5\1\uffff\1\2\2\uffff\1\6\1\3\1\4\1\uffff"+
        "\1\12\1\7\1\uffff\1\10\1\11";
    static final String DFA6_specialS =
        "\24\uffff}>";
    static final String[] DFA6_transitionS = {
            "\1\1",
            "\1\4\5\uffff\1\4\3\uffff\1\2\1\uffff\1\3",
            "\1\5\10\uffff\1\6",
            "\1\7",
            "",
            "\1\10\1\11",
            "",
            "\1\13\5\uffff\1\13\3\uffff\1\12",
            "",
            "\1\15\7\uffff\1\14",
            "\1\16\10\uffff\1\17",
            "",
            "",
            "",
            "\1\20\1\21",
            "",
            "",
            "\1\23\7\uffff\1\22",
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

    class DFA6 extends DFA {

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
        public String getDescription() {
            return "56:1: signal returns [IntegrationSignal value] : (id= ID | id= ID '[' min= NUMBER ']' | id= ID '[' min= NUMBER ':' ']' | id= ID '[' min= NUMBER ':' max= NUMBER ']' | id= ID '[' ':' max= NUMBER ']' | id= ID ':' t= NUMBER | id= ID ':' t= NUMBER '[' min= NUMBER ']' | id= ID ':' t= NUMBER '[' min= NUMBER ':' ']' | id= ID ':' t= NUMBER '[' min= NUMBER ':' max= NUMBER ']' | id= ID ':' t= NUMBER '[' ':' max= NUMBER ']' );";
        }
    }
 

    public static final BitSet FOLLOW_functionexpror_in_eval28 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_functionexprand_in_functionexpror48 = new BitSet(new long[]{0x0000000000000012L});
    public static final BitSet FOLLOW_OR_in_functionexpror51 = new BitSet(new long[]{0x0000000000000240L});
    public static final BitSet FOLLOW_functionexprand_in_functionexpror55 = new BitSet(new long[]{0x0000000000000012L});
    public static final BitSet FOLLOW_functionexprnot_in_functionexprand79 = new BitSet(new long[]{0x0000000000000022L});
    public static final BitSet FOLLOW_AND_in_functionexprand82 = new BitSet(new long[]{0x0000000000000240L});
    public static final BitSet FOLLOW_functionexprnot_in_functionexprand86 = new BitSet(new long[]{0x0000000000000022L});
    public static final BitSet FOLLOW_cardconst_in_functionexprnot109 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NOT_in_functionexprnot118 = new BitSet(new long[]{0x0000000000000200L});
    public static final BitSet FOLLOW_cardconst_in_functionexprnot122 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_9_in_cardconst141 = new BitSet(new long[]{0x0000000000000100L});
    public static final BitSet FOLLOW_signalexpror_in_cardconst145 = new BitSet(new long[]{0x0000000000000400L});
    public static final BitSet FOLLOW_10_in_cardconst147 = new BitSet(new long[]{0x0000000000000800L});
    public static final BitSet FOLLOW_11_in_cardconst149 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_NUMBER_in_cardconst153 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_12_in_cardconst155 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_9_in_cardconst164 = new BitSet(new long[]{0x0000000000000100L});
    public static final BitSet FOLLOW_signalexpror_in_cardconst168 = new BitSet(new long[]{0x0000000000000400L});
    public static final BitSet FOLLOW_10_in_cardconst170 = new BitSet(new long[]{0x0000000000002000L});
    public static final BitSet FOLLOW_13_in_cardconst172 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_NUMBER_in_cardconst176 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_12_in_cardconst178 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_9_in_cardconst187 = new BitSet(new long[]{0x0000000000000100L});
    public static final BitSet FOLLOW_signalexpror_in_cardconst191 = new BitSet(new long[]{0x0000000000000400L});
    public static final BitSet FOLLOW_10_in_cardconst193 = new BitSet(new long[]{0x0000000000000800L});
    public static final BitSet FOLLOW_11_in_cardconst195 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_NUMBER_in_cardconst199 = new BitSet(new long[]{0x0000000000000400L});
    public static final BitSet FOLLOW_10_in_cardconst201 = new BitSet(new long[]{0x0000000000002000L});
    public static final BitSet FOLLOW_13_in_cardconst203 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_NUMBER_in_cardconst207 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_12_in_cardconst209 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_signal_in_signalexpror230 = new BitSet(new long[]{0x0000000000000012L});
    public static final BitSet FOLLOW_OR_in_signalexpror233 = new BitSet(new long[]{0x0000000000000100L});
    public static final BitSet FOLLOW_signal_in_signalexpror237 = new BitSet(new long[]{0x0000000000000012L});
    public static final BitSet FOLLOW_ID_in_signal261 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_signal272 = new BitSet(new long[]{0x0000000000004000L});
    public static final BitSet FOLLOW_14_in_signal274 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_NUMBER_in_signal278 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_15_in_signal280 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_signal291 = new BitSet(new long[]{0x0000000000004000L});
    public static final BitSet FOLLOW_14_in_signal293 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_NUMBER_in_signal297 = new BitSet(new long[]{0x0000000000010000L});
    public static final BitSet FOLLOW_16_in_signal299 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_15_in_signal301 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_signal312 = new BitSet(new long[]{0x0000000000004000L});
    public static final BitSet FOLLOW_14_in_signal314 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_NUMBER_in_signal318 = new BitSet(new long[]{0x0000000000010000L});
    public static final BitSet FOLLOW_16_in_signal320 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_NUMBER_in_signal324 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_15_in_signal326 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_signal337 = new BitSet(new long[]{0x0000000000004000L});
    public static final BitSet FOLLOW_14_in_signal339 = new BitSet(new long[]{0x0000000000010000L});
    public static final BitSet FOLLOW_16_in_signal341 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_NUMBER_in_signal345 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_15_in_signal347 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_signal358 = new BitSet(new long[]{0x0000000000010000L});
    public static final BitSet FOLLOW_16_in_signal360 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_NUMBER_in_signal364 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_signal375 = new BitSet(new long[]{0x0000000000010000L});
    public static final BitSet FOLLOW_16_in_signal377 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_NUMBER_in_signal381 = new BitSet(new long[]{0x0000000000004000L});
    public static final BitSet FOLLOW_14_in_signal383 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_NUMBER_in_signal387 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_15_in_signal389 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_signal400 = new BitSet(new long[]{0x0000000000010000L});
    public static final BitSet FOLLOW_16_in_signal402 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_NUMBER_in_signal406 = new BitSet(new long[]{0x0000000000004000L});
    public static final BitSet FOLLOW_14_in_signal408 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_NUMBER_in_signal412 = new BitSet(new long[]{0x0000000000010000L});
    public static final BitSet FOLLOW_16_in_signal414 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_15_in_signal416 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_signal427 = new BitSet(new long[]{0x0000000000010000L});
    public static final BitSet FOLLOW_16_in_signal429 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_NUMBER_in_signal433 = new BitSet(new long[]{0x0000000000004000L});
    public static final BitSet FOLLOW_14_in_signal435 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_NUMBER_in_signal439 = new BitSet(new long[]{0x0000000000010000L});
    public static final BitSet FOLLOW_16_in_signal441 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_NUMBER_in_signal445 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_15_in_signal447 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_signal458 = new BitSet(new long[]{0x0000000000010000L});
    public static final BitSet FOLLOW_16_in_signal460 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_NUMBER_in_signal464 = new BitSet(new long[]{0x0000000000004000L});
    public static final BitSet FOLLOW_14_in_signal466 = new BitSet(new long[]{0x0000000000010000L});
    public static final BitSet FOLLOW_16_in_signal468 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_NUMBER_in_signal472 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_15_in_signal474 = new BitSet(new long[]{0x0000000000000002L});

}