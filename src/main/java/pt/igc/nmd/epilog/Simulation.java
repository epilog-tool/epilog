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

	private int iterationNumber = 1;
	private GlobalModel globalModel = null;

	public Grid currentGlobalState = null;
	private Grid nextGlobalState = null;
	private boolean runButtonActivated = false;

	private MainFrame mainFrame = null;

	private boolean isRunning = false;
	private boolean stableStateFound = false;

	public Simulation(MainFrame mainFrame) {
		this.mainFrame = mainFrame;
	}

	public void run() {

		while (!stableStateFound && iterationNumber % 30 != 0) {
			runButtonActivated = true;
			step();
			runButtonActivated = false;
		}
	}

	public void setRunning(boolean b) {
		this.isRunning = b;
	}

	public void reset() {
		setRunning(false);
		currentGlobalState = null;
		globalModel = null;
		nextGlobalState = null;
		resetIterationNumber();
		stableStateFound = false;
	}

	public boolean isRunning() {
		return this.isRunning;
	}

	public void step() {

		setRunning(true);
		this.mainFrame.setBorderHexagonsPanel(iterationNumber);

		this.mainFrame.simulationSetupPanel.simulationPanelsoff();

		if (currentGlobalState == null) {
			currentGlobalState = new Grid(
					this.mainFrame.topology.getNumberInstances(),
					this.mainFrame.epithelium.getUnitaryModel().getNodeOrder());
			for (int instance = 0; instance < currentGlobalState
					.getNumberInstances(); instance++)
				for (NodeInfo node : currentGlobalState.getListNodes())
					currentGlobalState.setGrid(instance, node,
							this.mainFrame.epithelium.getGridValue(instance, node));
		}

		if (globalModel == null) {

			globalModel = new GlobalModel(this.mainFrame);
		}

		nextGlobalState = globalModel.getNextState(currentGlobalState);

		//saveLastPic();
		if (!runButtonActivated)
			fillHexagons();

		this.iterationNumber++;

		if (nextGlobalState.equals(currentGlobalState)) {
			stableStateFound = true;
			resetIterationNumber();
			System.out.println("StableSate Found");
		}
		currentGlobalState = nextGlobalState;

	}

	public int getCurrentGlobalStateValue(int instance, NodeInfo node) {
		int value = 0;
		if (currentGlobalState != null)
			value = currentGlobalState.getValue(instance, node);
		else
			value = this.mainFrame.epithelium.getGridValue(instance, node);

		return value;
	}

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

	public boolean hasStableStateFound() {
		return this.stableStateFound;
	}

	public void setNeedComposedModel(Boolean b) {
		this.mainFrame.needsComposedModel = b;
	}

	public void initializeSimulation() {

		this.mainFrame.epithelium = this.mainFrame.getEpithelium();
		resetIterationNumber();

		if (this.mainFrame.epithelium.getComposedModel() == null
				&& this.mainFrame.needsComposedModel) {
			this.mainFrame.getLogicalModelComposition().createComposedModel();
		}

		setRunning(true);

		fillHexagons();
		this.mainFrame.hexagonsPanel
				.paintComponent(this.mainFrame.hexagonsPanel.getGraphics());
	}

	public int getIterationNumber() {
		return this.iterationNumber;
	}

	public void resetIterationNumber() {
		this.iterationNumber = 1;
	}

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

	public Color getCoordinateInitialColor(int instance) {
		return getCoordinateColor(instance, true);
	}

	public Color getCoordinateCurrentColor(int instance) {
		return getCoordinateColor(instance, false);
	}

	public void fillHexagons() {

		for (int instance = 0; instance < this.mainFrame.topology
				.getNumberInstances(); instance++) {

			for (NodeInfo node : this.mainFrame.epithelium.getUnitaryModel().getNodeOrder()) {

				Color color = getCoordinateCurrentColor(instance);
				this.mainFrame.hexagonsPanel.drawHexagon(instance,
						this.mainFrame.hexagonsPanel.getGraphics(), color);
			}
		}
	}

	public Color getColorLevel(Color color, int value, int max) {
		Color newColor = color;
		float res;
		if (value > 0) {
			res = value / max;
			newColor = lighter(newColor, (1-res));
		} else if (value == 0)
			newColor = Color.white;

		return newColor;
	}

	public Color lighter(Color color, float fraction) {
		
		
	    int red   = (int) Math.round (color.getRed()   * (1.0 + fraction));
	    int green = (int) Math.round (color.getGreen() * (1.0 + fraction));
	    int blue  = (int) Math.round (color.getBlue()  * (1.0 + fraction));

	    if (red   < 0) red   = 0; else if (red   > 255) red   = 255;
	    if (green < 0) green = 0; else if (green > 255) green = 255;
	    if (blue  < 0) blue  = 0; else if (blue  > 255) blue  = 255;    
	    
	    if (red   == 0) red   = (int) Math.round (color.getRed()   * (1.2 + fraction));
	    if (green == 0) green = (int) Math.round (color.getGreen()   * (1.2 + fraction));
	    if (blue  == 0) blue  = (int) Math.round (color.getBlue()   * (1.2 + fraction));  

	    int alpha = color.getAlpha();

	    return new Color (red, green, blue, alpha);

	}
}
