//package org.epilogtool.core;
//
//import java.awt.Color;
//import java.util.ArrayList;
//import java.util.List;
//
//import org.colomoto.biolqm.LogicalModel;
//import org.colomoto.biolqm.LogicalModelImpl;
//import org.colomoto.biolqm.NodeInfo;
//import org.colomoto.mddlib.MDDManager;
//import org.colomoto.mddlib.internal.MDDStoreImpl;
//import org.epilogtool.OptionStore;
//
//public class EmptyModel {
//	private static final Color default_color = Color.black;
//	private Color color;
//	private String name;
//	private LogicalModel model;
//
//	private static EmptyModel emptyModel = null;
//
//	private EmptyModel() {
//		String c = (String) OptionStore.getOption("EM");
//		this.color = ((c == null) ? default_color: Color.decode(c));
//		this.name = "Empty cell";
//		List<NodeInfo> vars = new ArrayList<NodeInfo>();
//		MDDManager manager = new MDDStoreImpl(vars, 2);
//		int[] functions = new int[0];
//		this.model = new LogicalModelImpl(vars, manager, functions);
//	}
//
//	public static EmptyModel getInstance() {
//		if (emptyModel == null) {
//			emptyModel = new EmptyModel();
//		}
//		return emptyModel;
//	}
//
//	public String getName() {
//		return this.name;
//	}
//
//	public LogicalModel getModel() {
//		return this.model;
//	}
//	
//	public Color getColor(){
//		return this.color;
//	}
//	
//	public void setColor(Color c) {
//		this.color = c;
//	}
//
//	public boolean isEmptyModel(String n) {
//		return n.equals(this.name);
//	}
//
//	public boolean isEmptyModel(LogicalModel m) {
//		return m.equals(this.model);
//	}
//}
