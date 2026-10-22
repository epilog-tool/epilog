package org.epilogtool.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.colomoto.biolqm.LogicalModel;

public class EpitheliumPhenotypes {
	private final Map<LogicalModel, LinkedHashSet<Phenotype>> phenotypes;

	public EpitheliumPhenotypes() {
		this.phenotypes = new LinkedHashMap<LogicalModel, LinkedHashSet<Phenotype>>();
	}

	public void addModel(LogicalModel model) {
		this.phenotypes.put(model, new LinkedHashSet<Phenotype>());
	}

	public void removeModel(LogicalModel model) {
		this.phenotypes.remove(model);
	}

	public Set<LogicalModel> getModelSet() {
		return Collections.unmodifiableSet(this.phenotypes.keySet());
	}

	public Set<Phenotype> getPhenotypes(LogicalModel model) {
		return this.phenotypes.get(model);
	}

	public Map<LogicalModel, LinkedHashSet<Phenotype>> getPhenotypes() {
		return Collections.unmodifiableMap(this.phenotypes);
	}

	public void addPhenotype(LogicalModel model, String name, String pattern) {
		this.phenotypes.computeIfAbsent(model, key -> new LinkedHashSet<Phenotype>())
				.add(new Phenotype(name, pattern));
	}

	public void addPhenotype(LogicalModel model, Phenotype phenotype) {
		this.phenotypes.computeIfAbsent(model, key -> new LinkedHashSet<Phenotype>()).add(phenotype);
	}

	public void addPhenoSet(LogicalModel model, Set<Phenotype> values) {
		this.phenotypes.computeIfAbsent(model, key -> new LinkedHashSet<Phenotype>()).addAll(values);
	}

	public void removePhenotype(LogicalModel model, String name, String pattern) {
		Set<Phenotype> values = this.phenotypes.get(model);
		if (values != null) {
			values.remove(new Phenotype(name, pattern));
		}
	}

	public Map<Phenotype, Set<Phenotype>> getOverlap(LogicalModel model) {
		Map<Phenotype, Set<Phenotype>> overlaps = new LinkedHashMap<Phenotype, Set<Phenotype>>();
		Set<Phenotype> values = this.phenotypes.get(model);
		if (values == null) {
			return overlaps;
		}

		List<Phenotype> ordered = new ArrayList<Phenotype>(values);
		Collections.sort(ordered, Collections.reverseOrder());
		for (int i = 0; i < ordered.size(); i++) {
			Phenotype phenotype = ordered.get(i);
			Set<Phenotype> matches = new LinkedHashSet<Phenotype>();
			for (int j = i + 1; j < ordered.size(); j++) {
				Phenotype candidate = ordered.get(j);
				if (phenotype.matches(candidate.pattern)) {
					matches.add(candidate);
				}
			}
			if (!matches.isEmpty()) {
				overlaps.put(phenotype, matches);
			}
		}
		return overlaps;
	}

	public EpitheliumPhenotypes clone() {
		EpitheliumPhenotypes copy = new EpitheliumPhenotypes();
		for (Map.Entry<LogicalModel, LinkedHashSet<Phenotype>> entry : this.phenotypes.entrySet()) {
			for (Phenotype phenotype : entry.getValue()) {
				copy.addPhenotype(entry.getKey(), phenotype.clone());
			}
		}
		return copy;
	}

	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof EpitheliumPhenotypes)) {
			return false;
		}
		return this.phenotypes.equals(((EpitheliumPhenotypes) other).phenotypes);
	}

	@Override
	public int hashCode() {
		return this.phenotypes.hashCode();
	}

	public static class Phenotype implements Comparable<Phenotype> {
		private final String name;
		private final String pattern;

		public Phenotype(String name, String pattern) {
			this.name = Objects.requireNonNull(name);
			this.pattern = Objects.requireNonNull(pattern);
		}

		public String getName() {
			return this.name;
		}

		public String getPheno() {
			return this.pattern;
		}

		public boolean match(byte[] state) {
			if (state.length != this.pattern.length()) {
				return false;
			}
			for (int i = 0; i < state.length; i++) {
				char expected = this.pattern.charAt(i);
				if (expected != '*' && expected != Character.forDigit(state[i], 10)) {
					return false;
				}
			}
			return true;
		}

		private boolean matches(String otherPattern) {
			if (otherPattern.length() != this.pattern.length()) {
				return false;
			}
			for (int i = 0; i < this.pattern.length(); i++) {
				char left = this.pattern.charAt(i);
				char right = otherPattern.charAt(i);
				if (left != '*' && right != '*' && left != right) {
					return false;
				}
			}
			return true;
		}

		@Override
		public int compareTo(Phenotype other) {
			return Integer.compare(wildcards(this.pattern), wildcards(other.pattern));
		}

		private int wildcards(String value) {
			int count = 0;
			for (int i = 0; i < value.length(); i++) {
				if (value.charAt(i) == '*') {
					count++;
				}
			}
			return count;
		}

		public Phenotype clone() {
			return new Phenotype(this.name, this.pattern);
		}

		@Override
		public boolean equals(Object other) {
			if (this == other) {
				return true;
			}
			if (!(other instanceof Phenotype)) {
				return false;
			}
			Phenotype phenotype = (Phenotype) other;
			return this.name.equals(phenotype.name) && this.pattern.equals(phenotype.pattern);
		}

		@Override
		public int hashCode() {
			return Objects.hash(this.name, this.pattern);
		}
	}
}