package org.epilogtool.gui.menu;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;

import javax.swing.AbstractAction;
import javax.swing.JMenu;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;

import org.epilogtool.OptionStore;
import org.epilogtool.common.Txt;
import org.epilogtool.gui.EpiGUI;
import org.epilogtool.gui.GUIInfo;

public class FileMenu {
	public static JMenu getMenu() {
		JMenu menu = new JMenu(Txt.get("s_MENU_FILE"));
		menu.setMnemonic(KeyEvent.VK_F);

		menu.add(new NewProjAction());
		menu.add(new LoadProjAction());
		menu.add(new RecentMenu());

		menu.add(new JSeparator());

		menu.add(new SaveAction());
		menu.add(new SaveAsAction());

		menu.add(new JSeparator());

		menu.add(new EditPrefsAction());

		menu.add(new JSeparator());

		menu.add(new QuitAction());

		return menu;
	}
}

class NewProjAction extends AbstractAction {
	private static final long serialVersionUID = 1728730440633848251L;

	public NewProjAction() {
		super(Txt.get("s_MENU_NEW_PROJ"));
		putValue(SHORT_DESCRIPTION, Txt.get("s_MENU_NEW_PROJ"));
		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_N, GUIInfo.MASK));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			EpiGUI.getInstance().newProject();
		} catch (IOException e1) {
			EpiGUI.getInstance().userMessageError("You have selected an invalid SBML file!", Txt.get("s_MENU_NEW_PROJ"));
		}
	}
}

class LoadProjAction extends AbstractAction {
	private static final long serialVersionUID = -2274417985854368549L;
	private final String filename;

	public LoadProjAction() {
		super(Txt.get("s_MENU_LOAD_PROJ"));
		putValue(SHORT_DESCRIPTION, Txt.get("s_MENU_LOAD_PROJ"));
		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_O, GUIInfo.MASK));
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
			EpiGUI.getInstance().userMessageError("Invalid project (PEPS) file!", Txt.get("s_MENU_LOAD_PROJ"));
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
		super(Txt.get("s_MENU_SAVE"));
		putValue(SHORT_DESCRIPTION, Txt.get("s_MENU_SAVE"));
		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_S, GUIInfo.MASK));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			EpiGUI.getInstance().savePEPS();
		} catch (IOException e1) {
			EpiGUI.getInstance().userMessageError(Txt.get("s_MENU_CANNOT_SAVE"), Txt.get("s_MENU_SAVE"));
		}
	}
}

class SaveAsAction extends AbstractAction {
	private static final long serialVersionUID = -2274417985854368549L;

	public SaveAsAction() {
		super(Txt.get("s_MENU_SAVE_AS"));
		putValue(SHORT_DESCRIPTION, Txt.get("s_MENU_SAVE_AS"));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			EpiGUI.getInstance().saveAsPEPS();
		} catch (IOException e1) {
			EpiGUI.getInstance().userMessageError(Txt.get("s_MENU_CANNOT_SAVE"), Txt.get("s_MENU_SAVE_AS"));
		}
	}
}

class EditPrefsAction extends AbstractAction {
	private static final long serialVersionUID = -2767709789639816800L;

	public EditPrefsAction() {
		super(Txt.get("s_EDIT_PREFS"));
		putValue(SHORT_DESCRIPTION, Txt.get("s_EDIT_PREFS"));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		EpiGUI.getInstance().editPreferences();
	}
}

class QuitAction extends AbstractAction {
	private static final long serialVersionUID = 1728730440633848251L;

	public QuitAction() {
		super(Txt.get("s_MENU_QUIT"));
		putValue(SHORT_DESCRIPTION, Txt.get("s_MENU_QUIT"));
		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_Q, GUIInfo.MASK));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		EpiGUI.getInstance().quitProject();
	}
}