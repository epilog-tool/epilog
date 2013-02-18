package pt.gulbenkian.igc.nmd;

import javax.swing.JCheckBox;
import java.awt.Color;
import java.awt.event.ItemEvent;
import java.io.Serializable;
import java.awt.event.ItemListener;

import javax.swing.JTextField;

public class Cell implements Serializable {
	
	

	public int G0;
	public Color color1;
	public MapColorPanel panel;
	

	public Cell(int g0) {
		super();
		this.G0 = g0;
		this.color1 = Color.white;

	}
	
	
	public void itemStateChanged(JCheckBox nodeBox, ItemEvent e)
	{
		nodeBox.addItemListener();
	    cb=(Checkbox)(e.getItemSelectable());
	    l1.setText(cb.getLabel());

	    cb2=(Checkbox)(e.getItemSelectable());
	    l2.setText(cb2.getLabel());
	}   
public void componentReady (MapColorPanel panel){ 
	this.panel = panel;
	
}


}