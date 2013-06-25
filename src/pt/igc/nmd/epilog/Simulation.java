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
	private boolean needsComposedModel = false;

	public Grid currentGlobalState = null;
	private Grid nextGlobalState = null;

	private MainFrame mainFrame = null;
	private Epithelium epithelium = null;

	private boolean isRunning = false;
	private boolean stableStateFound = false;

	public Simulation(MainFrame mainFrame, Epithelium epithelium) {
		this.mainFrame = mainFrame;
		this.epithelium = epithelium;
	}

	public Simulation(MainFrame mainFrame, Epithelium epithelium,
			boolean needsComposedModel) {
		this.mainFrame = mainFrame;
		this.epithelium = epithelium;
		this.needsComposedModel = needsComposedModel;
	}

	public void run() {
		resetIterationNumber();
		while (!stableStateFound) {
			step();
		}
	}

	public void setRunning(boolean b) {
		this.isRunning = b;
	}

	public void reset() {
		setRunning(false);
		currentGlobalState = null;
		globalModel = null;
		nextGlobalState=null;
		resetIterationNumber();
		stableStateFound = false;
	}

	public boolean isRunning() {
		return this.isRunning;
	}

	public void step() {
		
		setRunning(true);
		this.mainFrame.setBorderHexagonsPanel(iterationNumber);

		
		if (currentGlobalState == null) {
			currentGlobalState = new Grid(
					this.mainFrame.topology.getNumberInstances(), this.epithelium
							.getUnitaryModel().getNodeOrder());
			for (int instance = 0; instance < currentGlobalState
					.getNumberInstances(); instance++)
				for (NodeInfo node : currentGlobalState.getListNodes())
					currentGlobalState.setGrid(instance, node,
							this.epithelium.getGridValue(instance, node));
		}

		if (globalModel == null) {
			
			globalModel = new GlobalModel(this.mainFrame, this.epithelium,
					this.needsComposedModel);
		}

		nextGlobalState = globalModel.getNextState(currentGlobalState);

		saveLastPic();
		fillHexagons();

		this.iterationNumber++;

		if (nextGlobalState.equals(currentGlobalState)) {
			stableStateFound = true;
			resetIterationNumber();
		}
		currentGlobalState = nextGlobalState;

	}

	public int getCurrentGlobalStateValue(int instance, NodeInfo node){
		int value = 0;
		if (currentGlobalState != null)
			value = currentGlobalState.getValue(instance, node);
		else
			value = this.mainFrame.epithelium.getGridValue(instance,
					node);
		
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
	
	public void setNeedComposedModel(Boolean b){
		this.needsComposedModel = b;
	}

	public void initializeSimulation() {

		this.epithelium = this.mainFrame.getEpithelium();
		resetIterationNumber();

		if (this.epithelium.getComposedModel() == null && this.needsComposedModel) {
			this.mainFrame.getLogicalModelComposition().createComposedModel();
		}

		setRunning(true);

		fillHexagons();
		this.mainFrame.hexagonsPanel.paintComponent(this.mainFrame.hexagonsPanel
				.getGraphics());
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
						value = this.mainFrame.epithelium.getGridValue(instance,
								node);

					if (value > 0) {
						color = this.mainFrame.epithelium.getColor(node);
						color = getColorLevel(color, value);

						red = (red + color.getRed()) / 2;
						green = (green + color.getGreen()) / 2;
						blue = (blue + color.getBlue()) / 2;
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


		for (int instance = 0; instance < this.mainFrame.topology.getNumberInstances(); instance++) {

			for (NodeInfo node : epithelium.getUnitaryModel().getNodeOrder()) {

				Color color = getCoordinateCurrentColor(instance);
				this.mainFrame.hexagonsPanel.drawHexagon(instance,
						this.mainFrame.hexagonsPanel.getGraphics(), color);
			}
		}
	}

	public Color getColorLevel(Color color, float value) {
		Color newColor = color;

		if (value > 0)
			for (int j = 2; j <= value; j++)
				newColor = newColor.darker();
		else if (value == 0)
			newColor = Color.white;

		return newColor;
	}

}
