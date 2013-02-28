package pt.igc.nmd.epilogue;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JPanel;

public class RollOver extends JComboBox{

	public RollOver(){
		
	init();
	}
	
	static String roll;
	public static void setRollOver(String rollOver) {
		roll = rollOver;
	}
	public static String getRollOver() {
		return roll;
	}
	
	
	public JComboBox init() {

		addItem("No Roll-Over");
		addItem("Vertical Roll-Over");
		addItem("Horizontal Roll-Over");
		setBackground(Color.white);

		addActionListener(new ActionListener() {

			
			@Override
			public void actionPerformed(ActionEvent event) {
				JComboBox source = (JComboBox) event.getSource();
				String optionString = (String) source.getSelectedItem();
				setRollOver(optionString);
				System.out.println(optionString);

			}
		});
		return this;
	}
}
