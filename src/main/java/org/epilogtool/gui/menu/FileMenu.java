package org.epilogtool.gui.menu;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;

import javax.swing.AbstractAction;
import javax.swing.JMenu;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;

import org.epilogtool.OptionStore;
import org.epilogtool.gui.EpiGUI;
import org.epilogtool.gui.GUIInfo;

public class FileMenu {
	public static JMenu getMenu() {
		JMenu menu = new JMenu("File");
		menu.setMnemonic(KeyEvent.VK_F);

		menu.add(new NewProjAction());
		menu.add(new LoadProjAction());
		menu.add(new RecentMenu());

		menu.add(new JSeparator());

		menu.add(new SaveAction());
		menu.add(new SaveAsAction());

		menu.add(new JSeparator());

		menu.add(new QuitAction());

		return menu;
	}
}

class NewProjAction extends AbstractAction {
	private static final long serialVersionUID = 1728730440633848251L;

	public NewProjAction() {
		super("New Project");
		putValue(SHORT_DESCRIPTION, "New Project");
		putValue(ACCELERATOR_KEY,
				KeyStroke.getKeyStroke(KeyEvent.VK_N, GUIInfo.MASK));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			EpiGUI.getInstance().newProject();
		} catch (IOException e1) {
			EpiGUI.getInstance().userMessageError(
					"You have selected an invalid SBML file!", "New project");
		}
	}
}

class LoadProjAction extends AbstractAction {
	private static final long serialVersionUID = -2274417985854368549L;
	private final String filename;

	public LoadProjAction() {
		super("Load Project");
		putValue(SHORT_DESCRIPTION, "Load Project");
		putValue(ACCELERATOR_KEY,
				KeyStroke.getKeyStroke(KeyEvent.VK_O, GUIInfo.MASK));
		this.filename = null;
	}

	public LoadProjAction(String filename) {
		super(filename);
		this.filename = filename;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			if (filename == null) {
				EpiGUI.getInstance().loadProject();
			} else {
				EpiGUI.getInstance().loadPEPS(this.filename);
			}
		} catch (Exception e1) {
			EpiGUI.getInstance().userMessageError(
					"Invalid project (PEPS) file!", "Load project");
		}
	}
}

class RecentMenu extends JMenu {
	private static final long serialVersionUID = -7991362512417289070L;

	public RecentMenu() {
		super("Recent files");
	}

	@Override
	public void setSelected(boolean b) {
		if (b) {
			// rebuild the recent menu
			removeAll();
			for (String recent : OptionStore.getRecentFiles()) {
				add(new LoadProjAction(recent));
			}
		}
		super.setSelected(b);
	}
}

class SaveAction extends AbstractAction {
	private static final long serialVersionUID = -2274417985854368549L;

	public SaveAction() {
		super("Save");
		putValue(SHORT_DESCRIPTION, "Save");
		putValue(ACCELERATOR_KEY,
				KeyStroke.getKeyStroke(KeyEvent.VK_S, GUIInfo.MASK));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			EpiGUI.getInstance().savePEPS();
		} catch (IOException e1) {
			EpiGUI.getInstance().userMessageError("Could not save PEPS file",
					"Save");
		}
	}
}

class SaveAsAction extends AbstractAction {
	private static final long serialVersionUID = -2274417985854368549L;

	public SaveAsAction() {
		super("Save As");
		putValue(SHORT_DESCRIPTION, "Save As");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			EpiGUI.getInstance().saveAsPEPS();
		} catch (IOException e1) {
			EpiGUI.getInstance().userMessageError("Could not save PEPS file",
					"Save As");
		}
	}
}

class QuitAction extends AbstractAction {
	private static final long serialVersionUID = 1728730440633848251L;

	public QuitAction() {
		super("Quit");
		putValue(SHORT_DESCRIPTION, "Quit");
		putValue(ACCELERATOR_KEY,
				KeyStroke.getKeyStroke(KeyEvent.VK_Q, GUIInfo.MASK));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		EpiGUI.getInstance().quitProject();
	}
}