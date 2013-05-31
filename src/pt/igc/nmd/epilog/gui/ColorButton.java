package pt.igc.nmd.epilog.gui;



import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


import javax.swing.JButton;

import org.colomoto.logicalmodel.NodeInfo;



public class ColorButton extends JButton {


	/**
	 * 
	 */
	private static final long serialVersionUID = 8536601706858970133L;
	public ColorFrame frame;
	public ComponentsPanel panel;
	private NodeInfo node;


	public ColorButton(ComponentsPanel panel, NodeInfo node) {
		this.panel = panel;

		this.node = node;
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
