package pt.igc.nmd.epilogue;

import java.awt.Color;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

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
	private Hashtable<NodeInfo, Integer> initialState;
	public Hashtable<String, Byte> composedState = null;
	private Hashtable<NodeInfo, Integer> node2Int;
	private Hashtable<String, NodeInfo> string2OldNode;
	private Color color;
	private boolean hasStarted;
	private boolean stableStateFound;

	public Simulation(MainPanel mainPanel) {
		color = Color.white;
		this.mainPanel = mainPanel;
		this.state = null;
		this.composedModel = null;
		this.initialState = new Hashtable<NodeInfo, Integer>();
		this.composedState = new Hashtable<String, Byte>();
		this.node2Int = new Hashtable<NodeInfo, Integer>();
		this.string2OldNode = new Hashtable<String, NodeInfo>();
		this.hasStarted = false;
		this.stableStateFound = false;

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
		System.out.println(iterationNumber);

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
			// System.out.println(node +" "+ index+" "+node.getMax() + " "
			// +node.isInput()+ " " + current + " " + target + " "
			// + next);

			if (current != next) {
				stableStateFound_aux = false;
			}

		}
		if (stableStateFound_aux) {
			this.stableStateFound = true;
		}

		fillHexagons();
		mainPanel.hexagonsPanel.paintComponent(mainPanel.hexagonsPanel
				.getGraphics());
		this.iterationNumber++;
		this.state = nextState;

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

			for (NodeInfo a2 : a) {
				setComposedState(mainPanel.getLogicalModelComposition()
						.computeNewName(a2.getNodeID(), instance), mainPanel
						.getGrid().getGrid().get(instance).get(a2));
				string2OldNode.put(mainPanel.getLogicalModelComposition()
						.computeNewName(a2.getNodeID(), instance), a2);
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

	public void setInitialState(NodeInfo nodeInfo, Integer initialStateValue) {
		this.initialState.put(nodeInfo, initialStateValue);
	}

	public int getInitialState(NodeInfo a2) {
		return (int) this.initialState.get(a2);
	}

	public void setComposedState(String composedNodeID, byte composedState) {
		this.composedState.put(composedNodeID, new Byte(composedState));

	}

	public Hashtable<String, Byte> getComposedState() {
		return this.composedState;
	}

	public Color ColorInitial(int i, int j) {

		int red = 255;
		int green = 255;
		int blue = 255;
		color = new Color(red, green, blue);

		Set<NodeInfo> a = mainPanel.getEpithelium().getComponentsDisplayOn()
				.keySet();

		int instance = mainPanel.getTopology().coords2Instance(i, j);

		for (NodeInfo a2 : a) {
			if (mainPanel.getEpithelium().getComponentsDisplayOn().get(a2)) {

				int value = mainPanel.getGrid().getGrid().get(instance).get(a2);

				if (value > 0) {
					color = mainPanel.getEpithelium().getColors().get(a2);
					color = ColorBrightness(color, value);

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

	public void fillHexagons() {

		int row;
		int column;

		for (int i = 0; i < mainPanel.getTopology().getNumberInstances(); i++) {

			row = mainPanel.getTopology().instance2i(i,
					mainPanel.getTopology().getWidth());
			column = mainPanel.getTopology().instance2j(i,
					mainPanel.getTopology().getHeight());

			for (NodeInfo node : mainPanel.getEpithelium()
					.getComponentsDisplayOn().keySet()) {

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

		Set<NodeInfo> a = mainPanel.getEpithelium().getComponentsDisplayOn()
				.keySet();

		for (NodeInfo a2 : a) {
			if (mainPanel.getEpithelium().getComponentsDisplayOn().get(a2)) {

				String key = mainPanel.getLogicalModelComposition()
						.computeNewName(a2.getNodeID(),
								mainPanel.getTopology().coords2Instance(i, j));

				int value = composedState.get(key);

				if (value > 0) {
					this.color = mainPanel.getEpithelium().getColors().get(a2);
					this.color = ColorBrightness(color, value);

					red = (red + color.getRed()) / 2;
					green = (green + color.getGreen()) / 2;
					blue = (blue + color.getBlue()) / 2;
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
				this.color = color.darker();
			}
		} else if (value == 0) {
			this.color = Color.white;
		}
		return this.color;
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
