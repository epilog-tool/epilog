package org.epilogtool.core;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.colomoto.biolqm.LogicalModel;
import org.colomoto.biolqm.NodeInfo;
import org.colomoto.biolqm.modifier.perturbation.AbstractPerturbation;
import org.epilogtool.gui.color.ColorUtils;

public class EpitheliumPerturbations {
	private Map<LogicalModel, ModelPerturbations> perturbations;
	private Set<AbstractPerturbation> lstPerturbations;
	private Map<AbstractPerturbation, Color> perturbation2Color;

	public EpitheliumPerturbations() {
		this.perturbations = new HashMap<LogicalModel, ModelPerturbations>();
		this.lstPerturbations = new HashSet<AbstractPerturbation>();
		this.perturbation2Color = new HashMap<AbstractPerturbation, Color>();
	}

	public EpitheliumPerturbations clone() {
		EpitheliumPerturbations epiPerturb = new EpitheliumPerturbations();
		for (LogicalModel m : this.getModelSet())
			epiPerturb.addModelPerturbation(m, this.getModelPerturbations(m)
					.clone());
		for (AbstractPerturbation ap : this.lstPerturbations)
			epiPerturb.addPerturbation(ap);
		return epiPerturb;
	}

	
	/**
	 * Check if a given model has perturbations
	 * @param m
	 * @return
	 */
	public boolean hasModel(LogicalModel m) {
		return this.perturbations.containsKey(m);
	}

	/**
	 * Add a model to the perturbation perturbed models
	 * @param m
	 */
	public void addModel(LogicalModel m) {
		this.perturbations.put(m, new ModelPerturbations());
	}

	/**
	 * Add a petrurbation associated with a model 
	 * @param m
	 * @param mp
	 */
	public void addModelPerturbation(LogicalModel m, ModelPerturbations mp) {
		this.perturbations.put(m, mp);
	}
	
	/** Add a perturbation to the general perturbations list
	 * @param ap
	 */
	public void addPerturbation(AbstractPerturbation ap) {
		this.lstPerturbations.add(ap);
	}

	public void removeModel(LogicalModel m) {
		if (this.perturbations.containsKey(m))
			this.perturbations.remove(m);
	}

	public Set<LogicalModel> getModelSet() {
		return Collections.unmodifiableSet(this.perturbations.keySet());
	}

	public void addPerturbation(LogicalModel m, AbstractPerturbation ap) {
		if (!this.perturbations.containsKey(m)) {
			ModelPerturbations mp = new ModelPerturbations();
			this.perturbations.put(m, mp);
		}
		this.perturbations.get(m).addPerturbation(ap);
	}

	public void delPerturbation(LogicalModel m, AbstractPerturbation ap) {
		if (this.perturbations.containsKey(m)) {
			ModelPerturbations mp = this.perturbations.get(m);
			mp.delPerturbation(ap);
		}
	}
	
	/** Remove a perturbation from the general perturabation list
	 * @param ap
	 */
	public void delPerturbation(AbstractPerturbation ap) {
		this.lstPerturbations.remove(ap);
	}

	public void addPerturbationColor(LogicalModel m, AbstractPerturbation ap,
			Color c) {
		this.perturbations.get(m).addPerturbationColor(ap, c);
	}
	
	public void addPerturbationColor(AbstractPerturbation ap,
			Color c) {
		this.perturbation2Color.put(ap, c);
	}
	
	public Color getPerturbationColor(AbstractPerturbation ap) {
		if (this.perturbation2Color.get(ap)==null) {
			Color c = ColorUtils.random();
			this.perturbation2Color.put(ap,c);
		}
		return this.perturbation2Color.get(ap);
	}

	public ModelPerturbations getModelPerturbations(LogicalModel m) {
		return this.perturbations.get(m);
	}

	public boolean equals(Object o) {
		EpitheliumPerturbations ep = (EpitheliumPerturbations) o;
		Set<LogicalModel> sAllModels = new HashSet<LogicalModel>();
		sAllModels.addAll(this.getModelSet());
		sAllModels.addAll(ep.getModelSet());
		for (LogicalModel m : sAllModels) {
			// ModelPerturbations in one and not the other
			ModelPerturbations mpIn = this.getModelPerturbations(m);
			ModelPerturbations mpOut = ep.getModelPerturbations(m);
			if (mpIn == null || mpOut == null)
				return false;
			if (!mpIn.equals(mpOut))
				return false;
		}
		return true;
	}
	
	
	public List<AbstractPerturbation> getAllCreatedPerturbations() {
		
		List<AbstractPerturbation> lPertubations = new ArrayList<AbstractPerturbation>();
		
		if (perturbations!=null) {
		for (LogicalModel m : perturbations.keySet()) {
			for (AbstractPerturbation p : perturbations.get(m).getAllPerturbations()) {
				lPertubations.add(p);
			}
			
	}}
		return lPertubations;
}

	public List<AbstractPerturbation> getVisiblePerturbations(LogicalModel selModel) {
		
		List<AbstractPerturbation> lPertubations = new ArrayList<AbstractPerturbation>();
		
		if (getAllCreatedPerturbations()!=null) {
			for (AbstractPerturbation ap: getAllCreatedPerturbations()) {
				for (NodeInfo node: selModel.getComponents()) {
					if (ap.affectsNode(node)) {
						lPertubations.add(ap);
						System.out.println(selModel.getComponents() + " : " + ap);
						continue;
					}
				}
				
				
				
			}
		}
		
		return lPertubations;
	}
}