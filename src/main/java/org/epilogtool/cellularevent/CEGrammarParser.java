// $ANTLR 3.3 Nov 30, 2010 12:46:29 src/main/java/org/epilogtool/cellularevent/CEGrammar.g 2016-12-22 14:50:06

package org.epilogtool.cellularevent;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

public class CEGrammarParser extends Parser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "OR", "AND", "NOT", "ID", "NUMBER", "':'"
    };
    public static final int EOF=-1;
    public static final int T__9=9;
    public static final int OR=4;
    public static final int AND=5;
    public static final int NOT=6;
    public static final int ID=7;
    public static final int NUMBER=8;

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



    // $ANTLR start "eval"
    // src/main/java/org/epilogtool/cellularevent/CEGrammar.g:7:1: eval returns [CellularEventExpression value] : exp= expror ;
    public final CellularEventExpression eval() throws RecognitionException {
        CellularEventExpression value = null;

        CellularEventExpression exp = null;


        try {
            // src/main/java/org/epilogtool/cellularevent/CEGrammar.g:11:2: (exp= expror )
            // src/main/java/org/epilogtool/cellularevent/CEGrammar.g:11:4: exp= expror
            {
            pushFollow(FOLLOW_expror_in_eval28);
            exp=expror();

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


    // $ANTLR start "expror"
    // src/main/java/org/epilogtool/cellularevent/CEGrammar.g:13:1: expror returns [CellularEventExpression value] : o1= exprand ( OR o2= exprand )* ;
    public final CellularEventExpression expror() throws RecognitionException {
        CellularEventExpression value = null;

        CellularEventExpression o1 = null;

        CellularEventExpression o2 = null;


        try {
            // src/main/java/org/epilogtool/cellularevent/CEGrammar.g:14:2: (o1= exprand ( OR o2= exprand )* )
            // src/main/java/org/epilogtool/cellularevent/CEGrammar.g:14:4: o1= exprand ( OR o2= exprand )*
            {
            pushFollow(FOLLOW_exprand_in_expror46);
            o1=exprand();

            state._fsp--;

            // src/main/java/org/epilogtool/cellularevent/CEGrammar.g:14:15: ( OR o2= exprand )*
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( (LA1_0==OR) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // src/main/java/org/epilogtool/cellularevent/CEGrammar.g:14:16: OR o2= exprand
            	    {
            	    match(input,OR,FOLLOW_OR_in_expror49); 
            	    pushFollow(FOLLOW_exprand_in_expror53);
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
    // src/main/java/org/epilogtool/cellularevent/CEGrammar.g:18:1: exprand returns [CellularEventExpression value] : a1= exprnot ( AND a2= exprnot )* ;
    public final CellularEventExpression exprand() throws RecognitionException {
        CellularEventExpression value = null;

        CellularEventExpression a1 = null;

        CellularEventExpression a2 = null;


        try {
            // src/main/java/org/epilogtool/cellularevent/CEGrammar.g:19:2: (a1= exprnot ( AND a2= exprnot )* )
            // src/main/java/org/epilogtool/cellularevent/CEGrammar.g:19:4: a1= exprnot ( AND a2= exprnot )*
            {
            pushFollow(FOLLOW_exprnot_in_exprand77);
            a1=exprnot();

            state._fsp--;

            // src/main/java/org/epilogtool/cellularevent/CEGrammar.g:19:15: ( AND a2= exprnot )*
            loop2:
            do {
                int alt2=2;
                int LA2_0 = input.LA(1);

                if ( (LA2_0==AND) ) {
                    alt2=1;
                }


                switch (alt2) {
            	case 1 :
            	    // src/main/java/org/epilogtool/cellularevent/CEGrammar.g:19:16: AND a2= exprnot
            	    {
            	    match(input,AND,FOLLOW_AND_in_exprand80); 
            	    pushFollow(FOLLOW_exprnot_in_exprand84);
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
    // src/main/java/org/epilogtool/cellularevent/CEGrammar.g:23:1: exprnot returns [CellularEventExpression value] : (expr= node | NOT expr= node );
    public final CellularEventExpression exprnot() throws RecognitionException {
        CellularEventExpression value = null;

        CellularEventExpression expr = null;


        try {
            // src/main/java/org/epilogtool/cellularevent/CEGrammar.g:24:2: (expr= node | NOT expr= node )
            int alt3=2;
            int LA3_0 = input.LA(1);

            if ( (LA3_0==ID) ) {
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
                    // src/main/java/org/epilogtool/cellularevent/CEGrammar.g:24:4: expr= node
                    {
                    pushFollow(FOLLOW_node_in_exprnot107);
                    expr=node();

                    state._fsp--;

                     value = expr; 

                    }
                    break;
                case 2 :
                    // src/main/java/org/epilogtool/cellularevent/CEGrammar.g:26:4: NOT expr= node
                    {
                    match(input,NOT,FOLLOW_NOT_in_exprnot116); 
                    pushFollow(FOLLOW_node_in_exprnot120);
                    expr=node();

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


    // $ANTLR start "node"
    // src/main/java/org/epilogtool/cellularevent/CEGrammar.g:30:1: node returns [CellularEventExpression value] : (id= ID | id= ID ':' t= NUMBER );
    public final CellularEventExpression node() throws RecognitionException {
        CellularEventExpression value = null;

        Token id=null;
        Token t=null;

        try {
            // src/main/java/org/epilogtool/cellularevent/CEGrammar.g:31:2: (id= ID | id= ID ':' t= NUMBER )
            int alt4=2;
            int LA4_0 = input.LA(1);

            if ( (LA4_0==ID) ) {
                int LA4_1 = input.LA(2);

                if ( (LA4_1==9) ) {
                    alt4=2;
                }
                else if ( (LA4_1==EOF||(LA4_1>=OR && LA4_1<=AND)) ) {
                    alt4=1;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 4, 1, input);

                    throw nvae;
                }
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 4, 0, input);

                throw nvae;
            }
            switch (alt4) {
                case 1 :
                    // src/main/java/org/epilogtool/cellularevent/CEGrammar.g:31:4: id= ID
                    {
                    id=(Token)match(input,ID,FOLLOW_ID_in_node141); 
                     value = CESpecification.cellularEventNode((id!=null?id.getText():null), "1"); 

                    }
                    break;
                case 2 :
                    // src/main/java/org/epilogtool/cellularevent/CEGrammar.g:33:4: id= ID ':' t= NUMBER
                    {
                    id=(Token)match(input,ID,FOLLOW_ID_in_node152); 
                    match(input,9,FOLLOW_9_in_node154); 
                    t=(Token)match(input,NUMBER,FOLLOW_NUMBER_in_node158); 
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


 

    public static final BitSet FOLLOW_expror_in_eval28 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_exprand_in_expror46 = new BitSet(new long[]{0x0000000000000012L});
    public static final BitSet FOLLOW_OR_in_expror49 = new BitSet(new long[]{0x00000000000000C0L});
    public static final BitSet FOLLOW_exprand_in_expror53 = new BitSet(new long[]{0x0000000000000012L});
    public static final BitSet FOLLOW_exprnot_in_exprand77 = new BitSet(new long[]{0x0000000000000022L});
    public static final BitSet FOLLOW_AND_in_exprand80 = new BitSet(new long[]{0x00000000000000C0L});
    public static final BitSet FOLLOW_exprnot_in_exprand84 = new BitSet(new long[]{0x0000000000000022L});
    public static final BitSet FOLLOW_node_in_exprnot107 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NOT_in_exprnot116 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_node_in_exprnot120 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_node141 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_node152 = new BitSet(new long[]{0x0000000000000200L});
    public static final BitSet FOLLOW_9_in_node154 = new BitSet(new long[]{0x0000000000000100L});
    public static final BitSet FOLLOW_NUMBER_in_node158 = new BitSet(new long[]{0x0000000000000002L});

}