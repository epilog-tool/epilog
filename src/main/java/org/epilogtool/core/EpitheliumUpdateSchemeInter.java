package org.epilogtool.core;

import java.util.HashMap;
import java.util.Map;

import org.colomoto.logicalmodel.LogicalModel;
import org.epilogtool.common.RandomSeedType;
import org.epilogtool.project.ComponentPair;

public class EpitheliumUpdateSchemeInter {
	public static float DEFAULT_ALPHA = (float) 1.0;
	public static float DEFAULT_SIGMA = (float) 1.0;

	private float alphaAsyncParam;
	private Map<ComponentPair, Float> componentPairSigma;
	private UpdateCells updateCells;
	private RandomSeedType randomSeedType;
	private int randomSeed;

	public EpitheliumUpdateSchemeInter(float alpha, Map<ComponentPair, Float> sigmaAsync, UpdateCells updateCells,
			RandomSeedType randomSeedType, int randomSeed) {
		this.alphaAsyncParam = alpha;
		this.componentPairSigma = sigmaAsync;
		this.updateCells = updateCells;
		this.randomSeedType = randomSeedType;
		this.randomSeed = randomSeed;
	}

	public EpitheliumUpdateSchemeInter clone() {
		return new EpitheliumUpdateSchemeInter(this.alphaAsyncParam,
				new HashMap<ComponentPair, Float>(this.componentPairSigma), this.updateCells, this.randomSeedType,
				this.randomSeed);
	}

	// Alpha asynchronism methods
	public void setAlpha(float alpha) {
		this.alphaAsyncParam = alpha;
	}

	public float getAlpha() {
		return this.alphaAsyncParam;
	}

	// UpdateCells methods
	public void setUpdateCells(UpdateCells updateCells) {
		this.updateCells = updateCells;
	}

	public UpdateCells getUpdateCells() {
		return this.updateCells;
	}

	// RandomSeedType methods
	public void setRandomSeedType(RandomSeedType seedType) {
		this.randomSeedType = seedType;
	}

	public RandomSeedType getRandomSeedType() {
		return this.randomSeedType;
	}

	// Random Seed
	public void setRandomSeed(int seed) {
		this.randomSeed = seed;
	}

	public int getRandomSeed() {
		return this.randomSeed;
	}

	// Sigma asynchronism methods
	public void setCPSigma(ComponentPair cp, float sigma) {
		this.componentPairSigma.put(cp, sigma);
	}

	public void addCP(ComponentPair cp) {
		this.setCPSigma(cp, DEFAULT_SIGMA);
	}

	public float getCPSigma(ComponentPair cp) {
		return this.componentPairSigma.get(cp);
	}

	public void removeCPSigma(ComponentPair cp) {
		this.componentPairSigma.remove(cp);
	}

	public void clearAllCPSigma() {
		this.componentPairSigma.clear();
	}

	public Map<ComponentPair, Float> getModelCPSigma(LogicalModel m) {
		Map<ComponentPair, Float> tmpMap = new HashMap<ComponentPair, Float>();
		for (ComponentPair cp : this.componentPairSigma.keySet()) {
			if (cp.getModel() == m) {
				tmpMap.put(cp, this.componentPairSigma.get(cp));
			}
		}
		return tmpMap;
	}

	public boolean containsCPSigma(ComponentPair cp) {
		if (this.componentPairSigma.keySet().contains(cp))
			return true;
		return false;
	}

	public Map<ComponentPair, Float> getCPSigmas() {
		return this.componentPairSigma;
	}

	public boolean equals(Object o) {
		EpitheliumUpdateSchemeInter otherObj = (EpitheliumUpdateSchemeInter) o;
		if (this.alphaAsyncParam != otherObj.getAlpha())
			return false;
		if (!this.componentPairSigma.equals(otherObj.componentPairSigma))
			return false;
		if (!this.updateCells.equals(otherObj.updateCells))
			return false;
		return true;
	}

}
