package org.epilogtool.gui.menu;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;

import javax.swing.AbstractAction;
import javax.swing.JMenu;

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

public class SBMLMenu {
	public static JMenu getMenu() {
		JMenu menu = new JMenu("SBML");
		menu.setMnemonic(KeyEvent.VK_S);

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
		super("Load model (SBML)");
		putValue(SHORT_DESCRIPTION, "Load SBML model from file");
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
		super("Rename model (SBML)");
		putValue(SHORT_DESCRIPTION, "Rename selected SBML model");
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
		super("Remove model (SBML)");
		putValue(SHORT_DESCRIPTION, "Remove selected SBML model");
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
		super("Save model (SBML)");
		putValue(SHORT_DESCRIPTION, "Save selected SBML model");
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
		super("Replace model (SBML)");
		putValue(SHORT_DESCRIPTION, "Replace selected SBML model");
		// putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_N,
		// FrameActionManager.MASK));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		EpiGUI.getInstance().replaceSBML();
	}
}
