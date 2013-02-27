package pt.gulbenkian.igc.nmd;



import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


import javax.swing.JButton;


public class ColorButton extends JButton {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public ColorFrame frame;
	public MapColorPanel panel;
	public DrawPolygon dPanel;

	public ColorButton(MapColorPanel panel,DrawPolygon dPanel) {
		this.panel = panel;
		this.dPanel=dPanel;
		frame = new ColorFrame(this,this.dPanel);

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
