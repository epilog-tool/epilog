package org.epilogtool.core;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.colomoto.logicalmodel.LogicalModel;
import org.colomoto.logicalmodel.LogicalModelImpl;
import org.colomoto.logicalmodel.NodeInfo;
import org.colomoto.mddlib.MDDManager;
import org.colomoto.mddlib.internal.MDDStoreImpl;

public class EmptyModel {
	private Color color;
	private String name;
	private LogicalModel model;

	private static EmptyModel emptyModel = null;

	private EmptyModel() {
		this.color = Color.GRAY;
		this.name = "Empty model";
		List<NodeInfo> vars = new ArrayList<NodeInfo>();
		MDDManager manager = new MDDStoreImpl(vars, 2);
		int[] functions = new int[0];
		this.model = new LogicalModelImpl(vars, manager, functions);
	}

	public static EmptyModel getInstance() {
		if (emptyModel == null) {
			emptyModel = new EmptyModel();
		}
		return emptyModel;
	}

	public String getName() {
		return this.name;
	}

	public Color getColor() {
		return this.color;
	}

	public LogicalModel getModel() {
		return this.model;
	}

	public boolean isEmptyModel(String n) {
		return n.equals(this.name);
	}

	public boolean isEmptyModel(LogicalModel m) {
		return m.equals(this.model);
	}
}
