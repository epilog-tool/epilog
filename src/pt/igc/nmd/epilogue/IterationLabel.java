package pt.igc.nmd.epilogue;

import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JTextField;


public class IterationLabel extends JLabel {

	int iterationNumber= 0;
	//Simulation currentSimulation = new Simulation();
	//Logicalmodel model = new LogicalModel();
	
	
	public IterationLabel() {
		init();
	}

	public IterationLabel init() {

		setBackground(Color.white);
		//iterationNumber = Simulation().getIterationNumber();
		setText("" + iterationNumber);

		return this;

	}


}
