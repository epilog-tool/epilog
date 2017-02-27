package org.cellularasynchrony;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import org.colomoto.logicalmodel.NodeInfo;


public class StochasticSimulation {

	private MainFrame mainFrame = null;

	public  List<Integer> cumulativeGrid = null;
	public  List<Integer> ssIterationNumber;
	public  List<Grid> ssGrid;
	public  String compToShow;

	private int simulationsNumber;
	private int maxIteration;
	private String onlyUpdatable;
	private int alpha;
	private String updateScheme;
	private String rollOver;


	public Hashtable<NodeInfo, Integer> node2CumulativeValuePerGrid;


	/**
	 * Initializes the stochastic simulation setup.
	 * 
	 * @param mainFrame
	 */
	public StochasticSimulation(MainFrame mainFrame) {
		this.mainFrame = mainFrame;

		this.cumulativeGrid = cumulativeGrid;
		this.ssIterationNumber = ssIterationNumber;
		this.ssGrid = ssGrid;
		this.compToShow = compToShow;
		this.simulationsNumber = simulationsNumber;
		this.maxIteration = maxIteration;
		this.rollOver = rollOver;

		this.onlyUpdatable = onlyUpdatable;
		this.alpha = alpha;
		this.updateScheme = updateScheme;

		this.ssIterationNumber = new ArrayList<Integer>();
		this.ssGrid = new ArrayList<Grid>();

		this.node2CumulativeValuePerGrid= new Hashtable<NodeInfo, Integer>();

	}

	public void setMaxIteration(int maxIter){
		this.maxIteration = maxIter;
	}

	public int getMaxIteration(){
		return this.maxIteration;
	}

	public void setSimulationsNumber(int simNum){
		this.simulationsNumber = simNum;
	}

	public int getSimulationsNumber(){
		return this.simulationsNumber;
	}

	public void addSSFinalIteration(int cellIteration) {
		// TODO Auto-generated method stub
		this.ssIterationNumber.add(cellIteration);
	}


	public List<Integer> getSSIterations() {

		return ssIterationNumber;
	}

	public void addSSGrid(Grid nextGlobalState) {
		// TODO Auto-generated method stub
		this.ssGrid.add(nextGlobalState);
	}

	public List<Integer> getCumulativeGrid(){

		initializeCumulativeGrid(mainFrame.topology.getNumberInstances());
		for (int i = 0; i<this.ssGrid.size();i++){
			for (int j = 0; j<ssGrid.get(i).getNumberInstances();j++){
				int value = ssGrid.get(i).getValue(j, mainFrame.getEpithelium().getString2Node(compToShow));
				int value_old = this.cumulativeGrid.get(j);
				if (value >0){
					this.cumulativeGrid.set(j,value + value_old);
				}}
		}
		return this.cumulativeGrid;
	}


	private void initializeCumulativeGrid(int numberInstances) {
		this.cumulativeGrid = new ArrayList<Integer>();

		for (int i = 0; i<numberInstances;i++){
			cumulativeGrid.add(i, 0);
		}
	}


	public void setCompToShow(String optionString) {
		this.compToShow = optionString;

	}


	public void paintCumulativeGrid() {
		// TODO Auto-generated method stub
		//		mainFrame.hexagonsPanel.paintCumulativeComponent( mainFrame.hexagonsPanel.getGraphics());
		mainFrame.hexagonsPanel.repaint();


	}


	public Color getCoordinateCurrentColor(int instance) {
		int r = 255;
		int g = 255;
		int b = 255;

		float value = cumulativeGrid.get(instance);
		//		float maxValue = (float) Collections.max(cumulativeGrid);
		float maxValue = (float) ssIterationNumber.size();

		if (value>0){
			float frac = value/maxValue;
			r = 255-(int) Math.floor(r*frac);
			g=  255-(int) Math.floor(g*frac);
			b=  255-(int) Math.floor(b*frac);
		}
		Color newColor = new Color(r,g,b);
		return newColor;
	}

	public float getTotalPercentagePerNode (NodeInfo node, Grid grid){
		int index = 0;
		for (int i = 0; i<grid.getNumberInstances();i++)
			index = index + grid.getValue(i, node);	
		node2CumulativeValuePerGrid.put(node, index);
		return ((float) index)/(float) (grid.getNumberInstances());
	}

	public float getTotalPercentagePerLinePerNode (NodeInfo node, Grid grid,int line){
		int index = 0;
		for (int column = 0; column<mainFrame.topology.getWidth();column++){
			int instance = mainFrame.topology.coords2Instance(line, column);
			index = index + grid.getValue(instance, node);	
		}
		return ((float) index)/(float) (mainFrame.topology.getWidth());
	}

	public float getTotalPercentagePerColumnPerNode (NodeInfo node, Grid grid,int column){
		int index = 0;
		for (int line = 0; line<mainFrame.topology.getHeight();line++){
			int instance = mainFrame.topology.coords2Instance(line, column);
			index = index + grid.getValue(instance, node);	
		}
		return ((float) index)/(float) (mainFrame.topology.getWidth());
	}

