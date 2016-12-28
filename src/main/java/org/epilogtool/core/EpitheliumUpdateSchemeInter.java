package org.epilogtool.core;

import java.util.HashMap;
import java.util.Map;

import org.colomoto.logicalmodel.LogicalModel;
import org.epilogtool.project.ComponentPair;

public class EpitheliumUpdateSchemeInter {
	public static float DEFAULT_ALPHA = (float) 1.0;
	public static float DEFAULT_SIGMA = (float) 1.0;

	private float alphaAsyncParam;
	private Map<ComponentPair, Float> componentPairSigma;
	private UpdateOrder updateOrder;

	public EpitheliumUpdateSchemeInter(float alpha, Map<ComponentPair, Float> sigmaAsync, UpdateOrder updateOrder) {
		this.alphaAsyncParam = alpha;
		this.componentPairSigma = sigmaAsync;
		this.updateOrder = updateOrder;
	}

	public EpitheliumUpdateSchemeInter clone() {
		return new EpitheliumUpdateSchemeInter(this.alphaAsyncParam, 
				new HashMap<ComponentPair, Float>(this.componentPairSigma), this.updateOrder);
	}

	// Alpha asynchronism methods
	
	public void setAlpha(float alpha) {
		this.alphaAsyncParam = alpha;
	}
	public float getAlpha() {
		return this.alphaAsyncParam;
	}
	
	// Update Mode methods
	
	public void setUpdateOrder(UpdateOrder updateOrder) {
		this.updateOrder = updateOrder;
	}
	public UpdateOrder getUpdateOrder() {
		return this.updateOrder ;
	}

	// Sigma asynchronism methods
	
	public void setCPSigma(ComponentPair cp, float sigma){
		this.componentPairSigma.put(cp, sigma);
	}
	
	public void addCP(ComponentPair cp){
		this.setCPSigma(cp, DEFAULT_SIGMA);
	}
	
	public float getCPSigma(ComponentPair cp){
		return this.componentPairSigma.get(cp);
	}
	
	public void removeCPSigma(ComponentPair cp){
		this.componentPairSigma.remove(cp);
	}
	
	public void clearAllCPSigma(){
		this.componentPairSigma.clear();
	}

	public Map<ComponentPair, Float> getModelCPSigma(LogicalModel m) {
		Map<ComponentPair, Float> tmpMap = new HashMap<ComponentPair, Float>();
		for (ComponentPair cp : this.componentPairSigma.keySet()) {
			if (cp.getModel() == m){
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
		if (!this.updateOrder.equals(otherObj.updateOrder))
			return false;
		return true;
	}
}
