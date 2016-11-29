package org.epilogtool.core;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.colomoto.logicalmodel.LogicalModel;
import org.colomoto.logicalmodel.NodeInfo;
import org.colomoto.logicalmodel.perturbation.AbstractPerturbation;
import org.epilogtool.common.Tuple2D;
import org.epilogtool.core.topology.RollOver;
import org.epilogtool.core.topology.Topology;
import org.epilogtool.services.TopologyService;

public class EpitheliumGrid {
	private EpitheliumCell[][] gridEpiCell;
	private Topology topology;
	private Set<LogicalModel> modelSet;
	private Map<LogicalModel, List<Tuple2D<Integer>>> modelPositions;
	
	private Map<String, Integer> component2Count ;

	private EpitheliumGrid(EpitheliumCell[][] gridEpiCell, Topology topology,
			Set<LogicalModel> modelSet,
			Map<LogicalModel, List<Tuple2D<Integer>>> modelPositions) {
		this.gridEpiCell = gridEpiCell;
		this.topology = topology;
		this.modelSet = modelSet;
		this.modelPositions = modelPositions;
		
		this.component2Count = new HashMap<String, Integer>();
	}

	public void updateEpitheliumGrid(int gridX, int gridY, String topologyID,
			RollOver rollover) throws InstantiationException,
			IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException,
			SecurityException, ClassNotFoundException {
		// Create new EpiCell[][]
		EpitheliumCell[][] newGrid = new EpitheliumCell[gridX][gridY];
		for (int y = 0; y < gridY; y++) {
			for (int x = 0; x < gridX; x++) {
				if (x >= this.getX() || y >= this.getY()) {
					newGrid[x][y] = new EpitheliumCell(EmptyModel.getInstance()
							.getModel());
				} else {
					newGrid[x][y] = this.gridEpiCell[x][y];
				}
			}
		}
		this.gridEpiCell = newGrid;
		// Create new Topology
		this.setTopology(topologyID, gridX, gridY, rollover);
		// Update model Set
		this.updateModelSet();
	}

	public EpitheliumGrid(int gridX, int gridY, String topologyID,
			RollOver rollover, LogicalModel m) throws InstantiationException,
			IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException,
			SecurityException, ClassNotFoundException {
		this.setTopology(topologyID, gridX, gridY, rollover);
		this.modelPositions = new HashMap<LogicalModel, List<Tuple2D<Integer>>>();
		this.gridEpiCell = new EpitheliumCell[gridX][gridY];
		for (int y = 0; y < gridY; y++) {
			for (int x = 0; x < gridX; x++) {
				this.gridEpiCell[x][y] = new EpitheliumCell(m);
				if (this.modelPositions.keySet().contains(m)) {
					this.modelPositions.get(m).add(new Tuple2D<Integer>(x, y));
				} else {
					List<Tuple2D<Integer>> tmpList = new ArrayList<Tuple2D<Integer>>();
					tmpList.add(new Tuple2D<Integer>(x, y));
					this.modelPositions.put(m, tmpList);
				}
			}
		}
		this.modelSet = new HashSet<LogicalModel>();
		this.modelSet.add(m);
	}

	private void setTopology(String topologyID, int gridX, int gridY,
			RollOver rollover) throws InstantiationException,
			IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException,
			SecurityException, ClassNotFoundException {
		this.topology = TopologyService.getManager().getNewTopology(topologyID,
				gridX, gridY, rollover);
	}

	public int getX() {
		return this.topology.getX();
	}

	public int getY() {
		return this.topology.getY();
	}

	public Topology getTopology() {
		return this.topology;
	}

	public LogicalModel getModel(int x, int y) {
		return this.gridEpiCell[x][y].getModel();
	}

	public boolean hasModel(LogicalModel m) {
		return this.modelSet.contains(m);
	}

	public void restrictCellWithPerturbation(int x, int y) {
		this.gridEpiCell[x][y].restrictValuesWithPerturbation();
	}

	public byte[] getCellState(int x, int y) {
		return this.gridEpiCell[x][y].getState();
	}
	
	public byte getCellValue(int x, int y, String nodeID) {
		return this.gridEpiCell[x][y].getValue(nodeID);
	}

	public AbstractPerturbation getPerturbation(int x, int y) {
		return this.gridEpiCell[x][y].getPerturbation();
	}

	public Map<LogicalModel, List<Tuple2D<Integer>>> getModelPositions() {
		return this.modelPositions;
	}

