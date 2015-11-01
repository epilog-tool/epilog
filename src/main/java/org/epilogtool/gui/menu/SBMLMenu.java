package org.epilogtool.gui.menu;

import java.awt.event.ActionEvent;
import java.io.IOException;

import javax.swing.AbstractAction;
import javax.swing.JMenu;

import org.epilogtool.gui.EpiGUI;

public class SBMLMenu {
	public static JMenu getMenu() {
		JMenu menu = new JMenu("SBML");

		menu.add(new LoadSBMLAction());
		menu.add(new RemoveSBMLAction());

		return menu;
	}
}

class LoadSBMLAction extends AbstractAction {
	private static final long serialVersionUID = 8657096239401658327L;

	public LoadSBMLAction() {
		super("Load SBML");
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

class RemoveSBMLAction extends AbstractAction {
	private static final long serialVersionUID = -7400365619490423300L;

	public RemoveSBMLAction() {
		super("Remove SBML");
		putValue(SHORT_DESCRIPTION, "Remove selected SBML model");
		// putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_N,
		// FrameActionManager.MASK));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		EpiGUI.getInstance().removeSBML();
	}

}