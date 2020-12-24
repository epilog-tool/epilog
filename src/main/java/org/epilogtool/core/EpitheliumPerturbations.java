package org.epilogtool.core;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.colomoto.biolqm.LogicalModel;
import org.colomoto.biolqm.NodeInfo;
import org.colomoto.biolqm.modifier.perturbation.LogicalModelPerturbation;
import org.epilogtool.gui.color.ColorUtils;

public class EpitheliumPerturbations {
	private Set<LogicalModelPerturbation> lstPerturbations;
	private Map<LogicalModelPerturbation, Color> perturbation2Color;

	public EpitheliumPerturbations() {
		this.lstPerturbations = new HashSet<LogicalModelPerturbation>();
		this.perturbation2Color = new HashMap<LogicalModelPerturbation, Color>();
	}

	
	public Map<LogicalModelPerturbation, Color> getPerturbation2Color() {
		return perturbation2Color;
	}
	public EpitheliumPerturbations clone() {
		EpitheliumPerturbations epiPerturb = new EpitheliumPerturbations();
		
		for (LogicalModelPerturbation ap : this.lstPerturbations) {
			epiPerturb.addPerturbation(ap);
			epiPerturb.addPerturbationColor(ap, this.perturbation2Color.get(ap));
		}
		return epiPerturb;
	}


	
	/** Add a perturbation to the general perturbations list
	 * @param ap
	 */
	public void addPerturbation(LogicalModelPerturbation ap) {
		this.lstPerturbations.add(ap);
	}

	
	/** Remove a perturbation from the general perturabation list
	 * @param ap
	 */
	public void delPerturbation(LogicalModelPerturbation ap) {
		this.lstPerturbations.remove(ap);
	}

	
	public void addPerturbationColor(LogicalModelPerturbation ap,
			Color c) {
		this.perturbation2Color.put(ap, c);
	}
	
	public Color getPerturbationColor(LogicalModelPerturbation ap) {
		if (this.perturbation2Color.get(ap)==null) {
			Color c = ColorUtils.random();
			this.perturbation2Color.put(ap,c);
		}
		return this.perturbation2Color.get(ap);
	}


	public boolean equals(Object o) {
		EpitheliumPerturbations ep = (EpitheliumPerturbations) o;
		Set<LogicalModelPerturbation> sAllPerturbations = new HashSet<LogicalModelPerturbation>();
		
		Set<LogicalModelPerturbation> mpIn = this.lstPerturbations;
		Set<LogicalModelPerturbation> mpOut = ep.lstPerturbations;
		if (mpIn == null || mpOut == null)
			return false;
		
		for (LogicalModelPerturbation ap : this.lstPerturbations)
			sAllPerturbations.add(ap);
		for (LogicalModelPerturbation ap : ep.lstPerturbations)
			sAllPerturbations.add(ap);
		
		for (LogicalModelPerturbation ap : sAllPerturbations) {

			if (!this.lstPerturbations.contains(ap) && ep.lstPerturbations.contains(ap) )
				return false;
		}
		return true;
	}
	
	
	public Set<LogicalModelPerturbation> getAllCreatedPerturbations() {
		return this.lstPerturbations;
}

	public List<LogicalModelPerturbation> getVisiblePerturbations(LogicalModel selModel) {
		
		List<LogicalModelPerturbation> lPertubations = new ArrayList<LogicalModelPerturbation>();
		
		if (getAllCreatedPerturbations()!=null) {
			for (LogicalModelPerturbation ap: getAllCreatedPerturbations()) {
				for (NodeInfo node: selModel.getComponents()) {
					if (ap.affectsNode(node)) {
						lPertubations.add(ap);
						continue;
					}
				}
				
			}
		}
		
		return lPertubations;
	}

	public void delAllPerturbations() {
		this.lstPerturbations = new HashSet<LogicalModelPerturbation>();
	}

	public void delAllPerturbationsColors() {
		this.perturbation2Color = new HashMap<LogicalModelPerturbation, Color>();	
	}
}