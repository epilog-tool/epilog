package org.epilogtool.core;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.colomoto.biolqm.modifier.perturbation.AbstractPerturbation;

public class EpitheliumPerturbations {

	private Set<AbstractPerturbation> perturbations;
	private Map<AbstractPerturbation, Color> usedPerturbations;

	public EpitheliumPerturbations() {
		this.perturbations = new HashSet<AbstractPerturbation>();
		this.usedPerturbations = new HashMap<AbstractPerturbation, Color>();
	}
	
	public boolean hasPerturbation(AbstractPerturbation p) {
		return this.perturbations.contains(p);
	}
	
	
	//Perturbations with the same name are not added. They already exist
	public void addPerturbation(AbstractPerturbation p) {
		boolean ctrl = true;
		for (AbstractPerturbation ap: this.perturbations) {
			if (ap.toString().equals(p.toString())) ctrl = false;
		}
		if (ctrl) this.perturbations.add(p);
	}
	
	public void removePerturbation(AbstractPerturbation p) {
		this.perturbations.remove(p);
	}

	public Set<AbstractPerturbation> getPerturbations() {
		return this.perturbations;
	}
	
	
	public EpitheliumPerturbations clone() {
		EpitheliumPerturbations epiPerturb = new EpitheliumPerturbations();
		for (AbstractPerturbation p : this.perturbations)
			epiPerturb.addPerturbation(p);
		return epiPerturb;
	}
	

	public Color getPerturbationColor(AbstractPerturbation ap) {
		return this.usedPerturbations.get(ap);
	}
	
	public void addPerturbationColor(AbstractPerturbation ap, Color c) {
		this.usedPerturbations.put(ap, c);
	}

	public void delPerturbationColor(AbstractPerturbation ap) {
		this.usedPerturbations.remove(ap);
	}
	
	

	public boolean equals(Object o) {
		EpitheliumPerturbations ep = (EpitheliumPerturbations) o;
		List<AbstractPerturbation> apList = new ArrayList<AbstractPerturbation>();
		apList.addAll(this.perturbations);
		
		for (AbstractPerturbation ap : apList) {
			if (!this.perturbations.contains(ap)
					|| !ep.getPerturbations().contains(ap))
				return false;
		
		
		if (this.usedPerturbations.get(ap) != null
				&& ep.getPerturbationColor(ap) == null
				|| this.usedPerturbations.get(ap) == null
				&& ep.getPerturbationColor(ap) != null)
			return false;
		}

		return true;
	}


//	public boolean hasModel(LogicalModel m) {
//		return this.perturbations.containsKey(m);
//	}

//	public void addModel(LogicalModel m) {
//		this.perturbations.put(m, new ModelPerturbations());
//	}

//	public void addModelPerturbation(LogicalModel m, ModelPerturbations mp) {
//		this.perturbations.put(m, mp);
//	}

//	public void removeModel(LogicalModel m) {
//		if (this.perturbations.containsKey(m))
//			this.perturbations.remove(m);
//	}

//	public Set<LogicalModel> getModelSet() {
//		return Collections.unmodifiableSet(this.perturbations.keySet());
//	}

//	public void addPerturbation(LogicalModel m, AbstractPerturbation ap) {
//		if (!this.perturbations.containsKey(m)) {
//			ModelPerturbations mp = new ModelPerturbations();
//			this.perturbations.put(m, mp);
//		}
//		this.perturbations.get(m).addPerturbation(ap);
//	}

//	public void delPerturbation(LogicalModel m, AbstractPerturbation ap) {
//		if (this.perturbations.containsKey(m)) {
//			ModelPerturbations mp = this.perturbations.get(m);
//			mp.delPerturbation(ap);
//		}
//	}

//	public void addPerturbationColor(LogicalModel m, AbstractPerturbation ap,
//			Color c) {
//		this.perturbations.get(m).addPerturbationColor(ap, c);
//	}

//	public ModelPerturbations getModelPerturbations(LogicalModel m) {
//		return this.perturbations.get(m);
//	}

//	public boolean equals(Object o) {
//		EpitheliumPerturbations ep = (EpitheliumPerturbations) o;
//		Set<LogicalModel> sAllModels = new HashSet<LogicalModel>();
//		sAllModels.addAll(this.getModelSet());
//		sAllModels.addAll(ep.getModelSet());
//		for (LogicalModel m : sAllModels) {
//			// ModelPerturbations in one and not the other
//			ModelPerturbations mpIn = this.getModelPerturbations(m);
//			ModelPerturbations mpOut = ep.getModelPerturbations(m);
//			if (mpIn == null || mpOut == null)
//				return false;
//			if (!mpIn.equals(mpOut))
//				return false;
//		}
//		return true;
//	}
	
	
//	public List<AbstractPerturbation> getAllCreatedPerturbations() {
//		
//		List<AbstractPerturbation> lPertubations = new ArrayList<AbstractPerturbation>();
//		
//		if (perturbations!=null) {
//		for (LogicalModel m : perturbations.keySet()) {
//			for (AbstractPerturbation p : perturbations.get(m).getAllPerturbations()) {
//				lPertubations.add(p);
//			}
//			
//	}}
//		return lPertubations;
//}
}