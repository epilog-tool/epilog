package pt.igc.nmd.epilogue;

import java.awt.Color;
import java.util.Hashtable;
import java.util.Set;

import org.colomoto.logicalmodel.LogicalModel;
import org.colomoto.logicalmodel.NodeInfo;

public class Simulation {

	private int iterationNumber = 0;
	private LogicalModel unitaryModel = null;
	private LogicalModel composedModel = null;
	private byte[] state = null;
	private MainPanel mainPanel;
	private Hashtable<String, Integer> node2State;
	private Hashtable<String, Integer> initialState;
	private Hashtable<String, Integer> composedInitialState;
	private Hashtable<String, Integer> node2Int;
	private Color color;

	public Simulation(MainPanel mainPanel) {
		color = Color.white;
		this.mainPanel = mainPanel;
		this.state = null;
		this.composedModel = null;
		this.initialState = new Hashtable<String, Integer>();
		this.composedInitialState = new Hashtable<String, Integer>();
		this.node2Int = new Hashtable<String, Integer>();
		this.node2State = new Hashtable<String, Integer>();
	}

	public void setNode2Int(String nodeID, Integer index) {
		this.node2Int.put(nodeID, index);
	}

	public Hashtable<String, Integer> getNode2Int() {
		return this.node2Int;
	}

	private String computeNewName(String nodeID, int instanceIndex) {
		// moduleId starts at 1, as all iterations begin at 0, we add 1 here
		// (NUNO)
		return nodeID + "_" + (instanceIndex + 1);
	}

	public void run() {
		byte[] currentState;
		do {
			currentState = this.state;
			// fillHexagons();
			step();
		} while (hasChanged(currentState, this.state));
	}

	public void step() {

		composedModel = mainPanel.getLogicalModelComposition()
				.createComposedModel();

		if (iterationNumber == 0) {

			this.state = new byte[composedModel.getNodeOrder().size()];

			int i = 0;
			for (NodeInfo node : mainPanel.getLogicalModelComposition()
					.createComposedModel().getNodeOrder()) {

				this.state[i] = composedInitialState.get("" + node).byteValue();
				node2State.put(node.getNodeID(),
						composedInitialState.get("" + node));
				i++;

			}
			this.iterationNumber++;
			fillhexagons();
		} else {

			for (NodeInfo node : composedModel.getNodeOrder()) {

				int index = composedModel.getNodeOrder().indexOf(node);

				byte current = this.state[index];
				byte target = composedModel.getTargetValue(index, this.state);
				byte next = 0;
				if (current != 0 | target != 0) {
					next = (byte) (current + ((target - current) / (target + current)));
				}
				this.state[index] = next;
				node2State.put(node.getNodeID(), (int) next);
				System.out.println(node + " " + current + " " + target + " "
						+ next);
			}
			this.iterationNumber++;
		}
		fillhexagons();
		System.out.println(this.state);
	}

	public int getIterationNumber() {
		return this.iterationNumber;
	}

	public void resetIterationNumber() {
		this.iterationNumber = 0;
	}

	public byte[] getCurrentState() {
		return this.state;
	}

	private boolean hasChanged(byte[] previous, byte[] current) {
		if (previous.length != current.length)
			return true;

		for (int i = 0; i < previous.length; i++)
			if (previous[i] != current[i])
				return true;

		return false;

	}

	public void setInitialState(String nodeID, Integer initialStateValue) {
		this.initialState.put(nodeID, initialStateValue);
	}

	public int getInitialState(String nodeID) {
		return (int) this.initialState.get(nodeID);
	}

	public void setComposedInitialState(String composedNodeID,
			int ComposedInitialState) {
		this.composedInitialState.put(composedNodeID, ComposedInitialState);

	}

	public Hashtable<String, Integer> getComposedInitialState() {
		return this.composedInitialState;
	}

	public void fillhexagons() {

		int row;
		int column;

		for (int i = 0; i < mainPanel.getTopology().getNumberInstances(); i++) {

			row = mainPanel.getTopology().instance2Row(i,
					mainPanel.getTopology().getWidth());
			column = mainPanel.getTopology().instance2Column(i,
					mainPanel.getTopology().getHeight());

			for (String key : mainPanel.getEpithelium()
					.getComponentsDisplayOn().keySet()) {

				this.color = Color(row, column);
				// System.out.println(this.color);
				mainPanel.hexagonsPanel.drawHexagon(row, column,
						mainPanel.hexagonsPanel.getGraphics());
			}

		}
	}

	public Color Color(int i, int j) {

		int red = 255;
		int green = 255;
		int blue = 255;
		this.color = new Color(red, green, blue);

		Set<String> a = mainPanel.getEpithelium().getComponentsDisplayOn()
				.keySet();

		for (String a2 : a) {
			if (mainPanel.getEpithelium().getComponentsDisplayOn().get(a2)) {

				String key = mainPanel.getLogicalModelComposition()
						.computeNewName(a2,
								mainPanel.getTopology().coords2instance(i, j));
				int value = node2State.get(key);
				System.out.println(value);

				int maxValue = mainPanel.getEpithelium().getUnitaryModel()
						.getNodeOrder().get(node2Int.get(a2)).getMax();

				float as = (float) value / (float) maxValue;

				if (value > 0) {
					this.color = mainPanel.getEpithelium().getColors().get(a2);
					this.color = ColorBrightness(color, value);

					red = (red * color.getRed()) / 255;
					green = (green * color.getGreen()) / 255;
					blue = (blue * color.getBlue()) / 255;
					this.color = new Color(red, green, blue);
				}

				else if (value == 0) {
					this.color = new Color(red, green, blue);
				}

			}

		}

		return this.color;
	}

	public Color ColorBrightness(Color color, float value) {
		if (value > 0) {

			for (int j = 2; j <= value; j++) {
				this.color = color.brighter();
			}
		} else if (value == 0) {
			this.color = color.white;
		}
		return this.color;

	}

}
