package pt.igc.nmd.epilogue;



import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


import javax.swing.JButton;

import org.colomoto.logicalmodel.NodeInfo;

import sun.tools.tree.Node;


public class ColorButton extends JButton {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public ColorFrame frame;
	public ComponentsPanel panel;
//	public DrawPolygon dPanel;
	private NodeInfo node;

	//public ColorButton(ComponentsPanel panel,DrawPolygon dPanel, String nodeID) {
	public ColorButton(ComponentsPanel panel, NodeInfo node) {
		this.panel = panel;
//		this.dPanel=dPanel;
		this.node = node;
//		frame = new ColorFrame(this,this.dPanel,this.nodeID);
		frame = new ColorFrame(this,this.node);
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
