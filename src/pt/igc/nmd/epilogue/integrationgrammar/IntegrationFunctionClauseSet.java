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

		if (set == null || this.isImpossible())
			return this;

		if (set.isImpossible())
			this.setImpossible();
		else if (set.isTautological())
			return this;
		else if (this.isTautological())
			for (IntegrationFunctionClause clause : set.getClauses())
				this.addClause(clause);
		else {
			Set<IntegrationFunctionClause> clausesA = this.getClauses();
			Set<IntegrationFunctionClause> clausesB = set.getClauses();
			this.setImpossible();
			for (IntegrationFunctionClause clauseA : clausesA)
				for (IntegrationFunctionClause clauseB : clausesB)
					this.disjunctionWith(clauseA.conjunctionWith(clauseB));
		}

		return this;

	}

	public IntegrationFunctionClauseSet disjunctionWith(
			IntegrationFunctionClauseSet set) {

		if (set == null || set.isImpossible())
			return this;

		else if (this.isImpossible())
			for (IntegrationFunctionClause clause : set.getClauses())
				this.addClause(clause);
		else if (this.isTautological())
			return this;
		else if (set.isTautological())
			this.setTautological();
		else {
			for (IntegrationFunctionClause clause : set.getClauses())
				this.disjunctionWith(clause);

		}

		return this;

	}

	public IntegrationFunctionClauseSet conjunctionWith(
			IntegrationFunctionClause clause) {

		if (this.isImpossible() || clause.isTautological())
			return this;
		else if (clause.isImpossible())
			this.setImpossible();
		else if (this.isTautological())
			this.addClause(clause);
		else {
			Set<IntegrationFunctionClause> clauses = this.getClauses();
			this.setImpossible();

			for (IntegrationFunctionClause c : clauses)
				this.disjunctionWith(c.conjunctionWith(clause));

		}

		return this;
	}

	public IntegrationFunctionClauseSet disjunctionWith(
			IntegrationFunctionClause clause) {

		if (clause.isImpossible() || this.isTautological())
			return this;
		else if (clause.isTautological())
			this.setTautological();
		else
			this.addClause(clause);

		return this;

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

		if (!isImpossible())
			for (IntegrationFunctionClause clause : this.clauseSet) {
				IntegrationFunctionClauseSet negation = new IntegrationFunctionClauseSet();

				for (NodeInfo node : clause.getKeySet()) {
					for (byte value = 0; value <= node.getMax(); value++)
						if (value != clause.getConstraintValue(node)
								.byteValue()) {
							IntegrationFunctionClause novelClause = new IntegrationFunctionClause();
							novelClause.addConstraint(node, value);
							negation.addClause(novelClause);
						}
				}

				toConjugate.add(negation);
			}

		IntegrationFunctionClauseSet result = new IntegrationFunctionClauseSet();
		result.setTautological();

		for (IntegrationFunctionClauseSet set : toConjugate)
			result = result.conjunctionWith(set);

		this.clauseSet = result.clauseSet;
		return this;
	}

	public String toString() {
		String out = "\n([[DISJUCTION]]\n";

		if (this.isImpossible())
			out += "IMPOSSIBLE";
		else if (this.isTautological())
			out += "UNIVERSAL";
		else
			for (IntegrationFunctionClause clause : this.getClauses())
				out += clause.toString();

		return out += "\n)\n";
	}

}
