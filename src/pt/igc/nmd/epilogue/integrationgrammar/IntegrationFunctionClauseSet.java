package pt.igc.nmd.epilogue.integrationgrammar;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.colomoto.logicalmodel.NodeInfo;

public class IntegrationFunctionClauseSet {

	private Set<IntegrationFunctionClause> clauseSet = null;

	public IntegrationFunctionClauseSet() {

	}

	public void addClause(IntegrationFunctionClause clause) {
		if (this.clauseSet == null)
			this.clauseSet = new HashSet<IntegrationFunctionClause>();
		this.clauseSet.add(clause);
	}

	public Set<IntegrationFunctionClause> getClauses() {
		return this.clauseSet;
	}

	public IntegrationFunctionClauseSet conjunctionWith(
			IntegrationFunctionClauseSet set) {
		if (set == null)
			return this;

		// System.err.println(this.asString() + "\nAND\n" + set.asString());

		IntegrationFunctionClauseSet result = new IntegrationFunctionClauseSet();

		if (set.isImpossible() || this.isImpossible())
			result.setImpossible();
		else if (this.isTautological() && set.isTautological())
			result.setTautological();
		else if (this.isTautological())
			for (IntegrationFunctionClause clause : set.getClauses())
				result.addClause(clause);
		else if (set.isTautological())
			for (IntegrationFunctionClause clause : this.getClauses())
				result.addClause(clause);
		else {
			result.setImpossible();
			for (IntegrationFunctionClause clause : set.getClauses()) {
				IntegrationFunctionClauseSet intermediate = this
						.conjunctionWith(clause);
				if (!intermediate.isImpossible())
					for (IntegrationFunctionClause toAdd : intermediate
							.getClauses())
						result = result.disjunctionWith(toAdd);
			}
		}
		// System.err.println("RESULT=" + result.asString());

		return result;

	}

	public IntegrationFunctionClauseSet disjunctionWith(
			IntegrationFunctionClauseSet set) {

		if (set == null)
			return this;

		IntegrationFunctionClauseSet result = new IntegrationFunctionClauseSet();

		// System.err.println(this.asString() + "\nOR\n" + set.asString());

		if (this.isImpossible() && set.isImpossible())
			result.setImpossible();
		else if (this.isTautological() || set.isTautological())
			result.setTautological();
		else if (this.isImpossible())
			for (IntegrationFunctionClause clause : set.getClauses())
				result.addClause(clause);
		else if (set.isImpossible())
			for (IntegrationFunctionClause clause : this.getClauses())
				result.addClause(clause);
		else {
			result.setImpossible();

			for (IntegrationFunctionClause clause : set.getClauses()) {
				IntegrationFunctionClauseSet intermediate = this
						.disjunctionWith(clause);
				if (!intermediate.isImpossible())
					for (IntegrationFunctionClause toAdd : intermediate
							.getClauses())
						result = result.disjunctionWith(toAdd);
			}
		}
		// System.err.println("RESULT=" + result.asString());
		return result;

	}

	public IntegrationFunctionClauseSet conjunctionWith(
			IntegrationFunctionClause clause) {

		IntegrationFunctionClauseSet result = new IntegrationFunctionClauseSet();

//		System.err.println(this.asString() + "\nAND\n" + clause.asString());

		if (clause.isImpossible() || this.isImpossible())
			result.setImpossible();
		else if (this.isTautological() && clause.isTautological())
			result.setTautological();
		else if (this.isTautological())
			result.addClause(clause);
		else if (clause.isTautological())
			for (IntegrationFunctionClause c : this.getClauses())
				result.addClause(c);
		else {
			result.setImpossible();
			for (IntegrationFunctionClause c : this.getClauses())
				result = result.disjunctionWith(c.conjunctionWith(clause));
		}
//		System.err.println("RESULT=" + result.asString());
		return result;
	}

	public IntegrationFunctionClauseSet disjunctionWith(
			IntegrationFunctionClause clause) {

		IntegrationFunctionClauseSet result = new IntegrationFunctionClauseSet();
		// System.err.println(this.asString() + "\nOR\n" + clause.asString());

		if (clause.isImpossible() && this.isImpossible()) {
			// System.err.println("Everyone is impossible");
			result.setImpossible();
		} else if (clause.isTautological() || this.isTautological()) {
			// System.err.println("Someone is tautological");
			result.setTautological();
		} else if (clause.isImpossible()) {
			// System.err.println("Clause is impossible");
			for (IntegrationFunctionClause c : this.getClauses())
				result.addClause(c);
		} else if (this.isImpossible()) {
			// System.err.println("Set is impossible");
			result.addClause(clause);
		} else {
			for (IntegrationFunctionClause c : this.getClauses())
				result.addClause(c);
			result.addClause(clause);
		}

		// System.err.println("RESULT=" + result.asString());
		return result;

	}

	public boolean isImpossible() {
		return this.clauseSet == null;
	}

	public boolean isTautological() {
		return this.clauseSet != null && this.clauseSet.isEmpty();
	}

	public void setImpossible() {
		this.clauseSet = null;
	}

	public void setTautological() {
		this.clauseSet = new HashSet<IntegrationFunctionClause>();
	}

	public IntegrationFunctionClauseSet negate() {
		List<IntegrationFunctionClauseSet> toConjugate = new ArrayList<IntegrationFunctionClauseSet>();

		for (IntegrationFunctionClause clause : this.clauseSet) {
//			System.out.println("About to negate clause : " + clause.asString());

			IntegrationFunctionClauseSet negation = new IntegrationFunctionClauseSet();

			for (NodeInfo node : clause.getKeySet()) {
				for (byte value = 0; value <= node.getMax(); value++)
					if (value != clause.getConstraintValue(node).byteValue()) {
						IntegrationFunctionClause novelClause = new IntegrationFunctionClause();
						novelClause.addConstraint(node, value);
						negation.addClause(novelClause);
					}
			}

//			System.out.println("Negation is " + negation.asString());

			toConjugate.add(negation);
		}

		IntegrationFunctionClauseSet result = new IntegrationFunctionClauseSet();
		result.setTautological();

		for (IntegrationFunctionClauseSet set : toConjugate) {
//			System.err.println(result.asString() + " >> AND <<"
//					+ set.asString());
			result = result.conjunctionWith(set);
//			System.err.println("Produces : " + result.asString());
		}

//		System.err.println("Negation: " + result.asString());

		return result;
	}

	public String asString() {
		String out = "\n([[DISJUCTION]]\n";

		if (this.isImpossible())
			out += "IMPOSSIBLE";
		else if (this.isTautological())
			out += "UNIVERSAL";
		else
			for (IntegrationFunctionClause clause : this.getClauses())
				out += clause.asString();

		return out += "\n)\n";
	}

}
