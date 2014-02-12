package org.ginsim.epilog.gui.color;

import java.awt.Color;

import org.colomoto.logicalmodel.NodeInfo;

public class EpiColorComponent extends EpiColor {
	private NodeInfo component;
	
	public Color getColorAtLevel(int level) {
		// TODO check old code
		return this.color;
	}
}
