package org.cellularasynchrony;

import java.awt.Color;
import java.awt.Container;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;

import org.colomoto.logicalmodel.NodeInfo;


public class Simulation {

	public Boolean stochastic = false;
	
	private int iterationNumber = 0;
	public int cellIteration = 0;
	public int maxCellIteration;
	private int stopCriterium = 30;
	
	private GlobalModel globalModel = null;

	public Grid currentGlobalState = null;
	private Grid nextGlobalState = null;
	private boolean runButtonActivated = false;

	private MainFrame mainFrame = null;
	private Utils utils = null;

	private boolean isRunning = false;
	private boolean stableStateFound = false;
	private boolean runcontrol = true;

	private Hashtable<Integer, Grid> statesCycleDetection;

	public String updateMode;
	public String onlyUpdatable;
	public int alpha;


	public  List<Integer> schuffledInstances;
	public  List<Integer> runningSchuffledInstances;
	public List<Double> exponentialInstances;
	private ArrayList<Double> exponentialInstances_aux;

	/**
	 * Initializes the simulation setup.
	 * 
	 * @param mainFrame
	 */
	public Simulation(MainFrame mainFrame) {
		this.mainFrame = mainFrame;
		this.utils = utils;
		this.schuffledInstances = schuffledInstances;
		this.runningSchuffledInstances = runningSchuffledInstances;
		this.exponentialInstances = null;
		this.alpha = 0;

		statesCycleDetection = new Hashtable<Integer, Grid>();
	}

	private boolean checkCycle() {

		for (Grid state : statesCycleDetection.values())
			if (currentGlobalState.equals(state)) {
				mainFrame.setUserMessage(MsgStatus.STATUS, "Cycle Detected");
				return true;
			}
		statesCycleDetection.put(iterationNumber, currentGlobalState);
		return false;
	}

	/**
	 * Simulates for n iterations or until a stable state is found.
	 */
	public void run() {
		runcontrol = false;

		while (!stableStateFound
				&& (iterationNumber % this.stopCriterium!=0) || runcontrol == false) {
			runcontrol = true;
			runButtonActivated = true;
			step();
			runButtonActivated = false;
		}
		runcontrol = false;
	}
	
	public void runStochastic() {
		runcontrol = false;
		this.maxCellIteration = 200000;
//		System.out.println(this.maxCellIteration);
		while (!stableStateFound
				&& (cellIteration < this.maxCellIteration) || runcontrol == false) {
			runcontrol = true;
			runButtonActivated = true;
			step();
			runButtonActivated = false;
//			System.out.println(this.cellIteration + " " +this.maxCellIteration);
			if (this.cellIteration >= this.maxCellIteration)
				mainFrame.stochasticSimulation.addSSFinalIteration(-1);
			
		}
		
		runcontrol = false;
	}

	/**
	 * Sets a boolean value of true if the simulation is running, false
	 * otherwise.
	 * 
	 * @param b
	 *            boolean value
	 * @see step()
	 */
	public void setRunning(boolean b) {
		this.isRunning = b;
	}

	/**
	 * Reset the simulation definitions.
	 */
	public void reset() {
		setRunning(false);
		currentGlobalState = null;
		globalModel = null;
		nextGlobalState = null;
		resetIterationNumber();
		stableStateFound = false;
		schuffledInstances = null;
		runningSchuffledInstances= null;
		exponentialInstances= null;
		//		cellIteration
		exponentialInstances_aux= null;
	}

	/**
	 * Returns true if the simulation is running, false otherwise.
	 * 
	 * @return
	 */
	public boolean isRunning() {
		return this.isRunning;
	}

