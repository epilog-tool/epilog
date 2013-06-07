package pt.igc.nmd.epilog;

import java.util.List;

import org.colomoto.logicalmodel.NodeInfo;

public class PerturbedGrid {

	private byte[][][] grid;
	private List<NodeInfo> listNodes = null;
	private boolean cellPerturbed[]=null;

	public PerturbedGrid(int instancesTotalNumber, List<NodeInfo> listNodes) {

		this.grid = new byte[instancesTotalNumber][][];
		this.listNodes = listNodes;
		cellPerturbed = new boolean[instancesTotalNumber];
		init();
	}

	public void init() {
		for (int i = 0; i < this.grid.length; i++) {
			setCellPerturbed(i,false);
			this.grid[i] = new byte[this.listNodes.size()][];
			for (NodeInfo node : this.listNodes) {
				this.grid[i][this.listNodes.indexOf(node)] = new byte[2];
				this.grid[i][this.listNodes.indexOf(node)][0] = 0;
				this.grid[i][this.listNodes.indexOf(node)][1] = 0;
			}
		}
	}

	public void setPerturbedGrid(int instance, NodeInfo perturbedNode, byte min, byte max){
		this.grid[instance][this.listNodes.indexOf(perturbedNode)][0] = min;
		this.grid[instance][this.listNodes.indexOf(perturbedNode)][1] = max;
		setCellPerturbed(instance,true);
	}
	
	public void setCellPerturbed(int instance, boolean b){
		cellPerturbed[instance]=b;
	}
	
	public boolean isPerturbed(int instance){
		return cellPerturbed[instance];
	}
	
	public byte getMin(int instance, NodeInfo node){
		return this.grid[instance][this.listNodes.indexOf(node)][0];
	}
	
	public byte getMax(int instance, NodeInfo node){
		return this.grid[instance][this.listNodes.indexOf(node)][1];
	}
}
