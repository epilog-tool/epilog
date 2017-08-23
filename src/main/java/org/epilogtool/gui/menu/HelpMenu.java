package org.epilogtool.gui.menu;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.JMenu;

import org.epilogtool.common.Txt;
import org.epilogtool.common.Web;
import org.epilogtool.gui.EpiGUI;

public class HelpMenu {
	public static JMenu getMenu() {
		JMenu menu = new JMenu(Txt.get("s_MENU_HELP"));
		menu.setMnemonic(KeyEvent.VK_H);

		menu.add(new DocAction());
		menu.add(new AboutAction());

		return menu;
	}
}

class DocAction extends AbstractAction {
	private static final long serialVersionUID = 1728730440633848251L;

	public DocAction() {
		super(Txt.get("s_MENU_HELP_DOC"));
		putValue(SHORT_DESCRIPTION, Txt.get("s_MENU_HELP_DOC"));
		// putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_N,
		// FrameActionManager.MASK));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Web.openURI(Txt.get("s_MENU_HELP_DOC_URL"));
	}
}

class AboutAction extends AbstractAction {
	private static final long serialVersionUID = 1728730440633848251L;

	public AboutAction() {
		super(Txt.get("s_MENU_HELP_ABOUT"));
		putValue(SHORT_DESCRIPTION, Txt.get("s_MENU_HELP_ABOUT"));
		// putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_N,
		// FrameActionManager.MASK));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			EpiGUI.getInstance().aboutDialog();
		} catch (Exception e1) {
			// TODO: handle java reflection in the future
			e1.printStackTrace();
		}
	}
}