	/**
	 * Iterates one step at a time.
	 * 
	 * @see run()
	 */
	public void step() {

		this.iterationNumber++;
		setRunning(true);

		this.mainFrame.simulationSetupPanel.restartButton.setEnabled(true);
		this.mainFrame.componentsPanel.saveAsInitialState.setEnabled(true);
		this.mainFrame.componentsPanel.setICName.setEnabled(true);

		this.mainFrame.simulationSetupPanel.simulationPanelsoff();


		if (globalModel == null) {
			if (!mainFrame.isStochastic()){
				this.updateMode = (String) mainFrame.simulationSetupPanel.updateMode.getSelectedItem();
				this.onlyUpdatable = (String) mainFrame.simulationSetupPanel.onlyUpdatable.getSelectedItem();
				this.alpha = (int) mainFrame.simulationSetupPanel.alpha.getSelectedItem();
				mainFrame.topology.setRollOver((String) mainFrame.simulationSetupPanel.rollOver.getSelectedItem()); 
			}

			globalModel = new GlobalModel(this.mainFrame);
		}

		if (iterationNumber > 30){
			//			System.out.println("Estou a checkar");
			checkCycle();
		}

		if (currentGlobalState == null) {
			currentGlobalState = new Grid(
					this.mainFrame.topology.getNumberInstances(),
					this.mainFrame.epithelium.getUnitaryModel().getNodeOrder());
			for (int instance = 0; instance < currentGlobalState
					.getNumberInstances(); instance++)
				for (NodeInfo node : currentGlobalState.getListNodes())
					currentGlobalState.setGrid(instance, node,
							this.mainFrame.epithelium.getGridValue(instance,
									node));
		}


		List<Integer> updatableCellsList = new ArrayList<Integer>();
		if ((updateMode == "Synchronous" || updateMode == null) ){
			int numberCells = mainFrame.simulation.currentGlobalState.getNumberInstances();

			if (this.onlyUpdatable =="Updatable cells"){
				updatableCellsList = utils.shuffleAndSelect(getCellsAvailableToUpdate(), numberCells);
				
			}
			else{
				updatableCellsList = utils.shuffleAndSelect(mainFrame.simulation.currentGlobalState.getNumberInstances(), numberCells);
				
			}
			nextGlobalState = globalModel.getNextState(currentGlobalState,updatableCellsList);
			this.cellIteration = this.cellIteration + numberCells;
		}


		if (updateMode == "Random Independent" ){

			int numberCells;
			if (this.onlyUpdatable =="Updatable cells"){

				List<Integer> updatableCells = getCellsAvailableToUpdate();
				numberCells = (int) Math.ceil(alpha * updatableCells.size()/100);
				if (numberCells ==0)
					numberCells =1;
				updatableCellsList = utils.shuffleAndSelect(updatableCells, numberCells);
			}
			else{
				numberCells = (int) Math.ceil(alpha * mainFrame.simulation.currentGlobalState.getNumberInstances()/100);
				if (numberCells ==0)
					numberCells =1;

				updatableCellsList = utils.shuffleAndSelect(mainFrame.simulation.currentGlobalState.getNumberInstances(), numberCells);
			}
			nextGlobalState = globalModel.getNextState(currentGlobalState, updatableCellsList);
			this.cellIteration = this.cellIteration + numberCells;
		}


		if (updateMode == "Cyclic Order" ){

			int numberCells = 0;
			int maxNumberCells = mainFrame.simulation.currentGlobalState.getNumberInstances();
			if (this.schuffledInstances == null){
				this.schuffledInstances = utils.shuffleAndSelect(currentGlobalState
						.getNumberInstances(), maxNumberCells);
				this.runningSchuffledInstances = new ArrayList<Integer>();
				for (int n =0; n<this.schuffledInstances.size();n++)
					this.runningSchuffledInstances.add(n, this.schuffledInstances.get(n));
			}
			if (this.onlyUpdatable =="Updatable cells"){
				List<Integer> updatableCells = getCellsAvailableToUpdate();
				numberCells = (int) Math.ceil(alpha * updatableCells.size()/100);
				if (numberCells ==0)
					numberCells =1;
				for (int n = 0; n<numberCells; n++)
					if (updatableCells.contains(this.runningSchuffledInstances.get(0))){
						updatableCellsList.add(this.runningSchuffledInstances.get(0));
						this.runningSchuffledInstances.remove(0);
						if (this.runningSchuffledInstances.size()==0)
							for (int k =0; k<this.schuffledInstances.size();k++)
								this.runningSchuffledInstances.add(k, this.schuffledInstances.get(k));	
					}	
					else{
						this.runningSchuffledInstances.remove(0);
						if (this.runningSchuffledInstances.size()==0)
							for (int k =0; k<this.schuffledInstances.size();k++)
								this.runningSchuffledInstances.add(k, this.schuffledInstances.get(k));
						n = n-1;
					}}
			else{
				numberCells = (int) Math.ceil(alpha * mainFrame.simulation.currentGlobalState.getNumberInstances()/100);
				if (numberCells ==0)
					numberCells =1;

				updatableCellsList = new ArrayList<Integer>();
				for (int n = 0; n<numberCells; n++){
					updatableCellsList.add(this.runningSchuffledInstances.get(0));
					this.runningSchuffledInstances.remove(0);
					if (this.runningSchuffledInstances.size()==0){
						for (int k =0; k<this.schuffledInstances.size();k++)
							this.runningSchuffledInstances.add(k, this.schuffledInstances.get(k));
					}}}

			nextGlobalState = globalModel.getNextState(currentGlobalState,updatableCellsList);
			this.cellIteration = this.cellIteration + numberCells;
		}

		if (updateMode == "Random Order" ){

			int numberCells = 0;
			int maxNumberCells = mainFrame.simulation.currentGlobalState.getNumberInstances();
			if (this.schuffledInstances == null){
				this.schuffledInstances = utils.shuffleAndSelect(currentGlobalState
						.getNumberInstances(), maxNumberCells);
				this.runningSchuffledInstances = new ArrayList<Integer>();
				for (int n =0; n<this.schuffledInstances.size();n++)
					this.runningSchuffledInstances.add(n, this.schuffledInstances.get(n));
			}

			if (this.onlyUpdatable =="Updatable cells"){
				List<Integer> updatableCells = getCellsAvailableToUpdate();
				numberCells = (int) Math.ceil(alpha * updatableCells.size()/100);
				if (numberCells ==0)
					numberCells =1;
				for (int n = 0; n<numberCells; n++)
					if (updatableCells.contains(this.runningSchuffledInstances.get(0))){
						updatableCellsList.add(this.runningSchuffledInstances.get(0));
						this.runningSchuffledInstances.remove(0);
						if (this.runningSchuffledInstances.size()==0){
							this.schuffledInstances = utils.shuffleAndSelect(currentGlobalState
									.getNumberInstances(), maxNumberCells);
							for (int k =0; k<this.schuffledInstances.size();k++)
								this.runningSchuffledInstances.add(k, this.schuffledInstances.get(k));
						}

					}	
					else{
						this.runningSchuffledInstances.remove(0);
						if (this.runningSchuffledInstances.size()==0){
							this.schuffledInstances = utils.shuffleAndSelect(currentGlobalState
									.getNumberInstances(), maxNumberCells);
							for (int k =0; k<this.schuffledInstances.size();k++)
								this.runningSchuffledInstances.add(k, this.schuffledInstances.get(k));
						}

						n = n-1;


					}}
			else{
				numberCells = (int) Math.ceil(this.alpha * mainFrame.simulation.currentGlobalState.getNumberInstances()/100);
				if (numberCells ==0)
					numberCells = 1;

				updatableCellsList = new ArrayList<Integer>();
				for (int n = 0; n<numberCells; n++){
					updatableCellsList.add(this.runningSchuffledInstances.get(0));
					this.runningSchuffledInstances.remove(0);

					if (this.runningSchuffledInstances.size()==0){
						this.schuffledInstances = utils.shuffleAndSelect(currentGlobalState
								.getNumberInstances(), maxNumberCells);
						for (int k =0; k<this.schuffledInstances.size();k++)
							this.runningSchuffledInstances.add(k, this.schuffledInstances.get(k));
					}
				}}

			nextGlobalState = globalModel.getNextState(currentGlobalState,updatableCellsList);
			this.cellIteration = this.cellIteration + numberCells;

		}


		if (updateMode == "Exponential Clocked" ){
			int lambda = 1;
			Random randomno = new Random();

			int numberCells = 0;
			int maxNumberCells = mainFrame.simulation.currentGlobalState.getNumberInstances();
			if (this.exponentialInstances == null){
				this.exponentialInstances = new ArrayList<Double>();
				for (int n =0; n<maxNumberCells;n++)
				{
					double aux = Math.log(1-randomno.nextDouble())/(-lambda);
					this.exponentialInstances.add(n, aux);
				}
			}
				
				List<Integer> updatableCells = getCellsAvailableToUpdate();
	
				numberCells = (int) Math.ceil(alpha * updatableCells.size()/100);
				if (numberCells ==0)
					numberCells =1;
				
				updatableCellsList = utils.findMinIdx(this.exponentialInstances, numberCells,updatableCells) ;
				
				
				this.exponentialInstances_aux = new ArrayList<Double>();
				
				for (int n = 0; n<maxNumberCells; n++){
					double value = this.exponentialInstances.get(n);
					if (updatableCellsList.contains(n)){
						double aux = Math.log(1-randomno.nextDouble())/(-lambda);
						value = this.exponentialInstances.get(n)+aux;
					}
					this.exponentialInstances_aux.add(n, value);

				}

				this.exponentialInstances = new ArrayList<Double>();
				for (int n = 0; n<maxNumberCells; n++){
					double value = this.exponentialInstances_aux.get(n);
					this.exponentialInstances.add(n, value);


					}
				
		

			
			nextGlobalState = globalModel.getNextState(currentGlobalState,updatableCellsList);
			this.cellIteration = this.cellIteration + numberCells;
		}


		// saveLastPic();
		if (!runButtonActivated)
			this.mainFrame.fillHexagons();

		// if (nextGlobalState.equals(currentGlobalState)) {
		int numberCells = mainFrame.simulation.currentGlobalState.getNumberInstances();
		updatableCellsList = utils.shuffleAndSelect(mainFrame.simulation.currentGlobalState.getNumberInstances(), numberCells);


		Grid syncNextState = globalModel.getNextState(nextGlobalState, updatableCellsList);

		if (stateComparative(nextGlobalState, syncNextState)) {
			stableStateFound = true;
			
			if (mainFrame.isStochastic()){
				System.out.println("Stable State Found: " + cellIteration);
				mainFrame.stochasticSimulation.addSSFinalIteration(cellIteration);
				mainFrame.stochasticSimulation.addSSGrid(nextGlobalState);
			}
				resetIterationNumber();
		} else
			this.mainFrame.setBorderHexagonsPanel(iterationNumber, cellIteration);

		currentGlobalState = nextGlobalState;

	}


