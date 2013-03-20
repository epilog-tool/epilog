package pt.igc.nmd.epilogue;



import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


import javax.swing.JButton;

import sun.tools.tree.Node;


public class ColorButton extends JButton {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public ColorFrame frame;
	public ComponentsPanel panel;
//	public DrawPolygon dPanel;
	private String nodeID;

	//public ColorButton(ComponentsPanel panel,DrawPolygon dPanel, String nodeID) {
	public ColorButton(ComponentsPanel panel, String nodeID) {
		this.panel = panel;
//		this.dPanel=dPanel;
		this.nodeID = nodeID;
//		frame = new ColorFrame(this,this.dPanel,this.nodeID);
		frame = new ColorFrame(this,this.nodeID);
		this.initialize();

	}

	public void initialize() {

		addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				frame.initialize();
				

			}
		});

	}

}
