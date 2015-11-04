package org.epilogtool.gui.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import org.epilogtool.gui.EpiGUI;

public class SBMLPopupMenu extends JPopupMenu {
	private static final long serialVersionUID = 7968226829964184722L;

	JMenuItem remove;

	public SBMLPopupMenu() {
		JMenuItem load = new JMenuItem("Load SBML");
		load.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					EpiGUI.getInstance().loadSBML();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		this.add(load);

		this.remove = new JMenuItem("Remove SBML");
		remove.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				EpiGUI.getInstance().removeSBML();
			}
		});
		this.add(this.remove);
	}

	public void updateMenuItems(boolean enable) {
		this.remove.setEnabled(enable);
	}
}
