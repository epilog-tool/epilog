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
import org.colomoto.biolqm.modifier.perturbation.AbstractPerturbation;
import org.epilogtool.gui.color.ColorUtils;

public class EpitheliumPerturbations {
	private Set<AbstractPerturbation> lstPerturbations;
	private Map<AbstractPerturbation, Color> perturbation2Color;

	public EpitheliumPerturbations() {
		this.lstPerturbations = new HashSet<AbstractPerturbation>();
		this.perturbation2Color = new HashMap<AbstractPerturbation, Color>();
	}

	
	public Map<AbstractPerturbation, Color> getPerturbation2Color() {
		return perturbation2Color;
	}
	public EpitheliumPerturbations clone() {
		EpitheliumPerturbations epiPerturb = new EpitheliumPerturbations();
		
		for (AbstractPerturbation ap : this.lstPerturbations) {
			epiPerturb.addPerturbation(ap);
			epiPerturb.addPerturbationColor(ap, this.perturbation2Color.get(ap));
		}
		return epiPerturb;
	}


	
	/** Add a perturbation to the general perturbations list
	 * @param ap
	 */
	public void addPerturbation(AbstractPerturbation ap) {
		this.lstPerturbations.add(ap);
	}

	
	/** Remove a perturbation from the general perturabation list
	 * @param ap
	 */
	public void delPerturbation(AbstractPerturbation ap) {
		this.lstPerturbations.remove(ap);
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


	public boolean equals(Object o) {
		EpitheliumPerturbations ep = (EpitheliumPerturbations) o;
		Set<AbstractPerturbation> sAllPerturbations = new HashSet<AbstractPerturbation>();
		
		Set<AbstractPerturbation> mpIn = this.lstPerturbations;
		Set<AbstractPerturbation> mpOut = ep.lstPerturbations;
		if (mpIn == null || mpOut == null)
			return false;
		
		for (AbstractPerturbation ap : this.lstPerturbations)
			sAllPerturbations.add(ap);
		for (AbstractPerturbation ap : ep.lstPerturbations)
			sAllPerturbations.add(ap);
		
		for (AbstractPerturbation ap : sAllPerturbations) {

			if (!this.lstPerturbations.contains(ap) && ep.lstPerturbations.contains(ap) )
				return false;
		}
		return true;
	}
	
	
	public Set<AbstractPerturbation> getAllCreatedPerturbations() {
		return this.lstPerturbations;
}

	public List<AbstractPerturbation> getVisiblePerturbations(LogicalModel selModel) {
		
		List<AbstractPerturbation> lPertubations = new ArrayList<AbstractPerturbation>();
		
		if (getAllCreatedPerturbations()!=null) {
			for (AbstractPerturbation ap: getAllCreatedPerturbations()) {
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
		this.lstPerturbations = new HashSet<AbstractPerturbation>();
	}

	public void delAllPerturbationsColors() {
		this.perturbation2Color = new HashMap<AbstractPerturbation, Color>();	
	}
}