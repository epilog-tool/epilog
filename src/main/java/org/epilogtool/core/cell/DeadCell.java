package org.epilogtool.core.cell;

import java.awt.Color;



import org.epilogtool.OptionStore;
import org.epilogtool.common.Txt;

public class DeadCell extends AbstractCell{
	
	private static final Color default_color = Color.black;


	public DeadCell() {
		
		String c = (String) OptionStore.getOption(Txt.get("s_DEAD_CELL"));
		System.out.println(c);
		this.color = ((c == null) ? default_color: Color.decode(c));
		this.name = Txt.get("s_DEAD_CELL");

	}
	
	
	public DeadCell clone() {
		return new DeadCell();
	}

	public boolean isDeadCell(String n) {
		return n.equals(this.name);
	}


}
