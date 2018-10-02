package org.epilogtool.core.algorithms;

public abstract class AbstractAlgorithm{
	
	protected String name;
	
	public String getName() {
		return this.name;
	}
	
	public abstract AbstractAlgorithm clone();
}
