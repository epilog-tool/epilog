package org.epilogtool.core.cell;

import java.awt.Color;

import org.epilogtool.OptionStore;
import org.epilogtool.common.Txt;

public class EmptyCell extends AbstractCell {
	private static final Color default_color = Color.DARK_GRAY;




	public EmptyCell() {
		String c = (String) OptionStore.getOption(Txt.get("s_EMPTY_POSITION"));
		this.color = ((c == null) ? default_color: Color.decode(c));
		this.name = Txt.get("s_EMPTY_POSITION");

	}

	public EmptyCell clone() {
		return new EmptyCell();
	}

	public boolean isEmptyModel(String n) {
		return n.equals(this.name);
	}

}