	public int getNodeIndex(int x, int y, String nodeID) {
		return this.gridEpiCell[x][y].getNodeIndex(nodeID);
	}

	public Set<LogicalModel> getModelSet() {
		return Collections.unmodifiableSet(this.modelSet);
	}

	public boolean hasEmptyModel(int x, int y) {
		return this.gridEpiCell[x][y].hasEmptyModel();
	}

	public void updateModelSet() {
		this.modelSet = new HashSet<LogicalModel>();
		this.modelPositions = new HashMap<LogicalModel, List<Tuple2D<Integer>>>();
		for (int y = 0; y < this.getY(); y++) {
			for (int x = 0; x < this.getX(); x++) {
				if (this.hasEmptyModel(x, y)) {
					continue;
				}
				LogicalModel m = this.gridEpiCell[x][y].getModel();
				this.modelSet.add(m);
				Tuple2D<Integer> tmpTuple = new Tuple2D<Integer>(x, y);
				if (this.modelPositions.containsKey(m)) {
					this.modelPositions.get(m).add(tmpTuple);
				} else {
					List<Tuple2D<Integer>> tmpList = new ArrayList<Tuple2D<Integer>>();
					tmpList.add(tmpTuple);
					this.modelPositions.put(m, tmpList);
				}
			}
		}
	}

	public void setRollOver(RollOver r) {
		this.topology.setRollOver(r);
	}

	/** Sets a given cell(x,y) with model m
	 * @param x
	 * @param y
	 * @param m
	 */
	public void setModel(int x, int y, LogicalModel m) {
		Tuple2D<Integer> tmpTuple = new Tuple2D<Integer>(x, y);
		if (this.gridEpiCell[x][y].getModel() != m) {
			if (!EmptyModel.getInstance().isEmptyModel(
					this.gridEpiCell[x][y].getModel())) {
				if (this.modelPositions.get(this.gridEpiCell[x][y].getModel())
						.contains(tmpTuple)) {
					this.modelPositions.get(this.gridEpiCell[x][y].getModel())
							.remove(tmpTuple);
				}
			}
		}
		gridEpiCell[x][y].setModel(m);
		if (!this.modelPositions.containsKey(m)) {
			List<Tuple2D<Integer>> tmpList = new ArrayList<Tuple2D<Integer>>();
			tmpList.add(tmpTuple);
			this.modelPositions.put(m, tmpList);
		} else {
			this.modelPositions.get(m).add(tmpTuple);
		}
	}

	public void setPerturbation(LogicalModel m, List<Tuple2D<Integer>> lTuples,
			AbstractPerturbation ap) {
		for (Tuple2D<Integer> tuple : lTuples) {
			if (this.gridEpiCell[tuple.getX()][tuple.getY()].getModel().equals(
					m)) {
				this.setPerturbation(tuple.getX(), tuple.getY(), ap);
			}
		}
	}

	public void setPerturbation(int x, int y, AbstractPerturbation ap) {
		gridEpiCell[x][y].setPerturbation(ap);
	}

	public void setCellState(int x, int y, byte[] state) {
		gridEpiCell[x][y].setState(state);
	}

	public void setCellComponentValue(int x, int y, String nodeID, byte value) {
		gridEpiCell[x][y].setValue(nodeID, value);
	}

	public EpitheliumCell cloneEpitheliumCellAt(int x, int y) {
		return this.gridEpiCell[x][y].clone();
	}

	protected void cloneEpitheliumCellTo(int x1, int y1, int x2, int y2) {
		EpitheliumCell epiCell = this.cloneEpitheliumCellAt(x1, y1);
		this.gridEpiCell[x2][y2] = epiCell;
	}

	protected int emptyModelNumber() {
		int gridSize = this.getX() * this.getY();
		int cellNumber = 0;
		for (LogicalModel m : this.modelPositions.keySet()) {
			cellNumber += this.modelPositions.get(m).size();
		}
		return gridSize - cellNumber;
	}

	public String hashGrid() {
		String hash = "";
		for (int y = 0; y < this.getY(); y++) {
			for (int x = 0; x < this.getX(); x++) {
				hash += this.gridEpiCell[x][y].hashState();
			}
		}
		return hash;
	}

	public String toString() {
		String s = "";
		for (int y = 0; y < this.getY(); y++) {
			s += (y + 1) + "|";
			for (int x = 0; x < this.getX(); x++) {
				byte[] currState = this.getCellState(x, y);
				for (int i = 0; i < currState.length; i++) {
					s += currState[i];
				}
				s += "|";
			}
			s += "\n";
		}
		return s;
	}

