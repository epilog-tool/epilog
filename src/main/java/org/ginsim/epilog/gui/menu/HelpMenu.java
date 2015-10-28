package org.ginsim.epilog.gui.menu;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JMenu;

import org.ginsim.epilog.common.Web;
import org.ginsim.epilog.gui.EpiGUI;

public class HelpMenu {
	public static JMenu getMenu() {
		JMenu menu = new JMenu("Help");

		menu.add(new DocAction());
		menu.add(new AboutAction());

		return menu;
	}
}

class DocAction extends AbstractAction {
	private static final long serialVersionUID = 1728730440633848251L;

	public DocAction() {
		super("Documentation");
		putValue(SHORT_DESCRIPTION, "Documentation");
		// putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_N,
		// FrameActionManager.MASK));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Web.openURI("http://epilog-tool.org/documentation");
	}
}

class AboutAction extends AbstractAction {
	private static final long serialVersionUID = 1728730440633848251L;

	public AboutAction() {
		super("About");
		putValue(SHORT_DESCRIPTION, "About");
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