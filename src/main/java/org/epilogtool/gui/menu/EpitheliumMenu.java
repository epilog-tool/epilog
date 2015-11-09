package org.epilogtool.gui.menu;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JMenu;

import org.epilogtool.gui.EpiGUI;

public class EpitheliumMenu {
	public static JMenu getMenu() {
		JMenu menu = new JMenu("Epithelium");

		menu.add(new NewEpiAction());
		menu.add(new DeleteEpiAction());
		menu.add(new RenameEpiAction());
		menu.add(new CloneEpiAction());

		return menu;
	}
}

class NewEpiAction extends AbstractAction {
	private static final long serialVersionUID = 1728730440633848251L;

	public NewEpiAction() {
		super("New");
		putValue(SHORT_DESCRIPTION, "New");
		// putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_N,
		// FrameActionManager.MASK));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			EpiGUI.getInstance().newEpithelium();
		} catch (Exception e1) {
			// TODO: handle java reflection in the future
			e1.printStackTrace();
		}
	}
}

class DeleteEpiAction extends AbstractAction {
	private static final long serialVersionUID = 1728730440633848251L;

	public DeleteEpiAction() {
		super("Delete");
		putValue(SHORT_DESCRIPTION, "Delete");
		// putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_N,
		// FrameActionManager.MASK));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		EpiGUI.getInstance().deleteEpithelium();
	}
}

class RenameEpiAction extends AbstractAction {
	private static final long serialVersionUID = 1728730440633848251L;

	public RenameEpiAction() {
		super("Rename");
		putValue(SHORT_DESCRIPTION, "Rename");
		// putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_N,
		// FrameActionManager.MASK));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		EpiGUI.getInstance().renameEpithelium();
	}
}

class CloneEpiAction extends AbstractAction {
	private static final long serialVersionUID = 1728730440633848251L;

	public CloneEpiAction() {
		super("Clone");
		putValue(SHORT_DESCRIPTION, "Clone");
		// putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_N,
		// FrameActionManager.MASK));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		EpiGUI.getInstance().cloneEpithelium();
	}
}