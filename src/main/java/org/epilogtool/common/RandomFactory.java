package org.epilogtool.common;

import java.util.Random;

public class RandomFactory {

	private static RandomFactory instance;
	private Random random;

	private RandomFactory() {
		this.random = new Random();
	}

	public static RandomFactory getInstance() {
		if (instance == null) {
			instance = new RandomFactory();
		}
		return instance;
	}

	public Random getGenerator() {
		return this.random;
	}

	public void setSeed(long seed) {
		this.random.setSeed(seed);
	}

	public int nextInt() {
		return this.random.nextInt();
	}

	public int nextInt(int n) {
		return this.random.nextInt(n);
	}

	public float nextFloat() {
		return this.random.nextFloat();
	}

	public double nextDouble() {
		return this.random.nextDouble();
	}

}
