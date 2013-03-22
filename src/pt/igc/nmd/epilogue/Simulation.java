package pt.igc.nmd.epilogue;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Set;

import org.colomoto.logicalmodel.LogicalModel;
import org.colomoto.logicalmodel.NodeInfo;

public class Simulation {

	private int iterationNumber = 0;
	private LogicalModel composedModel = null;
	private byte[] state = null;
	private MainPanel mainPanel;
	private Hashtable<String, Integer> node2State;
	private Hashtable<String, Integer> initialState;
	private Hashtable<String, Integer> composedInitialState;
	private Hashtable<String, Integer> node2Int;
	private Color color;
	private boolean hasStarted;

	public Simulation(MainPanel mainPanel) {
		color = Color.white;
		this.mainPanel = mainPanel;
		this.state = null;
		this.composedModel = null;
		this.initialState = new Hashtable<String, Integer>();
		this.composedInitialState = new Hashtable<String, Integer>();
		this.node2Int = new Hashtable<String, Integer>();
		this.node2State = new Hashtable<String, Integer>();
		this.hasStarted = false;
	}

	public void setNode2Int(String nodeID, Integer index) {
		this.node2Int.put(nodeID, index);
	}

	public Hashtable<String, Integer> getNode2Int() {
		return this.node2Int;
	}

	public void run() {
		byte[] currentState;
		do {
			step();
			currentState = this.state;
			fillHexagons();
			 System.out.println(hasChanged(currentState, this.state)) ;
		} while (hasChanged(currentState, this.state));
	}
	
	public void setHasInitiated(boolean b){
		this.hasStarted=b;
	}
	
	public boolean getHasInitiated(){
		return this.hasStarted;
	}

	public void step() {
		
		if (!mainPanel.getSimulation().getHasInitiated()){
			mainPanel.getSimulation().initializeInitialStates();	
			mainPanel.getSimulation().setHasInitiated(true);
			System.out.println("estou aqui");
		}
		testmethod();
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
			fillHexagons();
			mainPanel.hexagonsPanel.paintComponent(mainPanel.hexagonsPanel
					.getGraphics());
		} else {

			for (NodeInfo node : composedModel.getNodeOrder()) {

				int index = composedModel.getNodeOrder().indexOf(node);
				
				byte next = 0;
				byte target;
				byte current;

				current = this.state[index];
				target = composedModel.getTargetValue(index, this.state);
				
				
				if (current != 0 | target != 0) {
					next = (byte) (current + ((target - current) / (target + current)));
				}

				this.state[index] = next;
				node2State.put(node.getNodeID(), (int) next);
				System.out.println(node +" "+ index+" "+node.getMax() + " " +node.isInput()+ " " + current + " " + target + " "
						+ next);
				//System.out.println(next);
				
			}
			this.iterationNumber++;
			
		}
		fillHexagons();
		mainPanel.hexagonsPanel.paintComponent(mainPanel.hexagonsPanel
				.getGraphics());

	}


	public void initializeInitialStates() {

		Set<String> a = mainPanel.getSimulation().getNode2Int().keySet();
		for (int i = 0; i < mainPanel.getTopology().getWidth(); i++) {
			for (int j = 0; j < mainPanel.getTopology().getHeight(); j++) {
				for (String a2 : a) {
					mainPanel.getSimulation().setComposedInitialState(
							mainPanel.getLogicalModelComposition()
									.computeNewName(
											a2,
											mainPanel.getTopology()
													.coords2instance(i, j)),
							(byte) 0);
				}
			}
		}
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

	public void fillHexagons() {

		int row;
		int column;

		for (int i = 0; i < mainPanel.getTopology().getNumberInstances(); i++) {

			row = mainPanel.getTopology().instance2Row(i,
					mainPanel.getTopology().getWidth());
			column = mainPanel.getTopology().instance2Column(i,
					mainPanel.getTopology().getHeight());

			for (String key : mainPanel.getEpithelium()
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

		Set<String> a = mainPanel.getEpithelium().getComponentsDisplayOn()
				.keySet();

		for (String a2 : a) {
			if (mainPanel.getEpithelium().getComponentsDisplayOn().get(a2)) {

				String key = mainPanel.getLogicalModelComposition()
						.computeNewName(a2,
								mainPanel.getTopology().coords2instance(i, j));
				int value = node2State.get(key);

				int maxValue = mainPanel.getEpithelium().getUnitaryModel()
						.getNodeOrder().get(node2Int.get(a2)).getMax();

				float as = (float) value / (float) maxValue;

				if (value > 0) {
					this.color = mainPanel.getEpithelium().getColors().get(a2);
					this.color = ColorBrightness(color, value);

					red = (red + color.getRed())/2;
					green =( green + color.getGreen())/2;
					blue = (blue + color.getBlue())/2;
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

public void resetComposedInitialState(){
	for(String node: this.composedInitialState.keySet()){
		this.composedInitialState.put(node, 0);
	}
}

public void testmethod(){
	byte[] unitaryState;
	
	unitaryState =  new byte[mainPanel.getEpithelium().getUnitaryModel().getNodeOrder().size()];
//	System.out.println("Current:");
//	for (int i = 0;i<unitaryState.length;i++){
//		unitaryState[i]=1;
//		unitaryState[1]=0;
//		System.out.print(unitaryState[i]);
//	}
//	
//	System.out.println("Target:");
//	for (int i = 0;i<unitaryState.length;i++){
//		System.out.print(mainPanel.getEpithelium().getUnitaryModel().getTargetValue(1, unitaryState));
//		unitaryState[i]=mainPanel.getEpithelium().getUnitaryModel().getTargetValue(1, unitaryState);
//	}
//	
//	System.out.println("Current:");
//	for (int i = 0;i<unitaryState.length;i++){
//		System.out.print(unitaryState[i]);
//
//	}
//	System.out.println("Target:");
//	for (int i = 0;i<unitaryState.length;i++){
//		System.out.print(mainPanel.getEpithelium().getUnitaryModel().getTargetValue(1, unitaryState));
//		unitaryState[i]=mainPanel.getEpithelium().getUnitaryModel().getTargetValue(1, unitaryState);
//	}
//	
//	System.out.println("Current:");
//	for (int i = 0;i<unitaryState.length;i++){
//		System.out.print(unitaryState[i]);
//
//	}
//	System.out.println("Target:");
//	for (int i = 0;i<unitaryState.length;i++){
//		System.out.print(mainPanel.getEpithelium().getUnitaryModel().getTargetValue(1, unitaryState));
//		unitaryState[i]=mainPanel.getEpithelium().getUnitaryModel().getTargetValue(1, unitaryState);
//	}
}
	
}
