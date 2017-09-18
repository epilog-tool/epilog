package org.epilogtool.gui.menu;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;

import javax.swing.AbstractAction;
import javax.swing.JMenu;

import org.epilogtool.common.Txt;
import org.epilogtool.gui.EpiGUI;

/**
 * This class implements the SBML Menu at the toolbar. There are 4 possible actions: 
 * Load (Add an SBML file to the list of SBLMs in the project) 
 * Rename (Rename an existing SBML)
 * Remove (Remove an SBML from the project) - Check dependencies
 * Export (Save an SBML as an .SBML file in a chosen directory)  
 * 
 * Once an option is selected it is the EpiGUI that calls the shots.
 *
 */

public class CellularModelMenu {
	public static JMenu getMenu() {
		JMenu menu = new JMenu(Txt.get("s_MENU_SBML"));
		menu.setMnemonic(KeyEvent.VK_C);

		menu.add(new LoadSBMLAction());
		menu.add(new RenameSBMLAction());
		menu.add(new RemoveSBMLAction());
		menu.add(new ExportSBMLAction());
		menu.add(new ReplaceSBMLAction());

		return menu;
	}
}

class LoadSBMLAction extends AbstractAction {
	private static final long serialVersionUID = 8657096239401658327L;

	public LoadSBMLAction() {
		super(Txt.get("s_MENU_SBML_LOAD"));
		putValue(SHORT_DESCRIPTION, Txt.get("s_MENU_SBML_LOAD_LONG"));
		// putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_N,
		// FrameActionManager.MASK));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			EpiGUI.getInstance().loadSBML();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

}

class RenameSBMLAction extends AbstractAction {
	private static final long serialVersionUID = -7400365619490423300L;

	public RenameSBMLAction() {
		super(Txt.get("s_MENU_SBML_RENAME"));
		putValue(SHORT_DESCRIPTION, Txt.get("s_MENU_SBML_RENAME_LONG"));
		// putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_N,
		// FrameActionManager.MASK));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		EpiGUI.getInstance().renameSBML();
	}

}


class RemoveSBMLAction extends AbstractAction {
	private static final long serialVersionUID = -7400365619490423300L;

	public RemoveSBMLAction() {
		super(Txt.get("s_MENU_SBML_REMOVE"));
		putValue(SHORT_DESCRIPTION, Txt.get("s_MENU_SBML_REMOVE_LONG"));
		// putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_N,
		// FrameActionManager.MASK));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		EpiGUI.getInstance().removeSBML();
	}
}

class ExportSBMLAction extends AbstractAction {
	private static final long serialVersionUID = -7400365619490423300L;

	public ExportSBMLAction() {
		super(Txt.get("s_MENU_SBML_SAVE"));
		putValue(SHORT_DESCRIPTION, Txt.get("s_MENU_SBML_SAVE_LONG"));
		// putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_N,
		// FrameActionManager.MASK));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			EpiGUI.getInstance().exportSBML();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}

class ReplaceSBMLAction extends AbstractAction {
	private static final long serialVersionUID = -7400365619490423300L;

	public ReplaceSBMLAction() {
		super(Txt.get("s_MENU_SBML_REPLACE"));
		putValue(SHORT_DESCRIPTION, Txt.get("s_MENU_SBML_REPLACE_LONG"));
		// putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_N,
		// FrameActionManager.MASK));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		EpiGUI.getInstance().replaceSBML();
	}
}
