package org.epilogtool.core.cell;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.colomoto.biolqm.LogicalModel;
import org.colomoto.biolqm.LogicalModelImpl;
import org.colomoto.biolqm.NodeInfo;
import org.colomoto.mddlib.MDDManager;
import org.colomoto.mddlib.internal.MDDStoreImpl;
import org.epilogtool.OptionStore;
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

	public LogicalModel getModel() {
		return this.model;
	}
	
	public Color getColor(){
		return this.color;
	}
	
	public void setColor(Color c) {
		this.color = c;
	}

}
