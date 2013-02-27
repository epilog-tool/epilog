package pt.igc.nmd.epilogue;

import java.awt.Color;
import java.io.Serializable;

import javax.swing.JTextField;

public class Cell implements Serializable {
	
	

	public int G0;
	public Color color1;


	public Cell(int g0) {
		super();
		this.G0 = g0;
		this.color1 = Color.white;

	}



}