package pt.igc.nmd.epilogue;





import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;


public class BtnChangeColor extends JButton {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public  ColorButton colorButton;
	public Color color;


	public BtnChangeColor(ColorButton cb,Color c) {
		super();
		this.colorButton = cb;
		this.color=c;
		this.setBackground(c);
	}

	
	
	public void initialize(){
			addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					
					colorButton.setBackground(color);
					colorButton.panel.mapcolor=color;
					colorButton.panel.revalidate();
					colorButton.panel.paint(colorButton.panel.getGraphics());
		
				}
			});
			
			
		}
		
		
	}
	
	
	
	
	


