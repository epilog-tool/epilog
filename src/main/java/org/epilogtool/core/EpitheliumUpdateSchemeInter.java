package org.epilogtool.core;

import java.util.HashMap;
import java.util.Map;

import org.epilogtool.project.ComponentPair;

public class EpitheliumUpdateSchemeInter {
	public static float DEFAULT_ALPHA = (float) 1.0;
	public static float DEFAULT_SIGMA = (float) 1.0;

	private float alphaAsyncParam;
	private Map<ComponentPair, Float> componentPairSigma;

	public EpitheliumUpdateSchemeInter(float alpha, Map<ComponentPair, Float> sigmaAsync) {
		this.alphaAsyncParam = alpha;
		this.componentPairSigma = sigmaAsync;
	}

	public EpitheliumUpdateSchemeInter clone() {
		return new EpitheliumUpdateSchemeInter(this.alphaAsyncParam, 
				new HashMap<ComponentPair, Float>(this.componentPairSigma));
	}

	public void setAlpha(float alpha) {
		this.alphaAsyncParam = alpha;
	}

	public float getAlpha() {
		return this.alphaAsyncParam;
	}
	
	public void setComponentSigma(ComponentPair cp, float sigma){
		this.componentPairSigma.put(cp, sigma);
	}
	
	public void addComponent(ComponentPair cp){
		this.setComponentSigma(cp, DEFAULT_SIGMA);
	}
	
	public float getComponentSigma(ComponentPair cp){
		return this.componentPairSigma.get(cp);
	}
	
	public boolean equals(Object o) {
		EpitheliumUpdateSchemeInter otherObj = (EpitheliumUpdateSchemeInter) o;
		return this.alphaAsyncParam == otherObj.getAlpha();
	}
}
