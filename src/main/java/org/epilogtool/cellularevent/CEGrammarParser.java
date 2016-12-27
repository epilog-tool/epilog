// $ANTLR 3.3 Nov 30, 2010 12:46:29 src/main/java/org/epilogtool/cellularevent/CEGrammar.g 2016-12-27 15:04:23

package org.epilogtool.cellularevent;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

public class CEGrammarParser extends Parser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "OR", "AND", "NOT", "ID", "NUMBER", "SPACE", "WS", "'('", "')'", "':'"
    };
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

    // delegates
    // delegators


        public CEGrammarParser(TokenStream input) {
            this(input, new RecognizerSharedState());
        }
        public CEGrammarParser(TokenStream input, RecognizerSharedState state) {
            super(input, state);
             
        }
        

    public String[] getTokenNames() { return CEGrammarParser.tokenNames; }
    public String getGrammarFileName() { return "src/main/java/org/epilogtool/cellularevent/CEGrammar.g"; }


      @Override
      public void reportError(RecognitionException e) {
        throw new RuntimeException("I quit!\n" + e.getMessage());
      }



    // $ANTLR start "eval"
    // src/main/java/org/epilogtool/cellularevent/CEGrammar.g:21:1: eval returns [CellularEventExpression value] : exp= expror EOF ;
    public final CellularEventExpression eval() throws RecognitionException {
        CellularEventExpression value = null;

        CellularEventExpression exp = null;


        try {
            // src/main/java/org/epilogtool/cellularevent/CEGrammar.g:25:2: (exp= expror EOF )
            // src/main/java/org/epilogtool/cellularevent/CEGrammar.g:25:4: exp= expror EOF
            {
            pushFollow(FOLLOW_expror_in_eval46);
            exp=expror();

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


    // $ANTLR start "expror"
    // src/main/java/org/epilogtool/cellularevent/CEGrammar.g:28:1: expror returns [CellularEventExpression value] : o1= exprand ( OR o2= exprand )* ;
    public final CellularEventExpression expror() throws RecognitionException {
        CellularEventExpression value = null;

        CellularEventExpression o1 = null;

        CellularEventExpression o2 = null;


        try {
            // src/main/java/org/epilogtool/cellularevent/CEGrammar.g:29:2: (o1= exprand ( OR o2= exprand )* )
            // src/main/java/org/epilogtool/cellularevent/CEGrammar.g:29:4: o1= exprand ( OR o2= exprand )*
            {
            pushFollow(FOLLOW_exprand_in_expror67);
            o1=exprand();

            state._fsp--;

            // src/main/java/org/epilogtool/cellularevent/CEGrammar.g:29:15: ( OR o2= exprand )*
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( (LA1_0==OR) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // src/main/java/org/epilogtool/cellularevent/CEGrammar.g:29:16: OR o2= exprand
            	    {
            	    match(input,OR,FOLLOW_OR_in_expror70); 
            	    pushFollow(FOLLOW_exprand_in_expror74);
            	    o2=exprand();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop1;
                }
            } while (true);

             value = CESpecification.cellularEventOperationOR(o1,o2); 

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
    // $ANTLR end "expror"


    // $ANTLR start "exprand"
    // src/main/java/org/epilogtool/cellularevent/CEGrammar.g:33:1: exprand returns [CellularEventExpression value] : a1= exprnot ( AND a2= exprnot )* ;
    public final CellularEventExpression exprand() throws RecognitionException {
        CellularEventExpression value = null;

        CellularEventExpression a1 = null;

        CellularEventExpression a2 = null;


        try {
            // src/main/java/org/epilogtool/cellularevent/CEGrammar.g:34:2: (a1= exprnot ( AND a2= exprnot )* )
            // src/main/java/org/epilogtool/cellularevent/CEGrammar.g:34:4: a1= exprnot ( AND a2= exprnot )*
            {
            pushFollow(FOLLOW_exprnot_in_exprand98);
            a1=exprnot();

            state._fsp--;

            // src/main/java/org/epilogtool/cellularevent/CEGrammar.g:34:15: ( AND a2= exprnot )*
            loop2:
            do {
                int alt2=2;
                int LA2_0 = input.LA(1);

                if ( (LA2_0==AND) ) {
                    alt2=1;
                }


                switch (alt2) {
            	case 1 :
            	    // src/main/java/org/epilogtool/cellularevent/CEGrammar.g:34:16: AND a2= exprnot
            	    {
            	    match(input,AND,FOLLOW_AND_in_exprand101); 
            	    pushFollow(FOLLOW_exprnot_in_exprand105);
            	    a2=exprnot();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop2;
                }
            } while (true);

             value = CESpecification.cellularEventOperationAND(a1,a2); 

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
    // $ANTLR end "exprand"


    // $ANTLR start "exprnot"
    // src/main/java/org/epilogtool/cellularevent/CEGrammar.g:38:1: exprnot returns [CellularEventExpression value] : (expr= exprparen | NOT expr= exprparen );
    public final CellularEventExpression exprnot() throws RecognitionException {
        CellularEventExpression value = null;

        CellularEventExpression expr = null;


        try {
            // src/main/java/org/epilogtool/cellularevent/CEGrammar.g:39:2: (expr= exprparen | NOT expr= exprparen )
            int alt3=2;
            int LA3_0 = input.LA(1);

            if ( (LA3_0==ID||LA3_0==11) ) {
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
                    // src/main/java/org/epilogtool/cellularevent/CEGrammar.g:39:4: expr= exprparen
                    {
                    pushFollow(FOLLOW_exprparen_in_exprnot128);
                    expr=exprparen();

                    state._fsp--;

                     value = expr; 

                    }
                    break;
                case 2 :
                    // src/main/java/org/epilogtool/cellularevent/CEGrammar.g:41:4: NOT expr= exprparen
                    {
                    match(input,NOT,FOLLOW_NOT_in_exprnot137); 
                    pushFollow(FOLLOW_exprparen_in_exprnot141);
                    expr=exprparen();

                    state._fsp--;

                     value = CESpecification.cellularEventNOT(expr); 

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
    // $ANTLR end "exprnot"


    // $ANTLR start "exprparen"
    // src/main/java/org/epilogtool/cellularevent/CEGrammar.g:45:1: exprparen returns [CellularEventExpression value] : ( '(' expr= expror ')' | expr= node );
    public final CellularEventExpression exprparen() throws RecognitionException {
        CellularEventExpression value = null;

        CellularEventExpression expr = null;


        try {
            // src/main/java/org/epilogtool/cellularevent/CEGrammar.g:46:2: ( '(' expr= expror ')' | expr= node )
            int alt4=2;
            int LA4_0 = input.LA(1);

            if ( (LA4_0==11) ) {
                alt4=1;
            }
            else if ( (LA4_0==ID) ) {
                alt4=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 4, 0, input);

                throw nvae;
            }
            switch (alt4) {
                case 1 :
                    // src/main/java/org/epilogtool/cellularevent/CEGrammar.g:46:4: '(' expr= expror ')'
                    {
                    match(input,11,FOLLOW_11_in_exprparen160); 
                    pushFollow(FOLLOW_expror_in_exprparen164);
                    expr=expror();

                    state._fsp--;

                    match(input,12,FOLLOW_12_in_exprparen166); 
                     value = expr; 

                    }
                    break;
                case 2 :
                    // src/main/java/org/epilogtool/cellularevent/CEGrammar.g:48:4: expr= node
                    {
                    pushFollow(FOLLOW_node_in_exprparen177);
                    expr=node();

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
    // $ANTLR end "exprparen"


    // $ANTLR start "node"
    // src/main/java/org/epilogtool/cellularevent/CEGrammar.g:52:1: node returns [CellularEventExpression value] : (id= ID | id= ID ':' t= NUMBER );
    public final CellularEventExpression node() throws RecognitionException {
        CellularEventExpression value = null;

        Token id=null;
        Token t=null;

        try {
            // src/main/java/org/epilogtool/cellularevent/CEGrammar.g:53:2: (id= ID | id= ID ':' t= NUMBER )
            int alt5=2;
            int LA5_0 = input.LA(1);

            if ( (LA5_0==ID) ) {
                int LA5_1 = input.LA(2);

                if ( (LA5_1==13) ) {
                    alt5=2;
                }
                else if ( (LA5_1==EOF||(LA5_1>=OR && LA5_1<=AND)||LA5_1==12) ) {
                    alt5=1;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 5, 1, input);

                    throw nvae;
                }
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 5, 0, input);

                throw nvae;
            }
            switch (alt5) {
                case 1 :
                    // src/main/java/org/epilogtool/cellularevent/CEGrammar.g:53:4: id= ID
                    {
                    id=(Token)match(input,ID,FOLLOW_ID_in_node198); 
                     value = CESpecification.cellularEventNode((id!=null?id.getText():null), "1"); 

                    }
                    break;
                case 2 :
                    // src/main/java/org/epilogtool/cellularevent/CEGrammar.g:55:4: id= ID ':' t= NUMBER
                    {
                    id=(Token)match(input,ID,FOLLOW_ID_in_node209); 
                    match(input,13,FOLLOW_13_in_node211); 
                    t=(Token)match(input,NUMBER,FOLLOW_NUMBER_in_node215); 
                     value = CESpecification.cellularEventNode((id!=null?id.getText():null), (t!=null?t.getText():null)); 

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
    // $ANTLR end "node"

    // Delegated rules


 

    public static final BitSet FOLLOW_expror_in_eval46 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_eval48 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_exprand_in_expror67 = new BitSet(new long[]{0x0000000000000012L});
    public static final BitSet FOLLOW_OR_in_expror70 = new BitSet(new long[]{0x00000000000008C0L});
    public static final BitSet FOLLOW_exprand_in_expror74 = new BitSet(new long[]{0x0000000000000012L});
    public static final BitSet FOLLOW_exprnot_in_exprand98 = new BitSet(new long[]{0x0000000000000022L});
    public static final BitSet FOLLOW_AND_in_exprand101 = new BitSet(new long[]{0x00000000000008C0L});
    public static final BitSet FOLLOW_exprnot_in_exprand105 = new BitSet(new long[]{0x0000000000000022L});
    public static final BitSet FOLLOW_exprparen_in_exprnot128 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NOT_in_exprnot137 = new BitSet(new long[]{0x0000000000000880L});
    public static final BitSet FOLLOW_exprparen_in_exprnot141 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_11_in_exprparen160 = new BitSet(new long[]{0x00000000000008C0L});
    public static final BitSet FOLLOW_expror_in_exprparen164 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_12_in_exprparen166 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_node_in_exprparen177 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_node198 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_node209 = new BitSet(new long[]{0x0000000000002000L});
    public static final BitSet FOLLOW_13_in_node211 = new BitSet(new long[]{0x0000000000000100L});
    public static final BitSet FOLLOW_NUMBER_in_node215 = new BitSet(new long[]{0x0000000000000002L});

}