	public List<Integer> getCellsAvailableToUpdate() {
		// TODO Auto-generated method stub

		List cellsAvailableToUpdate = new ArrayList<Integer>();

		for (int i = 0; i< mainFrame.simulation.currentGlobalState.getNumberInstances(); i++)
		{
			if (globalModel.isCellAvailableToUpdate(mainFrame.simulation.currentGlobalState,  i))
				cellsAvailableToUpdate.add(i);
		}
		return cellsAvailableToUpdate;
	}

	/**
	 * Global states are compared, without comparing the integration inputs,
	 * since they provide no information regarding the updating iteration.
	 * 
	 * @param currentState
	 * @param nextState
	 * @return true if states are equal, false otherwise
	 */
	public boolean stateComparative(Grid currentState, Grid nextState) {
		for (int instance = 0; instance < nextState.getNumberInstances(); instance++) {
			for (NodeInfo node : nextState.getListNodes()) {
				if (!mainFrame.epithelium.isIntegrationComponent(node))

					if (currentState.getValue(instance, node) != nextState
					.getValue(instance, node)) {

						return false;
					}
			}
		}
		return true;
	}

	/**
	 * Get the current global state of the world for a node.
	 * 
	 * @param instance
	 *            instance in evaluation
	 * @param node
	 *            node in evaluation
	 * @return current value of the node
	 */
	public int getCurrentGlobalStateValue(int instance, NodeInfo node) {
		int value = 0;
		if (currentGlobalState != null)
			value = currentGlobalState.getValue(instance, node);
		else
			value = this.mainFrame.epithelium.getGridValue(instance, node);

		return value;
	}

