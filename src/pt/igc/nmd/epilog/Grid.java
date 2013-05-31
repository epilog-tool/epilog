package pt.igc.nmd.epilog;

import java.util.List;

import org.colomoto.logicalmodel.NodeInfo;

public class Grid {

	private byte[][] grid;
	private List<NodeInfo> listNodes = null;

	public Grid(int instancesTotalNumber, List<NodeInfo> listNodes) {

		this.grid = new byte[instancesTotalNumber][];
		this.listNodes = listNodes;
		init();
	}

	private void init() {
		for (int i = 0; i < this.grid.length; i++) {
			this.grid[i] = new byte[this.listNodes.size()];
			for (NodeInfo node : this.listNodes)
				this.grid[i][this.listNodes.indexOf(node)] = 0;
		}
	}

	public void setGrid(int instance, NodeInfo node, byte value) {
		this.grid[instance][listNodes.indexOf(node)] = value;
	}

	
	// TODO: re-think this method, particularly exception handling
	public byte getValue(int instance, NodeInfo node) {
		if (listNodes.contains(node))
			return this.grid[instance][listNodes.indexOf(node)];
		else
			return -1;
	}

	public int getNumberInstances(){
		return this.grid.length;
	}
	
	public List<NodeInfo> getListNodes(){
		return this.listNodes;
	}
	
	public byte[][] asByteMatrix(){
		return grid;
	}
	
	public byte[] asByteArray(int instance){
		return grid[instance];
	}
	
	public boolean equals(Grid other){
		if (this.getListNodes().equals(other.getListNodes()) && this.getNumberInstances() == other.getNumberInstances()){
			for (int instance = 0; instance < this.getNumberInstances(); instance++)
				for (NodeInfo node : this.getListNodes())
					if (this.getValue(instance, node) != other.getValue(instance, node))
						return false;
		} else
			return false;
		
		return true;
	}
	
}