	public String getName(){
		String name = "";

		if (this.updateScheme=="Random Independent")
			name = name + "RI_";
		else if (this.updateScheme=="Random Order")
			name = name + "RO_";
//		else if (this.updateScheme=="Cyclic Order")
//			name = name + "CO_";
//		else if (this.updateScheme=="Exponential Clocked")
//			name = name + "EO_";
//		else if (this.updateScheme=="Synchronous")
//			name = name + "S_";

		if (this.onlyUpdatable=="All cells")
			name = name + "AC_";
		else if (this.onlyUpdatable=="Updatable cells")
			name = name + "UC_";

		name = name + this.alpha+"_";
		
		name = name + mainFrame.topology.beta+"_";

		if (this.rollOver =="No Roll-Over")
			name = name + "NR_";
		else if (this.rollOver =="Horizontal Roll-Over")
			name = name + "HR_";
		else if (this.rollOver =="Vertical Roll-Over")
			name = name + "VR_";
		else if (this.rollOver =="Double Roll-Over")
			name = name + "DR_";

		name = name + mainFrame.topology.getWidth() + "_";
		name = name + mainFrame.topology.getHeight() + "_";

		name = name + simulationsNumber;
		name = name + ".txt";
		return name;
	}

	public void printSolutions(){

		PrintWriter out = null;

		if (!fileExists(getName())){

			try {
				out = new PrintWriter(getName());
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			createStochasticDataFile(out);
			System.out.println("file created");
		}

//		String name = "Raw_"+getName();
//		if (!fileExists(name)){
//
//
//		PrintWriter out_Raw = null;
//
//		try {
//			out_Raw = new PrintWriter("Raw_"+getName());
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		createRawDataFile(out_Raw);
//		System.out.println("Raw file created");
//		}
	}

	public boolean fileExists(String string) {
		File f = new File(string);
		if (f.exists()) return true;
		return false;
	}

	private void createRawDataFile(PrintWriter out) {

		out.write("Model: " + mainFrame.getEpithelium().getSBMLFilename() + "\n");
		out.write("Dimensions: x= " + mainFrame.topology.getWidth() + " y= " + mainFrame.topology.getHeight() + "\n");
		out.write("Number Sim.: "+ simulationsNumber + "\n");
		out.write("Max. Iteration: " + maxIteration + " ( 10^3)\n");
		out.write("Roll-Over: " + mainFrame.stochasticSimulation.getRollOver()+"\n");
		out.write("Update Scheme: " + mainFrame.stochasticSimulation.getUpdateScheme()+"\n");
		out.write("Alpha: " + mainFrame.stochasticSimulation.getAlpha()+"\n");
		out.write("Beta: " + mainFrame.topology.beta+"\n");
		out .write("Updating: " + mainFrame.stochasticSimulation.getOnlyUpdate()+"\n");
		out.write( "\n");

		int indexGrid = 0;

		for (int i = 0; i<ssIterationNumber.size(); i++)
			if (i>=0){
				Grid grid = ssGrid.get(i);
				for (NodeInfo node: grid.getListNodes()){
					if (!node.isInput()){

						for (int value = 1; value < node.getMax() + 1; value++) {

							out.write("Grid_" + indexGrid+"_"+node+":"+ value + " ( ");

							// int next = 0;
							int current = 0;
							boolean start = true;
							boolean ongoing = false;

							for (int instance = 0; instance < grid
									.getNumberInstances(); instance++) {
								if (grid.getValue(instance,
										node) == value) {

									if (start) {
										out.write("" + instance);
										start = false;
										current = instance;
									} else if ((instance != current + 1)
											& ongoing == false) {
										out.write(",");
										out.write("" + instance);
										current = instance;
									} else if ((instance == current + 1)) {
										ongoing = true;
										current = instance;
									}
								} else if (ongoing == true) {
									ongoing = false;
									out.write("-");
									out.write("" + (current));
								}
								if (instance == (grid
										.getNumberInstances() - 1) & ongoing == true) {
									out.write("-");
									out.write("" + (instance));
								}

							}
							out.write(" )\n");
						}
					}}
				indexGrid = indexGrid + 1;
				out.write("\n");
			}
		out.close();
	}

	public void createStochasticDataFile(PrintWriter out){

		out.write("Model: " + mainFrame.getEpithelium().getSBMLFilename() + "\n");
		out.write("Dimensions: x= " + mainFrame.topology.getWidth() + " y= " + mainFrame.topology.getHeight() + "\n");
		out.write("Number Sim.: "+ simulationsNumber + "\n");
		out.write("Max. Iteration: " + maxIteration + " ( 10^3)\n");
		out.write("Roll-Over: " + mainFrame.stochasticSimulation.getRollOver()+"\n");
		out.write("Update Scheme: " + mainFrame.stochasticSimulation.getUpdateScheme()+"\n");
		out.write("Alpha: " + mainFrame.stochasticSimulation.getAlpha()+"\n");
		out.write("Beta: " + mainFrame.topology.beta+"\n");
		out.write("Updating: " + mainFrame.stochasticSimulation.getOnlyUpdate()+"\n");
		out.write( "\n");
		out.write("The average of cell iterations until SS are: " + getSSMean()+"\n");
		for (NodeInfo node: mainFrame.getEpithelium().getUnitaryModel().getNodeOrder()){
			if (!node.isInput())
				out.write("The average percentage of " +node +" is: " + getMeanComponent(node)+"\n");
		}
		out.write("Number of non-Reachable stable Grids: " + getNonStableGrids()+"\n");
		out.write( "\n");

//		int index = 0;
//		for (int i = 0; i<ssGrid.size();i++){
//			Grid grid = ssGrid.get(i);
//			out.write("Grid_"+index+" SS:"+ssIterationNumber.get(index)+ " ");
//			for (NodeInfo node: mainFrame.getEpithelium().getUnitaryModel().getNodeOrder())
//				if (!node.isInput())
//					out.write(node +":" + getTotalPercentagePerNode(node,grid)+ " ");
//
//			out.write("\n");
//		
//			for (NodeInfo node: mainFrame.getEpithelium().getUnitaryModel().getNodeOrder())
//				if (!node.isInput()){
//					out.write("Grid_"+index + " percentage of " + node +" in lines " + "[");
//					for (int line =0; line<mainFrame.topology.getHeight();line++){
//						out.write(""+getTotalPercentagePerLinePerNode(node,grid,line));
//						if (line<mainFrame.topology.getHeight()-1){
//							out.write(",");
//						}
//					}out.write("]\n");
//
//					out.write("Grid_"+index + " percentage of " + node +" in columns " + "[");
//					for (int column =0; column<mainFrame.topology.getWidth();column++){
//						out.write(""+getTotalPercentagePerColumnPerNode(node,grid,column));
//						if (column<mainFrame.topology.getWidth()-1){
//							out.write(",");
//						}
//					}out.write("]\n");
//
//				}
//			out.write("\n");
//			index = index +1;
//		}
		out.close();
	}

	private int getNonStableGrids() {

		int notSSIndex = 0;

		for (int i :ssIterationNumber){
			if (i<0)
				notSSIndex = notSSIndex+1;}
		return notSSIndex;
	}

	private float getMeanComponent(NodeInfo node) {

		float total = 0;
		int notSSIndex = 0;
		for (int i = 0; i<ssGrid.size();i++){
			int sum = 0;
			Grid grid = ssGrid.get(i);
			if (ssIterationNumber.get(i)<0){
				notSSIndex = notSSIndex+1;}
			else{
				for (int instance = 0; instance<grid.getNumberInstances(); instance++){
					sum = sum + grid.getValue(instance, node);
				}
				total = total + ((float) sum)/((float) grid.getNumberInstances());
			}

		}


		return ((float) total)/((float) ssGrid.size()-notSSIndex);
	}

	private float getSSMean() {

		int notSSIndex = 0;
		int sum = 0;
		for (int i = 0; i<ssIterationNumber.size(); i++){
			if (ssIterationNumber.get(i)<0){
				notSSIndex = notSSIndex + 1;
				sum = sum +200000;
			}
			else{
				sum = sum +ssIterationNumber.get(i);
			}}
		if (sum==0)
			return 0;
		else
//			return ((float) sum)/((float) ssIterationNumber.size()-notSSIndex);
			return ((float) sum)/((float) ssIterationNumber.size());
	}

	public int getAlpha() {
		return this.alpha;
	}

	public void setAlpha(int alpha) {
		this.alpha = alpha;
	}

	public String getOnlyUpdate() {
		return this.onlyUpdatable;
	}

	public void setOnlyUpdate(String onlyU) {
		this.onlyUpdatable = onlyU;
	}

	public String getRollOver() {
		return this.rollOver;
	}

	public void setRollOver(String rollOver) {
		this.rollOver = rollOver;
	}

	public String getUpdateScheme() {
		return this.updateScheme;
	}

	public void setUpdateScheme(String updScheme) {
		this.updateScheme = updScheme;
	}

	public void reset() {

		this.rollOver = null;
		this.updateScheme = null;
//		this.onlyUpdatable = (String) mainFrame.stochasticSimulationPanel.onlyUpdatable.getSelectedItem();
		this.simulationsNumber = 0;
		this.cumulativeGrid =null;
		this.ssIterationNumber = null;
		this.alpha = 0;
		this.node2CumulativeValuePerGrid = null;
		this.ssGrid = null;

		this.ssIterationNumber = new ArrayList<Integer>();
		this.ssGrid = new ArrayList<Grid>();
		this.node2CumulativeValuePerGrid= new Hashtable<NodeInfo, Integer>();

		

	}


}