	/**
	 * Saves an image with the epithelium at iteration.
	 */
	public void saveLastPic() {
		Container c = this.mainFrame.hexagonsPanel;
		BufferedImage im = new BufferedImage(c.getWidth(), c.getHeight(),
				BufferedImage.TYPE_INT_ARGB);
		c.paint(im.getGraphics());
		try {
			ImageIO.write(im, "PNG", new File("shot" + this.iterationNumber
					+ ".png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Returns true if a stable state is found, false otherwise
	 * 
	 * @return
	 */
	public boolean hasStableStateFound() {
		return this.stableStateFound;
	}


	/**
	 * Initializes simulation, creating or not the composed model.
	 */
	public void initializeSimulation() {

		this.mainFrame.epithelium = this.mainFrame.getEpithelium();
		resetIterationNumber();


		setRunning(true);

		this.mainFrame.fillHexagons();
		this.mainFrame.hexagonsPanel
		.paintComponent(this.mainFrame.hexagonsPanel.getGraphics());
	}


//	/**
//	 * Returns the number of stochastic simulations
//	 * 
//	 * @return
//	 */
//	public int getSimulationsNumber() {
//		return this.simulationsNumber;
//	}

//	/**
//	 * Sets the number of stochastic simulations
//	 */
//	public void setSimulationsNumber(String simulationsNumber) {
//		this.simulationsNumber = Integer.parseInt(simulationsNumber);
//	}


//	/**
//	 * Returns the Maximum iteration number.
//	 * 
//	 * @return
//	 */
//	public int getMaxIterationNumber() {
//		return this.fs;
//	}

//	/**
//	 * Sets the Maximum iteration number
//	 */
//	public void setMaxIterationNumber(String maxIterations) {
//		this.maxIterations = Integer.parseInt(maxIterations);
//	}


	/**
	 * Returns alpha.
	 * 
	 * @return
	 */
	public int getAlpha() {
		return this.alpha;
	}

	/**
	 * sets alpha
	 */
	public void setAlpha(int i) {
		this.alpha = i;
	}

	/**
	 * Returns the iteration number.
	 * 
	 * @return
	 */
	public int getIterationNumber() {
		return this.iterationNumber;
	}

	/**
	 * Resets the iteration number.
	 */
	public void resetIterationNumber() {
		this.iterationNumber = 0;
		this.cellIteration = 0;
	}

	public void setUpdateMode(String optionString) {
		// TODO Auto-generated method stub
		this.updateMode = optionString;
	}

	public String getUpdateMode() {
		return this.updateMode;
	}


	public void setOnlyUpdatable(String optionString) {
		// TODO Auto-generated method stub
		this.onlyUpdatable = optionString;
	}

	public String getOnlyUpdatable() {
		return this.onlyUpdatable;
	}


	/**
	 * Returns the color of an instance when simulating
	 * 
	 * @param instance
	 * @param initial
	 * @return
	 */
	private Color getCoordinateColor(int instance, boolean initial) {
		int red = 255;
		int green = 255;
		int blue = 255;
		Color color = new Color(red, green, blue);

		if (!initial) {
			for (NodeInfo node : this.mainFrame.epithelium.getUnitaryModel()
					.getNodeOrder()) {

				if (this.mainFrame.epithelium.isDisplayComponentOn(node)) {

					int value = 0;
					if (currentGlobalState != null)
						value = currentGlobalState.getValue(instance, node);
					else
						value = this.mainFrame.epithelium.getGridValue(
								instance, node);

					if (value > 0) {
						color = this.mainFrame.epithelium.getColor(node);
						if (value >= 1)
							color = getColorLevel(color, value, node.getMax());

						if (red != 255)
							red = (red + color.getRed()) / 2;
						else
							red = color.getRed();

						if (green != 255)
							green = (green + color.getGreen()) / 2;
						else
							green = color.getGreen();

						if (blue != 255)
							blue = (blue + color.getBlue()) / 2;
						else
							blue = color.getBlue();

						color = new Color(red, green, blue);

					} else if (value == 0) {
						color = new Color(red, green, blue);

					}
				}
			}
		}
		return color;
	}

	/**
	 * Calls the method to generate an instance color at the initial conditions
	 * definition.
	 * 
	 * @param instance
	 * @return
	 * @see getCoordinateColor(int instance, boolean b)
	 */
	public Color getCoordinateInitialColor(int instance) {
		return getCoordinateColor(instance, true);
	}

	/**
	 * Calls the method to generate an instance color at the simulation.
	 * 
	 * @param instance
	 * @return
	 * @see getCoordinateColor(int instance, boolean b)
	 */
	public Color getCoordinateCurrentColor(int instance) {
		return getCoordinateColor(instance, false);
	}

	// public void fillHexagons() {
	//
	// for (int instance = 0; instance < this.mainFrame.topology
	// .getNumberInstances(); instance++) {
	//
	// for (NodeInfo node : this.mainFrame.epithelium.getUnitaryModel()
	// .getNodeOrder()) {
	//
	// Color color = getCoordinateCurrentColor(instance);
	// this.mainFrame.hexagonsPanel.drawHexagon(instance,
	// this.mainFrame.hexagonsPanel.getGraphics(), color);
	// }
	// }
	// }

	/**
	 * Returns the color selected for a component, but with a gradient
	 * associated with the value of the component and the maximum value
	 * 
	 * @param color
	 * @param value
	 * @param max
	 * @return
	 */
	public Color getColorLevel(Color color, int value, int max) {
		Color newColor = color;
		float res;
		if (value > 0) {
			res = (float) value / max;
			newColor = lighter(newColor, (1 - res));
		} else if (value == 0)
			newColor = Color.white;

		return newColor;
	}

	/**
	 * Lightens the color received.
	 * 
	 * @param color
	 * @param fraction
	 * @return lighter version of the color received
	 */
	public Color lighter(Color color, float fraction) {

		int red = (int) Math.round(color.getRed() * (1.0 + fraction));
		int green = (int) Math.round(color.getGreen() * (1.0 + fraction));
		int blue = (int) Math.round(color.getBlue() * (1.0 + fraction));

		if (red < 0)
			red = 0;
		else if (red > 255)
			red = 255;
		if (green < 0)
			green = 0;
		else if (green > 255)
			green = 255;
		if (blue < 0)
			blue = 0;
		else if (blue > 255)
			blue = 255;

		if (red == 0)
			red = (int) Math.round(color.getRed() * (1.2 + fraction));
		if (green == 0)
			green = (int) Math.round(color.getGreen() * (1.2 + fraction));
		if (blue == 0)
			blue = (int) Math.round(color.getBlue() * (1.2 + fraction));

		int alpha = color.getAlpha();

		return new Color(red, green, blue, alpha);

	}

	public void setMaxCellIterationNumber(String string) {
		// TODO Auto-generated method stub
		this.maxCellIteration =Integer. parseInt(string)*1000;

	}

	public void setStopCriterium(String string) {
		// TODO Auto-generated method stub
		this.stopCriterium =Integer. parseInt(string);
	}

	public void setStochastic(boolean b) {
		// TODO Auto-generated method stub
		this.stochastic = b;
	}


}
