package org.epilogtool.gui.menu;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JMenu;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

import org.epilogtool.gui.EpiGUI;
import org.epilogtool.gui.GUIInfo;

public class WindowMenu {
	public static JMenu getMenu() {
		JMenu menu = new JMenu("Window");
		menu.setMnemonic(KeyEvent.VK_W);

		JMenu closeSubMenu = new JMenu("Close Tabs");
		closeSubMenu.add(new AllTabsAction());

		closeSubMenu.add(new JSeparator());

		closeSubMenu.add(new ActiveTabAction());
		closeSubMenu.add(new OtherTabsAction());

		closeSubMenu.add(new JSeparator());

		closeSubMenu.add(new EpiTabsAction());
		closeSubMenu.add(new OtherEpisAction());
		menu.add(closeSubMenu);
		
		menu.add(new JSeparator());

		menu.addMenuListener(new MenuListener() {
			@Override
			public void menuSelected(MenuEvent e) {
				JMenu menu = (JMenu) e.getSource();
				// Clean old menu entries
				for (int i = menu.getItemCount() - 1; i > 1; i--) {
					menu.remove(i);
				}
				// Insert entries for existing Tabs
				List<String> lTabs = EpiGUI.getInstance().getOpenTabList();
				for (int i = 0; i < lTabs.size(); i++) {
					menu.add(new SelectTabAction(i, lTabs.get(i)));
				}
			}
			
			@Override
			public void menuDeselected(MenuEvent e) {
			}
			
			@Override
			public void menuCanceled(MenuEvent e) {
			}
		});
		
		return menu;
	}
}

class SelectTabAction extends AbstractAction {
	private static final long serialVersionUID = -3868464206077575338L;
	int index;

	public SelectTabAction(int index, String title) {
		super(title);
		putValue(SHORT_DESCRIPTION, title);
		this.index = index;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		EpiGUI.getInstance().epiTabSelect(this.index);
	}
}

class AllTabsAction extends AbstractAction {
	private static final long serialVersionUID = 1184714272494540096L;

	public AllTabsAction() {
		super("All tabs");
		putValue(SHORT_DESCRIPTION, "All tabs");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		EpiGUI.getInstance().epiTabCloseAllTabs();
	}
}

class ActiveTabAction extends AbstractAction {
	private static final long serialVersionUID = 7296937929263564402L;

	public ActiveTabAction() {
		super("Active");
		putValue(SHORT_DESCRIPTION, "Active");
		putValue(ACCELERATOR_KEY,
				KeyStroke.getKeyStroke(KeyEvent.VK_W, GUIInfo.MASK));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		EpiGUI.getInstance().epiTabCloseActiveTab();
	}
}

class OtherTabsAction extends AbstractAction {
	private static final long serialVersionUID = 8709816950791190172L;

	public OtherTabsAction() {
		super("Other than active");
		putValue(SHORT_DESCRIPTION, "Other than active");
		putValue(
				ACCELERATOR_KEY,
				KeyStroke.getKeyStroke(KeyEvent.VK_W, ActionEvent.SHIFT_MASK
						+ ActionEvent.CTRL_MASK));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		EpiGUI.getInstance().epiTabCloseOtherTabs();
	}
}

class EpiTabsAction extends AbstractAction {
	private static final long serialVersionUID = -7168470080632696018L;

	public EpiTabsAction() {
		super("Selected Epithelium tabs");
		putValue(SHORT_DESCRIPTION, "Selected Epithelium");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		EpiGUI.getInstance().epiTabCloseActiveEpi();
	}
}

class OtherEpisAction extends AbstractAction {
	private static final long serialVersionUID = -509170329753215350L;

	public OtherEpisAction() {
		super("Other Epithelia tabs");
		putValue(SHORT_DESCRIPTION, "Other Epithelia tabs");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		EpiGUI.getInstance().epiTabCloseOtherEpis();
	}
}