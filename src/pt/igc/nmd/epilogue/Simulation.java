package pt.igc.nmd.epilogue;

import java.awt.Color;
import java.awt.Container;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.border.TitledBorder;

import org.colomoto.logicalmodel.LogicalModel;
import org.colomoto.logicalmodel.NodeInfo;

public class Simulation {

	private int iterationNumber = 1;
	private LogicalModel composedModel = null;
	private byte[] state = null;
	byte[] currentState = null;
	private MainPanel mainPanel;
	private SphericalEpithelium epithelium;
	public Hashtable<String, Byte> composedState = null;
	private Hashtable<NodeInfo, Integer> node2Int;
	private Hashtable<String, NodeInfo> string2OldNode;
	private boolean hasStarted;
	private boolean stableStateFound;

	public Simulation(MainPanel mainPanel, SphericalEpithelium epithelium) {
		this.mainPanel = mainPanel;
		this.state = null;
		this.composedModel = null;
		this.composedState = new Hashtable<String, Byte>();
		this.node2Int = new Hashtable<NodeInfo, Integer>();
		this.string2OldNode = new Hashtable<String, NodeInfo>();
		this.hasStarted = false;
		this.stableStateFound = false;
		this.epithelium = epithelium;

	}

	public void setNode2Int(NodeInfo node, Integer index) {
		this.node2Int.put(node, index);
	}

	public Hashtable<NodeInfo, Integer> getNode2Int() {
		return this.node2Int;
	}

	public void run() {
		resetIterationNumber();
		this.stableStateFound = false;
		while (!this.stableStateFound) {
			step();
		}
	}

	public void setHasInitiated(boolean b) {
		this.hasStarted = b;
	}

	public boolean getHasInitiated() {
		return this.hasStarted;
	}

	public void step() {

		this.stableStateFound = false;
		testmethod();
		// System.out.println(iterationNumber);

		mainPanel.setBorderHexagonsPanel(iterationNumber);

		for (NodeInfo node : composedModel.getNodeOrder()) {

			int index = composedModel.getNodeOrder().indexOf(node);
			this.state[index] = composedState.get(node.getNodeID());
		}

		boolean stableStateFound_aux = true;

		byte[] nextState = new byte[this.state.length];
		for (NodeInfo node : composedModel.getNodeOrder()) {

			int index = composedModel.getNodeOrder().indexOf(node);

			byte next = 0;
			byte target;
			byte current;

			current = this.state[index];
			target = composedModel.getTargetValue(index, this.state);

			if (current != target)
				next = (byte) (current + ((target - current) / Math.abs(target
						- current)));
			else
				next = target;

			nextState[index] = next;
			setComposedState(node.getNodeID(), next);
			// System.out.println(node + " " + index + " " + node.getMax() + " "
			// + node.isInput() + " " + current + " " + target + " "
			// + next);

			if (current != next) {
				stableStateFound_aux = false;
			}

		}
		if (stableStateFound_aux) {
			this.stableStateFound = true;
		}
		saveLastPic();
		fillHexagons();
		mainPanel.hexagonsPanel.paintComponent(mainPanel.hexagonsPanel
				.getGraphics());
		this.iterationNumber++;
		this.state = nextState;

	}

