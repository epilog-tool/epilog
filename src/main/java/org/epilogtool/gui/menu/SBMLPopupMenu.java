package org.epilogtool.gui.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import org.epilogtool.common.Txt;
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

	JMenuItem load;
	JMenuItem rename;
	JMenuItem remove;
	JMenuItem save;
	JMenuItem replace;

	public SBMLPopupMenu() {
		
		// LOAD SBML
		this.load = new JMenuItem(Txt.get("s_MENU_SBML_LOAD"));
		this.load.addActionListener(new ActionListener() {
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
		this.add(this.load);

		// Rename SBML
		this.rename = new JMenuItem(Txt.get("s_MENU_SBML_RENAME"));
		this.rename.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				EpiGUI.getInstance().renameSBML();
			}
		});
		this.add(this.rename);
		
		// Remove SBML
		this.remove = new JMenuItem(Txt.get("s_MENU_SBML_REMOVE"));
		this.remove.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				EpiGUI.getInstance().removeSBML();
			}
		});
		this.add(this.remove);
		
		
		// Save SBML
		this.save = new JMenuItem(Txt.get("s_MENU_SBML_SAVE"));
		this.save.addActionListener(new ActionListener() {
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
		this.add(this.save);	
		
		// Replace SBML
		this.replace = new JMenuItem(Txt.get("s_MENU_SBML_REPLACE"));
		this.replace.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				EpiGUI.getInstance().replaceSBML();
			}
		});
		this.add(this.replace);	
		
	}

	public void updateMenuItems(boolean hasModel) {
		this.load.setEnabled(true);
		this.rename.setEnabled(hasModel);
		this.remove.setEnabled(hasModel);
		this.save.setEnabled(hasModel);
		this.replace.setEnabled(hasModel);
	}
}
