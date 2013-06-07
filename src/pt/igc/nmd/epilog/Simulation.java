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
			boolean needsComposeModel) {
		this.mainFrame = mainFrame;
		this.epithelium = epithelium;
		this.needsComposedModel = needsComposeModel;
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
	}

	public boolean isRunning() {
		return this.isRunning;
	}

	public void step() {
		
		setRunning(true);
		mainFrame.setBorderHexagonsPanel(iterationNumber);

		if (currentGlobalState == null) {
			currentGlobalState = new Grid(
					mainFrame.topology.getNumberInstances(), epithelium
							.getUnitaryModel().getNodeOrder());
			for (int instance = 0; instance < currentGlobalState
					.getNumberInstances(); instance++)
				for (NodeInfo node : currentGlobalState.getListNodes())
					currentGlobalState.setGrid(instance, node,
							epithelium.getGridValue(instance, node));
		}

		if (globalModel == null) {
			globalModel = new GlobalModel(mainFrame, epithelium,
					needsComposedModel);
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

	public void saveLastPic() {
		Container c = mainFrame.hexagonsPanel;
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

	public void initializeSimulation() {

		this.epithelium = mainFrame.getEpithelium();
		resetIterationNumber();

		if (epithelium.getComposedModel() == null && needsComposedModel) {
			mainFrame.getLogicalModelComposition().createComposedModel();
		}

		setRunning(true);

		fillHexagons();
		mainFrame.hexagonsPanel.paintComponent(mainFrame.hexagonsPanel
				.getGraphics());
	}

	public int getIterationNumber() {
		return this.iterationNumber;
	}

	public void resetIterationNumber() {
		this.iterationNumber = 1;
	}

	private Color getCoordinateColor(int i, int j, boolean initial) {
		int red = 255;
		int green = 255;
		int blue = 255;
		Color color = new Color(red, green, blue);

		int instance = mainFrame.topology.coords2Instance(i, j);

		if (!initial) {
			for (NodeInfo node : mainFrame.epithelium.getUnitaryModel()
					.getNodeOrder()) {
				
				if (mainFrame.epithelium.isDisplayComponentOn(node)) {
					

					int value = 0;
					if (currentGlobalState != null)
						value = currentGlobalState.getValue(instance, node);
					else
						value = mainFrame.epithelium.getGridValue(instance,
								node);

					if (value > 0) {
						color = mainFrame.epithelium.getColor(node);
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

	public Color getCoordinateInitialColor(int i, int j) {
		return getCoordinateColor(i, j, true);
	}

	public Color getCoordinateCurrentColor(int i, int j) {
		return getCoordinateColor(i, j, false);
	}

	public void fillHexagons() {

		int row;
		int column;

		for (int i = 0; i < mainFrame.topology.getNumberInstances(); i++) {

			row = mainFrame.topology.instance2i(i,
					mainFrame.topology.getWidth());
			column = mainFrame.topology.instance2j(i,
					mainFrame.topology.getHeight());

			for (NodeInfo node : epithelium.getUnitaryModel().getNodeOrder()) {

				Color color = getCoordinateCurrentColor(row, column);
				mainFrame.hexagonsPanel.drawHexagon(row, column,
						mainFrame.hexagonsPanel.getGraphics(), color);
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
