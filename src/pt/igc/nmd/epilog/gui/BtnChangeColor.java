package pt.igc.nmd.epilog.gui;





import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import org.colomoto.logicalmodel.NodeInfo;



public class BtnChangeColor extends JButton {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public  ColorButton colorButton;
	public Color color;
	private NodeInfo node;


	public BtnChangeColor(ColorButton cb,Color c, NodeInfo node) {
		super();
		this.colorButton = cb;
		this.color=c;
		this.setBackground(c);
		this.node = node;
	}

	
	
	public void initialize(){
			addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					
					colorButton.setBackground(color);
					colorButton.panel.mapcolor=color;
					colorButton.panel.revalidate();
					colorButton.panel.paint(colorButton.panel.getGraphics());
					colorButton.panel.mainPanel.getEpithelium().setColor(node, color);
					

		
				}
			});
			
			
		}
		
		
	}
	
	
	
	
	


