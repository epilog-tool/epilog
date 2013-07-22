package pt.igc.nmd.epilog;

import java.awt.Color;
import java.awt.Container;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.colomoto.logicalmodel.NodeInfo;

import pt.igc.nmd.epilog.gui.MainFrame;

public class Simulation {

	private int iterationNumber = 0;
	private GlobalModel globalModel = null;

	public Grid currentGlobalState = null;
	private Grid nextGlobalState = null;
	private boolean runButtonActivated = false;

	private MainFrame mainFrame = null;

	private boolean isRunning = false;
	private boolean stableStateFound = false;
	private boolean runcontrol = true;

	/**
	 * Initializes the simulation setup.
	 * 
	 * @param mainFrame
	 */
	public Simulation(MainFrame mainFrame) {
		this.mainFrame = mainFrame;
	}

	/**
	 * Simulates for 30 iterations or until a stable state is found.
	 */
	public void run() {

		while (!stableStateFound
				&& (iterationNumber % 30 != 0 || runcontrol == false)) {
			runcontrol = true;
			runButtonActivated = true;
			step();
			runButtonActivated = false;
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
		//this.mainFrame.setBorderHexagonsPanel(iterationNumber);

		this.mainFrame.simulationSetupPanel.simulationPanelsoff();

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

		if (globalModel == null) {

			globalModel = new GlobalModel(this.mainFrame);
		}

		nextGlobalState = globalModel.getNextState(currentGlobalState);

		// saveLastPic();
		if (!runButtonActivated)
			this.mainFrame.fillHexagons();

		// if (nextGlobalState.equals(currentGlobalState)) {
		if (stateComparative(nextGlobalState, currentGlobalState)) {
			stableStateFound = true;
			resetIterationNumber();
			System.out.println("Stable State Found");
		}
		else
			this.mainFrame.setBorderHexagonsPanel(iterationNumber);
		
		currentGlobalState = nextGlobalState;
		
		
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
	 * Sets the need to reset the composed model as true or false
	 * 
	 * @param b
	 *            boolean value
	 */
	public void setNeedComposedModel(Boolean b) {
		this.mainFrame.needsComposedModel = b;
	}

	/**
	 * Initializes simulation, creating or not the composed model.
	 */
	public void initializeSimulation() {

		this.mainFrame.epithelium = this.mainFrame.getEpithelium();
		resetIterationNumber();

		if (this.mainFrame.epithelium.getComposedModel() == null
				&& this.mainFrame.needsComposedModel) {
			this.mainFrame.getLogicalModelComposition().createComposedModel();
		}

		setRunning(true);

		this.mainFrame.fillHexagons();
		this.mainFrame.hexagonsPanel
				.paintComponent(this.mainFrame.hexagonsPanel.getGraphics());
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

		System.out.println(fraction);
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
}
