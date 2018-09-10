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

public class InvalidCell  extends AbstractCell{
	private static final Color default_color = Color.gray;

	public InvalidCell() {
		String c = (String) OptionStore.getOption("s_INVALID_POSITION");
		this.color = ((c == null) ? default_color: Color.decode(c));
		this.name = Txt.get("s_INVALID_POSITION");

	}


	public boolean isInvalidPosition(String n) {
		return n.equals(this.name);
	}
	
	public InvalidCell clone() {
		return new InvalidCell();
	}
	
}
