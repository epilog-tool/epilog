package org.epilogtool.core.cell;

import java.awt.Color;

import org.colomoto.biolqm.LogicalModel;
import org.epilogtool.common.Txt;

public class AbstractCell{
	
	protected Color color;
	protected String name;
	protected LogicalModel model;
	

	protected AbstractCell() {
	}
	
	public String getName() {
		return this.name;
	}
	
	public Color getColor(){
		return this.color;
	}
	
	public void setColor(Color c) {
		this.color = c;
	}
	
	public AbstractCell clone() {
		return new AbstractCell();
	}
	
	public boolean isLivingCell() {
		if (this.getName().equals(Txt.get("s_LIVING_CELL")))
			return true;
		else return false;
	}

}
