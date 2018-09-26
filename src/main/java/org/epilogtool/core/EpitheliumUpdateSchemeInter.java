package org.epilogtool.core;

import org.epilogtool.common.EnumRandomSeed;

public class EpitheliumUpdateSchemeInter {
	public static float DEFAULT_ALPHA = (float) 0.0;
	public static float DEFAULT_SIGMA = (float) 0.0;

	private float alphaAsyncParam;
	private UpdateCells updateCells;
	private EnumRandomSeed randomSeedType;
	private int randomSeed;

	public EpitheliumUpdateSchemeInter(float alpha, UpdateCells updateCells, EnumRandomSeed randomSeedType,
			int randomSeed) {
		this.alphaAsyncParam = alpha;
		this.updateCells = updateCells;
		this.randomSeedType = randomSeedType;
		this.randomSeed = randomSeed;
	}

	public EpitheliumUpdateSchemeInter clone() {
		return new EpitheliumUpdateSchemeInter(this.alphaAsyncParam, this.updateCells, this.randomSeedType,
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
	public void setRandomSeedType(EnumRandomSeed seedType) {
		this.randomSeedType = seedType;
	}

	public EnumRandomSeed getRandomSeedType() {
		return this.randomSeedType;
	}

	// Random Seed
	public void setRandomSeed(int seed) {
		this.randomSeed = seed;
	}

	public int getRandomSeed() {
		return this.randomSeed;
	}

	public boolean equals(Object o) {
		EpitheliumUpdateSchemeInter otherObj = (EpitheliumUpdateSchemeInter) o;
		if (this.alphaAsyncParam != otherObj.getAlpha())
			return false;
		if (!this.updateCells.equals(otherObj.updateCells))
			return false;
		if (!this.randomSeedType.equals(otherObj.randomSeedType))
			return false;
		return true;
	}

}
