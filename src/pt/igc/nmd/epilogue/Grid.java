package pt.igc.nmd.epilogue;

import java.util.Hashtable;

import org.colomoto.logicalmodel.LogicalModel;
import org.colomoto.logicalmodel.NodeInfo;

public class Grid {

	private Hashtable<NodeInfo, Integer> cell;
	private Hashtable<Integer, Hashtable<NodeInfo, Byte>> grid;
	private MainPanel mainPanel;
	private LogicalModel model;

	public Grid(MainPanel mainPanel) {
		this.mainPanel = mainPanel;
		this.grid = new Hashtable<Integer, Hashtable<NodeInfo, Byte>>();

	}

	public void initializeGrid() {
		model = mainPanel.getEpithelium().getUnitaryModel();

		for (int i = 0; i < mainPanel.getTopology().getNumberInstances(); i++) {
			grid.put(i, new Hashtable<NodeInfo, Byte>());
			for (NodeInfo node : model.getNodeOrder()) {
				grid.get(i).put(node, (byte) 0);
			}
		}

	}

	public Hashtable<Integer, Hashtable<NodeInfo, Byte>> getGrid() {
		return this.grid;
	}

	public void setGrid(Integer instance, NodeInfo node, byte b) {
//		System.out.println("instance no setGrid " + instance + " " + grid);
//		System.out.println(grid.get(0));
		this.grid.get(instance).put(node, b);

	}

}