	public void saveLastPic() {
		Container c = mainPanel.hexagonsPanel;
		BufferedImage im = new BufferedImage(c.getWidth(), c.getHeight(),
				BufferedImage.TYPE_INT_ARGB);
		c.paint(im.getGraphics());
		try {
			ImageIO.write(im, "PNG", new File("shot.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public boolean hasStableStateFound() {
		return this.stableStateFound;
	}

	public void initializeSimulation() {

		this.composedModel = mainPanel.getEpithelium().getComposedModel();

		if (mainPanel.initialSetupHasChanged) {
			composedModel = mainPanel.getLogicalModelComposition()
					.createComposedModel();

		}
		mainPanel.setInitialSetupHasChanged(false);

		this.state = new byte[this.composedModel.getNodeOrder().size()];
		setHasInitiated(true);

		List<NodeInfo> a = mainPanel.getEpithelium().getUnitaryModel()
				.getNodeOrder();

		for (int instance = 0; instance < mainPanel.getTopology()
				.getNumberInstances(); instance++) {

			for (NodeInfo node : a) {
				setComposedState(mainPanel.getLogicalModelComposition()
						.computeNewName(node.getNodeID(), instance), 
						epithelium.getGridValue(instance, node)
						);
				string2OldNode.put(mainPanel.getLogicalModelComposition()
						.computeNewName(node.getNodeID(), instance), node);
			}
		}

		fillHexagons();
		mainPanel.hexagonsPanel.paintComponent(mainPanel.hexagonsPanel
				.getGraphics());
	}

	public int getIterationNumber() {
		return this.iterationNumber;
	}

	public void resetIterationNumber() {
		this.iterationNumber = 1;
	}

	public byte[] getCurrentState() {
		return this.state;
	}

	public void setComposedState(String composedNodeID, byte composedState) {
		this.composedState.put(composedNodeID, new Byte(composedState));

	}

	public Hashtable<String, Byte> getComposedState() {
		return this.composedState;
	}

	private Color getCoordinateColor(int i, int j, boolean initial) {
		int red = 255;
		int green = 255;
		int blue = 255;
		Color color = new Color(red, green, blue);

		int instance = mainPanel.getTopology().coords2Instance(i, j);

		for (NodeInfo node : mainPanel.getEpithelium().getUnitaryModel()
				.getNodeOrder()) {
			if (mainPanel.getEpithelium().isDisplayComponentOn(node)) {

				int value = 0;
				if (initial)
					value = epithelium.getGridValue(instance, node);
				else
					value = composedState.get(mainPanel
							.getLogicalModelComposition().computeNewName(
									node.getNodeID(), instance));

				if (value > 0) {
					color = mainPanel.getEpithelium().getColor(node);
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

		for (int i = 0; i < mainPanel.getTopology().getNumberInstances(); i++) {

			row = mainPanel.getTopology().instance2i(i,
					mainPanel.getTopology().getWidth());
			column = mainPanel.getTopology().instance2j(i,
					mainPanel.getTopology().getHeight());

			for (NodeInfo node : mainPanel.getEpithelium().getUnitaryModel()
					.getNodeOrder()) {

				mainPanel.hexagonsPanel.drawHexagon(row, column,
						mainPanel.hexagonsPanel.getGraphics());
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

	public void resetComposedInitialState() {
		for (String node : this.composedState.keySet()) {
			this.composedState.put(node, (byte) 0);
		}
	}

	public void testmethod() {
		// byte[] unitaryState;

		// unitaryState = new byte[mainPanel.getEpithelium().getUnitaryModel()
		// .getNodeOrder().size()];
		// System.out.println("Current:");
		// for (int i = 0;i<unitaryState.length;i++){
		// unitaryState[i]=1;
		// unitaryState[1]=0;
		// System.out.print(unitaryState[i]);
		// }
		//
		// System.out.println("Target:");
		// for (int i = 0;i<unitaryState.length;i++){
		// System.out.print(mainPanel.getEpithelium().getUnitaryModel().getTargetValue(1,
		// unitaryState));
		// unitaryState[i]=mainPanel.getEpithelium().getUnitaryModel().getTargetValue(1,
		// unitaryState);
		// }
		//
		// System.out.println("Current:");
		// for (int i = 0;i<unitaryState.length;i++){
		// System.out.print(unitaryState[i]);
		//
		// }
		// System.out.println("Target:");
		// for (int i = 0;i<unitaryState.length;i++){
		// System.out.print(mainPanel.getEpithelium().getUnitaryModel().getTargetValue(1,
		// unitaryState));
		// unitaryState[i]=mainPanel.getEpithelium().getUnitaryModel().getTargetValue(1,
		// unitaryState);
		// }
		//
		// System.out.println("Current:");
		// for (int i = 0;i<unitaryState.length;i++){
		// System.out.print(unitaryState[i]);
		//
		// }
		// System.out.println("Target:");
		// for (int i = 0;i<unitaryState.length;i++){
		// System.out.print(mainPanel.getEpithelium().getUnitaryModel().getTargetValue(1,
		// unitaryState));
		// unitaryState[i]=mainPanel.getEpithelium().getUnitaryModel().getTargetValue(1,
		// unitaryState);
		// }
	}

}