	public boolean equals(Object a) {
		if (a == null || !(a instanceof EpitheliumGrid))
			return false;
		if (a == this)
			return true;
		EpitheliumGrid o = (EpitheliumGrid) a;

		if (!this.topology.equals(o.topology))
			return false;
		for (int y = 0; y < this.getY(); y++) {
			for (int x = 0; x < this.getX(); x++) {
				if (!this.gridEpiCell[x][y].equals(o.gridEpiCell[x][y]))
					return false;
			}
		}
		return true;
	}

	public EpitheliumGrid clone() {
		EpitheliumCell[][] newGrid = new EpitheliumCell[this.getX()][this
				.getY()];
		for (int y = 0; y < this.getY(); y++) {
			for (int x = 0; x < this.getX(); x++) {
				newGrid[x][y] = this.gridEpiCell[x][y].clone();
			}
		}
		Topology newTop = this.topology.clone();
		Set<LogicalModel> newModelSet = new HashSet<LogicalModel>(this.modelSet);
		Map<LogicalModel, List<Tuple2D<Integer>>> newModelPositions = new HashMap<LogicalModel, List<Tuple2D<Integer>>>(
				this.modelPositions);
		return new EpitheliumGrid(newGrid, newTop, newModelSet,
				newModelPositions);
	}
	
	
	private void initializeComponent2Count(){
		
		Set<LogicalModel> lModels = this.getModelSet();
		this.component2Count = new HashMap<String, Integer>();
		//Initialize the component2Count
		for (LogicalModel model: lModels){
			for (NodeInfo node: model.getNodeOrder()){
				for (Byte val=1;val<=node.getMax();val++){
				
					String name = node.getNodeID()+"_"+val;
						this.component2Count.put(name,0);
					}
			}
		}
//		System.out.println(this.component2Count);
		for (int x = 0; x < this.getX(); x++) {
			for (int y = 0; y < this.getY(); y++) {
				LogicalModel model = this.getModel(x, y);
				for (NodeInfo node: model.getNodeOrder()){
					for (Byte val=1;val<=node.getMax();val++){
						String name = node.getNodeID()+"_"+val;
						if (this.getCellValue(x, y, node.getNodeID())>0){
							int count = this.component2Count.get(name)+1;
							this.component2Count.put(name,count);
						}
					}
				}
			}
			
			}
		
		
	}
	
	
	public String getPercentage(String nodeID) {
		// TODO Auto-generated method stub

		initializeComponent2Count();
		byte max = 0;
		
		
		for (LogicalModel model: this.getModelSet()){
			for (NodeInfo node: model.getNodeOrder()){
				if (node.getNodeID().equals(nodeID))
					max = node.getMax();
			}
		}
		
		
		
//		int nCells = 0;
//		for (LogicalModel model: lModels){
//		nCells = nCells + this.getModelPositions().get(model).size();
//		}
//		
//		System.out.println(this.component2Count);
		String output = "";
		int nCells = this.getX()*this.getY();
		for (Byte val=1;val<=max;val++){
			String name = nodeID+"_"+val;
			float count = this.component2Count.get(name);
			float percentage = (count/nCells)*100;
			output = output +"("+ val+" : "+ percentage +  "%)";
//			System.out.println(" " + count + " " +nodeID +" "+val);
			}
		
		return output;
		

				

		
		
		
//		Map<String, LogicalModel> model2Node = new HashMap<String, LogicalModel>();
//		Map<Byte, Integer> value2Count = new HashMap<Byte, Integer>();
//		Map<Map<String, LogicalModel>,Map<Byte, Integer>> node4count = new HashMap<Map<String, LogicalModel>,Map<Byte, Integer>>();
//		
//		
//		for (int x = 0; x < epitheliumGrid.getX(); x++) {
//			for (int y = 0; y < epitheliumGrid.getY(); y++) {
//				List<NodeInfo> lNode= epitheliumGrid.getModel(x, y).getNodeOrder();
//				for (NodeInfo node: lNode){
//					if (node.getNodeID().equals(nodeID)){
////						model2Node.put(epitheliumGrid.getModel(x, y), nodeID);
//						byte value = epitheliumGrid.getCellValue(x, y, nodeID);
//						int count = value2Count.get(value);
//						value2Count.put(value, count);
//						node4count.put(model2Node, value2Count);
//					}
//				}
//				
//			}
//			}
		
		
	}
	
	
}
