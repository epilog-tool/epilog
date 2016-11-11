package org.epilogtool.gui.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import org.epilogtool.gui.EpiGUI;

/**
 * Class that implements the options popup for the SBML. 
 * Instead of going to the "SBML" menu bar the user can select the action here. 
 * There are 4 possible actions: 
 * Load (Add an SBML file to the list of SBLMs in the project) 
 * Rename (Rename an existing SBML)
 * Remove (Remove an SBML from the project) - Check dependencies
 * Export (Save an SBML as an .SBML file in a chosen directory)  
 * 
 * Once an option is selected it is the EpiGUI that calls the shots.
 *
 *
 */
public class SBMLPopupMenu extends JPopupMenu {
	private static final long serialVersionUID = 7968226829964184722L;

	JMenuItem remove;

	public SBMLPopupMenu() {
		
		// LOAD SBML
		JMenuItem load = new JMenuItem("Load model (SBML)");
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

		// RENAME SBML
		JMenuItem rename = new JMenuItem("Rename model (SBML)");
		rename.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				EpiGUI.getInstance().renameSBML();
			}
		});
		this.add(rename);
		
		// REMOVE SBML
		this.remove = new JMenuItem("Remove model (SBML)");
		remove.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				EpiGUI.getInstance().removeSBML();
			}
		});
		this.add(this.remove);
		
		
		// Export SBML
		JMenuItem export = new JMenuItem("Save model (SBML)");
		export.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					EpiGUI.getInstance().exportSBML();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		this.add(export);	
		
		// Replace SBML
		JMenuItem replace = new JMenuItem("Replace model (SBML)");
		replace.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				EpiGUI.getInstance().replaceSBML();
			}
		});
		this.add(replace);	
		
	}

	public void updateMenuItems(boolean enable) {
		this.remove.setEnabled(enable);
	}
}
