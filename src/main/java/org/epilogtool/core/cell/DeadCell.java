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

public class DeadCell extends AbstractCell{
	
	private static final Color default_color = Color.black;
	
	private static DeadCell deadCell = null;


	private DeadCell() {
		
		String c = (String) OptionStore.getOption(Txt.get("s_DEAD_CELL"));
		System.out.println(c);
		this.color = ((c == null) ? default_color: Color.decode(c));
		this.name = Txt.get("s_DEAD_CELL");
		
		this.model = null;
	}
	
	
	public static DeadCell getInstance() {
		if (deadCell == null) {
			deadCell = new DeadCell();
		}
		return deadCell;
	}
	

	public boolean isDeadCell(String n) {
		return n.equals(this.name);
	}


}
