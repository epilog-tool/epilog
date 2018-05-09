package org.epilogtool.gui.menu;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.JMenu;

import org.epilogtool.common.Txt;
import org.epilogtool.gui.EpiGUI;

public class EpitheliumMenu {
	public static JMenu getMenu() {
		JMenu menu = new JMenu(Txt.get("s_MENU_EPI"));
		menu.setMnemonic(KeyEvent.VK_E);

		menu.add(new NewEpiAction());
		menu.add(new DeleteEpiAction());
		menu.add(new EditEpiAction());
		menu.add(new CloneEpiAction());

		return menu;
	}
}

class NewEpiAction extends AbstractAction {
	private static final long serialVersionUID = 1728730440633848251L;

	public NewEpiAction() {
		super(Txt.get("s_MENU_EPI_NEW"));
		putValue(SHORT_DESCRIPTION, Txt.get("s_MENU_EPI_NEW"));
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
		super(Txt.get("s_MENU_EPI_DEL"));
		putValue(SHORT_DESCRIPTION, Txt.get("s_MENU_EPI_DEL"));
		// putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_N,
		// FrameActionManager.MASK));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		EpiGUI.getInstance().deleteEpithelium();
	}
}

class EditEpiAction extends AbstractAction {
	private static final long serialVersionUID = 1728730440633848251L;

	public EditEpiAction() {
		super(Txt.get("s_MENU_EPI_EDIT"));
		putValue(SHORT_DESCRIPTION, Txt.get("s_MENU_EPI_EDIT"));
		// putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_N,
		// FrameActionManager.MASK));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			EpiGUI.getInstance().editEpithelium();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}

class CloneEpiAction extends AbstractAction {
	private static final long serialVersionUID = 1728730440633848251L;

	public CloneEpiAction() {
		super(Txt.get("s_MENU_EPI_CLONE"));
		putValue(SHORT_DESCRIPTION, Txt.get("s_MENU_EPI_CLONE"));
		// putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_N,
		// FrameActionManager.MASK));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		EpiGUI.getInstance().cloneEpithelium();
	}
}