// $ANTLR 3.3 Nov 30, 2010 12:46:29 src/main/java/org/epilogtool/integration/IntegrationGrammar.g 2017-05-24 22:29:09

package org.epilogtool.integration;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

public class IntegrationGrammarParser extends Parser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "OR", "AND", "NOT", "NUMBER", "ID", "SPACE", "WS", "'('", "')'", "'{'", "'}'", "','", "'min'", "'='", "'max'", "'['", "']'", "':'"
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
    public static final int OR=4;
    public static final int AND=5;
    public static final int NOT=6;
    public static final int NUMBER=7;
    public static final int ID=8;
    public static final int SPACE=9;
    public static final int WS=10;

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


      @Override
      public void reportError(RecognitionException e) {
        throw new RuntimeException("I quit!\n" + e.getMessage()); 
      }



    // $ANTLR start "eval"
    // src/main/java/org/epilogtool/integration/IntegrationGrammar.g:21:1: eval returns [IntegrationFunctionExpression value] : exp= functionexpror EOF ;
    public final IntegrationFunctionExpression eval() throws RecognitionException {
        IntegrationFunctionExpression value = null;

        IntegrationFunctionExpression exp = null;


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
        }
        return value;
    }
    // $ANTLR end "eval"


    // $ANTLR start "functionexpror"
    // src/main/java/org/epilogtool/integration/IntegrationGrammar.g:28:1: functionexpror returns [IntegrationFunctionExpression value] : o1= functionexprand ( OR o2= functionexprand )* ;
    public final IntegrationFunctionExpression functionexpror() throws RecognitionException {
        IntegrationFunctionExpression value = null;

        IntegrationFunctionExpression o1 = null;

        IntegrationFunctionExpression o2 = null;


        try {
            // src/main/java/org/epilogtool/integration/IntegrationGrammar.g:29:2: (o1= functionexprand ( OR o2= functionexprand )* )
            // src/main/java/org/epilogtool/integration/IntegrationGrammar.g:29:4: o1= functionexprand ( OR o2= functionexprand )*
            {
            pushFollow(FOLLOW_functionexprand_in_functionexpror67);
            o1=functionexprand();

            state._fsp--;

            // src/main/java/org/epilogtool/integration/IntegrationGrammar.g:29:23: ( OR o2= functionexprand )*
            loop1:
            do {
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
    // src/main/java/org/epilogtool/integration/IntegrationGrammar.g:33:1: functionexprand returns [IntegrationFunctionExpression value] : a1= functionexprnot ( AND a2= functionexprnot )* ;
    public final IntegrationFunctionExpression functionexprand() throws RecognitionException {
        IntegrationFunctionExpression value = null;

        IntegrationFunctionExpression a1 = null;

        IntegrationFunctionExpression a2 = null;


        try {
            // src/main/java/org/epilogtool/integration/IntegrationGrammar.g:34:2: (a1= functionexprnot ( AND a2= functionexprnot )* )
            // src/main/java/org/epilogtool/integration/IntegrationGrammar.g:34:4: a1= functionexprnot ( AND a2= functionexprnot )*
            {
            pushFollow(FOLLOW_functionexprnot_in_functionexprand98);
            a1=functionexprnot();

            state._fsp--;

            // src/main/java/org/epilogtool/integration/IntegrationGrammar.g:34:23: ( AND a2= functionexprnot )*
            loop2:
            do {
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
    // src/main/java/org/epilogtool/integration/IntegrationGrammar.g:38:1: functionexprnot returns [IntegrationFunctionExpression value] : (expr= functionparen | NOT expr= functionparen );
    public final IntegrationFunctionExpression functionexprnot() throws RecognitionException {
        IntegrationFunctionExpression value = null;

        IntegrationFunctionExpression expr = null;


        try {
            // src/main/java/org/epilogtool/integration/IntegrationGrammar.g:39:2: (expr= functionparen | NOT expr= functionparen )
            int alt3=2;
            int LA3_0 = input.LA(1);

            if ( (LA3_0==11||LA3_0==13) ) {
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
        }
        return value;
    }
    // $ANTLR end "functionexprnot"


    // $ANTLR start "functionparen"
    // src/main/java/org/epilogtool/integration/IntegrationGrammar.g:45:1: functionparen returns [IntegrationFunctionExpression value] : ( '(' expr= functionexpror ')' | expr= cardconst );
    public final IntegrationFunctionExpression functionparen() throws RecognitionException {
        IntegrationFunctionExpression value = null;

        IntegrationFunctionExpression expr = null;


        try {
            // src/main/java/org/epilogtool/integration/IntegrationGrammar.g:46:2: ( '(' expr= functionexpror ')' | expr= cardconst )
            int alt4=2;
            int LA4_0 = input.LA(1);

            if ( (LA4_0==11) ) {
                alt4=1;
            }
            else if ( (LA4_0==13) ) {
                alt4=2;
            }
            else {
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
    // $ANTLR end "functionparen"


    // $ANTLR start "cardconst"
    // src/main/java/org/epilogtool/integration/IntegrationGrammar.g:52:1: cardconst returns [IntegrationFunctionExpression value ] : ( '{' expr= signalexpror '}' | '{' expr= signalexpror ',' 'min' '=' min= NUMBER '}' | '{' expr= signalexpror ',' 'max' '=' max= NUMBER '}' | '{' expr= signalexpror ',' 'min' '=' min= NUMBER ',' 'max' '=' max= NUMBER '}' );
    public final IntegrationFunctionExpression cardconst() throws RecognitionException {
        IntegrationFunctionExpression value = null;

        Token min=null;
        Token max=null;
        IntegrationSignalExpression expr = null;


        try {
            // src/main/java/org/epilogtool/integration/IntegrationGrammar.g:53:2: ( '{' expr= signalexpror '}' | '{' expr= signalexpror ',' 'min' '=' min= NUMBER '}' | '{' expr= signalexpror ',' 'max' '=' max= NUMBER '}' | '{' expr= signalexpror ',' 'min' '=' min= NUMBER ',' 'max' '=' max= NUMBER '}' )
            int alt5=4;
            alt5 = dfa5.predict(input);
            switch (alt5) {
                case 1 :
                    // src/main/java/org/epilogtool/integration/IntegrationGrammar.g:53:4: '{' expr= signalexpror '}'
                    {
                    match(input,13,FOLLOW_13_in_cardconst196); 
                    pushFollow(FOLLOW_signalexpror_in_cardconst200);
                    expr=signalexpror();

                    state._fsp--;

                    match(input,14,FOLLOW_14_in_cardconst202); 
                     value = IFSpecification.cardinalityConstraint(expr, "1", "-1"); 

                    }
                    break;
                case 2 :
                    // src/main/java/org/epilogtool/integration/IntegrationGrammar.g:55:4: '{' expr= signalexpror ',' 'min' '=' min= NUMBER '}'
                    {
                    match(input,13,FOLLOW_13_in_cardconst211); 
                    pushFollow(FOLLOW_signalexpror_in_cardconst215);
                    expr=signalexpror();

                    state._fsp--;

                    match(input,15,FOLLOW_15_in_cardconst217); 
                    match(input,16,FOLLOW_16_in_cardconst219); 
                    match(input,17,FOLLOW_17_in_cardconst221); 
                    min=(Token)match(input,NUMBER,FOLLOW_NUMBER_in_cardconst225); 
                    match(input,14,FOLLOW_14_in_cardconst227); 
                     value = IFSpecification.cardinalityConstraint(expr, (min!=null?min.getText():null), "-1"); 

                    }
                    break;
                case 3 :
                    // src/main/java/org/epilogtool/integration/IntegrationGrammar.g:57:4: '{' expr= signalexpror ',' 'max' '=' max= NUMBER '}'
                    {
                    match(input,13,FOLLOW_13_in_cardconst236); 
                    pushFollow(FOLLOW_signalexpror_in_cardconst240);
                    expr=signalexpror();

                    state._fsp--;

                    match(input,15,FOLLOW_15_in_cardconst242); 
                    match(input,18,FOLLOW_18_in_cardconst244); 
                    match(input,17,FOLLOW_17_in_cardconst246); 
                    max=(Token)match(input,NUMBER,FOLLOW_NUMBER_in_cardconst250); 
                    match(input,14,FOLLOW_14_in_cardconst252); 
                     value = IFSpecification.cardinalityConstraint(expr, "-1", (max!=null?max.getText():null)); 

                    }
                    break;
                case 4 :
                    // src/main/java/org/epilogtool/integration/IntegrationGrammar.g:59:4: '{' expr= signalexpror ',' 'min' '=' min= NUMBER ',' 'max' '=' max= NUMBER '}'
                    {
                    match(input,13,FOLLOW_13_in_cardconst261); 
                    pushFollow(FOLLOW_signalexpror_in_cardconst265);
                    expr=signalexpror();

                    state._fsp--;

                    match(input,15,FOLLOW_15_in_cardconst267); 
                    match(input,16,FOLLOW_16_in_cardconst269); 
                    match(input,17,FOLLOW_17_in_cardconst271); 
                    min=(Token)match(input,NUMBER,FOLLOW_NUMBER_in_cardconst275); 
                    match(input,15,FOLLOW_15_in_cardconst277); 
                    match(input,18,FOLLOW_18_in_cardconst279); 
                    match(input,17,FOLLOW_17_in_cardconst281); 
                    max=(Token)match(input,NUMBER,FOLLOW_NUMBER_in_cardconst285); 
                    match(input,14,FOLLOW_14_in_cardconst287); 
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
    // src/main/java/org/epilogtool/integration/IntegrationGrammar.g:63:1: signalexpror returns [IntegrationSignalExpression value] : o1= signal ( OR o2= signal )* ;
    public final IntegrationSignalExpression signalexpror() throws RecognitionException {
        IntegrationSignalExpression value = null;

        IntegrationSignal o1 = null;

        IntegrationSignal o2 = null;


        try {
            // src/main/java/org/epilogtool/integration/IntegrationGrammar.g:64:2: (o1= signal ( OR o2= signal )* )
            // src/main/java/org/epilogtool/integration/IntegrationGrammar.g:64:4: o1= signal ( OR o2= signal )*
            {
            pushFollow(FOLLOW_signal_in_signalexpror308);
            o1=signal();

            state._fsp--;

            // src/main/java/org/epilogtool/integration/IntegrationGrammar.g:64:14: ( OR o2= signal )*
            loop6:
            do {
                int alt6=2;
                int LA6_0 = input.LA(1);

                if ( (LA6_0==OR) ) {
                    alt6=1;
                }


                switch (alt6) {
            	case 1 :
            	    // src/main/java/org/epilogtool/integration/IntegrationGrammar.g:64:15: OR o2= signal
            	    {
            	    match(input,OR,FOLLOW_OR_in_signalexpror311); 
            	    pushFollow(FOLLOW_signal_in_signalexpror315);
            	    o2=signal();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop6;
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
    // src/main/java/org/epilogtool/integration/IntegrationGrammar.g:68:1: signal returns [IntegrationSignal value] : (id= ID | id= ID '[' min= NUMBER ']' | id= ID '[' min= NUMBER ':' ']' | id= ID '[' min= NUMBER ':' max= NUMBER ']' | id= ID '[' ':' max= NUMBER ']' | id= ID ':' t= NUMBER | id= ID ':' t= NUMBER '[' min= NUMBER ']' | id= ID ':' t= NUMBER '[' min= NUMBER ':' ']' | id= ID ':' t= NUMBER '[' min= NUMBER ':' max= NUMBER ']' | id= ID ':' t= NUMBER '[' ':' max= NUMBER ']' );
    public final IntegrationSignal signal() throws RecognitionException {
        IntegrationSignal value = null;

        Token id=null;
        Token min=null;
        Token max=null;
        Token t=null;

        try {
            // src/main/java/org/epilogtool/integration/IntegrationGrammar.g:69:2: (id= ID | id= ID '[' min= NUMBER ']' | id= ID '[' min= NUMBER ':' ']' | id= ID '[' min= NUMBER ':' max= NUMBER ']' | id= ID '[' ':' max= NUMBER ']' | id= ID ':' t= NUMBER | id= ID ':' t= NUMBER '[' min= NUMBER ']' | id= ID ':' t= NUMBER '[' min= NUMBER ':' ']' | id= ID ':' t= NUMBER '[' min= NUMBER ':' max= NUMBER ']' | id= ID ':' t= NUMBER '[' ':' max= NUMBER ']' )
            int alt7=10;
            alt7 = dfa7.predict(input);
            switch (alt7) {
                case 1 :
                    // src/main/java/org/epilogtool/integration/IntegrationGrammar.g:69:4: id= ID
                    {
                    id=(Token)match(input,ID,FOLLOW_ID_in_signal339); 
                     value = IFSpecification.integrationSignal((id!=null?id.getText():null), "1", new IntegrationDistance("1", "1")); 

                    }
                    break;
                case 2 :
                    // src/main/java/org/epilogtool/integration/IntegrationGrammar.g:71:4: id= ID '[' min= NUMBER ']'
                    {
                    id=(Token)match(input,ID,FOLLOW_ID_in_signal350); 
                    match(input,19,FOLLOW_19_in_signal352); 
                    min=(Token)match(input,NUMBER,FOLLOW_NUMBER_in_signal356); 
                    match(input,20,FOLLOW_20_in_signal358); 
                     value = IFSpecification.integrationSignal((id!=null?id.getText():null), "1", new IntegrationDistance((min!=null?min.getText():null), (min!=null?min.getText():null))); 

                    }
                    break;
                case 3 :
                    // src/main/java/org/epilogtool/integration/IntegrationGrammar.g:73:4: id= ID '[' min= NUMBER ':' ']'
                    {
                    id=(Token)match(input,ID,FOLLOW_ID_in_signal369); 
                    match(input,19,FOLLOW_19_in_signal371); 
                    min=(Token)match(input,NUMBER,FOLLOW_NUMBER_in_signal375); 
                    match(input,21,FOLLOW_21_in_signal377); 
                    match(input,20,FOLLOW_20_in_signal379); 
                     value = IFSpecification.integrationSignal((id!=null?id.getText():null), "1", new IntegrationDistance((min!=null?min.getText():null), "-1")); 

                    }
                    break;
                case 4 :
                    // src/main/java/org/epilogtool/integration/IntegrationGrammar.g:75:4: id= ID '[' min= NUMBER ':' max= NUMBER ']'
                    {
                    id=(Token)match(input,ID,FOLLOW_ID_in_signal390); 
                    match(input,19,FOLLOW_19_in_signal392); 
                    min=(Token)match(input,NUMBER,FOLLOW_NUMBER_in_signal396); 
                    match(input,21,FOLLOW_21_in_signal398); 
                    max=(Token)match(input,NUMBER,FOLLOW_NUMBER_in_signal402); 
                    match(input,20,FOLLOW_20_in_signal404); 
                     value = IFSpecification.integrationSignal((id!=null?id.getText():null), "1", new IntegrationDistance((min!=null?min.getText():null), (max!=null?max.getText():null))); 

                    }
                    break;
                case 5 :
                    // src/main/java/org/epilogtool/integration/IntegrationGrammar.g:77:4: id= ID '[' ':' max= NUMBER ']'
                    {
                    id=(Token)match(input,ID,FOLLOW_ID_in_signal415); 
                    match(input,19,FOLLOW_19_in_signal417); 
                    match(input,21,FOLLOW_21_in_signal419); 
                    max=(Token)match(input,NUMBER,FOLLOW_NUMBER_in_signal423); 
                    match(input,20,FOLLOW_20_in_signal425); 
                     value = IFSpecification.integrationSignal((id!=null?id.getText():null), "1", new IntegrationDistance("-1", (max!=null?max.getText():null))); 

                    }
                    break;
                case 6 :
                    // src/main/java/org/epilogtool/integration/IntegrationGrammar.g:79:4: id= ID ':' t= NUMBER
                    {
                    id=(Token)match(input,ID,FOLLOW_ID_in_signal436); 
                    match(input,21,FOLLOW_21_in_signal438); 
                    t=(Token)match(input,NUMBER,FOLLOW_NUMBER_in_signal442); 
                     value = IFSpecification.integrationSignal((id!=null?id.getText():null), (t!=null?t.getText():null), new IntegrationDistance("1", "1")); 

                    }
                    break;
                case 7 :
                    // src/main/java/org/epilogtool/integration/IntegrationGrammar.g:81:4: id= ID ':' t= NUMBER '[' min= NUMBER ']'
                    {
                    id=(Token)match(input,ID,FOLLOW_ID_in_signal453); 
                    match(input,21,FOLLOW_21_in_signal455); 
                    t=(Token)match(input,NUMBER,FOLLOW_NUMBER_in_signal459); 
                    match(input,19,FOLLOW_19_in_signal461); 
                    min=(Token)match(input,NUMBER,FOLLOW_NUMBER_in_signal465); 
                    match(input,20,FOLLOW_20_in_signal467); 
                     value = IFSpecification.integrationSignal((id!=null?id.getText():null), (t!=null?t.getText():null), new IntegrationDistance((min!=null?min.getText():null), (min!=null?min.getText():null))); 

                    }
                    break;
                case 8 :
                    // src/main/java/org/epilogtool/integration/IntegrationGrammar.g:83:4: id= ID ':' t= NUMBER '[' min= NUMBER ':' ']'
                    {
                    id=(Token)match(input,ID,FOLLOW_ID_in_signal478); 
                    match(input,21,FOLLOW_21_in_signal480); 
                    t=(Token)match(input,NUMBER,FOLLOW_NUMBER_in_signal484); 
                    match(input,19,FOLLOW_19_in_signal486); 
                    min=(Token)match(input,NUMBER,FOLLOW_NUMBER_in_signal490); 
                    match(input,21,FOLLOW_21_in_signal492); 
                    match(input,20,FOLLOW_20_in_signal494); 
                     value = IFSpecification.integrationSignal((id!=null?id.getText():null), (t!=null?t.getText():null), new IntegrationDistance((min!=null?min.getText():null), "-1")); 

                    }
                    break;
                case 9 :
                    // src/main/java/org/epilogtool/integration/IntegrationGrammar.g:85:4: id= ID ':' t= NUMBER '[' min= NUMBER ':' max= NUMBER ']'
                    {
                    id=(Token)match(input,ID,FOLLOW_ID_in_signal505); 
                    match(input,21,FOLLOW_21_in_signal507); 
                    t=(Token)match(input,NUMBER,FOLLOW_NUMBER_in_signal511); 
                    match(input,19,FOLLOW_19_in_signal513); 
                    min=(Token)match(input,NUMBER,FOLLOW_NUMBER_in_signal517); 
                    match(input,21,FOLLOW_21_in_signal519); 
                    max=(Token)match(input,NUMBER,FOLLOW_NUMBER_in_signal523); 
                    match(input,20,FOLLOW_20_in_signal525); 
                     value = IFSpecification.integrationSignal((id!=null?id.getText():null), (t!=null?t.getText():null), new IntegrationDistance((min!=null?min.getText():null), (max!=null?max.getText():null))); 

                    }
                    break;
                case 10 :
                    // src/main/java/org/epilogtool/integration/IntegrationGrammar.g:87:4: id= ID ':' t= NUMBER '[' ':' max= NUMBER ']'
                    {
                    id=(Token)match(input,ID,FOLLOW_ID_in_signal536); 
                    match(input,21,FOLLOW_21_in_signal538); 
                    t=(Token)match(input,NUMBER,FOLLOW_NUMBER_in_signal542); 
                    match(input,19,FOLLOW_19_in_signal544); 
                    match(input,21,FOLLOW_21_in_signal546); 
                    max=(Token)match(input,NUMBER,FOLLOW_NUMBER_in_signal550); 
                    match(input,20,FOLLOW_20_in_signal552); 
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


    protected DFA5 dfa5 = new DFA5(this);
    protected DFA7 dfa7 = new DFA7(this);
    static final String DFA5_eotS =
        "\71\uffff";
    static final String DFA5_eofS =
        "\71\uffff";
    static final String DFA5_minS =
        "\1\15\1\10\1\4\2\7\1\10\1\uffff\1\20\1\24\1\7\2\4\1\21\1\uffff\1"+
        "\4\1\7\1\24\4\7\1\4\1\24\1\4\1\24\1\7\1\24\1\7\1\4\1\16\2\4\1\7"+
        "\1\24\1\4\1\7\1\24\1\7\2\uffff\1\4\1\24\2\4\1\24\1\4\1\24\1\7\3"+
        "\4\1\7\1\24\1\4\1\24\2\4";
    static final String DFA5_maxS =
        "\1\15\1\10\2\25\1\7\1\10\1\uffff\1\22\1\25\1\7\1\23\1\25\1\21\1"+
        "\uffff\1\17\2\24\2\25\2\7\1\17\1\24\1\17\1\25\1\7\1\25\1\7\1\23"+
        "\3\17\2\24\1\17\2\24\1\25\2\uffff\1\17\1\24\2\17\1\24\1\17\1\25"+
        "\1\7\3\17\2\24\1\17\1\24\2\17";
    static final String DFA5_acceptS =
        "\6\uffff\1\1\6\uffff\1\3\30\uffff\1\2\1\4\21\uffff";
    static final String DFA5_specialS =
        "\71\uffff}>";
    static final String[] DFA5_transitionS = {
            "\1\1",
            "\1\2",
            "\1\5\11\uffff\1\6\1\7\3\uffff\1\3\1\uffff\1\4",
            "\1\10\15\uffff\1\11",
            "\1\12",
            "\1\13",
            "",
            "\1\14\1\uffff\1\15",
            "\1\16\1\17",
            "\1\20",
            "\1\5\11\uffff\1\6\1\7\3\uffff\1\21",
            "\1\5\11\uffff\1\6\1\7\3\uffff\1\22\1\uffff\1\23",
            "\1\24",
            "",
            "\1\5\11\uffff\1\6\1\7",
            "\1\26\14\uffff\1\25",
            "\1\27",
            "\1\30\15\uffff\1\31",
            "\1\32\15\uffff\1\33",
            "\1\34",
            "\1\35",
            "\1\5\11\uffff\1\6\1\7",
            "\1\36",
            "\1\5\11\uffff\1\6\1\7",
            "\1\37\1\40",
            "\1\41",
            "\1\42\1\43",
            "\1\44",
            "\1\5\11\uffff\1\6\1\7\3\uffff\1\45",
            "\1\46\1\47",
            "\1\5\11\uffff\1\6\1\7",
            "\1\5\11\uffff\1\6\1\7",
            "\1\51\14\uffff\1\50",
            "\1\52",
            "\1\5\11\uffff\1\6\1\7",
            "\1\54\14\uffff\1\53",
            "\1\55",
            "\1\56\15\uffff\1\57",
            "",
            "",
            "\1\5\11\uffff\1\6\1\7",
            "\1\60",
            "\1\5\11\uffff\1\6\1\7",
            "\1\5\11\uffff\1\6\1\7",
            "\1\61",
            "\1\5\11\uffff\1\6\1\7",
            "\1\62\1\63",
            "\1\64",
            "\1\5\11\uffff\1\6\1\7",
            "\1\5\11\uffff\1\6\1\7",
            "\1\5\11\uffff\1\6\1\7",
            "\1\66\14\uffff\1\65",
            "\1\67",
            "\1\5\11\uffff\1\6\1\7",
            "\1\70",
            "\1\5\11\uffff\1\6\1\7",
            "\1\5\11\uffff\1\6\1\7"
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

    class DFA5 extends DFA {

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
        public String getDescription() {
            return "52:1: cardconst returns [IntegrationFunctionExpression value ] : ( '{' expr= signalexpror '}' | '{' expr= signalexpror ',' 'min' '=' min= NUMBER '}' | '{' expr= signalexpror ',' 'max' '=' max= NUMBER '}' | '{' expr= signalexpror ',' 'min' '=' min= NUMBER ',' 'max' '=' max= NUMBER '}' );";
        }
    }
    static final String DFA7_eotS =
        "\24\uffff";
    static final String DFA7_eofS =
        "\24\uffff";
    static final String DFA7_minS =
        "\1\10\1\4\2\7\1\uffff\1\24\1\uffff\1\4\1\uffff\2\7\3\uffff\1\24"+
        "\2\uffff\1\7\2\uffff";
    static final String DFA7_maxS =
        "\1\10\2\25\1\7\1\uffff\1\25\1\uffff\1\23\1\uffff\1\24\1\25\3\uffff"+
        "\1\25\2\uffff\1\24\2\uffff";
    static final String DFA7_acceptS =
        "\4\uffff\1\1\1\uffff\1\5\1\uffff\1\2\2\uffff\1\6\1\3\1\4\1\uffff"+
        "\1\12\1\7\1\uffff\1\10\1\11";
    static final String DFA7_specialS =
        "\24\uffff}>";
    static final String[] DFA7_transitionS = {
            "\1\1",
            "\1\4\11\uffff\2\4\3\uffff\1\2\1\uffff\1\3",
            "\1\5\15\uffff\1\6",
            "\1\7",
            "",
            "\1\10\1\11",
            "",
            "\1\13\11\uffff\2\13\3\uffff\1\12",
            "",
            "\1\15\14\uffff\1\14",
            "\1\16\15\uffff\1\17",
            "",
            "",
            "",
            "\1\20\1\21",
            "",
            "",
            "\1\23\14\uffff\1\22",
            "",
            ""
    };

    static final short[] DFA7_eot = DFA.unpackEncodedString(DFA7_eotS);
    static final short[] DFA7_eof = DFA.unpackEncodedString(DFA7_eofS);
    static final char[] DFA7_min = DFA.unpackEncodedStringToUnsignedChars(DFA7_minS);
    static final char[] DFA7_max = DFA.unpackEncodedStringToUnsignedChars(DFA7_maxS);
    static final short[] DFA7_accept = DFA.unpackEncodedString(DFA7_acceptS);
    static final short[] DFA7_special = DFA.unpackEncodedString(DFA7_specialS);
    static final short[][] DFA7_transition;

    static {
        int numStates = DFA7_transitionS.length;
        DFA7_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA7_transition[i] = DFA.unpackEncodedString(DFA7_transitionS[i]);
        }
    }

    class DFA7 extends DFA {

        public DFA7(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 7;
            this.eot = DFA7_eot;
            this.eof = DFA7_eof;
            this.min = DFA7_min;
            this.max = DFA7_max;
            this.accept = DFA7_accept;
            this.special = DFA7_special;
            this.transition = DFA7_transition;
        }
        public String getDescription() {
            return "68:1: signal returns [IntegrationSignal value] : (id= ID | id= ID '[' min= NUMBER ']' | id= ID '[' min= NUMBER ':' ']' | id= ID '[' min= NUMBER ':' max= NUMBER ']' | id= ID '[' ':' max= NUMBER ']' | id= ID ':' t= NUMBER | id= ID ':' t= NUMBER '[' min= NUMBER ']' | id= ID ':' t= NUMBER '[' min= NUMBER ':' ']' | id= ID ':' t= NUMBER '[' min= NUMBER ':' max= NUMBER ']' | id= ID ':' t= NUMBER '[' ':' max= NUMBER ']' );";
        }
    }
 

    public static final BitSet FOLLOW_functionexpror_in_eval46 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_eval48 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_functionexprand_in_functionexpror67 = new BitSet(new long[]{0x0000000000000012L});
    public static final BitSet FOLLOW_OR_in_functionexpror70 = new BitSet(new long[]{0x0000000000002840L});
    public static final BitSet FOLLOW_functionexprand_in_functionexpror74 = new BitSet(new long[]{0x0000000000000012L});
    public static final BitSet FOLLOW_functionexprnot_in_functionexprand98 = new BitSet(new long[]{0x0000000000000022L});
    public static final BitSet FOLLOW_AND_in_functionexprand101 = new BitSet(new long[]{0x0000000000002840L});
    public static final BitSet FOLLOW_functionexprnot_in_functionexprand105 = new BitSet(new long[]{0x0000000000000022L});
    public static final BitSet FOLLOW_functionparen_in_functionexprnot128 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NOT_in_functionexprnot137 = new BitSet(new long[]{0x0000000000002800L});
    public static final BitSet FOLLOW_functionparen_in_functionexprnot141 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_11_in_functionparen160 = new BitSet(new long[]{0x0000000000002840L});
    public static final BitSet FOLLOW_functionexpror_in_functionparen164 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_12_in_functionparen166 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_cardconst_in_functionparen177 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_13_in_cardconst196 = new BitSet(new long[]{0x0000000000000100L});
    public static final BitSet FOLLOW_signalexpror_in_cardconst200 = new BitSet(new long[]{0x0000000000004000L});
    public static final BitSet FOLLOW_14_in_cardconst202 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_13_in_cardconst211 = new BitSet(new long[]{0x0000000000000100L});
    public static final BitSet FOLLOW_signalexpror_in_cardconst215 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_15_in_cardconst217 = new BitSet(new long[]{0x0000000000010000L});
    public static final BitSet FOLLOW_16_in_cardconst219 = new BitSet(new long[]{0x0000000000020000L});
    public static final BitSet FOLLOW_17_in_cardconst221 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_NUMBER_in_cardconst225 = new BitSet(new long[]{0x0000000000004000L});
    public static final BitSet FOLLOW_14_in_cardconst227 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_13_in_cardconst236 = new BitSet(new long[]{0x0000000000000100L});
    public static final BitSet FOLLOW_signalexpror_in_cardconst240 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_15_in_cardconst242 = new BitSet(new long[]{0x0000000000040000L});
    public static final BitSet FOLLOW_18_in_cardconst244 = new BitSet(new long[]{0x0000000000020000L});
    public static final BitSet FOLLOW_17_in_cardconst246 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_NUMBER_in_cardconst250 = new BitSet(new long[]{0x0000000000004000L});
    public static final BitSet FOLLOW_14_in_cardconst252 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_13_in_cardconst261 = new BitSet(new long[]{0x0000000000000100L});
    public static final BitSet FOLLOW_signalexpror_in_cardconst265 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_15_in_cardconst267 = new BitSet(new long[]{0x0000000000010000L});
    public static final BitSet FOLLOW_16_in_cardconst269 = new BitSet(new long[]{0x0000000000020000L});
    public static final BitSet FOLLOW_17_in_cardconst271 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_NUMBER_in_cardconst275 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_15_in_cardconst277 = new BitSet(new long[]{0x0000000000040000L});
    public static final BitSet FOLLOW_18_in_cardconst279 = new BitSet(new long[]{0x0000000000020000L});
    public static final BitSet FOLLOW_17_in_cardconst281 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_NUMBER_in_cardconst285 = new BitSet(new long[]{0x0000000000004000L});
    public static final BitSet FOLLOW_14_in_cardconst287 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_signal_in_signalexpror308 = new BitSet(new long[]{0x0000000000000012L});
    public static final BitSet FOLLOW_OR_in_signalexpror311 = new BitSet(new long[]{0x0000000000000100L});
    public static final BitSet FOLLOW_signal_in_signalexpror315 = new BitSet(new long[]{0x0000000000000012L});
    public static final BitSet FOLLOW_ID_in_signal339 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_signal350 = new BitSet(new long[]{0x0000000000080000L});
    public static final BitSet FOLLOW_19_in_signal352 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_NUMBER_in_signal356 = new BitSet(new long[]{0x0000000000100000L});
    public static final BitSet FOLLOW_20_in_signal358 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_signal369 = new BitSet(new long[]{0x0000000000080000L});
    public static final BitSet FOLLOW_19_in_signal371 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_NUMBER_in_signal375 = new BitSet(new long[]{0x0000000000200000L});
    public static final BitSet FOLLOW_21_in_signal377 = new BitSet(new long[]{0x0000000000100000L});
    public static final BitSet FOLLOW_20_in_signal379 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_signal390 = new BitSet(new long[]{0x0000000000080000L});
    public static final BitSet FOLLOW_19_in_signal392 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_NUMBER_in_signal396 = new BitSet(new long[]{0x0000000000200000L});
    public static final BitSet FOLLOW_21_in_signal398 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_NUMBER_in_signal402 = new BitSet(new long[]{0x0000000000100000L});
    public static final BitSet FOLLOW_20_in_signal404 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_signal415 = new BitSet(new long[]{0x0000000000080000L});
    public static final BitSet FOLLOW_19_in_signal417 = new BitSet(new long[]{0x0000000000200000L});
    public static final BitSet FOLLOW_21_in_signal419 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_NUMBER_in_signal423 = new BitSet(new long[]{0x0000000000100000L});
    public static final BitSet FOLLOW_20_in_signal425 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_signal436 = new BitSet(new long[]{0x0000000000200000L});
    public static final BitSet FOLLOW_21_in_signal438 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_NUMBER_in_signal442 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_signal453 = new BitSet(new long[]{0x0000000000200000L});
    public static final BitSet FOLLOW_21_in_signal455 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_NUMBER_in_signal459 = new BitSet(new long[]{0x0000000000080000L});
    public static final BitSet FOLLOW_19_in_signal461 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_NUMBER_in_signal465 = new BitSet(new long[]{0x0000000000100000L});
    public static final BitSet FOLLOW_20_in_signal467 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_signal478 = new BitSet(new long[]{0x0000000000200000L});
    public static final BitSet FOLLOW_21_in_signal480 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_NUMBER_in_signal484 = new BitSet(new long[]{0x0000000000080000L});
    public static final BitSet FOLLOW_19_in_signal486 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_NUMBER_in_signal490 = new BitSet(new long[]{0x0000000000200000L});
    public static final BitSet FOLLOW_21_in_signal492 = new BitSet(new long[]{0x0000000000100000L});
    public static final BitSet FOLLOW_20_in_signal494 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_signal505 = new BitSet(new long[]{0x0000000000200000L});
    public static final BitSet FOLLOW_21_in_signal507 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_NUMBER_in_signal511 = new BitSet(new long[]{0x0000000000080000L});
    public static final BitSet FOLLOW_19_in_signal513 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_NUMBER_in_signal517 = new BitSet(new long[]{0x0000000000200000L});
    public static final BitSet FOLLOW_21_in_signal519 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_NUMBER_in_signal523 = new BitSet(new long[]{0x0000000000100000L});
    public static final BitSet FOLLOW_20_in_signal525 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_signal536 = new BitSet(new long[]{0x0000000000200000L});
    public static final BitSet FOLLOW_21_in_signal538 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_NUMBER_in_signal542 = new BitSet(new long[]{0x0000000000080000L});
    public static final BitSet FOLLOW_19_in_signal544 = new BitSet(new long[]{0x0000000000200000L});
    public static final BitSet FOLLOW_21_in_signal546 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_NUMBER_in_signal550 = new BitSet(new long[]{0x0000000000100000L});
    public static final BitSet FOLLOW_20_in_signal552 = new BitSet(new long[]{0x0000000000000002L});

}