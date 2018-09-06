package org.epilogtool.core;

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

public class InvalidPosition {
	private static final Color default_color = Color.gray;
	private Color color;
	private String name;
	private LogicalModel model;

	private static InvalidPosition emptyModel = null;

	private InvalidPosition() {
		String c = (String) OptionStore.getOption("s_INVALID_POSITION");
		this.color = ((c == null) ? default_color: Color.decode(c));
		this.name = Txt.get("s_INVALID_POSITION");
		List<NodeInfo> vars = new ArrayList<NodeInfo>();
		MDDManager manager = new MDDStoreImpl(vars, 2);
		int[] functions = new int[0];
		this.model = new LogicalModelImpl(vars, manager, functions);
	}

	public static InvalidPosition getInstance() {
		if (emptyModel == null) {
			emptyModel = new InvalidPosition();
		}
		return emptyModel;
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

	public boolean isInvalidPosition(String n) {
		return n.equals(this.name);
	}

	public boolean isInvalidPosition(LogicalModel m) {
		return m.equals(this.model);
	}
}
