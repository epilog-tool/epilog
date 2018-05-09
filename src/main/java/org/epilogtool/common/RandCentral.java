package org.epilogtool.common;

import java.util.Random;

public class RandCentral {

	private static RandCentral instance;
	private Random random;

	private RandCentral() {
		this.random = new Random();
	}

	public static RandCentral getInstance() {
		if (instance == null) {
			instance = new RandCentral();
		}
		return instance;
	}

	public void setSeed(long seed) {
		this.random = new Random(seed);
	}

	public Random getNewGenerator() {
		return new Random(this.nextInt());
	}

	public Random getNewGenerator(int seed) {
		return new Random(seed);
	}

	public int nextInt() {
		return this.random.nextInt();
	}

	public int nextInt(int n) {
		return this.random.nextInt(n);
	}
}
