// $ANTLR 3.1.3 Mar 18, 2009 10:09:25 src/main/java/org.epilogtool/integration/IntegrationGrammar.g 2014-03-28 15:59:40

package org.epilogtool.integration;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

public class IntegrationGrammarParser extends Parser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "OR", "AND", "ID", "ENUMBER", "RANGE", "NOT", "'('", "','", "')'"
    };
    public static final int T__12=12;
    public static final int T__11=11;
    public static final int ENUMBER=7;
    public static final int OR=4;
    public static final int T__10=10;
    public static final int RANGE=8;
    public static final int NOT=9;
    public static final int ID=6;
    public static final int AND=5;
    public static final int EOF=-1;

    // delegates
    // delegators


        public IntegrationGrammarParser(TokenStream input) {
            this(input, new RecognizerSharedState());
        }
        public IntegrationGrammarParser(TokenStream input, RecognizerSharedState state) {
            super(input, state);
             
        }
        

    public String[] getTokenNames() { return IntegrationGrammarParser.tokenNames; }
    public String getGrammarFileName() { return "src/main/java/org.epilogtool/integration/IntegrationGrammar.g"; }



    // $ANTLR start "eval"
    // src/main/java/org.epilogtool/integration/IntegrationGrammar.g:8:1: eval returns [IntegrationFunctionSpecification.IntegrationExpression value] : exp= expression ;
    public final IntegrationFunctionSpecification.IntegrationExpression eval() throws RecognitionException {
        IntegrationFunctionSpecification.IntegrationExpression value = null;

        IntegrationFunctionSpecification.IntegrationExpression exp = null;


        try {
            // src/main/java/org.epilogtool/integration/IntegrationGrammar.g:9:2: (exp= expression )
            // src/main/java/org.epilogtool/integration/IntegrationGrammar.g:9:4: exp= expression
            {
            pushFollow(FOLLOW_expression_in_eval25);
            exp=expression();

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


    // $ANTLR start "expression"
    // src/main/java/org.epilogtool/integration/IntegrationGrammar.g:12:1: expression returns [IntegrationFunctionSpecification.IntegrationExpression value] : or= disjunction ;
    public final IntegrationFunctionSpecification.IntegrationExpression expression() throws RecognitionException {
        IntegrationFunctionSpecification.IntegrationExpression value = null;

        IntegrationFunctionSpecification.IntegrationExpression or = null;


        try {
            // src/main/java/org.epilogtool/integration/IntegrationGrammar.g:13:2: (or= disjunction )
            // src/main/java/org.epilogtool/integration/IntegrationGrammar.g:13:4: or= disjunction
            {
            pushFollow(FOLLOW_disjunction_in_expression46);
            or=disjunction();

            state._fsp--;

             value = or; 

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
    // $ANTLR end "expression"


    // $ANTLR start "disjunction"
    // src/main/java/org.epilogtool/integration/IntegrationGrammar.g:16:1: disjunction returns [IntegrationFunctionSpecification.IntegrationExpression value] : c1= conjunction ( OR c2= conjunction )* ;
    public final IntegrationFunctionSpecification.IntegrationExpression disjunction() throws RecognitionException {
        IntegrationFunctionSpecification.IntegrationExpression value = null;

        IntegrationFunctionSpecification.IntegrationExpression c1 = null;

        IntegrationFunctionSpecification.IntegrationExpression c2 = null;


        try {
            // src/main/java/org.epilogtool/integration/IntegrationGrammar.g:17:2: (c1= conjunction ( OR c2= conjunction )* )
            // src/main/java/org.epilogtool/integration/IntegrationGrammar.g:17:4: c1= conjunction ( OR c2= conjunction )*
            {
            pushFollow(FOLLOW_conjunction_in_disjunction66);
            c1=conjunction();

            state._fsp--;

            // src/main/java/org.epilogtool/integration/IntegrationGrammar.g:17:19: ( OR c2= conjunction )*
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( (LA1_0==OR) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // src/main/java/org.epilogtool/integration/IntegrationGrammar.g:17:20: OR c2= conjunction
            	    {
            	    match(input,OR,FOLLOW_OR_in_disjunction69); 
            	    pushFollow(FOLLOW_conjunction_in_disjunction73);
            	    c2=conjunction();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop1;
                }
            } while (true);

            value = IntegrationFunctionSpecification.createDisjunction(c1,c2); 

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
    // $ANTLR end "disjunction"


    // $ANTLR start "conjunction"
    // src/main/java/org.epilogtool/integration/IntegrationGrammar.g:20:1: conjunction returns [IntegrationFunctionSpecification.IntegrationExpression value ] : a1= atom ( AND a2= atom )* ;
    public final IntegrationFunctionSpecification.IntegrationExpression conjunction() throws RecognitionException {
        IntegrationFunctionSpecification.IntegrationExpression value = null;

        IntegrationFunctionSpecification.IntegrationExpression a1 = null;

        IntegrationFunctionSpecification.IntegrationExpression a2 = null;


        try {
            // src/main/java/org.epilogtool/integration/IntegrationGrammar.g:21:2: (a1= atom ( AND a2= atom )* )
            // src/main/java/org.epilogtool/integration/IntegrationGrammar.g:21:4: a1= atom ( AND a2= atom )*
            {
            pushFollow(FOLLOW_atom_in_conjunction99);
            a1=atom();

            state._fsp--;

            // src/main/java/org.epilogtool/integration/IntegrationGrammar.g:21:12: ( AND a2= atom )*
            loop2:
            do {
                int alt2=2;
                int LA2_0 = input.LA(1);

                if ( (LA2_0==AND) ) {
                    alt2=1;
                }


                switch (alt2) {
            	case 1 :
            	    // src/main/java/org.epilogtool/integration/IntegrationGrammar.g:21:13: AND a2= atom
            	    {
            	    match(input,AND,FOLLOW_AND_in_conjunction102); 
            	    pushFollow(FOLLOW_atom_in_conjunction106);
            	    a2=atom();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop2;
                }
            } while (true);

            value = IntegrationFunctionSpecification.createConjunction(a1,a2); 

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
    // $ANTLR end "conjunction"


    // $ANTLR start "atom"
    // src/main/java/org.epilogtool/integration/IntegrationGrammar.g:25:1: atom returns [ IntegrationFunctionSpecification.IntegrationExpression value] : (id= ID '(' threshold= ENUMBER ',' min= ENUMBER ',' max= ENUMBER ')' | id= ID '(' threshold= RANGE ',' min= ENUMBER ',' max= ENUMBER ')' | id= ID '(' threshold= ENUMBER ',' min= ENUMBER ',' max= ENUMBER ',' dist= ENUMBER ')' | id= ID '(' threshold= RANGE ',' min= ENUMBER ',' max= ENUMBER ',' dist= ENUMBER ')' | id= ID '(' threshold= ENUMBER ',' min= ENUMBER ',' max= ENUMBER ',' dist= RANGE ')' | id= ID '(' threshold= RANGE ',' min= ENUMBER ',' max= ENUMBER ',' dist= RANGE ')' | '(' exp= expression ')' | NOT a= atom );
    public final IntegrationFunctionSpecification.IntegrationExpression atom() throws RecognitionException {
        IntegrationFunctionSpecification.IntegrationExpression value = null;

        Token id=null;
        Token threshold=null;
        Token min=null;
        Token max=null;
        Token dist=null;
        IntegrationFunctionSpecification.IntegrationExpression exp = null;

        IntegrationFunctionSpecification.IntegrationExpression a = null;


        try {
            // src/main/java/org.epilogtool/integration/IntegrationGrammar.g:26:2: (id= ID '(' threshold= ENUMBER ',' min= ENUMBER ',' max= ENUMBER ')' | id= ID '(' threshold= RANGE ',' min= ENUMBER ',' max= ENUMBER ')' | id= ID '(' threshold= ENUMBER ',' min= ENUMBER ',' max= ENUMBER ',' dist= ENUMBER ')' | id= ID '(' threshold= RANGE ',' min= ENUMBER ',' max= ENUMBER ',' dist= ENUMBER ')' | id= ID '(' threshold= ENUMBER ',' min= ENUMBER ',' max= ENUMBER ',' dist= RANGE ')' | id= ID '(' threshold= RANGE ',' min= ENUMBER ',' max= ENUMBER ',' dist= RANGE ')' | '(' exp= expression ')' | NOT a= atom )
            int alt3=8;
            alt3 = dfa3.predict(input);
            switch (alt3) {
                case 1 :
                    // src/main/java/org.epilogtool/integration/IntegrationGrammar.g:26:4: id= ID '(' threshold= ENUMBER ',' min= ENUMBER ',' max= ENUMBER ')'
                    {
                    id=(Token)match(input,ID,FOLLOW_ID_in_atom132); 
                    match(input,10,FOLLOW_10_in_atom134); 
                    threshold=(Token)match(input,ENUMBER,FOLLOW_ENUMBER_in_atom138); 
                    match(input,11,FOLLOW_11_in_atom140); 
                    min=(Token)match(input,ENUMBER,FOLLOW_ENUMBER_in_atom144); 
                    match(input,11,FOLLOW_11_in_atom146); 
                    max=(Token)match(input,ENUMBER,FOLLOW_ENUMBER_in_atom150); 
                    match(input,12,FOLLOW_12_in_atom152); 
                     value = IntegrationFunctionSpecification.createAtom((id!=null?id.getText():null),(threshold!=null?threshold.getText():null),(max!=null?max.getText():null),(min!=null?min.getText():null));

                    }
                    break;
                case 2 :
                    // src/main/java/org.epilogtool/integration/IntegrationGrammar.g:28:4: id= ID '(' threshold= RANGE ',' min= ENUMBER ',' max= ENUMBER ')'
                    {
                    id=(Token)match(input,ID,FOLLOW_ID_in_atom163); 
                    match(input,10,FOLLOW_10_in_atom165); 
                    threshold=(Token)match(input,RANGE,FOLLOW_RANGE_in_atom169); 
                    match(input,11,FOLLOW_11_in_atom171); 
                    min=(Token)match(input,ENUMBER,FOLLOW_ENUMBER_in_atom175); 
                    match(input,11,FOLLOW_11_in_atom177); 
                    max=(Token)match(input,ENUMBER,FOLLOW_ENUMBER_in_atom181); 
                    match(input,12,FOLLOW_12_in_atom183); 
                     value = IntegrationFunctionSpecification.createAtom((id!=null?id.getText():null),(threshold!=null?threshold.getText():null),(max!=null?max.getText():null),(min!=null?min.getText():null));

                    }
                    break;
                case 3 :
                    // src/main/java/org.epilogtool/integration/IntegrationGrammar.g:30:4: id= ID '(' threshold= ENUMBER ',' min= ENUMBER ',' max= ENUMBER ',' dist= ENUMBER ')'
                    {
                    id=(Token)match(input,ID,FOLLOW_ID_in_atom194); 
                    match(input,10,FOLLOW_10_in_atom196); 
                    threshold=(Token)match(input,ENUMBER,FOLLOW_ENUMBER_in_atom200); 
                    match(input,11,FOLLOW_11_in_atom202); 
                    min=(Token)match(input,ENUMBER,FOLLOW_ENUMBER_in_atom206); 
                    match(input,11,FOLLOW_11_in_atom208); 
                    max=(Token)match(input,ENUMBER,FOLLOW_ENUMBER_in_atom212); 
                    match(input,11,FOLLOW_11_in_atom214); 
                    dist=(Token)match(input,ENUMBER,FOLLOW_ENUMBER_in_atom218); 
                    match(input,12,FOLLOW_12_in_atom220); 
                     value = IntegrationFunctionSpecification.createAtom((id!=null?id.getText():null),(threshold!=null?threshold.getText():null),(min!=null?min.getText():null),(max!=null?max.getText():null),(dist!=null?dist.getText():null));

                    }
                    break;
                case 4 :
                    // src/main/java/org.epilogtool/integration/IntegrationGrammar.g:32:4: id= ID '(' threshold= RANGE ',' min= ENUMBER ',' max= ENUMBER ',' dist= ENUMBER ')'
                    {
                    id=(Token)match(input,ID,FOLLOW_ID_in_atom230); 
                    match(input,10,FOLLOW_10_in_atom232); 
                    threshold=(Token)match(input,RANGE,FOLLOW_RANGE_in_atom236); 
                    match(input,11,FOLLOW_11_in_atom238); 
                    min=(Token)match(input,ENUMBER,FOLLOW_ENUMBER_in_atom242); 
                    match(input,11,FOLLOW_11_in_atom244); 
                    max=(Token)match(input,ENUMBER,FOLLOW_ENUMBER_in_atom248); 
                    match(input,11,FOLLOW_11_in_atom250); 
                    dist=(Token)match(input,ENUMBER,FOLLOW_ENUMBER_in_atom254); 
                    match(input,12,FOLLOW_12_in_atom256); 
                     value = IntegrationFunctionSpecification.createAtom((id!=null?id.getText():null),(threshold!=null?threshold.getText():null),(min!=null?min.getText():null),(max!=null?max.getText():null),(dist!=null?dist.getText():null));

                    }
                    break;
                case 5 :
                    // src/main/java/org.epilogtool/integration/IntegrationGrammar.g:34:4: id= ID '(' threshold= ENUMBER ',' min= ENUMBER ',' max= ENUMBER ',' dist= RANGE ')'
                    {
                    id=(Token)match(input,ID,FOLLOW_ID_in_atom266); 
                    match(input,10,FOLLOW_10_in_atom268); 
                    threshold=(Token)match(input,ENUMBER,FOLLOW_ENUMBER_in_atom272); 
                    match(input,11,FOLLOW_11_in_atom274); 
                    min=(Token)match(input,ENUMBER,FOLLOW_ENUMBER_in_atom278); 
                    match(input,11,FOLLOW_11_in_atom280); 
                    max=(Token)match(input,ENUMBER,FOLLOW_ENUMBER_in_atom284); 
                    match(input,11,FOLLOW_11_in_atom286); 
                    dist=(Token)match(input,RANGE,FOLLOW_RANGE_in_atom290); 
                    match(input,12,FOLLOW_12_in_atom292); 
                     value = IntegrationFunctionSpecification.createAtom((id!=null?id.getText():null),(threshold!=null?threshold.getText():null),(min!=null?min.getText():null),(max!=null?max.getText():null),(dist!=null?dist.getText():null));

                    }
                    break;
                case 6 :
                    // src/main/java/org.epilogtool/integration/IntegrationGrammar.g:36:4: id= ID '(' threshold= RANGE ',' min= ENUMBER ',' max= ENUMBER ',' dist= RANGE ')'
                    {
                    id=(Token)match(input,ID,FOLLOW_ID_in_atom302); 
                    match(input,10,FOLLOW_10_in_atom304); 
                    threshold=(Token)match(input,RANGE,FOLLOW_RANGE_in_atom308); 
                    match(input,11,FOLLOW_11_in_atom310); 
                    min=(Token)match(input,ENUMBER,FOLLOW_ENUMBER_in_atom314); 
                    match(input,11,FOLLOW_11_in_atom316); 
                    max=(Token)match(input,ENUMBER,FOLLOW_ENUMBER_in_atom320); 
                    match(input,11,FOLLOW_11_in_atom322); 
                    dist=(Token)match(input,RANGE,FOLLOW_RANGE_in_atom326); 
                    match(input,12,FOLLOW_12_in_atom328); 
                     value = IntegrationFunctionSpecification.createAtom((id!=null?id.getText():null),(threshold!=null?threshold.getText():null),(min!=null?min.getText():null),(max!=null?max.getText():null),(dist!=null?dist.getText():null));

                    }
                    break;
                case 7 :
                    // src/main/java/org.epilogtool/integration/IntegrationGrammar.g:38:3: '(' exp= expression ')'
                    {
                    match(input,10,FOLLOW_10_in_atom335); 
                    pushFollow(FOLLOW_expression_in_atom339);
                    exp=expression();

                    state._fsp--;

                    match(input,12,FOLLOW_12_in_atom341); 
                     value = IntegrationFunctionSpecification.createAtom(exp);

                    }
                    break;
                case 8 :
                    // src/main/java/org.epilogtool/integration/IntegrationGrammar.g:39:4: NOT a= atom
                    {
                    match(input,NOT,FOLLOW_NOT_in_atom348); 
                    pushFollow(FOLLOW_atom_in_atom352);
                    a=atom();

                    state._fsp--;

                     value = IntegrationFunctionSpecification.createNegation(a);

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
    // $ANTLR end "atom"

    // Delegated rules


    protected DFA3 dfa3 = new DFA3(this);
    static final String DFA3_eotS =
        "\27\uffff";
    static final String DFA3_eofS =
        "\27\uffff";
    static final String DFA3_minS =
        "\1\6\1\12\2\uffff\1\7\2\13\2\7\2\13\2\7\2\13\1\uffff\1\7\1\uffff"+
        "\1\7\4\uffff";
    static final String DFA3_maxS =
        "\2\12\2\uffff\1\10\2\13\2\7\2\13\2\7\2\14\1\uffff\1\10\1\uffff\1"+
        "\10\4\uffff";
    static final String DFA3_acceptS =
        "\2\uffff\1\7\1\10\13\uffff\1\1\1\uffff\1\2\1\uffff\1\3\1\5\1\4\1"+
        "\6";
    static final String DFA3_specialS =
        "\27\uffff}>";
    static final String[] DFA3_transitionS = {
            "\1\1\2\uffff\1\3\1\2",
            "\1\4",
            "",
            "",
            "\1\5\1\6",
            "\1\7",
            "\1\10",
            "\1\11",
            "\1\12",
            "\1\13",
            "\1\14",
            "\1\15",
            "\1\16",
            "\1\20\1\17",
            "\1\22\1\21",
            "",
            "\1\23\1\24",
            "",
            "\1\25\1\26",
            "",
            "",
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
            return "25:1: atom returns [ IntegrationFunctionSpecification.IntegrationExpression value] : (id= ID '(' threshold= ENUMBER ',' min= ENUMBER ',' max= ENUMBER ')' | id= ID '(' threshold= RANGE ',' min= ENUMBER ',' max= ENUMBER ')' | id= ID '(' threshold= ENUMBER ',' min= ENUMBER ',' max= ENUMBER ',' dist= ENUMBER ')' | id= ID '(' threshold= RANGE ',' min= ENUMBER ',' max= ENUMBER ',' dist= ENUMBER ')' | id= ID '(' threshold= ENUMBER ',' min= ENUMBER ',' max= ENUMBER ',' dist= RANGE ')' | id= ID '(' threshold= RANGE ',' min= ENUMBER ',' max= ENUMBER ',' dist= RANGE ')' | '(' exp= expression ')' | NOT a= atom );";
        }
    }
 

    public static final BitSet FOLLOW_expression_in_eval25 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_disjunction_in_expression46 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_conjunction_in_disjunction66 = new BitSet(new long[]{0x0000000000000012L});
    public static final BitSet FOLLOW_OR_in_disjunction69 = new BitSet(new long[]{0x0000000000000640L});
    public static final BitSet FOLLOW_conjunction_in_disjunction73 = new BitSet(new long[]{0x0000000000000012L});
    public static final BitSet FOLLOW_atom_in_conjunction99 = new BitSet(new long[]{0x0000000000000022L});
    public static final BitSet FOLLOW_AND_in_conjunction102 = new BitSet(new long[]{0x0000000000000640L});
    public static final BitSet FOLLOW_atom_in_conjunction106 = new BitSet(new long[]{0x0000000000000022L});
    public static final BitSet FOLLOW_ID_in_atom132 = new BitSet(new long[]{0x0000000000000400L});
    public static final BitSet FOLLOW_10_in_atom134 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_ENUMBER_in_atom138 = new BitSet(new long[]{0x0000000000000800L});
    public static final BitSet FOLLOW_11_in_atom140 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_ENUMBER_in_atom144 = new BitSet(new long[]{0x0000000000000800L});
    public static final BitSet FOLLOW_11_in_atom146 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_ENUMBER_in_atom150 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_12_in_atom152 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_atom163 = new BitSet(new long[]{0x0000000000000400L});
    public static final BitSet FOLLOW_10_in_atom165 = new BitSet(new long[]{0x0000000000000100L});
    public static final BitSet FOLLOW_RANGE_in_atom169 = new BitSet(new long[]{0x0000000000000800L});
    public static final BitSet FOLLOW_11_in_atom171 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_ENUMBER_in_atom175 = new BitSet(new long[]{0x0000000000000800L});
    public static final BitSet FOLLOW_11_in_atom177 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_ENUMBER_in_atom181 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_12_in_atom183 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_atom194 = new BitSet(new long[]{0x0000000000000400L});
    public static final BitSet FOLLOW_10_in_atom196 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_ENUMBER_in_atom200 = new BitSet(new long[]{0x0000000000000800L});
    public static final BitSet FOLLOW_11_in_atom202 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_ENUMBER_in_atom206 = new BitSet(new long[]{0x0000000000000800L});
    public static final BitSet FOLLOW_11_in_atom208 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_ENUMBER_in_atom212 = new BitSet(new long[]{0x0000000000000800L});
    public static final BitSet FOLLOW_11_in_atom214 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_ENUMBER_in_atom218 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_12_in_atom220 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_atom230 = new BitSet(new long[]{0x0000000000000400L});
    public static final BitSet FOLLOW_10_in_atom232 = new BitSet(new long[]{0x0000000000000100L});
    public static final BitSet FOLLOW_RANGE_in_atom236 = new BitSet(new long[]{0x0000000000000800L});
    public static final BitSet FOLLOW_11_in_atom238 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_ENUMBER_in_atom242 = new BitSet(new long[]{0x0000000000000800L});
    public static final BitSet FOLLOW_11_in_atom244 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_ENUMBER_in_atom248 = new BitSet(new long[]{0x0000000000000800L});
    public static final BitSet FOLLOW_11_in_atom250 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_ENUMBER_in_atom254 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_12_in_atom256 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_atom266 = new BitSet(new long[]{0x0000000000000400L});
    public static final BitSet FOLLOW_10_in_atom268 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_ENUMBER_in_atom272 = new BitSet(new long[]{0x0000000000000800L});
    public static final BitSet FOLLOW_11_in_atom274 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_ENUMBER_in_atom278 = new BitSet(new long[]{0x0000000000000800L});
    public static final BitSet FOLLOW_11_in_atom280 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_ENUMBER_in_atom284 = new BitSet(new long[]{0x0000000000000800L});
    public static final BitSet FOLLOW_11_in_atom286 = new BitSet(new long[]{0x0000000000000100L});
    public static final BitSet FOLLOW_RANGE_in_atom290 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_12_in_atom292 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_atom302 = new BitSet(new long[]{0x0000000000000400L});
    public static final BitSet FOLLOW_10_in_atom304 = new BitSet(new long[]{0x0000000000000100L});
    public static final BitSet FOLLOW_RANGE_in_atom308 = new BitSet(new long[]{0x0000000000000800L});
    public static final BitSet FOLLOW_11_in_atom310 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_ENUMBER_in_atom314 = new BitSet(new long[]{0x0000000000000800L});
    public static final BitSet FOLLOW_11_in_atom316 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_ENUMBER_in_atom320 = new BitSet(new long[]{0x0000000000000800L});
    public static final BitSet FOLLOW_11_in_atom322 = new BitSet(new long[]{0x0000000000000100L});
    public static final BitSet FOLLOW_RANGE_in_atom326 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_12_in_atom328 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_10_in_atom335 = new BitSet(new long[]{0x0000000000000640L});
    public static final BitSet FOLLOW_expression_in_atom339 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_12_in_atom341 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NOT_in_atom348 = new BitSet(new long[]{0x0000000000000640L});
    public static final BitSet FOLLOW_atom_in_atom352 = new BitSet(new long[]{0x0000000000000002L});

}