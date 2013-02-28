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
	public  ColorButton cb;
	public Color clr;


	public BtnChangeColor(ColorButton cb,Color c) {
		super();
		this.cb = cb;
		this.clr=c;
		this.setBackground(c);
	}

	
	
	public void initialize(){
			addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					cb.setBackground(clr);
					cb.panel.mapcolor=clr;
					cb.panel.revalidate();
					cb.panel.paint(cb.panel.getGraphics());
					cb.dPanel.paintComponent(cb.dPanel.getGraphics());
					
					
					
				}
			});
			
			
		}
		
		
	}
	
	
	
	
	


